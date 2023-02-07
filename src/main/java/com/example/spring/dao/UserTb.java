package com.example.spring.dao;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.EntityPath;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class UserTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(length = 20, nullable = false)
    private String account;

    @Column(length = 200, nullable = false)
    private String pw;

    @Column(length = 20)
    private String name;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getUser_id() {
        return user_id;
    }
    public void setUser_id(Integer pk) {
        this.user_id = pk;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }



}
