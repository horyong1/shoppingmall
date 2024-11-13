package com.hr.shoppingmall.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.seller.service.SellerService;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("shop")
public class ShopController {

    @Autowired
    private ShopService shopService;
    @Autowired 
    private SellerService sellerService;

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
        public String productDetailPage(@RequestParam("productNo")int productNo, Model model){
            
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

}
