package com.cyssxt.huobisync.service.trigger;

import com.cyssxt.huobisync.constant.CandlestickEnum;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Date;

public class KlineTrigger implements Trigger {

    CandlestickEnum candlestickEnum;
    int interval;
    int startInterval = 0;

    public KlineTrigger(int interval, CandlestickEnum candlestickEnum, int starter) {
        this.candlestickEnum = candlestickEnum;
        this.interval = interval;
        this.startInterval = starter;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date starter = triggerContext.lastCompletionTime();
        Date result;
        if(starter==null){
            result = new Date();
        }else{
            result = starter;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        int interval = candlestickEnum.getInterval();
        int type = candlestickEnum.getType();
        if(starter==null){
            interval = this.startInterval;
            type = Calendar.SECOND;
        }
        calendar.add(type,interval);
        if(calendar.getTime().getTime()<System.currentTimeMillis()){
            calendar.setTime(new Date());
            calendar.add(Calendar.SECOND,interval);
        }
        return calendar.getTime();
    }

}
