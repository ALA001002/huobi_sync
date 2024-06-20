package com.cyssxt.huobisync.view;

import com.bigo.project.bigo.marketsituation.domain.Bline;
import lombok.Data;

import java.util.List;

@Data
public class HbBline {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 新加坡时间的时间戳，单位毫秒
     */
    private Long ts;
    /**
     * b线数据
     */
    private List<Bline> data;
}
