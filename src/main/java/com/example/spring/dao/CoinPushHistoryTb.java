package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CoinpushHistory")
public class CoinPushHistoryTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private Date date;

    @Column(name = "giver")
    private int giver;

    @Column(name = "receiver")
    private int receiver;

    @Column(name = "coin")
    private double coin;

    @Column(name = "community_id")
    private int community_id;

    //1. 꽃달기, 2. 응원하기, 3. 이자 지급
    @Column(name = "type")
    private int type;

}
