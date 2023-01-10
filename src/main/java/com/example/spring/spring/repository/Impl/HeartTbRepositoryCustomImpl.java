package com.example.spring.spring.repository.Impl;

import com.example.spring.spring.dao.HeartTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.domain.QHeartTb;
import com.example.spring.spring.domain.QUserTb;
import com.example.spring.spring.repository.CommunityTbRepositoryCustom;
import com.example.spring.spring.repository.HeartTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class HeartTbRepositoryCustomImpl extends QuerydslRepositorySupport implements HeartTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public HeartTbRepositoryCustomImpl() {
        super(UserTb.class);
    }

//    public List<HeartTb> HeartCheck(HeartTb HartTb){
//
//        QHeartTb hearttb = QHeartTb.heart;
//
//        return query.selectFrom(hearttb).fetch();
//    }
}
