# 🚀 创启未来 — 大学初创团队孵化器与融资平台

<div align="center">

**连接创业者、孵化器与投资人的一站式平台**

[![Vue](https://img.shields.io/badge/Vue-3.5-4FC08D?logo=vue.js&logoColor=white)](https://vuejs.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.2-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![SQLite](https://img.shields.io/badge/SQLite-3-003B57?logo=sqlite&logoColor=white)](https://www.sqlite.org/)
[![JWT](https://img.shields.io/badge/JWT-0.12-000000?logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![GSAP](https://img.shields.io/badge/GSAP-3.15-88CE02?logo=greensock&logoColor=white)](https://gsap.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

<br/>

### 🌐 在线体验

<h1>
  <a href="https://test.guyueqi.cc.cd/" target="_blank">
    <strong>🔗 test.guyueqi.cc.cd</strong>
  </a>
</h1>

> 🎯 点击上方链接，即刻体验完整平台功能

</div>

---

## 📖 项目简介

**创启未来**是一个大学初创团队孵化器与融资平台，致力于连接**创业者**、**孵化器**与**投资人**，打造一站式创业服务生态。

平台提供从 **BP 打磨** → **孵化器入驻** → **投资人对接** 的全链条服务，包含项目展示、入驻申请、工具赋能、资源匹配等核心功能。

### ✨ 核心特色

- 🎨 **Awwwards 级沉浸式 UI** — GSAP 动画、Lenis 平滑滚动、粒子背景、自定义光标
- 🔐 **JWT 认证 + 角色权限** — 创业者 / 投资人 / 管理员三级权限
- 📊 **项目管理** — 项目创建、编辑、提交审核、状态追踪
- 📝 **入驻申请** — 在线填写、材料提交、审核流转
- 🧰 **创业工具箱** — BP 模板、财务模型、市场分析等
- 🌐 **项目大厅** — 投资人浏览、筛选、对接优质项目

---

## 🖥️ 界面预览

| Hero 首页 | 角色卡片 | 入驻流程 |
|:---:|:---:|:---:|
| ![Hero](screenshots/hero.png) | ![Roles](screenshots/roles.png) | ![Process](screenshots/process.png) |

| 工具赋能 | CTA | 注册登录 |
|:---:|:---:|:---:|
| ![Tools](screenshots/tools.png) | ![CTA](screenshots/cta.png) | ![Login](screenshots/login.png) |

---

## 🛠️ 技术栈

### 前端

| 技术 | 版本 | 用途 |
|------|------|------|
| [Vue 3](https://vuejs.org/) | 3.5 | 渐进式前端框架 (Composition API) |
| [Vite](https://vitejs.dev/) | 8.0 | 下一代构建工具 |
| [Vue Router](https://router.vuejs.org/) | 4.6 | 路由管理 |
| [Pinia](https://pinia.vuejs.org/) | 3.0 | 状态管理 |
| [Axios](https://axios-http.com/) | 1.17 | HTTP 客户端 |
| [GSAP](https://gsap.com/) | 3.15 | 高性能动画引擎 |
| [Lenis](https://lenis.studiofreight.com/) | 1.3 | 平滑滚动 |

### 后端

| 技术 | 版本 | 用途 |
|------|------|------|
| [Spring Boot](https://spring.io/projects/spring-boot) | 3.2.0 | Java 后端框架 |
| [Spring Security](https://spring.io/projects/spring-security) | 6.2 | 认证与授权 |
| [Spring Data JPA](https://spring.io/projects/spring-data-jpa) | 3.2 | ORM 数据访问 |
| [JJWT](https://github.com/jwtk/jjwt) | 0.12.3 | JWT 令牌管理 |
| [SQLite](https://www.sqlite.org/) | 3.44 | 嵌入式关系数据库 |
| [Hibernate Community Dialects](https://github.com/hibernate/hibernate-community-dialects) | 6.4 | SQLite Hibernate 方言 |

---

## 🚀 快速开始

### 环境要求

- **Node.js** >= 18
- **Java** >= 17
- **Maven** >= 3.8

### 1. 克隆仓库

```bash
git clone https://github.com/qiyan233/chuangqi-weilai.git
cd chuangqi-weilai
```

### 2. 启动后端 (Spring Boot)

```bash
cd backend
mvn spring-boot:run
```

后端运行在 `http://localhost:8080`，SQLite 数据库文件 `startup.db` 会自动创建。

### 3. 启动前端 (Vue 3 + Vite)

```bash
cd frontend
npm install
npm run dev
```

前端运行在 `http://localhost:3000`，API 请求自动代理到后端 `:8080`。

### 4. 打开浏览器

访问 **`http://localhost:3000`** 即可体验。

---

## 📁 项目结构

```
chuangqi-weilai/
├── backend/                         # Spring Boot 后端
│   ├── pom.xml                      # Maven 配置
│   ├── src/main/java/com/qi/startup/
│   │   ├── Application.java         # 入口类
│   │   ├── config/
│   │   │   ├── CorsConfig.java      # CORS 跨域配置
│   │   │   └── SecurityConfig.java  # Spring Security + JWT 配置
│   │   ├── controller/
│   │   │   ├── AdminController.java    # 管理员 API
│   │   │   ├── ApplicationController.java  # 入驻申请 API
│   │   │   ├── AuthController.java     # 认证 API (登录/注册)
│   │   │   ├── ProjectController.java  # 项目 CRUD API
│   │   │   └── StatsController.java    # 统计数据 API
│   │   ├── dto/
│   │   │   ├── AuthResponse.java       # 认证响应
│   │   │   ├── LoginRequest.java       # 登录请求
│   │   │   └── RegisterRequest.java    # 注册请求
│   │   ├── model/
│   │   │   ├── Application.java        # 入驻申请实体
│   │   │   ├── Project.java            # 项目实体
│   │   │   └── User.java               # 用户实体
│   │   ├── repository/                 # JPA 数据访问层
│   │   ├── service/                    # 业务逻辑层
│   │   └── util/
│   │       └── JwtUtil.java            # JWT 工具类
│   └── src/main/resources/
│       ├── application.yml             # 应用配置
│       └── db/schema.sql               # 数据库建表脚本
│
├── frontend/                        # Vue 3 前端
│   ├── index.html                   # SPA 入口
│   ├── vite.config.js               # Vite 配置 (含 API 代理)
│   ├── package.json
│   └── src/
│       ├── main.js                  # Vue 应用入口
│       ├── App.vue                  # 根组件 (Lenis + 光标 + 页面转场)
│       ├── api/index.js             # Axios 实例 (拦截器封装)
│       ├── composables/
│       │   ├── useGsap.js           # GSAP 动画组合函数
│       │   └── useLenis.js          # Lenis 平滑滚动组合函数
│       ├── components/
│       │   ├── AppCursor.vue        # 自定义光标
│       │   └── layout/
│       │       ├── AppNav.vue       # 导航栏
│       │       └── AppFooter.vue    # 页脚
│       ├── router/index.js          # 路由 + 导航守卫
│       ├── stores/auth.js           # Pinia 认证 Store
│       ├── styles/global.css        # 全局样式 + CSS 变量
│       └── views/
│           ├── Home.vue             # 首页 (Hero + Stats + Roles + Process + Tools + CTA)
│           ├── Cases.vue            # 成功案例
│           ├── Tools.vue            # 创业工具
│           ├── auth/
│           │   ├── Login.vue        # 登录 (玻璃拟态)
│           │   └── Register.vue     # 注册 (玻璃拟态)
│           ├── startup/
│           │   ├── Apply.vue        # 入驻申请
│           │   └── Dashboard.vue    # 创业者仪表盘
│           └── investor/
│               ├── Dashboard.vue    # 投资人仪表盘
│               └── Projects.vue     # 项目大厅
│
└── README.md
```

---

## 📡 API 接口

### 认证 Auth

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| `POST` | `/api/auth/register` | 用户注册 | 否 |
| `POST` | `/api/auth/login` | 用户登录 | 否 |
| `GET` | `/api/auth/me` | 获取当前用户信息 | 是 |

### 项目 Project

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `GET` | `/api/projects/approved` | 已审核项目列表 (公开) | 否 |
| `GET` | `/api/projects/:id` | 项目详情 | 否 |
| `GET` | `/api/projects/my` | 我的项目列表 | ENTREPRENEUR |
| `POST` | `/api/projects` | 创建项目 | ENTREPRENEUR |
| `PUT` | `/api/projects/:id` | 更新项目 | 所有者 |
| `POST` | `/api/projects/:id/submit` | 提交审核 | 所有者 |

### 入驻申请 Application

| 方法 | 路径 | 说明 | 权限 |
|------|------|------|------|
| `POST` | `/api/applications` | 提交入驻申请 | ENTREPRENEUR |
| `GET` | `/api/applications/my` | 我的申请列表 | ENTREPRENEUR |

### 统计 Stats

| 方法 | 路径 | 说明 |
|------|------|------|
| `GET` | `/api/stats` | 平台统计数据 |

---

## 🗄️ 数据库

使用 **SQLite** 嵌入式数据库，共 3 张核心表：

### users — 用户表
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | INTEGER PK | 自增主键 |
| `username` | VARCHAR(50) UNIQUE | 用户名 |
| `email` | VARCHAR(100) UNIQUE | 邮箱 |
| `password_hash` | VARCHAR(255) | BCrypt 加密密码 |
| `role` | ENUM | ENTREPRENEUR / INVESTOR / ADMIN |
| `real_name`, `phone`, `city` | VARCHAR | 个人信息 |
| `company`, `position` | VARCHAR | 任职信息 |

### projects — 项目表
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | INTEGER PK | 自增主键 |
| `user_id` | FK → users | 所属创业者 |
| `name`, `industry`, `stage` | VARCHAR | 基本信息 |
| `description`, `pain_point` | TEXT | 项目描述 |
| `business_model`, `core_tech` | TEXT | 商业模式 |
| `funding_round`, `funding_amount` | VARCHAR | 融资信息 |
| `status` | ENUM | DRAFT / PENDING / APPROVED / REJECTED |

### applications — 入驻申请表
| 字段 | 类型 | 说明 |
|------|------|------|
| `id` | INTEGER PK | 自增主键 |
| `user_id` | FK → users | 申请人 |
| `project_id` | FK → projects | 关联项目 |
| `team_members`, `equity_structure` | TEXT | 团队信息 |
| `status` | ENUM | PENDING / APPROVED / REJECTED |
| `review_notes` | TEXT | 审核意见 |

---

## 🎨 UI 动画系统

项目使用 **GSAP 3** + **Lenis** 构建沉浸式动画体验：

| 动画 | 技术 | 说明 |
|------|------|------|
| Hero 入场时间线 | `gsap.timeline` | 标题、按钮、轨道顺序入场 |
| 统计数字滚动 | `gsap.to` + ScrollTrigger | 滚动到 stats 区触发计数 |
| 卡片滚动渐入 | `gsap.to` + ScrollTrigger | 角色卡片、工具卡片 |
| 轨道旋转 | CSS `@keyframes rotate` | 3 层同心轨道循环旋转 |
| 粒子背景 | Canvas + `requestAnimationFrame` | 80 粒子 + 连接线 |
| 平滑滚动 | Lenis | 全局平滑滚动 + 惯性 |
| 自定义光标 | GSAP `to` + 事件监听 | 鼠标跟随 + 悬停缩放 |
| 页面转场 | Vue `<transition>` | 路由切换淡入淡出 |
| 视差效果 | ScrollTrigger + `scrub: true` | Hero 背景、轨道跟随滚动 |

---

## 🔮 待实现

- [ ] 文件上传 (BP / 图片)
- [ ] 消息通知系统
- [ ] 管理后台完善
- [ ] 数据统计图表
- [ ] 移动端适配优化
- [ ] 单元测试与集成测试
- [ ] CI/CD 自动化部署

---

## 📄 许可证

本项目基于 **MIT License** 开源。

---

<div align="center">

**Made with ❤️ by [qiyan233](https://github.com/qiyan233)**

Vue 3 · Spring Boot · SQLite · GSAP

</div>
