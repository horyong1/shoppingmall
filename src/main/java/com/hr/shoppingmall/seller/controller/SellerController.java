package com.hr.shoppingmall.seller.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
    public String registerProductPage(HttpSession session){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        return "seller/product/registerProductPage";
    }

    // 상품 등록 프로세스
    @RequestMapping("registerProductProcess")
    public String registerProductProcess(ProductDto params, HttpSession session, MultipartFile mainImgeUrl){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        
        SellerDto sellerInfo = getSellerInfo(session);
       
        if(mainImgeUrl != null){
            String rootPath = "C:/uploadfiles/";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
            String todayPath = sdf.format(new Date());
            File todayFolderForCreate = new File(rootPath+todayPath);

            if(!todayFolderForCreate.exists()){
                todayFolderForCreate.mkdirs();
            }

            String originalFilename = mainImgeUrl.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            long currentTime = System.currentTimeMillis();

            String fileName = uuid + "_" + currentTime;

            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName += ext;

            try {
                mainImgeUrl.transferTo(new File(rootPath+todayPath+fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            String url = todayPath + fileName;
            System.out.println("url >>>  "+ url);
            params.setMainImageUrl(url);
        }



        

        params.setSellerNo(sellerInfo.getSellerNo());
        sellerService.registerProduct(params);

        return "seller/product/registerProductSuccess";
    }

    // 등록 상품 관리 페이지
    @RequestMapping("updateDeleteProductPage")
    public String updateDeleteProductPage(HttpSession session, Model model){
        
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        SellerDto sellerDto = getSellerInfo(session);
        model.addAttribute("productList", sellerService.getProuctList(sellerDto.getSellerNo()));

        return"seller/product/updateDeleteProductPage";
    }

    // 상품 삭제 프로세스
    @RequestMapping("deleteProductProcess")
    public String deleteProductProcess(@RequestParam("productNo") int productNo, HttpSession session){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        sellerService.deleteProduct(productNo);
        return "seller/product/deleteProductSuccess";
    }

    // 상품 수정 페이지
    @RequestMapping("updateProductPage")
    public String updateProductPage(@RequestParam("productNo")int productNo,HttpSession session, Model model){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        
        model.addAttribute("productDto", sellerService.getProductInfo(productNo));
        return "seller/product//updateProductPage";
    }

    //  상품 수정 프로세스
    @RequestMapping("updateProductProcess")
    public String updateProductProcess(HttpSession session,ProductDto productDto){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        sellerService.updateProduct(productDto);

        return "seller/product/updateProductSuccess";
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
