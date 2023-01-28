package com.example.spring.spring.repository;

import com.example.spring.spring.dao.LikeTb;
import com.example.spring.spring.dao.StakingTb;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StakingRepository extends JpaRepository<StakingTb, Integer>, StakingRepositoryCustom{

}
