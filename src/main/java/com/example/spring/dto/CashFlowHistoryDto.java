package com.example.spring.dto;

import com.example.spring.dao.CashFlowHistoryTb;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.text.SimpleDateFormat;

@Getter
@Setter
public class CashFlowHistoryDto {

    private int id;

    private int user_id;

    private int state;

    private double coin;

    private String address;

    private String date;

    public CashFlowHistoryDto(CashFlowHistoryTb cashFlowHistoryTb) {
        this.id = cashFlowHistoryTb.getId();
        this.user_id = cashFlowHistoryTb.getUser_id();
        this.state = cashFlowHistoryTb.getState();
        this.coin = cashFlowHistoryTb.getCoin();
        this.address = cashFlowHistoryTb.getTo_address();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.date = format.format(cashFlowHistoryTb.getDate());
    }
}
