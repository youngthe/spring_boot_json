package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QHistoryTb is a Querydsl query type for HistoryTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QHistoryTb extends EntityPathBase<HistoryTb> {

    private static final long serialVersionUID = -360185898L;

    public static final QHistoryTb historyTb = new QHistoryTb("historyTb");

    public final DateTimePath<java.util.Date> coin = createDateTime("coin", java.util.Date.class);

    public final DateTimePath<java.util.Date> date = createDateTime("date", java.util.Date.class);

    public final StringPath input_output = createString("input_output");

    public final NumberPath<Integer> transaction_id = createNumber("transaction_id", Integer.class);

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QHistoryTb(String variable) {
        super(HistoryTb.class, forVariable(variable));
    }

    public QHistoryTb(Path<? extends HistoryTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QHistoryTb(PathMetadata metadata) {
        super(HistoryTb.class, metadata);
    }

}

