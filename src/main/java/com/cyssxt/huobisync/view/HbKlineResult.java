package com.cyssxt.huobisync.view;

import com.bigo.project.bigo.marketsituation.domain.Kline;
import lombok.Data;

import java.util.List;


@Data
public class HbKlineResult {
    /**
     * K线主题
     */
    private String ch;
    /**
     * 请求结果
     */
    private String status;
    /**
     * 时间戳
     */
    private Long ts;
    /**
     * K线数据
     */
    private List<Kline> data;
}
