import requests
import json
import time
from datetime import date, timedelta

# 配置
BASE_URL = "http://localhost:8081"
USER_TOKEN = ""
USER_ID = None
CANTEEN_ID = 1
FOOD_ID = 1
ORDER_ID = None
RESERVATION_ID = None
QR_TOKEN = None

def init_test_data():
    """初始化测试数据，确保有当前日期的时间段"""
    print("\n[0] 初始化测试数据...")
    
    try:
        import pymysql
        
        # 连接数据库
        conn = pymysql.connect(
            host='localhost',
            port=3306,
            user='root',
            password='1234',
            database='canteen',
            charset='utf8mb4'
        )
        cursor = conn.cursor()
        
        # 清理旧数据，确保测试环境干净
        cursor.execute("SET FOREIGN_KEY_CHECKS = 0")
        cursor.execute("TRUNCATE TABLE qr_scan_logs")
        cursor.execute("TRUNCATE TABLE pickup_qr_codes")
        cursor.execute("TRUNCATE TABLE reservations")
        cursor.execute("TRUNCATE TABLE order_items")
        cursor.execute("TRUNCATE TABLE orders")
        cursor.execute("TRUNCATE TABLE shopping_cart_items")
        cursor.execute("SET FOREIGN_KEY_CHECKS = 1")
        
        # 获取当前日期和未来几天的日期
        today = date.today()
        tomorrow = today + timedelta(days=1)
        
        # 删除旧的时间段数据（可选，避免重复）
        cursor.execute("DELETE FROM canteen_time_slots WHERE slot_date < %s", (today,))
        
        # 检查是否已有当前日期的时间段
        cursor.execute("SELECT COUNT(*) FROM canteen_time_slots WHERE slot_date >= %s", (today,))
        count = cursor.fetchone()[0]
        
        if count == 0:
            # 插入当前日期和明天的时间段
            time_slots = [
                # 食堂1 - 今天
                (1, today, '11:00:00', '12:00:00', 50),
                (1, today, '12:00:00', '13:00:00', 50),
                (1, today, '17:00:00', '18:00:00', 50),
                # 食堂1 - 明天
                (1, tomorrow, '11:00:00', '12:00:00', 50),
                (1, tomorrow, '12:00:00', '13:00:00', 50),
                # 食堂2 - 今天
                (2, today, '11:30:00', '12:30:00', 40),
                (2, today, '17:30:00', '18:30:00', 40),
            ]
            
            sql = """
                INSERT INTO canteen_time_slots (canteen_id, slot_date, start_time, end_time, reservation_limit)
                VALUES (%s, %s, %s, %s, %s)
            """
            cursor.executemany(sql, time_slots)
            conn.commit()
            print(f"✓ 已插入 {len(time_slots)} 个时间段（日期：{today} 和 {tomorrow}）")
        else:
            print(f"✓ 已存在 {count} 个有效时间段，无需插入")
        
        cursor.close()
        conn.close()
        
    except ImportError:
        print("⚠ 未安装 pymysql，跳过数据初始化。请运行: pip install pymysql")
        print("  如果数据库中已有时间段数据，测试可能仍能继续。")
    except Exception as e:
        print(f"⚠ 数据库初始化失败: {e}")
        print("  如果数据库中已有时间段数据，测试可能仍能继续。")

