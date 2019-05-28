package com.seckill.product.service.intf;

import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.ProductInfo;

public interface ProductService {
    /**
     * 根据产品编码查询库存
     * @param productNo
     * @return
     */
    ProductInfo queryProductByProductNo(String productNo);

    /**
     * 更新库存
     * @param productNo
     * @return
     */
    CommonResult updateStock(String productNo,int number);
}
