package com.hr.shoppingmall.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.shop.dto.OptionDetailDto;
import com.hr.shoppingmall.shop.dto.OptionDto;
import com.hr.shoppingmall.shop.dto.OptionMappingDto;
import com.hr.shoppingmall.shop.mapper.OptionSqlMapper;

@Service
public class OptionService {

    @Autowired
    private OptionSqlMapper optionSqlMapper;

    public void registerOption(OptionDto optionDto, OptionDetailDto optionDetailDto, int productNo){
        // 옵션 등록
        optionSqlMapper.createOption(optionDto);
        
        // 옵션 상세 등록
        optionDetailDto.setOptionNo(optionDto.getOptionNo());
        optionSqlMapper.createOptionDetail(optionDetailDto);

        System.out.println("옵션디테일 값 확인"+optionDetailDto);
        // 옵션 매핑 등록
        OptionMappingDto mappingDto = new OptionMappingDto();
        mappingDto.setProductNo(productNo);
        mappingDto.setOptionNo(optionDto.getOptionNo());
        mappingDto.setOptionDetailNo(optionDetailDto.getOptionDetailNo());
        optionSqlMapper.createOptionMapping(mappingDto);

    }


    /**
     * 판매자 제품 옵션 리스트 가져오기
     * @param productNo
     * @param sellerNo
     * @return
     */
    public List<Map<String,Object>> getOptionsByProductNo(int productNo, int sellerNo){
        List<Map<String,Object>> list = new ArrayList<>();

        List<OptionMappingDto> optionMappingDtoList = optionSqlMapper.optionMappingFindByProductNo(productNo);

        for(OptionMappingDto optionMappingDto : optionMappingDtoList){
            List<Map<String,Object>> oplist = new ArrayList<>();
            Map<String,Object> opMappingMap = new HashMap<>();
            List<OptionDto> optionDtoList = optionSqlMapper.optionFindByOptinNo(sellerNo, optionMappingDto.getOptionNo());
            
            for(OptionDto optionDto : optionDtoList){
                Map<String,Object> opDetailMap = new HashMap<>();
                List<OptionDetailDto> optionDetailDtoList = optionSqlMapper.optionDetailFindByOptinNo(optionDto.getOptionNo());

                opDetailMap.put("optionDto", optionDto);
                opDetailMap.put("optionDetailDtoList", optionDetailDtoList);
                
                oplist.add(opDetailMap);
            }

            opMappingMap.put("optionMappingDto", optionMappingDto);
            opMappingMap.put("optionDtoList", optionDtoList);

            list.add(opMappingMap);
        }

        return list;
    }
}
