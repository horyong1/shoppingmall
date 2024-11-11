package com.hr.shoppingmall.seller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;

@Service
public class SellerService {

    @Autowired
    private SellerSqlMapper sellerSqlMapper;

    public void registerSeller(SellerDto sellerDto){
        sellerSqlMapper.createSeller(sellerDto);
    }

    public SellerDto findByIdAndPassword(SellerDto sellerDto){
        return sellerSqlMapper.findByIdAndPassword(sellerDto);
    }

    public SellerDto findById(int sellerNo){
        return sellerSqlMapper.findById(sellerNo);
    }
}
