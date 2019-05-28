package com.seckill.purchase.service.thread;

import com.seckill.common.dto.PurchaseProductInfo;
import com.seckill.common.util.KeyBuilder;
import com.seckill.common.util.RedisUtil;
import com.seckill.purchase.client.OrderServiceClient;
import com.seckill.purchase.dao.ProductInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class PurchaseThread1 implements Runnable{

    private OrderServiceClient orderServiceClient;

    @Autowired
    private ProductInfoDao productInfoDao;

    private PurchaseProductInfo purchaseProductInfo;

    public OrderServiceClient getOrderServiceClient() {
        return orderServiceClient;
    }

    public void setOrderServiceClient(OrderServiceClient orderServiceClient) {
        this.orderServiceClient = orderServiceClient;
    }

    public void setPurchaseProductInfo(PurchaseProductInfo purchaseProductInfo) {
        this.purchaseProductInfo = purchaseProductInfo;
    }

    @Override
    @Transactional
    public void run() {
        orderServiceClient.saveOrder(purchaseProductInfo);
        productInfoDao.insert(purchaseProductInfo);
        RedisUtil.bitSet(KeyBuilder.build(purchaseProductInfo.getUserNo(),purchaseProductInfo.getProductNo()),"1");
    }
}
