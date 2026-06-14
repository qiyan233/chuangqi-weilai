<script setup>
import { ref, computed, onMounted } from 'vue'
import gsap from 'gsap'
import api from '@/api'

const applications = ref([])
const statusFilter = ref('')
const reviewModal = ref(false)
const reviewingApp = ref(null)
const reviewStatus = ref('APPROVED')
const reviewNotes = ref('')
const loading = ref(true)

const filteredApplications = computed(() => {
  if (!statusFilter.value) return applications.value
  return applications.value.filter(a => a.status === statusFilter.value)
})

const statusBadgeClass = (status) => ({
  'badge-pending': status === 'PENDING',
  'badge-reviewing': status === 'REVIEWING',
  'badge-interview': status === 'INTERVIEW',
  'badge-approved': status === 'APPROVED',
  'badge-rejected': status === 'REJECTED',
})

const statusLabel = (status) => ({
  PENDING: '待审核', REVIEWING: '审核中', INTERVIEW: '面试中',
  APPROVED: '已通过', REJECTED: '已拒绝',
}[status] || status)

async function fetchApplications() {
  try {
    const params = statusFilter.value ? { status: statusFilter.value } : {}
    const res = await api.get('/admin/applications', { params })
    applications.value = res.data
  } catch (e) {
    console.error('加载申请列表失败:', e)
  } finally {
    loading.value = false
    gsap.from('tr', {
      opacity: 0, y: 20, duration: 0.4, stagger: 0.05, ease: 'power2.out', delay: 0.1,
    })
  }
}

onMounted(fetchApplications)

function openReview(app) {
  reviewingApp.value = app
  reviewStatus.value = 'APPROVED'
  reviewNotes.value = ''
  reviewModal.value = true
}

function closeReview() {
  reviewModal.value = false
  reviewingApp.value = null
}

async function submitReview() {
  if (!reviewingApp.value) return
  try {
    await api.put(`/admin/applications/${reviewingApp.value.id}/review`, {
      status: reviewStatus.value,
      reviewNotes: reviewNotes.value,
    })
    const a = applications.value.find(a => a.id === reviewingApp.value.id)
    if (a) a.status = reviewStatus.value
    closeReview()
  } catch (e) {
    console.error('审核失败:', e)
  }
}

function setFilter(status) {
  statusFilter.value = status
  loading.value = true
  fetchApplications()
}

const filterTabs = [
  { key: '', label: '全部' },
  { key: 'PENDING', label: '待审核' },
  { key: 'REVIEWING', label: '审核中' },
  { key: 'APPROVED', label: '已通过' },
  { key: 'REJECTED', label: '已拒绝' },
]
</script>

