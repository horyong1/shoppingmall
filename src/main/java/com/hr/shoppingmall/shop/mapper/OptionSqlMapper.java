package com.hr.shoppingmall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.hr.shoppingmall.shop.dto.OptionDetailDto;
import com.hr.shoppingmall.shop.dto.OptionDto;
import com.hr.shoppingmall.shop.dto.OptionMappingDto;

@Mapper
public interface OptionSqlMapper {
    // option method
    // 옵션 생성
    void createOption(OptionDto optionDto);
    // 옵션 리스트
    List<OptionDto> optionFindByOptinNo(@Param(value = "sellerNo")int sellerNo,@Param(value = "optionNo") int optionNo);


    // optionDetail method
    // 옵션상세 생성
    void createOptionDetail(OptionDetailDto detailDto);
    // 옵션번호 기준 옵션 상세 리스트
    List<OptionDetailDto> optionDetailFindByOptinNo(int optionNo);


    // optionMapping method
    // 옵션 매핑 생성
    void createOptionMapping(OptionMappingDto mappingDto);
    // 옵션 매핑 리스트 가져오기
    List<OptionMappingDto> optionMappingFindByProductNo(int productNo);
}
