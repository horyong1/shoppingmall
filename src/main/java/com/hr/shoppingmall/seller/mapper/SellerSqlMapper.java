package com.hr.shoppingmall.seller.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.seller.dto.SellerDto;

@Mapper
public interface SellerSqlMapper {
    void createSeller(SellerDto sellerDto);
    SellerDto findById(int sellerNo);
    SellerDto findByIdAndPassword(SellerDto sellerDto);
}
