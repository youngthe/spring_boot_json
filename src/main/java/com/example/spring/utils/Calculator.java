package com.example.spring.utils;


import java.util.Date;

public class Calculator {

    /** 이자 만기 후에 받는 금액, 단리 **/
    public double getReward(int initialPrice, double interest_rate){

        double totalPrice = initialPrice + (initialPrice*interest_rate);
        return totalPrice;

    }

    /** 중도 해지했을 때 받는 금액 **/
    public double getRewardHalfTermination(int initialPrice, double interest_rate, long startTime){

        //지난 날짜
        Date now = new Date();
        long day = DayParse(now.getTime() - startTime);

        return getRewardAfterN(initialPrice, interest_rate, day);
    }

    /** 1일 지났을 때 받는 금액 **/
    /** 하루당 받는 이자 금액 **/
    public double getRewardOneday(int initialPrice, double interest_rate){

        double payBackAfterOneday = initialPrice + ((initialPrice*interest_rate)/365);
        return payBackAfterOneday;

    }

    /** n일 지났을 때 받는 금액 **/
    public double getRewardAfterN(int initialPrice, double interest_rate, long n){

        double payBackAfterNday = initialPrice + (((initialPrice*interest_rate)/365) * n);
        return payBackAfterNday;

    }

    /** 밀리초 단위를 일 단위로 환산 **/
    public long DayParse(long Time){

        long day = Time/24/60/60/1000;
        return day;

    }


}
