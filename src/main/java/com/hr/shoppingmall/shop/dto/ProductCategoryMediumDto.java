package com.hr.shoppingmall.shop.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductCategoryMediumDto {
    private int categoryMediumNo;
    private int categoryNo;
    private String categoryMediumName;
    private Date createdAt;
}
