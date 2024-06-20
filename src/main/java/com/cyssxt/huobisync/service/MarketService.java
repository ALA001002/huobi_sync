package com.cyssxt.huobisync.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigo.project.bigo.marketsituation.domain.Bline;
import com.bigo.project.bigo.marketsituation.domain.Kline;
import com.bigo.project.bigo.marketsituation.domain.SlipDot;
import com.cyssxt.huobisync.constant.CandlestickEnum;
import com.cyssxt.huobisync.repository.BlineRepository;
import com.cyssxt.huobisync.repository.KlineRepository;
import com.cyssxt.huobisync.util.DateUtils;
import com.cyssxt.huobisync.util.HttpClientUtil;
import com.cyssxt.huobisync.view.HbKlineResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MarketService {
    @Resource
    KlineRepository klineRepository;

    @Resource
    BlineRepository blineRepository;

    @Resource
    RedisCache redisCache;

    @Resource
    SlipDotService slipDotService;

    @Value("${huobi.bline.url}")
    private String blineUrl;

    @Value("${huobi.kline.url}")
    private String klineUrl;

    @Resource
    BgSlipConfigService bgSlipConfigService;

    @Value("${filter.startTime}")
    private Long startTime;

    public Long getStartTime() {
        return Optional.ofNullable(startTime).orElse(0L);
    }

    public String getBlineUrl() {
        return blineUrl;
    }

    public String getKlineUrl() {
        return klineUrl;
    }

    public Long getMaxTradeIdBySymbol(String symbol) {
        Object cache = redisCache.getCacheObject(symbol + "_max_trade_id");
        if (cache == null) {
            return blineRepository.queryMaxTradeId(symbol);
        } else {
            return Long.valueOf(cache.toString());
        }
    }

    public void calContractInfo(String symbol, Bline bline) {
        log.info("calContractInfo ={},{}",symbol + "_price",bline.getPrice());
        redisCache.setCacheObject(symbol + "_price", bline.getPrice());
        redisCache.setCacheObject(symbol + "_max_trade_id", bline.getTradeId());
    }

    public void calKlineLastPoint(String symbol, Bline bline) {
        for (CandlestickEnum period : CandlestickEnum.values()) {
            String klineKey = symbol + period.getCode();
            List<Kline> klineList = redisCache.getCacheObject(klineKey);
            if (CollectionUtils.isEmpty(klineList)) {
                return;
            }
            Kline lastPoint = klineList.get(0);
            lastPoint.setClose(bline.getPrice());
            //如果b线的价格大于最后一个k线的高点，则将K线高点设为b线的价格
            if (bline.getPrice().compareTo(lastPoint.getHigh()) > 0) {
                lastPoint.setHigh(bline.getPrice());
            }
            //如果b线的价格小于于最后一个k线的低点，则将K线低点设为b线的价格
            if (bline.getPrice().compareTo(lastPoint.getLow()) < 0) {
                lastPoint.setLow(bline.getPrice());
            }
            redisCache.setCacheObject(klineKey, klineList);
        }
    }

    public Long getMaxTimestampBySymbolAndPeriod(Kline klineQuery) {
        String symbol = klineQuery.getSymbol();
        String period = klineQuery.getPeriod();
        String key = symbol + "_" + period + "_max_ts";
        Object cache = redisCache.getCacheObject(key);
        if (cache == null) {
            return klineRepository.getMaxTimestampBySymbolAndPeriod(symbol,period);
        } else {
            return Long.valueOf(cache.toString());
        }
    }
    private Date getEndTimeByPeriod(Date startTime, CandlestickEnum period) {
        Date endTime;
        switch (period) {
            case MIN1:
                endTime = DateUtils.addMinutes(startTime, 1);
                break;
            case MIN5:
                endTime = DateUtils.addMinutes(startTime, 5);
                break;
            case MIN15:
                endTime = DateUtils.addMinutes(startTime, 15);
                break;
            case MIN30:
                endTime = DateUtils.addMinutes(startTime, 30);
                break;
            case MIN60:
                endTime = DateUtils.addMinutes(startTime, 60);
                break;
            case HOUR4:
                endTime = DateUtils.addHours(startTime, 4);
                break;
            case DAY1:
                endTime = DateUtils.addDays(startTime, 1);
                break;
            case WEEK1:
                endTime = DateUtils.addDays(startTime, 7);
                break;
            case MON1:
                endTime = DateUtils.addMonths(startTime, 1);
                break;
            default:
                endTime = startTime;
        }
        return endTime;
    }


    public void dealSlipDotBeforeInsert(List<Kline> klineList, CandlestickEnum period) {
        for (Kline kline : klineList) {
            Date startTime = new Date(kline.getTimestamp() * 1000);
            Date endTime = getEndTimeByPeriod(startTime, period);
            List<SlipDot> slipDotList = slipDotService.listSlipDotByTime(startTime, endTime, kline.getSymbol());
            if (CollectionUtils.isEmpty(slipDotList)) {
                continue;
            }
            BigDecimal low = kline.getLow();
            BigDecimal high = kline.getHigh();
            for (SlipDot dot : slipDotList) {
                //如果滑点开始时间早于k线开始时间，则开要加上滑点
                if (dot.getStartDotTime().before(startTime)) {
                    kline.setRealOpen(kline.getOpen());
                    kline.setOpen(kline.getOpen().add(dot.getAdjustPrice()));
                }
                //如果滑点结束时间晚于于k线结束时间，则收要加上滑点
                if (dot.getStopDotTime() == null || dot.getStopDotTime().after(endTime)) {
                    kline.setRealClose(kline.getClose());
                    kline.setClose(kline.getClose().add(dot.getAdjustPrice()));
                }
                List<Bline> blineList = blineRepository.listByTime(startTime.getTime(), endTime.getTime(), kline.getSymbol());
                Boolean upSlip = dot.getAdjustPrice().compareTo(BigDecimal.ZERO) >= 0;
                for (Bline bline : blineList) {
                    if (upSlip) {
                        if (bline.getPrice().compareTo(high) > 0) {
                            high = bline.getPrice();
                        }
                        if (bline.getRealPrice().compareTo(low) < 1) {
                            low = bline.getPrice();
                        }
                    } else {
                        if (bline.getRealPrice().compareTo(high) >= 0) {
                            high = bline.getPrice();
                        }
                        if (bline.getPrice().compareTo(low) < 1) {
                            low = bline.getPrice();
                        }
                    }
                }
            }
            kline.setRealHigh(kline.getHigh());
            kline.setHigh(high);
            kline.setRealLow(kline.getLow());
            kline.setLow(low);
        }
    }

    public String  sync(String symbol,String periodCode){
        String url = getKlineUrl() + symbol + "&period=" + periodCode;
        log.info("SyncKlineTask symbol={},url={}",symbol,url);
        String resultJson = HttpClientUtil.get(url);
        log.info("SyncKlineTask symbol={},url={} end",symbol,url);
        try {
            HbKlineResult result = JSONObject.toJavaObject(JSON.parseObject(resultJson), HbKlineResult.class);
            if (result == null || !"ok".equals(result.getStatus())) {
                log.error("请求K线数据失败，url：{}，返回报文：{}", url, resultJson);
                return "ERROR";
            }
            List<Kline> list = result.getData();
            if (list.size() == 0) {
                return "ERROR";
            }
//            BgSlipConfig bgSlipConfig = bgSlipConfigService.queryConfig(symbol);
            for (Kline kline : list) {
                kline.setTimestamp(kline.getId());
                kline.setSymbol(symbol);
                kline.setPeriod(periodCode);
//                if(symbol.equals("btcusdt")) {
//                    System.out.println(111);
//                }
                bgSlipConfigService.calcPrice(kline, symbol);
            }
            Kline klineQuery = new Kline();
            klineQuery.setSymbol(symbol);
            klineQuery.setPeriod(periodCode);
            Long lastKid =  klineRepository.getMaxTimestampBySymbolAndPeriod(symbol,periodCode);
            //记录今天的交易数据，最高价，最低价，交易量
            if (periodCode.equals(CandlestickEnum.DAY1.getCode())) {
                //天粒度，第一条数据就是今天的数据
                Kline todayKline = list.get(0);
                redisCache.setCacheObject(symbol + "_today_kline", todayKline);
            }
            //已经写入数据库的数据过滤掉
            if (lastKid != null) {
            /*    if(symbol.equals(SymbolEnum.BIXBTC.getCode()) || symbol.equals(SymbolEnum.BIXETH.getCode()) || symbol.equals(SymbolEnum.BIXUSDT.getCode())) {
                    list = list.stream().filter(a -> a.getTimestamp() > lastKid && a.getTimestamp() >= marketService.getStartTime()).collect(Collectors.toList());
                } else {
                }*/
                list = list.stream().filter(a -> a.getTimestamp() > lastKid).collect(Collectors.toList());
            }
            log.info("kline-save sync={}",list.size());
            if (list.size() > 1) {
                //最新的一条数据不入库，因为收还没确定
                Kline lastKline = list.get(0);
                list.remove(0);
                //火币返回的数据是根据时间戳倒序排列的，存入数据库需要重新排序
                list.sort((a, b) -> (int) (a.getTimestamp() - b.getTimestamp()));
//                marketService.dealSlipDotBeforeInsert(list, period);
                klineRepository.saveAll(list);
                log.info("kline-save sync={}",list.size());
                list = klineRepository.queryTopSize(symbol,periodCode,30);
                list.add(0, lastKline);
                redisCache.deleteObject(symbol + periodCode);
                redisCache.setCacheObject(symbol + periodCode, list);
                redisCache.setCacheObject(symbol + "_" + periodCode + "_max_ts", list.get(1).getTimestamp());
            }
        }catch (Exception e){
            log.error("e={}",e);
        }
        return "SUCCESS";
    }
}
