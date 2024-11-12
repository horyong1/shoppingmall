package com.hr.shoppingmall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;

@Mapper
public interface ShopSqlMapper {
    // 카테고리 리스트
    List<ProductCategoryDto> categoryFindAll();
    
    // 상품 목록 카테고리별 조회 가져오기
    List<ProductDto> productFindCategoryId(int categoryNo);
    
    // 상품 목록 6개만 가져오기 카테고리별
    List<ProductDto> productLimitFindCategoryId(int categoryNo);
    
    // 상품 상세 정보
    ProductDto findByProductNo(int productNo);
    
    // 상품 구매 등록
    void createPurchase(ShoppingPurchaseDto purchaseDto);
    
    // 상품 수량 수정 
    void updateTotalQuantity(ShoppingPurchaseDto purchaseDto);
    
    // 고객 상품 구매 목록 
    List<ShoppingPurchaseDto> purchaseFindByConsumerNo(int consumerNo);
    
    // 고객 상품 구매 상세 
    ShoppingPurchaseDto purchaseFindByConsumerNoAndPurchaseNo(ShoppingPurchaseDto purchaseDto);
    
}
