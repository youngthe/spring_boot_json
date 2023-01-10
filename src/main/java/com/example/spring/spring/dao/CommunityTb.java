package com.example.spring.spring.dao;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "COMMUNITY")
public class CommunityTb {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usertb_id")
    private UserTb user;


    public UserTb getUser() {
        return user;
    }

    public void setUser(UserTb user) {
        this.user = user;
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

    public int getId() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
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


}
