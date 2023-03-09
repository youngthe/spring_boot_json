package com.example.spring.dto;

import com.example.spring.dao.CommunityTb;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class MyCommunityDto {

    int community_id;

    String name;

    String title;

    String content;

    String date;

    int hits;

    int user_id;

    String category;

    boolean highlight;

    boolean comment_allow;

//    boolean like;

    int comment_total;

    double get_coin;

    int like_total;

    double total_reward;

    public MyCommunityDto(CommunityTb community, int comment_total, int like_total, double total_reward, String name) {
        this.community_id = community.getCommunity_id();
        this.name = name;
        this.title = community.getTitle();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = format.format(community.getDate());
        this.hits = community.getHits();
        this.user_id = community.getUser_id();
        this.category = community.getCategory();
        this.highlight = community.isHighlight();
        this.comment_allow = community.isComment_allow();
        this.get_coin = community.getGet_coin();
        this.comment_total = comment_total;
        this.like_total = like_total;
        this.total_reward = total_reward;
        this.content = community.getContent();
    }
}
