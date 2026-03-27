# 织织乐后端技术方案

## 1. 文档目标

本文档基于 [spec.md](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/specs/spec.md) 进一步整理当前阶段后端落地方案，覆盖以下内容：

- 目录结构
- 核心数据模型
- 接口定义
- 实施阶段

本文档用于指导后端开发、联调和阶段性交付。

## 2. 方案概览

本期后端以“项目”为核心组织单元，围绕以下四类能力展开：

- 项目管理
- 图解管理
- 阅读配置管理
- 进度同步与恢复

图解资源支持两种来源：

- 本地文件上传
- 手动录入外部图解链接

系统目标是在页面刷新或重新进入时，恢复用户最近一次的项目状态，包括当前图解、阅读配置、计数和计时结果。

## 3. 推荐目录结构

结合当前仓库现状，建议按业务模块和分层职责组织代码。

```text
zhizhile-backend/
├── specs/
│   ├── spec.md
│   └── plan.md
├── src/
│   ├── main/
│   │   ├── java/com/zzl/zhizhile/
│   │   │   ├── ZhizhileBackendApplication.java
│   │   │   ├── common/
│   │   │   │   ├── api/
│   │   │   │   ├── config/
│   │   │   │   ├── constant/
│   │   │   │   ├── exception/
│   │   │   │   └── util/
│   │   │   ├── project/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/
│   │   │   │   │   ├── dto/
│   │   │   │   │   └── vo/
│   │   │   │   └── mapper/
│   │   │   ├── pattern/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/
│   │   │   │   │   ├── dto/
│   │   │   │   │   └── vo/
│   │   │   │   └── mapper/
│   │   │   ├── file/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/
│   │   │   │   │   ├── dto/
│   │   │   │   │   └── vo/
│   │   │   │   └── mapper/
│   │   │   ├── progress/
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── model/
│   │   │   │   │   ├── entity/
│   │   │   │   │   ├── dto/
│   │   │   │   │   └── vo/
│   │   │   │   └── mapper/
│   │   │   └── configstate/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── model/
│   │   │       │   ├── entity/
│   │   │       │   ├── dto/
│   │   │       │   └── vo/
│   │   │       └── mapper/
│   │   └── resources/
│   │       ├── application.yml
│   │       └── mapper/
│   └── test/
│       └── java/com/zzl/zhizhile/
```

## 4. 分层职责

### 4.1 `controller`

负责对外暴露接口，处理请求接入、参数校验结果返回和响应包装。

### 4.2 `service`

负责业务编排，处理项目、图解、文件、阅读配置和进度之间的业务关系。

### 4.3 `mapper`

负责数据持久化访问。

### 4.4 `entity`

对应核心数据对象，用于表达持久化结构。

### 4.5 `dto`

用于承载请求参数和服务层输入输出。

### 4.6 `vo`

用于接口响应结构。

### 4.7 `common`

用于承载全局通用能力，例如统一返回体、异常规范、配置项、常量和工具类。

## 5. 核心数据模型

本期建议围绕 5 个核心数据对象组织业务。

## 5.1 Project

表示用户创建的一项手作项目，是所有后续数据的归属容器。

核心字段建议：

- `id`
- `userId`
- `name`
- `status`
- `currentPatternId`
- `createTime`
- `updateTime`

模型职责：

- 表示项目主信息
- 记录当前使用图解
- 作为阅读配置、计数和计时的归属主对象

## 5.2 ProjectPattern

表示项目下的一份图解资源，统一承载上传文件和外部链接两种来源。

核心字段建议：

- `id`
- `projectId`
- `sourceType`
- `displayName`
- `fileId`
- `externalUrl`
- `createTime`
- `updateTime`

模型职责：

- 表示项目下的图解列表
- 支持用户手动切换当前图解
- 支持删除图解

业务规则：

- `sourceType = UPLOAD` 时，关联文件资源
- `sourceType = LINK` 时，保存外部链接

## 5.3 FileResource

表示本地上传后的文件资源信息。

核心字段建议：

- `id`
- `originalName`
- `storedName`
- `fileExt`
- `contentType`
- `fileSize`
- `storageType`
- `relativePath`
- `createTime`
- `updateTime`

模型职责：

