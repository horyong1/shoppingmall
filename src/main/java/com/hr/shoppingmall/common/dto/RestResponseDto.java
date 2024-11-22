package com.hr.shoppingmall.common.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class RestResponseDto {
    private String result;  // success, fail 로 설정
    private String reason;  // 실패 이유 
    private Map<String,Object> data = new HashMap<>();

    public void add(String name, Object value){
        data.put(name, value);
    }
}
