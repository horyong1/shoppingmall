package com.hr.shoppingmall.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.dto.ProductReviewDto;
import com.hr.shoppingmall.shop.dto.PurchaseListDto;
import com.hr.shoppingmall.shop.service.ReviewService;
import com.hr.shoppingmall.shop.service.ShopService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("review")
public class ReviewController {
    @Autowired
    private ShopService shopService;
    @Autowired
    private ReviewService reviewService;

    // 제품 리뷰 페이지
    @RequestMapping("productReviewList")
    public String productReviewList(@RequestParam("productNo")int productNo, Model model){
        model.addAttribute("reviewList", reviewService.getReviewList(productNo));

        return "shop/review/productReviewList";
    }

    // 리뷰 작성 페이지
    @RequestMapping("reviewEdit")
    public String reviewEdit(HttpSession session,
    @RequestParam("purchaseNo")int purchaseNo, @RequestParam("productNo")int productNo , Model model){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        PurchaseListDto purchaseListDto = new PurchaseListDto();
        purchaseListDto.setPurchaseNo(purchaseNo);
        purchaseListDto.setProductNo(productNo);
        


        model.addAttribute("map",reviewService.getReviewitem(purchaseListDto));
        return "shop/review/reviewEdit";
    }

    // 리뷰 작성 프로세스
    @RequestMapping("registerReviewProcess")
    public String registerReviewProcess(HttpSession session, ProductReviewDto reviewDto){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getConsumerInfo(session);
        reviewDto.setConsumerNo(consumerInfo.getConsumerNo());
        reviewService.registerReview(reviewDto);

        return "redirect:/consumer/purchaseList";
    }

    // 내 리뷰 목록 가져오기
    @RequestMapping("myReviewList")
    public String myReviewList(HttpSession session, Model model){
        if(!isConsumerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getConsumerInfo(session);

        model.addAttribute("reviewList", reviewService.getMyReviewList(consumerInfo.getConsumerNo()));
        return "shop/review/myReviewList";
    }




    // 세션 로그인 체크
    private boolean isConsumerLoggedIn(HttpSession session) {
        ConsumerDto consumerInfo = (ConsumerDto)session.getAttribute("consumerInfo");
        return consumerInfo != null;
    }
    // 세션 sellerDto 값 세팅
    private ConsumerDto getConsumerInfo(HttpSession session){
        return (ConsumerDto)session.getAttribute("consumerInfo");
    }
}
