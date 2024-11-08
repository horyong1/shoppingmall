package com.hr.shoppingmall.seller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("seller")
public class SellerController {

    @RequestMapping("loginPage")
    public String loginPage(){
        return "seller/sellerLoginPage";
    }
}
