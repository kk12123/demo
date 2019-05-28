package com.seckill.purchase.service.intf;

import com.seckill.common.dto.CommonResult;
import com.seckill.purchase.exception.SecKillException;

public interface SecKillService {
    CommonResult purchase(String userNo,String goodsNo) throws SecKillException;
}
