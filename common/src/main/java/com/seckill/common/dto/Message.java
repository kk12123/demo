package com.seckill.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Message {

    private long id;

    private String data;

    private Date sendTime;
}
