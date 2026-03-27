# 织织乐前端开发任务拆解（按页面/组件维度）

## 1. 文档目标
基于以下输入产出前端开发任务拆解，用于 `zhizhile-frontend` 落地实施：
- `prds/pic-upload-prd.md`
- `yuanxin_pics/工作台.png`
- `yuanxin_pics/设置.png`
- `yuanxin_pics/图解库.png`
- `zhizhile-backend/specs/spec.md`
- `zhizhile-backend/specs/plan.md`

## 2. 页面范围（MVP）
- 图解库页（Pattern Library）
- 工作台页（Workbench）
- 设置页（Settings）
- 全局骨架（侧边栏、顶部栏、状态提示、弹窗）
- 数据统计页（导航保留，MVP 可占位）

## 3. 全局公共模块任务

### 3.1 应用骨架与路由
- 建立路由：`/library`、`/workbench/:projectId`、`/settings`、`/stats`
- 实现左侧导航高亮与页面切换
- 提供全局空态、加载态、错误态容器

### 3.2 API 基础层
- 统一封装请求客户端（超时、错误映射、响应解包）
- 对接后端接口：
- `POST /api/v1/projects`
- `GET /api/v1/projects/{projectId}`
- `GET /api/v1/projects/{projectId}/overview`
- `POST /api/v1/projects/{projectId}/patterns/upload`
- `POST /api/v1/projects/{projectId}/patterns/link`
- `GET /api/v1/projects/{projectId}/patterns`
- `PUT /api/v1/projects/{projectId}/patterns/{patternId}/current`
- `DELETE /api/v1/projects/{projectId}/patterns/{patternId}`
- `GET /api/v1/files/{fileId}/content`
- `GET /api/v1/projects/{projectId}/pattern-config`
- `PUT /api/v1/projects/{projectId}/pattern-config`
- `PUT /api/v1/projects/{projectId}/progress/row`
- `PUT /api/v1/projects/{projectId}/progress/time`
- `GET /api/v1/projects/{projectId}/progress`

### 3.3 全局状态管理
- 拆分状态域：项目、图解列表、当前图解、阅读配置、计数计时、设置项
- 页面刷新恢复：优先调用 `overview` 一次性恢复
- 实现“最后写入生效”的前端协作策略（本地覆盖显示 + 服务端返回校正）

### 3.4 防抖与快捷键中枢
- 阅读配置保存防抖：1000ms
- 计数同步防抖：1500ms
- 计时同步轮询：每 3000ms
- 快捷键：`ArrowUp`、`ArrowDown`、`Space`
- 输入框聚焦时快捷键自动禁用

### 3.5 通用反馈组件
- Toast（成功/失败）
- Confirm Dialog（删除图解）
- Upload Panel（上传/链接输入）
- Empty State（无项目、无图解、无当前图解）

## 4. 页面任务拆解

## 4.1 图解库页（/library）
### 页面目标
- 管理项目与项目内图解资源
- 支持新增项目、上传图解、添加链接、切换当前图解、删除图解

### 组件与任务
- `ProjectHeader`
- 搜索框（前端过滤）
- 新建项目按钮（调用 `POST /projects`）
- `ProjectCardGrid`
- 项目卡片渲染（封面、名称、格式标签、更新时间）
- 进入工作台按钮（跳转 `/workbench/:projectId`）
- `PatternManageDrawer/Modal`
- 上传图解文件（`POST /patterns/upload`）
- 添加图解链接（`POST /patterns/link`，前端 URL 格式校验）
- 图解列表展示（`GET /patterns`）
- 设为当前图解（`PUT /patterns/{patternId}/current`）
- 删除图解（`DELETE /patterns/{patternId}` + 二次确认）
- `ProjectQuickStatus`
- 展示当前行数、当前图解、同步状态（可来自 `overview/progress`）

### 验收点
- 能完成项目创建与图解新增两种来源
- 能手动切换当前图解
- 删除当前图解后可正确显示后端返回的新当前图解

