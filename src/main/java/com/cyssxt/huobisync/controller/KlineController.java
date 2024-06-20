package com.cyssxt.huobisync.controller;

import com.bigo.project.bigo.marketsituation.domain.Kline;
import com.bigo.project.bigo.marketsituation.domain.RandomConfig;
import com.cyssxt.huobisync.repository.KlineRepository;
import com.cyssxt.huobisync.service.MarketService;
import com.cyssxt.huobisync.service.RandomService;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping(value="/kline")
public class KlineController {

    @Resource
    KlineRepository klineRepository;

    @Resource
    RandomService randomService;

    @Resource
    MarketService marketService;

    @RequestMapping(value="query")
    public List<Kline> queryKline(String symbol,String period){
        return klineRepository.queryBySymbolAndPeriod(symbol,period, Sort.by(Sort.Order.asc("timestamp")));
    }

    @RequestMapping(value="sync/{symbol}/{period}")
    public String sync(@PathVariable("symbol") String symbol,@PathVariable("period") String period) {
        return marketService.sync(symbol, period);
    }

    @RequestMapping(value="save")
    public RandomConfig save(@RequestBody RandomConfig randomConfig){
        return randomService.save(randomConfig);
    }
    @RequestMapping(value="info/{symbol}")
    public RandomConfig info(@PathVariable String symbol){
        return randomService.info(symbol);
    }
}
