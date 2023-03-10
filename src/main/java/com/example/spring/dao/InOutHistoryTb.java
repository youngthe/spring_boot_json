package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "cashFlowTb")
public class InOutHistoryTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "in_out")
    private boolean in_out;

    @Column(name = "coin")
    private double coin;

    @Column(name = "address")
    private String address;

    @Column(name = "date")
    private Date date;


}
