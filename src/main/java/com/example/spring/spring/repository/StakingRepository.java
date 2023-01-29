package com.example.spring.spring.repository;

import com.example.spring.spring.dao.LikeTb;
import org.springframework.stereotype.Repository;
import com.example.spring.spring.dao.StakingTb;
import com.example.spring.spring.dao.WalletTb;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface StakingRepository extends JpaRepository<StakingTb, Integer>, StakingRepositoryCustom{

}