package com.example.spring.dao;

import javax.persistence.*;

@Entity
@Table(name = "likes")
public class LikeTb {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int likes_id;

    @Column(name = "community_id")
    private int community_id;

    @Column(name = "user_id")
    private int user_id;


    public int getCommunity_id() {
        return community_id;
    }

    public void setCommunity_id(int community_id) {
        this.community_id = community_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getLike_id() {
        return likes_id;
    }

    public void setLike_id(int like_id) {
        this.likes_id = like_id;
    }

}
