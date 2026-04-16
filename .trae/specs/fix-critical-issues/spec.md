# 修复关键问题 Spec

## Why
项目检查发现了3个影响演示的关键问题：事务管理缺失、WebMvcConfiguration配置错误、测试数据日期硬编码。这些问题可能导致演示时出现数据不一致、静态资源无法访问、测试脚本运行失败等问题，需要立即修复。

## What Changes
- 为缺失的Service方法添加@Transactional注解，确保数据一致性
- 修复WebMvcConfiguration配置，从继承WebMvcConfigurationSupport改为实现WebMvcConfigurer接口
- 修复测试数据日期硬编码问题，改为动态生成当前日期的测试数据

## Impact
- Affected specs: 后端Service层、配置层、数据库初始化
- Affected code: 
  - CanteenServiceImpl.java
  - CartServiceImpl.java
  - OrderServiceImpl.java
  - QrcodeServiceImpl.java
  - UserServiceImpl.java
  - WebMvcConfiguration.java
  - data_init.sql

## ADDED Requirements

### Requirement: 事务管理完整性
所有涉及数据修改的Service方法必须使用@Transactional注解，确保操作的原子性。

#### Scenario: 添加菜品事务
- **WHEN** 管理员添加菜品
- **THEN** 应使用@Transactional注解确保操作原子性

#### Scenario: 购物车操作事务
- **WHEN** 用户修改购物车
- **THEN** 应使用@Transactional注解确保操作原子性

#### Scenario: 订单支付事务
- **WHEN** 处理订单支付
- **THEN** 应使用@Transactional注解确保状态更新原子性

### Requirement: WebMvcConfiguration正确配置
配置类应实现WebMvcConfigurer接口而非继承WebMvcConfigurationSupport，以保留Spring Boot自动配置。

#### Scenario: 拦截器配置
- **WHEN** 应用启动
- **THEN** WebMvcConfiguration应正确实现WebMvcConfigurer接口
- **AND** JWT拦截器应正常工作
- **AND** 静态资源应可访问

### Requirement: 测试数据动态生成
测试数据应使用动态日期，确保测试脚本在任何时间都能正常运行。

#### Scenario: 时间段数据初始化
- **WHEN** 初始化测试数据
- **THEN** 时间段日期应基于当前日期生成
- **AND** 测试脚本能找到可用时段
