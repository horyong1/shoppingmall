package com.hr.shoppingmall.seller.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.shop.dto.ProductDetailImageDto;
import com.hr.shoppingmall.shop.dto.ProductDto;

@Mapper
public interface SellerSqlMapper {
    void createSeller(SellerDto sellerDto);
    SellerDto findByNo(int sellerNo);
    SellerDto findByIdAndPassword(SellerDto sellerDto);
    void createProduct(ProductDto productDto);
    void removeProduct(int productNo);
    
    // 판매자 제품 리스트
    List<ProductDto> productFindBySellerNo(int sellerNo);

    // 제품 찾기
    ProductDto productFindBySellerNoAndProductNo(int productNo);

    // 상품 업데이트
    void updateProduct(ProductDto productDto);
    
    // 상세 이미지 등록
    void createProductDetailImage(ProductDetailImageDto detailImageDto);

    // 상세 이미지 삭제
    void removeProductDetailImage(int product);

    Map<String,Integer> checkShipmentStatusUpdates(int sellerNo);
}
