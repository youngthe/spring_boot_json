package com.example.spring.controller;

import com.example.spring.dao.StakingTb;
import com.example.spring.dao.UserTb;
import com.example.spring.dao.WalletTb;
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
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
    StakingRepository stakingRepository;


    @ApiOperation(value = "서버 동작 확인용", notes = "hello 메세지만 뿌림")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HashMap login(){

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
    @ApiOperation(value = "내 정보 확인", notes = "현재 로그인 되어 있는 사용자의 정보, 지갑, 내 스테이킹 정보 주는 엔드포인트")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, wallet, staking, user")
    })
    @RequestMapping(value = "/myinfo", method = RequestMethod.GET)
    public HashMap mypage(@RequestHeader("token") String tokenHeader){

        HashMap<String, Object> result = new HashMap<>();

        if(!jwtTokenProvider.validateToken(tokenHeader)){
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

        int user_id = jwtTokenProvider.getUserId(tokenHeader);

        try{
            WalletTb walletTb = walletRepository.getWalletByUser_id(user_id);
            UserTb userTb = userRepository.getUserTbByUserId(user_id);
            StakingTb stakingTb = stakingRepository.getStakingTbByUserId(user_id);

            JSONArray walletArray = new JSONArray();
            JSONArray userArray = new JSONArray();
            JSONArray stakingArray = new JSONArray();
            JSONObject temp1 = new JSONObject();


            temp1.put("address", walletTb.getAddress());
            temp1.put("coin", walletTb.getCoin());
            walletArray.put(temp1);

            JSONObject temp2 = new JSONObject();
            temp2.put("name", userTb.getName());
            userArray.put(temp2);

            JSONObject temp3 = new JSONObject();
            temp3.put("name", stakingTb.getName());
            temp3.put("reward", stakingTb.getReward_amount());
            temp3.put("create_date", stakingTb.getCreated_date());
            stakingArray.put(temp3);

            result.put("wallet", walletArray.toString());
            result.put("staking", stakingArray.toString());
            result.put("user", userArray.toString());
            result.put("resultCode", "true");
            return result;

        }catch(Exception e){
            result.put("message", "error");
            result.put("resultCode", "false");
        }

        return result;
    }

}