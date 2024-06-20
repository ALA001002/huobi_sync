package com.cyssxt.huobisync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.bigo.project.bigo.marketsituation.domain")
@EnableJpaRepositories("com.cyssxt.huobisync.repository")
public class HuobiSyncApplication {


    public static void main(String[] args) {
        SpringApplication.run(HuobiSyncApplication.class, args);
    }

}
