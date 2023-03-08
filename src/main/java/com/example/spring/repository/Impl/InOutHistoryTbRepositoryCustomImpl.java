package com.example.spring.repository.Impl;

import com.example.spring.dao.InOutHistoryTb;
import com.example.spring.dao.QInOutHistoryTb;
import com.example.spring.repository.InOutHistoryTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class InOutHistoryTbRepositoryCustomImpl extends QuerydslRepositorySupport implements InOutHistoryTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public InOutHistoryTbRepositoryCustomImpl() {
        super(InOutHistoryTb.class);
    }

    public List<InOutHistoryTb> getInoutHistoryByUserId(int user_id){
        QInOutHistoryTb qInOutHistoryTb = QInOutHistoryTb.inOutHistoryTb;

        return
                query
                        .selectFrom(qInOutHistoryTb)
                        .where(qInOutHistoryTb.user_id.eq(user_id))
                        .fetch();
    }
}
