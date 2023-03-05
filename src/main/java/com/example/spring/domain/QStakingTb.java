package com.example.spring.domain;

import com.example.spring.dao.StakingTb;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QStakingTb extends EntityPathBase<StakingTb>{

        public static final QStakingTb stakingTb = new QStakingTb("staking");
        public final NumberPath<Integer> staking_id = createNumber("staking_id", Integer.class);
        public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);
        public final NumberPath<Integer> wallet_id = createNumber("wallet_id", Integer.class);
        public final StringPath expire_date = createString("expire_date");
        public final StringPath created_date = createString("created_date");
        public final StringPath release_date = createString("release_date");
        public final NumberPath<Double> reward_amount =  createNumber("reward_amount", double.class);
        public final StringPath name = createString("name");
        public final NumberPath<Double> start_amount =  createNumber("start_amount", double.class);
        public final NumberPath<Double> percent =  createNumber("percent", double.class);
        public final BooleanPath state = createBoolean("state");
        public final StringPath block_hash = createString("block_hash");
        public final StringPath wallet_address = createString("wallet_address");
        public QStakingTb(String variable) {

            super(StakingTb.class, forVariable(variable));

        }
}
