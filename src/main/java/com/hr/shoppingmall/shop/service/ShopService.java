package com.hr.shoppingmall.shop.service;

import java.nio.MappedByteBuffer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;
import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.mapper.ShopSqlMapper;

@Service
public class ShopService {

    @Autowired
    private ShopSqlMapper shopSqlMapper;
    @Autowired
    private SellerSqlMapper sellerSqlMapper;

    // 카테고리 목록 가져오기
    public Map<String,String> getCategoryList(){
        Map<String,String> map = new HashMap<>();
        int cnt = 1;
        for(ProductCategoryDto dto : shopSqlMapper.categoryFindAll()){
            String value = String.valueOf(cnt);
            map.put(value, dto.getCategoryName());
            cnt++;
        }
        return map;
    }

    // 메인페이지 상품 목록 6개만 가져오기
    public List<Map<String,Object>> getProductShow(int no){
        List<Map<String,Object>> list = new ArrayList<>();
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        for(ProductDto dto : shopSqlMapper.productLimitFindCategoryId(no)){

            Map<String,Object> map = new HashMap<>();
            map.put("productNo", dto.getProductNo()); 
            map.put("categoryNo", dto.getCategoryNo()); 
            map.put("sellerNo", dto.getSellerNo()); 
            map.put("productName", dto.getProductName()); 
            // map.put("productDescription", dto.getProductNo()); 
            map.put("price", decimalFormat.format(dto.getPrice())); 
            map.put("mainImageUrl", dto.getMainImageUrl()); 
            // map.put("totalQuantity", dto.getProductNo()); 
            // map.put("createdAt", dto.getProductNo()); 
            list.add(map);  
        }
        return list;
    }

    // 상품 상세정보 가져오기
    public List<Map<String,Object>> getProductDetail(int productNo){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();

        ProductDto productDto = shopSqlMapper.findByProductNo(productNo);
        SellerDto sellerDto = sellerSqlMapper.findById(productDto.getSellerNo());
        
        map.put("productDto", productDto);
        map.put("sellerDto", sellerDto);

        list.add(map);
        
        return list;
    }
}
