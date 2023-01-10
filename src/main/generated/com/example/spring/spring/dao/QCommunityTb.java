package com.example.spring.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCommunityTb is a Querydsl query type for CommunityTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityTb extends EntityPathBase<CommunityTb> {

    private static final long serialVersionUID = -1586283862L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCommunityTb communityTb = new QCommunityTb("communityTb");

    public final QCommentTb comment;

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final StringPath file_name = createString("file_name");

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath title = createString("title");

    public final QUserTb user;

    public QCommunityTb(String variable) {
        this(CommunityTb.class, forVariable(variable), INITS);
    }

    public QCommunityTb(Path<? extends CommunityTb> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCommunityTb(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCommunityTb(PathMetadata metadata, PathInits inits) {
        this(CommunityTb.class, metadata, inits);
    }

    public QCommunityTb(Class<? extends CommunityTb> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.comment = inits.isInitialized("comment") ? new QCommentTb(forProperty("comment")) : null;
        this.user = inits.isInitialized("user") ? new QUserTb(forProperty("user")) : null;
    }

}

