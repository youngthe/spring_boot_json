package com.example.spring.utils;

import com.example.spring.Abi;
import com.example.spring.dao.CoinPushHistoryTb;
import com.example.spring.dao.LoginHistoryTb;
import com.example.spring.dao.StakingTb;
import com.example.spring.dao.UserTb;
import com.example.spring.dto.CoinpushDto;
import com.example.spring.repository.CoinPushHistoryRepository;
import com.example.spring.repository.LoginHistoryRepository;
import com.example.spring.repository.StakingRepository;
import com.example.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
@Component // 추가

public class ScheduleTest {

    @Autowired
    private StakingRepository stakingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private CoinPushHistoryRepository coinPushHistoryRepository;

//    @Scheduled(fixedRate = 1000000)
    @Scheduled(cron = "0 0 0 * * ?")
    public void scheduleFixedRateWithInitialDelayTask() throws Exception {
        List<StakingTb> stakingTbList = stakingRepository.getStakingTbThatStateTrue();

        for(int i=0;i<stakingTbList.size();i++){

            Date now = new Date();

            if(now.after(stakingTbList.get(i).getExpire_date())){
                    System.out.println("user coin add start");
                    UserTb user = userRepository.getUserTbByUserId(stakingTbList.get(i).getUser_id());
                    double reward_amount = stakingTbList.get(i).getReward_amount();

                    stakingTbList.get(i).setState(false);
                    stakingRepository.save(stakingTbList.get(i));

                    BigDecimal number1 = BigDecimal.valueOf(user.getCoin());
                    BigDecimal number2 = BigDecimal.valueOf(reward_amount);
                    user.setCoin(number1.add(number2).doubleValue());
                    userRepository.save(user);
            }

        }

    }
    //초 분 시 일 월 년
    //00시마다 작동하는 스케쥴러
    @Scheduled(cron = "0 0 0 * * ?")
    public void TimeScaduler() throws Exception {
        Date now = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        System.out.println(format.format(now));

            //금일 방문자 레코드 생성
            LoginHistoryTb loginHistoryTb = new LoginHistoryTb();
            loginHistoryTb.setDate(format.format(now));
            loginHistoryTb.setCount(0);
            loginHistoryRepository.save(loginHistoryTb);

            //스테이킹 이자 지급
            List<StakingTb> stakingTbList = stakingRepository.getStakingTbThatStateTrue();
            for(int i=0;i<stakingTbList.size();i++){
                BigDecimal number1 = BigDecimal.valueOf(stakingTbList.get(i).getStart_amount());
                BigDecimal number2 = new BigDecimal("0.1");
                double add_coin = number1.multiply(number2).doubleValue();
                stakingTbList.get(i).setAdd_amount(add_coin);
                stakingRepository.save(stakingTbList.get(i));

                CoinPushHistoryTb coinPushHistoryTb = new CoinPushHistoryTb();
                coinPushHistoryTb.setCoin(add_coin);
                coinPushHistoryTb.setGiver(0);
                coinPushHistoryTb.setReceiver(stakingTbList.get(i).getUser_id());
                coinPushHistoryTb.setDate(now);
                coinPushHistoryTb.setType(3);
                coinPushHistoryRepository.save(coinPushHistoryTb);

                UserTb userTb = userRepository.getUserTbByUserId(stakingTbList.get(i).getUser_id());

                BigDecimal number3 = BigDecimal.valueOf(add_coin);
                BigDecimal number4 = BigDecimal.valueOf(userTb.getCoin());
                userTb.setCoin(number3.add(number4).doubleValue());
                userRepository.save(userTb);
            }
    }

}
