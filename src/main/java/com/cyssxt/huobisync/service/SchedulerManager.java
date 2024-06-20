package com.cyssxt.huobisync.service;

import com.cyssxt.huobisync.constant.CandlestickEnum;
import com.cyssxt.huobisync.constant.SymbolEnum;
import com.cyssxt.huobisync.repository.BlineRepository;
import com.cyssxt.huobisync.repository.KlineRepository;
import com.cyssxt.huobisync.service.task.SyncBlineTask;
import com.cyssxt.huobisync.service.task.SyncKlineTask;
import com.cyssxt.huobisync.service.trigger.BlineTrigger;
import com.cyssxt.huobisync.service.trigger.KlineTrigger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Component
@Slf4j
public class SchedulerManager {

    @Resource
    BlineRepository blineRepository;
    @Resource
    MarketService marketService;
    @Resource
    KlineRepository klineRepository;

    @Resource
    RedisCache redisCache;

    @Resource
    JobScheduler jobScheduler;

    @Resource
    BgSlipConfigService bgSlipConfigService;

    @Value("${sync.kline.time}")
    int klineInterval;
    @Value("${sync.bline.time}")
    int blineInterval;

    private final static Map<String,ScheduledFuture> BLINE_MAP = new ConcurrentHashMap<>();
    private final static Map<String,ScheduledFuture> KLINE_MAP = new ConcurrentHashMap<>();
    String getKey(String symbolCode,CandlestickEnum candlestickEnum){
        return String.format("%s_%s",symbolCode,candlestickEnum.getCode());
    }

    void startKLineTask(String symbolCode, CandlestickEnum candlestickEnum,int starter){
        log.info("startKLineTask symbol={},candlest={}",symbolCode,candlestickEnum);
        SyncKlineTask syncKlineTask = new SyncKlineTask(symbolCode,candlestickEnum,marketService,redisCache,klineRepository,bgSlipConfigService);
        KlineTrigger klineTrigger = new KlineTrigger(klineInterval,candlestickEnum,starter);
        ScheduledFuture refreshFuture = jobScheduler.schedule(syncKlineTask,klineTrigger);
        KLINE_MAP.put(getKey(symbolCode,candlestickEnum),refreshFuture);
    }

    void startBlineTask(String symbolCode,int random){
        log.info("startBlineTask symbol={}",symbolCode);
        SyncBlineTask syncBlineTask = new SyncBlineTask(symbolCode,blineRepository,redisCache,marketService,bgSlipConfigService);
        BlineTrigger trigger = new BlineTrigger(blineInterval,random);
        ScheduledFuture refreshFuture = jobScheduler.schedule(syncBlineTask,trigger);
        BLINE_MAP.put(symbolCode,refreshFuture);
    }

    public void startBlineTask(){
        SymbolEnum[] values = SymbolEnum.values();
        int i = 0;
        for(SymbolEnum symbol:values){
            i++;
            String symbolCode = symbol.getCode();
            startBlineTask(symbolCode,i);
        }
    }

    public void startKlineTask(){
        int i = 0 ;
        for(CandlestickEnum candlestickEnum:CandlestickEnum.values()) {
            for (SymbolEnum symbol : SymbolEnum.values()) {
                String symbolCode = symbol.getCode();
                startKLineTask(symbolCode,candlestickEnum,i);
                i++;
            }
        }
    }

    public void startAll() {
        startKlineTask();
        startBlineTask();
    }
}
