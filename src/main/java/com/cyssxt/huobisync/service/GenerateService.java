package com.cyssxt.huobisync.service;

import com.bigo.project.bigo.marketsituation.domain.Kline;
import com.bigo.project.bigo.marketsituation.domain.RandomConfig;
import com.cyssxt.huobisync.repository.KlineRepository;
import com.cyssxt.huobisync.util.DateUtils;
import com.cyssxt.huobisync.util.RandomUtil;
import com.cyssxt.huobisync.view.RandomView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class GenerateService {

    ExecutorService executorService = Executors.newFixedThreadPool(1);

    @Resource
    RandomUtil randomUtil;

    @Resource
    KlineRepository klineRepository;

    @Resource
    RandomService randomService;

    @Resource
    RedisCache redisCache;

    public Kline create(int period, String alias, String symbol, long prev, long next,BigDecimal start, RandomConfig randomConfig){
        Kline pre = klineRepository.findFirstByTimestampAndPeriodAndSymbol(prev,alias,symbol);
        BigDecimal open = start;
        if(pre!=null){
            open = pre.getClose();
        }
        long timestamp = next;
        RandomView randomView = randomUtil.random(open,timestamp,period,randomConfig);
        Kline kline = new Kline();
        kline.setOpen(open);
        kline.setMinuteNo(DateUtils.parseDateToMinuteStr(next));
        kline.setTimestamp(timestamp);
        kline.setSymbol(symbol);
        kline.setPeriod(alias);
        kline.setAmount(BigDecimal.valueOf(new Random().nextInt(1000)));
        kline.setCount(Long.valueOf(new Random().nextInt(1000)));
        BigDecimal close = randomView.getClose();
        kline.setClose(close);
        kline.setHigh(randomView.getHigh());
        kline.setLow(randomView.getLow());
        kline.setVol(new BigDecimal(new Random().nextDouble()*100000));
        klineRepository.save(kline);
        updateBlineData(symbol,kline,timestamp);
        if (!StringUtils.isEmpty(randomConfig.getSymbolList())) {
            String[] symbolList = randomConfig.getSymbolList().split(",");
            String[] rateList = randomConfig.getSymbolRateList().split(",");
            for(int i=0;i<symbolList.length;i++) {
                String tempSymbol = symbolList[i];
                String tmp = tempSymbol.substring(tempSymbol.length()-3);
                BigDecimal rate = redisCache.getCacheObject(tmp+"usdt_price");
                rate=rate==null?new BigDecimal(rateList[i]):rate;
                log.info("rate={},tempSymbol={}",rate,tempSymbol);
                create(tempSymbol,kline,rate);
            }
        }
        return kline;
    }

    private BigDecimal divide(BigDecimal origin,BigDecimal rate){
        return origin.divide(rate,9,BigDecimal.ROUND_HALF_UP);
    }

    private void create(String tempSymbol, Kline parent, BigDecimal rate) {
        Kline kline = new Kline();
        kline.setOpen(divide(parent.getOpen(),rate));
        kline.setMinuteNo(parent.getMinuteNo());
        kline.setTimestamp(parent.getTimestamp());
        kline.setSymbol(tempSymbol);
        kline.setPeriod(parent.getPeriod());
        kline.setAmount(BigDecimal.valueOf(new Random().nextInt(1000)));
        kline.setCount(Long.valueOf(new Random().nextInt(1000)));
        BigDecimal close = divide(parent.getClose(),rate);//parent.getClose().divide(rate);
        kline.setClose(close);
        kline.setHigh(divide(parent.getHigh(),rate));
        kline.setLow(divide(parent.getLow(),rate));
        kline.setVol(new BigDecimal(new Random().nextDouble()*100000));
        klineRepository.save(kline);
        updateRedis(tempSymbol,kline.getPeriod());
        updateBlineData(tempSymbol,kline, kline.getTimestamp());
    }

    public Calendar nextMinute(Calendar prev,int period){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(prev.getTimeInMillis());
        calendar.add(Calendar.MINUTE,period);
        return calendar;
    }

    public void startGenerate(){
        RandomConfig randomConfig = randomService.first();
        startGenerate(randomConfig.getSymbol());
    }

    public void startGenerate(String symbol){
        int[] periods = new int[]{1,5,15,30,60,240,1440};
        String[] aliases = new String[]{"1min","5min","15min","30min","60min","4hour","1day"};
        executorService.submit((Runnable) () -> {
            Calendar prev = getStart(symbol);
            Calendar next;
            while (true){
                next = nextMinute(prev,1);
                log.info("start={},next={}",prev,next.getTimeInMillis()/1000);
                while (next.getTimeInMillis()/1000>System.currentTimeMillis()/1000){
                    log.info("pause");
                    try {
                        Thread.sleep(30*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                RandomConfig randomConfig = randomService.get("1min",symbol);
                for(int i=0;i<periods.length;i++) {
                    int period = periods[i];
                    String alias = aliases[i];
                    int hours = next.get(Calendar.HOUR_OF_DAY);
                    int minutes = next.get(Calendar.MINUTE);
                    if((hours*60+minutes)%period==0) {
                        log.info("create={}",alias);
                        try {
                            create(period, alias, symbol, (next.getTimeInMillis() / 1000) - period * 60, next.getTimeInMillis() / 1000, randomConfig.getStart(), randomConfig);
                            updateRedis(symbol,alias);
                        }catch (Exception e){
                            log.error("create error={}",e);
                        }
                    }
                }
                prev = next;
                log.info("start={},current={}",prev,next.getTimeInMillis()/1000);
            }
        });
    }

    public void updateRedis(String symbol,String alias){
        List<Kline> list = klineRepository.queryTopSize(symbol,alias,100);
        redisCache.deleteObject(symbol + alias);
        redisCache.setCacheObject(symbol + alias, list);
        if(list.size()>1) {
            redisCache.setCacheObject(symbol + "_" + alias + "_max_ts", list.get(1).getTimestamp());
        }
    }

    public Calendar getStart(String symbol){
        Long timestamp = klineRepository.findMaxTimestamp("1min",symbol);
        Calendar calendar = Calendar.getInstance();
        if(timestamp!=null){
            calendar.setTimeInMillis(timestamp*1000+60*1000);
        }else {
            calendar.set(Calendar.YEAR, 2021);
            calendar.set(Calendar.MONDAY, 3);
            calendar.set(Calendar.DATE, 15);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }
        return calendar;
    }

    public void updateBlineData(String symbol,Kline kline,long timestamp){
        redisCache.setCacheObject(symbol + "_price", kline.getClose());
        if(timestamp % 10000==0) {
            redisCache.setCacheObject(symbol + "_today_kline", kline);
        }
    }

    public static void main(String[] args) {
        String type = null;
        String minuteNo = "123456";
        String endNo = minuteNo.substring(minuteNo.length()-1);
        String prefix = minuteNo.substring(0,minuteNo.length()-1);
        System.out.println(endNo);
        System.out.println(prefix);
//        System.out.println(type.toString());
//        System.out.println(System.currentTimeMillis());
//        new GenerateService().startGenerate();
    }

}
