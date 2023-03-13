package com.example.spring.dao;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "user")
public class UserTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int user_id;

    @Column(length = 50, nullable = false)
    private String account;

    @Column(length = 200, nullable = false)
    private String pw;

    @Column(length = 20)
    private String name;

    @Column()
    private double coin;

    @Column(length = 20)
    private String role;

    //true 활성화 false 비활성화
    @Column()
    private boolean state;

    @Column()
    private Date create_date;

    @Column()
    private Date last_login_date;

}
