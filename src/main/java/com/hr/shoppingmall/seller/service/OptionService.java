package com.hr.shoppingmall.seller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.shop.dto.OptionDetailDto;
import com.hr.shoppingmall.shop.dto.OptionDto;
import com.hr.shoppingmall.shop.dto.OptionCombinationsDto;
import com.hr.shoppingmall.shop.mapper.OptionSqlMapper;

@Service
public class OptionService {

    @Autowired
    private OptionSqlMapper optionSqlMapper;

    // 옵션 등록
    public void registerOption(OptionDto optionDto, String optionDetailName, int productNo){
        OptionDto optionCheck = optionSqlMapper.optionFindByOptionName(optionDto.getOptionName());
        OptionDetailDto detailDto = new OptionDetailDto();

        if(optionCheck == null){
            // 옵션 등록
            optionSqlMapper.createOption(optionDto);
            // 옵션 상세 등록
            detailDto.setOptionNo(optionDto.getOptionNo());
            detailDto.setOptionDetailName(optionDetailName);
            optionSqlMapper.createOptionDetail(detailDto);
        }else{
            detailDto.setOptionNo(optionCheck.getOptionNo());
            detailDto.setOptionDetailName(optionDetailName);
            optionSqlMapper.createOptionDetail(detailDto);
        }
        

        // 옵션 매핑 등록
        OptionCombinationsDto mappingDto = new OptionCombinationsDto();

    }

    // 옵션 상세 등록 
    public void registerOptionDetail(List<OptionDto> params,List<String> detailNames, int stock){
        String name ="";
        
        
    }


    /**
     * 제품 옵션 리스트 가져오기
     * @param productNo
     * @param sellerNo
     * @return
     */
    public List<Map<String,Object>> getOptionsByProductNo(int productNo){
        List<Map<String,Object>> list = new ArrayList<>();

        List<OptionDto> optionDtos = optionSqlMapper.optionFindByProductNo(productNo);
        for(OptionDto optionDto : optionDtos){
            Map<String, Object> map = new HashMap<>();
            List<OptionDetailDto> detailDto = optionSqlMapper.optionDetailFindByOptinNo(optionDto.getOptionNo());

            System.out.println(detailDto);

            map.put("optionDto", optionDto);
            map.put("detailDto", detailDto);

            list.add(map);
        }

        return list;
    }
}
