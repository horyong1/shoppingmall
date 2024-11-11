package com.hr.shoppingmall.shop.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.shop.dto.ProductCategoryDto;
import com.hr.shoppingmall.shop.service.ShopService;

@Controller
@RequestMapping("shop")
public class ShopController {

    @Autowired
    private ShopService shopService;

    // 메인 페이지
    @RequestMapping("mainPage")
    public String mainPage(Model model){
        Map<String,String> map = shopService.getCategoryList(); 
        System.out.println(map);
        
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
            model.addAttribute("productDetail", shopService.getProductDetail(productNo));
            return"shop/productDetailPage";
        }
    
}
