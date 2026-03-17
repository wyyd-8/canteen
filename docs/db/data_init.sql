SET NAMES utf8mb4;
-- 初始化测试数据 (V1)
-- 请在执行全链路测试前运行此脚本

-- 1. 初始化食堂
INSERT INTO canteens (canteen_code, canteen_name, capacity, is_active) 
VALUES ('C001', '第一食堂', 200, 1), ('C002', '第二食堂', 150, 1);

-- 2. 初始化时间段 (假设今天是 2026-03-17)
INSERT INTO canteen_time_slots (canteen_id, slot_date, start_time, end_time, reservation_limit)
VALUES (1, '2026-03-17', '11:00:00', '12:00:00', 50),
       (1, '2026-03-17', '12:00:00', '13:00:00', 50),
       (2, '2026-03-17', '11:30:00', '12:30:00', 40);

-- 3. 初始化座位
INSERT INTO seats (canteen_id, seat_no, seat_area, is_active)
VALUES (1, 'A-001', '一楼东区', 1),
       (1, 'A-002', '一楼东区', 1),
       (2, 'B-101', '二楼窗口旁', 1);

-- 4. 初始化菜品
INSERT INTO food_items (canteen_id, food_name, category, price, stock, is_on_sale)
VALUES (1, '红烧肉', '热菜', 15.00, 100, 1),
       (1, '清炒时蔬', '热菜', 6.00, 200, 1),
       (1, '米饭', '主食', 1.00, 500, 1),
       (2, '牛肉拉面', '面食', 12.00, 80, 1);

-- 5. 初始化管理员 (用于扫码核销)
-- 密码设为 password123
INSERT INTO users (user_no, user_name, phone, password, role)
VALUES ('ADMIN001', '系统管理员', '18888888888', 'password123', 'ADMIN');
