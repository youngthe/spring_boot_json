package com.example.spring.repository.Impl;

import com.example.spring.dao.LoginHistoryTb;
import com.example.spring.dao.QLoginHistoryTb;
import com.example.spring.repository.LoginHistoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.transaction.Transactional;
import java.util.List;

public class LoginHistoryRepositoryCustomImpl  extends QuerydslRepositorySupport implements LoginHistoryRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public LoginHistoryRepositoryCustomImpl() {
        super(LoginHistoryTb.class);
    }

    public List<LoginHistoryTb> getUserHistory(){
        QLoginHistoryTb qLoginHistoryTb = QLoginHistoryTb.loginHistoryTb;

        return query.selectFrom(qLoginHistoryTb)
                .fetch();

    }

    public int getCountByDate(String date){
        QLoginHistoryTb qLoginHistoryTb = QLoginHistoryTb.loginHistoryTb;

        return query.selectFrom(qLoginHistoryTb)
                .where(qLoginHistoryTb.date.eq(date))
                .fetchFirst().getCount();

    }
    @Transactional
    public void setIncreaseCount(String date){
        QLoginHistoryTb qLoginHistoryTb = QLoginHistoryTb.loginHistoryTb;

        query.update(qLoginHistoryTb)
                .where(qLoginHistoryTb.date.eq(date))
                .set(qLoginHistoryTb.count, qLoginHistoryTb.count.add(1))
                .execute();

    }

}
