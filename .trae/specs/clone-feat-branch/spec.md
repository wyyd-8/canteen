# 克隆 feat/v1.1.0-fullstack 分支 Spec

## Why
用户需要将 GitHub 仓库 `wyyd-8/canteen` 的 `feat/v1.1.0-fullstack` 分支克隆到本地目录 `c:\Users\11428\Desktop\canteen`，以便进行后续开发和操作。

## What Changes
- 在当前目录 `c:\Users\11428\Desktop\canteen` 下初始化或克隆 `feat/v1.1.0-fullstack` 分支的完整代码
- 保留现有 `.vscode` 配置文件

## Impact
- Affected code: 当前目录下将新增完整项目文件（Spring Boot 后端、Vue 3 前端、Docker 配置、数据库脚本等）

## ADDED Requirements

### Requirement: 克隆指定分支代码
系统 SHALL 将 `https://github.com/wyyd-8/canteen` 仓库的 `feat/v1.1.0-fullstack` 分支完整克隆到 `c:\Users\11428\Desktop\canteen` 目录下。

#### Scenario: 成功克隆
- **WHEN** 执行克隆操作
- **THEN** 当前目录下包含完整的 `feat/v1.1.0-fullstack` 分支代码，包括后端、前端、Docker 配置、数据库脚本等所有文件
- **AND** `.vscode` 目录保留不丢失
- **AND** `git branch` 显示当前处于 `feat/v1.1.0-fullstack` 分支