<template>
  <div class="admin-page">
    <div class="page-header">
      <div>
        <h1>申请管理</h1>
        <p>{{ filteredApplications.length }} 条入驻申请</p>
      </div>
    </div>

    <div class="filter-tabs">
      <button
        v-for="tab in filterTabs" :key="tab.key"
        class="filter-tab" :class="{ active: statusFilter === tab.key }"
        @click="setFilter(tab.key)"
      >
        {{ tab.label }}
      </button>
    </div>

    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>申请人</th>
            <th>关联项目</th>
            <th>团队成员</th>
            <th>股权结构</th>
            <th>状态</th>
            <th>申请时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="app in filteredApplications" :key="app.id">
            <td class="td-id">{{ app.id }}</td>
            <td class="td-name">{{ app.userName || app.user?.realName || app.user?.username || '-' }}</td>
            <td>{{ app.projectName || app.project?.name || '-' }}</td>
            <td>{{ app.teamMembers?.substring(0, 30) || '-' }}{{ app.teamMembers?.length > 30 ? '...' : '' }}</td>
            <td>{{ app.equityStructure?.substring(0, 30) || '-' }}{{ app.equityStructure?.length > 30 ? '...' : '' }}</td>
            <td>
              <span class="status-badge" :class="statusBadgeClass(app.status)">
                {{ statusLabel(app.status) }}
              </span>
            </td>
            <td class="td-date">{{ app.createdAt?.substring(0, 10) }}</td>
            <td class="td-actions">
              <button
                v-if="app.status === 'PENDING' || app.status === 'REVIEWING'"
                class="btn-review"
                @click="openReview(app)"
              >
                审核
              </button>
              <span v-else class="no-action">-</span>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredApplications.length === 0 && !loading" class="empty-state">
        暂无申请
      </div>
    </div>

    <!-- 审核弹窗 -->
    <div v-if="reviewModal" class="modal-overlay" @click.self="closeReview">
      <div class="modal-content">
        <h2>审核申请</h2>
        <div class="review-info">
          <div class="info-row"><span>申请人</span><strong>{{ reviewingApp?.userName || reviewingApp?.user?.realName || '-' }}</strong></div>
          <div class="info-row"><span>关联项目</span><strong>{{ reviewingApp?.projectName || '-' }}</strong></div>
          <div class="info-row"><span>团队成员</span><strong>{{ reviewingApp?.teamMembers || '-' }}</strong></div>
          <div class="info-row"><span>股权结构</span><strong>{{ reviewingApp?.equityStructure || '-' }}</strong></div>
        </div>

        <div class="form-group">
          <label>审核结果</label>
          <div class="review-actions-radio">
            <label class="radio-label" :class="{ selected: reviewStatus === 'APPROVED' }">
              <input v-model="reviewStatus" type="radio" value="APPROVED" />
              <span class="radio-text">✅ 通过</span>
            </label>
            <label class="radio-label" :class="{ selected: reviewStatus === 'REJECTED' }">
              <input v-model="reviewStatus" type="radio" value="REJECTED" />
              <span class="radio-text">❌ 拒绝</span>
            </label>
            <label class="radio-label" :class="{ selected: reviewStatus === 'INTERVIEW' }">
              <input v-model="reviewStatus" type="radio" value="INTERVIEW" />
              <span class="radio-text">📞 进入面试</span>
            </label>
          </div>
        </div>

        <div class="form-group">
          <label>审核备注</label>
          <textarea v-model="reviewNotes" placeholder="输入审核意见..." rows="4" />
        </div>

        <div class="modal-actions">
          <button class="btn-cancel" @click="closeReview">取消</button>
          <button class="btn-submit" @click="submitReview">提交审核</button>
        </div>
      </div>
    </div>

    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner" />
    </div>
  </div>
</template>

<style scoped>
.admin-page {
  min-height: 100vh;
  background: var(--ink);
  padding: 8rem 4rem 4rem;
}

.page-header { margin-bottom: 2rem; }

.page-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: white;
  margin-bottom: 0.3rem;
}

.page-header p { color: rgba(255,255,255,0.45); font-size: 0.95rem; }

/* Filter Tabs */
.filter-tabs {
  display: flex;
  gap: 0.75rem;
  margin-bottom: 2rem;
  flex-wrap: wrap;
}

.filter-tab {
  padding: 0.6rem 1.25rem;
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.08);
  border-radius: 10px;
  color: rgba(255,255,255,0.6);
  font-size: 0.9rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.filter-tab:hover { border-color: rgba(255,255,255,0.2); color: white; }

.filter-tab.active {
  background: rgba(233,69,96,0.15);
  border-color: var(--accent);
  color: white;
}

/* Table */
.table-container {
  background: rgba(255,255,255,0.03);
  border: 1px solid rgba(255,255,255,0.06);
  border-radius: 16px;
  overflow: hidden;
}

table { width: 100%; border-collapse: collapse; }
thead { background: rgba(255,255,255,0.04); }

