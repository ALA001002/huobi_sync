package com.cyssxt.huobisync.view;

import lombok.Data;

import java.util.List;

@Data
public class HbBlineResult {
    /**
     * K线主题
     */
    private String ch;
    /**
     * 请求结果
     */
    private String status;
    /**
     * 请求时间戳
     */
    private Long ts;
    /**
     * K线数据
     */
    private List<HbBline> data;
}
