package com.example.spring.spring.repository.Impl;

import com.example.spring.spring.dao.AskingTb;
import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.repository.AskingRepositoryCustom;
import com.example.spring.spring.repository.CommentTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class AskingTbRepositoryCustomImpl extends QuerydslRepositorySupport implements AskingRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public AskingTbRepositoryCustomImpl() {
        super(AskingTb.class);
    }



}
