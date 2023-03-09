package com.example.spring.repository;

import com.example.spring.dao.LoginHistoryTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginHistoryRepository  extends JpaRepository<LoginHistoryTb, Integer>, LoginHistoryRepositoryCustom{
}
