# 织织乐前端开发任务拆解（MVP）

## 1. 文档目标
基于以下输入文档与原型，沉淀可执行的前端开发任务拆解（按页面/组件维度）：
- `prds/pic-upload-prd.md`
- `zhizhile-backend/specs/spec.md`
- `zhizhile-backend/specs/plan.md`
- `yuanxin_pics/图解库.png`
- `yuanxin_pics/工作台.png`
- `yuanxin_pics/设置.png`

## 2. 范围定义

### 2.1 本期包含
- 图解库页（项目与图解管理）
- 工作台页（图解阅读 + 聚焦遮罩 + 计数/计时）
- 设置页（工作台行为参数）
- 全局框架（侧边导航、顶部栏、状态反馈、弹层）
- 前后端联调（项目、图解、阅读配置、进度同步）

### 2.2 本期不包含
- 统计看板页（`统计看板.png`）

## 3. 页面级任务拆解

## 3.1 图解库页（`/library`）

### 页面目标
- 以“项目”为单位管理图解资源
- 支持上传图解、添加图解链接、切换当前图解、删除图解
- 从图解库进入工作台

### 页面模块
- 顶部搜索与筛选区
- 项目卡片列表区
- 新建项目入口
- 图解管理弹层/抽屉

### 页面任务
- 接入项目创建：`POST /api/v1/projects`
- 接入项目列表（可由后端详情/概览组合实现）
- 接入上传图解：`POST /api/v1/projects/{projectId}/patterns/upload`
- 接入添加链接：`POST /api/v1/projects/{projectId}/patterns/link`
- 接入图解列表：`GET /api/v1/projects/{projectId}/patterns`
- 接入切换当前图解：`PUT /api/v1/projects/{projectId}/patterns/{patternId}/current`
- 接入删除图解：`DELETE /api/v1/projects/{projectId}/patterns/{patternId}`
- 删除当前图解后，按后端返回刷新当前图解状态
- 点击项目卡片进入工作台：`/workbench/:projectId`

### 验收标准
- 可创建项目且项目名必填
- 支持上传图解与链接图解两种来源
- 同项目可管理多图解并可手动切换当前图解
- 删除当前图解后 UI 正确反映后端自动切换结果

## 3.2 工作台页（`/workbench/:projectId`）

### 页面目标
- 左侧图解阅读器 + 右侧计数计时控制台
- 支持阅读状态、计数与计时的保存恢复

### 页面模块
- 图解阅读区（PDF/图片）
- 聚焦遮罩层
- 当前行数/计数器
- 计时器
- 项目与图解信息侧栏
- 保存状态提示

### 页面任务
- 首屏恢复：`GET /api/v1/projects/{projectId}/overview`
- 兜底读取阅读配置：`GET /api/v1/projects/{projectId}/pattern-config`
- 渲染当前图解（文件源通过 `GET /api/v1/files/{fileId}/content`）
- 支持 PDF/图片缩放与滚动阅读
- 实现蒙层 + 镂空聚焦窗（`pointer-events: none`）
- 快捷键 `ArrowUp/ArrowDown` 调整 `maskTopOffset`
- 支持调节聚焦窗高度 `maskHeight`
- 保存阅读配置：`PUT /api/v1/projects/{projectId}/pattern-config`
- 空格键与大热区触发计数 +1（拦截默认滚动）
- 保存计数进度：`PUT /api/v1/projects/{projectId}/progress/row`
- 支持计时器手动模式与动作唤醒模式
- 保存计时进度：`PUT /api/v1/projects/{projectId}/progress/time`
- 离开页面前执行一次进度 flush

### 同步策略
- 阅读配置：停止操作后 1000ms 防抖提交
- 计数：停止点击后 1500ms 防抖提交
- 计时：运行中每 3000ms 周期同步
- 快捷键隔离：输入控件聚焦时禁用 `Space/ArrowUp/ArrowDown`

### 验收标准
- 可正常加载并阅读当前图解（PDF/图片）
- 聚焦遮罩可键盘移动且可调高度
- 空格计数不触发页面滚动
- 动作唤醒模式下首次计数可自动启动计时
- 连续操作无高频接口轰炸（防抖生效）
- 刷新后恢复当前图解、页码/遮罩、计数、计时

## 3.3 设置页（`/settings`）

### 页面目标
- 管理工作台交互偏好
- 提供聚焦阅读参数与同步控制

### 页面模块
- 工作台控制卡
- 聚焦视窗参数卡
- 实时预览卡

