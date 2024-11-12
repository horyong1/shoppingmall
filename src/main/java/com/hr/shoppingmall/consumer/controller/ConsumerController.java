package com.hr.shoppingmall.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerAdressDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.service.ConsumerService;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private ShopService shopService;

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
    public String loginPageProcess(ConsumerDto consumerDto, HttpSession session){
        ConsumerDto consumerInfo = consumerService.loginCheck(consumerDto);
        
        if(consumerInfo == null){
            return "consumer/loginFailPage";
        }
        session.setAttribute("consumerInfo", consumerInfo);
        
        return "redirect:/shop/mainPage";
    }

    @RequestMapping("logOut")
    public String logOut(HttpSession session){
        session.invalidate();
        
        return "consumer/consumerLoginPage";
    }
    
    // 회원가입 프로세스
    @RequestMapping("registerConsumerProcess")
    public String registerConsumerProcess(@RequestParam("adress")String adress, ConsumerDto consumerDto){
        consumerService.registerConsumer(consumerDto, adress);

        return "consumer/registerSuccessPage";
    } 

    // MyPage 
    @RequestMapping("myPage")
    public String myPage(){
        return "consumer/myPage";
    }

    // 배송지 등록/수정 page
    @RequestMapping("adressEdit")
    public String adressEditPage(HttpSession session, Model model){
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        
        if(consumerInfo == null){
            return "redirect:./loginPage";
        }

        model.addAttribute("adressList", consumerService.getAdressList(consumerInfo.getConsumerNo()));

        return "consumer/adressEdit";
    }
    
    // 배송지 등록 프로세스
    @RequestMapping("registerAdress")
    public String registerAdress(ConsumerAdressDto adressDto, HttpSession session){
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");

        adressDto.setConsumerNo(consumerInfo.getConsumerNo());
        consumerService.registerAdress(adressDto);

        return"consumer/adressUpdateSuccess";
    }

    // 배송지 삭제 
    @RequestMapping("deleteAdress")
    public String deleteAdress(ConsumerAdressDto adressDto, HttpSession session){
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");

        adressDto.setConsumerNo(consumerInfo.getConsumerNo());
        consumerService.deleteAdress(adressDto);

        return"redirect:./adressEdit";
    }

    // 주문 내역 리스트
    @RequestMapping("purchaseList")
    public String purchaseList(HttpSession session, Model model){
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");

        if(consumerInfo == null){
            return "redirect:./loginPage";
        }

        int sonsumerNo = consumerInfo.getConsumerNo();

        model.addAttribute("purchaseList", shopService.getPurchaseList(sonsumerNo));

        return "shop/purchaseList";
    }
}
