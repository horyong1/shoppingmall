package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SellerWishListDto {
    private int sellerWishListNo;
    private int consumerNo;
    private int sellerNo;
    private Date createdAt;
}
