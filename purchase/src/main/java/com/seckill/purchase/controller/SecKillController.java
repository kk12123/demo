package com.seckill.purchase.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.PurchaseProductInfo;
import com.seckill.common.util.KeyBuilder;
import com.seckill.common.util.RedisUtil;
import com.seckill.purchase.client.OrderServiceClient;
import com.seckill.purchase.service.intf.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;
import java.util.UUID;

@RestController
public class SecKillController {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillController.class);
    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private SecKillService secKillService;

    @HystrixCommand(fallbackMethod = "fallBack",
            threadPoolProperties = {  //10个核心线程池,超过20个的队列外的请求被拒绝; 当一切都是正常的时候，线程池一般仅会有1到2个线程激活来提供服务
                    @HystrixProperty(name = "coreSize", value = "10"),
                    @HystrixProperty(name = "maxQueueSize", value = "100"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "20") },
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000"),
                    //命令执行超时时间
                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),
                    //若干10s一个窗口内失败三次, 则达到触发熔断的最少请求量
                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "30000")
                    //断路30s后尝试执行, 默认为5s
            })
    @RequestMapping("/purchase")
    public CommonResult secKill(PurchaseProductInfo purchaseProductInfo) {
        purchaseProductInfo.setUserNo(UUID.randomUUID().toString().replaceAll("-","").substring(0,8));
        purchaseProductInfo.setProductNo("123456");
        LOGGER.info("secKill begin:{}",purchaseProductInfo);
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        CommonResult commonResult = new CommonResult();
        try {
//            if (RedisUtil.lock(purchaseProductInfo.getUserNo()+"lock", uuid)) {
                //先查缓存，是否已存在用户秒杀信息，已参与的用户不能重复参加
                boolean isPurchased = RedisUtil
                        .getbit(KeyBuilder.build(purchaseProductInfo.getUserNo(), purchaseProductInfo.getProductNo()));
                if (isPurchased) {
                    commonResult.fail("1111", "不可重复参与秒杀活动");
                    RedisUtil.release(purchaseProductInfo.getUserNo(), uuid);
                    return commonResult;
                }
                commonResult = secKillService.purchase(purchaseProductInfo.getUserNo(), purchaseProductInfo.getProductNo());
//            }
        } catch (Exception e) {
            commonResult.fail("9999", e.getMessage());
            return commonResult;
        }finally {
            RedisUtil.release(purchaseProductInfo.getUserNo()+"lock", uuid);
        }
        return commonResult;
    }
    protected CommonResult  fallBack(PurchaseProductInfo purchaseProductInfo){
        CommonResult commonResult = new CommonResult();
        commonResult.fail("12314","系统异常");
        return commonResult;
    }
}
