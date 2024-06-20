package com.bigo.project.bigo.marketsituation.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "bg_slip_dot")
public class SlipDot implements Serializable {
    private Long id;
    private String symbol;
    private BigDecimal adjustPrice;
    private Byte status;
    private Timestamp startDotTime;
    private Timestamp stopDotTime;
    private Timestamp createTime;
    private Integer creatorId;
    private byte deleted;

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
    @Column(name = "symbol")
    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Basic
    @Column(name = "adjust_price")
    public BigDecimal getAdjustPrice() {
        return adjustPrice;
    }

    public void setAdjustPrice(BigDecimal adjustPrice) {
        this.adjustPrice = adjustPrice;
    }

    @Basic
    @Column(name = "status")
    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Basic
    @Column(name = "start_dot_time")
    public Timestamp getStartDotTime() {
        return startDotTime;
    }

    public void setStartDotTime(Timestamp startDotTime) {
        this.startDotTime = startDotTime;
    }

    @Basic
    @Column(name = "stop_dot_time")
    public Timestamp getStopDotTime() {
        return stopDotTime;
    }

    public void setStopDotTime(Timestamp stopDotTime) {
        this.stopDotTime = stopDotTime;
    }

    @Basic
    @Column(name = "create_time")
    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Basic
    @Column(name = "creator_id")
    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    @Basic
    @Column(name = "deleted")
    public byte getDeleted() {
        return deleted;
    }

    public void setDeleted(byte deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SlipDot that = (SlipDot) o;
        return deleted == that.deleted && Objects.equals(id, that.id) && Objects.equals(symbol, that.symbol) && Objects.equals(adjustPrice, that.adjustPrice) && Objects.equals(status, that.status) && Objects.equals(startDotTime, that.startDotTime) && Objects.equals(stopDotTime, that.stopDotTime) && Objects.equals(createTime, that.createTime) && Objects.equals(creatorId, that.creatorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, symbol, adjustPrice, status, startDotTime, stopDotTime, createTime, creatorId, deleted);
    }
}
