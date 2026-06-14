# 🔧 创启未来 - Bug 修复报告

**修复日期**: 2026-06-14
**修复人**: Claude Code

---

## 📋 修复概览

本次共修复 **14 个问题**，其中：
- 🔴 严重/高优先级：8 个
- 🟡 中优先级：6 个

---

## 🔴 严重/高优先级修复

### 1. [安全] 注册接口允许任意角色（权限提升漏洞）
**文件**: `backend/src/main/java/com/qi/startup/service/AuthService.java`
**问题**: 用户注册时可以传入 `role=ADMIN`，从而获取管理员权限
**修复**: 忽略客户端传入的 role，强制注册为 `ENTREPRENEUR`

### 2. [安全] JWT 密钥硬编码在配置文件中
**文件**: `backend/src/main/resources/application.yml`
**问题**: JWT 密钥明文存储在版本库中，可被用于伪造 token
**修复**: 改为使用环境变量 `JWT_SECRET`，开发环境使用默认值

### 3. [安全] Security 配置路径匹配漏洞
**文件**: `backend/src/main/java/com/qi/startup/config/SecurityConfig.java`
**问题**: 管理员接口路径配置错误，导致任何登录用户可审核项目
**修复**: 移除冗余和错误的路径规则，统一使用 `/api/admin/**` 保护管理员接口

### 4. [安全] 项目详情接口泄露未审核项目
**文件**: `backend/src/main/java/com/qi/startup/controller/ProjectController.java`
**问题**: `/api/projects/{id}` 返回任意状态的项目，包括草稿
**修复**: 添加状态检查，公开接口只返回 `APPROVED` 状态的项目

### 5. [安全] 输入验证不足
**文件**: `backend/src/main/java/com/qi/startup/dto/RegisterRequest.java`
**问题**: role 字段只检查非空，未限制合法值
**修复**: 添加 `@Pattern` 注解，只允许 `ENTREPRENEUR` 或 `INVESTOR`

### 6. [内存泄漏] useGsap mouseParallax 事件监听器
**文件**: `frontend/src/composables/useGsap.js`
**问题**: `mousemove` 事件监听器在组件卸载后不会被移除
**修复**: 将清理函数保存到数组，在 `onUnmounted` 中统一执行

### 7. [内存泄漏] useLenis gsap.ticker 回调
**文件**: `frontend/src/composables/useLenis.js`
**问题**: gsap.ticker 回调在组件卸载后继续执行
**修复**: 保存回调引用，在 `onUnmounted` 中调用 `gsap.ticker.remove()`

### 8. [内存泄漏] Home.vue resize 事件监听器
**文件**: `frontend/src/views/Home.vue`
**问题**: `window.addEventListener('resize', ...)` 未在卸载时移除
**修复**: 保存 handler 引用，在 `onUnmounted` 中移除

---

## 🟡 中优先级修复

### 9. [功能] 删除重复的 CORS 配置
**文件**: `backend/src/main/java/com/qi/startup/config/CorsConfig.java`
**问题**: CorsConfig 和 SecurityConfig 重复配置 CORS
**修复**: 删除 CorsConfig.java，保留 SecurityConfig 中的配置

### 10. [性能] JwtUtil SecretKey 重复创建
**文件**: `backend/src/main/java/com/qi/startup/util/JwtUtil.java`
**问题**: 每次调用都重新生成 SecretKey
**修复**: 使用 `@PostConstruct` 缓存 SecretKey

### 11. [架构] 缺少全局异常处理器
**文件**: 新建 `backend/src/main/java/com/qi/startup/config/GlobalExceptionHandler.java`
**问题**: 部分 Controller 没有异常处理，返回格式不一致
**修复**: 创建 `@RestControllerAdvice` 统一处理异常

### 12. [安全] JSON.parse 缺少 try-catch
**文件**: `frontend/src/stores/auth.js`, `frontend/src/router/index.js`
**问题**: localStorage 数据损坏会导致应用崩溃
**修复**: 添加 try-catch 保护

### 13. [功能] ScrollTrigger 全局清理破坏其他组件
**文件**: `frontend/src/views/Home.vue`
**问题**: `ScrollTrigger.getAll().forEach(kill)` 会杀死所有组件的动画
**修复**: 使用 `gsap.context` 限定动画作用域

### 14. [功能] 路由守卫未处理已登录用户
**文件**: `frontend/src/router/index.js`
**问题**: 已登录用户仍可访问登录/注册页
**修复**: 添加重定向逻辑

---

## 📊 修改文件清单

| 文件 | 操作 |
|------|------|
| `backend/src/main/java/com/qi/startup/service/AuthService.java` | 修改 |
| `backend/src/main/resources/application.yml` | 修改 |
| `backend/src/main/java/com/qi/startup/config/SecurityConfig.java` | 修改 |
| `backend/src/main/java/com/qi/startup/config/CorsConfig.java` | 删除 |
| `backend/src/main/java/com/qi/startup/config/GlobalExceptionHandler.java` | 新建 |
| `backend/src/main/java/com/qi/startup/util/JwtUtil.java` | 修改 |
| `backend/src/main/java/com/qi/startup/dto/RegisterRequest.java` | 修改 |
| `backend/src/main/java/com/qi/startup/controller/ProjectController.java` | 修改 |
| `frontend/src/composables/useGsap.js` | 修改 |
| `frontend/src/composables/useLenis.js` | 修改 |
| `frontend/src/stores/auth.js` | 修改 |
| `frontend/src/router/index.js` | 修改 |
| `frontend/src/views/Home.vue` | 修改 |

---

## 🧪 测试建议

1. **注册测试**: 尝试注册时传入 `role=ADMIN`，应被拒绝
2. **JWT 测试**: 设置 `JWT_SECRET` 环境变量后启动应用
3. **项目详情测试**: 未登录状态下访问未审核项目，应返回错误
4. **内存泄漏测试**: 在首页和其他页面间多次导航，检查是否有事件监听器累积
5. **异常处理测试**: 提交无效参数，检查返回格式是否统一

---

## ⚠️ 待处理问题

以下问题已识别但未在本次修复中处理：

1. **N+1 查询问题**: Project 和 Application 的关联查询需要优化
2. **硬编码统计数据**: StatsController 返回假数据
3. **缺少输入验证**: Project 创建/更新接口需要 DTO
4. **项目详情路由缺失**: 前端 `/investor/projects/:id` 路由未定义
5. **数据库连接池配置**: SQLite 需要配置 HikariCP
