package com.example.spring.spring.domain;

import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.dao.HeartTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QHeartTb extends EntityPathBase<HeartTb> {
    public static final QHeartTb heart = new QHeartTb("heart");

    public final StringPath community_id = createString("community_id");
    public final StringPath writer_id = createString("writer_id");

    public QHeartTb(String variable) {
        super(HeartTb.class, forVariable(variable));
    }

}
