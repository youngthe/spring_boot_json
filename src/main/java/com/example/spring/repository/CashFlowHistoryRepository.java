package com.example.spring.repository;

import com.example.spring.dao.CashFlowHistoryTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface CashFlowHistoryRepository extends JpaRepository<CashFlowHistoryTb, Integer>, CashFlowHistoryTbRepositoryCustom {

}
