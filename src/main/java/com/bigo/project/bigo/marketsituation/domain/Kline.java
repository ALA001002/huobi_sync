package com.bigo.project.bigo.marketsituation.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bg_kline")
public class Kline implements Serializable {
    private Long id;
    private String period;
    private String symbol;
    private Long timestamp;
    private BigDecimal amount;
    private Long count;
    private BigDecimal open;
    private BigDecimal close;
    private BigDecimal low;
    private BigDecimal high;
    private BigDecimal vol;
    private BigDecimal realOpen;
    private BigDecimal realClose;
    private BigDecimal realLow;
    private BigDecimal realHigh;
    private Long minuteNo;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "period")
    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Basic
    @Column(name = "symbol")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Basic
    @Column(name = "timestamp")
    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Basic
    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "count")
    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Basic
    @Column(name = "open")
    public BigDecimal getOpen() {
        return open;
    }

    public void setOpen(BigDecimal open) {
        this.open = open;
    }

    @Basic
    @Column(name = "close")
    public BigDecimal getClose() {
        return close;
    }

    public void setClose(BigDecimal close) {
        this.close = close;
    }

    @Basic
    @Column(name = "low")
    public BigDecimal getLow() {
        return low;
    }

    public void setLow(BigDecimal low) {
        this.low = low;
    }

    @Basic
    @Column(name = "high")
    public BigDecimal getHigh() {
        return high;
    }

    public void setHigh(BigDecimal high) {
        this.high = high;
    }

    @Basic
    @Column(name = "vol")
    public BigDecimal getVol() {
        return vol;
    }

    public void setVol(BigDecimal vol) {
        this.vol = vol;
    }

    @Basic
    @Column(name = "real_open")
    public BigDecimal getRealOpen() {
        return realOpen;
    }

    public void setRealOpen(BigDecimal realOpen) {
        this.realOpen = realOpen;
    }

    @Basic
    @Column(name = "real_close")
    public BigDecimal getRealClose() {
        return realClose;
    }

    public void setRealClose(BigDecimal realClose) {
        this.realClose = realClose;
    }

    @Basic
    @Column(name = "real_low")
    public BigDecimal getRealLow() {
        return realLow;
    }

    public void setRealLow(BigDecimal realLow) {
        this.realLow = realLow;
    }

    @Basic
    @Column(name = "real_high")
    public BigDecimal getRealHigh() {
        return realHigh;
    }

    public void setRealHigh(BigDecimal realHigh) {
        this.realHigh = realHigh;
    }

    @Basic
    @Column(name = "minute_no")
    public Long getMinuteNo() {
        return minuteNo;
    }

    public void setMinuteNo(Long minuteNo) {
        this.minuteNo = minuteNo;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Kline that = (Kline) o;
        return Objects.equals(id, that.id) && Objects.equals(period, that.period) && Objects.equals(symbol, that.symbol) && Objects.equals(timestamp, that.timestamp) && Objects.equals(amount, that.amount) && Objects.equals(count, that.count) && Objects.equals(open, that.open) && Objects.equals(close, that.close) && Objects.equals(low, that.low) && Objects.equals(high, that.high) && Objects.equals(vol, that.vol) && Objects.equals(realOpen, that.realOpen) && Objects.equals(realClose, that.realClose) && Objects.equals(realLow, that.realLow) && Objects.equals(realHigh, that.realHigh);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, period, symbol, timestamp, amount, count, open, close, low, high, vol, realOpen, realClose, realLow, realHigh);
    }
}
