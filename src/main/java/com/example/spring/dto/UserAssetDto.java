package com.example.spring.dto;

import com.example.spring.dao.StakingTb;
import com.example.spring.dao.UserTb;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class UserAssetDto {

    private double total_coin;

    private double usable_coin;

    private double staking_coin;

    private String expire_date;

    private boolean staking_state;

    public UserAssetDto(UserTb usertb, StakingTb stakingTb) {
        this.total_coin = usertb.getCoin() + stakingTb.getStart_amount() + stakingTb.getAdd_amount();
        System.out.println(usertb.getCoin());
        System.out.println(stakingTb.getAdd_amount());
        System.out.println(stakingTb.getStart_amount());
        this.usable_coin = usertb.getCoin();
        this.staking_coin = stakingTb.getStart_amount();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.expire_date = format.format(stakingTb.getExpire_date());
        if(stakingTb.getStart_amount() == 0){
            staking_state = false;
        }else{
            staking_state = true;
        }
    }
}
