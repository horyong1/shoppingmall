package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ShoppingPurchaseDto {
    private int purchaseNo;
    private int consumerNo;
    private int productNo;
    private int quantity;
    private String shoppingAdress;
    private Date purchaseDate;
    private String state;

}
