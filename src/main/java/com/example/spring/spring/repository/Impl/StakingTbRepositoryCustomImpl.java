package com.example.spring.spring.repository.Impl;


import com.example.spring.spring.dao.StakingTb;
import com.example.spring.spring.domain.QStakingTb;
import com.example.spring.spring.repository.StakingRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class StakingTbRepositoryCustomImpl extends QuerydslRepositorySupport implements StakingRepositoryCustom {

    @Autowired
    JPAQueryFactory query;

    public StakingTbRepositoryCustomImpl() {
        super(StakingTb.class);
    }


    public StakingTb getStakingByStakingId(int staking_id){

        QStakingTb qStakingTb = QStakingTb.stakingTb;

        return query.selectFrom(qStakingTb)
                .where(qStakingTb.staking_id.eq(staking_id))
                .fetchOne();


    }
}
