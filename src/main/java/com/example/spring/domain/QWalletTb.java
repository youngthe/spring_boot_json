package com.example.spring.domain;

import com.example.spring.dao.WalletTb;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringPath;

import static com.querydsl.core.types.PathMetadataFactory.forVariable;

public class QWalletTb extends EntityPathBase<WalletTb> {

    public static final QWalletTb wallet = new QWalletTb("wallet");

    public final NumberPath<Integer> wallet_id =  createNumber("wallet_id", Integer.class);
    public final NumberPath<Integer> user_id =  createNumber("user_id", Integer.class);
    public final StringPath address = createString("address");
    public final StringPath created_date = createString("created_date");
    public final StringPath last_modified_date = createString("last_modified_date");

    public QWalletTb(String variable) {
        super(WalletTb.class, forVariable(variable));
    }
}
