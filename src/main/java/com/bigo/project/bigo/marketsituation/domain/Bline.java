package com.bigo.project.bigo.marketsituation.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "bg_bline")
public class Bline implements Serializable {
    private Long id;
    private Long bid;
    private Long tradeId;
    private String symbol;
    private BigDecimal amount;
    private BigDecimal price;
    private BigDecimal realPrice;
    private Long ts;
    private String direction;
    private BigDecimal realAmount;

    @Basic
    @Column(name = "real_amount")
    public BigDecimal getRealAmount() {
        return realAmount;
    }

    public void setRealAmount(BigDecimal realAmount) {
        this.realAmount = realAmount;
    }

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
    @Column(name = "bid")
    public Long getBid() {
        return bid;
    }

    public void setBid(Long bid) {
        this.bid = bid;
    }

    @Basic
    @Column(name = "trade_id")
    public Long getTradeId() {
        return tradeId;
    }

    public void setTradeId(Long tradeId) {
        this.tradeId = tradeId;
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
    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Basic
    @Column(name = "price")
    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Basic
    @Column(name = "real_price")
    public BigDecimal getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(BigDecimal realPrice) {
        this.realPrice = realPrice;
    }

    @Basic
    @Column(name = "ts")
    public Long getTs() {
        return ts;
    }

    public void setTs(Long ts) {
        this.ts = ts;
    }

    @Basic
    @Column(name = "direction")
    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bline that = (Bline) o;
        return tradeId == that.tradeId && Objects.equals(id, that.id) && Objects.equals(bid, that.bid) && Objects.equals(symbol, that.symbol) && Objects.equals(amount, that.amount) && Objects.equals(price, that.price) && Objects.equals(realPrice, that.realPrice) && Objects.equals(ts, that.ts) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, bid, tradeId, symbol, amount, price, realPrice, ts, direction);
    }
}
