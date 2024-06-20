package com.cyssxt.huobisync.controller;

import com.bigo.project.bigo.marketsituation.domain.BgSlipConfig;
import com.cyssxt.huobisync.controller.request.PageReq;
import com.cyssxt.huobisync.controller.result.AjaxResult;
import com.cyssxt.huobisync.service.BgSlipConfigService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/bgSlip/")
public class BgSlipConfigController {
    @Resource
    BgSlipConfigService bgSlipConfigService;

    @RequestMapping(value="save")
    public AjaxResult save(@RequestBody BgSlipConfig config){
        return bgSlipConfigService.save(config);
    }

    @RequestMapping(value="updateStatus")
    public AjaxResult updateStatus(@RequestBody BgSlipConfig config){
        return bgSlipConfigService.updateStatus(config);
    }

    @RequestMapping(value="page")
    public AjaxResult list(@RequestBody PageReq req){
        return bgSlipConfigService.page(req);
    }

    @RequestMapping(value="getConfig/{id}")
    public AjaxResult getConfig(@PathVariable("id") Long id){
        return bgSlipConfigService.getId(id);
    }
}
