# Tasks
- [x] Task 1: 修正后端配置和测试数据
  - [x] SubTask 1.1: 更新 `application.yml` 设置 `server.port: 8081`
  - [x] SubTask 1.2: 更新 `data_init.sql` 中的时间段为当前日期 (2026-04-16)
  - [x] SubTask 1.3: 更新 `full_link_test.py` 中的 `BASE_URL` 为 `http://localhost:8081` (如果不是)
- [x] Task 2: 完善前端 `OrderDetails.vue` 联调
  - [x] SubTask 2.1: 检查 `handleSlotChange` 逻辑，确保正确加载 `available-seats`
  - [x] SubTask 2.2: 支付成功后 (`handlePay`) 自动调用 `getOrder` 触发时段加载
  - [x] SubTask 2.3: 验证预约成功后的展示及 `goToQrcode` 跳转逻辑
- [x] Task 3: 全链路自动化测试验证
  - [x] SubTask 3.1: 启动 Docker 容器 (MySQL)
  - [x] SubTask 3.2: 运行 Spring Boot 应用并验证 8081 端口启动
  - [x] SubTask 3.3: 运行 `full_link_test.py` 脚本验证注册-下单-支付-预约-核销完整流程

# Task Dependencies
- [Task 2] 依赖 [Task 1] (后端服务启动且数据正确)
- [Task 3] 依赖 [Task 2] (前端功能逻辑正确)
