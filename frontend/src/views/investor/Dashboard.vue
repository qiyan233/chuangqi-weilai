<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import api from '@/api'

const authStore = useAuthStore()
const stats = ref({
  approvedProjects: 0,
  totalFunding: '0',
  partnerInvestors: 0,
  mentors: 0,
})
const recentProjects = ref([])
const loading = ref(true)

onMounted(async () => {
  try {
    const [statsRes, projectsRes] = await Promise.all([
      api.get('/stats'),
      api.get('/projects/approved'),
    ])
    stats.value = statsRes.data
    recentProjects.value = projectsRes.data.slice(0, 6)
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
})

function getProjectLogo(name) {
  return name ? name.charAt(0) : '项'
}

function getLogoColor(index) {
  const colors = ['#e94560', '#00b894', '#f4a261', '#74b9ff', '#a29bfe', '#fd79a8']
  return colors[index % colors.length]
}
</script>

<template>
  <div class="dashboard-page">
    <section class="page-hero">
      <div class="page-hero-content">
        <h1>投资人后台</h1>
        <p>欢迎回来，{{ authStore.user?.realName || authStore.user?.username }}</p>
      </div>
    </section>

    <section class="dashboard-section">
      <!-- 统计卡片 -->
      <div class="stats-grid">
        <div class="stat-card">
          <h3>{{ stats.approvedProjects }}+</h3>
          <p>优质项目</p>
          <span class="trend">↑ 23% 较上月</span>
        </div>
        <div class="stat-card">
          <h3>¥{{ stats.totalFunding }}</h3>
          <p>累计融资额</p>
          <span class="trend">↑ 15% 较上月</span>
        </div>
        <div class="stat-card">
          <h3>{{ stats.partnerInvestors }}+</h3>
          <p>合作投资机构</p>
        </div>
        <div class="stat-card">
          <h3>{{ stats.mentors }}+</h3>
          <p>资深导师</p>
        </div>
      </div>

      <!-- 快捷操作 -->
      <div class="quick-actions">
        <router-link to="/investor/projects" class="action-card">
          <span class="action-icon">🔍</span>
          <h3>浏览项目</h3>
          <p>发现优质创业项目</p>
        </router-link>
        <router-link to="/tools" class="action-card">
          <span class="action-icon">📊</span>
          <h3>数据分析</h3>
          <p>查看行业趋势与报告</p>
        </router-link>
      </div>

      <!-- 最新项目 -->
      <div class="section-block">
        <div class="section-header">
          <h2 class="section-title">最新优质项目</h2>
          <router-link to="/investor/projects" class="view-all">查看全部 →</router-link>
        </div>

        <div v-if="loading" class="loading">加载中...</div>
        <div v-else class="projects-grid">
          <div v-for="(project, index) in recentProjects" :key="project.id" class="project-card">
            <div class="project-header">
              <div class="project-logo" :style="{ background: getLogoColor(index) }">
                {{ getProjectLogo(project.name) }}
              </div>
              <span class="project-stage">{{ project.stage }}</span>
            </div>
            <h3>{{ project.name }}</h3>
            <p class="project-desc">{{ project.description }}</p>
            <div class="project-tags">
              <span class="project-tag">{{ project.industry }}</span>
              <span class="project-tag">{{ project.fundingRound }}</span>
            </div>
            <div class="project-footer">
              <span class="project-amount">融资 {{ project.fundingAmount }}</span>
              <router-link :to="`/investor/projects/${project.id}`" class="project-action">
                查看详情 →
              </router-link>
            </div>
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
  max-width: 1400px;
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
  font-size: 2.2rem;
  color: var(--accent);
  margin-bottom: 0.5rem;
}

.stat-card p {
  color: var(--text-light);
  font-size: 0.9rem;
  margin-bottom: 0.5rem;
}

.trend {
  display: inline-block;
  background: rgba(0, 184, 148, 0.1);
  color: var(--success);
  padding: 0.2rem 0.6rem;
  border-radius: 12px;
  font-size: 0.8rem;
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

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--stone);
}

.section-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.5rem;
  color: var(--ink);
}

.view-all {
  color: var(--accent);
  font-size: 0.9rem;
  font-weight: 600;
  text-decoration: none;
}

.view-all:hover {
  text-decoration: underline;
}

.loading {
  text-align: center;
  padding: 2rem;
  color: var(--text-light);
}

.projects-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
}

.project-card {
  background: white;
  border-radius: 16px;
  padding: 1.5rem;
  border: 1px solid var(--stone);
  transition: all 0.3s;
}

.project-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.project-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.project-logo {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 700;
  font-size: 1.1rem;
}

.project-stage {
  background: rgba(0, 184, 148, 0.1);
  color: var(--success);
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.75rem;
  font-weight: 600;
}

.project-card h3 {
  font-size: 1.1rem;
  color: var(--ink);
  margin-bottom: 0.5rem;
}

.project-desc {
  color: var(--text-light);
  font-size: 0.85rem;
  line-height: 1.5;
  margin-bottom: 1rem;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.project-tags {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
  margin-bottom: 1rem;
}

.project-tag {
  background: var(--cream);
  color: var(--text-light);
  padding: 0.25rem 0.6rem;
  border-radius: 6px;
  font-size: 0.75rem;
}

.project-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 1px solid var(--stone);
}

.project-amount {
  font-weight: 700;
  color: var(--accent);
  font-size: 1rem;
}

.project-action {
  color: var(--accent);
  font-size: 0.85rem;
  font-weight: 600;
  text-decoration: none;
}

.project-action:hover {
  text-decoration: underline;
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .quick-actions {
    grid-template-columns: 1fr;
  }

  .projects-grid {
    grid-template-columns: repeat(2, 1fr);
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

  .projects-grid {
    grid-template-columns: 1fr;
  }
}
</style>
