package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductCategoryDto {
    private int categoryNo;
    private String categoryName;
    private Date createdAt;
}
