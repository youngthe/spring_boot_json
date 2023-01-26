package com.example.spring.spring.controller;

import com.example.spring.spring.dao.UserTb;
import com.example.spring.spring.repository.UserRepository;
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
import java.util.HashMap;
import java.util.List;

@RestController
public class HomeController {

    private final Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());
    @Autowired
    private UserRepository userRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    PasswordEncoder passwordEncoder;




    @RequestMapping(value = "/")
    public HashMap login(HttpSession session){

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

    @RequestMapping(value = "/logout")
    public String logout(HttpSession session){

        session.invalidate();

        return "login";
    }



}
