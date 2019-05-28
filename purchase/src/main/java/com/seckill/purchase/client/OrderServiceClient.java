package com.seckill.purchase.client;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.PurchaseProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@FeignClient(value = "ORDER-SERVICE")
public interface OrderServiceClient {

    @RequestMapping("/saveOrder")
    CommonResult saveOrder(@RequestBody  PurchaseProductInfo purchaseProductInfo);

}
