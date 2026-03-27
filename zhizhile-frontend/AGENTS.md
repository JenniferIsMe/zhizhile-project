# Repository Guidelines

## 项目结构与模块组织
- `src/`：前端业务代码主目录。
- 入口文件是 `src/main.ts`，根组件是 `src/App.vue`。
- 全局样式放在 `src/style.css`。
- 静态资源放在 `public/`，用于直接对外提供。
- 需求与任务拆解文档位于 `specs/`（如 `specs/spec.md`）。
- 构建与工具配置位于仓库根目录：`vite.config.ts`、`tsconfig*.json`、`package.json`。

## 构建、测试与开发命令
- `npm install`：安装依赖。
- `npm run dev`：启动 Vite 本地开发服务。
- `npm run build`：先执行 `vue-tsc -b` 类型检查，再构建生产包。
- `npm run preview`：本地预览生产构建结果。

示例：
```bash
npm install
npm run dev
npm run build
```

## 代码风格与命名规范
- 使用 TypeScript + Vue 3 SFC（`.vue`）。
- 使用 2 空格缩进（与现有代码保持一致）。
- 组件文件采用 PascalCase（例如 `ProjectHeader.vue`）。
- 工具模块在同一目录内保持统一命名（camelCase 或 kebab-case 二选一）。
- 路由和模块命名应贴合业务语义：`library`、`workbench`、`settings`、`stats`。
- 局部样式优先写在组件内；全局规则放入 `src/style.css`。

## 测试规范
- 当前 `package.json` 尚未配置自动化测试框架。
- 现阶段最低质量门槛：提交 PR 前必须通过 `npm run build`。
- 后续引入测试时，建议放在同目录或 `src/__tests__/`，文件命名使用 `*.spec.ts`。

## 提交与 Pull Request 规范
- 提交信息使用简洁祈使句；当前历史以中文短句为主（示例：`初始化前、后端项目，实现后端的接口层代码`）。
- 每次提交应聚焦单一变更主题。
- PR 至少包含：
- 变更范围与动机说明；
- 关联 issue 或规格文档链接；
- 涉及 UI 时提供截图或录屏；
- 验证步骤（执行命令与预期结果）。

## 安全与配置建议
- 禁止提交密钥、令牌及任何环境敏感信息。
- 随着 API 接入推进，优先通过 Vite 环境变量管理运行时配置（如接口基地址）。
