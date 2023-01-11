package com.example.spring.spring.dao;

import javax.persistence.*;

@Entity
@Table(name = "HEART")
public class HeartTb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "communit_id")
    private String community_id;

    @Column(name = "writer_id")
    private int writer_id;


    public String getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(String community_id) {
        this.community_id = community_id;
    }

    public int getWriter_id() {
        return writer_id;
    }

    public void setWriter_id(int writer_id) {
        this.writer_id = writer_id;
    }
}
