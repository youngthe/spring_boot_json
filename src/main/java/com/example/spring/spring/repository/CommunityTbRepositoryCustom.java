package com.example.spring.spring.repository;

import com.example.spring.spring.dao.CommunityTb;

import java.util.List;

public interface CommunityTbRepositoryCustom {

    public List<CommunityTb> getCommunity();

    public List<CommunityTb> getCommunityBySearch(String title);
    public CommunityTb getCommunityById(int Community_id);
    public void Increase_like(CommunityTb communityTb);

    public void updateCommunity(CommunityTb communityTb);
}


