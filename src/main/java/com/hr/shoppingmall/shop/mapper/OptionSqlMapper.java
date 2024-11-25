package com.hr.shoppingmall.shop.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.hr.shoppingmall.shop.dto.OptionCombinationsDto;
import com.hr.shoppingmall.shop.dto.OptionDetailDto;
import com.hr.shoppingmall.shop.dto.OptionDto;

@Mapper
public interface OptionSqlMapper {
    // option method
    // 옵션 생성
    void createOption(OptionDto optionDto);
    // 옵션 리스트
    List<OptionDto> optionFindByProductNo(int productNo);
    // 옵션이름 중복 확인
    OptionDto optionFindByOptionName(String optionName);


    // optionDetail method
    // 옵션상세 생성
    void createOptionDetail(OptionDetailDto detailDto);
    // 옵션번호 기준 옵션 상세 리스트
    List<OptionDetailDto> optionDetailFindByOptinNo(int optionNo);
    


    // optionMapping method
    // 옵션 매핑 생성
    void createOptionCombination(OptionCombinationsDto mappingDto);
    // 옵션 매핑 리스트 가져오기
    List<OptionCombinationsDto> optionCombinationFindByProductNo(int productNo);
}
