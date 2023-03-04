package com.example.spring.domain;

import com.example.spring.dao.LikeTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QLikeTb extends EntityPathBase<LikeTb> {
    public static final QLikeTb like = new QLikeTb("likes");

    public final NumberPath<Integer> likes_id = createNumber("likes_id", Integer.class);
    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);
    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QLikeTb(String variable) {
        super(LikeTb.class, forVariable(variable));
    }

}
