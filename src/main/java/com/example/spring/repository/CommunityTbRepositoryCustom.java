package com.example.spring.repository;

import com.example.spring.dao.CommunityTb;
import com.example.spring.dao.TestContent;
import com.querydsl.core.Tuple;

import java.util.List;

public interface CommunityTbRepositoryCustom {

    public List<CommunityTb> getCommunity();

    public List<Tuple> getCommunityBySearch(String title);
    public CommunityTb getCommunityById(int Community_id);
    public void Increase_like(CommunityTb communityTb);

    public void updateCommunity(CommunityTb communityTb);

    public List<TestContent> getCommunityByCategory(String category);

    public TestContent getCommunBylast();


    public List<TestContent> getCommunityByUserId(int user_id);

    public List<CommunityTb> getCommunityListByUserId(int user_id);

    public List<CommunityTb> getCommunityBylimit(int limit);

    public List<TestContent> getCommunityListByUserIdAndDate(int user_id);
}


