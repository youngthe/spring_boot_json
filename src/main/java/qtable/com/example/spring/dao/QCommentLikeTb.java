package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentLikeTb is a Querydsl query type for CommentLikeTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentLikeTb extends EntityPathBase<CommentLikeTb> {

    private static final long serialVersionUID = 1051882328L;

    public static final QCommentLikeTb commentLikeTb = new QCommentLikeTb("commentLikeTb");

    public final NumberPath<Integer> comment_id = createNumber("comment_id", Integer.class);

    public final NumberPath<Integer> commentlike_id = createNumber("commentlike_id", Integer.class);

    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QCommentLikeTb(String variable) {
        super(CommentLikeTb.class, forVariable(variable));
    }

    public QCommentLikeTb(Path<? extends CommentLikeTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentLikeTb(PathMetadata metadata) {
        super(CommentLikeTb.class, metadata);
    }

}

