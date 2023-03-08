package com.example.spring.dao;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.types.EntityPath;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Columns;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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


}
