package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCoinPushHistoryTb is a Querydsl query type for CoinPushHistoryTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCoinPushHistoryTb extends EntityPathBase<CoinPushHistoryTb> {

    private static final long serialVersionUID = 1632849611L;

    public static final QCoinPushHistoryTb coinPushHistoryTb = new QCoinPushHistoryTb("coinPushHistoryTb");

    public final NumberPath<Double> coin = createNumber("coin", Double.class);

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final NumberPath<Integer> giver = createNumber("giver", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final NumberPath<Integer> receiver = createNumber("receiver", Integer.class);

    public QCoinPushHistoryTb(String variable) {
        super(CoinPushHistoryTb.class, forVariable(variable));
    }

    public QCoinPushHistoryTb(Path<? extends CoinPushHistoryTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCoinPushHistoryTb(PathMetadata metadata) {
        super(CoinPushHistoryTb.class, metadata);
    }

}

