package com.cyssxt.huobisync.repository;

import com.bigo.project.bigo.marketsituation.domain.RandomConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface RandomConfigRepository extends CrudRepository<RandomConfig,Integer>, JpaRepository<RandomConfig,Integer> {

    RandomConfig findFirstByPeriodAndSymbol(String period,String symbol);
    RandomConfig findFirstBySymbol(String symbol);
}
