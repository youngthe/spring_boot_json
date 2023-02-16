package com.example.spring.dto;

import com.example.spring.dao.StakingTb;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class StakingDto {

    private int staking_id;

    private String wallet_address;

    private int user_id;

    private double reward_amount;

    private String expire_date;

    private String created_date;

    private String last_modified_date;

    private String name;

    private double percent;

    private double start_amount;


    public StakingDto(StakingTb stakingTb) {
        this.staking_id = stakingTb.getStaking_id();
        this.wallet_address = stakingTb.getWallet_address();
        this.user_id = stakingTb.getUser_id();
        this.reward_amount = stakingTb.getReward_amount();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        this.expire_date = format.format(stakingTb.getExpire_date()) ;
        this.created_date = format.format(stakingTb.getCreated_date());
        this.last_modified_date = format.format(stakingTb.getLast_modified_date());
        this.name = stakingTb.getName();
        this.percent = stakingTb.getPercent();
        this.start_amount = stakingTb.getStart_amount();
    }
}
