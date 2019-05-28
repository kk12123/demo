package com.seckill.orderservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.seckill.common.dto.*;
import com.seckill.common.util.KeyBuilder;
import com.seckill.common.util.RedisUtil;
import com.seckill.orderservice.client.ProductStockClient;
import com.seckill.orderservice.service.intf.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class OrderServiceImpl implements OrderService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private ProductStockClient productStockClient;

    @Override
    public CommonResult saveOrder(PurchaseProductInfo purchaseProductInfo) {
        LOGGER.info("OrderServiceImpl.saveOrder begin,purchaseProductInfo:{}",purchaseProductInfo);
        CommonResult commonResult = new CommonResult();
        try {
            ProductInfo productInfo = productStockClient.queryProductInfo(purchaseProductInfo.getProductNo());
            if (productInfo.getStock() <= 0){
                commonResult.fail("2222","无库存，抢购失败");
                return commonResult;
            }
            OrderInfoDto orderInfoDto = new OrderInfoDto();
            orderInfoDto.setCreateDate(new Date());
            UserInfo userInfo = new UserInfo(purchaseProductInfo.getUserNo());
            orderInfoDto.setUserInfo(userInfo);
            orderInfoDto.setProductInfo(productInfo);
            orderInfoDto.setPrice(productInfo.getPrice() * orderInfoDto.getNumber());
            RedisUtil.set(KeyBuilder.orderKeyBuilder(purchaseProductInfo.getUserNo(),purchaseProductInfo.getProductNo()),
                    JSON.toJSONString(orderInfoDto));
            CommonResult syncStockResult = productStockClient.syncStock(orderInfoDto);
            if(!syncStockResult.isSuccess()){
                RedisUtil.del(KeyBuilder.orderKeyBuilder(purchaseProductInfo.getUserNo(),purchaseProductInfo.getProductNo()));
                commonResult.fail(syncStockResult.getResponseCode(),syncStockResult.getRepsonseMsg());
            }
        }catch (Exception e){
            LOGGER.error("保存订单异常，e:{}",e);
            RedisUtil.del(KeyBuilder.orderKeyBuilder(purchaseProductInfo.getUserNo(),purchaseProductInfo.getProductNo()));
            commonResult.fail("8876","保存订单异常");
        }
        LOGGER.info("OrderServiceImpl.saveOrder end,commonResult:{}",commonResult);
        return commonResult;
    }
}
