package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "community")
public class TestContent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int community_id;

    @Column(name = "title")
    private String title;


    @Column(name = "date")
    private Date date;

    @Column(name = "hits")
    private int hits;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "category")
    private String category;

    @Column(name = "highlight")
    private boolean highlight;

    @Column(name = "get_coin")
    private double get_coin;

    @Column(name = "comment_allow")
    private boolean comment_allow;
}
