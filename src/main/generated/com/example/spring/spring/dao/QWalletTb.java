package com.example.spring.spring.dao;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWalletTb is a Querydsl query type for WalletTb
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWalletTb extends EntityPathBase<WalletTb> {

    private static final long serialVersionUID = -720323916L;

    public static final QWalletTb walletTb = new QWalletTb("walletTb");

    public final StringPath address = createString("address");

    public final NumberPath<Double> coin = createNumber("coin", Double.class);

    public final DatePath<java.time.LocalDate> created_date = createDate("created_date", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> last_modified_date = createDate("last_modified_date", java.time.LocalDate.class);

    public final NumberPath<Integer> user_id = createNumber("user_id", Integer.class);

    public final NumberPath<Integer> wallet_id = createNumber("wallet_id", Integer.class);

    public QWalletTb(String variable) {
        super(WalletTb.class, forVariable(variable));
    }

    public QWalletTb(Path<? extends WalletTb> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWalletTb(PathMetadata metadata) {
        super(WalletTb.class, metadata);
    }

}

