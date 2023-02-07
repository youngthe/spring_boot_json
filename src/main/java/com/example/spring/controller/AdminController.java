package com.example.spring.controller;

import com.example.spring.dao.AskingTb;
import com.example.spring.dao.UserTb;
import com.example.spring.repository.AskingRepository;
import com.example.spring.repository.UserRepository;
import com.example.spring.utils.JwtTokenProvider;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;


@RestController
public class AdminController {
    @Autowired
    private AskingRepository askingRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    @ApiOperation(value = "관리자 페이지", notes = "admin 계정만 관리자 페이지 접근 가능, 입출금 요청 승인 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode, askinglist")
    })
    @RequestMapping(value="/admin", method = RequestMethod.GET)
    public HashMap admin(@RequestHeader("token") String tokenHeader){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));


        if(jwtTokenProvider.validateToken(tokenHeader) && Objects.equals(usertb.getAccount(), "admin")){
            List<AskingTb> askinglist = askingRepository.findAll();
            result.put("askinglist", askinglist);
            return result;
        }else{
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "입금 또는 출금 승인", notes = "입금 출금 요청 승인 확인")

    @ApiImplicitParams({
            @ApiImplicitParam(name = "asking_id", value = "승인 요청 id", required = true),
    })
    
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/{asking_id}", method = RequestMethod.POST)
    public HashMap admin_asking(@RequestHeader("token") String tokenHeader, @PathVariable("asking_id") int asking_id){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));


        if(jwtTokenProvider.validateToken(tokenHeader) && Objects.equals(usertb.getAccount(), "admin")){
            List<AskingTb> askinglist = askingRepository.findAll();
            result.put("resultarray", askinglist);
            return result;
        }else{
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "입금 또는 출금 승인 거절", notes = "입금 출금 요청 승인 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/{asking_id}", method = RequestMethod.DELETE)
    public HashMap admin_delete(@RequestHeader("token") String tokenHeader, @PathVariable("asking_id") int asking_id){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

        if(jwtTokenProvider.validateToken(tokenHeader) && Objects.equals(usertb.getAccount(), "admin")){
            List<AskingTb> askinglist = askingRepository.findAll();
            result.put("resultarray", askinglist);
            return result;
        }else{
            result.put("message", "Token validate");
            result.put("resultCode", "false");
            return result;
        }

    }
}
