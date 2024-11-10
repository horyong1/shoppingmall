package com.hr.shoppingmall.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.service.ConsumerService;

@Controller
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;

    // 소비자 로그인 페이지
    @RequestMapping("loginPage")
    public String loginPage(){
        return "consumer/consumerLoginPage";
    }

    // 소비자 회원가입 페이지
    @RequestMapping("registerConsumerPage")
    public String registerConsumerPage(){
        return "consumer/registerConsumerPage";
    }

    // 소비자 로그인 프로세스
    @RequestMapping("loginProcess")
    public String loginPageProcess(ConsumerDto consumerDto){
        ConsumerDto dto = consumerService.loginCheck(consumerDto);
        
        if(dto == null){
            return "consumer/loginFailPage";
        }

        return "shop/mainPage";
    }
    
    // 회원가입 프로세스
    @RequestMapping("registerConsumerProcess")
    public String registerConsumerProcess(@RequestParam("adress")String adress, ConsumerDto consumerDto){
        consumerService.registerConsumer(consumerDto, adress);
        return "consumer/registerSuccessPage";
    }   
}
