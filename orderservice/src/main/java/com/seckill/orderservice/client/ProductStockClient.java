package com.seckill.orderservice.client;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.OrderInfoDto;
import com.seckill.common.dto.ProductInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "PRODUCT-SERVICE")
public interface ProductStockClient {

    @RequestMapping(value = "/queryProductInfo")
    ProductInfo queryProductInfo(@RequestParam("productNo") String productNo);

    @RequestMapping(value = "/syncStock")
    CommonResult syncStock(@RequestBody OrderInfoDto orderInfoDto);

}
