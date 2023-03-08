package com.example.spring.dao;

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

    private static final long serialVersionUID = 672716819L;

    public static final QWalletTb walletTb = new QWalletTb("walletTb");

    public final StringPath address = createString("address");

    public final DateTimePath<java.util.Date> created_date = createDateTime("created_date", java.util.Date.class);

    public final DateTimePath<java.util.Date> last_modified_date = createDateTime("last_modified_date", java.util.Date.class);

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

