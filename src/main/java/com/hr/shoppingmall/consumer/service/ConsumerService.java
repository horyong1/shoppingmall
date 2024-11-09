package com.hr.shoppingmall.consumer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.consumer.dto.ConsumerAdressDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.mapper.ConsumerSqlMapper;

@Service
public class ConsumerService {

    @Autowired 
    private ConsumerSqlMapper consumerSqlMapper;

    // 고객 회원가입 
    public void registerConsumer(ConsumerDto consumerDto, String adress){
        ConsumerAdressDto consumerAdressDto = new ConsumerAdressDto();
        consumerSqlMapper.createConsumer(consumerDto);
        
        consumerAdressDto.setConsumerNo(consumerDto.getConsumerNo());
        consumerAdressDto.setConsumerAdress(adress);
        consumerSqlMapper.createConsumerAdress(consumerAdressDto);

    }

    // 로그인 확인
    public ConsumerDto loginCheck(ConsumerDto consumerDto){
        return consumerSqlMapper.findByIdAndPassword(consumerDto);
    }
}
