package com.hr.shoppingmall.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.dto.ProductReviewDto;
import com.hr.shoppingmall.consumer.mapper.ConsumerSqlMapper;
import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.PurchaseListDto;
import com.hr.shoppingmall.shop.mapper.PurchaseListSqlMapper;
import com.hr.shoppingmall.shop.mapper.ReviewSqlMapper;
import com.hr.shoppingmall.shop.mapper.ShopSqlMapper;

@Service
public class ReviewService {
    @Autowired
    private ReviewSqlMapper reviewSqlMapper;
    @Autowired
    private ConsumerSqlMapper consumerSqlMapper;
    @Autowired
    private ShopSqlMapper shopSqlMapper;
    @Autowired
    private PurchaseListSqlMapper purchaseListSqlMapper;
    @Autowired
    private SellerSqlMapper sellerSqlMapper;

    /**
     * 리뷰 등록
     * @param reviewDto
     */
    public void registerReview(ProductReviewDto reviewDto){
        reviewSqlMapper.createReview(reviewDto);
    }

    /**
     * 리뷰 수정
     * @param reviewDto
     */
    public void updateReview(ProductReviewDto reviewDto){
        reviewSqlMapper.updateReview(reviewDto);
    }

    /**
     * 제품 리뷰 리스트
     * @param productNo
     * @return
     */
    public List<Map<String,Object>> getReviewList(int productNo){
        List<Map<String,Object>> list = new ArrayList<>();

        for(ProductReviewDto reviewDto : reviewSqlMapper.reviewFindByProductNo(productNo)){
            
            ConsumerDto consumerDto =consumerSqlMapper.findByNo(reviewDto.getConsumerNo());
            Map<String,Object> map = new HashMap<>(); 
            
            map.put("consumerDto", consumerDto);
            map.put("reviewDto", reviewDto);

            list.add(map);
        }

        return list;
    }

    /**
     * 제품 리뷰 개수 
     * @param productNo
     * @return
     */
    public int reviewConut(int productNo){
        return reviewSqlMapper.reviewConut(productNo);
    }

    public List<Map<String,Object>> getMyReviewList(int consumerNo){
        List<Map<String,Object>> list = new ArrayList<>();

        for(ProductReviewDto reviewDto : reviewSqlMapper.reviewFindByConsumerNo(consumerNo)){
            Map<String,Object> map = new HashMap<>(); 
            
            ConsumerDto consumerDto = consumerSqlMapper.findByNo(reviewDto.getConsumerNo());
            ProductDto productDto = shopSqlMapper.findByProductNo(reviewDto.getProductNo());
            SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());
            
            map.put("consumerDto", consumerDto);
            map.put("reviewDto", reviewDto);
            map.put("productDto", productDto);
            map.put("sellerDto", sellerDto);

            list.add(map);
        }

        return list;
    }
    
    /**
     * 주문번호 기분 리뷰 작성 할 제품 
     * @param params
     * @return
     */
    public Map<String,Object> getReviewitem(PurchaseListDto params){
        Map<String,Object> map = new HashMap<>();

        PurchaseListDto purchaseListDto = purchaseListSqlMapper.purchaseListFindByPurchaseNoAndProductNo(params);
        ProductDto productDto = shopSqlMapper.findByProductNo(purchaseListDto.getProductNo());

        map.put("purchaseListDto", purchaseListDto);
        map.put("productDto",productDto);

        return map;
    }


    /**
     * 제품 리뷰 목록
     * @param productNo
     * @return
     */
    public List<Map<String, Object>> getProductReviewList(int productNo){
        List<Map<String, Object>> list = new ArrayList<>();
        
        List<ProductReviewDto> reviewDtos = reviewSqlMapper.reviewFindByProductNo(productNo);
        for(ProductReviewDto reviewDto : reviewDtos){
            Map<String, Object> map = new HashMap<>();
            ProductDto productDto = shopSqlMapper.findByProductNo(productNo);
            SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());
            ConsumerDto consumerDto = consumerSqlMapper.findByNo(reviewDto.getConsumerNo());

            map.put("reviewDto", reviewDto);
            map.put("productDto", productDto);
            map.put("sellerDto", sellerDto);
            map.put("consumerDto", consumerDto);

            list.add(map);
        }

        return list;
    }

    public void registerReply(ProductReviewDto reviewDto){
        reviewSqlMapper.updateReply(reviewDto);

    }
    /**
     * 리뷰 평점
     * @param productNo
     * @return
     */
    public double reviewAvg(int productNo){
        return reviewSqlMapper.reviewAvg(productNo);
    }
}
