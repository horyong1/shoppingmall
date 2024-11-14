package com.hr.shoppingmall.seller.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.shop.dto.ProductDto;

@Mapper
public interface SellerSqlMapper {
    void createSeller(SellerDto sellerDto);
    SellerDto findByNo(int sellerNo);
    SellerDto findByIdAndPassword(SellerDto sellerDto);
    void createProduct(ProductDto productDto);
    void removeProduct(int productNo);
    List<ProductDto> productFindBySellerNo(int sellerNo);
    ProductDto productFindBySellerNoAndProductNo(int productNo);
    void updateProduct(ProductDto productDto);
}
