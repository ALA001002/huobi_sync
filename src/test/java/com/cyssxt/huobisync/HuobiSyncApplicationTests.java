package com.cyssxt.huobisync;

import com.cyssxt.huobisync.service.GenerateService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;

@SpringBootTest
@ActiveProfiles("dev")
class HuobiSyncApplicationTests {

    @Resource
    GenerateService generateService;

    @Test
    void contextLoads() {
    }

    @Test
    void startGenerate(){
        generateService.startGenerate("bixusdt");
//        generateService.startGenerate(1,"1min");
//        generateService.startGenerate(5,"5min");
//        generateService.startGenerate(15,"15min");
//        generateService.startGenerate(30,"30min");
//        generateService.startGenerate(60,"1h");
//        generateService.startGenerate(240,"4h");
//        generateService.startGenerate(24*60,"1d");
    }

}
