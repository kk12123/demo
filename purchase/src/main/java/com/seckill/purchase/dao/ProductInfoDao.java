package com.seckill.purchase.dao;

import com.seckill.common.dto.PurchaseProductInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ProductInfoDao {
    /**
     * 插入
     * @param purchaseProductInfo
     */
    void insert(PurchaseProductInfo purchaseProductInfo);


}
