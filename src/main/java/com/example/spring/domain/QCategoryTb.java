package com.example.spring.domain;

import com.example.spring.dao.CategoryTb;
import com.example.spring.dao.CommentTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QCategoryTb extends EntityPathBase<CategoryTb> {

    public static final QCommentTb category = new QCommentTb("category");
    public final NumberPath<Integer> category_id = createNumber("category_id", Integer.class);
    public final StringPath category_name = createString("category_name");

    public QCategoryTb(String variable) {
        super(CategoryTb.class, forVariable(variable));
    }

}
