package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;



@Entity
@Table(name = "staking")
public class StakingTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staking_id")
    private int staking_id;

    @Column(name = "wallet_id")
    private int wallet_id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "reward_amount")
    private double reward_amount;

    @Column(name = "expire_date")
    private LocalDate expire_date;

    @Column(name = "created_date")
    private LocalDate created_date;

    @Column(name = "last_modified_date")
    private LocalDate last_modified_date;

    @Column(name = "name")
    private String name;


    public int getStaking_id() {
        return staking_id;
    }

    public void setStakingId(int staking_id) {
        this.staking_id = staking_id;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getReward_amount() {
        return reward_amount;
    }

    public void setReward_amount(double reward_amount) {
        this.reward_amount = reward_amount;
    }

    public LocalDate getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(LocalDate expire_date) {
        this.expire_date = expire_date;
    }

    public LocalDate getCreated_date() {
        return created_date;
    }

    public void setCreated_date(LocalDate created_date) {
        this.created_date = created_date;
    }

    public LocalDate getLast_modified_date() {
        return last_modified_date;
    }

    public void setLast_modified_date(LocalDate last_modified_date) {
        this.last_modified_date = last_modified_date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
