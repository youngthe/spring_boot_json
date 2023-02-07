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

    public final DatePath<java.time.LocalDate> created_date = createDate("created_date", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> expire_date = createDate("expire_date", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> last_modified_date = createDate("last_modified_date", java.time.LocalDate.class);

    public final StringPath name = createString("name");

    public final NumberPath<Double> reward_amount = createNumber("reward_amount", Double.class);

    public final NumberPath<Integer> staking_id = createNumber("staking_id", Integer.class);

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public final NumberPath<Integer> wallet_id = createNumber("wallet_id", Integer.class);

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

