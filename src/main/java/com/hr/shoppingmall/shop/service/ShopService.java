package com.hr.shoppingmall.shop.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.consumer.dto.ConsumerAdressDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.mapper.ConsumerSqlMapper;
import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;
import com.hr.shoppingmall.shop.dto.CartDto;
import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.ProductWishlistDto;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;
import com.hr.shoppingmall.shop.mapper.ReviewSqlMapper;
import com.hr.shoppingmall.shop.mapper.ShopSqlMapper;

@Service
public class ShopService {

    @Autowired
    private ShopSqlMapper shopSqlMapper;
    @Autowired
    private ConsumerSqlMapper consumerSqlMapper;
    @Autowired
    private SellerSqlMapper sellerSqlMapper;
    @Autowired
    private ReviewSqlMapper reviewSqlMapper;

    /**
     * 카테고리 전체 목록
     * @return Map
     */
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

    /**
     * 메인페이지 상품 목록 6개 가져오기
     * @param no
     * @return List
     */
    public List<Map<String,Object>> getProductShow(int no){
        List<Map<String,Object>> list = new ArrayList<>();
        for(ProductDto dto : shopSqlMapper.productLimitFindCategoryId(no,6)){

            String price = decimelFormatter(dto.getPrice());
            Map<String,Object> map = new HashMap<>();
            map.put("productDto", dto);
            map.put("price",price);
           
            list.add(map);  
        }
        return list;
    }

    /**
     * 상품 상세정보 
     * @param productNo
     * @return Map
     */
    public Map<String,Object> getProductDetail(int productNo){
        Map<String,Object> map = new HashMap<>();
        
        ProductDto dto = shopSqlMapper.findByProductNo(productNo);
        String priceTrans = decimelFormatter(dto.getPrice());
        SellerDto sellerDto = sellerSqlMapper.findByNo(dto.getSellerNo());
        int reviewCount = reviewSqlMapper.reviewConut(productNo);

        map.put("reviewCount", reviewCount);
        map.put("productDto", dto);
        map.put("price",priceTrans);
        map.put("sellerDto",sellerDto);
        return map;
    }

    /**
     * 고객 상품 구매 등록
     * @param consumerNo
     * @param productNo
     * @param quantity
     * @return List<Map<String,Object>>
     */
    public List<Map<String,Object>> registerPurchase(ShoppingPurchaseDto purchaseDto){
        List<Map<String,Object>> list = new ArrayList<>();
        Map<String,Object> map = new HashMap<>();
        ConsumerDto consumerDto = consumerSqlMapper.findByNo(purchaseDto.getConsumerNo());
        System.out.println("주소 " + purchaseDto);
        
        purchaseDto.setShoppingAdress(consumerDto.getAdress());

        shopSqlMapper.createPurchase(purchaseDto);
        shopSqlMapper.updateTotalQuantity(purchaseDto);
        
        ProductDto productDto = shopSqlMapper.findByProductNo(purchaseDto.getProductNo());
        String totalPrice = decimelFormatter(purchaseDto.getQuantity() * productDto.getPrice());
        SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());
        
        map.put("productDto",productDto);
        map.put("totalPrice",totalPrice);
        map.put("purchaseDto",shopSqlMapper.purchaseFindByConsumerNoAndPurchaseNo(purchaseDto));
        map.put("sellerDto", sellerDto);

        list.add(map);

