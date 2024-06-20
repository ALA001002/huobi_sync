package com.cyssxt.huobisync.service;

import com.bigo.project.bigo.marketsituation.domain.RandomConfig;
import com.cyssxt.huobisync.repository.RandomConfigRepository;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

@Service
public class RandomService {

    @Resource
    RandomConfigRepository randomConfigRepository;

    public RandomConfig get(String period,String symbol){
        RandomConfig randomConfig = randomConfigRepository.findFirstByPeriodAndSymbol(period,symbol);
        if(randomConfig==null){
            randomConfig = new RandomConfig();
            randomConfig.setRandomRate(1440);
            randomConfig.setDecline(0.2);
            randomConfig.setPeriod(period);
            randomConfig.setSymbol(symbol);
            randomConfig.setLowDeg(1.5);
            randomConfig.setLeadWire(1.5);
            randomConfigRepository.save(randomConfig);
        }
        return randomConfig;
    }

    public RandomConfig save(RandomConfig randomConfig){
        randomConfigRepository.save(randomConfig);
        return randomConfig;
    }

    public RandomConfig info(String symbol){
        return randomConfigRepository.findFirstBySymbol(symbol);
    }

    public RandomConfig first(){
        List<RandomConfig> randomConfigList = randomConfigRepository.findAll();
        return randomConfigList.get(0);
    }
}
