package com.example.spring.spring.controller;

import com.example.spring.spring.dao.StakingTb;
import com.example.spring.spring.dao.WalletTb;
import com.example.spring.spring.repository.StakingRepository;
import com.example.spring.spring.repository.UserRepository;
import com.example.spring.spring.repository.WalletRepository;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

@RestController
public class WalletController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private StakingRepository stakingRepository;


    @RequestMapping(value = "/wallet/add", method = RequestMethod.POST)
    public HashMap wallet_add(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        String address = data.get("address").toString();

        try{

            LocalDate now = LocalDate.now();
            WalletTb walletTb = new WalletTb();
            walletTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            walletTb.setAddress(address);
            walletTb.setCreated_date(now);
            walletTb.setLast_modified_date(now);
            walletTb.setCoin(0);
            walletRepository.save(walletTb);
            result.put("resultCode", "true");

        }catch (Exception e){
            result.put("resultCode", "false");
        }

        return result;
    }

    @RequestMapping(value = "/wallet/modify", method = RequestMethod.POST)
    public HashMap wallet_modify(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int wallet_id = Integer.parseInt(data.get("wallet_id").toString());
        String wallet_address = data.get("address").toString();

        try{
            WalletTb walletTb = walletRepository.getWalletByWallet_id(wallet_id);

            if(walletTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader)){
                walletTb.setAddress(wallet_address);
                walletRepository.save(walletTb);
                result.put("resultCode", "true");
            }else{
                result.put("message","unauthorized");
                result.put("resultCode", "false");
            }

        } catch (Exception e){
            result.put("resultCode", "false");
        }

        return result;
    }

    @RequestMapping(value = "/staking/add", method = RequestMethod.POST)
    public HashMap staking_add(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int coin = Integer.parseInt(data.get("coin").toString());

        //지갑을 선택해서 지갑에 있는 돈으로 스테이킹
        int wallet_id = Integer.parseInt(data.get("wallet_id").toString());
        String name = data.get("name").toString();

        // ex 스테이킹한 금액의 0.05센트를 1년 경과 시 지급
        double interest_rate = 0.05;

        WalletTb walletTb = walletRepository.getWalletByWallet_id(wallet_id);

        if(walletTb.getCoin() >= coin){

            double total = walletTb.getCoin() - coin;
            walletTb.setCoin(total);
            walletRepository.save(walletTb);

            StakingTb stakingTb = new StakingTb();
            LocalDate now = LocalDate.now();
            LocalDate afterOneYear = now.plusYears(1);
            Date date = new Date();

            stakingTb.setWallet_id(wallet_id);
            stakingTb.setName(name);
            stakingTb.setExpire_date(afterOneYear);
            stakingTb.setCreated_date(now);
            stakingTb.setReward_amount(coin);
            stakingTb.setLast_modified_date(now);
            stakingTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));

            stakingRepository.save(stakingTb);

            result.put("resultCode", "true");

        }else{
            result.put("message", "over");
            result.put("resultCode", "false");
        }

        return result;
    }

    @RequestMapping(value = "/staking/cancel", method = RequestMethod.POST)
    public HashMap staking_cancel(@RequestBody HashMap<String, Object> data, @RequestHeader("jwt") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int staking_id = Integer.parseInt(data.get("staking_id").toString());
        int wallet_id = Integer.parseInt(data.get("wallet_id").toString());

        WalletTb walletTb = walletRepository.getWalletByWallet_id(wallet_id);
//        StakingTb stakingTb = stakingRepository.getStakingTbByStakingId(staking_id);
//        if(jwtTokenProvider.getUserId(tokenHeader) == stakingTb.getUser_id()){

//            double reward = stakingTb.getReward_amount();
//            walletTb.setCoin(walletTb.getCoin()+reward);
//            walletRepository.save(walletTb);
//            stakingRepository.delete(stakingTb);
//            result.put("resultCode", "true");

//        }else{
//
//            result.put("message", "unauthority");
//            result.put("resultCode", "false");
//        }

        return result;
    }
}
