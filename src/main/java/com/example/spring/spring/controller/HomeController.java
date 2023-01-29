package com.example.spring.spring.controller;

import com.example.spring.spring.dao.StakingTb;
import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.dao.WalletTb;
import com.example.spring.spring.repository.StakingRepository;
import com.example.spring.spring.repository.UserRepository;
import com.example.spring.spring.repository.WalletRepository;
import com.example.spring.spring.utils.JwtTokenProvider;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
public class HomeController {

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




    @RequestMapping(value = "/")
    public HashMap login(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("message", "hello");
        return result;


    }

    @RequestMapping(value = "/logincheck", method = RequestMethod.POST)
    public HashMap loginCheck(@RequestBody HashMap<String, Object> data){

        HashMap<String, Object> result = new HashMap<>();
        String account = data.get("account").toString();
        String pw = data.get("pw").toString();

        log.info("account : {}", account);

        try{
            UserTb user = userRepository.getUserTbByAccount(account);

            String get_pw = user.getPw();
            if(passwordEncoder.matches(pw, get_pw)){
                result.put("resultCode", "true");
                result.put("jwt", jwtTokenProvider.createToken(user));
                return result;
            }else{
                result.put("message","not exist");
                result.put("resultCode","false");
                return result;
            }

        }catch(Exception e){
            result.put("message","not exist");
            result.put("resultCode", "false");
            return result;
        }
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public HashMap Register(@RequestBody HashMap<String, Object> data){

        HashMap<String, Object> result = new HashMap<>();

        String account = data.get("account").toString();
        String pw = data.get("pw").toString();
        String name = data.get("name").toString();

        log.info("account : {}", account);
        log.info("pw : {}", pw);
        log.info("name : {}", name);

        if(userRepository.AccountCheck(account)) {
            try{
                UserTb userTb = new UserTb();
                userTb.setAccount(account);
                userTb.setPw(passwordEncoder.encode(pw));
                userTb.setName(name);
                userRepository.save(userTb);
                result.put("resultCode", "true");
                return result;
            }catch(Exception e){
                result.put("resultCode", "false");
                return result;
            }
        }else{
            result.put("message", "already");
            result.put("resultCode", "false");
            return result;
        }
    }
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
        StakingTb stakingTb = stakingRepository.getStakingByStakingId(staking_id);
        if(jwtTokenProvider.getUserId(tokenHeader) == stakingTb.getUser_id()){

            double reward = stakingTb.getReward_amount();
            walletTb.setCoin(walletTb.getCoin()+reward);
            walletRepository.save(walletTb);
            stakingRepository.delete(stakingTb);
            result.put("resultCode", "true");

        }else{

            result.put("message", "unauthority");
            result.put("resultCode", "false");
        }

        return result;
    }

}