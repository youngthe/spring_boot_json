package com.example.spring.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentTb is a Querydsl query type for CommentTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentTb extends EntityPathBase<CommentTb> {

    private static final long serialVersionUID = 479671392L;

    public static final QCommentTb commentTb = new QCommentTb("commentTb");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> community_id = createNumber("community_id", Long.class);

    public final StringPath date = createString("date");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> ref = createNumber("ref", Integer.class);

    public QCommentTb(String variable) {
        super(CommentTb.class, forVariable(variable));
    }

    public QCommentTb(Path<? extends CommentTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentTb(PathMetadata metadata) {
        super(CommentTb.class, metadata);
    }

}

