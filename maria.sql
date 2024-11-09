-- 데이터 베이스 생성 및 설정
-- CREATE DATABASE shop;
-- CREATE USER 'tttt'@'%'IDENTIFIED BY '1234';
-- GRANT ALL PRIVILEGES ON shop.* TO 'tttt'@'%';
-- FLUSH PRIVILEGES;

-- 구매자
#고객 
DROP TABLE sp_consumer;
CREATE TABLE  sp_consumer  (
	 consumer_no 	INT	,
	 consumer_id 	VARCHAR(30)	,
	 password 	VARCHAR(160)	,
	 nickname 	VARCHAR(30)	,
	 gender 	VARCHAR(1)	,
	 created_at  DATETIME DEFAULT NOW()	
);

#고객배송지
DROP TABLE sp_consumer_adress;
CREATE TABLE  sp_consumer_adress  (
	 adress_no 	INT,
	 consumer_no  INT,
	 adress  VARCHAR(400),
	 created_at  DATETIME DEFAULT NOW()		
);

#상품정보
DROP TABLE sp_product;
CREATE TABLE  sp_product  (
	 product_no  INT,
	 category_no  INT,
	 seller_no  INT,
	 product_name  VARCHAR(80),
	 product_description  VARCHAR(1000),
	 price 	INT,
	 main_image_url  VARCHAR(500),
	 total_quantity  INT,
	 created_at  DATETIME DEFAULT NOW()		 
);

#상품카테고리
DROP TABLE sp_product_category;
CREATE TABLE  sp_product_category  (
	 category_no 	INT	   ,
	 category_name 	VARCHAR(50)	 ,
	 crated_at 	DATETIME DEFAULT NOW()		 
);

#상품구매
DROP TABLE sp_product_purchase;
CREATE TABLE  sp_product_purchase  (
	 purchase_no  INT,
	 consumer_no  INT,
	 product_no  INT,
	 quantity  INT	 ,
	 shipping_address  VARCHAR(400),
	 purchase_date  DATETIME DEFAULT NOW(),
	 Field2  VARCHAR(10)	 
);

#판매자
DROP TABLE sp_seller;
CREATE TABLE  sp_seller  (
	 seller_no 	INT,
	 seller_id 	VARCHAR(30),
	 password 	VARCHAR(160),
	 shop_name 	VARCHAR(40),
	 created_at  DATETIME DEFAULT NOW()		 
);

#판매자찜
DROP TABLE sp_seller_wishlist;
CREATE TABLE  sp_seller_wishlist  (
	 seller_wishlist_no  INT,
	 consumer_no  INT,
	 seller_no  INT,
	 created_at  DATETIME DEFAULT NOW()		 
);

#상품리뷰
DROP TABLE sp_product_review;
CREATE TABLE  sp_product_review  (
	 review_no 	INT,
	 product_no  INT,
	 consumer_no  INT,
	 review_content  VARCHAR(4000),
	 rating  INT,
	 created_at  DATETIME,
	 seller_reply  VARCHAR(4000),
	 reply_date  DATETIME DEFAULT NOW()		 
);

#상품상세이미지
DROP TABLE sp_product_detail_image;
CREATE TABLE  sp_product_detail_image  (
	 image_no  INT,
	 product_no  INT,
	 image_link  VARCHAR(50),
	 created_at  DATETIME DEFAULT NOW()		 
);

#상품찜
DROP TABLE sp_product_wishlist;
CREATE TABLE  sp_product_wishlist  (
	 product_wishlist_no  INT,
	 consumer_no  INT,
	 product_no  INT,
	 created_at  DATETIME DEFAULT NOW()		 
);