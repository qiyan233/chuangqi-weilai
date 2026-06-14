<script setup>
import { ref, computed, onMounted } from 'vue'
import gsap from 'gsap'
import api from '@/api'

const users = ref([])
const searchQuery = ref('')
const loading = ref(true)
const error = ref('')

// "编辑角色" 下拉状态
const editingUserId = ref(null)

const filteredUsers = computed(() => {
  if (!searchQuery.value) return users.value
  const q = searchQuery.value.toLowerCase()
  return users.value.filter(u =>
    u.username?.toLowerCase().includes(q) ||
    u.email?.toLowerCase().includes(q) ||
    u.realName?.toLowerCase().includes(q)
  )
})

onMounted(async () => {
  // 入场动画 —— 先启动，不等待 API
  const tl = gsap.timeline()

  tl.from('.admin-page', {
    y: 40,
    opacity: 0,
    duration: 0.8,
    ease: 'power3.out',
  })

  tl.from('.page-header h1', {
    y: 30,
    opacity: 0,
    duration: 0.6,
    ease: 'power2.out',
  }, '-=0.4')

  tl.from('.page-header p, .search-bar', {
    y: 20,
    opacity: 0,
    duration: 0.5,
    ease: 'power2.out',
  }, '-=0.3')

  // 获取用户列表
  try {
    const res = await api.get('/admin/users')
    users.value = res.data
  } catch (e) {
    error.value = e.response?.data?.message || '用户数据加载失败，请稍后重试'
  } finally {
    loading.value = false
    animateRows()
  }
})

function animateRows() {
  gsap.from('tbody tr', {
    opacity: 0,
    y: 20,
    duration: 0.4,
    stagger: 0.05,
    ease: 'power2.out',
    delay: 0.1,
  })
}

// ---- 角色下拉 ----
function openRoleDropdown(user) {
  // 切换：如果同一个用户，关闭；否则打开
  if (editingUserId.value === user.id) {
    editingUserId.value = null
  } else {
    editingUserId.value = user.id
  }
}

function closeRoleDropdown() {
  editingUserId.value = null
}

async function updateRole(user, role) {
  if (role === user.role) {
    closeRoleDropdown()
    return
  }
  try {
    await api.put(`/admin/users/${user.id}/role`, { role })
    user.role = role
    closeRoleDropdown()
  } catch (e) {
    console.error('更新角色失败:', e)
  }
}

// ---- 辅助 ----
const roleBadgeClass = (role) => ({
  'badge-entrepreneur': role === 'ENTREPRENEUR',
  'badge-investor': role === 'INVESTOR',
  'badge-admin': role === 'ADMIN',
})

const roleLabel = (role) => ({
  ENTREPRENEUR: '创业者',
  INVESTOR: '投资人',
  ADMIN: '管理员',
}[role] || role)

const roleOptions = ['ENTREPRENEUR', 'INVESTOR', 'ADMIN']
</script>

