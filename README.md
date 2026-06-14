# 创启未来 - 大学初创团队孵化器与融资平台

连接创业者、孵化器与投资人的一站式平台。

## 技术栈

- **前端**：Vue 3 + Vite + Vue Router + Pinia + Axios
- **后端**：Spring Boot 3.2 + Spring Security + Spring Data JPA
- **数据库**：SQLite
- **认证**：JWT (JSON Web Token)

## 项目结构

```
startup-incubator-platform/
├── frontend/                    # Vue 3 前端
│   ├── src/
│   │   ├── api/                # API 请求封装
│   │   ├── components/         # 公共组件
│   │   ├── router/             # 路由配置
│   │   ├── stores/             # Pinia 状态管理
│   │   ├── styles/             # 全局样式
│   │   └── views/              # 页面视图
│   ├── index.html
│   ├── vite.config.js
│   └── package.json
│
├── backend/                     # Spring Boot 后端
│   ├── src/main/java/com/qi/startup/
│   │   ├── config/             # 配置类
│   │   ├── controller/         # 控制器
│   │   ├── service/            # 业务逻辑
│   │   ├── repository/         # 数据访问
│   │   ├── model/              # 实体类
│   │   ├── dto/                # 数据传输对象
│   │   └── util/               # 工具类
│   ├── src/main/resources/
│   │   ├── application.yml
│   │   └── db/schema.sql
│   └── pom.xml
│
└── README.md
```

## 快速开始

### 前置要求

- Node.js 18+
- Java 17+
- Maven 3.8+

### 启动后端

```bash
cd backend

# 首次运行需要下载依赖
mvn clean install

# 启动 Spring Boot 应用
mvn spring-boot:run
```

后端将在 http://localhost:8080 启动

### 启动前端

```bash
cd frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

前端将在 http://localhost:3000 启动

## 功能特性

### 已实现 (MVP)

- ✅ 用户注册/登录（创业者/投资人角色）
- ✅ 创业者提交入驻申请
- ✅ 创业者创建/管理项目
- ✅ 项目大厅（投资人浏览项目）
- ✅ 创业者/投资人后台仪表盘
- ✅ 成功案例展示
- ✅ 创业工具页面
- ✅ JWT 认证保护

### 待实现

- 📝 文件上传（BP、财务模型）
- 📝 管理员后台
- 📝 项目详情页
- 📝 消息通知系统
- 📝 数据统计图表

## API 接口

### 认证相关
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/auth/register | 用户注册 |
| POST | /api/auth/login | 用户登录 |
| GET  | /api/auth/me | 获取当前用户信息 |

### 项目相关
| 方法 | 路径 | 说明 |
|------|------|------|
| GET    | /api/projects/approved | 获取已审核项目列表 |
| GET    | /api/projects/:id | 获取项目详情 |
| GET    | /api/projects/my | 获取我的项目 |
| POST   | /api/projects | 创建项目 |
| PUT    | /api/projects/:id | 更新项目 |
| POST   | /api/projects/:id/submit | 提交审核 |

### 入驻申请相关
| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/applications | 提交入驻申请 |
| GET  | /api/applications/my | 我的申请记录 |

## 测试账号

启动后端后，数据库会自动创建。可以通过注册页面创建新账号。

## 注意事项

1. 首次启动后端时，SQLite 数据库文件 `startup.db` 会自动创建在 `backend/` 目录下
2. 前端开发服务器配置了代理，`/api` 请求会自动转发到后端 http://localhost:8080
3. JWT Token 存储在浏览器 localStorage 中，有效期 24 小时

## License

MIT
