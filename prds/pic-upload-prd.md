# 🧶 织织乐 Web 端 - 核心功能综合 PRD (V1.0 MVP)

| 版本 | 状态 | 负责人 | 目标上线时间 | 核心技术栈 |
| --- | --- | --- | --- | --- |
| v1.0 | 全栈开发中 | Jennifer | 2 个月内 | Vue 3 + TS + Spring Boot 3 + MySQL |

---

## 🎯 1. 产品背景与定位

对钩织（Crochet）和编织（Knitting）爱好者而言，看图解和记录行数/时间是天然绑定的高频动作。
本产品旨在打造一个**免登录、本地优先、云端同步**的看图手作工作台。核心解决织女“看错行”、“忘记计时”、“多设备同步麻烦”的痛点。

---

## 🎨 2. 页面全局布局与视觉设计 (UI/UE)

页面采用**左右分栏工作台**布局：

- **左侧区域（占比 70%）**：PDF/图片图解展示区，覆盖半透明滤镜与智能聚焦镂空视窗。
- **右侧区域（占比 30%）**：悬浮的手作计时与计数控制中控台，大字号、大点击热区。

---

## ⚙️ 3. 前端功能模块需求 (Feature List)

### 📂 模块 A：图解阅读器 (Reading PDF / Images)

- **A.1 PDF 基础渲染**：支持加载本地或线上 PDF/图片文件，支持鼠标滚轮缩放、原生手势滚动。
- **A.2 智能遮罩行 (亮点功能)**：
    - **视觉效果**：全局蒙层 `rgba(0, 0, 0, 0.4)`，仅在当前行切出透明视窗（镂空聚焦）。
    - **按键控制**：键盘 `↑`（上移）、`↓`（下移），默认移动步长 `20px - 30px`。
    - **自适应高度**：允许用户调整视窗高度以适配不同字号的文本。
- **A.3 穿透点击**：蒙层需配置 `pointer-events: none`，确保用户可以正常拖拽底层的 PDF 原生滚动条。

### ⏱️ 模块 B：手作计时与计数器 (Timer & Counter)

- **B.1 计时双模式切换 (核心机制)**：
    - **纯手动模式 (默认)**：秒表默认静止。用户必须物理点击“开始计时”，秒表才开始走字。
    - **动作唤醒模式**：秒表默认静止。当用户发生第一次计数动作（敲击空格或点击热区）时，系统判定已正式开工，自动唤醒并运行秒表。
- **B.2 空格键与大热区计数**：
    - **无感计数**：键盘敲击 `Space (空格键)` 触发计数 $+1$（需拦截默认网页滚动事件）。
    - **大热区**：鼠标点击右侧计数卡片任意视觉位置，触发计数 $+1$。
- **B.3 音效反馈**：计数成功时伴随清脆的“打卡”机械音效。

---

## 🗄️ 4. 后端与数据库设计 (Backend & DB)

### 📊 4.1 MySQL 数据库表结构

```sql
-- 1. 项目主表 (记录项目的基本信息)
CREATE TABLE `t_project` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '项目主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID (V1.0默认固定值 1)',
    `name` VARCHAR(100) NOT NULL COMMENT '项目名称',
    `status` TINYINT DEFAULT 0 COMMENT '状态: 0-进行中, 1-已完成',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_status` (`user_id`, `status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='钩织项目主表';

-- 2. 图解配置进度表 (关联 PDF 视觉渲染参数)
CREATE TABLE `t_pattern_config` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '配置主键ID',
    `project_id` BIGINT NOT NULL UNIQUE COMMENT '关联的项目ID',
    `file_path` VARCHAR(255) COMMENT 'PDF 存储路径',
    `current_page` INT DEFAULT 1 COMMENT '当前页码',
    `mask_top_offset` INT DEFAULT 100 COMMENT '遮罩 Y 轴偏移 (px)',
    `mask_height` INT DEFAULT 30 COMMENT '遮罩高度 (px)',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='PDF图解配置表';

-- 3. 结构化步骤计时表 (为后续文字图解计数作准备)
CREATE TABLE `t_project_step` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '步骤主键ID',
    `project_id` BIGINT NOT NULL COMMENT '关联的项目ID',
    `row_index` INT NOT NULL COMMENT '行号/圈数',
    `is_completed` TINYINT DEFAULT 0 COMMENT '是否完成',
    `cost_seconds` INT DEFAULT 0 COMMENT '该行累计耗时(秒)',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_project_row` (`project_id`, `row_index`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目步骤与计时表';
```

### 📡 4.2 Spring MVC API 接口规范

- **进度与配置获取**
    - `GET /api/v1/projects/{projectId}/pattern-config`
- **图解阅读位置同步**
    - `POST /api/v1/projects/sync-pattern-progress`
- **计数行数同步**
    - `POST /api/v1/projects/sync-row`
- **累计时长计时同步**
    - `POST /api/v1/projects/sync-time`

---

## 🚥 5. 性能与前后端联调规范 (Anti-Shaking)

1. **前端防抖保存 (Debounce)**：
    - **图解视觉 Y 轴位置**：停止拖拽或停止按键盘移动 1000 毫秒后向后端发送请求。
    - **计数点击**：疯狂连续点击时，停止点击 1500 毫秒后向后端推送最新的累计数值。
    - **计时轮询**：秒表运行时，每隔 3000 毫秒自动向云端静默同步一次累计秒数。
2. **快捷键隔离机制**：
    - 用户聚焦到 Input、Textarea 等文字录入控件时，自动挂起并禁用 `↑` `↓` `Space` 快捷键，防止干扰打字。

---

## ✅ 6. MVP 验收清单 (Go-Live Checklist)

- [ ]  成功在页面左侧渲染出 PDF 图解，且上方覆盖了镂空高亮的遮罩行。
- [ ]  切换至【动作唤醒模式】，点击网页右侧计数框，计时器自动开启走字。
- [ ]  敲击键盘空格键，计数器 +1，且页面未发生默认的滚动条向下滑动。
- [ ]  疯狂点击计数或连续移动遮罩，观察浏览器 Network F12 面板，没有发生高频 API 接口轰炸（防抖生效）。
- [ ]  刷新网页，前端生命周期钩子从 Spring Boot 拉取数据重置页面，阅读与计时进度无缝衔接。