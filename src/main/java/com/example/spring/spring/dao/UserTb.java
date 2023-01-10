package com.example.spring.spring.dao;


import com.querydsl.core.types.EntityPath;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "USER")
public class UserTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, nullable = false)
    private String account;

    @Column(length = 20, nullable = false)
    private String pw;

    @Column(length = 20)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<CommunityTb> CommunityTb = new ArrayList<CommunityTb>();

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer pk) {
        this.id = pk;
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
