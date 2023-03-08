package com.example.spring.repository.Impl;

import com.example.spring.dao.QUserTb;
import com.example.spring.dao.UserTb;
import com.example.spring.repository.UserTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class UserTbRepositoryCustomImpl extends QuerydslRepositorySupport implements UserTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public UserTbRepositoryCustomImpl() {
        super(UserTb.class);
    }

    public boolean LoginCheck(UserTb userTb){

        QUserTb qusertb = QUserTb.userTb;

        if(query.selectFrom(qusertb)
                .where(qusertb.account.eq(userTb.getAccount()))
                .where(qusertb.pw.eq(userTb.getPw()))
                .fetch().isEmpty()){

            return false;
        }else{
            return true;
        }

    }

    public boolean AccountCheck(String account){

        QUserTb qusertb = QUserTb.userTb;

        if(query.selectFrom(qusertb)
                .where(qusertb.account.eq(account))
                .fetch().isEmpty()){

            return true;
        }else{
            return false;
        }

    }

    public UserTb getUserTbByAccount(String account){

        QUserTb qusertb = QUserTb.userTb;

        return query
                .selectFrom(qusertb)
                .where(qusertb.account.eq(account))
                .fetchOne();

    }

    public UserTb getUserTbByUserId(int user_id){

        QUserTb qusertb = QUserTb.userTb;

        return query
                .selectFrom(qusertb)
                .where(qusertb.user_id.eq(user_id))
                .fetchOne();

    }

    public String getNameByAccount(String account){

        QUserTb qusertb = QUserTb.userTb;

        return query
                .selectFrom(qusertb)
                .where(qusertb.account.eq(account))
                .fetchOne()
                .getName();
    }

    public String getNameByPk(int id){

        QUserTb qusertb = QUserTb.userTb;

        return query.selectFrom(qusertb)
                .where(qusertb.user_id.eq(id))
                .fetchOne()
                .getName();
    }

    public String getRoleByUserId(int user_id){

        QUserTb qusertb = QUserTb.userTb;

        return query.selectFrom(qusertb)
                .where(qusertb.user_id.eq(user_id))
                .fetchOne().getRole();

    }
}