<template>
  <div class="admin-page">
    <!-- 背景效果 — 与 Dashboard 一致的玻璃态背景 -->
    <div class="page-bg">
      <div class="bg-gradient" />
      <div class="bg-grid" />
      <div class="bg-glow" />
    </div>

    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1>用户管理</h1>
        <p>{{ filteredUsers.length }} 个用户</p>
      </div>
      <div class="search-bar">
        <span class="search-icon">🔍</span>
        <input
          v-model="searchQuery"
          type="text"
          placeholder="搜索用户名、邮箱或姓名..."
          class="search-input"
        />
      </div>
    </div>

    <!-- 错误提示 -->
    <div v-if="error" class="error-banner">
      <span class="error-icon">!</span>
      {{ error }}
    </div>

    <!-- 用户表格 -->
    <div class="table-container">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>邮箱</th>
            <th>角色</th>
            <th>姓名</th>
            <th>注册时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in filteredUsers" :key="user.id">
            <td class="td-id">{{ user.id }}</td>
            <td class="td-username">{{ user.username }}</td>
            <td class="td-email">{{ user.email }}</td>
            <td>
              <span class="role-badge" :class="roleBadgeClass(user.role)">
                {{ roleLabel(user.role) }}
              </span>
            </td>
            <td>{{ user.realName || '-' }}</td>
            <td class="td-date">{{ user.createdAt?.substring(0, 10) }}</td>
            <td class="td-actions">
              <div class="role-action-wrapper">
                <button
                  class="btn-edit-role"
                  @click.stop="openRoleDropdown(user)"
                >
                  编辑角色
                </button>
                <!-- 下拉菜单 -->
                <div
                  v-if="editingUserId === user.id"
                  class="role-dropdown"
                >
                  <button
                    v-for="role in roleOptions"
                    :key="role"
                    class="role-dropdown-item"
                    :class="{ active: role === user.role }"
                    @click.stop="updateRole(user, role)"
                  >
                    <span class="role-dropdown-dot" :class="'dot-' + role.toLowerCase()" />
                    <span>{{ roleLabel(role) }}</span>
                    <span v-if="role === user.role" class="role-dropdown-check">✓</span>
                  </button>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
      <div v-if="filteredUsers.length === 0 && !loading" class="empty-state">
        暂无用户数据
      </div>
    </div>

    <!-- 点击空白收起下拉 -->
    <div v-if="editingUserId" class="dropdown-backdrop" @click="closeRoleDropdown" />

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-overlay">
      <div class="loading-spinner" />
    </div>
  </div>
</template>

<style scoped>
/* ========== 全局背景 ========== */
.admin-page {
  min-height: 100vh;
  background: var(--ink);
  position: relative;
  overflow: hidden;
  padding: 4rem 3rem 5rem;
}

/* ---- 背景效果 (与 Dashboard 一致) ---- */
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

/* ========== 页面头部 ========== */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 2.5rem;
  flex-wrap: wrap;
  gap: 1.5rem;
  position: relative;
  z-index: 10;
}

.header-left h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: white;
  margin-bottom: 0.3rem;
  letter-spacing: 0.05em;
}

.header-left p {
  color: rgba(255, 255, 255, 0.45);
  font-size: 0.95rem;
}

/* ---- 搜索框 ---- */
.search-bar {
  position: relative;
  display: flex;
  align-items: center;
}

.search-icon {
  position: absolute;
  left: 1rem;
  font-size: 0.9rem;
  pointer-events: none;
}

.search-input {
  width: 320px;
  padding: 0.85rem 1rem 0.85rem 2.75rem;
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  font-family: inherit;
}

