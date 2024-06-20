package com.cyssxt.huobisync.constant;

public enum SymbolEnum {
    BTCUSDT("btcusdt","BTC/USDT", 1),
    /**
     * 以太坊/USDT
     */
    ETHUSDT("ethusdt","ETH/USDT",1),

    /**
     * HT/USDT
     */
    HTUSDT("htusdt","HT/USDT",0),

    /**
     * BNB/USDT
     */
    BNBUSDT("bnbusdt","BNB/USDT",0),


    /**
     * XRP/USDT
     */
    XRPUSDT("xrpusdt","XRP/USDT",0),

    /**
     * LINK/USDT
     */
    LINKUSDT("linkusdt","LINK/USDT",0),

    /**
     * 比特币现金/USDT
     */
    BCHUSDT("bchusdt","BCH/USDT",0),


    /**
     * LTC/USDT
     */
    LTCUSDT("ltcusdt","LTC/USDT",0),


    /**
     * BSV/USDT
     */
    BSVUSDT("bsvusdt","BSV/USDT",0),


    /**
     * ADA/USDT
     */
    ADAUSDT("adausdt","ADA/USDT",0),


    /**
     * EOS/USDT
     */
    EOSUSDT("eosusdt","EOS/USDT",0),


    /**
     * TRX/USDT
     */
    TRXUSDT("trxusdt","TRX/USDT",0),


    /**
     * DOT/USDT
     */
    DOTUSDT("dotusdt","DOT/USDT",0),



    /**
     * DOGE/USDT
     */
    DOGEUSDT("dogeusdt","DOGE/USDT",0),

    /**
     * IOTA/USDT
     */
    IOTAUSDT("iotausdt","IOTA/USDT",0),


    /**
     * XMR/USDT
     */
    XMRUSDT("xmrusdt","XMR/USDT",0),

    /**
     * NEXT/USDT
     */
    NEXTUSDT("nextusdt","NEXT/USDT",0),

    /**
     * TON/USDT
     */
    TONUSDT("tonusdt","TON/USDT",0),









    ;
    /**
     * 交易对代码
     */
    private String code;
    /**
     * 交易对名称
     */
    private String name;
    /**
     * 是否支持限合约 0-否 1-是
     */
    private Integer supTimeContract;

    SymbolEnum(String code, String name, Integer supTimeContract) {
        this.code = code;
        this.name = name;
        this.supTimeContract = supTimeContract;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Integer getSupTimeContract() {
        return supTimeContract;
    }

    /**
     * 根据编码获取交易对名称
     *
     * @param code
     * @return
     */
    public static String getNameByCode(String code) {
        for (SymbolEnum symbol : SymbolEnum.values()) {
            if (symbol.code.equals(code)) {
                return symbol.name;
            }
        }
        return null;
    }

    /**
     * 判断交易对是否支持限时合约
     *
     * @param code
     * @return
     */
    public static Boolean isSupTimeContract(String code) {
        for (SymbolEnum symbol : SymbolEnum.values()) {
            if (symbol.code.equals(code)) {
                return symbol.supTimeContract == 1;
            }
        }
        return false;
    }

}
