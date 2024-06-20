package com.cyssxt.huobisync.constant;


import java.util.Calendar;

/**
 * 行情粒度：1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year
 * @author Administrator
 */
public enum CandlestickEnum {
    /**
     * 1分钟
     */
    MIN1("1min", Calendar.MINUTE,1),
    /**
     * 5分钟
     */
    MIN5("5min", Calendar.MINUTE,1),
    /**
     * 15分钟
     */
    MIN15("15min", Calendar.MINUTE,5),
    /**
     * 30分钟
     */
    MIN30("30min", Calendar.MINUTE,15),
    /**
     * 60分钟
     */
    MIN60("60min", Calendar.MINUTE,10),
    /**
     * 4小时
     */
    HOUR4("4hour", Calendar.MINUTE,10),
    /**
     * 1天
     */
    DAY1("1day", Calendar.MINUTE,10),
    /**
     * 1周
     */
    WEEK1("1week", Calendar.MINUTE,10),
    /**
     * 1个月
     */
    MON1("1mon", Calendar.MINUTE,180),
    /**
     * 1年
     */
    //YEAR1("1year"),
    ;

    private final String code;
    private final int type;
    private final int interval;

    CandlestickEnum(String code, int type,int interval) {
        this.code = code;
        this.type = type;
        this.interval = interval;
    }

    public int getType() {
        return type;
    }

    public int getInterval() {
        return interval;
    }

    public String getCode(){
        return code;
    }

}
