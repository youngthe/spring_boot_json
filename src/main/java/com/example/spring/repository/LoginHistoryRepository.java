package com.example.spring.repository;

import com.example.spring.dao.LikeTb;
import com.example.spring.dao.LoginHistoryTb;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

@Repository
public interface LoginHistoryRepository  extends JpaRepository<LoginHistoryTb, Integer>, LoginHistoryRepositoryCustom{
}
