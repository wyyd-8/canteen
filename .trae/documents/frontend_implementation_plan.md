# 食堂就餐管理系统前端开发规划

本项目前端将采用 **Vue 3 + Vite + Axios + Element Plus** 技术栈，构建一个响应式、易用的单页面应用（SPA）。

## 1. 技术栈选型
- **框架**: Vue 3 (Composition API)
- **构建工具**: Vite
- **UI 组件库**: Element Plus
- **路由管理**: Vue Router 4
- **状态管理**: Pinia
- **网络请求**: Axios
- **图表展示**: ECharts (用于管理端统计报表)

## 2. 目录结构设计
```text
canteen-frontend/
├── src/
│   ├── api/            # Axios 请求封装与接口定义
│   ├── assets/         # 静态资源（图片、全局样式）
│   ├── components/     # 公共组件
│   ├── layout/         # 页面布局组件 (UserLayout, AdminLayout)
│   ├── store/          # Pinia 状态管理 (user, cart)
│   ├── router/         # 路由配置
│   ├── utils/          # 工具类 (request.js, auth.js)
│   └── views/          # 业务页面组件
│       ├── user/       # 用户端页面
│       └── admin/      # 管理端页面
├── public/
└── index.html
```

## 3. 核心功能实现步骤

### 3.1 基础配置
1.  **Axios 封装**: 设置统一的 `baseURL` 为后端接口地址，配置请求拦截器（添加 token 或 userId）和响应拦截器（处理统一响应格式 `Result<T>`）。
2.  **Element Plus 集成**: 全局引入组件及样式，配置主题色。
3.  **状态管理**: 使用 Pinia 存储当前登录用户信息和购物车状态。

### 3.2 用户端 (User Side)
- **注册与认证**: 实现用户注册页面，并模拟简单的登录（存储 `userId`）。
- **食堂概览**: 首页展示食堂列表及其当前占用率、剩余座位。
- **选餐与购物车**: 
    - 某食堂的菜品列表展示。
    - 购物车加减逻辑，与后端同步数据。
- **订单流程**:
    - 下单页面：确认购物车明细。
    - 支付模拟：跳转至支付成功页面。
- **预约与核销**:
    - 支付成功后，选择预约的时间段和座位。
    - 生成并展示取餐二维码（基于后端返回的 `qrToken`）。
- **个人中心**: 查看历史订单及其预约状态。

### 3.3 管理端 (Admin Side)
- **实时看板**: 
    - 使用卡片和进度条展示各食堂实时占用率。
    - 自动轮询或刷新数据。
- **统计报表**: 
    - 就餐人数统计（折线图）。
    - 菜品销量排行统计（柱状图）。
- **菜品管理**: 
    - 实现菜品的 CRUD（增删改查）表格。
    - 支持按食堂筛选。
- **扫码核销**: 
    - 输入或扫描 `qrToken` 进行核销。
    - 核销成功后展示关联的订单和用户信息。

## 4. 路由规划 (Router)
- `/user/register`: 注册
- `/user/canteens`: 食堂列表
- `/user/canteens/:id/foods`: 选餐
- `/user/cart`: 购物车
- `/user/orders/:id`: 订单详情与预约
- `/admin/dashboard`: 实时看板
- `/admin/statistics`: 统计分析
- `/admin/foods`: 菜品管理
- `/admin/verify`: 扫码核销

## 5. 开发顺序建议
1.  **Phase 1**: 项目初始化，Axios 与 Element Plus 基础配置。
2.  **Phase 2**: 用户端注册、食堂列表、菜品列表展示。
3.  **Phase 3**: 购物车与订单下单流程（连通后端接口）。
4.  **Phase 4**: 预约系统与二维码生成展示。
5.  **Phase 5**: 管理端实时看板与菜品管理。
6.  **Phase 6**: 管理端统计报表（集成 ECharts）。
7.  **Phase 7**: 扫码核销功能实现与整体联调。
