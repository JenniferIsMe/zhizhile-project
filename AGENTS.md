# Repository Guidelines

## 一级目录说明（Agent 快速入口）
- `zhizhile-frontend/`: 前端应用（Vue 3 + TypeScript + Vite）。主要开发目录在 `src/`，静态资源在 `public/`。
- `zhizhile-backend/`: 后端服务（Spring Boot 3 + Java 17 + Maven）。业务代码在 `src/main/java`，配置与 SQL 在 `src/main/resources`。
- `prds/`: 产品需求文档目录（当前含 `pic-upload-prd.md`），用于确认功能边界与交互预期。
- `yuanxin_pics/`: 原型/UI 截图素材目录（如工作台、设置、统计看板），用于对齐视觉与页面结构。
- `.git/`: Git 元数据目录，不作为业务代码编辑目标。

## 项目结构与模块组织
- 前后端分仓同目录管理，协作时优先按子项目独立运行。
- 后端建议按 `controller/service/mapper/domain` 分层扩展（位于 `com.zzl` 包下）。
- `specs/`（前后端均有）可用于接口或任务说明；新增规范优先放这里，避免散落。

## 构建、运行与开发命令
- 前端：`cd zhizhile-frontend && npm install && npm run dev`（本地开发）。
- 前端构建：`cd zhizhile-frontend && npm run build`（类型检查 + 打包）。
- 后端启动：`cd zhizhile-backend && mvn spring-boot:run`。
- 后端打包：`cd zhizhile-backend && mvn clean package`。
- 后端测试：`cd zhizhile-backend && mvn test`。

## 代码风格与命名
- 前端：TypeScript + Vue SFC，2 空格缩进；组件文件用 `PascalCase.vue`，普通模块用 `kebab-case` 或 `camelCase`。
- 后端：Java 17，4 空格缩进；类名 `PascalCase`，方法/变量 `camelCase`，常量 `UPPER_SNAKE_CASE`。

## 测试与提交规范
- 当前后端已接入 JUnit（`spring-boot-starter-test`），新增测试放 `zhizhile-backend/src/test/java`，文件名使用 `*Test.java`。
- 前端暂未配置测试框架；若新增，建议采用 Vitest 并将测试放在 `src/**/*.test.ts`。
- 提交信息延续现有风格：简短中文动宾短语，例如“新增图片上传接口并补充参数校验”。
- PR 至少包含：变更范围、运行/测试结果、关联需求（`prds/` 文件名），涉及 UI 请附截图（可引用 `yuanxin_pics/` 对照）。
