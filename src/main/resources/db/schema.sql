-- Create Database
CREATE DATABASE IF NOT EXISTS coupon_service;
USE coupon_service;

-- User (회원) Table
CREATE TABLE user (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      phone VARCHAR(20),
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(50) NOT NULL DEFAULT 'USER' COMMENT '권한 (USER / ADMIN)',
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                      deleted_at TIMESTAMP NULL
) COMMENT='회원 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Auth Table
CREATE TABLE auth (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      user_id BIGINT NOT NULL,
                      token VARCHAR(500) NOT NULL,
                      expired_at TIMESTAMP NOT NULL
) COMMENT='인증 토큰 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Event Table
CREATE TABLE event (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       title VARCHAR(255) NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='이벤트 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Coupon Master (쿠폰 마스터) Table
CREATE TABLE coupon_master (
                               id BIGINT AUTO_INCREMENT PRIMARY KEY,
                               event_id BIGINT NOT NULL,
                               title VARCHAR(255) NOT NULL COMMENT '쿠폰명',
                               discount_type VARCHAR(50) NOT NULL COMMENT '쿠폰 타입 (FIXED / RATE)',
                               discount_value DECIMAL(10, 2) NOT NULL COMMENT '할인 금액 or 할인율',
                               total_quantity INT NOT NULL COMMENT '쿠폰 발급 수량 제한',
                               issue_expired_at TIMESTAMP NOT NULL COMMENT '쿠폰 발급 기한',
                               use_expired_at TIMESTAMP NOT NULL COMMENT '쿠폰 사용 기한',
                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT='쿠폰 마스터 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Coupon Issue (쿠폰 발급) Table
CREATE TABLE coupon_issue (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              coupon_master_id BIGINT NOT NULL,
                              status VARCHAR(50) NOT NULL DEFAULT 'UNUSED' COMMENT '쿠폰 상태 (UNUSED / USED / EXPIRED)',
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '발급일자',
                              used_at TIMESTAMP NULL COMMENT '사용일자',
                              deleted_at TIMESTAMP NULL COMMENT '삭제일자'
) COMMENT='쿠폰 발급 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Product (상품) Table
CREATE TABLE product (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         title VARCHAR(255) NOT NULL COMMENT '상품명',
                         price DECIMAL(10, 2) NOT NULL COMMENT '가격',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) COMMENT='상품 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Order (주문) Table
CREATE TABLE `order` (
                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                         user_id BIGINT NOT NULL,
                         product_id BIGINT NOT NULL,
                         coupon_issue_id BIGINT,
                         quantity INT NOT NULL COMMENT '주문 수량',
                         original_price DECIMAL(10, 2) NOT NULL COMMENT '원본 가격 (수량 × 상품 가격)',
                         discount_price DECIMAL(10, 2) NOT NULL DEFAULT 0 COMMENT '할인 금액',
                         final_price DECIMAL(10, 2) NOT NULL COMMENT '최종 가격',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '주문일자'
) COMMENT='주문 테이블' ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Indexes
CREATE INDEX idx_auth_user_id ON auth(user_id);
CREATE INDEX idx_auth_token ON auth(token);
CREATE INDEX idx_coupon_master_event_id ON coupon_master(event_id);
CREATE INDEX idx_coupon_issue_user_id ON coupon_issue(user_id);
CREATE INDEX idx_coupon_issue_coupon_master_id ON coupon_issue(coupon_master_id);
CREATE INDEX idx_order_user_id ON `order`(user_id);
CREATE INDEX idx_order_product_id ON `order`(product_id);
CREATE INDEX idx_order_coupon_issue_id ON `order`(coupon_issue_id);