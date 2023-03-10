package com.example.spring.repository;

import com.example.spring.dao.InOutHistoryTb;

import java.util.List;

public interface InOutHistoryTbRepositoryCustom {
    public List<InOutHistoryTb> getInoutHistoryByUserId(int user_id);
}
