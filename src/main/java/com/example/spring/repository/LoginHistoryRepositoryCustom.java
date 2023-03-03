package com.example.spring.repository;

import com.example.spring.dao.LoginHistoryTb;

import java.util.List;

public interface LoginHistoryRepositoryCustom {

    public List<LoginHistoryTb> getUserHistory();

    public int getCountByDate(String date);

    public void setIncreaseCount(String date);
}
