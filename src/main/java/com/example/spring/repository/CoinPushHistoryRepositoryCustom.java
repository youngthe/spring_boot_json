package com.example.spring.repository;

import com.example.spring.dao.CoinPushHistoryTb;

import java.util.List;

public interface CoinPushHistoryRepositoryCustom {

    public List<CoinPushHistoryTb> getCoinPushHistoryByUserId(int user_id);
}
