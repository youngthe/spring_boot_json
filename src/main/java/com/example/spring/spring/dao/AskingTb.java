package com.example.spring.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "asking")
public class AskingTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asking_id;

    @Column(name = "amount")
    private double amount;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "created_date")
    private LocalDate created_date;

    @Column(name = "status")
    private boolean status;

    @Column(name = "input_output")
    private boolean input_output;

}
