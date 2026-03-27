# 织织乐前端原子任务清单（MVP）

## 1. 文档目标

本文档基于 `zhizhile-frontend/specs/spec.md` 拆分前端开发任务，形成可执行、可分配、可验收的任务列表。

拆分规则：
- 每个任务聚焦一个明确产出
- 任务按主链路推进：先框架，再图解库，再工作台，再设置，再联调
- 每阶段结束可独立演示

## 2. 执行约束

### 2.1 范围约束
- 本期只覆盖：图解库、工作台、设置、全局框架、接口联调
- 本期不实现：统计看板页

### 2.2 质量约束
- 所有接口调用必须有错误反馈
- 快捷键必须支持输入态隔离
- 同步必须满足防抖与轮询约束
- 页面刷新后必须可恢复关键状态

### 2.3 交付约束
- 每个任务需附带“完成定义（DoD）”
- 阶段末执行一次回归验证

## 3. 任务阶段总览

### 阶段一
前端基础框架与工程能力

### 阶段二
图解库页（项目与图解管理）

### 阶段三
工作台页（阅读器 + 计数计时）

### 阶段四
设置页与全局交互联动

### 阶段五
联调验收与稳定性补齐

## 4. 原子任务清单

## 阶段一：前端基础框架与工程能力

### F001
文件：`src/main.ts`

任务：
- 接入全局路由与全局样式初始化入口

DoD：
- 应用启动后可正常挂载路由页面

### F002
文件：`src/App.vue`

任务：
- 落地 `AppShell` 页面骨架容器
- 预留侧边导航、顶部栏、内容区插槽

DoD：
- 三段式布局可渲染并承载子页面

### F003
文件：`src/router/index.ts`

任务：
- 新建路由：`/library`、`/workbench/:projectId`、`/settings`
- 设置默认重定向到 `/library`

DoD：
- 三个页面可通过路由访问

### F004
文件：`src/api/http.ts`

任务：
- 封装统一请求客户端
- 提供基础错误处理与响应解包

DoD：
- 页面可统一通过该客户端发起请求

### F005
文件：`src/types/api.ts`

任务：
- 定义项目、图解、阅读配置、进度数据类型

DoD：
- 页面与接口层共享统一类型

### F006
文件：`src/stores/project.ts`

任务：
- 建立项目与图解基础状态域（当前项目、图解列表、当前图解）

DoD：
- 页面可读取并更新项目/图解核心状态

### F007
文件：`src/components/common/GlobalToast.vue`

任务：
- 提供全局成功/失败提示组件

DoD：
- 任意页面可触发统一消息提示

## 阶段二：图解库页（项目与图解管理）

### F008
文件：`src/views/LibraryView.vue`

任务：
- 搭建图解库页主布局与区块（搜索区、卡片区、新建入口）

DoD：
- 页面结构与原型一致，可显示占位数据

### F009
文件：`src/api/project.ts`

任务：
- 实现项目相关 API：创建项目、项目详情、项目概览

DoD：
- 页面可调用项目接口获取与创建项目

### F010
文件：`src/components/library/CreateProjectModal.vue`

任务：
- 实现新建项目弹窗（名称必填校验）

DoD：
- 可提交创建项目并反馈成功/失败

### F011
文件：`src/components/library/ProjectCardGrid.vue`

任务：
- 实现项目卡片列表与空态展示

DoD：
- 可渲染项目列表，无数据时显示空态

### F012
文件：`src/api/pattern.ts`

任务：
- 实现图解 API：上传、添加链接、列表查询、切换当前、删除

DoD：
- 图解管理操作具备完整接口调用能力

### F013
文件：`src/components/library/PatternManagerDrawer.vue`

任务：
- 实现图解管理抽屉容器
- 集成上传、链接、列表切换、删除入口

DoD：
- 在一个容器内完成图解管理全流程

### F014
文件：`src/components/library/UploadPatternPanel.vue`

任务：
- 实现图解文件上传表单与结果反馈

DoD：
- 可上传图解并刷新图解列表

### F015
文件：`src/components/library/LinkPatternPanel.vue`

任务：
- 实现图解链接录入与前置 URL 校验

DoD：
- 非法链接阻断提交，合法链接可创建成功

### F016
文件：`src/components/library/PatternList.vue`

任务：
- 实现图解列表渲染
- 支持设为当前图解与删除图解

DoD：
- 切换当前与删除操作可正确回写 UI

### F017
文件：`src/components/common/ConfirmDialog.vue`

任务：
- 实现通用二次确认弹窗（用于删除图解）

DoD：
- 删除操作必须经确认后执行

## 阶段三：工作台页（阅读器 + 计数计时）

### F018
文件：`src/views/WorkbenchView.vue`

任务：
- 搭建工作台页布局（左阅读区 + 右控制区）
- 接入 `overview` 首屏恢复流程

DoD：
- 进入页面可读取并展示项目总览状态

### F019
文件：`src/api/file.ts`

任务：
- 实现文件访问 API（`/files/{fileId}/content`）

DoD：
- 可生成阅读器可用的文件资源地址

