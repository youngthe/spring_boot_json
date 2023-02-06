package com.example.spring.spring;

import com.example.spring.spring.dao.AskingTb;
import com.example.spring.spring.dao.CommentTb;
import com.example.spring.spring.dao.CommunityTb;
import com.example.spring.spring.repository.AskingRepository;
import com.example.spring.spring.repository.CommentRepository;
import com.example.spring.spring.repository.CommunityRepository;
import com.example.spring.spring.repository.UserRepository;
import com.example.spring.spring.utils.Calculator;
import com.example.spring.spring.utils.JwtTokenProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SpringBootTest
class ApplicationTests {


    @Autowired
    AskingRepository askingRepository;

    @Test
    void contextLoads() {

        Calculator calculator = new Calculator();
        System.out.println(calculator.getReward(1000, 0.0123746F));
        Date now = new Date();
        Date beforeOneHour = new Date();
        System.out.println(beforeOneHour.getTime());
        long now_time = now.getTime();
        long test = beforeOneHour.getTime() - (2 * 24 * 60 * 60 * 1000);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd / hh-mm");
        System.out.println( format.format(test));
        //하루 추출
        DayParse(now_time-test);
        calculator.getRewardHalfTermination(1000, 0.5, test);
//        calculator.getRewardHalfTermination(1000, 0.05F, );
    }

    //하루 추출, 내림형태, 만약 3일이 되기 1시간 전이면 2일로 나타남
    public void DayParse(long Time){
        long day = Time/24/60/60/1000;
        System.out.println(day);
    }

//    @Test
//    void test(){
//        AskingTb askingTb = new AskingTb();
//        askingTb.setAmount(1);
//        askingRepository.save(askingTb);
//    }

}
