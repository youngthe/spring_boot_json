package com.example.spring.spring.repository;

import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.dao.WalletTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletTb, Integer>, WalletTbRepositoryCustom{
}
