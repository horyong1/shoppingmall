package com.hr.shoppingmall.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hr.shoppingmall.common.dto.RestResponseDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.service.ConsumerService;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/consumer")
public class RestConsumerController {

    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private ShopService shopService;
    // 세션 로그인 체크
    @RequestMapping("getSessionConsumerInfo")
    public RestResponseDto getSessionConsumerInfo(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        if(consumerInfo == null){
            restResponseDto.setResult("fail");
            restResponseDto.setReason("인증X");
        
            return restResponseDto;
        }
        restResponseDto.setResult("success");
        restResponseDto.setReason("인증O");
        restResponseDto.add("consumerNo", consumerInfo.getConsumerNo());

        return restResponseDto;
    }

    // 상품 찜 목록
    @RequestMapping("productWishList")
    public RestResponseDto productWishList(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        
        if(consumerInfo == null){
            restResponseDto.setResult("fail");
            restResponseDto.setReason("인증X");
        
            return restResponseDto;
        }
        
        restResponseDto.add("productWishList", shopService.getWishlist(consumerInfo.getConsumerNo()));
        
        
        return restResponseDto;
    }

    // 브랜드 찜 목록
    @RequestMapping("sellerWishList")
    public RestResponseDto sellerWishList(HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        
        if(consumerInfo == null){
            restResponseDto.setResult("fail");
            restResponseDto.setReason("인증X");
        
            return restResponseDto;
        }

        restResponseDto.add("sellerWishList", shopService.getWishlistByConsumerNo(consumerInfo.getConsumerNo()));
        
        return restResponseDto; 
    }
}
