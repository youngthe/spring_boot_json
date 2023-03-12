package com.example.spring.repository;

import com.example.spring.dao.UserTb;

import java.util.List;

public interface UserTbRepositoryCustom {

    public boolean LoginCheck(UserTb userTb);

    public UserTb getUserTbByUserId(int user_id);

    public UserTb getUserTbByAccount(String account);

    public String getNameByAccount(String account);

    public String getNameByPk(int pk);

    public boolean AccountCheck(String account);

    public String getRoleByUserId(int user_id);

    public List<UserTb> getUserTbByRole(String role);
}
