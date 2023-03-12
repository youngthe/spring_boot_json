package com.example.spring.repository;

import com.example.spring.dao.CashFlowHistoryTb;

import java.util.List;

public interface CashFlowHistoryTbRepositoryCustom {
    public List<CashFlowHistoryTb> getInoutHistoryByUserId(int user_id);
}
