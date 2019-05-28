package com.seckill.pay.service.intf;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.OrderInfoDto;

public interface PaySuccessService {

    CommonResult paySuccess(OrderInfoDto orderInfoDto);
}
