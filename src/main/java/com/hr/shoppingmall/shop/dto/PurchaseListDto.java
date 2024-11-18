package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class PurchaseListDto {

    private int purchaseListNo;
    private int purchaseNo;
    private int productNo;
    private int quantity;
    private int paymentPrice;
    private Date createdAt;
}
