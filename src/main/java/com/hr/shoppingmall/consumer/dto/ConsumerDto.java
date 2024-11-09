package com.hr.shoppingmall.consumer.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ConsumerDto {
    private int consumerNo;
    private String consumerId;
    private String password;
    private String nickname;
    private String gender;
    private Date createdAt;
}
