package com.seckill.orderservice.controller;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.PurchaseProductInfo;
import com.seckill.common.util.RedisUtil;
import com.seckill.orderservice.service.intf.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class OrderController {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private OrderService orderService;

    @RequestMapping("/saveOrder")
    public CommonResult saveOrder(@RequestBody PurchaseProductInfo purchaseProductInfo){
        LOGGER.info("保存订单开始:{}",purchaseProductInfo);
        CommonResult commonResult = new CommonResult();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        try {
            if(RedisUtil.lock(purchaseProductInfo.getUserNo() + purchaseProductInfo.getProductNo()+"lock",uuid)){
                commonResult = orderService.saveOrder(purchaseProductInfo);
            }else {
                commonResult.fail("7877","正在处理中，请稍后再试");
            }
        }catch (Exception e){
            LOGGER.error("保存订单异常，e:{}",e);
            commonResult.fail("123214","保存订单失败");
        }finally {
            RedisUtil.release(purchaseProductInfo.getUserNo() + purchaseProductInfo.getProductNo()+"lock",uuid);
        }
        LOGGER.info("保存订单结束，result：{},purchaseProductInfo,{}",commonResult,purchaseProductInfo);
        return commonResult;
    }
}
