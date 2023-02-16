package com.example.spring.repository.Impl;

import com.example.spring.dao.CoinPushHistoryTb;
import com.example.spring.dao.CommunityTb;
import com.example.spring.domain.QCoinPushHistoryTb;
import com.example.spring.domain.QCommunityTb;
import com.example.spring.domain.QUserTb;
import com.example.spring.repository.CategoryTbRepositoryCustom;
import com.example.spring.repository.CoinPushHistoryRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class CoinPushHistoryRepositoryCustomImpl  extends QuerydslRepositorySupport implements CoinPushHistoryRepositoryCustom {


    @Autowired
    JPAQueryFactory query;

    public CoinPushHistoryRepositoryCustomImpl() {
        super(CoinPushHistoryTb.class);
    }

    public List<CoinPushHistoryTb> getCoinPushHistoryByUserId(int user_id) {


        QCoinPushHistoryTb qCoin = QCoinPushHistoryTb.coinpushhistory;

        return query
                .selectFrom(qCoin)
                .where(qCoin.receiver.eq(user_id))
                .orderBy(qCoin.date.asc())
                .fetch();
    }


}
