package com.example.spring.repository.Impl;

import com.example.spring.dao.CategoryTb;
import com.example.spring.repository.CategoryTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class CategoryTbRepositoryImpl extends QuerydslRepositorySupport implements CategoryTbRepositoryCustom {
    @Autowired
    JPAQueryFactory query;

    public CategoryTbRepositoryImpl() {
        super(CategoryTb.class);
    }

}
