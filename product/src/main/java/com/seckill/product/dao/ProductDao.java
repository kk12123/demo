package com.seckill.product.dao;

import com.seckill.common.dto.ProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductDao {

    /**
     * 根据产品编码查询库存（redis未命中）
     * @param productNo
     * @return
     */
    ProductInfo queryProductInDB(String productNo);

    /**
     *
     * @param productInfo
     * @return
     */
    boolean updateProductInDB(ProductInfo productInfo);
}
