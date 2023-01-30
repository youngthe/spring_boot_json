package com.example.spring.spring.repository;

import com.example.spring.spring.dao.StakingTb;

public interface StakingRepositoryCustom {
    public StakingTb getStakingTbByStakingId(int staking_id);

    public StakingTb getStakingBtByUserId(int user_id);

}
