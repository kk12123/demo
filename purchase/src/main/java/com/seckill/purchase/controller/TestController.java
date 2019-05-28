package com.seckill.purchase.controller;

import com.seckill.common.constants.CommonConstants;
import com.seckill.common.dto.OrderInfoDto;
import com.seckill.common.util.KafkaMessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Autowired
    KafkaMessageProducer kafkaMessageProducer;

    @RequestMapping("/test")
    public void test(){
        OrderInfoDto o = new OrderInfoDto();
        o.getProductInfo().setProductNo("1231421421");
        o.getUserInfo().setUserNo("123141");
        o.setPrice(1121d);
        o.setNumber(1);
        kafkaMessageProducer.send("test",o);
    }
}
