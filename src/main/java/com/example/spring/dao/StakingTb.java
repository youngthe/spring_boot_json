package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@Entity
@Table(name = "staking")
public class StakingTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staking_id")
    private int staking_id;

    @Column(name = "wallet_address")
    private String wallet_address;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "reward_amount")
    private double reward_amount;

    @Column(name = "expire_date")
    private Date expire_date;

    @Column(name = "created_date")
    private Date created_date;

    @Column(name = "release_date")
    private Date release_date;

    @Column(name = "name")
    private String name;

    @Column(name = "percent")
    private double percent;

    @Column(name = "start_amount")
    private double start_amount;

    @Column(name = "add_amount")
    private double add_amount;

    @Column(name = "state")
    private boolean state;

    @Column(name = "block_hash")
    private String block_hash;


}
