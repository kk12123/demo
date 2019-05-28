package com.seckill.common.dto;

import lombok.Data;

@Data
public class UserInfo {

    private String userNo;

    private String userName;

    private String password;

    private String token;

    public UserInfo(){

    }
    public UserInfo(String userNo){
        this.userNo = userNo;
    }
}
