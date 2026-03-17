# 食堂就餐管理系统 (Canteen Management System)

本项目是一个基于 Spring Boot 3 的后端服务，集成了 JWT 认证、MyBatis 持久层框架以及全链路业务流程。

## 1. 环境准备

### 1.1 软件依赖
- **Java 21**
- **Maven 3.x**
- **MySQL 8.0+**
- **Docker** (推荐，用于快速启动数据库)

### 1.2 数据库准备 (使用 Docker)
本项目根目录下提供了 `docker-compose.yml`，可以一键启动 MySQL 并自动初始化表结构和测试数据：

```bash
docker-compose up -d
```

启动后，数据库信息如下：
- **地址**: `localhost:3306`
- **库名**: `canteen`
- **用户**: `root`
- **密码**: `1234`

*如果不使用 Docker，请手动执行 `docs/db/schema.sql` 和 `docs/db/data_init.sql`。*

## 2. 项目运行

### 2.1 编译与打包
```bash
./mvnw clean package -DskipTests
```

### 2.2 启动应用
```bash
./mvnw spring-boot:run
```
默认运行在 `http://localhost:8080`。

## 3. 全链路测试

项目中包含了一套全链路测试脚本，用于验证从注册到核销的完整业务流程。

### 3.1 Python 脚本测试 (推荐)
1. 确保已安装 `requests` 库：`pip install requests`
2. 运行脚本：`python tests/full_link_test.py`

### 3.2 REST Client 测试
打开 `tests/full_link_test.http`，使用 VS Code / Trae 的 REST Client 插件进行交互式测试。

## 4. 核心功能说明
- **认证**: 使用 JWT 令牌，除公开接口外，所有请求需在 Header 中携带 `token`。
- **用户端**: 支持注册、登录、浏览食堂、购物车、下单、预约就餐、生成取餐码。
- **管理端**: 支持扫码核销、实时看板、历史统计、菜品管理。
- **上下文**: 后端通过 `ThreadLocal` (BaseContext) 自动管理当前登录用户 ID。
