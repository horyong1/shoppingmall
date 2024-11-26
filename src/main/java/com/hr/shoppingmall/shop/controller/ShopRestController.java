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
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.ProductWishlistDto;
import com.hr.shoppingmall.shop.dto.SellerWishListDto;
import com.hr.shoppingmall.shop.service.ReviewService;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("api/shop")
public class ShopRestController {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ReviewService reviewService;

    // 카테고리별 상세 제품 가져오기
    @RequestMapping("getProductsByCategory")
    public RestResponseDto getProductsByCategory(@RequestParam("categoryNo")int categoryNo,
        @RequestParam("categoryMediumNo")int categoryMediumNo, HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();
        
        ConsumerDto consumerInfo = getConsumerInfo(session);
        int consumerNo = 0;
        if(consumerInfo != null){
            consumerNo = consumerInfo.getConsumerNo();
        }
        ProductDto selectCategory = new ProductDto();
        selectCategory.setCategoryNo(categoryNo);
        selectCategory.setCategoryMediumNo(categoryMediumNo);
        List<Map<String,Object>> list = shopService.getProductCategoryList(selectCategory, consumerNo);
        
        if (list == null) {
            restResponseDto.setResult("fail!!!");
            return restResponseDto;
        }
        restResponseDto.setResult("success!!!");
        restResponseDto.add("categoryProductList", list);
        restResponseDto.add("categoryNo", categoryNo);
        restResponseDto.add("categoryMediumList", shopService.getProductCategoryMediumList(categoryNo));
        
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

    // 상품 상세정보 모달
    @RequestMapping("productDetailModal")
    public RestResponseDto productDetailModal(@RequestParam(value="productNo")int productNo,HttpSession session){
        RestResponseDto restResponseDto = new RestResponseDto();

        ConsumerDto consumerInfo = getConsumerInfo(session);
        int consumerNo = 0;
        if(consumerInfo != null){
            consumerNo = consumerInfo.getConsumerNo();
        }
        ProductWishlistDto wishlistDto = new ProductWishlistDto();
        wishlistDto.setConsumerNo(consumerNo);
        wishlistDto.setProductNo(productNo);
        restResponseDto.add("productList", shopService.getProductDetail(productNo));
        restResponseDto.add("isHeart",shopService.isConsumerProductWishList(wishlistDto));
        SellerWishListDto sellerWishListDto = new SellerWishListDto();
        sellerWishListDto.setConsumerNo(consumerNo);
        restResponseDto.add("isSeller", shopService.getSellerWishList(sellerWishListDto,productNo));
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