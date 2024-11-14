package com.hr.shoppingmall.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.consumer.dto.ConsumerDto;
import com.hr.shoppingmall.consumer.dto.ProductReviewDto;
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
        System.out.println(">>>>>>>>" + reviewService.getReviewList(productNo));
        model.addAttribute("reviewList", reviewService.getReviewList(productNo));

        return "shop/productReviewList";
    }
    // 리뷰 작성 페이지
    @RequestMapping("reviewEdit")
    public String reviewEdit(HttpSession session, @RequestParam("productNo")int productNo, Model model){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }

        model.addAttribute("map",shopService.getProductDetail(productNo));
        return "shop/reviewEdit";
    }

    // 리뷰 작성 프로세스
    @RequestMapping("registerReviewProcess")
    public String registerReviewProcess(HttpSession session, ProductReviewDto reviewDto){
        if(!isSellerLoggedIn(session)){
            return "redirect:/consumer/loginPage";
        }
        ConsumerDto consumerInfo = getSellerInfo(session);
        reviewDto.setConsumerNo(consumerInfo.getConsumerNo());
        System.out.println(">>>>>>>>> " + reviewDto);
        reviewService.registerReview(reviewDto);

        return "shop/reviewSuccess";
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
