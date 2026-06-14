<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '@/api'
import gsap from 'gsap'

const router = useRouter()

const stats = ref({
  totalUsers: 0,
  totalProjects: 0,
  pendingReviews: 0,
  totalApplications: 0,
})
const loading = ref(true)
const error = ref('')

const statCards = [
  {
    key: 'totalUsers',
    label: '用户总数',
    icon: '👥',
    color: 'var(--accent)',
    glowColor: 'rgba(233, 69, 96, 0.3)',
  },
  {
    key: 'totalProjects',
    label: '项目总数',
    icon: '📁',
    color: '#74b9ff',
    glowColor: 'rgba(116, 185, 255, 0.3)',
  },
  {
    key: 'pendingReviews',
    label: '待审核',
    icon: '⏳',
    color: '#fdcb6e',
    glowColor: 'rgba(253, 203, 110, 0.3)',
  },
  {
    key: 'totalApplications',
    label: '申请总数',
    icon: '📋',
    color: '#00b894',
    glowColor: 'rgba(0, 184, 148, 0.3)',
  },
]

const quickActions = [
  { label: '用户管理', icon: '👥', path: '/admin/users', desc: '查看与管理平台用户' },
  { label: '项目管理', icon: '📁', path: '/admin/projects', desc: '审核与管理创业项目' },
  { label: '申请管理', icon: '📋', path: '/admin/applications', desc: '处理入驻与投资申请' },
]

onMounted(async () => {
  // 入场动画 — 先执行，不等待 API
  const tl = gsap.timeline()

  tl.from('.dashboard-container', {
    y: 40,
    opacity: 0,
    duration: 0.8,
    ease: 'power3.out',
  })

  tl.from('.dashboard-header h1', {
    y: 30,
    opacity: 0,
    duration: 0.6,
    ease: 'power2.out',
  }, '-=0.4')

  tl.from('.dashboard-header .subtitle', {
    y: 20,
    opacity: 0,
    duration: 0.5,
    ease: 'power2.out',
  }, '-=0.3')

  tl.from('.stat-card', {
    y: 40,
    opacity: 0,
    duration: 0.6,
    stagger: 0.12,
    ease: 'back.out(1.4)',
  }, '-=0.2')

  tl.from('.quick-action-card', {
    y: 30,
    opacity: 0,
    duration: 0.5,
    stagger: 0.1,
    ease: 'power2.out',
  }, '-=0.3')

  // 获取仪表盘数据
  try {
    const res = await api.get('/admin/stats')
    if (res.data) {
      stats.value = {
        totalUsers: res.data.totalUsers ?? 0,
        totalProjects: res.data.totalProjects ?? 0,
        pendingReviews: res.data.pendingProjects ?? 0,
        totalApplications: res.data.totalApplications ?? 0,
      }
    }
  } catch (err) {
    console.error('Dashboard load error:', err)
    error.value = err.response?.data?.error || err.response?.data?.message || '数据加载失败，请稍后重试'
  } finally {
    loading.value = false
  }
})

function navigateTo(path) {
  router.push(path)
}
</script>

<template>
  <div class="admin-dashboard-page">
    <!-- 背景效果 — 与 Login/Register 一致的玻璃态背景 -->
    <div class="dashboard-bg">
      <div class="bg-gradient" />
      <div class="bg-grid" />
      <div class="bg-glow" />
    </div>

    <div class="dashboard-container">
      <!-- 头部 -->
      <div class="dashboard-header">
        <div class="logo-wrapper">
          <span class="logo-icon">管</span>
        </div>
        <h1>管理后台</h1>
        <p class="subtitle">平台数据概览与管理</p>
      </div>

      <!-- 错误提示 -->
      <div v-if="error" class="error-banner">
        <span class="error-icon">!</span>
        {{ error }}
      </div>

      <!-- 统计卡片网格 -->
      <div class="stats-grid">
        <div
          v-for="card in statCards"
          :key="card.key"
          class="stat-card"
          :style="{ '--card-color': card.color, '--card-glow': card.glowColor }"
        >
          <div class="stat-card-inner">
            <div class="stat-icon-wrapper">
              <span class="stat-icon">{{ card.icon }}</span>
            </div>
            <div class="stat-info">
              <span class="stat-number">
                <span v-if="loading" class="stat-skeleton" />
                <template v-else>{{ stats[card.key] }}</template>
              </span>
              <span class="stat-label">{{ card.label }}</span>
            </div>
          </div>
          <div class="stat-card-glow" />
        </div>
      </div>

      <!-- 快捷操作 -->
      <div class="quick-actions-section">
        <h2 class="section-heading">快捷操作</h2>
        <div class="actions-grid">
          <button
            v-for="action in quickActions"
            :key="action.path"
            class="quick-action-card"
            @click="navigateTo(action.path)"
            data-cursor
          >
            <span class="action-icon">{{ action.icon }}</span>
            <div class="action-info">
              <span class="action-label">{{ action.label }}</span>
              <span class="action-desc">{{ action.desc }}</span>
            </div>
            <span class="action-arrow">→</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.admin-dashboard-page {
  min-height: 100vh;
  background: var(--ink);
  position: relative;
  overflow: hidden;
}

