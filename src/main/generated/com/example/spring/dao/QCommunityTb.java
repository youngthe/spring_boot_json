package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommunityTb is a Querydsl query type for CommunityTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommunityTb extends EntityPathBase<CommunityTb> {

    private static final long serialVersionUID = 516238571L;

    public static final QCommunityTb communityTb = new QCommunityTb("communityTb");

    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);

    public final StringPath content = createString("content");

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final StringPath file_name = createString("file_name");

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QCommunityTb(String variable) {
        super(CommunityTb.class, forVariable(variable));
    }

    public QCommunityTb(Path<? extends CommunityTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommunityTb(PathMetadata metadata) {
        super(CommunityTb.class, metadata);
    }

}
