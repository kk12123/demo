package com.seckill.orderservice.service.intf;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.PurchaseProductInfo;

public interface OrderService {

    CommonResult saveOrder(PurchaseProductInfo purchaseProductInfo);
}
