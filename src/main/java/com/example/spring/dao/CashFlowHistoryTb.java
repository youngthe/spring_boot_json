package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "CashFlowHistoryTb")
public class CashFlowHistoryTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "coin")
    private double coin;

    @Column(name = "from_address")
    private String from_address;

    @Column(name = "to_ address")
    private String to_address;

    @Column(name = "date")
    private Date date;

    //0. 출금, 1. 입금, 2. 강제 입금, 3. 강제 출금
    @Column(name = "state")
    private int state;

}
