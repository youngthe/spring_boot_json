package com.example.spring.repository;

import com.example.spring.dao.StakingTb;

public interface StakingTbRepositoryCustom {

    public StakingTb getStakingTbByStakingId(int staking_id);

    public StakingTb getStakingTbByUserId(int user_id);

}
