package com.example.spring.domain;

import com.example.spring.dao.UserTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QUserTb extends EntityPathBase<UserTb> {

    public static final QUserTb user = new QUserTb("user");

    public final NumberPath<Integer> user_id =  createNumber("user_id", Integer.class);;
    public final StringPath account = createString("account");
    public final StringPath pw = createString("pw");

    public final StringPath name = createString("name");


    public QUserTb(String variable) {
        super(UserTb.class, forVariable(variable));
    }

}