.search-input::placeholder { color: rgba(255, 255, 255, 0.3); }
.search-input:focus {
  outline: none;
  border-color: var(--accent);
  box-shadow: 0 0 0 3px rgba(233, 69, 96, 0.1);
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
  position: relative;
  z-index: 10;
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

/* ========== 表格容器 ========== */
.table-container {
  background: rgba(255, 255, 255, 0.03);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 16px;
  overflow: hidden;
  position: relative;
  z-index: 10;
}

table {
  width: 100%;
  border-collapse: collapse;
}

thead {
  background: rgba(255, 255, 255, 0.04);
}

th {
  padding: 1rem 1.25rem;
  text-align: left;
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.05em;
}

td {
  padding: 1rem 1.25rem;
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  border-top: 1px solid rgba(255, 255, 255, 0.04);
}

.td-id {
  color: rgba(255, 255, 255, 0.35);
  font-variant-numeric: tabular-nums;
}

.td-username {
  color: white;
  font-weight: 500;
}

.td-email {
  color: rgba(255, 255, 255, 0.55);
}

.td-date {
  color: rgba(255, 255, 255, 0.35);
  font-size: 0.8rem;
  white-space: nowrap;
}

tr:hover td {
  background: rgba(255, 255, 255, 0.02);
}

/* ========== 角色徽章 ========== */
.role-badge {
  display: inline-block;
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.78rem;
  font-weight: 500;
}

.badge-entrepreneur { background: rgba(116, 185, 255, 0.15); color: #74b9ff; }
.badge-investor { background: rgba(244, 162, 97, 0.15); color: #f4a261; }
.badge-admin { background: rgba(233, 69, 96, 0.15); color: #e94560; }

/* ========== 角色编辑下拉 ========== */
.td-actions {
  white-space: nowrap;
}

.role-action-wrapper {
  position: relative;
}

.btn-edit-role {
  padding: 0.4rem 0.9rem;
  background: rgba(255, 255, 255, 0.08);
  color: rgba(255, 255, 255, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 8px;
  font-size: 0.8rem;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
}

.btn-edit-role:hover {
  background: rgba(233, 69, 96, 0.2);
  border-color: var(--accent);
  color: white;
}

/* ---- 下拉菜单 ---- */
.role-dropdown {
  position: absolute;
  top: calc(100% + 6px);
  right: 0;
  min-width: 160px;
  background: rgba(20, 20, 40, 0.95);
  backdrop-filter: blur(30px);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: 12px;
  padding: 0.5rem;
  z-index: 100;
  box-shadow:
    0 12px 40px rgba(0, 0, 0, 0.5),
    0 0 30px rgba(233, 69, 96, 0.08);
  animation: dropdownIn 0.2s ease;
}

@keyframes dropdownIn {
  from { opacity: 0; transform: translateY(-6px) scale(0.95); }
  to   { opacity: 1; transform: translateY(0) scale(1); }
}

.role-dropdown-item {
  width: 100%;
  display: flex;
  align-items: center;
  gap: 0.65rem;
  padding: 0.6rem 0.75rem;
  background: transparent;
  border: none;
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.75);
  font-size: 0.85rem;
  font-family: inherit;
  cursor: pointer;
  transition: all 0.2s ease;
  text-align: left;
}

.role-dropdown-item:hover {
  background: rgba(255, 255, 255, 0.08);
  color: white;
}

.role-dropdown-item.active {
  background: rgba(233, 69, 96, 0.1);
  color: white;
}

.role-dropdown-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  flex-shrink: 0;
}

.dot-entrepreneur { background: #74b9ff; }
.dot-investor { background: #f4a261; }
.dot-admin { background: #e94560; }

.role-dropdown-check {
  margin-left: auto;
  color: var(--accent);
  font-weight: 600;
  font-size: 0.75rem;
}

/* ---- 点击空白收起下拉 ---- */
.dropdown-backdrop {
  position: fixed;
  inset: 0;
  z-index: 99;
}

/* ========== 空状态 ========== */
.empty-state {
  text-align: center;
  padding: 4rem;
  color: rgba(255, 255, 255, 0.3);
  font-size: 0.95rem;
}

/* ========== 加载覆盖层 ========== */
.loading-overlay {
  position: fixed;
  inset: 0;
  background: rgba(26, 26, 46, 0.8);
  backdrop-filter: blur(5px);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 200;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 3px solid rgba(255, 255, 255, 0.2);
  border-top-color: var(--accent);
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin { to { transform: rotate(360deg); } }

/* ========== 响应式 ========== */
@media (max-width: 1024px) {
  .admin-page { padding: 6rem 1.5rem 3rem; }
  .table-container { overflow-x: auto; }
  table { min-width: 800px; }

  .page-header h1 {
    font-size: 2rem;
  }
}

@media (max-width: 640px) {
  .admin-page { padding: 5rem 1rem 3rem; }

  .page-header {
    flex-direction: column;
    align-items: stretch;
  }

  .search-input {
    width: 100%;
  }

  .page-header h1 {
    font-size: 1.75rem;
  }
}
</style>
