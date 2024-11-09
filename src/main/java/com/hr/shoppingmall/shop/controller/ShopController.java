package com.hr.shoppingmall.shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("shop")
public class ShopController {

    @RequestMapping("mainPage")
    public String mainPage(){
        return "shop/mainPage";
    }
}
