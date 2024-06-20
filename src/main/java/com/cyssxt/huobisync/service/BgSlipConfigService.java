package com.cyssxt.huobisync.service;

import com.alibaba.fastjson.JSON;
import com.bigo.project.bigo.marketsituation.domain.BgSlipConfig;
import com.bigo.project.bigo.marketsituation.domain.Bline;
import com.bigo.project.bigo.marketsituation.domain.Kline;
import com.cyssxt.huobisync.controller.request.PageReq;
import com.cyssxt.huobisync.controller.result.AjaxResult;
import com.cyssxt.huobisync.repository.BgSlipConfigRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.action.internal.CollectionUpdateAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

@Service
@Slf4j
public class BgSlipConfigService {

    @Resource
    BgSlipConfigRepository bgSlipConfigRepository;

    public BgSlipConfig queryConfig(String symbol,long currentTime){
        List<BgSlipConfig> configs = bgSlipConfigRepository.findValidConfig(symbol,currentTime);
        return !CollectionUtils.isEmpty(configs)?configs.get(0):null;
    }

    public BigDecimal querySumAddAmount(String symbol,long currentTime){
        return bgSlipConfigRepository.querySumAddMount(symbol,currentTime);
    }

    public void calcPrice(Bline bline,String symbol){
        BigDecimal amount = bline.getAmount();
        long timestamp = bline.getTs();
        BigDecimal historyAddAmount = Optional.ofNullable(querySumAddAmount(symbol,timestamp)).orElse(BigDecimal.ZERO);
        log.info("bline calcPrice={} symbol={},timestamp={}",historyAddAmount,symbol,timestamp);
        BgSlipConfig config = queryConfig(symbol,timestamp);
        if (config != null) {
            Long startTime = config.getStartTime();
            Long endTime = config.getEndTime();
            if (startTime != null && endTime != null && timestamp > startTime && timestamp < endTime && config.getAddValue() != null && config.getAddValue().compareTo(BigDecimal.ZERO)!=0) {
                BigDecimal price = bline.getPrice();
                BigDecimal addValue = getFormatValue(timestamp,startTime,endTime,config,price);
                addValue = historyAddAmount.add(addValue);
                calcBlinePrice(bline,addValue);
            }
        }else {
            if(historyAddAmount.compareTo(BigDecimal.ZERO)!=0){
                calcBlinePrice(bline,historyAddAmount);
            }
        }
    }
    void calcBlinePrice(Bline bline,BigDecimal addValue){
        BigDecimal price = bline.getPrice();
        bline.setRealPrice(price);
        bline.setPrice(price.add(addValue));
        double addAmountValue = 0;
//        if(addAmount!=null && addAmount.compareTo(BigDecimal.ZERO)!=0){
//            addAmountValue = (timestamp-startTime)*addAmount.doubleValue()/(endTime-startTime);
//            bline.setRealAmount(amount);
//            bline.setAmount(amount.add(BigDecimal.valueOf(addAmountValue)));
//        }
        log.debug("calcPrice bline={},addValue={},addAmountValue={}",bline,addValue,addAmountValue);
    }

    BigDecimal getFormatValue(long timestamp,long startTime,long endTime,BgSlipConfig config,BigDecimal open){
        double addValueDouble = (timestamp-startTime)*config.getAddValue().doubleValue()/(endTime-startTime);
        String[] opens = open.toString().split(".");
        int length = 2;
        if(opens.length>1){
            length = opens[1].length();
        }
        String addValue = String.format("%."+length+"f", addValueDouble);
        return new BigDecimal(addValue);
    }
    public void calcPrice(Kline kline,String symbol){
        long timestamp = kline.getTimestamp()*1000;
        BigDecimal historyAddAmount = Optional.ofNullable(querySumAddAmount(symbol,timestamp)).orElse(BigDecimal.ZERO);
        log.info("kline calcPrice={} symbol={},timestamp={}",historyAddAmount,symbol,timestamp);
        BgSlipConfig config = queryConfig(symbol,timestamp);
        if (config != null) {
            Long startTime = config.getStartTime();
            Long endTime = config.getEndTime();
            log.info("startTime={},timstamp={},endTime={}",startTime,timestamp,endTime);
            if((startTime!=null && endTime!=null && timestamp>startTime && timestamp<endTime && config.getAddValue()!=null && config.getAddValue().compareTo(BigDecimal.ZERO)!=0)){
                BigDecimal open = kline.getOpen();
                BigDecimal addValue = getFormatValue(timestamp,startTime,endTime,config,open);
                addValue = historyAddAmount.add(addValue);
                calcValue(kline,addValue);
            }
        }else{
            if(historyAddAmount.compareTo(BigDecimal.ZERO)!=0){
                calcValue(kline,historyAddAmount);
            }
        }
    }

