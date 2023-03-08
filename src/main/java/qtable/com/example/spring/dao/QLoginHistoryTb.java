package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QLoginHistoryTb is a Querydsl query type for LoginHistoryTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QLoginHistoryTb extends EntityPathBase<LoginHistoryTb> {

    private static final long serialVersionUID = 1182931845L;

    public static final QLoginHistoryTb loginHistoryTb = new QLoginHistoryTb("loginHistoryTb");

    public final NumberPath<Integer> count = createNumber("count", Integer.class);

    public final StringPath date = createString("date");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public QLoginHistoryTb(String variable) {
        super(LoginHistoryTb.class, forVariable(variable));
    }

    public QLoginHistoryTb(Path<? extends LoginHistoryTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QLoginHistoryTb(PathMetadata metadata) {
        super(LoginHistoryTb.class, metadata);
    }

}

