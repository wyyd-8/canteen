import requests
import json
import time

# 配置
BASE_URL = "http://localhost:8081"
USER_TOKEN = ""
USER_ID = None
CANTEEN_ID = 1
FOOD_ID = 1
ORDER_ID = None
RESERVATION_ID = None
QR_TOKEN = None

def test_full_link():
    global USER_TOKEN, USER_ID, ORDER_ID, RESERVATION_ID, QR_TOKEN

    print("=== 开始全链路测试 ===")

    # 1. 注册
    print("\n[1] 正在注册新用户...")
    register_data = {
        "userName": "测试用户",
        "phone": str(int(time.time())), # 唯一手机号
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
    reservation_data = {
        "orderId": ORDER_ID,
        "canteenId": CANTEEN_ID,
        "timeSlotId": 1, # 假设 ID 为 1
        "seatId": 1     # 假设 ID 为 1
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
