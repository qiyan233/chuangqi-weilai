<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import gsap from 'gsap'
import api from '@/api'

const projects = ref([])
const statusFilter = ref('')
const reviewModal = ref(false)
const reviewingProject = ref(null)
const reviewStatus = ref('APPROVED')
const reviewNotes = ref('')
const loading = ref(true)
const submitting = ref(false)

const filteredProjects = computed(() => {
  if (!statusFilter.value) return projects.value
  return projects.value.filter(p => p.status === statusFilter.value)
})

const statusBadgeClass = (status) => ({
  'badge-pending': status === 'PENDING',
  'badge-approved': status === 'APPROVED',
  'badge-rejected': status === 'REJECTED',
  'badge-draft': status === 'DRAFT',
})

const statusLabel = (status) => ({
  DRAFT: '草稿',
  PENDING: '待审核',
  APPROVED: '已通过',
  REJECTED: '已拒绝',
}[status] || status)

const filterTabs = [
  { key: '', label: '全部' },
  { key: 'PENDING', label: '待审核' },
  { key: 'APPROVED', label: '已通过' },
  { key: 'REJECTED', label: '已拒绝' },
]

const reviewStatusOptions = [
  { value: 'APPROVED', label: '通过' },
  { value: 'REJECTED', label: '拒绝' },
]

async function fetchProjects() {
  loading.value = true
  try {
    const params = statusFilter.value ? { status: statusFilter.value } : {}
    const res = await api.get('/admin/projects', { params })
    projects.value = res.data
    await nextTick()
    animateTableEntrance()
  } catch (e) {
    console.error('加载项目列表失败:', e)
  } finally {
    loading.value = false
  }
}

// ==================== GSAP 动画 ====================

function animatePageEntrance() {
  const tl = gsap.timeline()

  tl.from('.page-header', {
    y: 40,
    opacity: 0,
    duration: 0.7,
    ease: 'power3.out',
  })

  tl.from('.filter-tab', {
    y: 20,
    opacity: 0,
    duration: 0.4,
    stagger: 0.06,
    ease: 'power2.out',
  }, '-=0.3')

  tl.from('.table-container', {
    y: 30,
    opacity: 0,
    duration: 0.6,
    ease: 'power3.out',
  }, '-=0.2')
}

function animateTableEntrance() {
  gsap.from('tbody tr', {
    opacity: 0,
    y: 24,
    duration: 0.45,
    stagger: 0.05,
    ease: 'power2.out',
    delay: 0.1,
  })
}

function animateFilterSwitch() {
  gsap.fromTo('tbody tr', {
    opacity: 0,
    y: 16,
  }, {
    opacity: 1,
    y: 0,
    duration: 0.4,
    stagger: 0.05,
    ease: 'power2.out',
  })
}

function animateModalIn() {
  nextTick(() => {
    gsap.from('.modal-overlay', {
      opacity: 0,
      duration: 0.3,
      ease: 'power2.out',
    })
    gsap.from('.modal-content', {
      scale: 0.92,
      y: 40,
      opacity: 0,
      duration: 0.45,
      ease: 'back.out(1.5)',
      delay: 0.05,
    })
  })
}

function animateModalOut(callback) {
  const tl = gsap.timeline({
    onComplete: callback,
  })
  tl.to('.modal-content', {
    scale: 0.92,
    y: 30,
    opacity: 0,
    duration: 0.25,
    ease: 'power2.in',
  })
  tl.to('.modal-overlay', {
    opacity: 0,
    duration: 0.2,
    ease: 'power2.out',
  }, '-=0.15')
}

onMounted(async () => {
  await fetchProjects()
  animatePageEntrance()
})

// ==================== 审核逻辑 ====================

function openReview(project) {
  reviewingProject.value = project
  reviewStatus.value = 'APPROVED'
  reviewNotes.value = ''
  reviewModal.value = true
  animateModalIn()
}

function closeReview() {
  animateModalOut(() => {
    reviewModal.value = false
    reviewingProject.value = null
  })
}

async function submitReview() {
  if (!reviewingProject.value) return
  submitting.value = true
  try {
    await api.put(`/admin/projects/${reviewingProject.value.id}/review`, {
      status: reviewStatus.value,
      reviewNotes: reviewNotes.value,
    })
    const p = projects.value.find(p => p.id === reviewingProject.value.id)
    if (p) {
      p.status = reviewStatus.value
      p.reviewNotes = reviewNotes.value
    }
    submitting.value = false
    closeReview()
  } catch (e) {
    submitting.value = false
    console.error('审核失败:', e)
    alert('审核操作失败，请稍后重试')
  }
}

function setFilter(status) {
  if (statusFilter.value === status) return
  statusFilter.value = status
  fetchProjects()
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  return dateStr.substring(0, 10)
}
</script>

