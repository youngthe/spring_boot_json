package com.example.spring.domain;

import com.example.spring.dao.LoginHistoryTb;
import com.example.spring.dao.StakingTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QLoginHistoryTb  extends EntityPathBase<LoginHistoryTb> {

    public static final QLoginHistoryTb login_history = new QLoginHistoryTb("login_history");

    public final NumberPath<Integer> id = createNumber("id", Integer.class);
    public final StringPath date = createString("date");
    public final NumberPath<Integer> count = createNumber("count", Integer.class);


    public QLoginHistoryTb(String variable) {

        super(LoginHistoryTb.class, forVariable(variable));

    }
}
