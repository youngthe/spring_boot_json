package com.example.spring.domain;

import com.example.spring.dao.CategoryTb;
import com.example.spring.dao.CommentLikeTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QCommentLikeTb extends EntityPathBase<CommentLikeTb> {

    public static final QCommentLikeTb qCommentLikeTb = new QCommentLikeTb("commentLikeTb");
    public final NumberPath<Integer> comment_id = createNumber("comment_id", Integer.class);
    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);
    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QCommentLikeTb(String variable) {
        super(CommentLikeTb.class, forVariable(variable));
    }

}
