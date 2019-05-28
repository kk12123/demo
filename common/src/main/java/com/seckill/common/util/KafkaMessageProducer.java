package com.seckill.common.util;

import com.alibaba.fastjson.JSON;
import com.seckill.common.dto.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class KafkaMessageProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    public void send(String topic,Object data){

        Message message = new Message();

        message.setId(System.currentTimeMillis());
        message.setData(JSON.toJSONString(data));
        message.setSendTime(new Date());

        kafkaTemplate.send(topic,JSON.toJSONString(message));
    }
}
