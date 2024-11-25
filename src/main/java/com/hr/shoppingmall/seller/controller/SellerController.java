package com.hr.shoppingmall.seller.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hr.shoppingmall.consumer.dto.ProductReviewDto;
import com.hr.shoppingmall.seller.dto.SellerDto;
import com.hr.shoppingmall.seller.service.OptionService;
import com.hr.shoppingmall.seller.service.SellerService;
import com.hr.shoppingmall.shop.dto.OptionDetailDto;
import com.hr.shoppingmall.shop.dto.OptionDto;
import com.hr.shoppingmall.shop.dto.ProductDto;
import com.hr.shoppingmall.shop.service.ReviewService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("seller")
public class SellerController {

    @Autowired
    private SellerService sellerService;
    @Autowired
    private OptionService optionService;
    @Autowired
    private ReviewService reviewService;

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
    public String registerProductPage(HttpSession session, Model model){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        model.addAttribute("categoryList", sellerService.getCategoryList());
        model.addAttribute("categoryMediumList", sellerService.getCategoryMediumList());
        return "seller/product/registerProductPage";
    }

    // 상품 등록 프로세스
    @RequestMapping("registerProductProcess")
    public String registerProductProcess(ProductDto params, HttpSession session,
        @RequestParam("mainImgUrl") MultipartFile[] mainImgUrl,@RequestParam("imageLink") MultipartFile[] imageLink){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        
        SellerDto sellerInfo = getSellerInfo(session);
       
        List<String> mainUrlList = uploadFiles(mainImgUrl);
        List<String> detailUrlList = uploadFiles(imageLink);
         
        
        
        params.setSellerNo(sellerInfo.getSellerNo());
        sellerService.registerProduct(params,mainUrlList,detailUrlList);

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
        model.addAttribute("categoryList", sellerService.getCategoryList());
        model.addAttribute("categoryMediumList", sellerService.getCategoryMediumList());
        return "seller/product//updateProductPage";
    }

    //  상품 수정 프로세스
    @RequestMapping("updateProductProcess")
    public String updateProductProcess(HttpSession session,ProductDto productDto,
    @RequestParam("mainImgUrl") MultipartFile[] mainImgUrl,@RequestParam("imageLink") MultipartFile[] imageLink){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        List<String> mainUrlList = uploadFiles(mainImgUrl);
        List<String> detailUrlList = uploadFiles(imageLink);
        
        sellerService.updateProduct(productDto, mainUrlList, detailUrlList);

        return "seller/product/updateProductSuccess";
    }

    // 특정 상품 옵션 관리 페이지
    @RequestMapping("optionEditPage")
    public String optionEditPage(Model model, HttpSession session, @RequestParam("productNo")int productNo){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        model.addAttribute("optionList", optionService.getOptionsByProductNo(productNo));
        model.addAttribute("productNo", productNo);
        return "seller/product/optionEditPage";
    }

    // 옵션 등록
    @RequestMapping("registerOptionList")
    public String registerOptionList(Model model, HttpSession session,
        OptionDto optionDto, 
        @RequestParam(value = "optionDetailName") String optionDetailName){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        int productNo = optionDto.getProductNo();
        optionService.registerOption(optionDto, optionDetailName, productNo);

        return "redirect:./optionEditPage?productNo="+productNo;
    }

    // 옵션 상세 등록
    @RequestMapping("registerOptionDetail")
    public String registerOptionDetail(
        @RequestBody Map<String, Object> requestData, 
        HttpSession session) {
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        int productNo = (int)requestData.get("productNo");
        List<?> rawList = (List<?>) requestData.get("productDetailNo");
        int stock = (int)requestData.get("stock");
        List<Integer> productDetailList = rawList.stream()
                                                 .map(item -> Integer.valueOf((item.toString())))
                                                 .collect(Collectors.toList());
        System.out.println("productNo >>>> " + productNo);
        System.out.println("productDetailList >>>> " + productDetailList);
        System.out.println("stock >>>> " + stock);

        return "성공";
    }

    // 제품 리뷰 관리 페이지
    @RequestMapping("productReviewList")
    public String productReviewList(HttpSession session, Model model, @RequestParam("productNo") int productNo){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }

        List<Map<String, Object>> list = reviewService.getProductReviewList(productNo);
        System.out.println(list);
        model.addAttribute("reviewList", list);
        model.addAttribute("reviewCount", reviewService.reviewConut(productNo));
        return "seller/product/productReviewList";
    }

    // 리뷰 답글 달기
    @RequestMapping("registerReply")
    public String registerReply(HttpSession session, Model model, ProductReviewDto reviewDto){
        if (!isSellerLoggedIn(session)) {
            return "redirect:./loginPage";
        }
        reviewService.registerReply(reviewDto);

        return"redirect:/seller/productReviewList?productNo=" + reviewDto.getProductNo();
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

    /**
     * 이미지 저장 로직
     * @param uploadFiles
     * @return
     */
    private List<String> uploadFiles(MultipartFile[] uploadFiles) {
        List<String> fileList = new ArrayList<>();
        String rootPath = "C:/uploadFiles/";

        // 날짜별 폴더(디렉토리) 생성
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/");
        String todayPath = sdf.format(new Date());
        File todayFolderForCreate = new File(rootPath + todayPath);

        // 해당 폴더가 존재하지 않으면 생성
        if (!todayFolderForCreate.exists()) {
            todayFolderForCreate.mkdirs();
        }

        // 각 파일 처리
        for (MultipartFile uploadFile : uploadFiles) {
            if (uploadFile.isEmpty()) {
                continue;
            }

            String originalFilename = uploadFile.getOriginalFilename();
            String uuid = UUID.randomUUID().toString();
            long currentTime = System.currentTimeMillis();
            String fileName = uuid + "_" + currentTime;

            // 확장자명 추출
            String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
            fileName += ext;

            try {
                uploadFile.transferTo(new File(rootPath + todayPath + fileName));
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            fileList.add(todayPath + fileName);
        }

        return fileList;
    }
}
