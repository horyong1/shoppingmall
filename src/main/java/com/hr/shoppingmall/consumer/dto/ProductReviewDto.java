package com.hr.shoppingmall.consumer.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ProductReviewDto {
    private int reviewNo;
    private int productNo;
    private int consumerNo;
    private String reviewContent;
    private int rating;
    private Date createdAt;
    private String sellerReply;
    private Date replyDate;
}
