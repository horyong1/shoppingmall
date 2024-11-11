package com.hr.shoppingmall.shop.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.service.SellerService;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.service.ShopService;

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
            
            ProductDto productDto = shopService.getProductDetail(productNo);
            SellerDto sellerDto = sellerService.findById(productDto.getSellerNo());
            
            System.out.println("정보내놔 " + productDto);
            System.out.println("정보내놔 " + sellerDto);
            
            model.addAttribute("productDto", productDto);
            model.addAttribute("sellerDto", sellerDto);

            return"shop/productDetailPage";
        }
    
}
