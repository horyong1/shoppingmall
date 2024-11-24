-- 데이터 베이스 생성 및 설정
-- CREATE DATABASE shop;
-- CREATE USER 'tttt'@'%'IDENTIFIED BY '1234';
-- GRANT ALL PRIVILEGES ON shop.* TO 'tttt'@'%';
-- FLUSH PRIVILEGES;


#고객 
DROP TABLE sp_consumer;
CREATE TABLE  sp_consumer  (
	 consumer_no INT PRIMARY KEY AUTO_INCREMENT ,
	 consumer_id VARCHAR(30),
	 password VARCHAR(160),
	 nickname VARCHAR(30),
	 gender VARCHAR(1),
	 adress VARCHAR(400),
	 created_at DATETIME DEFAULT NOW()	
);

#고객배송지
DROP TABLE sp_consumer_adress;
CREATE TABLE  sp_consumer_adress  (
	 adress_no 	INT PRIMARY KEY AUTO_INCREMENT,
	 consumer_no  INT,
	 consumer_adress  VARCHAR(400),
	 created_at  DATETIME DEFAULT NOW()		
);

#상품정보
DROP TABLE sp_product;
CREATE TABLE  sp_product  (
	 product_no  INT PRIMARY KEY AUTO_INCREMENT,
	 category_no  INT,
	 category_medium_no INT,
	 seller_no  INT,
	 product_name  VARCHAR(80),
	 product_description  VARCHAR(1000),
	 price 	INT,
	 main_image_url  VARCHAR(500),
	 total_quantity  INT DEFAULT 0,
	 created_at  DATETIME DEFAULT NOW()		 
);

#상품 카테고리(대분류)
DROP TABLE sp_product_category;
CREATE TABLE  sp_product_category  (
	 category_no 	INT PRIMARY KEY AUTO_INCREMENT,
	 category_name 	VARCHAR(50),
	 created_at 	DATETIME DEFAULT NOW()		 
);
-- INSERT INTO sp_product_category(category_name)
-- VALUES('전체');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('패션의류/잡화');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('뷰티');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('출산/유아동');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('식품');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('주방용품');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('생활용품');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('홈인테리어');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('가전디지털');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('스포츠/레저');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('자동차용품');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('도서/음반/DVD');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('완구/취미');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('반려동물용품');
-- INSERT INTO sp_product_category(category_name)
-- VALUES('헬스/건강식품');

#상품 카테고리(중분류)
DROP TABLE sp_product_category_medium;
CREATE TABLE sp_product_category_medium (
	category_medium_no INT PRIMARY KEY AUTO_INCREMENT,
	category_no	INT,
	category_medium_name VARCHAR(50),
	created_at DATETIME	DEFAULT NOW()
);

#상품구매
DROP TABLE sp_product_purchase;
CREATE TABLE  sp_product_purchase  (
	 purchase_no  INT PRIMARY KEY AUTO_INCREMENT,
	 consumer_no  INT,
	 shopping_adress  VARCHAR(400),
	 purchase_date  DATETIME DEFAULT NOW(),
	 state  VARCHAR(10)	 
);

#상품 구매 리스트
DROP TABLE sp_product_purchase_list;
CREATE TABLE  sp_product_purchase_list  (
	 purchase_list_no INT PRIMARY KEY AUTO_INCREMENT,
	 purchase_no INT,
	 product_no INT,
	 quantity INT,
	 payment_price INT,
	 created_at DATETIME DEFAULT now()
);

#판매자
DROP TABLE sp_seller;
CREATE TABLE  sp_seller  (
	 seller_no 	INT PRIMARY KEY AUTO_INCREMENT,
	 seller_id 	VARCHAR(30),
	 password 	VARCHAR(160),
	 shop_name 	VARCHAR(40),
	 created_at  DATETIME DEFAULT NOW()		 
);

#판매자찜
DROP TABLE sp_seller_wishlist;
CREATE TABLE  sp_seller_wishlist  (
	 seller_wishlist_no INT PRIMARY KEY AUTO_INCREMENT,
	 consumer_no  INT,
	 seller_no  INT,
	 created_at  DATETIME DEFAULT NOW()		 
);

#상품리뷰
DROP TABLE sp_product_review;
CREATE TABLE  sp_product_review  (
	 review_no INT PRIMARY KEY AUTO_INCREMENT,
	 product_no INT,
	 consumer_no INT,
	 purchase_no INT,
	 review_content  VARCHAR(4000),
	 rating  INT,
	 created_at  DATETIME DEFAULT NOW(),
	 seller_reply  VARCHAR(4000),
	 reply_date  DATETIME 		 
);

#상품상세이미지
DROP TABLE sp_product_detail_image;
CREATE TABLE  sp_product_detail_image  (
	 image_no  INT PRIMARY KEY AUTO_INCREMENT,
	 product_no  INT,
	 image_link  VARCHAR(500),
	 created_at  DATETIME DEFAULT NOW()		 
);

#상품찜
DROP TABLE sp_product_wishlist;
CREATE TABLE  sp_product_wishlist  (
	 product_wishlist_no  INT PRIMARY KEY AUTO_INCREMENT,
	 consumer_no  INT,
	 product_no  INT,
	 created_at  DATETIME DEFAULT NOW()		 
);

#장바구니
DROP TABLE sp_cart;
CREATE TABLE sp_cart (
	cart_no INT PRIMARY KEY AUTO_INCREMENT,
	consumer_no	INT,
	product_no INT,
	quantity INT,
	created_at DATETIME DEFAULT NOW()
);

#옵션
DROP TABLE sp_product_option; 
CREATE TABLE sp_product_option (
    option_no INT PRIMARY KEY AUTO_INCREMENT,    		-- 옵션 고유 번호
    seller_no INT,						-- 판매자 번호 (판매자 고유 옵션 관리)
    option_name VARCHAR(200),           		 	-- 옵션 이름 (예: 색상, 사이즈)
    created_at DATETIME DEFAULT NOW(),            
    updated_at DATETIME DEFAULT NOW() ON UPDATE NOW()  
);

#옵션 상세
DROP TABLE sp_product_option_detail; 
CREATE TABLE sp_product_option_detail (
    option_detail_no INT PRIMARY KEY AUTO_INCREMENT,   	-- 옵션 상세 고유 번호
    option_no INT,                                     	-- 옵션 번호 (외래 키)
    option_detail_name VARCHAR(200),          		-- 세부 옵션 이름 (예: 빨간색, M 사이즈)
    price_adjustment INT DEFAULT 0,                    	-- 옵션에 따른 가격 조정 (기본 가격에 더해질 금액)
    created_at DATETIME DEFAULT NOW(),                 
    updated_at DATETIME DEFAULT NOW() ON UPDATE NOW()
);

#중간 옵션 매핑
DROP TABLE sp_product_option_mapping; 
CREATE TABLE sp_product_option_mapping (
    mapping_id INT PRIMARY KEY AUTO_INCREMENT,  	-- 매핑 고유 번호
    product_no INT,                            		-- 상품 번호 (외래 키)
    option_no INT,                             		-- 옵션 번호 (외래 키)
    option_detail_no INT,                      		-- 옵션 상세 번호 (외래 키)
    total_quantity INT DEFAULT 0,                   	-- 재고 수량
    UNIQUE (product_no, option_no, option_detail_no) 	-- 동일한 상품-옵션-상세 조합 중복 방지
);
