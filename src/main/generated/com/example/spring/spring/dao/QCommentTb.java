package com.example.spring.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommentTb is a Querydsl query type for CommentTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentTb extends EntityPathBase<CommentTb> {

    private static final long serialVersionUID = 479671392L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommentTb commentTb = new QCommentTb("commentTb");

    public final StringPath comment = createString("comment");

    public final NumberPath<Long> community_id = createNumber("community_id", Long.class);

    public final StringPath date = createString("date");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final QCommentTb parent;

    public QCommentTb(String variable) {
        this(CommentTb.class, forVariable(variable), INITS);
    }

    public QCommentTb(Path<? extends CommentTb> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommentTb(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommentTb(PathMetadata metadata, PathInits inits) {
        this(CommentTb.class, metadata, inits);
    }

    public QCommentTb(Class<? extends CommentTb> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.parent = inits.isInitialized("parent") ? new QCommentTb(forProperty("parent"), inits.get("parent")) : null;
    }

}

