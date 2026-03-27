# 织织乐后端原子任务清单

## 1. 文档目标

本文档基于 [spec.md](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/specs/spec.md) 和 [plan.md](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/specs/plan.md) 拆分后端开发任务，目标是形成一份可直接执行的原子任务列表。

拆分规则如下：

- 每个任务只允许修改一个文件
- 采用测试先行
- 先补测试，再补实现
- 任务顺序尽量保证主链路可持续推进

## 2. 执行约束

### 2.1 单任务约束

- 一个任务只能改一个文件
- 如果一个功能需要同时改测试和实现，则必须拆成两个连续任务
- 如果一个功能涉及多个实现文件，则继续拆小，直到每个任务只落在一个文件上

### 2.2 测试先行约束

每个功能按以下顺序推进：

1. 先新增或修改测试文件
2. 再新增或修改对应实现文件
3. 保持每一步都能解释清楚它验证的业务规则

### 2.3 任务组织原则

- 先搭公共骨架，再做项目主链路
- 先做项目与图解，再做配置和进度同步
- 先完成查询与保存，再做异常与边界
- 测试文件优先与实现文件成对出现

## 3. 任务阶段总览

### 阶段一

基础骨架与统一规范

### 阶段二

项目能力

### 阶段三

图解管理能力

### 阶段四

阅读配置与进度同步

### 阶段五

项目总览恢复与异常补齐

## 4. 原子任务清单

## 阶段一：基础骨架与统一规范

### T001

文件：[application.yml](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/resources/application.yml)

任务：

- 补充项目基础配置占位
- 补充上传目录、文件大小限制、测试用户等配置项占位

产出目标：

- 为后续模块实现提供统一配置入口

### T002

文件：[ZhizhileBackendApplication.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/ZhizhileBackendApplication.java)

任务：

- 新增 Spring Boot 启动类

产出目标：

- 项目具备应用启动入口

### T003

文件：[ApiResponse.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/common/api/ApiResponse.java)

任务：

- 新增统一响应结构

产出目标：

- 所有接口具备统一返回格式

### T004

文件：[ErrorCode.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/common/constant/ErrorCode.java)

任务：

- 新增错误码枚举或常量定义

产出目标：

- 项目、图解、文件、参数等异常场景具备统一错误标识

### T005

文件：[BizException.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/common/exception/BizException.java)

任务：

- 新增业务异常类

产出目标：

- 业务失败可统一抛出和承接

### T006

文件：[GlobalExceptionHandlerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/common/GlobalExceptionHandlerTest.java)

任务：

- 先写全局异常处理测试
- 验证业务异常和通用异常能输出统一响应格式

产出目标：

- 明确异常处理对外行为

### T007

文件：[GlobalExceptionHandler.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/common/exception/GlobalExceptionHandler.java)

任务：

- 新增全局异常处理器实现

产出目标：

- 满足 T006 中定义的接口异常响应规则

## 阶段二：项目能力

### T008

文件：[ProjectEntity.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/model/entity/ProjectEntity.java)

任务：

- 新增项目实体定义

产出目标：

- 表达项目主信息和当前图解关系

### T009

文件：[CreateProjectRequest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/model/dto/CreateProjectRequest.java)

任务：

- 新增创建项目请求模型

产出目标：

- 承接创建项目接口请求字段

### T010

文件：[ProjectDetailVO.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/model/vo/ProjectDetailVO.java)

任务：

- 新增项目详情响应模型

产出目标：

- 统一项目创建和查询返回结构

### T011

文件：[ProjectMapper.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/mapper/ProjectMapper.java)

任务：

- 新增项目数据访问接口

产出目标：

- 提供项目新增和查询能力入口

### T012

文件：[ProjectServiceTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/project/service/ProjectServiceTest.java)

任务：

- 先写项目服务测试
- 覆盖创建项目成功
- 覆盖项目名称为空失败
- 覆盖查询不存在项目失败

产出目标：

- 锁定项目服务核心业务规则

### T013

文件：[ProjectService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/service/ProjectService.java)

任务：

- 实现项目创建和项目详情查询服务

产出目标：

- 满足 T012 中定义的业务行为

### T014

文件：[ProjectControllerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/project/controller/ProjectControllerTest.java)

任务：

- 先写项目控制层测试
- 覆盖创建项目接口
- 覆盖项目详情接口

产出目标：

- 锁定接口输入输出和状态码行为

### T015

文件：[ProjectController.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/controller/ProjectController.java)

任务：

- 实现项目创建接口
- 实现项目详情接口

产出目标：

- 满足 T014 中定义的接口行为

## 阶段三：图解管理能力

### T016

文件：[ProjectPatternEntity.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/model/entity/ProjectPatternEntity.java)

任务：

- 新增图解实体定义

产出目标：

- 表达上传图解和链接图解的统一模型

### T017

文件：[FileResourceEntity.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/file/model/entity/FileResourceEntity.java)

任务：

- 新增文件资源实体定义

产出目标：

- 表达上传文件的核心属性

### T018

文件：[CreatePatternLinkRequest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/model/dto/CreatePatternLinkRequest.java)

任务：

