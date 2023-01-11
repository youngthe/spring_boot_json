package com.example.spring.spring.repository.Impl;

import com.example.spring.spring.dao.HeartTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.domain.QHeartTb;
import com.example.spring.spring.repository.HeartTbRepositoryCustom;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.transaction.Transactional;

public class HeartTbRepositoryCustomImpl extends QuerydslRepositorySupport implements HeartTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public HeartTbRepositoryCustomImpl() {
        super(UserTb.class);
    }

    @Transactional
    public boolean deleteByCommunityIdAndWriterId(HeartTb heartTb){

        QHeartTb qhearttb = QHeartTb.heart;

        try{
            query
                    .delete(qhearttb)
                    .where(qhearttb.community_id.eq(heartTb.getCommunity_id()))
                    .where(qhearttb.writer_id.eq(heartTb.getWriter_id()))
                    .execute();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public boolean HeartCheck(HeartTb heartTb){
        QHeartTb qhearttb = QHeartTb.heart;

        if(query
                .selectFrom(qhearttb)
                .where(qhearttb.writer_id.eq(heartTb.getWriter_id()))
                .where(qhearttb.community_id.eq(heartTb.getCommunity_id()))
                .fetchOne() == null){
            return true;
        }else{
            return false;
        }
    }
}
