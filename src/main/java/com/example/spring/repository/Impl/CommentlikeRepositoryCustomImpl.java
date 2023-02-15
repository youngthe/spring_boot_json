package com.example.spring.repository.Impl;

import com.example.spring.dao.CommentLikeTb;
import com.example.spring.dao.CommentTb;
import com.example.spring.dao.LikeTb;
import com.example.spring.domain.QCommentLikeTb;
import com.example.spring.domain.QLikeTb;
import com.example.spring.repository.CommentTbRepositoryCustom;
import com.example.spring.repository.CommentlikeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class CommentlikeRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentlikeRepositoryCustom {
    @Autowired
    JPAQueryFactory query;

    public CommentlikeRepositoryCustomImpl() {
        super(CommentLikeTb.class);
    }


    public boolean CommentLikeCheckByAll(CommentLikeTb commentLikeTb){

        QCommentLikeTb qCommentLikeTb = QCommentLikeTb.qCommentLikeTb;

        if(query
                .selectFrom(qCommentLikeTb)
                .where(qCommentLikeTb.comment_id.eq(commentLikeTb.getComment_id()))
                .where(qCommentLikeTb.community_id.eq(commentLikeTb.getCommunity_id()))
                .where(qCommentLikeTb.user_id.eq(commentLikeTb.getUser_id()))
                .fetchFirst() == null){
            return false;
        }else{
            return true;
        }
    }

    public CommentLikeTb getCommentLike(CommentLikeTb commentLikeTb){

        QCommentLikeTb qCommentLikeTb = QCommentLikeTb.qCommentLikeTb;

        return query
                .selectFrom(qCommentLikeTb)
                .where(qCommentLikeTb.comment_id.eq(commentLikeTb.getComment_id()))
                .where(qCommentLikeTb.community_id.eq(commentLikeTb.getCommunity_id()))
                .fetchOne();

    }

    public List<CommentLikeTb> getCommentLikeListByCommentLikeTb(CommentLikeTb commentLikeTb){
        QCommentLikeTb qCommentLikeTb = QCommentLikeTb.qCommentLikeTb;

        return query
                .selectFrom(qCommentLikeTb)
                .where(qCommentLikeTb.community_id.eq(commentLikeTb.getCommunity_id()))
                .where(qCommentLikeTb.user_id.eq(commentLikeTb.getUser_id()))
                .fetch();

    }

    public int getCommentLike_total(int comment_id){

        QCommentLikeTb qCommentLikeTb = QCommentLikeTb.qCommentLikeTb;

        return query
                .selectFrom(qCommentLikeTb)
                .where(qCommentLikeTb.comment_id.eq(comment_id))
                .fetch().size();

    }
}
