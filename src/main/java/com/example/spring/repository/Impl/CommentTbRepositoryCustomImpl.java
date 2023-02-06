package com.example.spring.repository.Impl;

import com.example.spring.dao.CommentTb;
import com.example.spring.domain.QCommentTb;
import com.example.spring.repository.CommentTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.transaction.Transactional;
import java.util.List;

public class CommentTbRepositoryCustomImpl extends QuerydslRepositorySupport implements CommentTbRepositoryCustom {


    @Autowired
    JPAQueryFactory query;

    public CommentTbRepositoryCustomImpl() {
        super(CommentTb.class);
    }


    @Override
    public List<CommentTb> getCommentList(int Community_id){

        QCommentTb qCommentTb = QCommentTb.comments;

        return query
                .selectFrom(qCommentTb)
                .where(qCommentTb.community_id.eq(Community_id))
                .orderBy(qCommentTb.parent.asc())
                .fetch();

    }

    public CommentTb getCommentByCommentId(int commentId){
        QCommentTb qCommentTb = QCommentTb.comments;

        return query
                .selectFrom(qCommentTb)
                .where(qCommentTb.id.eq(commentId))
                .fetchOne();

    }
    @Override
    public int getCommunityIdByCommentId(int CommentId){

        QCommentTb qCommentTb = QCommentTb.comments;

        return (int) query
                .selectFrom(qCommentTb)
                .where(qCommentTb.id.eq(CommentId))
                .fetch().get(0).getCommunity_id();

    }


    @Override
    @Transactional
    public void deleteByCommunityId(int community_id){

        QCommentTb qCommentTb = QCommentTb.comments;

        query
                .delete(qCommentTb)
                .where(qCommentTb.community_id.eq(community_id))
                .execute();
    }

    @Override
    @Transactional
    public void deleteByParent(int parent_id){

        QCommentTb qCommentTb = QCommentTb.comments;

        query
                .delete(qCommentTb)
                .where(qCommentTb.parent.eq(parent_id))
                .execute();
    }

    @Override
    @Transactional
    public void deleteComment(int comment_id){

        QCommentTb qCommentTb = QCommentTb.comments;

        query
                .delete(qCommentTb)
                .where(qCommentTb.id.eq(comment_id))
                .execute();
    }

    public List<CommentTb> getCommentByCommunityId(int community_id){
        QCommentTb qCommentTb = QCommentTb.comments;

        return query.selectFrom(qCommentTb).where(qCommentTb.community_id.eq(community_id)).fetch();
    }

//    public List<CommentTb> getChildCommentByParentComment(int commentId){
//        QCommentTb qCommentTb = QCommentTb.comments;
//
//        return query
//                .selectFrom(qCommentTb)
////                .where(qCommentTb.)
//                .fetch();
//    }

}
