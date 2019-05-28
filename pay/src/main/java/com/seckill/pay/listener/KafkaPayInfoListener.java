package com.seckill.pay.listener;

import com.alibaba.fastjson.JSON;
import com.seckill.common.dto.Message;
import com.seckill.common.dto.OrderInfoDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
@Component
public class KafkaPayInfoListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaPayInfoListener.class);
    @KafkaListener(topics = "test")
    public void onMessage(String data){
        LOGGER.info("开始消费kafka信息：{}",data);
        if(StringUtils.isEmpty(data)){
            return ;
        }
        Message message = JSON.parseObject(data,Message.class);
        OrderInfoDto orderInfoDto =JSON.parseObject(message.getData(),OrderInfoDto.class);


        LOGGER.info("消费kafka信息结束");
    }
}
