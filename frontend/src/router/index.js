import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('@/views/Home.vue'),
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/auth/Login.vue'),
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/auth/Register.vue'),
  },
  {
    path: '/startup/apply',
    name: 'StartupApply',
    component: () => import('@/views/startup/Apply.vue'),
    meta: { requiresAuth: true, role: 'ENTREPRENEUR' },
  },
  {
    path: '/startup/dashboard',
    name: 'StartupDashboard',
    component: () => import('@/views/startup/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'ENTREPRENEUR' },
  },
  {
    path: '/investor/projects',
    name: 'InvestorProjects',
    component: () => import('@/views/investor/Projects.vue'),
  },
  {
    path: '/investor/dashboard',
    name: 'InvestorDashboard',
    component: () => import('@/views/investor/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'INVESTOR' },
  },
  {
    path: '/cases',
    name: 'Cases',
    component: () => import('@/views/Cases.vue'),
  },
  {
    path: '/tools',
    name: 'Tools',
    component: () => import('@/views/Tools.vue'),
  },
  {
    path: '/admin',
    name: 'AdminDashboard',
    component: () => import('@/views/admin/Dashboard.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
  },
  {
    path: '/admin/users',
    name: 'AdminUsers',
    component: () => import('@/views/admin/Users.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
  },
  {
    path: '/admin/projects',
    name: 'AdminProjects',
    component: () => import('@/views/admin/Projects.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
  },
  {
    path: '/admin/applications',
    name: 'AdminApplications',
    component: () => import('@/views/admin/Applications.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, from, savedPosition) {
    if (to.hash) {
      return { el: to.hash, behavior: 'smooth' }
    }
    return savedPosition || { top: 0 }
  },
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  // 安全解析用户信息
  let user = null
  try {
    user = JSON.parse(localStorage.getItem('user') || 'null')
  } catch (e) {
    console.warn('Failed to parse user from localStorage:', e)
    localStorage.removeItem('user')
  }

  // 已登录用户访问登录/注册页时重定向到首页
  if (token && (to.name === 'Login' || to.name === 'Register')) {
    next({ name: 'Home' })
    return
  }

  // 需要认证的页面但未登录
  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
    return
  }

  // 角色不匹配
  if (to.meta.role && user?.role !== to.meta.role) {
    next({ name: 'Home' })
    return
  }

  next()
})

export default router
