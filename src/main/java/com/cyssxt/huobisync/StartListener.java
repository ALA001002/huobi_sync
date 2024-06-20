package com.cyssxt.huobisync;

import com.cyssxt.huobisync.service.GenerateService;
import com.cyssxt.huobisync.service.SchedulerManager;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StartListener implements ApplicationListener<ContextRefreshedEvent> {

    @Resource
    SchedulerManager schedulerManager;

    @Resource
    GenerateService generateService;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        schedulerManager.startAll();
//        generateService.startGenerate();
    }
}
