package com.example.spring.repository;

import com.example.spring.dao.InOutHistoryTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InOutHistoryRepository extends JpaRepository<InOutHistoryTb, Integer>, InOutHistoryTbRepositoryCustom{

}
