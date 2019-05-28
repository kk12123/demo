package com.seckill.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.seckill.common.dto.CommonResult;
import com.seckill.common.dto.ProductInfo;
import com.seckill.common.util.RedisUtil;
import com.seckill.product.dao.ProductDao;
import com.seckill.product.service.intf.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductDao productDao;

    @Override
    public ProductInfo queryProductByProductNo(String productNo) {
        LOGGER.info("开始查询库存，productNo：{}",productNo);
        ProductInfo productInRedis = JSON.parseObject(RedisUtil.get(productNo),ProductInfo.class);

        if (productInRedis == null) {
            productInRedis = productDao.queryProductInDB(productNo);
            RedisUtil.set(productNo, JSON.toJSONString(productInRedis));
        }
        LOGGER.info("查询库存结束，productInRedis：{}",productInRedis);
        return productInRedis;
    }

    @Override
    public CommonResult updateStock(String productNo, int number) {
        CommonResult commonResult = new CommonResult();
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        try {
            if(RedisUtil.lock(productNo+"_payLock",uuid)){
                ProductInfo product = JSON.parseObject(RedisUtil.get(productNo),ProductInfo.class);
                if(product.getStock() <=  number){
                    commonResult.fail("2222","无库存，抢购失败");
                    return commonResult;
                }
                product.setStock(product.getStock() - number);

                RedisUtil.set(productNo, JSON.toJSONString(product));
                //存数据库 TODO
            }else {
                commonResult.fail("7877","正在处理中，请稍后再试");
            }
        }catch (Exception e){
            commonResult.fail("9999", e.getMessage());
        }finally {
            RedisUtil.release(productNo+"_payLock",uuid);
        }
        return commonResult;
    }
}
