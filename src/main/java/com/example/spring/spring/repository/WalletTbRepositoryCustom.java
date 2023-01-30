package com.example.spring.spring.repository;

import com.example.spring.spring.dao.WalletTb;

public interface WalletTbRepositoryCustom {
    public String getAddressByUserId(int user_id);

    public WalletTb getWalletByWallet_id(int wallet_id);

    public WalletTb getWalletByUser_id(int user_id);

    public boolean walletCheck(int user_id);

}
