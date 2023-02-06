package com.example.spring.repository;

import com.example.spring.dao.WalletTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<WalletTb, Integer>, WalletTbRepositoryCustom{
}