<template>
  <div class="admin-page">
    <!-- 背景装饰 -->
    <div class="page-bg">
      <div class="bg-gradient" />
      <div class="bg-grid" />
      <div class="bg-glow" />
    </div>

    <div class="content-wrapper">
      <!-- 头部 -->
      <div class="page-header">
        <h1>项目管理</h1>
        <p class="header-count">{{ filteredProjects.length }} 个项目</p>
      </div>

      <!-- 状态筛选 -->
      <div class="filter-tabs">
        <button
          v-for="tab in filterTabs"
          :key="tab.key"
          class="filter-tab"
          :class="{ active: statusFilter === tab.key }"
          @click="setFilter(tab.key)"
        >
          {{ tab.label }}
        </button>
      </div>

      <!-- 项目列表 -->
      <div class="table-container">
        <table>
          <thead>
            <tr>
              <th>项目名称</th>
              <th>创业者</th>
              <th>行业</th>
              <th>阶段</th>
              <th>状态</th>
              <th>创建时间</th>
              <th>操作</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="project in filteredProjects" :key="project.id">
              <td class="td-name">{{ project.name }}</td>
              <td>{{ project.userName || project.user?.username || '-' }}</td>
              <td>{{ project.industry || '-' }}</td>
              <td>{{ project.stage || '-' }}</td>
              <td>
                <span class="status-badge" :class="statusBadgeClass(project.status)">
                  {{ statusLabel(project.status) }}
                </span>
              </td>
              <td class="td-date">{{ formatDate(project.createdAt) }}</td>
              <td class="td-actions">
                <button
                  v-if="project.status === 'PENDING'"
                  class="btn-review"
                  @click="openReview(project)"
                >
                  审核
                </button>
                <span v-else class="no-action">-</span>
              </td>
            </tr>
          </tbody>
        </table>

        <div v-if="filteredProjects.length === 0 && !loading" class="empty-state">
          <span class="empty-icon">📋</span>
          <p>暂无项目数据</p>
        </div>
      </div>
    </div>

    <!-- 审核弹窗 -->
    <Teleport to="body">
      <div v-if="reviewModal" class="modal-overlay" @click.self="closeReview">
        <div class="modal-content">
          <div class="modal-header">
            <h2>审核项目</h2>
            <button class="modal-close" @click="closeReview" aria-label="关闭">
              <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                <path d="M15 5L5 15M5 5l10 10" stroke="currentColor" stroke-width="2" stroke-linecap="round" />
              </svg>
            </button>
          </div>

          <!-- 项目摘要 -->
          <div class="review-project-summary">
            <h3>{{ reviewingProject?.name }}</h3>
            <p class="project-desc">{{ reviewingProject?.description || '暂无项目描述' }}</p>
            <div class="project-meta-grid">
              <div class="meta-item">
                <span class="meta-label">行业</span>
                <span class="meta-value">{{ reviewingProject?.industry || '-' }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">阶段</span>
                <span class="meta-value">{{ reviewingProject?.stage || '-' }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">创业者</span>
                <span class="meta-value">{{ reviewingProject?.userName || reviewingProject?.user?.username || '-' }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">当前状态</span>
                <span
                  class="status-badge"
                  :class="statusBadgeClass(reviewingProject?.status)"
                >
                  {{ statusLabel(reviewingProject?.status) }}
                </span>
              </div>
            </div>
          </div>

          <!-- 审核表单 -->
          <div class="form-group">
            <label class="form-label">审核结果</label>
            <select v-model="reviewStatus" class="form-select-glass">
              <option
                v-for="opt in reviewStatusOptions"
                :key="opt.value"
                :value="opt.value"
              >
                {{ opt.label }}
              </option>
            </select>
          </div>

          <div class="form-group">
            <label class="form-label">审核备注</label>
            <textarea
              v-model="reviewNotes"
              class="form-textarea-glass"
              placeholder="请输入审核意见..."
              rows="5"
            />
          </div>

          <div class="modal-actions">
            <button class="btn-cancel" @click="closeReview">取消</button>
            <button
              class="btn-submit"
              :class="{ loading: submitting }"
              :disabled="submitting"
              @click="submitReview"
            >
              <span v-if="submitting" class="spinner" />
              <span v-else>提交审核</span>
            </button>
          </div>
        </div>
      </div>
    </Teleport>

    <!-- 加载遮罩 -->
    <Teleport to="body">
      <div v-if="loading" class="loading-overlay">
        <div class="loading-spinner" />
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
/* ==================== 布局 ==================== */
.admin-page {
  min-height: 100vh;
  background: var(--ink);
  position: relative;
  overflow: hidden;
}

.content-wrapper {
  max-width: 1280px;
  margin: 0 auto;
  padding: 6rem 3rem 4rem;
  position: relative;
  z-index: 10;
}

/* ==================== 背景 ==================== */
.page-bg {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 25% 0%, rgba(233, 69, 96, 0.08) 0%, transparent 50%),
    radial-gradient(ellipse at 70% 50%, rgba(0, 184, 148, 0.05) 0%, transparent 55%),
    radial-gradient(ellipse at 40% 100%, rgba(116, 185, 255, 0.06) 0%, transparent 50%);
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.02) 1px, transparent 1px);
  background-size: 60px 60px;
}

