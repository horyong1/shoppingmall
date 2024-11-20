package com.hr.shoppingmall.seller.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.consumer.dto.ProductReviewDto;
import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;
import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.dto.ProductCategoryMediumDto;
import com.hr.shoppingmall.shop.dto.ProductDetailImageDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.mapper.ReviewSqlMapper;
import com.hr.shoppingmall.shop.mapper.ShopSqlMapper;

@Service
public class SellerService {

    @Autowired
    private SellerSqlMapper sellerSqlMapper;
    @Autowired
    private ReviewSqlMapper reviewSqlMapper;
    @Autowired
    private ShopSqlMapper shopSqlMapper;

    public void registerSeller(SellerDto sellerDto){
        sellerSqlMapper.createSeller(sellerDto);
    }

    public SellerDto findByIdAndPassword(SellerDto sellerDto){
        return sellerSqlMapper.findByIdAndPassword(sellerDto);
    }

    public SellerDto findByNo(int sellerNo){
        return sellerSqlMapper.findByNo(sellerNo);
    }
    
    public List<ProductCategoryDto> getCategoryList(){
        return shopSqlMapper.categoryFindAll();
    }

    public List<ProductCategoryMediumDto> getCategoryMediumList(){
        return shopSqlMapper.categoryMediumFindAll();
    }


    /**
     * 판매자 제품 등록
     * @param productDto
     */
    public void registerProduct(ProductDto productDto, List<String> mainUrlList ,List<String> detailUrlList){
        productDto.setMainImageUrl(mainUrlList.get(0));
        sellerSqlMapper.createProduct(productDto);

        ProductDetailImageDto detailImageDto = new ProductDetailImageDto();
        detailImageDto.setProductNo(productDto.getProductNo());
        
        for(String detailUrl : detailUrlList){
            detailImageDto.setImageLink(detailUrl);
            sellerSqlMapper.createProductDetailImage(detailImageDto);
        }

    }

    /**
     * 판매자 제품 삭제
     * @param productDto
     */
    public void deleteProduct(int productNo){
        sellerSqlMapper.removeProduct(productNo);
    }

    /**
     * 판매자 제품 정보 수정
     * @param productDto
     * @param mainUrlList
     * @param detailUrlList
     */
    public void updateProduct(ProductDto productDto, List<String> mainUrlList ,List<String> detailUrlList){
        if(mainUrlList.size() > 0){
            productDto.setMainImageUrl(mainUrlList.get(0));
        }else{
            System.out.println("메인 이미지가 비어있습니다.");
            productDto.setMainImageUrl(null);
        }
        sellerSqlMapper.updateProduct(productDto);
        
        if (detailUrlList.size() > 0) {
            ProductDetailImageDto detailImageDto = new ProductDetailImageDto();
            detailImageDto.setProductNo(productDto.getProductNo());
            sellerSqlMapper.removeProductDetailImage(detailImageDto.getProductNo());

            for(String detailUrl : detailUrlList){
                detailImageDto.setImageLink(detailUrl);
                sellerSqlMapper.createProductDetailImage(detailImageDto);
            }
            
        }else{
            System.out.println("상세 이미지가 비어있습니다.");
        }
    }

    /**
     * 판매자 제품 리스트
     * @param sellerNo
     * @return
     */
    public List<ProductDto> sellerProductList(int sellerNo){
        return sellerSqlMapper.productFindBySellerNo(sellerNo);
    }

    /**
     * 판매자 제품 상세 정보
     * @param productNo
     * @return
     */
    public ProductDto getProductInfo(int productNo){
        return sellerSqlMapper.productFindBySellerNoAndProductNo(productNo);
    }

    public List<Map<String,Object>> getProuctList(int sellerNo){
        List<Map<String,Object>> list = new ArrayList<>();
        for(ProductDto productDto : sellerSqlMapper.productFindBySellerNo(sellerNo)){
            Map<String,Object> map = new HashMap<>();

            map.put("reviewCount", reviewSqlMapper.reviewConut(productDto.getProductNo()));
            map.put("productDto",productDto);

            list.add(map);
        }

        return list;
    }

}
