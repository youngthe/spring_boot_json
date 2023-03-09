package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class InOutHistoryTb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "in_out")
    private boolean in_out;

    @Column(name = "coin")
    private double coin;

    @Column(name = "account")
    private String account;


}
