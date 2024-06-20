package com.cyssxt.huobisync.controller.request;

import lombok.Data;

@Data
public class PageReq {
    Integer pageNo;
    Integer pageSize;
    String symbol;
}