- 新增图解链接创建请求模型

产出目标：

- 承接外部链接录入请求

### T019

文件：[PatternVO.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/model/vo/PatternVO.java)

任务：

- 新增图解响应模型

产出目标：

- 统一图解列表、创建、切换、删除结果结构

### T020

文件：[ProjectPatternMapper.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/mapper/ProjectPatternMapper.java)

任务：

- 新增图解数据访问接口

产出目标：

- 提供图解新增、查询、删除和当前图解相关访问能力

### T021

文件：[FileResourceMapper.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/file/mapper/FileResourceMapper.java)

任务：

- 新增文件资源数据访问接口

产出目标：

- 提供文件资源保存和查询能力

### T022

文件：[PatternServiceLinkTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/pattern/service/PatternServiceLinkTest.java)

任务：

- 先写图解链接服务测试
- 覆盖添加链接成功
- 覆盖非法链接失败
- 覆盖项目不存在失败

产出目标：

- 锁定链接图解服务规则

### T023

文件：[PatternService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/service/PatternService.java)

任务：

- 实现添加图解链接服务

产出目标：

- 满足 T022 中定义的业务行为

### T024

文件：[PatternServiceUploadTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/pattern/service/PatternServiceUploadTest.java)

任务：

- 先写上传图解服务测试
- 覆盖上传成功
- 覆盖重复上传失败
- 覆盖不支持的文件失败

产出目标：

- 锁定上传图解核心规则

### T025

文件：[FileStorageService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/file/service/FileStorageService.java)

任务：

- 新增文件存储服务接口

产出目标：

- 为上传流程提供统一文件保存入口

### T026

文件：[LocalFileStorageService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/file/service/LocalFileStorageService.java)

任务：

- 实现本地文件存储服务

产出目标：

- 满足上传图解服务对文件保存的依赖

### T027

文件：[PatternService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/service/PatternService.java)

任务：

- 在已有服务中补充上传图解能力

产出目标：

- 满足 T024 中定义的业务行为

### T028

文件：[PatternServiceSwitchTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/pattern/service/PatternServiceSwitchTest.java)

任务：

- 先写切换当前图解服务测试
- 覆盖切换成功
- 覆盖图解不属于项目失败

产出目标：

- 锁定当前图解切换规则

### T029

文件：[PatternService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/service/PatternService.java)

任务：

- 在已有服务中补充切换当前图解能力

产出目标：

- 满足 T028 中定义的业务行为

### T030

文件：[PatternServiceDeleteTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/pattern/service/PatternServiceDeleteTest.java)

任务：

- 先写删除图解服务测试
- 覆盖删除普通图解成功
- 覆盖删除当前图解后自动切换
- 覆盖删除最后一份图解后当前图解为空

产出目标：

- 锁定图解删除规则

### T031

文件：[PatternService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/service/PatternService.java)

任务：

- 在已有服务中补充删除图解能力

产出目标：

- 满足 T030 中定义的业务行为

### T032

文件：[PatternControllerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/pattern/controller/PatternControllerTest.java)

任务：

- 先写图解控制层测试
- 覆盖上传、链接创建、列表查询、设置当前图解、删除图解接口

产出目标：

- 锁定图解接口层对外行为

### T033

文件：[PatternController.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/controller/PatternController.java)

任务：

- 实现图解相关接口

产出目标：

- 满足 T032 中定义的接口行为

### T034

文件：[FileControllerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/file/controller/FileControllerTest.java)

任务：

- 先写文件访问接口测试
- 覆盖文件存在时可访问
- 覆盖文件不存在时返回明确错误

产出目标：

- 锁定统一文件访问接口行为

### T035

文件：[FileController.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/file/controller/FileController.java)

任务：

- 实现统一文件访问接口

产出目标：

- 满足 T034 中定义的接口行为

## 阶段四：阅读配置与进度同步

### T036

文件：[PatternConfigEntity.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/configstate/model/entity/PatternConfigEntity.java)

任务：

- 新增阅读配置实体定义

产出目标：

- 表达项目与图解维度的阅读状态

### T037

文件：[ProjectProgressEntity.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/progress/model/entity/ProjectProgressEntity.java)

任务：

- 新增项目进度实体定义

产出目标：

- 表达计数和计时汇总状态

### T038

文件：[SavePatternConfigRequest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/configstate/model/dto/SavePatternConfigRequest.java)

任务：

- 新增阅读配置保存请求模型

产出目标：

- 承接阅读配置保存接口入参

### T039

文件：[SaveRowProgressRequest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/progress/model/dto/SaveRowProgressRequest.java)

任务：

- 新增计数同步请求模型

产出目标：

- 承接当前行数和累计计数入参

### T040

文件：[SaveTimeProgressRequest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/progress/model/dto/SaveTimeProgressRequest.java)

任务：

- 新增计时同步请求模型

产出目标：

- 承接累计时长入参

### T041

文件：[PatternConfigMapper.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/configstate/mapper/PatternConfigMapper.java)

任务：

- 新增阅读配置数据访问接口

产出目标：

- 提供阅读配置查询和保存能力

### T042

