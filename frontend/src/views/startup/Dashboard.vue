<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import api from '@/api'

const authStore = useAuthStore()
const projects = ref([])
const applications = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const [projectsRes, applicationsRes] = await Promise.all([
      api.get('/projects/my'),
      api.get('/applications/my'),
    ])
    projects.value = projectsRes.data
    applications.value = applicationsRes.data
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
})

function getStatusText(status) {
  const statusMap = {
    DRAFT: '草稿',
    PENDING: '待审核',
    APPROVED: '已通过',
    REJECTED: '已拒绝',
    REVIEWING: '审核中',
    INTERVIEW: '待面试',
  }
  return statusMap[status] || status
}

function getStatusClass(status) {
  const classMap = {
    DRAFT: 'status-draft',
    PENDING: 'status-pending',
    APPROVED: 'status-approved',
    REJECTED: 'status-rejected',
    REVIEWING: 'status-reviewing',
    INTERVIEW: 'status-interview',
  }
  return classMap[status] || ''
}
</script>

<template>
  <div class="dashboard-page">
    <section class="page-hero">
      <div class="page-hero-content">
        <h1>创业者后台</h1>
        <p>欢迎回来，{{ authStore.user?.realName || authStore.user?.username }}</p>
      </div>
    </section>

    <section class="dashboard-section">
      <!-- 统计卡片 -->
      <div class="stats-grid">
        <div class="stat-card">
          <h3>{{ projects.length }}</h3>
          <p>我的项目</p>
        </div>
        <div class="stat-card">
          <h3>{{ applications.length }}</h3>
          <p>入驻申请</p>
        </div>
        <div class="stat-card">
          <h3>{{ projects.filter(p => p.status === 'APPROVED').length }}</h3>
          <p>已通过</p>
        </div>
        <div class="stat-card">
          <h3>{{ projects.filter(p => p.status === 'PENDING').length }}</h3>
          <p>待审核</p>
        </div>
      </div>

      <!-- 快捷操作 -->
      <div class="quick-actions">
        <router-link to="/startup/apply" class="action-card">
          <span class="action-icon">📝</span>
          <h3>提交新项目</h3>
          <p>创建新的创业项目并提交入驻申请</p>
        </router-link>
        <router-link to="/tools" class="action-card">
          <span class="action-icon">🛠️</span>
          <h3>创业工具</h3>
          <p>使用BP模板、财务计算器等工具</p>
        </router-link>
      </div>

      <!-- 我的项目 -->
      <div class="section-block">
        <h2 class="section-title">我的项目</h2>
        <div v-if="loading" class="loading">加载中...</div>
        <div v-else-if="projects.length === 0" class="empty-state">
          <p>暂无项目</p>
          <router-link to="/startup/apply" class="btn-primary">立即创建</router-link>
        </div>
        <div v-else class="project-list">
          <div v-for="project in projects" :key="project.id" class="project-item">
            <div class="project-info">
              <h3>{{ project.name }}</h3>
              <p>{{ project.description }}</p>
              <div class="project-meta">
                <span class="tag">{{ project.industry }}</span>
                <span class="tag">{{ project.stage }}</span>
                <span :class="['status-tag', getStatusClass(project.status)]">
                  {{ getStatusText(project.status) }}
                </span>
              </div>
            </div>
            <div class="project-actions">
              <span class="funding-info">{{ project.fundingRound }} · {{ project.fundingAmount }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 入驻申请记录 -->
      <div class="section-block">
        <h2 class="section-title">入驻申请记录</h2>
        <div v-if="applications.length === 0" class="empty-state">
          <p>暂无申请记录</p>
        </div>
        <div v-else class="application-list">
          <div v-for="app in applications" :key="app.id" class="application-item">
            <div class="app-info">
              <h3>申请 #{{ app.id }}</h3>
              <p>提交时间：{{ new Date(app.createdAt).toLocaleDateString() }}</p>
            </div>
            <span :class="['status-tag', getStatusClass(app.status)]">
              {{ getStatusText(app.status) }}
            </span>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
.page-hero {
  background: var(--ink);
  padding: 8rem 4rem 4rem;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.page-hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(ellipse at center, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
}

.page-hero-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
  margin: 0 auto;
}

.page-hero h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  color: white;
  margin-bottom: 0.5rem;
}

.page-hero p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
}

.dashboard-section {
  padding: 4rem;
  max-width: 1200px;
  margin: 0 auto;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.stat-card {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  border: 1px solid var(--stone);
}

.stat-card h3 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: var(--accent);
  margin-bottom: 0.5rem;
}

.stat-card p {
  color: var(--text-light);
  font-size: 0.9rem;
}

.quick-actions {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  margin-bottom: 3rem;
}

.action-card {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid var(--stone);
  transition: all 0.3s;
  text-decoration: none;
  color: inherit;
}

.action-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.action-icon {
  font-size: 2rem;
  display: block;
  margin-bottom: 1rem;
}

.action-card h3 {
  font-size: 1.2rem;
  color: var(--ink);
  margin-bottom: 0.5rem;
}

.action-card p {
  color: var(--text-light);
  font-size: 0.9rem;
}

.section-block {
  margin-bottom: 3rem;
}

.section-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.5rem;
  color: var(--ink);
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--stone);
}

.loading {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
}

.empty-state {
  text-align: center;
  padding: 3rem;
  background: white;
  border-radius: 16px;
  border: 1px solid var(--stone);
}

.empty-state p {
  color: var(--text-light);
  margin-bottom: 1rem;
}

.project-list,
.application-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.project-item,
.application-item {
  background: white;
  border-radius: 12px;
  padding: 1.5rem;
  border: 1px solid var(--stone);
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.project-info h3,
.app-info h3 {
  font-size: 1.1rem;
  color: var(--ink);
  margin-bottom: 0.5rem;
}

.project-info p,
.app-info p {
  color: var(--text-light);
  font-size: 0.9rem;
  margin-bottom: 0.75rem;
}

.project-meta {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.tag {
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

.status-tag {
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

.status-draft {
  background: rgba(108, 117, 125, 0.1);
  color: #6c757d;
}

.status-pending {
  background: rgba(253, 203, 110, 0.1);
  color: #e17055;
}

.status-approved {
  background: rgba(0, 184, 148, 0.1);
  color: var(--success);
}

.status-rejected {
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
}

.status-reviewing {
  background: rgba(116, 185, 255, 0.1);
  color: #0984e3;
}

.status-interview {
  background: rgba(162, 155, 254, 0.1);
  color: #6c5ce7;
}

.funding-info {
  color: var(--accent);
  font-weight: 600;
  font-size: 0.9rem;
}

.btn-primary {
  background: var(--accent);
  color: white;
  padding: 0.8rem 1.5rem;
  border-radius: 50px;
  text-decoration: none;
  font-weight: 600;
  font-size: 0.9rem;
  display: inline-block;
  transition: all 0.3s;
}

.btn-primary:hover {
  background: var(--accent-warm);
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .page-hero {
    padding: 6rem 1.5rem 3rem;
  }

  .page-hero h1 {
    font-size: 2rem;
  }

  .dashboard-section {
    padding: 2rem 1.5rem;
  }

  .project-item,
  .application-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 1rem;
  }
}
</style>
