package com.hr.shoppingmall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.dto.ProductDto;

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

}
