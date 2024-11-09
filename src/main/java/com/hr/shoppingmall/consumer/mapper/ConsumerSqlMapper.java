package com.hr.shoppingmall.consumer.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.consumer.dto.ConsumerAdressDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;

@Mapper
public interface ConsumerSqlMapper {

    void createConsumer(ConsumerDto consumerDto);
    ConsumerDto findByIdAndPassword(ConsumerDto consumerDto);
    void createConsumerAdress(ConsumerAdressDto  consumerAdressDto);
}
