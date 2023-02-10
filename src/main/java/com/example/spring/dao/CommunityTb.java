package com.example.spring.dao;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "community")
public class CommunityTb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int community_id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "file_name")
    private String file_name;


    @Column(name = "date")
    private LocalDate date;

    @Column(name = "hits")
    private int hits;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "type")
    private String type;

    @Column(name = "highlight")
    private boolean highlight;

    @Column(name = "get_coin")
    private double get_coin;


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
