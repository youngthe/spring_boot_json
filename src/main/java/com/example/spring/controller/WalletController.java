package com.example.spring.controller;

import com.example.spring.dao.*;
import com.example.spring.dto.StakingDto;
import com.example.spring.repository.*;
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

    @Autowired
    private CashFlowHistoryRepository cashFlowHistoryRepository;


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
    
    @ApiOperation(value = "지갑 삭제", notes = "지갑 주소 삭제하기")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "wallet_id", value = "지갑 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet/{wallet_id}", method = RequestMethod.DELETE)
    public HashMap wallet_delete(@PathVariable ("wallet_id") int wallet_id, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try {
            WalletTb walletTb = walletRepository.getWalletByWallet_id(wallet_id);

            int user_id = jwtTokenProvider.getUserId(tokenHeader);
            String user_role = userRepository.getRoleByUserId(user_id);

            if (walletTb.getUser_id() == jwtTokenProvider.getUserId(tokenHeader) || user_role.equals("admin")) {
                walletRepository.delete(walletTb);
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
        // 이자 3프로
        double interest_rate = 1.03;

        try{

            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            if (user.getCoin() >= coin) {

                BigDecimal number1 = BigDecimal.valueOf(user.getCoin());
                BigDecimal number2 = BigDecimal.valueOf(coin);

                user.setCoin(number1.subtract(number2).doubleValue());
                userRepository.save(user);

                StakingTb stakingTb = new StakingTb();
                Date now = new Date();

                LocalDate test = LocalDate.now();
                Calendar cal = Calendar.getInstance();
                cal.set(test.getYear(), test.getMonth().getValue()+3, test.getDayOfMonth(), 0, 0, 0);

                Date nextThreeMonth = cal.getTime();

                stakingTb.setWallet_address("none");
                stakingTb.setName(id);
                stakingTb.setExpire_date(nextThreeMonth);
                stakingTb.setCreated_date(now);
                stakingTb.setBlock_hash("none");
                BigDecimal number3 = BigDecimal.valueOf(coin);
                BigDecimal number4 = BigDecimal.valueOf(interest_rate);
                stakingTb.setReward_amount(number3.multiply(number4).doubleValue());
                stakingTb.setStart_amount(coin);
                stakingTb.setPercent(3); //연 3 퍼센트 이자
                stakingTb.setState(true);
                stakingTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
                stakingTb.setAdd_amount(0L);

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
            @ApiImplicitParam(name = "staking_id", value = "스테이킹 id", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking/{staking_id}", method = RequestMethod.DELETE)
    public HashMap staking_cancel(@PathVariable ("staking_id") int staking_id, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }


        StakingTb stakingTb = stakingRepository.getStakingTbByStakingId(staking_id);

        if(!stakingTb.isState()){
            result.put("message", "already done");
            result.put("resultCode", "false");
            return result;
        }

        UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
        if (jwtTokenProvider.getUserId(tokenHeader) == stakingTb.getUser_id()) {
            BigDecimal number1 = BigDecimal.valueOf(user.getCoin());
            BigDecimal number2 = BigDecimal.valueOf(stakingTb.getStart_amount());
            BigDecimal number3 = BigDecimal.valueOf(stakingTb.getAdd_amount());
            number2 = number2.add(number3);
            Date now = new Date();

            user.setCoin(number1.add(number2).doubleValue());

            userRepository.save(user);
            stakingTb.setRelease_date(now);
            stakingTb.setState(false);
            stakingRepository.save(stakingTb);

            StakingDto stakingDto = new StakingDto(stakingTb);


            result.put("staking", stakingDto);
            result.put("resultCode", "true");

        } else {
            result.put("message", "unauthority");
            result.put("resultCode", "false");
        }

        return result;
    }


    @ApiOperation(value = "내 스테이킹 확인", notes = "내가 등록한 스테이킹 확인")
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



    @ApiOperation(value = "입금 요청", notes = "거래소 등에서 입금 요청")
    @RequestMapping(value = "/wallet/asking", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin", value = "입금 요청할 코인 갯수", required = true, dataType = "string"),
            @ApiImplicitParam(name = "address", value = "입금한 addres 주소", required = true, dataType = "string"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    public HashMap input_asking(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(data.get("coin"))) {
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }

        if (ObjectUtils.isEmpty(data.get("address"))) {
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        double coin = Double.parseDouble(data.get("coin").toString());
        String address = data.get("address").toString();
        BigDecimal num1 = BigDecimal.valueOf(coin);
        BigDecimal num2 = new BigDecimal("2");
        coin = num1.multiply(num2).doubleValue();

        try {

            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

            Date now = new Date();
            AskingTb askingTb = new AskingTb();
            askingTb.setAddress(address);
            askingTb.setInput_output(true);
            askingTb.setAsking_time(now);
            askingTb.setCoin(coin);
            askingTb.setStatus(0);
            askingTb.setUser_id(user.getUser_id());
            askingRepository.save(askingTb);

            result.put("resultCode", "true");
            return result;


        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "출금 요청", notes = "계정에 가지고 있는 코인을 내 지갑으로 출금 요청")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin", value = "출금할 가격", required = true),
            @ApiImplicitParam(name = "address", value = "지갑 주소", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet/asking", method = RequestMethod.PUT)
    public HashMap asking_output(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

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

        if(ObjectUtils.isEmpty(data.get("address"))){
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        double coin = Double.parseDouble(data.get("coin").toString());
        String address = data.get("address").toString();

        UserTb userTb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

        double user_coin = userTb.getCoin();
        if(user_coin >= coin){

            try{
                Date now = new Date();
                AskingTb askingTb = new AskingTb();
                askingTb.setAsking_time(now);
                askingTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
                askingTb.setCoin(coin);
                askingTb.setInput_output(false);
                askingTb.setStatus(0);
                askingTb.setAddress(address);
                askingRepository.save(askingTb);
                result.put("resultCode", "true");
                return result;

            }catch(Exception e){
                result.put("resultCode", "false");
                return result;
            }

        }else{
            result.put("message", "coin less");
            result.put("resultCode", "false");
            return result;

        }

    }

    @ApiOperation(value = "내 요청상태 확인", notes = "내 요청상태 확인")
    @ApiImplicitParams({
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/wallet/asking", method = RequestMethod.GET)
    public HashMap asking_state(@RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        try{
            List<AskingTb> askingTbList = askingRepository.getAskingListByUserId(jwtTokenProvider.getUserId(tokenHeader));
            result.put("list", askingTbList);
            result.put("resultCode", "true");
            return result;
        }catch(Exception e){
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }

    }

}