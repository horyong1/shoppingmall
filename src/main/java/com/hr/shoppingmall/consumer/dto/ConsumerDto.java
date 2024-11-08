package com.hr.shoppingmall.consumer.dto;

import java.util.Date;

import lombok.Data;

@Data
public class ConsumerDto {
    private int no;
    private String id;
    private String password;
    private String nickname;
    private String gender;
    private Date createdAt;
}
