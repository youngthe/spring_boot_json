package com.example.spring.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLikeTb is a Querydsl query type for LikeTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLikeTb extends EntityPathBase<LikeTb> {

    private static final long serialVersionUID = -2091557358L;

    public static final QLikeTb likeTb = new QLikeTb("likeTb");

    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);

    public final NumberPath<Integer> likes_id = createNumber("likes_id", Integer.class);

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QLikeTb(String variable) {
        super(LikeTb.class, forVariable(variable));
    }

    public QLikeTb(Path<? extends LikeTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLikeTb(PathMetadata metadata) {
        super(LikeTb.class, metadata);
    }

}