- 表示上传后的真实文件
- 为统一文件访问提供基础信息
- 为未来扩展其他存储方式预留能力

## 5.4 PatternConfig

表示某个项目中某份图解的阅读配置。

核心字段建议：

- `id`
- `projectId`
- `patternId`
- `currentPage`
- `maskTopOffset`
- `maskHeight`
- `updateTime`

模型职责：

- 保存图解阅读位置
- 保存遮罩位置和高度
- 在多图解场景下隔离不同图解的阅读状态

## 5.5 ProjectProgress

表示某个项目当前的计数和计时汇总状态。

核心字段建议：

- `id`
- `projectId`
- `currentRowIndex`
- `totalCount`
- `totalSeconds`
- `updateTime`

模型职责：

- 保存当前行数或圈数
- 保存累计计数
- 保存累计时长

业务规则：

- 前端每次上报最新值
- 后端直接覆盖保存
- 冲突按最后一次写入生效

## 6. 核心业务流程

## 6.1 项目创建流程

1. 用户输入项目名称
2. 后端创建项目
3. 返回项目基本信息

## 6.2 上传图解流程

1. 用户在指定项目下选择本地文件
2. 后端校验上传内容
3. 保存文件信息
4. 建立项目与图解关系
5. 返回图解信息

## 6.3 添加图解链接流程

1. 用户在指定项目下输入图解链接
2. 后端校验链接格式
3. 建立项目与图解关系
4. 返回图解信息

## 6.4 切换当前图解流程

1. 用户在项目图解列表中选择一份图解
2. 后端更新项目当前图解
3. 返回新的当前图解标识

## 6.5 删除图解流程

1. 用户删除项目中的某份图解
2. 后端移除图解关系
3. 如果被删除的是当前图解，则自动切换到最近添加的一份图解
4. 若项目下无剩余图解，则当前图解为空

## 6.6 页面恢复流程

1. 用户重新进入项目
2. 后端返回项目基础信息
3. 后端返回当前图解和图解列表
4. 后端返回最近一次阅读配置
5. 后端返回最近一次计数和计时结果

## 7. 接口定义

以下接口定义用于指导前后端联调和服务拆分。

## 7.1 项目接口

### `POST /api/v1/projects`

用途：

- 创建项目

请求字段：

- `name`

返回字段：

- `projectId`
- `name`
- `status`
- `currentPatternId`
- `createTime`
- `updateTime`

### `GET /api/v1/projects/{projectId}`

用途：

- 查询项目详情

返回字段：

- `projectId`
- `name`
- `status`
- `currentPatternId`
- `createTime`
- `updateTime`

### `GET /api/v1/projects/{projectId}/overview`

用途：

- 恢复项目页面状态

返回内容：

- 项目基本信息
- 当前图解
- 图解列表
- 阅读配置
- 进度信息

## 7.2 图解接口

### `POST /api/v1/projects/{projectId}/patterns/upload`

用途：

- 在项目下上传图解文件

请求内容：

- 上传文件
- 可选显示名称

返回内容：

- `patternId`
- `projectId`
- `sourceType`
- `displayName`
- `fileId`
- `isCurrent`
- `createTime`

### `POST /api/v1/projects/{projectId}/patterns/link`

用途：

- 在项目下添加图解链接

请求字段：

- `displayName`
- `externalUrl`

返回内容：

- `patternId`
- `projectId`
- `sourceType`
- `displayName`
- `externalUrl`
- `isCurrent`
- `createTime`

### `GET /api/v1/projects/{projectId}/patterns`

用途：

- 查询项目下图解列表

返回内容：

- 图解列表
- 每项包含来源类型、显示名称、当前状态和创建时间

### `PUT /api/v1/projects/{projectId}/patterns/{patternId}/current`

用途：

- 手动设置当前图解

返回内容：

- `projectId`
- `currentPatternId`
- `updateTime`

### `DELETE /api/v1/projects/{projectId}/patterns/{patternId}`

用途：

- 删除项目中的图解

返回内容：

- `deletedPatternId`
- `nextCurrentPatternId`
- `updateTime`

## 7.3 文件接口

### `GET /api/v1/files/{fileId}/content`

用途：

- 获取文件内容
- 支持图片与 PDF 的统一访问

返回内容：

- 文件内容本身

异常场景：

- 文件不存在时返回明确错误结果

