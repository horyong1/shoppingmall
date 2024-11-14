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
import com.hr.shoppingmall.shop.mapper.ReviewSqlMapper;

@Service
public class ReviewService {
    @Autowired
    private ReviewSqlMapper reviewSqlMapper;
    @Autowired
    private ConsumerSqlMapper consumerSqlMapper;
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
}
