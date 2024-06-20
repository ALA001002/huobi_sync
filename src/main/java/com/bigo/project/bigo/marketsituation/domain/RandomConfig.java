package com.bigo.project.bigo.marketsituation.domain;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Optional;

@Data
@Entity
@Table(name = "random_config")
public class RandomConfig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "period")
    private String period;

    @Column(name = "symbol")
    private String symbol;
    /**
     * 引线长度
     */
    @Column(name = "lead_wire")
    private Double leadWire;

    /**
     * 跌幅
     */
    @Column(name = "decline")
    private Double decline;

    /**
     * 随机值
     */
    @Column(name="random_rate")
    private Integer randomRate;

    /**
     * 低值幅度
     */
    @Column(name="low_deg")
    private Double lowDeg;

    /**
     * 低值幅度
     */
    @Column(name="bar_rate")
    private Double barRate;

    @Column(name="start")
    private BigDecimal start;

    @Column(name = "symbol_list")
    private String symbolList;

    @Column(name = "symbol_rate_list")
    private String symbolRateList;

    @Column(name = "update_rate")
    private BigDecimal updateRate;

    public Double getBarRate() {
        return Optional.ofNullable(barRate).orElse(1.0);
    }

    public Double getLeadWire() {
        return Optional.ofNullable(leadWire).orElse(1.5);
    }

    public Double getDecline() {
        return Optional.ofNullable(decline).orElse(0.2);
    }

    public Integer getRandomRate() {
        return Optional.ofNullable(randomRate).orElse(1440);
    }

    public Double getLowDeg() {
        return Optional.ofNullable(lowDeg).orElse(1.5);
    }

    public BigDecimal getStart(){
        return Optional.ofNullable(start).orElse(new BigDecimal("0.01"));
    }

    public BigDecimal getUpdateRate() {
        return Optional.ofNullable(updateRate).orElse(new BigDecimal("0.09"));
    }
}
