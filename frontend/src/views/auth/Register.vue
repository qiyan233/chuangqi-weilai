<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const form = ref({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  role: 'ENTREPRENEUR',
  realName: '',
  phone: '',
  city: '',
})
const loading = ref(false)
const error = ref('')

async function handleRegister() {
  loading.value = true
  error.value = ''

  if (form.value.password !== form.value.confirmPassword) {
    error.value = '两次输入的密码不一致'
    loading.value = false
    return
  }

  try {
    const { confirmPassword, ...registerData } = form.value
    await authStore.register(registerData)
    // 注册成功后自动登录
    await authStore.login({
      username: form.value.username,
      password: form.value.password,
    })
    router.push('/')
  } catch (err) {
    error.value = err.response?.data?.message || '注册失败，请重试'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-header">
        <h1>加入创启未来</h1>
        <p>创建您的账号，开启创业之旅</p>
      </div>

      <form class="register-form" @submit.prevent="handleRegister">
        <div v-if="error" class="error-message">{{ error }}</div>

        <div class="role-selector">
          <button
            type="button"
            class="role-btn"
            :class="{ active: form.role === 'ENTREPRENEUR' }"
            @click="form.role = 'ENTREPRENEUR'"
          >
            🚀 我是创业者
          </button>
          <button
            type="button"
            class="role-btn"
            :class="{ active: form.role === 'INVESTOR' }"
            @click="form.role = 'INVESTOR'"
          >
            💰 我是投资人
          </button>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">用户名 <span class="required">*</span></label>
            <input
              v-model="form.username"
              type="text"
              class="form-input"
              placeholder="3-50个字符"
              required
            />
          </div>
          <div class="form-group">
            <label class="form-label">邮箱 <span class="required">*</span></label>
            <input
              v-model="form.email"
              type="email"
              class="form-input"
              placeholder="your@email.com"
              required
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">密码 <span class="required">*</span></label>
            <input
              v-model="form.password"
              type="password"
              class="form-input"
              placeholder="至少6位"
              required
            />
          </div>
          <div class="form-group">
            <label class="form-label">确认密码 <span class="required">*</span></label>
            <input
              v-model="form.confirmPassword"
              type="password"
              class="form-input"
              placeholder="再次输入密码"
              required
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">真实姓名</label>
            <input
              v-model="form.realName"
              type="text"
              class="form-input"
              placeholder="请输入真实姓名"
            />
          </div>
          <div class="form-group">
            <label class="form-label">手机号码</label>
            <input
              v-model="form.phone"
              type="tel"
              class="form-input"
              placeholder="11位手机号"
            />
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">所在城市</label>
          <input
            v-model="form.city"
            type="text"
            class="form-input"
            placeholder="如：北京、上海、深圳"
          />
        </div>

        <button type="submit" class="btn-primary register-btn" :disabled="loading">
          {{ loading ? '注册中...' : '注册' }}
        </button>
      </form>

      <div class="register-footer">
        <p>已有账号？<router-link to="/login">立即登录</router-link></p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.register-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--ink);
  padding: 2rem;
}

.register-container {
  background: white;
  border-radius: 20px;
  padding: 3rem;
  width: 100%;
  max-width: 600px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.register-header {
  text-align: center;
  margin-bottom: 2rem;
}

.register-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  color: var(--ink);
  margin-bottom: 0.5rem;
}

.register-header p {
  color: var(--text-light);
  font-size: 0.95rem;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.error-message {
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
  padding: 0.75rem 1rem;
  border-radius: 8px;
  font-size: 0.9rem;
}

.role-selector {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.role-btn {
  padding: 1rem;
  border: 2px solid var(--stone);
  border-radius: 12px;
  background: white;
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--text);
  transition: all 0.3s;
}

.role-btn:hover {
  border-color: var(--accent);
}

.role-btn.active {
  border-color: var(--accent);
  background: rgba(233, 69, 96, 0.05);
  color: var(--accent);
}

.required {
  color: var(--accent);
}

.register-btn {
  width: 100%;
  justify-content: center;
  padding: 1rem;
  font-size: 1rem;
  margin-top: 0.5rem;
}

.register-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.register-footer {
  text-align: center;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid var(--stone);
}

.register-footer p {
  color: var(--text-light);
  font-size: 0.9rem;
}

.register-footer a {
  color: var(--accent);
  font-weight: 600;
  text-decoration: none;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style>
