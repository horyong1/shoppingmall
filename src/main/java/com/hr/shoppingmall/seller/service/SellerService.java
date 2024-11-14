package com.hr.shoppingmall.seller.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;
import com.hr.shoppingmall.shop.dto.ProductDto;

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

    public SellerDto findByNo(int sellerNo){
        return sellerSqlMapper.findByNo(sellerNo);
    }

    /**
     * 판매자 제품 등록
     * @param productDto
     */
    public void registerProduct(ProductDto productDto){
        sellerSqlMapper.createProduct(productDto);
    }

    /**
     * 판매자 제품 삭제
     * @param productDto
     */
    public void deleteProduct(int productNo){
        sellerSqlMapper.removeProduct(productNo);
    }

    public void updateProduct(ProductDto productDto){
        sellerSqlMapper.updateProduct(productDto);
    }

    /**
     * 판매자 제품 리스트
     * @param sellerNo
     * @return
     */
    public List<ProductDto> sellerProductList(int sellerNo){
        return sellerSqlMapper.productFindBySellerNo(sellerNo);
    }

    /**
     * 판매자 제품 상세 정보
     * @param productNo
     * @return
     */
    public ProductDto getProductInfo(int productNo){
        return sellerSqlMapper.productFindBySellerNoAndProductNo(productNo);
    }

}
