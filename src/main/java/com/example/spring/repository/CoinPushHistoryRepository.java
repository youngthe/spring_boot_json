package com.example.spring.repository;

import com.example.spring.dao.CoinPushHistoryTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinPushHistoryRepository extends JpaRepository<CoinPushHistoryTb, Integer>, CoinPushHistoryRepositoryCustom{
}
