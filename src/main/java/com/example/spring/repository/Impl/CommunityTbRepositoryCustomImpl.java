package com.example.spring.repository.Impl;

import com.example.spring.dao.QCommunityTb;
import com.example.spring.dao.TestContent;
import com.example.spring.dao.CommunityTb;
import com.example.spring.domain.QCommunityTbWithoutContent;
import com.example.spring.repository.CommunityTbRepositoryCustom;
import com.querydsl.core.Tuple;
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

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

         return
                 query
                .selectFrom(qCommunityTb)
                .orderBy(qCommunityTb.community_id.desc())
                .fetch();
    }

    public List<Tuple> getCommunityBySearch(String title){

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        return query
                .select(qCommunityTb.community_id, qCommunityTb.title, qCommunityTb.hits,qCommunityTb.category, qCommunityTb.date,
                        qCommunityTb.highlight, qCommunityTb.user_id, qCommunityTb.get_coin, qCommunityTb.state)
                .where(qCommunityTb.title.startsWith(title))
                .orderBy(qCommunityTb.community_id.desc())
                .fetch();
    }

    @Override
    public CommunityTb getCommunityById(int Community_id){

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        return query
                .selectFrom(qCommunityTb)
                .where(qCommunityTb.community_id.eq(Community_id))
                .fetchFirst();

    }


    @Transactional
    public void Increase_like(CommunityTb communityTb){

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        query.update(qCommunityTb)
                .where(qCommunityTb.community_id.eq(communityTb.getCommunity_id()))
                .set(qCommunityTb.hits, communityTb.getHits()+1)
                .execute();
    }

    @Override
    @Transactional
    public void updateCommunity(CommunityTb communityTb) {
        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        query.update(qCommunityTb).where(qCommunityTb.community_id.eq(communityTb.getCommunity_id()))
                .set(qCommunityTb.title, communityTb.getTitle())
                .set(qCommunityTb.content, communityTb.getContent())
                .execute();
    }

    public List<TestContent> getCommunityByCategory(String category){
        QCommunityTbWithoutContent qCommunityTb = QCommunityTbWithoutContent.CommunityTb;

        return query
                .selectFrom(qCommunityTb)
                .where(qCommunityTb.category.eq(category))
                .where(qCommunityTb.state.eq(true))
                .orderBy(qCommunityTb.community_id.desc())
                .fetch();
    }


    public TestContent getCommunBylast(){
        QCommunityTbWithoutContent qCommunityTb = QCommunityTbWithoutContent.CommunityTb;

        return query
                .selectFrom(qCommunityTb)
                .orderBy(qCommunityTb.community_id.desc())
                .fetchFirst();
    }

    public List<TestContent> getCommunityByUserId(int user_id){

        QCommunityTbWithoutContent qCommunityTb = QCommunityTbWithoutContent.CommunityTb;

        return query
                .selectFrom(qCommunityTb)
                .where(qCommunityTb.user_id.eq(user_id))
                .orderBy(qCommunityTb.community_id.desc())
                .fetch();

    }

    public List<CommunityTb> getCommunityListByUserId(int user_id){

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        return query
                .selectFrom(qCommunityTb)
                .where(qCommunityTb.user_id.eq(user_id))
                .orderBy(qCommunityTb.community_id.desc())
                .fetch();

    }

    public List<CommunityTb> getCommunityBylimit(int limit){

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        return
                query
                        .selectFrom(qCommunityTb)
                        .orderBy(qCommunityTb.community_id.desc())
                        .limit(limit)
                        .fetch();
    }

    public List<TestContent> getCommunityListByUserIdAndDate(int user_id){

        QCommunityTbWithoutContent qCommunityTb = QCommunityTbWithoutContent.CommunityTb;

        return
                query
                        .selectFrom(qCommunityTb)
                        .where(qCommunityTb.user_id.eq(user_id))
                        .orderBy(qCommunityTb.date.asc())
                        .fetch();
    }

    public List<CommunityTb> getPoppularCommunityBylimit(int limit){

        QCommunityTb qCommunityTb = QCommunityTb.communityTb;

        return
                query
                        .selectFrom(qCommunityTb)
                        .orderBy(qCommunityTb.total_reward.desc())
                        .limit(limit)
                        .fetch();
    }


}
