package com.example.spring.controller;

import com.example.spring.dao.*;
import com.example.spring.dto.CashFlowHistoryDto;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
public class HomeController {
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
    private CommunityRepository communityRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentlikeRepository commentlikeRepository;

    @Autowired
    private LoginHistoryRepository loginHistoryRepository;

    @Autowired
    private CashFlowHistoryRepository cashFlowHistoryRepository;

    @ApiOperation(value = "서버 동작 확인용", notes = "hello 메세지만 뿌림")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HashMap login() {

        HashMap<String, Object> result = new HashMap<>();
        result.put("message", "hello");
        return result;

    }

    @ApiOperation(value = "로그인", notes = "ID, PW를 통한 로그인")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "아이디", required = true, dataType = "String"),
            @ApiImplicitParam(name = "pw", value = "비밀번호", required = true, dataType = "string"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, token")
    })
    public HashMap loginCheck(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(data.get("account"))) {
            result.put("message", "account is null");
            result.put("resultCode", "false");
            return result;
        }
        if (ObjectUtils.isEmpty(data.get("pw"))) {
            result.put("message", "pw is null");
            result.put("resultCode", "false");
            return result;
        }
        String account = data.get("account").toString();
        String pw = data.get("pw").toString();

        log.info("account : {}", account);


        if(userRepository.AccountCheck(account)){
            result.put("message", "not exist");
            result.put("resultCode", "false");
            return result;
        }

        try {

            UserTb user = userRepository.getUserTbByAccount(account);

            String get_pw = user.getPw();

            if (passwordEncoder.matches(pw, get_pw)) {

                if(!user.isState()){
                    result.put("message", "unavailable");
                    result.put("resultCode", "false");
                    return result;
                }else{
                    result.put("resultCode", "true");
                    result.put("jwt", jwtTokenProvider.createToken(user));
                    LoginHistoryTb loginHistoryTb = new LoginHistoryTb();
                }

//                Date date = new Date();
//                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//                System.out.println(format.format(date));
//                loginHistoryRepository.setIncreaseCount(format.format(date));

                return result;
            } else {
                result.put("message", "no match");
                result.put("resultCode", "false");
                return result;
            }
        } catch (Exception e) {
            System.out.println(e);
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "금일 방문자", notes = "금일 로그인 한 사용자  수")
    @RequestMapping(value = "/visit", method = RequestMethod.GET)
    @ApiImplicitParams({
    })
    public HashMap visit() {

        HashMap<String, Object> result = new HashMap<>();
        try {

            List<LoginHistoryTb> login_list = loginHistoryRepository.getUserHistory();
            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

            int count = loginHistoryRepository.getCountByDate(format.format(date));
            result.put("count", count);
            return result;
        }

        catch (Exception e){
            return result;
        }
    }

    @ApiOperation(value = "회원가입", notes = "아이디 비밀번호 별명을 통한 회원가입")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "account", value = "아이디", required = true),
            @ApiImplicitParam(name = "pw", value = "비밀번호", required = true),
            @ApiImplicitParam(name = "nickname", value = "별명", required = true)
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public HashMap Register(@RequestBody HashMap<String, Object> data) {

        HashMap<String, Object> result = new HashMap<>();

        if (ObjectUtils.isEmpty(data.get("account"))) {
            result.put("message", "account is null");
            result.put("resultCode", "false");
            return result;
        }
        if (ObjectUtils.isEmpty(data.get("pw"))) {
            result.put("message", "pw is null");
            result.put("resultCode", "false");
            return result;
        }
        if (ObjectUtils.isEmpty(data.get("nickname"))) {
            result.put("message", "nickname is null");
            result.put("resultCode", "false");
            return result;
        }

        String account = data.get("account").toString();
        String pw = data.get("pw").toString();
        String name = data.get("nickname").toString();

        log.info("account : {}", account);
        log.info("pw : {}", pw);
        log.info("name : {}", name);

        if (userRepository.AccountCheck(account)) {
            try {
                UserTb userTb = new UserTb();
                Date now = new Date();
                userTb.setAccount(account);
                userTb.setPw(passwordEncoder.encode(pw));
                userTb.setName(name);
                userTb.setRole("user");
                userTb.setCoin(0);
                userTb.setState(true);
                userTb.setDate(now);
                userRepository.save(userTb);
                result.put("resultCode", "true");
                return result;
            } catch (Exception e) {
                result.put("resultCode", "false");
                return result;
            }
        } else {
            result.put("message", "already");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "내 정보 확인", notes = "현재 로그인 되어 있는 사용자의 정보, wallet, user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, wallet, staking, user")
    })
    @RequestMapping(value = "/myinfo", method = RequestMethod.GET)
    public HashMap mypage(@RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();

        if (!jwtTokenProvider.validateToken(tokenHeader)) {
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        try {
            List<WalletTb> walletTb = walletRepository.getWalletByUser_id(user_id);
            UserTb userTb = userRepository.getUserTbByUserId(user_id);

            Date date = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            System.out.println(format.format(date));
            loginHistoryRepository.setIncreaseCount(format.format(date));

            result.put("wallet", walletTb);
            result.put("user", userTb);
            result.put("resultCode", "true");
            return result;

        } catch (Exception e) {
            result.put("message", "error");
            result.put("resultCode", "false");
        }

        return result;
    }

    @ApiOperation(value = "비밀번호 변경", notes = "비밀번호 변경")
    @RequestMapping(value = "/setting/password", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "change_pw", value = "바꿀 비밀번호", required = true, dataType = "string"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, token")
    })
    public HashMap password_check(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();


        if (ObjectUtils.isEmpty(data.get("change_pw"))) {
            result.put("message", "change_pw is null");
            result.put("resultCode", "false");
            return result;
        }

        String change_pw = data.get("change_pw").toString();



        try {
            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            user.setPw(passwordEncoder.encode(change_pw));
            userRepository.save(user);
            result.put("resultCode", "true");
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "not exist user");
            result.put("resultCode", "false");
            return result;
        }
    }


    @ApiOperation(value = "회원정보 변경", notes = "회원정보 변경")
    @RequestMapping(value = "/setting/info", method = RequestMethod.POST)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "바꿀 이름", required = true, dataType = "string"),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, token")
    })
    public HashMap name_change(@RequestBody HashMap<String, Object> data, @RequestHeader("token") String tokenHeader) {

        HashMap<String, Object> result = new HashMap<>();


        if (ObjectUtils.isEmpty(data.get("name"))) {
            result.put("message", "name is null");
            result.put("resultCode", "false");
            return result;
        }

        String name = data.get("name").toString();

        try {

            UserTb user = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));
            user.setName(name);
            userRepository.save(user);
            result.put("resultCode", "true");
            return result;

        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "not exist user");
            result.put("resultCode", "false");
            return result;
        }
    }

    @ApiOperation(value = "입출금 내역 조회", notes = "입출금 내역 조회하는 페이지")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "nowpage", value = "현재 페이지 번호", required = true),
            @ApiImplicitParam(name = "count", value = "보여줄 입출금 갯수", required = true),
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/cash-flow", method = RequestMethod.GET)
    public HashMap my_community(@RequestParam ("nowpage") int nowpage, @RequestParam ("count") int countpage, @RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int start = countpage * nowpage;
        int end = countpage*(nowpage+1) - 1;

        try{

//            List<CashFlowHistoryTb> cashFlowHistoryTbList = cashFlowHistoryRepository.getInoutHistoryByUserId(jwtTokenProvider.getUserId(tokenHeader));
            List<CashFlowHistoryDto> send_list = new ArrayList<>();

//            if(end>= cashFlowHistoryTbList.size()) end = cashFlowHistoryTbList.size()-1;
//
//            for(int i=start;i<=end;i++){
//                CashFlowHistoryDto cashFlowHistoryDto = new CashFlowHistoryDto(cashFlowHistoryTbList.get(i));
//                send_list.add(cashFlowHistoryDto);
//            }
//
//            result.put("total", cashFlowHistoryTbList.size());
            result.put("list", send_list);
            result.put("resultCode", "true");
            return result;

        }catch (Exception e){

            result.put("resultCode", "false");
            result.put("message", "db error");
            return result;

        }

    }


    @ApiOperation(value = "토큰 구매(입금)", notes = "토큰 구매")
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

            Date now = new Date();
            CashFlowHistoryTb cashFlowHistoryTb = new CashFlowHistoryTb();
            cashFlowHistoryTb.setDate(now);
            cashFlowHistoryTb.setCoin(coin);
            cashFlowHistoryTb.setUser_id(jwtTokenProvider.getUserId(tokenHeader));
            cashFlowHistoryTb.setTo_address("직접 구매");
            cashFlowHistoryTb.setFrom_address("직접 구매");
            cashFlowHistoryTb.setState(1);

            cashFlowHistoryRepository.save(cashFlowHistoryTb);


            result.put("resultCode", "true");
            return result;


        } catch (Exception e) {
            log.info("{}", e);
            result.put("message", "db error");
            result.put("resultCode", "false");
            return result;
        }
    }

}