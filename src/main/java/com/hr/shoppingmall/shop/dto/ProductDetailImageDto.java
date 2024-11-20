package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductDetailImageDto {
    private int imageNo;
    private int productNo;
    private String imageLink;
    private Date createdAt;
}
