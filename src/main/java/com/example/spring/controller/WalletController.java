package com.example.spring.controller;

import com.example.spring.dao.AskingTb;
import com.example.spring.dao.StakingTb;
import com.example.spring.dao.UserTb;
import com.example.spring.dao.WalletTb;
import com.example.spring.dto.StakingDto;
import com.example.spring.repository.AskingRepository;
import com.example.spring.repository.StakingRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.repository.WalletRepository;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@RestController
public class WalletController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private StakingRepository stakingRepository;

    @Autowired
    private AskingRepository askingRepository;

    @ApiOperation(value = "지갑 추가", notes = "지갑 주소 등록하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "지갑 주소", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet", method = RequestMethod.POST)
    public HashMap wallet_add(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("address"))){
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        String address = data.get("address").toString();

        try {

            Date now = new Date();
            WalletTb walletTb = new WalletTb();
            walletTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            walletTb.setAddress(address);
            walletTb.setCreated_date(now);
            walletTb.setLast_modified_date(now);
            walletRepository.save(walletTb);
            result.put("wallet_id", walletTb.getWallet_id());
            result.put("resultCode", "true");

        } catch (Exception e) {
            result.put("resultCode", "false");
        }

        return result;
    }

    @ApiOperation(value = "지갑 수정", notes = "지갑 주소 수정하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "지갑 주소", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet/{wallet_id}", method = RequestMethod.PATCH)
    public HashMap wallet_modify(@RequestBody HashMap<String, Object> data, @PathVariable ("wallet_id") int wallet_id, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();
        
        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("address"))){
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        String wallet_address = data.get("address").toString();

        try {
            WalletTb walletTb = walletRepository.getWalletByWallet_id(wallet_id);

            if (walletTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader)) {
                walletTb.setAddress(wallet_address);
                walletRepository.save(walletTb);
                result.put("resultCode", "true");
            } else {
                result.put("message", "unauthorized");
                result.put("resultCode", "false");
            }

        } catch (Exception e) {
            result.put("resultCode", "false");
        }

        return result;
    }

    @ApiOperation(value = "지갑 확인", notes = "내가 가진 지갑 주소 확인하기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet", method = RequestMethod.GET)
    public HashMap wallet_view(@RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        try {
            List<WalletTb> wallet = walletRepository.getWalletByUser_id(jwtTokenProvider.getUserId(tokenHeader));
            result.put("wallet", wallet);
            result.put("resultCode", "true");
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", "false");
        }

        return result;
    }

    @ApiOperation(value = "스테이킹 추가", notes = "코인으로 스테이킹 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin", value = "스테이킹할 코인 갯수", required = true),
            @ApiImplicitParam(name = "id", value = "지갑 uuid", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking", method = RequestMethod.POST)
    public HashMap staking_add(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("coin"))){
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("id"))){
            result.put("message", "id is null");
            result.put("resultCode", "false");
            return result;
        }


        double coin = Double.parseDouble(data.get("coin").toString());
        String id = data.get("id").toString();

        if(coin <= 0){
            result.put("message", "can't less than 0");
            result.put("resultCode", "false");
            return result;
        }


        // ex 스테이킹한 금액의 0.05센트를 1년 경과 시 지급
        double interest_rate = 0.05;

        try{

            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            if (user.getCoin() >= coin) {

                BigDecimal number1 = BigDecimal.valueOf(user.getCoin());
                BigDecimal number2 = BigDecimal.valueOf(coin);

                user.setCoin(number1.subtract(number2).doubleValue());
                userRepository.save(user);

                StakingTb stakingTb = new StakingTb();
                Date now = new Date();

                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_MONTH, 365);

                Date nextThreeMonth = cal.getTime();

                stakingTb.setWallet_address("");
                stakingTb.setName(id);
                stakingTb.setExpire_date(nextThreeMonth);
                stakingTb.setCreated_date(now);
                BigDecimal number3 = BigDecimal.valueOf(coin);
                BigDecimal number4 = new BigDecimal("1.03");
                stakingTb.setReward_amount(number3.multiply(number4).doubleValue());
                stakingTb.setLast_modified_date(now);
                stakingTb.setStart_amount(coin);
                stakingTb.setPercent(3); //연 3 퍼센트 이자
                stakingTb.setState(true);
                stakingTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));

                stakingRepository.save(stakingTb);

                StakingDto stakingDto = new StakingDto(stakingTb);

                result.put("staking", stakingDto);
                result.put("resultCode", "true");
                return result;

            } else {
                result.put("message", "coin over");
                result.put("resultCode", "false");
                return result;
            }

        }catch (Exception e){
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }


    }

    @ApiOperation(value = "스테이킹 취소", notes = "등록했던 스테이킹 취소하고 취소된 금액을 받을 지갑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wallet_id", value = "지갑 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking/{staking_id}", method = RequestMethod.DELETE)
    public HashMap staking_cancel(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("staking_id"))){
            result.put("message", "staking_id is null");
            result.put("resultCode", "false");
            return result;
        }
        if(ObjectUtils.isEmpty(data.get("wallet_id"))){
            result.put("message", "wallet_id is null");
            result.put("resultCode", "false");
            return result;
        }

        int staking_id = Integer.parseInt(data.get("staking_id").toString());
        int wallet_id = Integer.parseInt(data.get("wallet_id").toString());

        WalletTb walletTb = walletRepository.getWalletByWallet_id(wallet_id);
        StakingTb stakingTb = stakingRepository.getStakingTbByStakingId(staking_id);

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if (jwtTokenProvider.getUserId(tokenHeader) == stakingTb.getUser_id()) {
            double start_amount = stakingTb.getStart_amount();
            BigDecimal number1 = BigDecimal.valueOf(user.getCoin());
            BigDecimal number2 = BigDecimal.valueOf(stakingTb.getStart_amount());


            user.setCoin(number1.add(number2).doubleValue());

            userRepository.save(user);
            stakingRepository.delete(stakingTb);
            result.put("resultCode", "true");

        } else {
            result.put("message", "unauthority");
            result.put("resultCode", "false");
        }

        return result;
    }

    @ApiOperation(value = "내 스테이킹 확인", notes = "등록했던 스테이킹 취소하고 취소된 금액을 받을 지갑")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking", method = RequestMethod.GET)
    public HashMap staking_view(@RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{
            List<StakingTb> stakingTb = stakingRepository.getStakingTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            List<StakingDto> stakingDtos = new ArrayList<>();
            for(int i=0;i<stakingTb.size();i++){
                stakingTb.get(i);
                StakingDto stakingDto = new StakingDto(stakingTb.get(i));
                stakingDtos.add(stakingDto);
            }

            result.put("staking", stakingDtos);
            result.put("resultCode", "true");
            return result;
        }catch(Exception e){
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }


    }

    @ApiOperation(value = "입금 요청", notes = "지갑에서 서버계정으로 입금 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tx", value = "블록 해쉬값", required = true),
            @ApiImplicitParam(name = "value", value = "거래량", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet/order", method = RequestMethod.POST)
    public HashMap asking_input(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("tx"))){
            result.put("message", "tx is null");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("value"))){
            result.put("message", "value is null");
            result.put("resultCode", "false");
            return result;
        }

        String tx = data.get("tx").toString();
        double amount = Double.parseDouble(data.get("value").toString());


        if(ObjectUtils.isEmpty(data.get("wallet_id"))){
            result.put("message", "wallet_id is null");
            result.put("resultCode", "false");
            return result;
        }


        try{
            AskingTb askingTb = new AskingTb();
            askingTb.setCreated_date(LocalDate.now());
            askingTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            askingTb.setAmount(amount);
            //출금 요청 false, 입금 요청 true
            askingTb.setInput_output(true);
            //state 요청 실행 전 false, 실행 후 true
            askingTb.setStatus(false);
            askingRepository.save(askingTb);
            result.put("resultCode", "true");
            return result;
        }catch(Exception e){
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "출금 요청", notes = "계정에 가지고 있는 코인을 내 지갑으로 출금 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "amount", value = "출금할 가격", required = true),
            @ApiImplicitParam(name = "wallet_id", value = "지갑 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet/order", method = RequestMethod.PUT)
    public HashMap asking_output(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("amount"))){
            result.put("message", "amount is null");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("wallet_id"))){
            result.put("message", "wallet_id is null");
            result.put("resultCode", "false");
            return result;
        }

        double amount = Double.parseDouble(data.get("amount").toString());
        int wallet_id = Integer.parseInt(data.get("wallet_id").toString());


        try{
            AskingTb askingTb = new AskingTb();
            askingTb.setCreated_date(LocalDate.now());
            askingTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            askingTb.setAmount(amount);
            //출금 요청 false, 입금 요청 true
            askingTb.setInput_output(false);
            //state 요청 실행 전 false, 실행 후 true
            askingTb.setStatus(false);
            askingRepository.save(askingTb);
            result.put("resultCode", "true");
            return result;
        }catch(Exception e){
            result.put("resultCode", "false");
            return result;
        }

    }



}