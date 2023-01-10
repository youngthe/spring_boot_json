package com.example.spring.spring.repository.Impl;

import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.domain.QCommunityTb;
import com.example.spring.spring.domain.QUserTb;
import com.example.spring.spring.repository.CommunityTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.transaction.Transactional;
import java.util.List;

public class CommunityTbRepositoryCustomImpl extends QuerydslRepositorySupport implements CommunityTbRepositoryCustom {


    @Autowired
    JPAQueryFactory query;

    public CommunityTbRepositoryCustomImpl() {
        super(CommunityTb.class);
    }

    @Override
    public List<CommunityTb> getCommunity() {

        QCommunityTb qCommunityTb = QCommunityTb.CommunityTb;
        QUserTb qusertb = QUserTb.user;

        return query
                .selectFrom(qCommunityTb)
                .orderBy(qCommunityTb.id.desc())
                .fetch();
    }

    public List<CommunityTb> getCommunityBySearch(String title){

        QCommunityTb qCommunityTb = QCommunityTb.CommunityTb;

        return query
                .selectFrom(qCommunityTb)
                .where(qCommunityTb.title.startsWith(title))
                .orderBy(qCommunityTb.id.desc())
                .fetch();
    }

    @Override
    public CommunityTb getCommunityById(int Community_id){

        QCommunityTb qCommunityTb = QCommunityTb.CommunityTb;

        return query
                .selectFrom(qCommunityTb)
                .where(qCommunityTb.id.eq(Community_id))
                .fetchFirst();

    }


    @Transactional
    public void Increase_like(CommunityTb communityTb){

        QCommunityTb qCommunityTb = QCommunityTb.CommunityTb;

        query.update(qCommunityTb)
                .where(qCommunityTb.id.eq(communityTb.getId()))
                .set(qCommunityTb.hits, communityTb.getHits()+1)
                .execute();
    }

    @Override
    @Transactional
    public void updateCommunity(CommunityTb communityTb) {
        QCommunityTb qCommunityTb = QCommunityTb.CommunityTb;

        query.update(qCommunityTb).where(qCommunityTb.id.eq(communityTb.getId()))
                .set(qCommunityTb.title, communityTb.getTitle())
                .set(qCommunityTb.content, communityTb.getContent())
                .execute();
    }


}
