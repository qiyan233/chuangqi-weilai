<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import api from '@/api'
import gsap from 'gsap'

const route = useRoute()
const router = useRouter()

const project = ref(null)
const loading = ref(true)
const error = ref('')

onMounted(async () => {
  try {
    const res = await api.get(`/projects/${route.params.id}`)
    project.value = res.data
  } catch (err) {
    console.error('Load project error:', err)
    error.value = err.response?.data?.error || '项目不存在或未审核通过'
  } finally {
    loading.value = false
  }

  // 入场动画
  gsap.set(['.detail-header', '.detail-content', '.detail-sidebar'], { opacity: 0, y: 30 })
  gsap.to('.detail-header', { opacity: 1, y: 0, duration: 0.8, ease: 'power3.out' })
  gsap.to('.detail-content', { opacity: 1, y: 0, duration: 0.8, ease: 'power3.out', delay: 0.2 })
  gsap.to('.detail-sidebar', { opacity: 1, y: 0, duration: 0.8, ease: 'power3.out', delay: 0.4 })
})

function goBack() {
  router.push('/investor/projects')
}
</script>

<template>
  <div class="project-detail-page">
    <!-- 背景 -->
    <div class="detail-bg">
      <div class="bg-gradient" />
      <div class="bg-grid" />
    </div>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-state">
      <div class="loading-spinner" />
      <p>加载中...</p>
    </div>

    <!-- 错误状态 -->
    <div v-else-if="error" class="error-state">
      <div class="error-icon">!</div>
      <h2>{{ error }}</h2>
      <button class="btn-back" @click="goBack">返回项目大厅</button>
    </div>

    <!-- 项目详情 -->
    <div v-else-if="project" class="detail-container">
      <!-- 返回按钮 -->
      <button class="btn-back" @click="goBack">
        ← 返回项目大厅
      </button>

      <!-- 头部信息 -->
      <div class="detail-header">
        <div class="project-badge">
          <span class="badge-industry">{{ project.industry || '未分类' }}</span>
          <span class="badge-stage">{{ project.stage || '未知阶段' }}</span>
        </div>
        <h1>{{ project.name }}</h1>
        <p class="project-desc">{{ project.description }}</p>
        <div class="project-meta">
          <span v-if="project.user" class="meta-item">
            👤 {{ project.user.realName || project.user.username }}
          </span>
          <span v-if="project.createdAt" class="meta-item">
            📅 {{ new Date(project.createdAt).toLocaleDateString('zh-CN') }}
          </span>
          <span v-if="project.fundingRound" class="meta-item">
            💰 {{ project.fundingRound }}
          </span>
        </div>
      </div>

      <!-- 主要内容 -->
      <div class="detail-body">
        <div class="detail-content">
          <!-- 痛点描述 -->
          <section v-if="project.painPoint" class="content-section">
            <h3>🎯 市场痛点</h3>
            <p>{{ project.painPoint }}</p>
          </section>

          <!-- 商业模式 -->
          <section v-if="project.businessModel" class="content-section">
            <h3>💼 商业模式</h3>
            <p>{{ project.businessModel }}</p>
          </section>

          <!-- 核心技术 -->
          <section v-if="project.coreTech" class="content-section">
            <h3>🔬 核心技术</h3>
            <p>{{ project.coreTech }}</p>
          </section>

          <!-- 目标市场 -->
          <section v-if="project.targetMarket" class="content-section">
            <h3>🎯 目标市场</h3>
            <p>{{ project.targetMarket }}</p>
          </section>

          <!-- 资金用途 -->
          <section v-if="project.fundUsage" class="content-section">
            <h3>💸 资金用途</h3>
            <p>{{ project.fundUsage }}</p>
          </section>
        </div>

        <!-- 侧边栏 -->
        <div class="detail-sidebar">
          <!-- 融资信息 -->
          <div class="sidebar-card">
            <h3>融资信息</h3>
            <div class="info-list">
              <div v-if="project.fundingRound" class="info-item">
                <span class="info-label">融资轮次</span>
                <span class="info-value">{{ project.fundingRound }}</span>
              </div>
              <div v-if="project.fundingAmount" class="info-item">
                <span class="info-label">融资金额</span>
                <span class="info-value accent">{{ project.fundingAmount }}</span>
              </div>
              <div v-if="project.valuation" class="info-item">
                <span class="info-label">估值</span>
                <span class="info-value">{{ project.valuation }}</span>
              </div>
              <div v-if="project.equityPercent" class="info-item">
                <span class="info-label">出让股权</span>
                <span class="info-value">{{ project.equityPercent }}</span>
              </div>
            </div>
          </div>

          <!-- 运营数据 -->
          <div v-if="project.userCount || project.dauMau || project.mrr || project.arr" class="sidebar-card">
            <h3>运营数据</h3>
            <div class="info-list">
              <div v-if="project.userCount" class="info-item">
                <span class="info-label">用户总数</span>
                <span class="info-value">{{ project.userCount.toLocaleString() }}</span>
              </div>
              <div v-if="project.dauMau" class="info-item">
                <span class="info-label">DAU/MAU</span>
                <span class="info-value">{{ project.dauMau }}</span>
              </div>
              <div v-if="project.mrr" class="info-item">
                <span class="info-label">MRR</span>
                <span class="info-value">{{ project.mrr }}</span>
              </div>
              <div v-if="project.arr" class="info-item">
                <span class="info-label">ARR</span>
                <span class="info-value">{{ project.arr }}</span>
              </div>
            </div>
          </div>

          <!-- 联系按钮 -->
          <div class="sidebar-card contact-card">
            <h3>感兴趣？</h3>
            <p>联系创业者了解更多详情</p>
            <button class="btn-contact">发送投资意向</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.project-detail-page {
  min-height: 100vh;
  background: var(--ink);
  position: relative;
  overflow: hidden;
  padding: 6rem 4rem 4rem;
}

