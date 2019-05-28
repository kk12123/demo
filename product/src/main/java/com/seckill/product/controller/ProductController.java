package com.seckill.product.controller;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.OrderInfoDto;
import com.seckill.common.dto.ProductInfo;
import com.seckill.product.service.intf.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @RequestMapping("/queryProductInfo")
    public ProductInfo queryProductInfo(@RequestParam("productNo") String productNo){
        ProductInfo productInfo = productService.queryProductByProductNo(productNo);
        return productInfo;
    }
    @RequestMapping("/syncStock")
    public CommonResult syncStock(@RequestBody OrderInfoDto orderInfoDto){
        CommonResult result = productService.updateStock(orderInfoDto.getProductInfo().getProductNo(),orderInfoDto.getNumber());
        return result;
    }
}
