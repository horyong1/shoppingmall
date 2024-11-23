package com.hr.shoppingmall.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hr.shoppingmall.common.dto.RestResponseDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.shop.dto.ProductWishlistDto;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/shop")
public class ShopRestController {

    @Autowired
    private ShopService shopService;


    @RequestMapping("getProductsByCategory")
    public RestResponseDto getProductsByCategory(@RequestParam("categoryNo")int categoryNo, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        
        ConsumerDto consumerInfo = getConsumerInfo(session);
        int consumerNo = 0;
        if(consumerInfo != null){
            consumerNo = consumerInfo.getConsumerNo();
        }

        List<Map<String,Object>> list = shopService.getProductCategoryList(categoryNo, consumerNo);
        
        if (list == null) {
            restResponseDto.setResult("fail!!!");
            return restResponseDto;
        }
        restResponseDto.setResult("success!!!");
        restResponseDto.add("categoryProductList", list);
        restResponseDto.add("categoryNo", categoryNo);
        
        return restResponseDto;
    }

    // 찜 클릭 여부
    @RequestMapping("product/{productNo}/wishlist")
    public RestResponseDto isConsumerProductWishList(HttpSession session, @PathVariable("productNo") int productNo){
        RestResponseDto restResponseDto = new RestResponseDto();
        ConsumerDto consumerInfo = getConsumerInfo(session);
        if(consumerInfo == null){
            restResponseDto.setResult(null);
            restResponseDto.setResult("fail!!!");
            restResponseDto.setReason("인증X");
            return restResponseDto;
        }

        ProductWishlistDto  productWishlistDto = new ProductWishlistDto();
        productWishlistDto.setConsumerNo(consumerInfo.getConsumerNo());
        productWishlistDto.setProductNo(productNo);
        shopService.toggleWishlist(productWishlistDto);
        restResponseDto.add("isWishList", shopService.isConsumerProductWishList(productWishlistDto));
        return restResponseDto;
    }

    // 상품 찜 개수 가져오기
    @RequestMapping("{productNo}/wishListCount")
    public RestResponseDto productWishListCount(@PathVariable("productNo")int productNo){
        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.add("wishListCount", shopService.getProductWishListCount(productNo));
        return restResponseDto;
    }

     // 세션 로그인 체크
    private boolean isConsumerLoggedIn(HttpSession session) {
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        return consumerInfo != null;
    }
    // 세션 sellerDto 값 세팅
    private ConsumerDto getConsumerInfo(HttpSession session){
        return (ConsumerDto)session.getAttribute("consumerInfo");
    }
}