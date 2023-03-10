package com.example.spring.repository.Impl;

import com.example.spring.dao.QWalletTb;
import com.example.spring.dao.WalletTb;
import com.example.spring.repository.WalletTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class WalletTbRepositoryCustomImpl extends QuerydslRepositorySupport implements WalletTbRepositoryCustom {

    public WalletTbRepositoryCustomImpl() {
        super(WalletTb.class);
    }

    @Autowired
    JPAQueryFactory query;

    public String getAddressByUserId(int user_id){

        QWalletTb qWalletTb = QWalletTb.walletTb;

        return query.selectFrom(qWalletTb)
                .where(qWalletTb.user_id.eq(user_id))
                .fetch().get(0).getAddress();

    }

    public boolean walletCheck(int user_id){

        QWalletTb qWalletTb = QWalletTb.walletTb;

        if(query.selectFrom(qWalletTb)
                .where(qWalletTb.user_id.eq(user_id))
                .fetch().isEmpty()){

            return true;
        }else{
            return false;
        }

    }

    public WalletTb getWalletByWallet_id(int wallet_id){

        QWalletTb qWalletTb = QWalletTb.walletTb;

        return query.selectFrom(qWalletTb)
                .where(qWalletTb.wallet_id.eq(wallet_id))
                .fetch().get(0);

    }

    @Override
    public List<WalletTb> getWalletByUser_id(int user_id) {

        QWalletTb qWalletTb = QWalletTb.walletTb;

        return query.selectFrom(qWalletTb)
                .where(qWalletTb.user_id.eq(user_id))
                .fetch();

    }
}