### F020
文件：`src/stores/reader.ts`

任务：
- 建立阅读状态域：页码、缩放、遮罩偏移、遮罩高度

DoD：
- 阅读器状态可集中管理并被组件共享

### F021
文件：`src/components/workbench/PatternViewport.vue`

任务：
- 实现 PDF/图片图解渲染
- 支持滚动、缩放、页码变化抛出

DoD：
- 当前图解可稳定阅读并可变更页码

### F022
文件：`src/components/workbench/FocusMaskOverlay.vue`

任务：
- 实现半透明蒙层与镂空视窗
- 确保 `pointer-events: none`

DoD：
- 遮罩视觉正确且不阻断底层交互

### F023
文件：`src/components/workbench/ReaderHotkeyController.ts`

任务：
- 实现 `ArrowUp/ArrowDown/Space` 快捷键监听
- 输入控件聚焦时禁用快捷键

DoD：
- 快捷键行为正确且不干扰输入

### F024
文件：`src/components/workbench/MaskHeightControl.vue`

任务：
- 实现遮罩高度调节控件

DoD：
- 可调节 `maskHeight` 并实时生效

### F025
文件：`src/api/patternConfig.ts`

任务：
- 实现阅读配置查询与保存 API

DoD：
- 支持读取与提交 `currentPage/maskTopOffset/maskHeight`

### F026
文件：`src/utils/debounceSync.ts`

任务：
- 封装阅读配置与计数的防抖同步工具（1000ms/1500ms）

DoD：
- 高频操作不会产生高频请求

### F027
文件：`src/api/progress.ts`

任务：
- 实现进度 API：保存计数、保存计时、查询进度

DoD：
- 计数与计时可独立同步与查询

### F028
文件：`src/components/workbench/CounterCard.vue`

任务：
- 实现当前行数与累计计数展示
- 支持空格键与大热区点击计数 +1

DoD：
- 计数行为可用且不会触发页面默认滚动

### F029
文件：`src/components/workbench/TimerCard.vue`

任务：
- 实现计时器展示与开始/暂停
- 支持手动模式与动作唤醒模式

DoD：
- 两种模式下计时行为符合 PRD 规则

### F030
文件：`src/components/workbench/ProgressSaveIndicator.vue`

任务：
- 实现保存状态提示（保存中/已保存/失败）

DoD：
- 用户可感知同步状态与失败结果

## 阶段四：设置页与全局交互联动

### F031
文件：`src/views/SettingsView.vue`

任务：
- 搭建设置页主布局与三块卡片容器

DoD：
- 页面结构与原型一致

### F032
文件：`src/stores/settings.ts`

任务：
- 建立设置状态域：动作唤醒、音效、步进、透明度

DoD：
- 设置项可被设置页和工作台共享

### F033
文件：`src/components/settings/WorkbenchControlCard.vue`

任务：
- 实现动作唤醒、音效开关、立即同步入口

DoD：
- 开关状态变更可写入 settings 状态域

### F034
文件：`src/components/settings/FocusWindowSettingsCard.vue`

任务：
- 实现步进大小与遮罩透明度配置

DoD：
- 配置变更可实时影响工作台遮罩表现

### F035
文件：`src/components/settings/SettingsPreviewCard.vue`

任务：
- 实现设置效果实时预览

DoD：
- 用户可在设置页看到聚焦效果反馈

### F036
文件：`src/utils/settingsPersist.ts`

任务：
- 实现设置本地持久化与恢复

DoD：
- 刷新页面后设置项可恢复

## 阶段五：联调验收与稳定性补齐

### F037
文件：`src/components/common/ErrorState.vue`

任务：
- 实现通用错误态组件（项目/图解/文件异常）

DoD：
- 异常场景可统一展示并提供可执行操作

### F038
文件：`src/components/common/EmptyState.vue`

任务：
- 实现通用空态组件（无项目、无图解、无当前图解）

DoD：
- 空场景反馈清晰、引导路径明确

### F039
文件：`src/views/WorkbenchView.vue`

任务：
- 补充页面卸载前 `flush` 机制（阅读配置/计数/计时）

DoD：
- 关闭或跳转页面前尽量完成一次最终同步

### F040
文件：`src/tests/e2e/workbench-sync.spec.ts`

任务：
- 新增核心链路联调用例（恢复、防抖、快捷键、动作唤醒）

DoD：
- 关键验收项有自动化覆盖（或可执行脚本）

## 5. 最小可上线任务集合（建议）
- 阶段一全部
- 阶段二全部
- 阶段三至少到 F030
- 阶段四至少完成 F032/F033/F034/F036
- 阶段五至少完成 F037/F038/F039

## 6. 里程碑验收点

### M1（框架与图解库）
- 可创建项目
- 可添加/切换/删除图解
- 可进入工作台

### M2（工作台核心）
- 可阅读图解并操作聚焦遮罩
- 可计数与计时
- 同步策略符合防抖/轮询要求

### M3（设置与稳定）
- 设置项可即时生效并持久化
- 异常与空态提示完整
- 刷新恢复与离页 flush 可用
