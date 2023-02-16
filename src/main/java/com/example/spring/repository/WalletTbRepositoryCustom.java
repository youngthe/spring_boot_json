package com.example.spring.repository;

import com.example.spring.dao.WalletTb;

import java.util.List;

public interface WalletTbRepositoryCustom {
    public String getAddressByUserId(int user_id);

    public WalletTb getWalletByWallet_id(int wallet_id);

    public List<WalletTb> getWalletByUser_id(int user_id);

    public boolean walletCheck(int user_id);

}
