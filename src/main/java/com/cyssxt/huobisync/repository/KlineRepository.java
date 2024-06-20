package com.cyssxt.huobisync.repository;

import com.bigo.project.bigo.marketsituation.domain.Kline;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface KlineRepository extends JpaRepository<Kline,Long> {

    @Query(value="select max(`timestamp`) from bg_kline where symbol=:symbol and `period`=:period",nativeQuery = true)
    Long getMaxTimestampBySymbolAndPeriod(@Param("symbol") String symbol, @Param("period")String period);

    @Query(value = "select * from bg_kline where symbol = :symbol and period = :period order by `timestamp` DESC limit :size",nativeQuery = true)
    List<Kline> queryTopSize(@Param("symbol") String symbol,@Param("period")String period,@Param("size")int size);

    List<Kline> queryBySymbolAndPeriod(String symbol,String period,Sort sort);

    Kline findFirstByTimestampAndPeriodAndSymbol(Long timestamp,String period,String symbol);

    @Query("select max(A.high) as high,min(A.low) as low,sum(A.count) as total,sum(A.amount) as amount,min(A.timestamp) as start from Kline A where A.period=:period and A.symbol=:symbol " +
            " and A.minuteNo>=:startMinuteNo and A.minuteNo<=:endMinuteNo")
    Map<String, Object> queryMaxAndTotal(@Param("period")String period,@Param("symbol")String symbol
            ,@Param("startMinuteNo") Long startMinuteNo,@Param("endMinuteNo") Long endMinuteNo);

    @Query("select max(A.timestamp) from Kline A where A.period=:period and A.symbol=:symbol")
    Long findMaxTimestamp(@Param("period") String period,@Param("symbol") String symbol);
}
