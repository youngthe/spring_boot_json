package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStakingTb is a Querydsl query type for StakingTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStakingTb extends EntityPathBase<StakingTb> {

    private static final long serialVersionUID = -129808327L;

    public static final QStakingTb stakingTb = new QStakingTb("stakingTb");

    public final NumberPath<Double> add_amount = createNumber("add_amount", Double.class);

    public final StringPath block_hash = createString("block_hash");

    public final DateTimePath<java.util.Date> created_date = createDateTime("created_date", java.util.Date.class);

    public final DateTimePath<java.util.Date> expire_date = createDateTime("expire_date", java.util.Date.class);

    public final StringPath name = createString("name");

    public final NumberPath<Double> percent = createNumber("percent", Double.class);

    public final DateTimePath<java.util.Date> release_date = createDateTime("release_date", java.util.Date.class);

    public final NumberPath<Double> reward_amount = createNumber("reward_amount", Double.class);

    public final NumberPath<Integer> staking_id = createNumber("staking_id", Integer.class);

    public final NumberPath<Double> start_amount = createNumber("start_amount", Double.class);

    public final BooleanPath state = createBoolean("state");

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public final StringPath wallet_address = createString("wallet_address");

    public QStakingTb(String variable) {
        super(StakingTb.class, forVariable(variable));
    }

    public QStakingTb(Path<? extends StakingTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStakingTb(PathMetadata metadata) {
        super(StakingTb.class, metadata);
    }

}

