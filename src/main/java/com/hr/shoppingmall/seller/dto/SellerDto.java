package com.hr.shoppingmall.seller.dto;

import java.util.Date;

import lombok.Data;

@Data
public class SellerDto {
    private int sellerNo;
    private String sellerId;
    private String password;
    private String shopName;
    private Date createdAt;

}
