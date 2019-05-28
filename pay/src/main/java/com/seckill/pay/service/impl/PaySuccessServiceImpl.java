package com.seckill.pay.service.impl;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.OrderInfoDto;
import com.seckill.pay.client.SyncProductStockclient;
import com.seckill.pay.service.intf.PaySuccessService;
import org.springframework.beans.factory.annotation.Autowired;

public class PaySuccessServiceImpl implements PaySuccessService {

    @Autowired
    private SyncProductStockclient productStockclient;

    @Override
    public CommonResult paySuccess(OrderInfoDto orderInfoDto) {
        CommonResult commonResult = productStockclient.syncStock(orderInfoDto);
        return commonResult;
    }
}