## 4.2 工作台页（/workbench/:projectId）
### 页面目标
- 左侧阅读器 + 右侧计数/计时控制台
- 支持聚焦遮罩、计数、计时与自动同步

### 组件与任务
- `PatternViewport`
- 渲染 PDF/图片（文件流地址来自 `GET /files/{fileId}/content`）
- 支持缩放、滚动、翻页（至少保留页码状态）
- `FocusMaskOverlay`
- 蒙层透明孔效果
- `ArrowUp/ArrowDown` 调整 `maskTopOffset`
- 视窗高度可调（拖拽或滑杆）
- 蒙层 `pointer-events: none`
- `CounterPanel`
- 当前行/圈显示
- 大热区点击 + `Space` 触发 `totalCount +1`
- `currentRowIndex` 调整能力（加减或输入）
- `TimerPanel`
- 手动模式：点击开始/暂停
- 动作唤醒模式：首次计数自动开始
- 累计时长显示 `HH:mm:ss`
- `WorkbenchSideInfo`
- 当前项目信息、当前图解信息、保存状态提示
- 手动“保存当前进度”触发一次全量同步（可选）

### 数据与同步任务
- 首屏恢复：`GET /projects/{projectId}/overview`
- 阅读配置保存：`PUT /pattern-config`（防抖 1000ms）
- 计数保存：`PUT /progress/row`（防抖 1500ms）
- 计时保存：`PUT /progress/time`（每 3000ms）
- 离开页面前触发一次 `flush` 同步

### 验收点
- 空格计数不触发页面滚动
- 动作唤醒模式下首次计数自动启动计时
- 连续操作无接口轰炸（符合防抖/轮询要求）
- 刷新后恢复页码、遮罩、计数、计时、当前图解

## 4.3 设置页（/settings）
### 页面目标
- 配置工作台行为偏好与聚焦参数
- 提供同步状态感知

### 组件与任务
- `WorkbenchSettingsCard`
- 动作唤醒模式开关
- 音效开关
- 云同步状态展示与“立即同步”
- `FocusSettingsCard`
- 步进大小配置（影响 `ArrowUp/ArrowDown` 单次位移）
- 遮罩透明度配置（影响 UI 展示）
- `PreviewCard`
- 遮罩效果实时预览

### 验收点
- 设置变更可立即反映到工作台行为
- 设置在刷新后保持（本地存储或配置接口，MVP 可先本地）

## 4.4 数据统计页（/stats）
### 页面目标
- MVP 阶段提供占位与基础结构，避免导航死链

### 组件与任务
- `StatsPlaceholder`
- 显示“功能建设中”
- 预留后续图表容器结构

## 5. 关键技术任务（跨页面）

## 5.1 键盘事件隔离
- 输入控件聚焦时禁用 `Space/ArrowUp/ArrowDown`
- 非编辑区恢复快捷键监听

## 5.2 音效系统
- 计数成功播放短音效
- 处理浏览器自动播放限制（首次用户交互后启用）

## 5.3 错误处理映射
- 项目不存在、图解不存在、文件不存在、重复上传、非法链接等错误码映射为可读文案
- 文件失效时在工作台给出可操作提示（切换图解/重新上传）

## 5.4 响应式适配
- 桌面优先（与设计图一致）
- 窄屏下右侧面板折叠为抽屉或底部面板

## 6. 交付顺序建议

### 里程碑 1：基础打通
- 全局骨架、路由、API 层、项目创建、图解上传/链接、图解列表

### 里程碑 2：工作台核心
- 阅读器、聚焦遮罩、计数器、计时器、快捷键、防抖同步

### 里程碑 3：设置与稳定性
- 设置页联动、错误处理完善、离开页 flush、空态与异常态补齐

### 里程碑 4：验收回归
- 按 PRD 验收清单逐条回归
- 性能与网络调用频率验证