th {
  padding: 1rem 1.25rem;
  text-align: left;
  color: rgba(255,255,255,0.5);
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

td {
  padding: 1rem 1.25rem;
  color: rgba(255,255,255,0.8);
  font-size: 0.9rem;
  border-top: 1px solid rgba(255,255,255,0.04);
}

.td-id { color: rgba(255,255,255,0.35); }
.td-name { color: white; font-weight: 500; }
.td-date { color: rgba(255,255,255,0.35); font-size: 0.8rem; white-space: nowrap; }

tr:hover td { background: rgba(255,255,255,0.02); }

/* Status Badges */
.status-badge {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.78rem;
  font-weight: 500;
}

.badge-pending { background: rgba(253,203,110,0.15); color: #fdcb6e; }
.badge-reviewing { background: rgba(116,185,255,0.15); color: #74b9ff; }
.badge-interview { background: rgba(162,155,254,0.15); color: #a29bfe; }
.badge-approved { background: rgba(0,184,148,0.15); color: #00b894; }
.badge-rejected { background: rgba(233,69,96,0.15); color: #e94560; }

.btn-review {
  padding: 0.4rem 0.9rem;
  background: rgba(233,69,96,0.15);
  color: var(--accent);
  border: 1px solid rgba(233,69,96,0.3);
  border-radius: 8px;
  font-size: 0.8rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.btn-review:hover { background: var(--accent); color: white; }
.no-action { color: rgba(255,255,255,0.2); }

/* Modal */
.modal-overlay {
  position: fixed; inset: 0;
  background: rgba(0,0,0,0.7);
  backdrop-filter: blur(5px);
  display: flex; align-items: center; justify-content: center;
  z-index: 1000; padding: 2rem;
}

.modal-content {
  background: #1a1a2e;
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 20px;
  padding: 2.5rem;
  max-width: 600px;
  width: 100%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-content h2 {
  font-family: 'Noto Serif SC', serif;
  color: white;
  margin-bottom: 1.5rem;
}

.review-info {
  background: rgba(255,255,255,0.05);
  border-radius: 12px;
  padding: 1.25rem 1.5rem;
  margin-bottom: 1.5rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid rgba(255,255,255,0.05);
}

.info-row:last-child { border-bottom: none; }
.info-row span { color: rgba(255,255,255,0.4); font-size: 0.85rem; }
.info-row strong { color: rgba(255,255,255,0.85); font-size: 0.85rem; max-width: 60%; text-align: right; }

.form-group { margin-bottom: 1.5rem; }
.form-group label { display: block; color: rgba(255,255,255,0.7); margin-bottom: 0.75rem; font-size: 0.9rem; }

.review-actions-radio { display: flex; gap: 0.75rem; }

.radio-label {
  flex: 1;
  padding: 0.85rem 0.5rem;
  background: rgba(255,255,255,0.05);
  border: 2px solid rgba(255,255,255,0.1);
  border-radius: 12px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.radio-label input { display: none; }
.radio-text { color: rgba(255,255,255,0.7); font-size: 0.85rem; }
.radio-label.selected { border-color: var(--accent); background: rgba(233,69,96,0.1); }
.radio-label.selected .radio-text { color: white; }

textarea {
  width: 100%;
  padding: 1rem;
  background: rgba(255,255,255,0.05);
  border: 1px solid rgba(255,255,255,0.1);
  border-radius: 12px;
  color: white;
  font-size: 0.9rem;
  resize: vertical;
  font-family: inherit;
}

textarea:focus { outline: none; border-color: var(--accent); }
textarea::placeholder { color: rgba(255,255,255,0.3); }

.modal-actions { display: flex; gap: 1rem; justify-content: flex-end; }

.btn-cancel, .btn-submit {
  padding: 0.75rem 2rem;
  border-radius: 10px;
  font-size: 0.9rem;
  font-weight: 500;
  cursor: pointer;
  border: none;
  transition: all 0.3s ease;
}

.btn-cancel { background: rgba(255,255,255,0.08); color: rgba(255,255,255,0.6); }
.btn-cancel:hover { background: rgba(255,255,255,0.15); color: white; }
.btn-submit { background: var(--accent); color: white; }
.btn-submit:hover { background: var(--accent-warm); }

.empty-state { text-align: center; padding: 4rem; color: rgba(255,255,255,0.3); }

.loading-overlay {
  position: fixed; inset: 0;
  background: rgba(26,26,46,0.8);
  display: flex; align-items: center; justify-content: center; z-index: 100;
}

.loading-spinner {
  width: 40px; height: 40px;
  border: 3px solid rgba(255,255,255,0.2);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

@media (max-width: 1024px) {
  .admin-page { padding: 6rem 1.5rem 3rem; }
  .table-container { overflow-x: auto; }
  table { min-width: 700px; }
  .modal-content { padding: 1.5rem; }
}
</style>
