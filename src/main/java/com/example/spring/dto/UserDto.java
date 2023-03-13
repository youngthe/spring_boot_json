package com.example.spring.dto;

import com.example.spring.dao.UserTb;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;

@Getter
@Setter
public class UserDto {

    private int user_id;
    private String account;
    private String pw;
    private String name;
    private double coin;
    private String role;
    private boolean state;
    private String create_date;
    private String last_login_date;

    public UserDto(UserTb user) {
        this.user_id = user.getUser_id();
        this.account = user.getAccount();
        this.pw = user.getPw();
        this.name = user.getName();
        this.coin = user.getCoin();
        this.role = user.getRole();
        this.state = user.isState();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.create_date = format.format(user.getCreate_date());
        if(!ObjectUtils.isEmpty(user.getLast_login_date())){
            this.last_login_date = format.format(user.getLast_login_date());
        }else{
            this.last_login_date = "";
        }
    }
}
