package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductWishlistDto {
    private int productWishlistNo;
    private int consumerNo;
    private int productNo;
    private Date createdAt;
}
