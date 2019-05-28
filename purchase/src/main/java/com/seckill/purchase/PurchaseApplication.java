package com.seckill.purchase;

import com.seckill.common.util.RedisConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = {"com.seckill.common.util","com.seckill.purchase","com.seckill.common.config"})
@EnableEurekaClient
@EnableFeignClients
@EnableCircuitBreaker
@EnableConfigurationProperties(value = { RedisConfig.class })
public class PurchaseApplication {


    public static void main(String[] args) {
        SpringApplication.run(PurchaseApplication.class, args);
    }

}
