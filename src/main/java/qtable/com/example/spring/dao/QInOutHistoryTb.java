package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QInOutHistoryTb is a Querydsl query type for InOutHistoryTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QInOutHistoryTb extends EntityPathBase<InOutHistoryTb> {

    private static final long serialVersionUID = -1372635387L;

    public static final QInOutHistoryTb inOutHistoryTb = new QInOutHistoryTb("inOutHistoryTb");

    public final NumberPath<Double> coin = createNumber("coin", Double.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final BooleanPath in_out = createBoolean("in_out");

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QInOutHistoryTb(String variable) {
        super(InOutHistoryTb.class, forVariable(variable));
    }

    public QInOutHistoryTb(Path<? extends InOutHistoryTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QInOutHistoryTb(PathMetadata metadata) {
        super(InOutHistoryTb.class, metadata);
    }

}

