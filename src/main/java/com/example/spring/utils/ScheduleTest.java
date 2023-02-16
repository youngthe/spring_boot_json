package com.example.spring.utils;

import com.example.spring.dao.StakingTb;
import com.example.spring.dao.UserTb;
import com.example.spring.repository.StakingRepository;
import com.example.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Component // 추가

public class ScheduleTest {

    @Autowired
    private StakingRepository stakingRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateWithInitialDelayTask() {

        List<StakingTb> stakingTbList = stakingRepository.getStakingTbThatStateTrue();

        for(int i=0;i<stakingTbList.size();i++){

            Date now = new Date();

            if(now.after(stakingTbList.get(i).getExpire_date())){
                System.out.println("staking "+ stakingTbList.get(i).getStaking_id() + " is done ");
                UserTb user = userRepository.getUserTbByUserId(stakingTbList.get(i).getUser_id());
                BigDecimal number1 = BigDecimal.valueOf(stakingTbList.get(i).getReward_amount());
                BigDecimal number2 = BigDecimal.valueOf(user.getCoin());
                user.setCoin(number1.add(number2).doubleValue());

                userRepository.save(user);
                stakingTbList.get(i).setState(false);
                stakingRepository.save( stakingTbList.get(i));
            }

        }

    }


}
