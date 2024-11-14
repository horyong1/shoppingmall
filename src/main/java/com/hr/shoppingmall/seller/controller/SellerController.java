package com.hr.shoppingmall.seller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.service.SellerService;
import com.hr.shoppingmall.shop.dto.ProductDto;

import jakarta.servlet.http.HttpSession;

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
    public String loginProcess(SellerDto sellerDto, HttpSession session){
        SellerDto sellerInfo = sellerService.findByIdAndPassword(sellerDto);
        if(sellerInfo == null){
            return"seller/loginFailPage";
        }
        session.setAttribute("sellerInfo", sellerInfo);
        return"redirect:./mainPage";
    }

    // 회원가입 페이지
    @RequestMapping("registerSellerPage")
    public String registerSellerPage(){
        return"seller/registerSellerPage";
    }

    // 회원가입 프로세스
    @RequestMapping("registerSellerProcess")
    public String registerSellerProcess(SellerDto sellerDto, HttpSession session){
        sellerService.registerSeller(sellerDto);
        return"seller/registerSellerSuccess";
    }

    // 판매자 페이지
    @RequestMapping("mainPage")
    public String mainPage(HttpSession session){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        return "seller/mainPage";
    }

    // 판매자 상품 등록 페이지
    @RequestMapping("registerProductPage")
    public String registerProductPage(){
        return "seller/registerProductPage";
    }

    // 상품 등록 프로세스
    @RequestMapping("registerProductProcess")
    public String registerProductProcess(ProductDto params, HttpSession session){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        
        SellerDto sellerInfo = getSellerInfo(session);

        params.setSellerNo(sellerInfo.getSellerNo());
        params.setMainImageUrl("/public/img/패딩.png");
        System.out.println("정보 >>>>> "+params);
        sellerService.registerProduct(params);

        return "seller/registerProductSuccess";
    }

    // 등록 상품 수정/삭제 페이지
    @RequestMapping("updateDeleteProductPage")
    public String updateDeleteProductPage(HttpSession session, Model model){
        
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        SellerDto sellerDto = getSellerInfo(session);
        model.addAttribute("productList", sellerService.sellerProductList(sellerDto.getSellerNo()));

        return"seller/updateDeleteProductPage";
    }

    // 상품 삭제 프로세스
    @RequestMapping("deleteProductProcess")
    public String deleteProductProcess(@RequestParam("productNo") int productNo, HttpSession session){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        sellerService.deleteProduct(productNo);
        return "seller/deleteProductSuccess";
    }

    // 상품 수정 페이지
    @RequestMapping("updateProductPage")
    public String updateProductPage(@RequestParam("productNo")int productNo,HttpSession session, Model model){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        
        model.addAttribute("productDto", sellerService.getProductInfo(productNo));
        return "seller/updateProductPage";
    }

    //  상품 수정 프로세스
    @RequestMapping("updateProductProcess")
    public String updateProductProcess(HttpSession session,ProductDto productDto){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        sellerService.updateProduct(productDto);

        return "seller/updateProductSuccess";
    }


    // 세션 로그인 체크
    private boolean isSellerLoggedIn(HttpSession session) {
        SellerDto sellerInfo = (SellerDto) session.getAttribute("sellerInfo");
        return sellerInfo != null;
    }
    // 세션 sellerDto 값 세팅
    private SellerDto getSellerInfo(HttpSession session){
        return (SellerDto)session.getAttribute("sellerInfo");
    }

}
