package com.example.spring.dto;

import com.example.spring.dao.CommunityTb;
import com.example.spring.dao.TestContent;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CommunityWriterDtoWithoutContent {
    int community_id;

    String title;

    String date;

    int hits;

    int user_id;

    String category;

    boolean highlight;

    double get_coin;

    boolean comment_allow;

    String name;

    public CommunityWriterDtoWithoutContent(TestContent communityTb, String name) {
        this.community_id = communityTb.getCommunity_id();
        this.title = communityTb.getTitle();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
//        System.out.println(format.format(communityTb.getDate()));
        this.date = format.format(communityTb.getDate());
        this.hits = communityTb.getHits();
        this.user_id = communityTb.getUser_id();
        this.category = communityTb.getCategory();
        this.highlight = communityTb.isHighlight();
        this.get_coin = communityTb.getGet_coin();
        this.comment_allow = communityTb.isComment_allow();
        this.name = name;
    }

    public CommunityWriterDtoWithoutContent() {

    }
}
