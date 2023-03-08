package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QAskingTb is a Querydsl query type for AskingTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAskingTb extends EntityPathBase<AskingTb> {

    private static final long serialVersionUID = -250351901L;

    public static final QAskingTb askingTb = new QAskingTb("askingTb");

    public final NumberPath<Double> amount = createNumber("amount", Double.class);

    public final NumberPath<Integer> asking_id = createNumber("asking_id", Integer.class);

    public final DatePath<java.time.LocalDate> created_date = createDate("created_date", java.time.LocalDate.class);

    public final BooleanPath input_output = createBoolean("input_output");

    public final BooleanPath status = createBoolean("status");

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QAskingTb(String variable) {
        super(AskingTb.class, forVariable(variable));
    }

    public QAskingTb(Path<? extends AskingTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QAskingTb(PathMetadata metadata) {
        super(AskingTb.class, metadata);
    }

}

