package com.example.spring.repository.Impl;

import com.example.spring.dao.CashFlowHistoryTb;
import com.example.spring.dao.QCashFlowHistoryTb;
import com.example.spring.repository.CashFlowHistoryTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class CashFlowHistoryTbRepositoryCustomImpl extends QuerydslRepositorySupport implements CashFlowHistoryTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public CashFlowHistoryTbRepositoryCustomImpl() {
        super(CashFlowHistoryTb.class);
    }

    public List<CashFlowHistoryTb> getInoutHistoryByUserId(int user_id){
        QCashFlowHistoryTb qCashFlowHistoryTb = QCashFlowHistoryTb.cashFlowHistoryTb;

        return
                query
                        .selectFrom(qCashFlowHistoryTb)
                        .where(qCashFlowHistoryTb.user_id.eq(user_id))
                        .fetch();
    }
}
