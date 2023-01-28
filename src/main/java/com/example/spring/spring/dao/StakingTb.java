package com.example.spring.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="staking")
public class StakingTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int staking_id;

    @Column()
    private int wallet_id;

    @Column()
    private double reward_amount;

    @Column()
    private LocalDate expire_day;

    @Column()
    private LocalDate created_date;

    @Column()
    private LocalDate last_modified_date;

    @Column()
    private String name;

}
