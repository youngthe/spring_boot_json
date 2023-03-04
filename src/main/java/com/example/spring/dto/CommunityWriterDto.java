package com.example.spring.dto;

import com.example.spring.dao.CommunityTb;
import com.example.spring.dao.TestContent;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


@Getter
@Setter
public class CommunityWriterDto {

    int community_id;

    String title;

    String content;

    String date;

    int hits;

    int user_id;

    String category;

    boolean highlight;

    double get_coin;

    boolean comment_allow;

    String name;

    boolean like;

    int comment_total;

    int like_total;

    double total_reward;
    public CommunityWriterDto(CommunityTb communityTb, String name, boolean likeResult, int like_total, int comment_total) {
        this.community_id = communityTb.getCommunity_id();
        this.title = communityTb.getTitle();
        this.content = communityTb.getContent();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.hits = communityTb.getHits();
        this.date = format.format(communityTb.getDate());
        this.user_id = communityTb.getUser_id();
        this.category = communityTb.getCategory();
        this.highlight = communityTb.isHighlight();
        this.get_coin = communityTb.getGet_coin();
        this.comment_allow = communityTb.isComment_allow();
        this.name = name;
        this.like = likeResult;
        this.like_total = like_total;
        this.comment_total = comment_total;
        this.total_reward = communityTb.getTotal_reward();
    }

}
