package com.example.spring.dao;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

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


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }


    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    public double getGet_coin() {
        return get_coin;
    }

    public void setGet_coin(double get_coin) {
        this.get_coin = get_coin;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isComment_allow() {
        return comment_allow;
    }

    public void setComment_allow(boolean comment_allow) {
        this.comment_allow = comment_allow;
    }

    public double getTotal_reward() {
        return total_reward;
    }

    public void setTotal_reward(double total_reward) {
        this.total_reward = total_reward;
    }
}
