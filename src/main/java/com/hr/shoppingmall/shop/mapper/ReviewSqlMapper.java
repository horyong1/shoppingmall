package com.hr.shoppingmall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.consumer.dto.ProductReviewDto;

@Mapper
public interface ReviewSqlMapper {
    // 리뷰생성
    void createReview(ProductReviewDto reviewDto);
    // 리뷰 수정
    void updateReview(ProductReviewDto reviewDto);
    // 제품 리뷰 목록 가져오기
    List<ProductReviewDto> reviewFindByProductNoAndConsumerNo(ProductReviewDto reviewDto);
    // 제품 리뷰 리스트
    List<ProductReviewDto> reviewFindByProductNo(int productNo);
    // 특정 제품 리뷰 가져오기
    ProductReviewDto reviewFindByProductNoAndConsumerNoAndReviewNo(ProductReviewDto reviewDto);
    // 특정 제품 리뷰 개수
    int reviewConut(int productNo);
    // 내 리뷰 목록 
    List<ProductReviewDto> reviewFindByConsumerNo(int consumerNo);
    // 리뷰 작성 유무 확인
    ProductReviewDto reviewFindByPurchaseNoAndProductNo(ProductReviewDto reviewDto);
    // 리플 생성
    void updateReply(ProductReviewDto reviewDto);
    // 상품 리뷰 평점
    double reviewAvg(int productNo);
}