### 页面任务
- 动作唤醒模式开关（影响工作台计时启动机制）
- 音效开关（影响计数反馈音）
- 步进大小配置（影响 `ArrowUp/ArrowDown` 位移）
- 遮罩透明度配置（影响聚焦层显示）
- 云同步状态展示与“立即同步”入口
- 设置持久化（MVP 可本地存储）并在工作台即时生效

### 验收标准
- 设置变更可即时影响工作台行为
- 刷新后设置可恢复

## 4. 组件级任务拆解

## 4.1 全局组件
- `AppShell`：左侧导航 + 顶部栏 + 主内容容器
- `SideNav`：图解库/工作台/设置导航高亮与跳转
- `TopBar`：搜索入口、状态图标、用户占位
- `GlobalToast`：成功/失败反馈
- `ConfirmDialog`：删除图解二次确认
- `EmptyState`：无项目/无图解/无当前图解场景
- `ErrorState`：资源失效、接口异常场景

## 4.2 图解库页组件
- `ProjectSearchBar`：关键字过滤（前端过滤）
- `CreateProjectButton` + `CreateProjectModal`：新建项目
- `ProjectCardGrid`：项目列表容器
- `ProjectCard`：项目封面、标题、更新时间、入口按钮
- `PatternManagerDrawer`：图解上传、链接添加、图解列表管理
- `UploadPatternPanel`：文件上传表单与结果反馈
- `LinkPatternPanel`：链接校验与提交
- `PatternList`：当前图解标识、切换、删除

## 4.3 工作台页组件
- `WorkbenchLayout`：左读区右控区布局
- `PatternReaderContainer`：阅读器状态协调
- `PatternViewport`：PDF/图片渲染、缩放、翻页
- `FocusMaskOverlay`：半透明蒙层与镂空视窗
- `MaskHeightControl`：视窗高度调节
- `ReaderHotkeyController`：键盘事件绑定与隔离
- `CounterCard`：当前行数/累计计数/大热区点击
- `TimerCard`：计时显示、开始/暂停、模式状态
- `ProgressSaveIndicator`：保存中/已保存/失败提示
- `PatternMetaCard`：当前项目与图解信息展示

## 4.4 设置页组件
- `WorkbenchControlCard`：动作唤醒、音效、同步入口
- `FocusWindowSettingsCard`：步进大小、透明度
- `SettingsPreviewCard`：阅读效果预览

## 5. 状态管理与数据模型任务

### 5.1 全局状态域
- `projectState`：当前项目、项目基础信息
- `patternState`：图解列表、当前图解、图解来源
- `readerState`：页码、缩放、遮罩偏移、遮罩高度
- `progressState`：当前行数、累计计数、累计时长
- `settingsState`：动作唤醒、音效、步进、透明度

### 5.2 关键字段约定
- `currentPatternId`
- `currentPage`
- `maskTopOffset`
- `maskHeight`
- `currentRowIndex`
- `totalCount`
- `totalSeconds`

## 6. 接口联调任务

### 6.1 项目与图解
- 创建项目、查询项目详情/概览
- 上传图解、添加链接、查询图解列表
- 切换当前图解、删除图解

### 6.2 阅读配置
- 查询配置：`GET /projects/{projectId}/pattern-config`
- 保存配置：`PUT /projects/{projectId}/pattern-config`

### 6.3 进度同步
- 保存计数：`PUT /projects/{projectId}/progress/row`
- 保存计时：`PUT /projects/{projectId}/progress/time`
- 查询进度：`GET /projects/{projectId}/progress`

### 6.4 文件访问
- 文件流读取：`GET /files/{fileId}/content`

## 7. 异常与边界任务
- 项目不存在：提示并返回图解库
- 图解不存在：提示并引导重新选择
- 文件不存在/损坏：提示“文件失效”
- 重复上传：显示后端明确提示
- 链接不合法：前置校验 + 后端错误透传
- 外链不可打开：展示可操作错误提示，不静默失败

## 8. 开发排期建议（不含统计看板）

### 里程碑 1：基础框架与图解库打通
- AppShell + 路由 + 项目/图解管理接口 + 图解库页核心流程

### 里程碑 2：工作台核心阅读与同步
- 阅读器渲染 + 聚焦遮罩 + 快捷键 + 防抖同步 + 刷新恢复

### 里程碑 3：计数计时与设置联动
- 计数/计时完整流程 + 设置页参数生效 + 反馈与异常完善

### 里程碑 4：联调与验收
- 按 PRD 验收清单逐项回归（尤其防抖与恢复）
