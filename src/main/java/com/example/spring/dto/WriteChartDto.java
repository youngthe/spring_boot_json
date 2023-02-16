package com.example.spring.dto;

import com.example.spring.dao.TestContent;
import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class WriteChartDto {

    private int id;

    private String date;

    public WriteChartDto(TestContent testContent) {
        this.id = testContent.getCommunity_id();
        SimpleDateFormat format = new SimpleDateFormat("dd");
        this.date = format.format(testContent.getDate());
    }
}
