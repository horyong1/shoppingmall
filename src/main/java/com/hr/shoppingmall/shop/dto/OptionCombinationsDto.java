package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OptionCombinationsDto {
    private int combinationNo;
    private int productNo;
    private String combinationValue;
    private int stock;
    private Date createdAt;
}
