# 食堂就餐管理系统后端接口文档（V1）

## 1. 模块划分

- 用户端模块（`/api/v1/user/**`）
  - 食堂浏览与预约人数预览
  - 菜品浏览与下单
  - 就餐预约（座位 + 时间段）
  - 取餐二维码生成与查询
- 管理端模块（`/api/v1/admin/**`）
  - 实时人数看板
  - 历史用餐人数与菜品销量统计
  - 食堂菜品管理
  - 二维码核销查询

统一响应格式（示例）：

```json
{
  "code": 0,
  "message": "ok",
  "data": {}
}
```

---

## 2. 用户端接口

### 2.1 获取食堂列表（含预约人数预览）

- **GET** `/api/v1/user/canteens`
- Query 参数：
  - `date`（可选，`yyyy-MM-dd`，默认当天）
  - `timeSlotId`（可选，时间段 ID）
- 返回字段（`data[]`）：
  - `canteenId`
  - `canteenName`
  - `currentDiningCount`：当前就餐人数
  - `reservedCount`：指定时段已预约人数
  - `capacity`

### 2.2 获取某食堂可售菜品

- **GET** `/api/v1/user/canteens/{canteenId}/foods`
- Query 参数：
  - `onSaleOnly`（可选，默认 `true`）
- 返回字段（`data[]`）：
  - `foodId`、`foodName`、`price`
  - `stock`
  - `isOnSale`

### 2.3 创建订单（购买食物）

- **POST** `/api/v1/user/orders`
- 请求体：

```json
{
  "userId": 10001,
  "canteenId": 1,
  "items": [
    { "foodId": 501, "quantity": 2 },
    { "foodId": 607, "quantity": 1 }
  ],
  "paymentMethod": "WECHAT"
}
```

- 返回：
  - `orderId`
  - `orderNo`
  - `totalAmount`
  - `orderStatus`

### 2.4 预约就餐座位与时间段

- **POST** `/api/v1/user/reservations`
- 请求体：

```json
{
  "userId": 10001,
  "orderId": 200020,
  "canteenId": 1,
  "timeSlotId": 3001,
  "seatId": 701
}
```

- 业务约束：
  - 仅允许对已支付订单预约
  - 同一 `seatId + date + timeSlotId` 不能重复预约
- 返回：
  - `reservationId`
  - `reservationCode`
  - `reservationStatus`

### 2.5 生成取餐二维码

- **POST** `/api/v1/user/pickup-qrcodes`
- 请求体：

```json
{
  "userId": 10001,
  "orderId": 200020,
  "reservationId": 9009
}
```

- 返回：
  - `qrCodeId`
  - `qrToken`（唯一短码）
  - `expiresAt`
  - `qrImageUrl`

### 2.6 获取订单与二维码详情

- **GET** `/api/v1/user/orders/{orderId}`
- 返回含：订单详情、菜品明细、预约信息、二维码状态

---

## 3. 管理端接口

### 3.1 实时人数看板

- **GET** `/api/v1/admin/canteens/realtime-occupancy`
- Query 参数：
  - `canteenId`（可选，不传则返回全部食堂）
- 返回字段（`data[]`）：
  - `canteenId`
  - `currentDiningCount`
  - `reservedCountInCurrentSlot`
  - `capacity`
  - `occupancyRate`

### 3.2 近几日用餐人数统计

- **GET** `/api/v1/admin/statistics/dining`
- Query 参数：
  - `canteenId`（可选）
  - `startDate`、`endDate`（必填）
- 返回字段（`data[]`）：
  - `date`
  - `diningCount`
  - `reservationCount`

### 3.3 近几日菜品销量统计

- **GET** `/api/v1/admin/statistics/food-sales`
- Query 参数：
  - `canteenId`（可选）
  - `startDate`、`endDate`（必填）
- 返回字段（`data[]`）：
  - `foodId`
  - `foodName`
  - `soldQuantity`
  - `soldAmount`

### 3.4 管理菜品列表

- **GET** `/api/v1/admin/canteens/{canteenId}/foods`
- **POST** `/api/v1/admin/canteens/{canteenId}/foods`
- **PUT** `/api/v1/admin/canteens/{canteenId}/foods/{foodId}`
- **DELETE** `/api/v1/admin/canteens/{canteenId}/foods/{foodId}`（逻辑下架）

新增/修改请求体字段建议：
- `foodName`
- `price`
- `stock`
- `isOnSale`
- `category`

### 3.5 扫码核销并查看用户购买信息

- **POST** `/api/v1/admin/pickup-qrcodes/scan`
- 请求体：

```json
{
  "qrToken": "8ff1b8d1d9bd4f72a6ce3c03d7b2ef58",
  "operatorId": 30001
}
```

- 返回字段：
  - 用户信息（`userId`、`userName`）
  - 订单信息（`orderNo`、`totalAmount`、`paidAt`）
  - 菜品明细（`foodName`、`quantity`）
  - 预约信息（`canteenName`、`timeSlot`、`seatNo`）
  - 核销状态（`verified`）

---

## 4. 关键状态流转

- 订单：`CREATED -> PAID -> COMPLETED/CANCELLED`
- 预约：`RESERVED -> CHECKED_IN -> CANCELLED/EXPIRED`
- 二维码：`ACTIVE -> USED/EXPIRED/INVALID`

---

## 5. 安全与幂等建议

- 管理端接口需 RBAC（如 `ROLE_ADMIN`）
- 下单、预约、生成二维码接口建议携带 `Idempotency-Key`
- 扫码核销接口需保证一次性：二维码使用后不可重复核销
- 敏感数据（如手机号）仅在必要接口返回，并做脱敏
