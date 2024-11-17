package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class CartDto {
    private int cartNo;
    private int consumerNo;
    private int productNo;
    private int quantity;
    private Date createdAt;
}   
