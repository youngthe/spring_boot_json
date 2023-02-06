package com.example.spring.repository;

import com.example.spring.dao.StakingTb;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface StakingRepository extends JpaRepository<StakingTb, Integer>, StakingRepositoryCustom{

}