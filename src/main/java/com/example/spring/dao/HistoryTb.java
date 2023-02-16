package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "transaction")
public class HistoryTb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int transaction_id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "input_output")
    private String input_output;

    @Column(name = "coin")
    private Date coin;

    @Column(name = "date")
    private Date date;



}
