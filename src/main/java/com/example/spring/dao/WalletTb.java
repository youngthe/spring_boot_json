package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "wallet")
public class WalletTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int wallet_id;

    @Column(nullable = false)
    private int user_id;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private LocalDate created_date;

    @Column(nullable = false)
    private LocalDate last_modified_date;

    @Column(nullable = false)
    private double coin;


}