.bg-glow {
  position: absolute;
  top: 30%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.08) 0%, transparent 70%);
  animation: glowPulse 10s ease-in-out infinite;
}

@keyframes glowPulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.5; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.8; }
}

/* ==================== 头部 ==================== */
.page-header {
  margin-bottom: 2.5rem;
}

.page-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: white;
  margin-bottom: 0.4rem;
  letter-spacing: 0.03em;
}

.header-count {
  color: rgba(255, 255, 255, 0.4);
  font-size: 0.95rem;
}

/* ==================== 筛选标签 ==================== */
.filter-tabs {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.filter-tab {
  padding: 0.6rem 1.4rem;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 50px;
  color: rgba(255, 255, 255, 0.55);
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  backdrop-filter: blur(10px);
}

.filter-tab:hover {
  border-color: rgba(255, 255, 255, 0.2);
  color: rgba(255, 255, 255, 0.85);
  background: rgba(255, 255, 255, 0.07);
}

.filter-tab.active {
  background: rgba(233, 69, 96, 0.15);
  border-color: rgba(233, 69, 96, 0.4);
  color: white;
  box-shadow: 0 0 20px rgba(233, 69, 96, 0.1);
}

/* ==================== 表格容器 ==================== */
.table-container {
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(30px);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 20px;
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: rgba(255, 255, 255, 0.03);
}

th {
  padding: 1.1rem 1.25rem;
  text-align: left;
  color: rgba(255, 255, 255, 0.4);
  font-size: 0.78rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.06em;
  white-space: nowrap;
}

td {
  padding: 1.05rem 1.25rem;
  color: rgba(255, 255, 255, 0.75);
  font-size: 0.9rem;
  border-top: 1px solid rgba(255, 255, 255, 0.04);
  transition: background 0.2s ease;
}

.td-name {
  color: white;
  font-weight: 500;
}

.td-date {
  color: rgba(255, 255, 255, 0.35);
  font-size: 0.83rem;
  white-space: nowrap;
}

tr:hover td {
  background: rgba(255, 255, 255, 0.02);
}

/* ==================== 状态标签 ==================== */
.status-badge {
  display: inline-block;
  padding: 0.28rem 0.85rem;
  border-radius: 50px;
  font-size: 0.76rem;
  font-weight: 500;
  white-space: nowrap;
  letter-spacing: 0.02em;
}

.badge-pending {
  background: rgba(253, 203, 110, 0.14);
  color: #fdcb6e;
  border: 1px solid rgba(253, 203, 110, 0.25);
}

.badge-approved {
  background: rgba(0, 184, 148, 0.14);
  color: #00b894;
  border: 1px solid rgba(0, 184, 148, 0.25);
}

.badge-rejected {
  background: rgba(233, 69, 96, 0.14);
  color: #e94560;
  border: 1px solid rgba(233, 69, 96, 0.25);
}

.badge-draft {
  background: rgba(255, 255, 255, 0.06);
  color: rgba(255, 255, 255, 0.45);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

/* ==================== 审核按钮 ==================== */
.btn-review {
  padding: 0.4rem 1rem;
  background: rgba(233, 69, 96, 0.12);
  color: var(--accent);
  border: 1px solid rgba(233, 69, 96, 0.25);
  border-radius: 50px;
  font-size: 0.8rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-review:hover {
  background: var(--accent);
  color: white;
  border-color: var(--accent);
  box-shadow: 0 4px 16px rgba(233, 69, 96, 0.3);
}

.no-action {
  color: rgba(255, 255, 255, 0.15);
}

/* ==================== 空状态 ==================== */
.empty-state {
  text-align: center;
  padding: 5rem 2rem;
  color: rgba(255, 255, 255, 0.25);
}

.empty-icon {
  font-size: 2.5rem;
  display: block;
  margin-bottom: 0.75rem;
}

.empty-state p {
  font-size: 0.95rem;
}

/* ==================== 弹窗 ==================== */
.modal-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.65);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
  padding: 1.5rem;
}

.modal-content {
  background: linear-gradient(160deg, rgba(26, 26, 46, 0.95), rgba(22, 33, 62, 0.98));
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 24px;
  padding: 2.5rem;
  max-width: 620px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
  box-shadow:
    0 25px 80px rgba(0, 0, 0, 0.5),
    0 0 0 1px rgba(255, 255, 255, 0.05) inset;
  backdrop-filter: blur(30px);
}

.modal-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 1.75rem;
}

