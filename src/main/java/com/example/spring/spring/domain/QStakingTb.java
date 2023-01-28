package com.example.spring.spring.domain;

import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.StakingTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QStakingTb extends EntityPathBase<StakingTb>{

        public static final QStakingTb stakingTb = new QStakingTb("staking");
        public final NumberPath<Integer> staking_id = createNumber("staking_id", Integer.class);
        public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);
        public final StringPath expire_day = createString("expire_day");
        public final StringPath created_day = createString("created_day");
        public final StringPath last_modified_date = createString("last_modified_date");
        public final StringPath name = createString("name");

        public QStakingTb(String variable) {

            super(StakingTb.class, forVariable(variable));

        }
}