## 7.4 阅读配置接口

### `GET /api/v1/projects/{projectId}/pattern-config`

用途：

- 查询某个项目当前图解或指定图解的阅读配置

返回内容：

- `projectId`
- `patternId`
- `currentPage`
- `maskTopOffset`
- `maskHeight`
- `updateTime`

### `PUT /api/v1/projects/{projectId}/pattern-config`

用途：

- 保存阅读配置

请求字段：

- `patternId`
- `currentPage`
- `maskTopOffset`
- `maskHeight`

返回内容：

- 最新保存结果

## 7.5 进度接口

### `PUT /api/v1/projects/{projectId}/progress/row`

用途：

- 保存当前行数和累计计数

请求字段：

- `currentRowIndex`
- `totalCount`

返回内容：

- `projectId`
- `currentRowIndex`
- `totalCount`
- `updateTime`

### `PUT /api/v1/projects/{projectId}/progress/time`

用途：

- 保存累计时长

请求字段：

- `totalSeconds`

返回内容：

- `projectId`
- `totalSeconds`
- `updateTime`

### `GET /api/v1/projects/{projectId}/progress`

用途：

- 查询项目当前进度

返回内容：

- `projectId`
- `currentRowIndex`
- `totalCount`
- `totalSeconds`
- `updateTime`

## 8. 统一返回与异常口径

建议所有接口保持统一响应结构，便于前端判断成功与失败。

统一需要覆盖的异常场景：

- 项目不存在
- 图解不存在
- 文件不存在
- 上传内容不符合要求
- 上传文件重复
- 链接不合法
- 当前图解切换失败

异常返回原则：

- 能明确说明失败原因
- 能让前端直接映射为用户提示
- 不使用模糊的空结果代替错误信息

## 9. 实施阶段

建议按四个阶段推进，优先保证主链路可跑通，再补齐恢复能力和异常规范。

## 阶段一：基础骨架与项目能力

目标：

- 完成基础项目结构搭建
- 完成项目创建和项目详情查询
- 统一接口响应与异常风格

交付内容：

- 应用启动骨架
- 项目模块基础分层
- 项目创建接口
- 项目详情接口

## 阶段二：图解管理能力

目标：

- 跑通图解主链路

交付内容：

- 上传图解接口
- 添加图解链接接口
- 图解列表接口
- 设置当前图解接口
- 删除图解接口
- 文件访问接口

## 阶段三：阅读配置与进度同步

目标：

- 跑通阅读状态保存与恢复

交付内容：

- 阅读配置查询接口
- 阅读配置保存接口
- 行数与计数同步接口
- 计时同步接口
- 项目总览恢复接口

## 阶段四：稳定性与联调完善

目标：

- 提升联调效率和交付质量

交付内容：

- 统一异常规范补齐
- 关键场景测试补齐
- 边界条件校验补齐
- 联调文档补齐

## 10. 测试重点

### 10.1 主流程验证

- 创建项目成功
- 上传图解成功
- 添加图解链接成功
- 手动切换当前图解成功
- 删除当前图解后自动切换成功
- 页面刷新后能恢复核心状态

### 10.2 边界场景验证

- 项目名称为空
- 链接格式非法
- 重复上传同一文件
- 图解不存在
- 文件不存在
- 项目下最后一份图解被删除

## 11. 风险与关注点

### 11.1 多图解状态隔离

如果阅读配置未与具体图解关联，多图解切换时容易互相覆盖。

### 11.2 删除后的当前图解切换规则

删除当前图解后需要稳定执行自动切换规则，否则前端恢复状态会出现空洞。

### 11.3 外部链接不可控

链接型图解天然存在失效风险，需要在产品和联调中明确为当前阶段已接受限制。

### 11.4 用户体系后续接入

虽然本期不实现正式登录，但需要为后续账号体系保留清晰的归属关系。

## 12. 结论

本方案建议以后端业务模块化方式组织代码，以项目、图解、文件、阅读配置和进度为核心模型，优先打通以下主链路：

- 创建项目
- 添加图解
- 手动切换当前图解
- 保存阅读与进度状态
- 刷新后恢复状态

这样可以在当前阶段尽快完成 MVP 闭环，同时为后续用户体系、存储扩展和功能增强预留清晰边界。
