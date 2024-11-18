package com.hr.shoppingmall.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.seller.service.SellerService;
import com.hr.shoppingmall.shop.dto.CartDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.ProductWishlistDto;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;
import com.hr.shoppingmall.shop.service.ReviewService;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired 
    private SellerService sellerService;
    @Autowired
    private ReviewService reviewService;

    // 메인 페이지
    @RequestMapping("mainPage")
    public String mainPage(Model model){

        model.addAttribute("categoryMap", shopService.getCategoryList());
        model.addAttribute("clothingList", shopService.getProductShow(1));
        model.addAttribute("householdItemsList", shopService.getProductShow(2));
        model.addAttribute("electronicList", shopService.getProductShow(3));
        model.addAttribute("foodList", shopService.getProductShow(4));

        return "shop/mainPage";
    }
    
    // 상품 상세정보 페이지
    @RequestMapping("productDetailPage")
        public String productDetailPage(@RequestParam("productNo")int productNo,HttpSession session, Model model){
            ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
            ProductWishlistDto wishlistDto = new ProductWishlistDto();
            
            if(consumerInfo != null){
                wishlistDto.setConsumerNo(consumerInfo.getConsumerNo());
                wishlistDto.setProductNo(productNo);
                wishlistDto = shopService.getWishlistPruduct(wishlistDto);
                model.addAttribute("wishlistDto", wishlistDto);
            }
            Map<String,Object> map =  shopService.getProductDetail(productNo);
            
            model.addAttribute("productMap", map);
            return"shop/productDetailPage";
        }
    
    // 상품 구매 프로세스
    @RequestMapping("registerPurchaseProcess")
    public String registerPurchaseProcess(@RequestParam(value="count")String count,
        ProductDto params, HttpSession session, Model model){
            ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");

            if(consumerInfo == null){
                return "redirect:/consumer/loginPage";
            }
             
            int consumerNo = consumerInfo.getConsumerNo();
            int productNo = params.getProductNo();
            
            ShoppingPurchaseDto purchaseDto = new ShoppingPurchaseDto();
            purchaseDto.setConsumerNo(consumerNo);
            purchaseDto.setProductNo(productNo);
            purchaseDto.setQuantity(Integer.parseInt(count)); 
            
            List<Map<String,Object>> list = shopService.registerPurchase(purchaseDto);
            

            
            model.addAttribute("purchaseList", list);
            model.addAttribute("consumerInfo", consumerInfo);

            return "shop/purchaseSuccess";
    }

    // 검색화면
    @RequestMapping("searchProduct")
    public String searchProduct() {
        return "shop/searchProduct";
    }

    // 상품 찜 등록
    @RequestMapping("toggleWishlist")
    public String toggleWishlist(@RequestParam("productNo")int productNo, HttpSession session, Model model){
        
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getConsumerInfo(session);

        ProductWishlistDto wishlistDto = new ProductWishlistDto();
        wishlistDto.setConsumerNo(consumerInfo.getConsumerNo());
        wishlistDto.setProductNo(productNo);

        shopService.toggleWishlist(wishlistDto);

        wishlistDto = shopService.getWishlistPruduct(wishlistDto);
        
        Map<String,Object> map =  shopService.getProductDetail(productNo);
        
        model.addAttribute("productMap", map);
        model.addAttribute("wishlistDto", wishlistDto);
    
        
        return "shop/productDetailPage";
    }


    // 장바구니 페이지
    @RequestMapping("cartPage")
    public String cartPage(HttpSession session, Model model){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getConsumerInfo(session);
        model.addAttribute("cartList", shopService.getConsumerCartList(consumerInfo.getConsumerNo()));
        return "shop/cartPage";
    }

    
    // 장바구니 추가
    @RequestMapping("registerCart")
    public String registerCart(HttpSession session,CartDto params,
    @RequestParam(value="count")String count){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getConsumerInfo(session);
        params.setQuantity(Integer.parseInt(count));
        params.setConsumerNo(consumerInfo.getConsumerNo());

        shopService.registerCart(params);

        return "redirect:/shop/cartPage";
    }

    // 장바구니 삭제
    @RequestMapping("deleteCart")
    public String deleteCart(@RequestParam("cartNo") int cartNo,HttpSession session){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        shopService.deleteCart(cartNo);
        return "redirect:/shop/cartPage";
    }

    // 장바구니 옵션변경
    @RequestMapping("updateCart")
    public String updateCart(HttpSession session,@ModelAttribute CartDto params ){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        System.out.println("옵션변경" + params);
        shopService.updateCart(params);
        return "redirect:/shop/cartPage";
    }

    // 세션 로그인 체크
    private boolean isConsumerLoggedIn(HttpSession session) {
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        return consumerInfo != null;
    }
    // 세션 sellerDto 값 세팅
    private ConsumerDto getConsumerInfo(HttpSession session){
        return (ConsumerDto)session.getAttribute("consumerInfo");
    }
    
}
