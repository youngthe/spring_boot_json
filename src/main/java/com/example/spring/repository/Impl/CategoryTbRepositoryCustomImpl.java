package com.example.spring.repository.Impl;

import com.example.spring.dao.CategoryTb;
import com.example.spring.dao.QCategoryTb;
import com.example.spring.repository.CategoryTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class CategoryTbRepositoryCustomImpl extends QuerydslRepositorySupport implements CategoryTbRepositoryCustom {
    @Autowired
    JPAQueryFactory query;

    public CategoryTbRepositoryCustomImpl() {
        super(CategoryTb.class);
    }

    public List<CategoryTb> getCategoryParent(){
        QCategoryTb qCategoryTb = QCategoryTb.categoryTb;

        return query
                .selectFrom(qCategoryTb)
                .where(qCategoryTb.parent.eq(0))
                .fetch();

    }

    public List<CategoryTb> getCategoryChildByParent(int parent){
        QCategoryTb qCategoryTb = QCategoryTb.categoryTb;

        return query
                .selectFrom(qCategoryTb)
                .where(qCategoryTb.parent.eq(parent))
                .fetch();

    }

}
