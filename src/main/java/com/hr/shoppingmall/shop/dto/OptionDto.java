package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OptionDto {
    private int optionNo;
    private int productNo;
    private String optionName;
    private Date createdAt;
}
