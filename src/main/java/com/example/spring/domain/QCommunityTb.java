package com.example.spring.domain;

import com.example.spring.dao.CommunityTb;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QCommunityTb extends EntityPathBase<com.example.spring.dao.CommunityTb> {
    public static final QCommunityTb CommunityTb = new QCommunityTb("Community");
//    public final StringPath id = createString("id");
    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);
    public final StringPath title = createString("title");
    public final StringPath content = createString("content");
    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);
    public final StringPath date = createString("date");
    public final StringPath category = createString("category");
    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);
    public final NumberPath<Double> get_coin =  createNumber("get_coin", double.class);
    public final BooleanPath highlight = createBoolean("highlight");

    public QCommunityTb(String variable) {

        super(CommunityTb.class, forVariable(variable));

    }

}

