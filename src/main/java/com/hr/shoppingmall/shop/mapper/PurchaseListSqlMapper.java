package com.hr.shoppingmall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.shop.dto.PurchaseListDto;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;

@Mapper
public interface PurchaseListSqlMapper {
    // 결제목록 가져오기
    List<PurchaseListDto> purchaseListFindByPurchaseNo(int purchaseNo);
    // 상품 구매 생성
    void createPurchase(ShoppingPurchaseDto purchaseDto);
    // 상품 구매 리스트 생성
    void createPurchaseList(PurchaseListDto purchaseListDto);
    // 주문 번호 기준 단일 상품 정보 가져오기
    PurchaseListDto purchaseListFindByPurchaseNoAndProductNo(PurchaseListDto purchaseListDto);
    int productPurchaseCount(int productNo);
}
