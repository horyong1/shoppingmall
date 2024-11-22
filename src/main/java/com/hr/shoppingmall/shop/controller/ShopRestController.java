package com.hr.shoppingmall.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hr.shoppingmall.shop.service.ShopService;

@RestController
@RequestMapping("api/shop")
public class ShopRestController {

    @Autowired
    private ShopService shopService;


    
    
}
