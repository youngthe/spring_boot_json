package com.example.spring.repository.Impl;

import com.example.spring.dao.AskingTb;
import com.example.spring.dao.QAskingTb;
import com.example.spring.repository.AskingTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class AskingTbRepositoryCustomImpl extends QuerydslRepositorySupport implements AskingTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public AskingTbRepositoryCustomImpl() {
        super(AskingTb.class);
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {

        super.setEntityManager(entityManager);

    }

    public AskingTb getAskingTbByAskingId(int asking_id){

        QAskingTb qAskingTb = QAskingTb.askingTb;

        return query
                .selectFrom(qAskingTb)
                .where(qAskingTb.asking_id.eq(asking_id))
                .fetchOne();

    }

    public List<AskingTb> getAskingListByUserId(int user_id){

        QAskingTb qAskingTb = QAskingTb.askingTb;

        return query
                .selectFrom(qAskingTb)
                .where(qAskingTb.user_id.eq(user_id))
                .fetch();

    }
}
