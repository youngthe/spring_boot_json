package com.example.spring.spring.repository;

import com.example.spring.spring.dao.HeartTb;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface HeartTbRepositoryCustom {
    public boolean deleteByCommunityIdAndWriterId(HeartTb heartTb);

    public boolean HeartCheck(HeartTb heartTb);
}
