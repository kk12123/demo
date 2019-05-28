package com.seckill.common.dto;

import lombok.Data;

@Data
public class ProductInfo {

    private long id;

    private String productNo;

    private String productName;

    private long stock;

    private double price;
}
