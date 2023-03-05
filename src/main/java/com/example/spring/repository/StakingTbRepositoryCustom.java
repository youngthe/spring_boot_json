package com.example.spring.repository;

import com.example.spring.dao.StakingTb;

import java.util.List;

public interface StakingTbRepositoryCustom {

    public StakingTb getStakingTbByStakingId(int staking_id);

    public List<StakingTb> getStakingTbByUserId(int user_id);

    public List<StakingTb> getStakingTbThatStateTrue();

    public List<StakingTb> getStakingByAddress(String address);

}