文件：[ProjectProgressMapper.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/progress/mapper/ProjectProgressMapper.java)

任务：

- 新增项目进度数据访问接口

产出目标：

- 提供计数与计时查询保存能力

### T043

文件：[PatternConfigServiceTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/configstate/service/PatternConfigServiceTest.java)

任务：

- 先写阅读配置服务测试
- 覆盖首次保存成功
- 覆盖查询默认值
- 覆盖按图解维度隔离状态

产出目标：

- 锁定阅读配置服务规则

### T044

文件：[PatternConfigService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/configstate/service/PatternConfigService.java)

任务：

- 实现阅读配置查询和保存服务

产出目标：

- 满足 T043 中定义的业务行为

### T045

文件：[ProjectProgressServiceTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/progress/service/ProjectProgressServiceTest.java)

任务：

- 先写项目进度服务测试
- 覆盖计数覆盖保存
- 覆盖计时覆盖保存
- 覆盖查询当前进度

产出目标：

- 锁定进度同步规则

### T046

文件：[ProjectProgressService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/progress/service/ProjectProgressService.java)

任务：

- 实现计数和计时同步服务

产出目标：

- 满足 T045 中定义的业务行为

### T047

文件：[PatternConfigControllerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/configstate/controller/PatternConfigControllerTest.java)

任务：

- 先写阅读配置控制层测试
- 覆盖查询接口和保存接口

产出目标：

- 锁定阅读配置接口行为

### T048

文件：[PatternConfigController.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/configstate/controller/PatternConfigController.java)

任务：

- 实现阅读配置接口

产出目标：

- 满足 T047 中定义的接口行为

### T049

文件：[ProjectProgressControllerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/progress/controller/ProjectProgressControllerTest.java)

任务：

- 先写进度控制层测试
- 覆盖计数同步、计时同步、进度查询接口

产出目标：

- 锁定进度接口行为

### T050

文件：[ProjectProgressController.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/progress/controller/ProjectProgressController.java)

任务：

- 实现进度接口

产出目标：

- 满足 T049 中定义的接口行为

## 阶段五：总览恢复与稳定性补齐

### T051

文件：[ProjectOverviewVO.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/model/vo/ProjectOverviewVO.java)

任务：

- 新增项目总览响应模型

产出目标：

- 承接页面恢复所需聚合结果

### T052

文件：[ProjectOverviewServiceTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/project/service/ProjectOverviewServiceTest.java)

任务：

- 先写项目总览服务测试
- 覆盖总览聚合成功
- 覆盖项目不存在失败

产出目标：

- 锁定页面恢复主流程

### T053

文件：[ProjectService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/service/ProjectService.java)

任务：

- 在已有服务中补充项目总览聚合能力

产出目标：

- 满足 T052 中定义的业务行为

### T054

文件：[ProjectControllerTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/project/controller/ProjectControllerTest.java)

任务：

- 在已有控制层测试中补充项目总览接口测试

产出目标：

- 锁定总览接口对外行为

### T055

文件：[ProjectController.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/controller/ProjectController.java)

任务：

- 在已有控制层实现中补充项目总览接口

产出目标：

- 满足 T054 中定义的接口行为

### T056

文件：[PatternServiceValidationTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/pattern/service/PatternServiceValidationTest.java)

任务：

- 先写图解校验边界测试
- 覆盖链接为空
- 覆盖上传文件为空
- 覆盖删除不存在图解

产出目标：

- 补齐图解服务边界行为

### T057

文件：[PatternService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/pattern/service/PatternService.java)

任务：

- 在已有服务中补充图解边界校验

产出目标：

- 满足 T056 中定义的业务行为

### T058

文件：[ProjectServiceValidationTest.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/test/java/com/zzl/zhizhile/project/service/ProjectServiceValidationTest.java)

任务：

- 先写项目边界测试
- 覆盖当前图解不存在时的总览返回

产出目标：

- 补齐项目恢复边界行为

### T059

文件：[ProjectService.java](/Users/ouyangxiaofeng/dev/zzl-project/zhizhile-backend/src/main/java/com/zzl/zhizhile/project/service/ProjectService.java)

任务：

- 在已有服务中补充项目恢复边界处理

产出目标：

- 满足 T058 中定义的业务行为

## 5. 推荐执行顺序

建议严格按照任务编号顺序推进。

原因：

- 低编号任务为高编号任务提供依赖
- 同一功能始终先测试后实现
- 多次修改同一实现文件已经拆成连续任务，便于单次提交和回归验证

## 6. 单次提交建议

如果采用小步提交，建议每两步或三步形成一个自然提交单元。

示例：

- `T012 + T013` 作为项目服务一组
- `T014 + T015` 作为项目接口一组
- `T022 + T023` 作为图解链接服务一组
- `T043 + T044` 作为阅读配置服务一组

## 7. 完成标准

满足以下条件可认为任务清单执行完成：

- `spec.md` 中列出的核心业务能力全部落地
- `plan.md` 中定义的核心模块全部具备最小可运行实现
- 项目、图解、阅读配置、进度同步和总览恢复主链路全部可测试
- 每个功能均遵守测试先行和单任务单文件修改约束
