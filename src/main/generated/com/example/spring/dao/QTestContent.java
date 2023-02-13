package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTestContent is a Querydsl query type for TestContent
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTestContent extends EntityPathBase<TestContent> {

    private static final long serialVersionUID = 1960634427L;

    public static final QTestContent testContent = new QTestContent("testContent");

    public final StringPath category = createString("category");

    public final NumberPath<Integer> community_id = createNumber("community_id", Integer.class);

    public final DatePath<java.time.LocalDate> date = createDate("date", java.time.LocalDate.class);

    public final NumberPath<Double> get_coin = createNumber("get_coin", Double.class);

    public final BooleanPath highlight = createBoolean("highlight");

    public final NumberPath<Integer> hits = createNumber("hits", Integer.class);

    public final StringPath title = createString("title");

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public QTestContent(String variable) {
        super(TestContent.class, forVariable(variable));
    }

    public QTestContent(Path<? extends TestContent> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTestContent(PathMetadata metadata) {
        super(TestContent.class, metadata);
    }

}

