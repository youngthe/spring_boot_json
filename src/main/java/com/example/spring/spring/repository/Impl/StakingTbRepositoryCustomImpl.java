package com.example.spring.spring.repository.Impl;

import com.example.spring.spring.dao.StakingTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.repository.CommentTbRepositoryCustom;
import com.example.spring.spring.repository.StakingRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class StakingTbRepositoryCustomImpl extends QuerydslRepositorySupport implements StakingRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public StakingTbRepositoryCustomImpl() {
        super(StakingTb.class);
    }


}
