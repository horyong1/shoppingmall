package com.hr.shoppingmall.seller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.service.SellerService;

@Controller
@RequestMapping("seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;

    // 로그인 페이지
    @RequestMapping("loginPage")
    public String loginPage(){
        return "seller/sellerLoginPage";
    }

    //로그인 프로세스
    @RequestMapping("loginProcess")
    public String loginProcess(SellerDto sellerDto){
        SellerDto dto = sellerService.findByIdAndPassword(sellerDto);
        if(dto == null){
            return"seller/loginFailPage";
        }
        return"shop/mainPage";
    }

    // 회원가입 페이지
    @RequestMapping("registerSellerPage")
    public String registerSellerPage(){
        return"seller/registerSellerPage";
    }

    // 회원가입 프로세스
    @RequestMapping("registerSellerProcess")
    public String registerSellerProcess(SellerDto sellerDto){
        sellerService.registerSeller(sellerDto);
        return"seller/registerSellerSuccess";
    }
}