.detail-bg {
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
    radial-gradient(ellipse at 80% 60%, rgba(116, 185, 255, 0.08) 0%, transparent 50%);
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

.loading-state,
.error-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-height: 60vh;
  color: white;
  position: relative;
  z-index: 10;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.1);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 1rem;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.error-icon {
  width: 60px;
  height: 60px;
  background: var(--accent);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 2rem;
  font-weight: 700;
  margin-bottom: 1.5rem;
}

.error-state h2 {
  font-size: 1.5rem;
  margin-bottom: 2rem;
}

.detail-container {
  max-width: 1200px;
  margin: 0 auto;
  position: relative;
  z-index: 10;
}

.btn-back {
  background: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
  color: white;
  padding: 0.75rem 1.5rem;
  border-radius: 10px;
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-bottom: 2rem;
}

.btn-back:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: var(--accent);
}

.detail-header {
  margin-bottom: 3rem;
}

.project-badge {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 1.5rem;
}

.badge-industry,
.badge-stage {
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 600;
}

.badge-industry {
  background: rgba(233, 69, 96, 0.2);
  color: var(--accent);
}

.badge-stage {
  background: rgba(116, 185, 255, 0.2);
  color: #74b9ff;
}

.detail-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: white;
  margin-bottom: 1rem;
}

.project-desc {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
  line-height: 1.8;
  margin-bottom: 1.5rem;
}

.project-meta {
  display: flex;
  gap: 2rem;
  flex-wrap: wrap;
}

.meta-item {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.95rem;
}

.detail-body {
  display: grid;
  grid-template-columns: 1fr 350px;
  gap: 2rem;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.content-section {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 2rem;
}

.content-section h3 {
  color: white;
  font-size: 1.2rem;
  margin-bottom: 1rem;
}

.content-section p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.95rem;
  line-height: 1.8;
}

.detail-sidebar {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.sidebar-card {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 1.5rem;
}

.sidebar-card h3 {
  color: white;
  font-size: 1.1rem;
  margin-bottom: 1.25rem;
}

.info-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-label {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.9rem;
}

.info-value {
  color: white;
  font-weight: 600;
}

.info-value.accent {
  color: var(--accent);
}

.contact-card {
  text-align: center;
}

.contact-card p {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.9rem;
  margin-bottom: 1.5rem;
}

.btn-contact {
  width: 100%;
  background: var(--accent);
  color: white;
  border: none;
  padding: 1rem;
  border-radius: 12px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-contact:hover {
  background: var(--accent-warm);
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(233, 69, 96, 0.3);
}

@media (max-width: 1024px) {
  .detail-body {
    grid-template-columns: 1fr;
  }

  .project-detail-page {
    padding: 6rem 2rem 2rem;
  }
}

@media (max-width: 640px) {
  .detail-header h1 {
    font-size: 1.8rem;
  }

  .project-meta {
    flex-direction: column;
    gap: 0.75rem;
  }
}
</style>
