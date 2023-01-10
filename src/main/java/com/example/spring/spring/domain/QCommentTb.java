package com.example.spring.spring.domain;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.CommunityTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QCommentTb extends EntityPathBase<CommentTb> {
    public static final QCommentTb comments = new QCommentTb("COMMENTS");
    public final NumberPath<Integer> id = createNumber("id", Integer.class);
    public final StringPath writer = createString("writer");
    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);
    public final StringPath comment = createString("comment");
    public final StringPath Date = createString("Date");
    public final StringPath parent = createString("parent");

    public QCommentTb(String variable) {

        super(CommentTb.class, forVariable(variable));

    }
}
