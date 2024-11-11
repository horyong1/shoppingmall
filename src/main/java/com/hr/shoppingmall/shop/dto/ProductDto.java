package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductDto {
    private int productNo;
    private int categoryNo;
    private int sellerNo;
    private String productName;
    private String productDescription;
    private int price;
    private String mainImageUrl;
    private int totalQuantity;
    private Date createdAt;
}
