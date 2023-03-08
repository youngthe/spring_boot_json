package com.example.spring.repository.Impl;

import com.example.spring.dao.AskingTb;
import com.example.spring.dao.QAskingTb;
import com.example.spring.repository.AskingTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class AskingTbRepositoryCustomImpl extends QuerydslRepositorySupport implements AskingTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public AskingTbRepositoryCustomImpl() {
        super(AskingTb.class);
    }



    public AskingTb getAskingTbByAskingId(int asking_id){

        QAskingTb qAskingTb = QAskingTb.askingTb;

        return query
                .selectFrom(qAskingTb)
                .where(qAskingTb.asking_id.eq(asking_id))
                .fetchOne();

    }
}
