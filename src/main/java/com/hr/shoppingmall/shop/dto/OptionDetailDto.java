package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;


@Data
public class OptionDetailDto {
    private int optionDetailNo;
    private int optionNo;
    private String optionDetailName;
    private int totalQuantity;
    private int priceAdjustment;
    private Date createdAt;
    private Date updatedAt;
}