def test_full_link():
    global USER_TOKEN, USER_ID, ORDER_ID, RESERVATION_ID, QR_TOKEN

    print("=== 开始全链路测试 ===")
    
    # 0. 初始化测试数据
    init_test_data()

    # 1. 注册
    print("\n[1] 正在注册新用户...")
    register_data = {
        "userName": "测试用户",
        "phone": "188" + str(int(time.time()))[-8:], # 11位合规手机号
        "password": "password123"
    }
    resp = requests.post(f"{BASE_URL}/api/v1/user/register", json=register_data)
    print(f"状态码: {resp.status_code}, 响应: {resp.text}")
    assert resp.status_code == 200

    # 2. 登录
    print("\n[2] 正在登录...")
    login_data = {
        "phone": register_data["phone"],
        "password": register_data["password"]
    }
    resp = requests.post(f"{BASE_URL}/api/v1/user/login", json=login_data)
    print(f"状态码: {resp.status_code}, 响应: {resp.text}")
    assert resp.status_code == 200
    res_json = resp.json()
    USER_TOKEN = res_json['data']['token']
    USER_ID = res_json['data']['id']
    headers = {"token": USER_TOKEN}

    # 3. 查看食堂列表 (公开接口)
    print("\n[3] 查看食堂列表...")
    resp = requests.get(f"{BASE_URL}/api/v1/user/canteens")
    print(f"状态码: {resp.status_code}, 食堂数量: {len(resp.json()['data'])}")
    assert resp.status_code == 200

    # 4. 查看菜品列表 (公开接口)
    print("\n[4] 查看菜品列表...")
    resp = requests.get(f"{BASE_URL}/api/v1/user/canteens/{CANTEEN_ID}/foods")
    print(f"状态码: {resp.status_code}")
    assert resp.status_code == 200

    # 5. 加入购物车 (需要 Token)
    print("\n[5] 加入购物车...")
    cart_data = {
        "foodId": FOOD_ID,
        "quantity": 2
    }
    resp = requests.post(f"{BASE_URL}/api/v1/user/cart/items", json=cart_data, headers=headers)
    print(f"状态码: {resp.status_code}")
    assert resp.status_code == 200

    # 6. 下单 (需要 Token)
    print("\n[6] 创建订单...")
    order_data = {
        "canteenId": CANTEEN_ID,
        "paymentMethod": "WECHAT"
    }
    resp = requests.post(f"{BASE_URL}/api/v1/user/orders", json=order_data, headers=headers)
    print(f"状态码: {resp.status_code}, 响应: {resp.text}")
    assert resp.status_code == 200
    ORDER_ID = resp.json()['data']['id']

    # 7. 模拟支付回调 (内部/模拟接口)
    print("\n[7] 模拟支付回调...")
    callback_data = {"orderId": ORDER_ID}
    resp = requests.post(f"{BASE_URL}/api/v1/user/orders/payment-callback", json=callback_data, headers=headers)
    print(f"状态码: {resp.status_code}")
    assert resp.status_code == 200

    # 8. 预约就餐 (需要 Token)
    print("\n[8] 预约就餐...")
    # 8.1 获取可选时段
    resp = requests.get(f"{BASE_URL}/api/v1/user/canteens/{CANTEEN_ID}/time-slots", headers=headers)
    print(f"获取时段状态码: {resp.status_code}")
    assert resp.status_code == 200
    slots = resp.json()['data']
    print(f"获取到 {len(slots)} 个时段")
    assert len(slots) > 0
    
    time_slot_id = None
    seat_id = None
    
    for slot in slots:
        # 8.2 获取该时段可选座位
        resp = requests.get(f"{BASE_URL}/api/v1/user/canteens/{CANTEEN_ID}/available-seats", params={"timeSlotId": slot['id']}, headers=headers)
        if resp.status_code == 200:
            seats = resp.json()['data']
            if len(seats) > 0:
                time_slot_id = slot['id']
                seat_id = seats[0]['id']
                print(f"找到可用时段 {slot['startTime']}-{slot['endTime']}, 座位 {seats[0]['seatNo']}")
                break
    
    if not time_slot_id:
        print("未找到可用座位，请重置数据库或增加座位数据")
        assert False

    reservation_data = {
        "orderId": ORDER_ID,
        "canteenId": CANTEEN_ID,
        "timeSlotId": time_slot_id,
        "seatId": seat_id
    }
    resp = requests.post(f"{BASE_URL}/api/v1/user/reservations", json=reservation_data, headers=headers)
    print(f"状态码: {resp.status_code}, 响应: {resp.text}")
    assert resp.status_code == 200
    RESERVATION_ID = resp.json()['data']['id']

    # 9. 生成取餐二维码 (需要 Token)
    print("\n[9] 生成取餐二维码...")
    qr_data = {
        "orderId": ORDER_ID,
        "reservationId": RESERVATION_ID
    }
    resp = requests.post(f"{BASE_URL}/api/v1/user/pickup-qrcodes", json=qr_data, headers=headers)
    print(f"状态码: {resp.status_code}, 响应: {resp.text}")
    assert resp.status_code == 200
    QR_TOKEN = resp.json()['data']['qrToken']

    # 10. 管理端扫码核销 (需要 Token, 模拟操作人)
    print("\n[10] 管理端扫码核销...")
    scan_data = {
        "qrToken": QR_TOKEN,
        "operatorId": 1 # 假设管理员 ID 为 1
    }
    resp = requests.post(f"{BASE_URL}/api/v1/admin/pickup-qrcodes/scan", json=scan_data, headers=headers)
    print(f"状态码: {resp.status_code}, 响应: {resp.text}")
    assert resp.status_code == 200

    print("\n=== 全链路测试完成 ===")

if __name__ == "__main__":
    try:
        test_full_link()
    except requests.exceptions.ConnectionError:
        print("\n[错误] 无法连接到服务器，请确保 Spring Boot 应用已在 http://localhost:8080 启动。")
    except Exception as e:
        print(f"\n[错误] 测试过程中出现异常: {e}")
