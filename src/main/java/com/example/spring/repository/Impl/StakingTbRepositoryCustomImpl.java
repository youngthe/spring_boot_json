package com.example.spring.repository.Impl;


import com.example.spring.dao.StakingTb;
import com.example.spring.domain.QStakingTb;
import com.example.spring.repository.StakingTbRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class StakingTbRepositoryCustomImpl extends QuerydslRepositorySupport implements StakingTbRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public StakingTbRepositoryCustomImpl() {
        super(StakingTb.class);
    }


    public StakingTb getStakingTbByStakingId(int staking_id){

        QStakingTb qStakingTb = QStakingTb.stakingTb;

        return query.selectFrom(qStakingTb)
                .where(qStakingTb.staking_id.eq(staking_id))
                .fetchOne();


    }

    public StakingTb getStakingTbByUserId(int user_id){

        QStakingTb qStakingTb = QStakingTb.stakingTb;

        return query.selectFrom(qStakingTb)
                .where(qStakingTb.user_id.eq(user_id))
                .fetchOne();


    }
}