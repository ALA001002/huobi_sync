package com.bigo.project.bigo.marketsituation.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "bg_slip_config")
public class BgSlipConfig {
    private Long id;
    private Long startTime;
    private Long endTime;
    private Boolean openFlag;
    private BigDecimal addValue;
    private String symbol;
    private Boolean delFlag;
    private Timestamp createTime;
    private Timestamp updateTime;
    private BigDecimal addAmount;
    private Integer intervalTime;

    @Basic
    @Column(name="interval_time")
    public Integer getIntervalTime() {
        return intervalTime;
    }

    public void setIntervalTime(Integer intervalTime) {
        this.intervalTime = intervalTime;
    }

    public BigDecimal getAddAmount() {
        return addAmount;
    }

    public void setAddAmount(BigDecimal addAmount) {
        this.addAmount = addAmount;
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
    @Column(name = "start_time")
    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "open_flag")
    public Boolean getOpenFlag() {
        return openFlag;
    }

    public void setOpenFlag(Boolean openFlag) {
        this.openFlag = openFlag;
    }

    @Basic
    @Column(name = "add_value")
    public BigDecimal getAddValue() {
        return addValue;
    }

    public void setAddValue(BigDecimal addValue) {
        this.addValue = addValue;
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
    @Column(name = "del_flag")
    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
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
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BgSlipConfig that = (BgSlipConfig) o;
        return id == that.id && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(openFlag, that.openFlag) && Objects.equals(addValue, that.addValue) && Objects.equals(symbol, that.symbol) && Objects.equals(delFlag, that.delFlag) && Objects.equals(createTime, that.createTime) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startTime, endTime, openFlag, addValue, symbol, delFlag, createTime, updateTime);
    }
}
