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

    public void registerConsumer(ConsumerDto consumerDto, String adress){
        ConsumerAdressDto adressDto = new ConsumerAdressDto();
        consumerSqlMapper.createConsumer(consumerDto);
        
        adressDto.setUserNo(consumerDto.getNo());
        adressDto.setUserAdress(adress);
        consumerSqlMapper.createConsumerAdress(adressDto);

    }


    public ConsumerDto loginCheck(ConsumerDto consumerDto){
        return consumerSqlMapper.findByIdAndPassword(consumerDto);
    }
}
