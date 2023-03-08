package com.example.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCategoryTb is a Querydsl query type for CategoryTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCategoryTb extends EntityPathBase<CategoryTb> {

    private static final long serialVersionUID = 1064653016L;

    public static final QCategoryTb categoryTb = new QCategoryTb("categoryTb");

    public final NumberPath<Integer> category_id = createNumber("category_id", Integer.class);

    public final StringPath category_name = createString("category_name");

    public QCategoryTb(String variable) {
        super(CategoryTb.class, forVariable(variable));
    }

    public QCategoryTb(Path<? extends CategoryTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCategoryTb(PathMetadata metadata) {
        super(CategoryTb.class, metadata);
    }

}

