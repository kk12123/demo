package com.seckill.common.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PurchaseProductInfo implements Serializable {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 买家用户名
     */
    private String userNo;
    /**
     * 商品编码
     */
    private String productNo;
    /**
     * 请求发起时间
     */
    private Date createDate;
    /**
     * 抢购状态 0：抢购中，1：抢购成功，2：抢购失败
     */
    private String status;

}
