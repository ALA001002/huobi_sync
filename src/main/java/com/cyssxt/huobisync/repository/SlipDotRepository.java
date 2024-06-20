package com.cyssxt.huobisync.repository;

import com.bigo.project.bigo.marketsituation.domain.SlipDot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface SlipDotRepository extends JpaRepository<SlipDot,Long>, CrudRepository<SlipDot,Long> {

    @Query(value = "select *  from bg_slip_dot where `status` > 0 and symbol = :symbol and (NOT ((stop_dot_time < :startTime) OR (start_dot_time > :endTime) OR (start_dot_time <=:endTime and stop_dot_time is null)))",nativeQuery = true)
    List<SlipDot> listSlipDotByDate(@Param("symbol")String symbol, @Param("startTime") Date startTime, @Param("endTime")Date endTime);
}
