package com.example.spring.domain;

import com.example.spring.dao.AskingTb;
import com.example.spring.dao.CategoryTb;
import com.example.spring.dao.CoinPushHistoryTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QCoinPushHistoryTb extends EntityPathBase<CoinPushHistoryTb> {

    public static final QCoinPushHistoryTb coinpushhistory = new QCoinPushHistoryTb("CoinpushHistory");
    public final NumberPath<Integer> id = createNumber("id", Integer.class);
    public final NumberPath<Integer> giver = createNumber("giver", Integer.class);
    public final NumberPath<Integer> receiver = createNumber("receiver", Integer.class);
    public final NumberPath<Double> coin =  createNumber("coin", double.class);
    public final StringPath date = createString("date");

    public QCoinPushHistoryTb(String variable) {
        super(CoinPushHistoryTb.class, forVariable(variable));
    }
}
