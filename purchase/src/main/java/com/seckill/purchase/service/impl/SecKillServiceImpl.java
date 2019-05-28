package com.seckill.purchase.service.impl;

import com.alibaba.fastjson.JSON;
import com.netflix.discovery.converters.Auto;
import com.seckill.common.constants.CommonConstants;
import com.seckill.common.dto.*;
import com.seckill.common.util.KafkaMessageProducer;
import com.seckill.common.util.KeyBuilder;
import com.seckill.common.util.RedisUtil;
import com.seckill.common.util.WebSocketUtil;
import com.seckill.purchase.client.OrderServiceClient;
import com.seckill.purchase.dao.ProductInfoDao;
import com.seckill.purchase.exception.SecKillException;
import com.seckill.purchase.service.intf.SecKillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.*;

@Service
public class SecKillServiceImpl implements SecKillService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecKillServiceImpl.class);

    @Autowired
    private OrderServiceClient orderServiceClient;

    @Autowired
    private ProductInfoDao productInfoDao;

    @Autowired
    private KafkaMessageProducer kafkaMessageProducer;

    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(5, 20, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>());

    @Override
    public CommonResult purchase(String userNo, String product) throws SecKillException {
        try {
            PurchaseThread purchaseThread = new PurchaseThread();

            PurchaseProductInfo purchaseProductInfo = new PurchaseProductInfo();
            purchaseProductInfo.setUserNo(userNo);
            purchaseProductInfo.setProductNo(product);
            purchaseThread.setPurchaseProductInfo(purchaseProductInfo);
            threadPoolExecutor.execute(purchaseThread);
        } catch (Exception e) {
            LOGGER.error("抢购失败，堆栈：{}", e);
            throw new SecKillException("秒杀失败");
        }
        return new CommonResult();
    }

    private class PurchaseThread implements Runnable {

        private PurchaseProductInfo purchaseProductInfo;

        public void setPurchaseProductInfo(PurchaseProductInfo purchaseProductInfo) {
            this.purchaseProductInfo = purchaseProductInfo;
        }

        @Override
        @Transactional
        public void run() {
            CommonResult commonResult = new CommonResult();
            try{
                productInfoDao.insert(purchaseProductInfo);
                commonResult = orderServiceClient.saveOrder(purchaseProductInfo);
                if (commonResult.isSuccess()) {
                    RedisUtil.bitSet(KeyBuilder.build(purchaseProductInfo.getUserNo(), purchaseProductInfo.getProductNo()), "1");

                    OrderInfoDto orderInfoDto = new OrderInfoDto();
                    orderInfoDto.setUserInfo(new UserInfo(purchaseProductInfo.getUserNo()));
                    orderInfoDto.setProductInfo(new ProductInfo());
                    //保存订单成功后websocket推送订单信息到前端页面，进行支付跳转
                    WebSocketUtil webSocketUtil = new WebSocketUtil();
                    webSocketUtil.onMessage(JSON.toJSONString(orderInfoDto));
//                kafkaMessageProducer.send(CommonConstants.CALL_PAY_TOPIC, purchaseProductInfo);
                }
            }catch (Exception e){
                LOGGER.error("异常：{}",e);
                commonResult.fail("1111","保存订单失败");
            }

        }
    }
}
