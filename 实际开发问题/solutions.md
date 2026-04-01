# 实际开发问题解决记录

## 问题：前端创建项目时报 CORS 异常

### 现象
前端在 `http://localhost:5173` 调用后端 `POST http://localhost:8080/api/projects` 时失败，浏览器控制台报错：

- `Response to preflight request doesn't pass access control check`
- `No 'Access-Control-Allow-Origin' header is present`

### 成因分析
这是典型的跨域请求被浏览器拦截：

1. 前端和后端端口不同（`5173` 与 `8080`），属于不同源。
2. `POST + application/json` 会触发浏览器预检请求（`OPTIONS`）。
3. 后端未返回合法 CORS 响应头（尤其是 `Access-Control-Allow-Origin`），导致预检失败。
4. 预检失败后，浏览器不会发送真正的 `POST` 请求。

## 解决方法

### 采用方案
在 Spring Boot 后端添加**全局 CORS 配置**，并仅放行 `/api/**` 路径。

### 代码落地
新增文件：

- `zhizhile-backend/src/main/java/com/zzl/zhizhile/common/config/WebCorsConfig.java`

主要配置：

- `allowedOrigins`: `http://localhost:5173`
- `allowedMethods`: `GET, POST, PUT, DELETE, OPTIONS`
- `allowedHeaders`: `*`
- `allowCredentials`: `true`
- `maxAge`: `3600`
- `registerCorsConfiguration`: `/api/**`

### 操作步骤
1. 添加 `WebCorsConfig` 配置类。
2. 重启后端服务（端口 `8080`）。
3. 重新发起前端创建项目请求。

## 结果验证

使用预检请求验证：

- `OPTIONS http://localhost:8080/api/projects`
- 请求头包含 `Origin: http://localhost:5173`

后端返回成功且包含：

- `Access-Control-Allow-Origin: http://localhost:5173`
- `Access-Control-Allow-Methods: GET,POST,PUT,DELETE,OPTIONS`
- `Access-Control-Allow-Credentials: true`

说明 CORS 已生效，前端可正常调用接口。

## 生产环境建议

- 生产环境建议使用**明确白名单域名**，不要使用 `*`。
- 若后续上线域名变化，应同步更新 `allowedOrigins`。
- 可进一步改为通过配置文件维护白名单，便于多环境切换。
