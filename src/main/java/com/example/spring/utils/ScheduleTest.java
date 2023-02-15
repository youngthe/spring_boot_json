package com.example.spring.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component // 추가

public class ScheduleTest {


//    @Scheduled(cron = "0 18 10 15 * ?")
//    public void scheduleTaskUsingCronExpression() {
//        long now = System.currentTimeMillis() / 1000;
//        log.info("schedule tasks using cron jobs - {}", now);
//    }

//    @Scheduled(fixedRate = 10000)
//    public void scheduleFixedRateWithInitialDelayTask() {
//        long now = System.currentTimeMillis() / 1000;
//        log.info("Fixed rate task with one second initial delay -{}", now);
//    }


}
