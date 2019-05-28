package com.seckill.pay.client;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.OrderInfoDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "PRODUCT-SERVICE")
public interface SyncProductStockclient {

    /**
     *
     * @param orderInfoDto
     * @return
     */
    @RequestMapping("/syncStock")
    CommonResult syncStock(@RequestBody OrderInfoDto orderInfoDto);
}
