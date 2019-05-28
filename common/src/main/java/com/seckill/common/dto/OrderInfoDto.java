package com.seckill.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderInfoDto {

    /**
     * 主键id
     */
    private long id;
    /**
     * 用户信息
     */
    private UserInfo userInfo;
    /**
     * 商品信息
     */
    private ProductInfo productInfo;

    /**
     * 总价格
     */
    private double price;
    /**
     * 订单生成时间
     */
    private Date createDate;
    /**
     * 购买数量 ，默认为1
     */
    private int number = 1;
    /**
     * 抢购状态 0：抢购中，1：抢购成功，2：抢购失败
     */
    private String status;




}