    public void calcValue(Kline kline,BigDecimal addValue){
        BigDecimal open = kline.getOpen();
        BigDecimal close = kline.getClose();
        BigDecimal low = kline.getLow();
        BigDecimal high = kline.getHigh();
        kline.setRealOpen(open);
        kline.setRealClose(close);
        kline.setRealHigh(high);
        kline.setRealLow(low);
        kline.setOpen(open.add(addValue));
        kline.setClose(close.add(addValue));
        kline.setLow(low.add(addValue));
        kline.setHigh(high.add(addValue));
        log.info("set kline slip slip_config={},addValue={}",kline,addValue);
    }

    public AjaxResult save(BgSlipConfig config) {
        log.info("config={}", JSON.toJSONString(config));
        if(config.getSymbol()==null){
            return AjaxResult.error("交易对不能为空");
        }
        if(config.getAddValue()==null){
            return AjaxResult.error("增额不能为空");
        }
        if(config.getStartTime()==null){
            return AjaxResult.error("开始时间不能为空");
        }
        if(config.getIntervalTime()==null){
            return AjaxResult.error("时间间隔（分钟）不能为空");
        }
        long startTime = config.getStartTime();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(config.getStartTime());
        calendar.add(Calendar.MINUTE,config.getIntervalTime());
        config.setDelFlag(false);
        Timestamp now = new Timestamp(System.currentTimeMillis());
        long endTime = calendar.getTimeInMillis();
        config.setEndTime(endTime);
        String symbol = config.getSymbol();
        long id = Optional.ofNullable(config.getId()).orElse(-1l);
        long count = bgSlipConfigRepository.queryConflicts(startTime,endTime,symbol,id);
        if(count>0){
            return AjaxResult.error("时间段重复");
        }
        config.setCreateTime(now);
        config.setUpdateTime(now);
        config.setOpenFlag(Optional.ofNullable(config.getOpenFlag()).orElse(true));
        bgSlipConfigRepository.save(config);
        return AjaxResult.success();
    }

    public AjaxResult updateStatus(BgSlipConfig config) {
        Long id = config.getId();
        if(id==null){
            return AjaxResult.error("请传入对应id");
        }
        Optional<BgSlipConfig> optional = bgSlipConfigRepository.findById(id);
        if(!optional.isPresent()){
            return AjaxResult.error("对应配置不存在");
        }
        BgSlipConfig temp = optional.get();
        boolean openFlag = Optional.ofNullable(config.getOpenFlag()).orElse(false);
        temp.setOpenFlag(openFlag);
        temp.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        log.debug("updateStatus={}",temp);
        bgSlipConfigRepository.save(temp);
        return AjaxResult.success();
    }

    public AjaxResult page(PageReq req) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");
        int pageNo = Optional.ofNullable(req.getPageNo()).orElse(1);
        int pageSize = Optional.ofNullable(req.getPageSize()).orElse(10);
        Pageable pageable = PageRequest.of(pageNo-1, pageSize, sort);
        Page<BgSlipConfig> page = bgSlipConfigRepository.findAll(new Specification<BgSlipConfig>() {
            @Override
            public Predicate toPredicate(Root<BgSlipConfig> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(!StringUtils.isEmpty(req.getSymbol())){
                    predicateList.add(criteriaBuilder.like(root.get("symbol"),"%"+req.getSymbol()+"%"));
                }
                predicateList.add(criteriaBuilder.equal(root.get("delFlag"),false));
                if(!CollectionUtils.isEmpty(predicateList)){
                    criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
                }
                return criteriaQuery.getRestriction();
            }
        },pageable);
        return AjaxResult.success(page);
    }

    public AjaxResult getId(Long id) {
        if(id==null){
            return AjaxResult.error("请传入对应id");
        }
        Optional<BgSlipConfig> optional = bgSlipConfigRepository.findById(id);
        if(!optional.isPresent()){
            return AjaxResult.error("对应配置不存在");
        }
        BgSlipConfig temp = optional.get();
        return AjaxResult.success(temp);
    }
}
