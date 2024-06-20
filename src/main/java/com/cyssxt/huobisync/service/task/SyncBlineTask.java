package com.cyssxt.huobisync.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bigo.project.bigo.marketsituation.domain.Bline;
import com.cyssxt.huobisync.repository.BlineRepository;
import com.cyssxt.huobisync.service.BgSlipConfigService;
import com.cyssxt.huobisync.service.MarketService;
import com.cyssxt.huobisync.service.RedisCache;
import com.cyssxt.huobisync.util.HttpClientUtil;
import com.cyssxt.huobisync.view.HbBline;
import com.cyssxt.huobisync.view.HbBlineResult;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class SyncBlineTask implements Runnable{

    String symbolCode;
    BlineRepository blineRepository;
    RedisCache redisCache;
    MarketService marketService;
    BigDecimal startTimePrice;
    BgSlipConfigService bgSlipConfigService;
//    Date startTime = new Date(2021,3,2,10,10,10);
//    Date endTime = new Date(2021,3,2,10,30,10);
//    BigDecimal addValue = BigDecimal.valueOf(20);

    /*private static final String klineUrl = "https://api.huobi.de.com/market/history/kline?size=100&symbol=";
    private static final String blineUrl = "https://api.huobi.de.com/market/history/trade?size=100&symbol=";*/

    // 国外网络访问
//    private static final String blineUrl = "https://api.huobi.pro/market/history/trade?size=100&symbol=";
//     final static String blineUrl;// = "https://api.huobi.de.com/market/history/trade?size=100&symbol=";

    private String url;
    public SyncBlineTask(String symbolCode, BlineRepository blineRepository, RedisCache redisCache, MarketService marketService, BgSlipConfigService bgSlipConfigService) {
        this.symbolCode = symbolCode;
        this.blineRepository = blineRepository;
        this.redisCache = redisCache;
        this.marketService = marketService;
        this.url = marketService.getBlineUrl() + symbolCode;
        this.bgSlipConfigService = bgSlipConfigService;
    }

    @Override
    public void run() {
        try {
            log.info("SyncBlineTask symbol={},url={}",symbolCode,url);
            String resultJson = HttpClientUtil.get(url);
            log.info("SyncBlineTask symbol={},url={} end",symbolCode,url);
            HbBlineResult result = JSONObject.toJavaObject(JSON.parseObject(resultJson), HbBlineResult.class);
            if (result == null || !"ok".equals(result.getStatus())) {
                log.error("请求B线数据失败，url：{}，返回报文：{}", url, resultJson);
                return;
            }
            List<HbBline> list = result.getData();
            List<Bline> blineList = new ArrayList<>();
            if (list.size() == 0) {
                return;
            }
            //如果有滑点，要加上滑点
//            startTimePrice = redisCache.getCacheObject(symbolCode + "_slipdot");
            for (HbBline hbBline : list) {
                for (Bline bline : hbBline.getData()) {
                    bline.setBid(bline.getId());
                    bline.setSymbol(symbolCode);
//                    BigDecimal price = bline.getPrice();
//                    BigDecimal realPrice = price.add(BigDecimal.ZERO);
//                    bline.setRealPrice(price);
//                    bline.setPrice(realPrice);
                    bgSlipConfigService.calcPrice(bline,symbolCode);
                    blineList.add(bline);
                }
            }
            //写入redis
            redisCache.deleteObject(symbolCode);
            redisCache.setCacheObject(symbolCode, blineList);
            Long lastTradeId = marketService.getMaxTradeIdBySymbol(symbolCode);
            if (lastTradeId != null) {
                /*if(symbolCode.equals(SymbolEnum.BIXBTC.getCode()) || symbolCode.equals(SymbolEnum.BIXETH.getCode()) || symbolCode.equals(SymbolEnum.BIXUSDT.getCode())) {
                    blineList = blineList.stream().filter(a -> a.getTradeId() > lastTradeId && a.getTs() >= marketService.getStartTime() * 1000).collect(Collectors.toList());
                } else {
                }*/
                    blineList = blineList.stream().filter(a -> a.getTradeId() > lastTradeId).collect(Collectors.toList());
            }
            if (blineList.size() > 0) {
                blineList.sort((a, b) -> (int) (a.getTradeId() - b.getTradeId()));
                Bline bline = blineList.get(blineList.size() - 1);
//                if (slipPrice != null) {
//                    //有滑点的B线数据才插入数据库
//                    blineRepository.saveAll(blineList);
//                    log.info("bline-save={}",list.size());
//                }
                //计算合约信息
                marketService.calContractInfo(symbolCode, bline);
                //重新计算K线最后一个点
                marketService.calKlineLastPoint(symbolCode, bline);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("请求B线数据失败，url：{}，错误信息：{}", url, ex.getMessage(), ex);
        }
    }

    public static void main(String[] args) {
        System.out.println(1595052735230L/1000==1595052735729L/1000);
    }
}
