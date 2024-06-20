package com.cyssxt.huobisync.repository;

import com.bigo.project.bigo.marketsituation.domain.Bline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BlineRepository extends JpaRepository<Bline,Long>, CrudRepository<Bline,Long> {
    @Query(value = "select max(trade_id) from bg_bline where symbol=:symbol",nativeQuery = true)
    Long queryMaxTradeId(@Param("symbol") String symbol);

    @Query(value="select * from bg_bline where symbol =:symbol and ts >=:startTime and ts <= :endTime ORDER BY ts ASC",nativeQuery = true)
    List<Bline> listByTime(@Param("startTime") Long startTime, @Param("endTime") Long endTime, @Param("symbol") String symbol);
}
