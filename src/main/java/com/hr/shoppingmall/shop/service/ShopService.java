package com.hr.shoppingmall.shop.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.mapper.ConsumerSqlMapper;
import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.mapper.SellerSqlMapper;
import com.hr.shoppingmall.shop.dto.CartDto;
import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.ProductWishlistDto;
import com.hr.shoppingmall.shop.dto.PurchaseListDto;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;
import com.hr.shoppingmall.shop.mapper.PurchaseListSqlMapper;
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
    @Autowired
    private PurchaseListSqlMapper purchaseListSqlMapper;

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
    public ShoppingPurchaseDto registerPurchase(ShoppingPurchaseDto purchaseDto, int[] cartNos){
        ConsumerDto consumerDto = consumerSqlMapper.findByNo(purchaseDto.getConsumerNo());
        purchaseDto.setShoppingAdress(consumerDto.getAdress());
        purchaseListSqlMapper.createPurchase(purchaseDto);
        

        for(int cartNo : cartNos){
            PurchaseListDto purchaseListDto = new PurchaseListDto();
            CartDto cartDto = shopSqlMapper.cartFindByCartNo(cartNo);
            if (cartDto == null) {
                return new ShoppingPurchaseDto();
            }
            purchaseListDto.setPurchaseNo(purchaseDto.getPurchaseNo());
            purchaseListDto.setProductNo(cartDto.getProductNo());
            purchaseListDto.setQuantity(cartDto.getQuantity());

            ProductDto productDto = shopSqlMapper.findByProductNo(cartDto.getProductNo());
            int quantity = cartDto.getQuantity();
            int price = productDto.getPrice();
            int totalPrice = price * quantity;

            purchaseListDto.setPaymentPrice(totalPrice);

            purchaseListSqlMapper.createPurchaseList(purchaseListDto);
            shopSqlMapper.deleteCart(cartNo);

        }
        return purchaseDto;
        
        

    }
    /**
     * 구매번호로 리스트 가져오기
     * @param purchaseListNo
     * @return
     */
    public List<Map<String,Object>> getPurchaseList(int purchaseListNo){
        List<Map<String,Object>> list = new ArrayList<>();
        List<PurchaseListDto> purchaseListDtos = purchaseListSqlMapper.purchaseListFindByPurchaseNo(purchaseListNo);

        for(PurchaseListDto purchaseListDto : purchaseListDtos){
            Map<String,Object> map = new HashMap<>();
            ProductDto productDto = shopSqlMapper.findByProductNo(purchaseListDto.getProductNo());
            SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());

            String price = decimelFormatter(purchaseListDto.getPaymentPrice());
            map.put("productDto", productDto);
            map.put("purchaseListDto",purchaseListDto);
            map.put("sellerDto",sellerDto);
            map.put("price", price);

            list.add(map);
        }
        return list;
    }

    /**
     * 고객 아이디로 구매 리스트 가져오기
     * @param consumerNo
     * @return
     */
    public List<Map<String,Object>> getPurchaseListConsumerNo(int consumerNo){
        List<Map<String,Object>> list = new ArrayList<>();
        List<ShoppingPurchaseDto> purchaseDtoList = shopSqlMapper.purchaseFindByConsumerNo(consumerNo);

        for(ShoppingPurchaseDto purchaseDto : purchaseDtoList ){
            List<PurchaseListDto> purchaseListDtoList = purchaseListSqlMapper.purchaseListFindByPurchaseNo(purchaseDto.getPurchaseNo());

            List<Map<String, Object>> purchaseDetails = new ArrayList<>();
            for(PurchaseListDto purchaseListDto : purchaseListDtoList){
                Map<String,Object> purchaseDetail  = new HashMap<>();
                
                ProductDto productDto = shopSqlMapper.findByProductNo(purchaseListDto.getProductNo());
                SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());
                String price = decimelFormatter(purchaseListDto.getPaymentPrice());
                

                purchaseDetail.put("purchaseListDto", purchaseListDto);
                purchaseDetail.put("productDto", productDto);
                purchaseDetail.put("sellerDto", sellerDto);
                purchaseDetail.put("price", price);

                purchaseDetails.add(purchaseDetail);
            }

            Map<String,Object> map = new HashMap<>();

            map.put("purchaseDto", purchaseDto);
            map.put("purchaseDetails",purchaseDetails);

            list.add(map);
        }
        
        return list;
    }

    /**
     * 고객 상품 구매 정보
     * @param consmerNo
     * @param productNo
     * @return ShoppingPurchaseDto
     */
    public ShoppingPurchaseDto getPurchaseInfo(ShoppingPurchaseDto purchaseDto){
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

    /**
     * 장바구니 리스트
     * @param consumerNo
     * @return List
     */
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
     * 장바구니 추가
     * @param cartDto
     */
    public void registerCart(CartDto cartDto){
        shopSqlMapper.addCart(cartDto);
    }

    /**
     * 장바구니 삭제
     * @param cartNo
     */
    public void deleteCart(int cartNo){
        shopSqlMapper.deleteCart(cartNo);
    }
    
    /**
     * 장바구니 최신 제품 번호 가져오기
     * @param consumerNo
     * @return
     */
    public int[] getCartNoMax(int consumerNo){
        int[] arr = new int[1];
        arr[0] = shopSqlMapper.cartNoMax(consumerNo);
        return arr;
    }

    /**
     * 장바구니 옵션 수정
     * @param price
     * @return
     */
    public void updateCart(CartDto cartDto){
        shopSqlMapper.updateCart(cartDto);
    }

    /**
     * 장바구니 총 결제금액 가져오기
     * @param params
     * @return
     */
    public String getTotalPrice(int[] params){
        int sumPrice = 0;
        for(int cartNo : params){
            CartDto cartDto = shopSqlMapper.cartFindByCartNo(cartNo);
            ProductDto productDto = shopSqlMapper.findByProductNo(cartDto.getProductNo());
            sumPrice += productDto.getPrice() * cartDto.getQuantity();
        }
        String totalPrice = decimelFormatter(sumPrice);
        return totalPrice;
    }

    /**
     * 결제창 리스트 가져오기
     * @param params
     * @return List
     */
    public List<Map<String,Object>> getPaymentList(int[] params){
        List<Map<String,Object>> list = new ArrayList<>();
        
        int sumPrice = 0;
        for(int cartNo : params){

            Map<String,Object> map = new HashMap<>();
            CartDto cartDto = shopSqlMapper.cartFindByCartNo(cartNo);
            ProductDto productDto = shopSqlMapper.findByProductNo(cartDto.getProductNo());
            SellerDto sellerDto = sellerSqlMapper.findByNo(productDto.getSellerNo());
            
            String resultPrice = decimelFormatter(cartDto.getQuantity() * productDto.getPrice());
            sumPrice += productDto.getPrice() * cartDto.getQuantity();
            System.out.println("sumPrice" + sumPrice);
            map.put("cartDto", cartDto);
            map.put("productDto", productDto);
            map.put("sellerDto", sellerDto);
            map.put("resultPrice", resultPrice);
            // map.put("totalPrice", totalPrice);
            
            list.add(map);
        }
        
        return list;
    }

    /**
     * 총 결제금액 가져오기
     * @param params
     * @return
     */
    public String getPurchaseTotalPrice(int purchaseNo){
        List<PurchaseListDto> list = purchaseListSqlMapper.purchaseListFindByPurchaseNo(purchaseNo);
        int sumPrice =0;
        for(PurchaseListDto dto : list){
            ProductDto productDto = shopSqlMapper.findByProductNo(dto.getProductNo());
            sumPrice += dto.getQuantity() * productDto.getPrice();
        }
        String totalPrice = decimelFormatter(sumPrice);
        return totalPrice;
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

}
