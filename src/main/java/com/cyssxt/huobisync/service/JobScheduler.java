package com.cyssxt.huobisync.service;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler extends ThreadPoolTaskScheduler {
    @Override
    public void initialize() {
        super.setPoolSize(5);
        super.initialize();
    }
}
