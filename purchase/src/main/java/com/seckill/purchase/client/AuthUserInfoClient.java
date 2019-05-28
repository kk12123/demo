package com.seckill.purchase.client;

import com.seckill.common.dto.CommonResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

public interface AuthUserInfoClient {

    @RequestMapping
    CommonResult authUserInfo(HttpServletRequest request);

}
