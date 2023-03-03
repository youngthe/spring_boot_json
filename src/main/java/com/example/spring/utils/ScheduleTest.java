package com.example.spring.utils;

import com.example.spring.Abi;
import com.example.spring.dao.LoginHistoryTb;
import com.example.spring.dao.StakingTb;
import com.example.spring.dao.UserTb;
import com.example.spring.repository.LoginHistoryRepository;
import com.example.spring.repository.StakingRepository;
import com.example.spring.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;

import java.io.File;
import java.io.IOException;
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

    @Scheduled(fixedRate = 60000)
    public void scheduleFixedRateWithInitialDelayTask() throws Exception {

        List<StakingTb> stakingTbList = stakingRepository.getStakingTbThatStateTrue();

        for(int i=0;i<stakingTbList.size();i++){

            Date now = new Date();

            if(now.after(stakingTbList.get(i).getExpire_date())){
                System.out.println("staking "+ stakingTbList.get(i).getStaking_id() + " is done ");

                if(stakingTbList.get(i).getUser_id() == 0){

                    System.out.println("web3j start");
                    BigDecimal number1 = BigDecimal.valueOf(stakingTbList.get(i).getReward_amount());

                    stakingTbList.get(i).setState(false);
                    stakingRepository.save( stakingTbList.get(i));

                    Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));

                    ContractGasProvider gasProvider = new DefaultGasProvider();
                    BigInteger gasPrice = new BigInteger("750");
                    BigInteger GasPrice = BigInteger.valueOf(25000000000L);
                    BigInteger GasLimit = BigInteger.valueOf(30000000L);
                    EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
                    System.out.println("eth :" + ethGasPrice.getGasPrice());
                    File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
                    Credentials credentials = WalletUtils.loadCredentials("test", file);

                    Abi abi = Abi.load("0xe92C60dfEc285704b8394212faf40C8CDC42997e", web3j, credentials, GasPrice, GasLimit);

                    BigInteger value = Convert.toWei(number1.toString(), Convert.Unit.ETHER).toBigInteger();
                    abi.transfer(stakingTbList.get(i).getWallet_address(), value).send();
                }else{
                    System.out.println("user wallet add start");
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

    }
    //초 분 시 일 월 년
    @Scheduled(cron = "0 0 0 * * ?")
    public void TimeScaduler() throws Exception {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        System.out.println(format.format(date));

            LoginHistoryTb loginHistoryTb = new LoginHistoryTb();
            loginHistoryTb.setDate(format.format(date));
            loginHistoryTb.setCount(0);
            loginHistoryRepository.save(loginHistoryTb);

    }

}
