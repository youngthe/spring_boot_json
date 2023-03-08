package com.example.spring.controller;

import com.example.spring.Abi;
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
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Wallet;
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

    @ApiOperation(value = "로그인 없이 스테이킹 추가", notes = "스테이킹 등록")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin", value = "스테이킹한 코인량", required = true),
            @ApiImplicitParam(name = "block_hash", value = "블록 해쉬 값", required = true),
            @ApiImplicitParam(name = "address", value = "스테이킹 한 지갑 주소", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking-without", method = RequestMethod.POST)
    public HashMap staking_add2(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(data.get("coin"))){
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("block_hash"))){
            result.put("message", "block_hash is null");
            result.put("resultCode", "false");
            return result;
        }

        if(ObjectUtils.isEmpty(data.get("address"))){
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        double coin = Double.parseDouble(data.get("coin").toString());
        String block_hash = data.get("block_hash").toString();
        String address = data.get("address").toString();


        // ex 스테이킹한 금액의 0.05센트를 1년 경과 시 지급
        // 이자 3프로
        double interest_rate = 1.03;

        try{

            StakingTb stakingTb = new StakingTb();
            Date now = new Date();

            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH, 1);

            Date afterOneYear = cal.getTime();

            stakingTb.setWallet_address(address);
            stakingTb.setExpire_date(afterOneYear);
            stakingTb.setCreated_date(now);
            BigDecimal number1 = BigDecimal.valueOf(coin);
            BigDecimal number2 = BigDecimal.valueOf(interest_rate);
            stakingTb.setReward_amount(number1.multiply(number2).doubleValue());
            stakingTb.setStart_amount(coin);
            stakingTb.setPercent(3); //연 3 퍼센트 이자
            stakingTb.setState(true);
            stakingTb.setUser_id(0);
            stakingTb.setBlock_hash(block_hash);

            stakingRepository.save(stakingTb);

            StakingDto stakingDto = new StakingDto(stakingTb);

            result.put("staking", stakingDto);
            result.put("resultCode", "true");
            return result;

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

    @ApiOperation(value = "비로그인 스테이킹 취소", notes = "등록했던 스테이킹 취소하고 취소된 금액을 받을 지갑")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "스테이킹 등록했던 주소", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking-without", method = RequestMethod.PATCH)
    public HashMap staking_without_cancel(@RequestBody HashMap<String, Object> data) throws Exception {

        HashMap<String, Object> result = new HashMap<>();


        if(ObjectUtils.isEmpty(data.get("address"))){
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        String address = data.get("address").toString();

        List<StakingTb> stakingTb = stakingRepository.getStakingByAddress(address);

        if(stakingTb.size() == 0){
            result.put("message", "not exist");
            result.put("resultCode", "false");
            return result;
        }
        List<StakingDto> stakingDtos = new ArrayList<>();
        for(int i=0;i<stakingTb.size();i++){
            String toSendAddress = stakingTb.get(i).getWallet_address();
            double start_amount = stakingTb.get(i).getStart_amount();
            double add_amount = stakingTb.get(i).getAdd_amount();
            BigDecimal decimal_start_amount = BigDecimal.valueOf(start_amount);
            BigDecimal decimal_add_amount = BigDecimal.valueOf(add_amount);
            double send_amount = decimal_start_amount.add(decimal_add_amount).doubleValue();


            Date now = new Date();

            System.out.println("web3j start");
            Web3j web3j = Web3j.build(new HttpService("https://api.baobab.klaytn.net:8651"));

            BigInteger gasPrice = Convert.toWei("250", Convert.Unit.GWEI).toBigInteger();
            BigInteger GasLimit = BigInteger.valueOf(30000000L);
            String contract_address = "0x981AeB68B7A9d1B3d9341636D0f45660995C6Af5";
            EthGasPrice ethGasPrice = web3j.ethGasPrice().send();
            System.out.println("eth :" + ethGasPrice.getGasPrice());
            File file = new File("./UTC--2023-02-28T06-22-54.425506000Z--87e02340c9c5dab434d2e9f5cdbc3da06b8f47da.json");
            Credentials credentials = WalletUtils.loadCredentials("test", file);

            Abi abi = Abi.load(contract_address, web3j, credentials, gasPrice, GasLimit);

            BigInteger value = Convert.toWei(String.valueOf(send_amount), Convert.Unit.ETHER).toBigInteger();
            abi.transfer(toSendAddress, value).send();

            stakingTb.get(i).setState(false);
            stakingTb.get(i).setRelease_date(now);
            stakingRepository.save(stakingTb.get(i));
            StakingDto stakingDto = new StakingDto(stakingTb.get(i));
            stakingDtos.add(stakingDto);
        }
            result.put("staking", stakingDtos);
            result.put("resultCode", "true");

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

    @ApiOperation(value = "지갑 주소로 스테이킹 확인", notes = "지갑 주소 검색으로 스테이킹 현황 확인")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "address", value = "지갑 주소", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/staking/address", method = RequestMethod.POST)
    public HashMap staking_view_address(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();

        if(ObjectUtils.isEmpty(data.get("address"))){
            result.put("message", "address is null");
            result.put("resultCode", "false");
            return result;
        }

        String address = data.get("address").toString();

        try{
            List<StakingTb> stakingTb = stakingRepository.getStakingByAddress(address);
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

    @ApiOperation(value = "토큰 구매", notes = "토큰 구매")
    @RequestMapping(value = "/purchase", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "coin", value = "구입한 토큰 값", required = true, dataType = "string"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    public HashMap token(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();


        if (ObjectUtils.isEmpty(data.get("coin"))) {
            result.put("message", "coin is null");
            result.put("resultCode", "false");
            return result;
        }

        double coin = Double.parseDouble(data.get("coin").toString());
        BigDecimal num1 = BigDecimal.valueOf(coin);
        BigDecimal num2 = new BigDecimal("2");
        coin = num1.multiply(num2).doubleValue();

        try {

            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

            BigDecimal number1 = BigDecimal.valueOf(user.getCoin());
            BigDecimal number2 = new BigDecimal(coin);
            user.setCoin(number1.add(number2).doubleValue());
            userRepository.save(user);

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