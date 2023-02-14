package com.example.spring.dao;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "commentlike")

public class CommentLikeTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentlike_id;

    @Column(name = "comment_id")
    private int comment_id;

    @Column(name = "community_id")
    private int community_id;

    @Column(name = "user_id")
    private int user_id;
}
