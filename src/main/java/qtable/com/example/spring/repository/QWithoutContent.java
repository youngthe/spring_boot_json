package com.example.spring.repository;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.example.spring.repository.QWithoutContent is a Querydsl Projection type for WithoutContent
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QWithoutContent extends ConstructorExpression<WithoutContent> {

    private static final long serialVersionUID = 968563461L;

    public QWithoutContent(com.querydsl.core.types.Expression<Integer> community_id, com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<java.time.LocalDate> date, com.querydsl.core.types.Expression<Integer> hits, com.querydsl.core.types.Expression<Integer> user_id, com.querydsl.core.types.Expression<String> type, com.querydsl.core.types.Expression<Boolean> highlight, com.querydsl.core.types.Expression<Double> get_coin) {
        super(WithoutContent.class, new Class<?>[]{int.class, String.class, java.time.LocalDate.class, int.class, int.class, String.class, boolean.class, double.class}, community_id, title, date, hits, user_id, type, highlight, get_coin);
    }

}

