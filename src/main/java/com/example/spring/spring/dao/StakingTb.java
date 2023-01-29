package com.example.spring.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;


@Getter
@Setter
@Entity
@Table(name = "staking")
public class StakingTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staking_id")
    private int stakingId;

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

}
