package com.example.spring.dao;

import com.sun.jna.platform.win32.OaIdl;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "community")
public class CommunityTb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int community_id;

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "MEDIUMTEXT")
    private String content;

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

    @Column(name = "total_reward")
    private double total_reward;

}
