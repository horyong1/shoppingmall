package com.hr.shoppingmall.consumer.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.consumer.dto.ConsumerAdressDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.dto.ProductReviewDto;

@Mapper
public interface ConsumerSqlMapper {

    // 고객 등록
    void createConsumer(ConsumerDto consumerDto);
    // 고객 로그인 확인
    ConsumerDto findByIdAndPassword(ConsumerDto consumerDto);
    // 고객 번호 정보 가져오기
    ConsumerDto findByNo(int consumerNo);
    // 고객 배송지 등록
    void createConsumerAdress(ConsumerAdressDto  consumerAdressDto);
    // 고객 배송지 목록
    List<ConsumerAdressDto> adresslistFindByConsumerId(int consumerNo);
    // 고객 기본 배송지 
    ConsumerDto adressFindByConsumerId(int consumerNo);
    // 고객 배송지 삭제
    void deleteAdress(ConsumerAdressDto adressDto);
    
    void updateDefaulteAdress(ConsumerDto consumerDto);
    


}
