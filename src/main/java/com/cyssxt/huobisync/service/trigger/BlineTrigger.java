package com.cyssxt.huobisync.service.trigger;

import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;

import java.util.Calendar;
import java.util.Date;

public class BlineTrigger implements Trigger {

    int interval;
    int starterInterval;

    public BlineTrigger(int interval,int starter) {
        this.interval = interval;
        this.starterInterval = starter;
    }

    @Override
    public Date nextExecutionTime(TriggerContext triggerContext) {
        Date starter = triggerContext.lastCompletionTime();
        Date result = null;
        if(starter==null){
            result = new Date();
        }else{
            result = starter;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(result);
        int interval = this.interval;
        if(starter==null){
            interval = starterInterval;
        }
        calendar.add(Calendar.SECOND,interval);
        return calendar.getTime();
    }

}
