package com.example.spring.dto;

import com.example.spring.dao.CoinPushHistoryTb;
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
public class CoinpushDto {

    private int id;

    private String date;

    private int giver;

    private int receiver;

    private double coin;

    public CoinpushDto(CoinPushHistoryTb coinPushHistoryTb) {
        this.id = coinPushHistoryTb.getId();
        SimpleDateFormat format = new SimpleDateFormat("dd");
        this.date = format.format(coinPushHistoryTb.getDate());
        this.giver = coinPushHistoryTb.getGiver();
        this.receiver = coinPushHistoryTb.getReceiver();
        this.coin = coinPushHistoryTb.getCoin();
    }
}
