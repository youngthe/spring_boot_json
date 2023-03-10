package com.example.spring.dto;

import com.example.spring.dao.InOutHistoryTb;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@Setter
public class InOutHistoryDto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "in_out")
    private boolean in_out;

    @Column(name = "coin")
    private double coin;

    @Column(name = "address")
    private String address;

    @Column(name = "date")
    private String date;

    public InOutHistoryDto(InOutHistoryTb inOutHistoryTb) {
        this.id = inOutHistoryTb.getId();
        this.user_id = inOutHistoryTb.getUser_id();
        this.in_out = inOutHistoryTb.isIn_out();
        this.coin = inOutHistoryTb.getCoin();
        this.address = inOutHistoryTb.getAddress();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = format.format(inOutHistoryTb.getDate());
    }
}
