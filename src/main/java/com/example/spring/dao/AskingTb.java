package com.example.spring.dao;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "asking")
public class AskingTb {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int asking_id;

    @Column(name = "coin")
    private double coin;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "asking_time")
    private Date asking_time;

    @Column(name = "completed_time")
    private Date completed_time;

    //0. 요청 승인 전 상태, 1. 승인 정상 완료 2. 취소(요청 거절) 3. 직접 입금, 4. 강제 입금 또는 출금
    @Column(name = "status")
    private int status;

    //출금 false, 입금 true
    @Column(name = "input_output")
    private boolean input_output;

    @Column(name = "address")
    private String address;

}
