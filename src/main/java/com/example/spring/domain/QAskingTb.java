package com.example.spring.domain;

import com.example.spring.dao.AskingTb;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QAskingTb extends EntityPathBase<AskingTb> {

    public static final QAskingTb asking = new QAskingTb("asking");
    public final NumberPath<Integer> asking_id = createNumber("stakingId", Integer.class);
    public final NumberPath<Integer> user_id = createNumber("userId", Integer.class);
    public final NumberPath<Integer> community_id = createNumber("status", Integer.class);
    public final StringPath created_date = createString("created_date");
    public final BooleanPath status = createBoolean("status");
    public final BooleanPath input_output = createBoolean("input_output");

    public QAskingTb(String variable) {

        super(AskingTb.class, forVariable(variable));

    }

}
