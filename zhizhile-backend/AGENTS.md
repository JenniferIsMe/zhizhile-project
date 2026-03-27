# Repository Guidelines

## 项目结构与模块组织
本仓库是基于 Spring Boot 3 + Maven 的后端服务（`Java 17`）。核心代码位于 `src/main/java/com/zzl/zhizhile`，按业务域拆分：
- `project`、`pattern`、`progress`、`configstate`、`file`：各自包含 controller/service/mapper/model 分层。
- `common`：统一响应体、错误码与全局异常处理等通用能力。

运行配置在 `src/main/resources/application.yml`，数据库建表脚本在 `src/main/resources/db/schema.sql`。测试代码在 `src/test/java/com/zzl/zhizhile`，结构与主代码对应；测试配置在 `src/test/resources/application.yml`（H2 的 MySQL 兼容模式）。

## 构建、测试与本地开发命令
- `mvn spring-boot:run`：本地启动服务（默认端口 `8080`）。
- `mvn test`：执行全部 JUnit 5 / Spring Boot 测试。
- `mvn -Dtest=ProjectServiceTest test`：只运行单个测试类。
- `mvn clean package`：清理、编译、测试并打包产物到 `target/`。

说明：应用运行依赖本地 MySQL；自动化测试依赖 H2。

## 代码风格与命名约定
- 使用 4 空格缩进，遵循标准 Java 代码风格。
- 包名全小写；类名 `UpperCamelCase`；方法与字段 `lowerCamelCase`。
- DTO/VO/Entity 命名要语义明确，如 `CreateProjectRequest`、`ProjectOverviewVO`、`ProjectEntity`。
- 优先遵循模块内分层调用：`controller -> service -> mapper`；跨模块通用能力放到 `common`。

## 测试规范
- 测试框架：JUnit 5（`spring-boot-starter-test`）。
- 测试类以 `Test` 结尾，测试方法使用行为命名，如 `shouldFailWhenProjectNameBlank`。
- 新增功能至少覆盖成功路径和校验/异常路径（服务层与接口层）。
- 提交前执行 `mvn test`，确保涉及数据库逻辑的改动可通过 H2 初始化脚本验证。

## 提交与 PR 规范
当前历史提交以简洁中文为主（示例：`初始化前、后端项目，实现后端的接口层代码`）。建议继续使用简洁祈使句，并尽量带模块范围。

PR 建议包含：
- 变更摘要与影响模块。
- 关联任务/规格说明（如 `specs/spec.md`、`specs/tasks.md` 中的 `T043`）。
- 测试证据（至少包含 `mvn test` 结果，必要时附定向测试命令）。
- 接口行为变化说明（请求/响应结构、错误处理变更）。

## 安全与配置提示
- 不要提交真实账号密码；`application.yml` 中敏感值仅用于本地开发。
- 上传限制请与 `zhizhile.upload.allowed-exts` 和文件大小配置保持一致。
- 在未实现正式登录前，不要随意变更 `zhizhile.user.test-user-id` 语义。
