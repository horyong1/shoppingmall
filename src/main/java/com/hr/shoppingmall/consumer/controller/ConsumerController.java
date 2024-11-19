package com.hr.shoppingmall.consumer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerAdressDto;
import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.service.ConsumerService;
import com.hr.shoppingmall.shop.dto.ShoppingPurchaseDto;
import com.hr.shoppingmall.shop.service.ReviewService;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("consumer")
public class ConsumerController {

    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReviewService reviewService;

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

    // 로그아웃 
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

    // 배송지 관리 page
    @RequestMapping("adressEdit")
    public String adressEditPage(HttpSession session, Model model){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getSellerInfo(session);
        
        if(consumerInfo == null){
            return "redirect:./loginPage";
        }
        model.addAttribute("defaulteAdress", consumerService.getDefaulteAdress(consumerInfo.getConsumerNo()));
        model.addAttribute("adressList", consumerService.getAdressList(consumerInfo.getConsumerNo()));

        return "consumer/adressEdit";
    }
    
    // 배송지 등록 프로세스
    @RequestMapping("registerAdress")
    public String registerAdress(ConsumerAdressDto adressDto, HttpSession session){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getSellerInfo(session);

        adressDto.setConsumerNo(consumerInfo.getConsumerNo());
        consumerService.registerAdress(adressDto);

        return "consumer/adressUpdateSuccess";
    }

    // 배송지 삭제 
    @RequestMapping("deleteAdress")
    public String deleteAdress(ConsumerAdressDto adressDto, HttpSession session){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getSellerInfo(session);

        adressDto.setConsumerNo(consumerInfo.getConsumerNo());
        consumerService.deleteAdress(adressDto);

        return "redirect:./adressEdit";
    }

    // 기본배송지 수정
    @RequestMapping("updateDefaulteAdress")
    public String updateDefaulteAdress(HttpSession session, @RequestParam("adressNo")int adressNo){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getSellerInfo(session);
        consumerService.updateDefaulteAdress(consumerInfo.getConsumerNo(),adressNo);
        return "redirect:/consumer/adressEdit";
    }

    // 주문 내역 리스트
    @RequestMapping("purchaseList")
    public String purchaseList(HttpSession session, Model model){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getSellerInfo(session);
        int consumerNo = consumerInfo.getConsumerNo();
        model.addAttribute("purchaseList", shopService.getPurchaseListConsumerNo(consumerNo));

        return "shop/purchaseList";
    }

    // 주문 내역 리스트 상세 정보
    @RequestMapping("purchaseDetail")
    public String purchaseDetail(
        @RequestParam("purchaseNo")int purchaseNo,
        HttpSession session,
        Model model){

        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }

        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        
        ShoppingPurchaseDto purchaseDto = new ShoppingPurchaseDto();
        purchaseDto.setConsumerNo(consumerInfo.getConsumerNo());
        purchaseDto.setPurchaseNo(purchaseNo);

        model.addAttribute("purchaseDto", shopService.getPurchaseInfo(purchaseDto));
        model.addAttribute("purcahseList",shopService.getPurchaseList(purchaseNo));
        model.addAttribute("totalPrice", shopService.getPurchaseTotalPrice(purchaseNo));    

        return "shop/purchaseDetail";
    }

    // 찜 목록
    @RequestMapping("wishlist")
    public String wishlist(HttpSession session, Model model){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");

        model.addAttribute("list",  shopService.getWishlist(consumerInfo.getConsumerNo()));
        
        return "consumer/wishlist";
    }


    // 세션 로그인 체크
    private boolean isSellerLoggedIn(HttpSession session) {
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        return consumerInfo != null;
    }
    // 세션 sellerDto 값 세팅
    private ConsumerDto getSellerInfo(HttpSession session){
        return (ConsumerDto)session.getAttribute("consumerInfo");
    }
    
   
}
