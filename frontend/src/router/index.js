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
  const user = JSON.parse(localStorage.getItem('user') || 'null')

  if (to.meta.requiresAuth && !token) {
    next({ name: 'Login', query: { redirect: to.fullPath } })
  } else if (to.meta.role && user?.role !== to.meta.role) {
    next({ name: 'Home' })
  } else {
    next()
  }
})

export default router
