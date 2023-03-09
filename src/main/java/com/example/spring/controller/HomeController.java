package com.example.spring.controller;

import com.example.spring.dao.*;
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

import java.text.SimpleDateFormat;
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
        try {

            UserTb user = userRepository.getUserTbByAccount(account);

            String get_pw = user.getPw();



            if (passwordEncoder.matches(pw, get_pw)) {
                result.put("resultCode", "true");
                result.put("jwt", jwtTokenProvider.createToken(user));
                LoginHistoryTb loginHistoryTb = new LoginHistoryTb();

//                Date date = new Date();
//                SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
//                System.out.println(format.format(date));
//                loginHistoryRepository.setIncreaseCount(format.format(date));

                return result;
            } else {
                result.put("message", "not exist");
                result.put("resultCode", "false");
                return result;
            }
        } catch (Exception e) {
            result.put("message", "not exist");
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
                userTb.setAccount(account);
                userTb.setPw(passwordEncoder.encode(pw));
                userTb.setName(name);
                userTb.setRole("user");
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





}