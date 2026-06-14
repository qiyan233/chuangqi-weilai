<script setup>
import { ref, onMounted, computed } from 'vue'
import api from '@/api'

const projects = ref([])
const loading = ref(true)
const selectedIndustry = ref('')
const selectedStage = ref('')

const industries = [
  '人工智能', '企业服务', '消费零售', '医疗健康',
  '教育培训', '金融科技', '先进制造', '新能源'
]

const stages = ['概念阶段', '原型开发', 'MVP验证', '已上线运营', '规模化扩张']

onMounted(async () => {
  await fetchProjects()
})

async function fetchProjects() {
  loading.value = true
  try {
    const params = {}
    if (selectedIndustry.value) params.industry = selectedIndustry.value
    if (selectedStage.value) params.stage = selectedStage.value
    const response = await api.get('/projects/approved', { params })
    projects.value = response.data
  } catch (error) {
    console.error('加载项目失败:', error)
  } finally {
    loading.value = false
  }
}

function filterByIndustry(industry) {
  selectedIndustry.value = selectedIndustry.value === industry ? '' : industry
  fetchProjects()
}

function filterByStage(stage) {
  selectedStage.value = selectedStage.value === stage ? '' : stage
  fetchProjects()
}

function clearFilters() {
  selectedIndustry.value = ''
  selectedStage.value = ''
  fetchProjects()
}

function getProjectLogo(name) {
  return name ? name.charAt(0) : '项'
}

function getLogoColor(index) {
  const colors = ['#e94560', '#00b894', '#f4a261', '#74b9ff', '#a29bfe', '#fd79a8']
  return colors[index % colors.length]
}
</script>

<template>
  <div class="projects-page">
    <!-- Hero -->
    <section class="page-hero">
      <div class="page-hero-content">
        <h1>项目大厅</h1>
        <p>浏览优质创业项目，发现下一个独角兽</p>
      </div>
    </section>

    <!-- 筛选栏 -->
    <section class="filter-section">
      <div class="filter-container">
        <div class="filter-group">
          <span class="filter-label">行业：</span>
          <button
            v-for="ind in industries"
            :key="ind"
            class="filter-btn"
            :class="{ active: selectedIndustry === ind }"
            @click="filterByIndustry(ind)"
          >
            {{ ind }}
          </button>
        </div>
        <div class="filter-group">
          <span class="filter-label">阶段：</span>
          <button
            v-for="stage in stages"
            :key="stage"
            class="filter-btn"
            :class="{ active: selectedStage === stage }"
            @click="filterByStage(stage)"
          >
            {{ stage }}
          </button>
        </div>
        <button v-if="selectedIndustry || selectedStage" class="clear-btn" @click="clearFilters">
          清除筛选
        </button>
      </div>
    </section>

    <!-- 项目列表 -->
    <section class="projects-section">
      <div v-if="loading" class="loading">加载中...</div>
      <div v-else-if="projects.length === 0" class="empty-state">
        <p>暂无符合条件的项目</p>
      </div>
      <div v-else class="projects-grid">
        <div v-for="(project, index) in projects" :key="project.id" class="project-card">
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
          <div class="project-metrics">
            <div v-if="project.userCount" class="metric">
              <span class="metric-value">{{ project.userCount }}</span>
              <span class="metric-label">用户数</span>
            </div>
            <div v-if="project.mrr" class="metric">
              <span class="metric-value">{{ project.mrr }}</span>
              <span class="metric-label">MRR</span>
            </div>
          </div>
          <div class="project-footer">
            <span class="project-amount">融资 {{ project.fundingAmount }}</span>
            <router-link :to="`/investor/projects/${project.id}`" class="project-action">
              查看详情 →
            </router-link>
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
  margin-bottom: 1rem;
}

.page-hero p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
}

.filter-section {
  background: white;
  padding: 2rem 4rem;
  border-bottom: 1px solid var(--stone);
}

.filter-container {
  max-width: 1400px;
  margin: 0 auto;
}

.filter-group {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.filter-label {
  font-weight: 600;
  color: var(--ink);
  font-size: 0.9rem;
  min-width: 50px;
}

.filter-btn {
  padding: 0.5rem 1rem;
  border: 1.5px solid var(--stone);
  background: white;
  border-radius: 20px;
  font-family: inherit;
  font-size: 0.85rem;
  color: var(--text);
  cursor: pointer;
  transition: all 0.3s;
}

.filter-btn:hover {
  border-color: var(--accent);
  color: var(--accent);
}

.filter-btn.active {
  background: var(--accent);
  color: white;
  border-color: var(--accent);
}

.clear-btn {
  padding: 0.5rem 1rem;
  background: none;
  border: none;
  color: var(--accent);
  font-size: 0.85rem;
  cursor: pointer;
  text-decoration: underline;
}

.projects-section {
  padding: 4rem;
  max-width: 1400px;
  margin: 0 auto;
}

.loading,
.empty-state {
  text-align: center;
  padding: 3rem;
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
  cursor: pointer;
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

.project-metrics {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
  padding: 0.75rem;
  background: var(--cream);
  border-radius: 8px;
}

.metric {
  display: flex;
  flex-direction: column;
}

.metric-value {
  font-weight: 700;
  color: var(--ink);
  font-size: 0.95rem;
}

.metric-label {
  font-size: 0.75rem;
  color: var(--text-light);
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

  .filter-section {
    padding: 1.5rem;
  }

  .projects-section {
    padding: 2rem 1.5rem;
  }

  .projects-grid {
    grid-template-columns: 1fr;
  }
}
</style>
