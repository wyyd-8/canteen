# 动态预约功能实现与联调 Spec

## Why
目前系统的预约功能在前端虽然有初步实现，但需要确保全链路（从下单、支付到根据订单所属食堂动态查询可用时段和座位）能够正常工作，并解决之前用户反馈的 500 错误和乱码问题后的稳定性验证。

## What Changes
- **前端 (OrderDetails.vue)**: 
    - 确保级联选择逻辑：选择时段后动态加载该时段的可用座位。
    - 支付成功后自动触发时段查询。
    - 预约成功后展示预约号并提供跳转到取餐码页面的功能。
- **后端 (配置)**:
    - 确保 `application.yml` 中 `server.port` 为 8081 以匹配前端代理。
    - 验证 `GlobalExceptionHandler` 能够处理预约冲突（如座位已被占）。
- **测试数据 (data_init.sql)**:
    - 更新时间段数据为当前日期（2026-04-16），确保测试脚本和前端可以查到有效数据。
- **验证**:
    - 使用 `full_link_test.py` 进行全流程自动化测试。

## Impact
- Affected specs: 用户端预约流程。
- Affected code: `OrderDetails.vue`, `application.yml`, `data_init.sql`, `full_link_test.py`。

## ADDED Requirements
### Requirement: 动态时段与座位级联选择
系统应根据订单所属食堂，动态获取该食堂的可用时段；在用户选择时段后，系统应获取该时段内未被预约的座位。

#### Scenario: 成功预约流程
- **WHEN** 用户完成订单支付
- **THEN** 系统显示预约表单，并加载当前食堂的可选时段
- **WHEN** 用户选择一个时段
- **THEN** 系统加载该时段下的可用座位列表
- **WHEN** 用户选择座位并点击确认预约
- **THEN** 系统创建预约记录，并返回预约成功提示及预约号

## MODIFIED Requirements
### Requirement: 端口一致性
Spring Boot 应用端口应固定为 8081，以确保与前端 `vite.config.js` 中的代理配置一致。
