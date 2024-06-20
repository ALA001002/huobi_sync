package com.cyssxt.huobisync.repository;

import com.bigo.project.bigo.marketsituation.domain.BgSlipConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface BgSlipConfigRepository  extends JpaRepository<BgSlipConfig,Long>, CrudRepository<BgSlipConfig,Long> {

    @Query("select A from BgSlipConfig A where A.symbol=:symbol and A.openFlag=true and A.startTime<=:currentTime and A.endTime>=:currentTime and A.delFlag=false")
    List<BgSlipConfig> findValidConfig(@Param("symbol") String symbol, @Param("currentTime")Long currentTime);


    @Query("select count(A) from BgSlipConfig A where A.symbol=:symbol and ((A.startTime>=:endTime and A.endTime<=:endTime) or " +
            "(A.startTime<=:startTime and A.endTime>=:startTime) or " +
        " (A.startTime<:startTime and A.endTime>:endTime)) and A.id<>:id and A.delFlag=false ")
    long queryConflicts(@Param("startTime") Long startTime,@Param("endTime")Long endTime,@Param("symbol")String symbol,@Param("id")Long id);

    @Query("select sum(A.addValue) from BgSlipConfig A where A.symbol=:symbol and A.endTime<=:currentTime and A.delFlag=false and A.openFlag=false ")
    BigDecimal querySumAddMount(@Param("symbol")String symbol, @Param("currentTime") Long currentTime);
//    @Transactional
//    @Modifying
//    @Query("update BgSlipConfig A set  A.openFlag=false,A.updateTime=CURRENT_TIMESTAMP where A.symbol=:symbol and A.openFlag=true")
//    void deleteAllBySymbol(@Param("symbol") String symbol);

    Page<BgSlipConfig> findAll(Specification<BgSlipConfig> slipConfigSpecification, Pageable pageable);
}
