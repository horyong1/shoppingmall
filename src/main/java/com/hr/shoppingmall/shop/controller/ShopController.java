package com.hr.shoppingmall.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.service.ConsumerService;
import com.hr.shoppingmall.seller.service.SellerService;
import com.hr.shoppingmall.shop.dto.CartDto;
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
    @Autowired
    private ConsumerService consumerService;

    // 메인 페이지
    @RequestMapping("mainPage")
    public String mainPage(Model model){
        model.addAttribute("mainList", shopService.getProductShow());

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
    
    // 상품 디테일 페이지에서 구매 버튼 클릭 후 결제화면으로 넘어가기
    @RequestMapping("registerPurchaseProcess")
    public String registerPurchaseProcess(@RequestParam(value="count")String count,
        CartDto params, HttpSession session, Model model){
            if(!isConsumerLoggedIn(session)){
                return "redirect:/consumer/loginPage";
            }
            ConsumerDto consumerInfo = getConsumerInfo(session);
             
            int consumerNo = consumerInfo.getConsumerNo();
            params.setConsumerNo(consumerNo);
            params.setQuantity(Integer.parseInt(count));
            shopService.registerCart(params);

            int[] cartMax = shopService.getCartNoMax(consumerNo);

            model.addAttribute("consumerDto", consumerService.getConsumer(consumerInfo.getConsumerNo()));
            model.addAttribute("paymentList", shopService.getPaymentList(cartMax));
            model.addAttribute("totalPrice", shopService.getTotalPrice(cartMax));                       

            return "shop/paymentPage";
    }

    // 결제진행 프로세스
    @RequestMapping("purchaseProcess")
    public String purchaseProcess(HttpSession session, Model model, 
        @RequestParam("cartNos")int[] cartNos){
            if(!isConsumerLoggedIn(session)){
                return "redirect:/consumer/loginPage";
            }
            ConsumerDto consumerInfo = getConsumerInfo(session);

            ShoppingPurchaseDto purchaseDto = new ShoppingPurchaseDto();
            purchaseDto.setConsumerNo(consumerInfo.getConsumerNo());
           
            purchaseDto = shopService.registerPurchase(purchaseDto,cartNos);
            System.out.println(purchaseDto);
            if(purchaseDto.getPurchaseNo() == 0){
                return "redirect:/shop/mainPage";
            } 
            model.addAttribute("paymentList",  shopService.getPurchaseList(purchaseDto.getPurchaseNo()));
            model.addAttribute("consumerDto", consumerService.getConsumer(purchaseDto.getConsumerNo()));
            model.addAttribute("totalPrice", shopService.getPurchaseTotalPrice(purchaseDto.getPurchaseNo()));
            model.addAttribute("purchaseDto", purchaseDto);
        return"shop/purchaseSuccess";
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
    @RequestParam(value = "count", defaultValue = "0")String count){
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
        shopService.updateCart(params);
        return "redirect:/shop/cartPage";
    }

    // 구매 버튼 클릭 후 결제 페이지 넘어가기
    @RequestMapping("paymentPage")
    public String paymentPage(HttpSession session, Model model, @RequestParam("cartNos") int[] params){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getConsumerInfo(session);
        model.addAttribute("consumerDto", consumerService.getConsumer(consumerInfo.getConsumerNo()));
        model.addAttribute("paymentList", shopService.getPaymentList(params));
        model.addAttribute("totalPrice", shopService.getTotalPrice(params));
        return "shop/paymentPage";
    }

    // 카테고리 페이지
    @RequestMapping("categoryPage")
    public String categoryPage(Model model){
        model.addAttribute("categoryList",shopService.getCategoryList());
        return "shop/categoryPage";
    }

    // 카테고리별 제품 리스트 페이지
    @RequestMapping("categoryProductListPage")
    public String categoryProductListPage(Model model,@RequestParam(value = "categoryNo")int cateoryNo){
        model.addAttribute("categoryDto", shopService.getCategoryName(cateoryNo));
        model.addAttribute("categoryProductList", shopService.getProductCategoryList(cateoryNo));
        return "shop/categoryProductListPage";
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
