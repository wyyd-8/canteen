SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- 食堂就餐管理系统 MySQL 表结构草案（V1）
-- 兼容：MySQL 8+

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_no VARCHAR(32) NOT NULL UNIQUE,
    user_name VARCHAR(64) NOT NULL,
    phone VARCHAR(32) NULL,
    password VARCHAR(128) NOT NULL,
    role ENUM('USER', 'ADMIN') NOT NULL DEFAULT 'USER',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE canteens (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_code VARCHAR(32) NOT NULL UNIQUE,
    canteen_name VARCHAR(128) NOT NULL,
    capacity INT NOT NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE canteen_time_slots (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_id BIGINT NOT NULL,
    slot_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    reservation_limit INT NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_slot_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    UNIQUE KEY uk_slot_unique (canteen_id, slot_date, start_time, end_time),
    KEY idx_slot_date (slot_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE seats (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_id BIGINT NOT NULL,
    seat_no VARCHAR(32) NOT NULL,
    seat_area VARCHAR(64) NULL,
    is_active TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_seat_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    UNIQUE KEY uk_seat_no (canteen_id, seat_no)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE food_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    canteen_id BIGINT NOT NULL,
    food_name VARCHAR(128) NOT NULL,
    category VARCHAR(64) NULL,
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    is_on_sale TINYINT(1) NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_food_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    KEY idx_food_sale (canteen_id, is_on_sale)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE shopping_cart_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_cart_item_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_cart_item_food FOREIGN KEY (food_id) REFERENCES food_items(id),
    UNIQUE KEY uk_cart_user_food (user_id, food_id),
    KEY idx_cart_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    canteen_id BIGINT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(32) NOT NULL,
    order_status ENUM('CREATED', 'PAID', 'COMPLETED', 'CANCELLED') NOT NULL,
    paid_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_order_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    KEY idx_order_user_time (user_id, created_at),
    KEY idx_order_canteen_time (canteen_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    food_id BIGINT NOT NULL,
    food_name_snapshot VARCHAR(128) NOT NULL,
    unit_price_snapshot DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    subtotal_amount DECIMAL(10,2) NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_order_item_food FOREIGN KEY (food_id) REFERENCES food_items(id),
    KEY idx_order_item_order (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE reservations (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    reservation_code VARCHAR(64) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    canteen_id BIGINT NOT NULL,
    time_slot_id BIGINT NOT NULL,
    seat_id BIGINT NOT NULL,
    reservation_status ENUM('RESERVED', 'CHECKED_IN', 'CANCELLED', 'EXPIRED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_res_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_res_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_res_canteen FOREIGN KEY (canteen_id) REFERENCES canteens(id),
    CONSTRAINT fk_res_slot FOREIGN KEY (time_slot_id) REFERENCES canteen_time_slots(id),
    CONSTRAINT fk_res_seat FOREIGN KEY (seat_id) REFERENCES seats(id),
    UNIQUE KEY uk_res_slot_seat (time_slot_id, seat_id),
    KEY idx_res_canteen_slot (canteen_id, time_slot_id),
    KEY idx_res_user_time (user_id, created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE pickup_qr_codes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    qr_token CHAR(32) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    order_id BIGINT NOT NULL,
    reservation_id BIGINT NOT NULL,
    qr_status ENUM('ACTIVE', 'USED', 'EXPIRED', 'INVALID') NOT NULL DEFAULT 'ACTIVE',
    expires_at DATETIME NOT NULL,
    used_at DATETIME NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_qr_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_qr_order FOREIGN KEY (order_id) REFERENCES orders(id),
    CONSTRAINT fk_qr_reservation FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    KEY idx_qr_status_expire (qr_status, expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE qr_scan_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    qr_code_id BIGINT NOT NULL,
    operator_id BIGINT NOT NULL,
    scan_result ENUM('SUCCESS', 'INVALID', 'EXPIRED', 'REPEATED') NOT NULL,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_scan_qr FOREIGN KEY (qr_code_id) REFERENCES pickup_qr_codes(id),
    CONSTRAINT fk_scan_operator FOREIGN KEY (operator_id) REFERENCES users(id),
    KEY idx_scan_time (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 统计建议：
-- 1. 实时人数：可基于 reservations + reservation_status + time_slot 计算，或落地到汇总表。
-- 2. 历史就餐人数与销量：可通过 orders/order_items 按日聚合得到。
