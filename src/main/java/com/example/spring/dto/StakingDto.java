package com.example.spring.dto;

import com.example.spring.dao.StakingTb;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class StakingDto {

    private int staking_id;

    private String wallet_address;

    private int user_id;

    private double reward_amount;

    private String expire_date;

    private String created_date;


    private String name;

    private double percent;

    private double start_amount;

    private String release_date;

    private double add_amount;

    public StakingDto(StakingTb stakingTb) {
        this.staking_id = stakingTb.getStaking_id();
        this.wallet_address = stakingTb.getWallet_address();
        this.user_id = stakingTb.getUser_id();
        this.reward_amount = stakingTb.getReward_amount();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        this.expire_date = format.format(stakingTb.getExpire_date()) ;
        this.created_date = format.format(stakingTb.getCreated_date());
        this.name = stakingTb.getName();
        this.percent = stakingTb.getPercent();
        this.start_amount = stakingTb.getStart_amount();
        if(!ObjectUtils.isEmpty(stakingTb.getRelease_date())){
            this.release_date = format.format(stakingTb.getRelease_date());
        }
        this.add_amount = stakingTb.getAdd_amount();
    }
}
