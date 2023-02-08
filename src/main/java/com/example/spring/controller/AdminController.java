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

    @ApiOperation(value = "요청 승인", notes = "입금 출금 요청 승인 확인, +  요청 승인은 어떻게 처리해야하나")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/{asking_id}", method = RequestMethod.POST)
    public HashMap admin_asking(@RequestHeader("token") String tokenHeader, @PathVariable("asking_id") int asking_id){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));


        if(jwtTokenProvider.validateToken(tokenHeader) && Objects.equals(usertb.getAccount(), "admin")){

            //상태를 승인 확인으로 변경
            AskingTb askingTb = askingRepository.getAskingTbByAskingId(asking_id);

            if(askingTb.isInput_output()){
                //서버 계정으로 입금, 여기서 수수료 적용 아니면 입금요청을 할 때 수수료 적용
                usertb.setCoin(usertb.getCoin() + askingTb.getAmount());
                userRepository.save(usertb);
                askingTb.setStatus(true);
                askingRepository.save(askingTb);
                result.put("resultCode", "true");

            }else{
                //사용자 지갑으로 출금
                usertb.setCoin(usertb.getCoin() - askingTb.getAmount());

                //?????
                //출금처리를 승인을 하면 어떻게 지갑으로 보내주지?
                userRepository.save(usertb);
                askingTb.setStatus(true);
                askingRepository.save(askingTb);

                result.put("message", "");
                result.put("resultCode", "true");

            }

            askingTb.setStatus(true);
            askingRepository.save(askingTb);
            
            
            return result;
        }else{
            result.put("message", "Token validate or not admin");
            result.put("resultCode", "false");
            return result;
        }

    }

    @ApiOperation(value = "요청 거절", notes = "입금 또는 출금 승인 거절")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "resultCode")
    })
    @RequestMapping(value="/admin/{asking_id}", method = RequestMethod.DELETE)
    public HashMap admin_delete(@RequestHeader("token") String tokenHeader, @PathVariable("asking_id") int asking_id){

        HashMap<String,Object> result = new HashMap<>();
        UserTb usertb = userRepository.getUserTbByUserId(jwtTokenProvider.getUserId(tokenHeader));

        if(jwtTokenProvider.validateToken(tokenHeader) && Objects.equals(usertb.getAccount(), "admin")){
            List<AskingTb> askinglist = askingRepository.findAll();
            askingRepository.deleteById(asking_id);
            return result;

        }else{
            result.put("message", "Token validate or not admin");
            result.put("resultCode", "false");
            return result;
        }
    }
}
