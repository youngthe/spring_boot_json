package com.example.spring.spring.repository.Impl;

import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.dao.WalletTb;
import com.example.spring.spring.domain.QUserTb;
import com.example.spring.spring.domain.QWalletTb;
import com.example.spring.spring.repository.UserTbRepositoryCustom;
import com.example.spring.spring.repository.WalletTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class WalletTbRepositoryCustomImpl extends QuerydslRepositorySupport implements WalletTbRepositoryCustom {

    public WalletTbRepositoryCustomImpl() {
        super(WalletTb.class);
    }

    @Autowired
    JPAQueryFactory query;


    public String getAddressByUserId(int user_id){


        QWalletTb qWalletTb = QWalletTb.wallet;

        return query.selectFrom(qWalletTb)
                .where(qWalletTb.user_id.eq(user_id))
                .fetch().get(0).getAddress();


    }

}
