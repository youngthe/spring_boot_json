package com.example.spring.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHeartTb is a Querydsl query type for HeartTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHeartTb extends EntityPathBase<HeartTb> {

    private static final long serialVersionUID = 207849799L;

    public static final QHeartTb heartTb = new QHeartTb("heartTb");

    public final StringPath community_id = createString("community_id");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> writer_id = createNumber("writer_id", Integer.class);

    public QHeartTb(String variable) {
        super(HeartTb.class, forVariable(variable));
    }

    public QHeartTb(Path<? extends HeartTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHeartTb(PathMetadata metadata) {
        super(HeartTb.class, metadata);
    }

}

