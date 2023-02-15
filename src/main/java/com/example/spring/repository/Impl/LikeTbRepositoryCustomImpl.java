package com.example.spring.repository.Impl;

import com.example.spring.dao.LikeTb;
import com.example.spring.domain.QLikeTb;
import com.example.spring.repository.LikeTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.transaction.Transactional;

public class LikeTbRepositoryCustomImpl extends QuerydslRepositorySupport implements LikeTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public LikeTbRepositoryCustomImpl() {
        super(LikeTb.class);
    }

    @Transactional
    public boolean deleteByCommunityIdAndWriterId(LikeTb likeTb){

        QLikeTb qLikeTb = QLikeTb.like;

        try{
            query
                    .delete(qLikeTb)
                    .where(qLikeTb.community_id.eq(likeTb.getCommunity_id()))
                    .where(qLikeTb.user_id.eq(likeTb.getUser_id()))
                    .execute();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public LikeTb getLikeByUserIdAndCommunityId(LikeTb likeTb){
        QLikeTb qLikeTb = QLikeTb.like;

        return query
                .selectFrom(qLikeTb)
                .where(qLikeTb.user_id.eq(likeTb.getLike_id()))
                .where(qLikeTb.community_id.eq(likeTb.getCommunity_id()))
                .fetchOne();

    }

    public boolean LikeCheck(LikeTb likeTb){
        QLikeTb qLikeTb = QLikeTb.like;

        if(query
                .selectFrom(qLikeTb)
                .where(qLikeTb.user_id.eq(likeTb.getUser_id()))
                .where(qLikeTb.community_id.eq(likeTb.getCommunity_id()))
                .fetchFirst() == null){
            return false;
        }else{
            return true;
        }
    }

    public int getLikeTotal(int community_id){
        QLikeTb qLikeTb = QLikeTb.like;

        return query
                .selectFrom(qLikeTb)
                .where(qLikeTb.community_id.eq(community_id))
                .fetch().size();

    }

}