/* ========== 背景效果 ========== */
.dashboard-bg {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 20% 20%, rgba(233, 69, 96, 0.12) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 60%, rgba(116, 185, 255, 0.08) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 90%, rgba(0, 184, 148, 0.06) 0%, transparent 50%);
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

.bg-glow {
  position: absolute;
  top: 40%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 700px;
  height: 700px;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
  animation: glowPulse 8s ease-in-out infinite;
}

@keyframes glowPulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.4; }
  50% { transform: translate(-50%, -50%) scale(1.15); opacity: 0.7; }
}

/* ========== 容器 ========== */
.dashboard-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 3rem 2rem 5rem;
  position: relative;
  z-index: 10;
}

/* ========== 头部 ========== */
.dashboard-header {
  text-align: center;
  margin-bottom: 3rem;
}

.logo-wrapper {
  width: 64px;
  height: 64px;
  background: linear-gradient(135deg, var(--accent), var(--accent-warm));
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
  box-shadow: 0 10px 30px rgba(233, 69, 96, 0.3);
}

.logo-icon {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.6rem;
  font-weight: 700;
  color: white;
}

.dashboard-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.25rem;
  color: white;
  margin-bottom: 0.5rem;
  letter-spacing: 0.05em;
}

.subtitle {
  color: rgba(255, 255, 255, 0.55);
  font-size: 1rem;
}

/* ========== 错误横幅 ========== */
.error-banner {
  background: rgba(233, 69, 96, 0.12);
  border: 1px solid rgba(233, 69, 96, 0.3);
  color: var(--accent);
  padding: 1rem 1.5rem;
  border-radius: 12px;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 2rem;
  backdrop-filter: blur(10px);
}

.error-icon {
  width: 24px;
  height: 24px;
  background: var(--accent);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 0.8rem;
  flex-shrink: 0;
}

/* ========== 统计卡片网格 ========== */
.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.stat-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  padding: 1.75rem;
  position: relative;
  overflow: hidden;
  cursor: default;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
}

.stat-card:hover {
  transform: translateY(-6px);
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.15);
  box-shadow:
    0 12px 40px rgba(0, 0, 0, 0.3),
    0 0 30px var(--card-glow, transparent);
}

.stat-card-inner {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  position: relative;
  z-index: 2;
}

.stat-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 14px;
  background: var(--card-color);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
  opacity: 0.15;
  transition: opacity 0.35s ease;
}

.stat-card:hover .stat-icon-wrapper {
  opacity: 0.25;
}

.stat-icon {
  font-size: 1.4rem;
}

.stat-info {
  display: flex;
  flex-direction: column;
  gap: 0.35rem;
}

.stat-number {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  font-weight: 700;
  color: var(--card-color);
  line-height: 1.2;
}

.stat-skeleton {
  display: inline-block;
  width: 60px;
  height: 2rem;
  background: rgba(255, 255, 255, 0.08);
  border-radius: 6px;
  animation: skeletonPulse 1.5s ease-in-out infinite;
}

@keyframes skeletonPulse {
  0%, 100% { opacity: 0.4; }
  50% { opacity: 0.8; }
}

.stat-label {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.5);
}

.stat-card-glow {
  position: absolute;
  bottom: -50%;
  right: -30%;
  width: 150px;
  height: 150px;
  border-radius: 50%;
  background: var(--card-color);
  opacity: 0;
  filter: blur(40px);
  transition: opacity 0.35s ease;
  pointer-events: none;
}

.stat-card:hover .stat-card-glow {
  opacity: 0.08;
}

/* ========== 快捷操作 ========== */
.quick-actions-section {
  margin-top: 1rem;
}

.section-heading {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.4rem;
  color: white;
  margin-bottom: 1.5rem;
  padding-left: 0.25rem;
}

.actions-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.25rem;
}

.quick-action-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 18px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  gap: 1rem;
  transition: all 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  color: white;
  font-family: inherit;
  text-align: left;
}

.quick-action-card:hover {
  transform: translateY(-4px);
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(233, 69, 96, 0.3);
  box-shadow:
    0 10px 30px rgba(0, 0, 0, 0.3),
    0 0 25px rgba(233, 69, 96, 0.1);
}

.action-icon {
  font-size: 1.75rem;
  flex-shrink: 0;
  width: 48px;
  height: 48px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(255, 255, 255, 0.06);
  border-radius: 12px;
  transition: background 0.35s ease;
}

.quick-action-card:hover .action-icon {
  background: rgba(233, 69, 96, 0.15);
}

.action-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
}

.action-label {
  font-weight: 600;
  font-size: 1rem;
  color: white;
}

.action-desc {
  font-size: 0.85rem;
  color: rgba(255, 255, 255, 0.45);
}

.action-arrow {
  font-size: 1.2rem;
  color: rgba(255, 255, 255, 0.3);
  transition: all 0.35s ease;
  flex-shrink: 0;
}

.quick-action-card:hover .action-arrow {
  color: var(--accent);
  transform: translateX(4px);
}

/* ========== 响应式 ========== */
@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .actions-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .dashboard-container {
    padding: 2rem 1.25rem 4rem;
  }

  .dashboard-header h1 {
    font-size: 1.75rem;
  }

  .stats-grid {
    grid-template-columns: 1fr;
  }

  .actions-grid {
    grid-template-columns: 1fr;
  }

  .stat-number {
    font-size: 1.6rem;
  }
}
</style>