.modal-header h2 {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.6rem;
  color: white;
}

.modal-close {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border: 1px solid rgba(255, 255, 255, 0.1);
  background: rgba(255, 255, 255, 0.05);
  color: rgba(255, 255, 255, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 0;
  flex-shrink: 0;
}

.modal-close:hover {
  background: rgba(233, 69, 96, 0.15);
  border-color: rgba(233, 69, 96, 0.4);
  color: var(--accent);
}

/* ==================== 项目摘要 ==================== */
.review-project-summary {
  background: rgba(255, 255, 255, 0.03);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.review-project-summary h3 {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.2rem;
  color: white;
  margin-bottom: 0.5rem;
}

.project-desc {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.88rem;
  line-height: 1.7;
  margin-bottom: 1.25rem;
}

.project-meta-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 0.85rem;
}

.meta-item {
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.meta-label {
  font-size: 0.75rem;
  color: rgba(255, 255, 255, 0.35);
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

.meta-value {
  font-size: 0.9rem;
  color: rgba(255, 255, 255, 0.8);
}

/* ==================== 表单 ==================== */
.form-group {
  margin-bottom: 1.5rem;
}

.form-label {
  display: block;
  color: rgba(255, 255, 255, 0.65);
  font-size: 0.85rem;
  font-weight: 500;
  margin-bottom: 0.6rem;
}

.form-select-glass {
  width: 100%;
  padding: 0.85rem 1rem;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 0.95rem;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.3s ease;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='14' height='14' viewBox='0 0 24 24' fill='none' stroke='rgba(255,255,255,0.4)' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpolyline points='6 9 12 15 18 9'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 1rem center;
  padding-right: 2.5rem;
}

.form-select-glass:focus {
  outline: none;
  border-color: rgba(233, 69, 96, 0.5);
  box-shadow: 0 0 0 3px rgba(233, 69, 96, 0.08);
  background: rgba(255, 255, 255, 0.06);
}

.form-select-glass option {
  background: #1a1a2e;
  color: white;
  padding: 0.5rem;
}

.form-textarea-glass {
  width: 100%;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 0.93rem;
  font-family: inherit;
  resize: vertical;
  transition: all 0.3s ease;
  line-height: 1.7;
}

.form-textarea-glass:focus {
  outline: none;
  border-color: rgba(233, 69, 96, 0.5);
  box-shadow: 0 0 0 3px rgba(233, 69, 96, 0.08);
  background: rgba(255, 255, 255, 0.06);
}

.form-textarea-glass::placeholder {
  color: rgba(255, 255, 255, 0.25);
}

/* ==================== 弹窗按钮 ==================== */
.modal-actions {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 0.5rem;
}

.btn-cancel,
.btn-submit {
  padding: 0.75rem 2rem;
  border-radius: 50px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
  font-family: inherit;
}

.btn-cancel {
  background: rgba(255, 255, 255, 0.06);
  color: rgba(255, 255, 255, 0.55);
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.btn-cancel:hover {
  background: rgba(255, 255, 255, 0.12);
  color: white;
}

.btn-submit {
  background: linear-gradient(135deg, var(--accent), var(--accent-warm));
  color: white;
  box-shadow: 0 6px 24px rgba(233, 69, 96, 0.3);
  min-width: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
}

.btn-submit:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 10px 32px rgba(233, 69, 96, 0.4);
}

.btn-submit:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.7s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* ==================== 加载遮罩 ==================== */
.loading-overlay {
  position: fixed;
  inset: 0;
  background: rgba(8, 8, 20, 0.7);
  backdrop-filter: blur(4px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 500;
}

.loading-spinner {
  width: 44px;
  height: 44px;
  border: 3px solid rgba(255, 255, 255, 0.12);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

/* ==================== 响应式 ==================== */
@media (max-width: 1024px) {
  .content-wrapper {
    padding: 5rem 1.5rem 3rem;
  }

  .table-container {
    overflow-x: auto;
  }

  table {
    min-width: 720px;
  }
}

@media (max-width: 768px) {
  .project-meta-grid {
    grid-template-columns: 1fr;
  }

  .modal-content {
    padding: 1.75rem;
  }

  .modal-actions {
    flex-direction: column;
  }

  .btn-cancel,
  .btn-submit {
    width: 100%;
    text-align: center;
  }
}

@media (max-width: 480px) {
  .content-wrapper {
    padding: 4rem 1rem 2.5rem;
  }

  .page-header h1 {
    font-size: 1.8rem;
  }

  .filter-tab {
    padding: 0.5rem 1rem;
    font-size: 0.82rem;
  }
}
</style>
