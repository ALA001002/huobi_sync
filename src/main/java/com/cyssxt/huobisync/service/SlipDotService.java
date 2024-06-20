package com.cyssxt.huobisync.service;

import com.bigo.project.bigo.marketsituation.domain.SlipDot;
import com.cyssxt.huobisync.repository.SlipDotRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class SlipDotService {
    @Resource
    SlipDotRepository slipDotRepository;

    public List<SlipDot> listSlipDotByTime(Date startTime, Date endTime, String symbol) {
        return slipDotRepository.listSlipDotByDate(symbol,startTime,endTime);
    }
}
