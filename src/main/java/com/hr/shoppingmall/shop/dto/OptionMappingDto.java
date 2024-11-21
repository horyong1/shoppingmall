package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class OptionMappingDto {
    private int mappingNo;
    private int optionNo;
    private int optionDetailNo;
    private int productNo;
    private Date createdAt;
    private Date updatedAt;
}
