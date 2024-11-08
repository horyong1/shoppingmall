-- 데이터 베이스 생성 및 설정
-- CREATE DATABASE shop;
-- CREATE USER 'tttt'@'%'IDENTIFIED BY '1234';
-- GRANT ALL PRIVILEGES ON shop.* TO 'tttt'@'%';
-- FLUSH PRIVILEGES;

-- 구매자
DROP TABLE sp_consumer;
CREATE TABLE sp_consumer(
	no INT PRIMARY KEY AUTO_INCREMENT,
	id VARCHAR(30),
	password VARCHAR(160),
	nickname VARCHAR(30),
	gender VARCHAR(1) CHECK (gender IN ('F', 'M')),
	created_at DATETIME
);

-- 판매자
DROP TABLE sp_seller;
CREATE TABLE sp_seller(
	no INT PRIMARY KEY AUTO_INCREMENT,
	id VARCHAR(30),
	password VARCHAR(160),
	shop_name VARCHAR(40)
);