        return list;
    }

    /**
     * 고객 상품 구매 목록 리스트 
     * @param consumerNo
     * @return List<Map<String,Object>>
     */
    public List<Map<String,Object>> getPurchaseList(int consumerNo){
        List<Map<String,Object>> list = new ArrayList<>();
        List<ShoppingPurchaseDto> purchaseList = shopSqlMapper.purchaseFindByConsumerNo(consumerNo);
        DecimalFormat decimalFormat = new DecimalFormat("#,###");

        for(ShoppingPurchaseDto purchaseDto : purchaseList){
            Map<String, Object> map = new HashMap<>();
            
            int productNo = purchaseDto.getProductNo();
            ProductDto productDto =  shopSqlMapper.findByProductNo(productNo);

            String resultPrice = decimalFormat.format(productDto.getPrice()*purchaseDto.getQuantity());

            map.put("productDto", productDto);
            map.put("purchaseDto", purchaseDto);
            map.put("totalPrice",resultPrice);
            
            list.add(map);
        }
        return list;
    }

    /**
     * 주문 내역 리스트 상세 정보
     * @param purchaseNo
     * @param consumerNo
     * @return Map<String,Object>
     */
    public Map<String,Object> getPurchaseDetailInfo(int purchaseNo, int consumerNo){
        Map<String,Object> map = new HashMap<>();
        ShoppingPurchaseDto purchaseDto = new ShoppingPurchaseDto();
        purchaseDto.setPurchaseNo(purchaseNo);
        purchaseDto.setConsumerNo(consumerNo);
        
        purchaseDto = shopSqlMapper.purchaseFindByConsumerNoAndPurchaseNo(purchaseDto);
        ProductDto productDto = shopSqlMapper.findByProductNo(purchaseDto.getProductNo());
        String totalPrice = decimelFormatter(productDto.getPrice() * purchaseDto.getQuantity());
        ConsumerDto consumerDto = consumerSqlMapper.findByNo(consumerNo);
        map.put("purchaseDto", purchaseDto);
        map.put("productDto", productDto);
        map.put("totalPrice", totalPrice);
        map.put("consumerDto", consumerDto);
        map.put("seller", sellerSqlMapper.findByNo(productDto.getSellerNo()));

        return map;
    }

    /**
     * 고객 상품 구매 상세 정보
     * @param consmerNo
     * @param productNo
     * @return ShoppingPurchaseDto
     */
    public ShoppingPurchaseDto getPurchaseInfo(int consmerNo, int productNo){
        ShoppingPurchaseDto purchaseDto = new ShoppingPurchaseDto();
        purchaseDto.setConsumerNo(consmerNo);
        purchaseDto.setProductNo(productNo);
        return shopSqlMapper.purchaseFindByConsumerNoAndPurchaseNo(purchaseDto);
    }

    /**
     * 찜 목록 리스트
     * @param consumerNo
     * @return
     */
    public List<Map<String, Object>> getWishlist(int consumerNo){
        List<Map<String, Object>> list = new ArrayList<>();

        for(ProductWishlistDto wishlistDto : shopSqlMapper.wishlistFindByConsumerNo(consumerNo)){
            Map<String, Object> map = new HashMap<>();
            ProductDto productDto = shopSqlMapper.findByProductNo(wishlistDto.getProductNo());
            SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());
            String priceTans = decimelFormatter(productDto.getPrice());
            int wishlistCount = shopSqlMapper.wishlistCount(productDto.getProductNo());
            int reviewCount = reviewSqlMapper.reviewConut(productDto.getProductNo());

            map.put("reviewCount", reviewCount);
            map.put("wishlistCount",wishlistCount);
            map.put("priceTans", priceTans);
            map.put("sellerDto",sellerDto);
            map.put("productDto", productDto);
            map.put("wishlistDto", wishlistDto);

            list.add(map);

        }
        return list;
    }

    /** 
     * 찜목록 중 특정 상품 확인
     * @param wishlistDto
     * @return
    */
    public ProductWishlistDto getWishlistPruduct(ProductWishlistDto wishlistDto){
        return shopSqlMapper.wishlistFindByConsumerNoAndProductNo(wishlistDto);
    }

    /**
     * 상품 찜 추가, 삭제
     * @param wishlistDto
     */
    public void toggleWishlist(ProductWishlistDto wishlistDto){
        ProductWishlistDto dto = new ProductWishlistDto();
        dto = shopSqlMapper.wishlistFindByConsumerNoAndProductNo(wishlistDto);
    
        if(dto == null){
            shopSqlMapper.addToWishlist(wishlistDto);
        }else{
            shopSqlMapper.removeFromWishlist(wishlistDto);
        }
    }


    public List<Map<String,Object>> getConsumerCartList(int consumerNo){
        List<Map<String,Object>> list = new ArrayList<>();

        for(CartDto cartDto : shopSqlMapper.cartFindByConsumerNo(consumerNo)){
            Map<String,Object> map = new HashMap<>();
            ProductDto productDto = shopSqlMapper.findByProductNo(cartDto.getProductNo());
            SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());

            String resultPrice = decimelFormatter(productDto.getPrice() * cartDto.getQuantity());

            map.put("cartDto", cartDto);
            map.put("productDto", productDto);
            map.put("sellerDto", sellerDto);
            map.put("resultPrice", resultPrice);

            list.add(map);
        }

        return list;
    }

    /**
     * 금액 #,### 포멧터
     * @param price
     * @return String
     */ 
    private String decimelFormatter(int price){
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        String resultPrice = decimalFormat.format(price);
        return resultPrice;
    }

    // // Map 값 세팅하기
    // private Map<String,Object> productSettingMap(ProductDto dto){
    //     Map<String,Object> map = new HashMap<>();
    //     DecimalFormat decimalFormat = new DecimalFormat("#,###");
        
    //     map.put("productNo", dto.getProductNo()); 
    //     map.put("categoryNo", dto.getCategoryNo()); 
    //     map.put("sellerNo", dto.getSellerNo()); 
    //     map.put("productName", dto.getProductName()); 
    //     map.put("productDescription", dto.getProductDescription()); 
    //     map.put("price", decimalFormat.format(dto.getPrice())); 
    //     map.put("mainImageUrl", dto.getMainImageUrl()); 
    //     map.put("totalQuantity", dto.getTotalQuantity()); 
    //     map.put("createdAt", dto.getCreatedAt());
        
    //     return map; 
    // }


}
