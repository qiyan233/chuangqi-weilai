<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import gsap from 'gsap'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()

const form = ref({
  username: '',
  password: '',
})
const loading = ref(false)
const error = ref('')

onMounted(() => {
  // 入场动画 - 使用 set + to 模式避免 from 动画导致的可见性问题
  const tl = gsap.timeline()

  // 先设置初始状态
  gsap.set(['.login-container', '.login-header h1', '.login-header p', '.form-group', '.login-btn', '.login-footer'], {
    opacity: 0,
    y: 30,
  })

  // 然后执行入场动画
  tl.to('.login-container', {
    opacity: 1,
    y: 0,
    duration: 0.8,
    ease: 'power3.out',
  })

  tl.to('.login-header h1', {
    opacity: 1,
    y: 0,
    duration: 0.6,
    ease: 'power2.out',
  }, '-=0.4')

  tl.to('.login-header p', {
    opacity: 1,
    y: 0,
    duration: 0.5,
    ease: 'power2.out',
  }, '-=0.3')

  tl.to('.form-group', {
    opacity: 1,
    y: 0,
    duration: 0.5,
    stagger: 0.1,
    ease: 'power2.out',
  }, '-=0.2')

  tl.to('.login-btn', {
    opacity: 1,
    y: 0,
    duration: 0.5,
    ease: 'power2.out',
  }, '-=0.2')

  tl.to('.login-footer', {
    opacity: 1,
    y: 0,
    duration: 0.5,
    ease: 'power2.out',
  }, '-=0.2')
})

async function handleLogin() {
  loading.value = true
  error.value = ''

  try {
    await authStore.login(form.value)
    const redirect = route.query.redirect || '/'

    // 成功动画
    gsap.to('.login-container', {
      scale: 0.95,
      opacity: 0,
      duration: 0.3,
      onComplete: () => router.push(redirect),
    })
  } catch (err) {
    error.value = err.response?.data?.message || '登录失败，请重试'

    // 错误抖动动画
    gsap.to('.login-container', {
      x: [-10, 10, -10, 10, 0],
      duration: 0.4,
      ease: 'power2.inOut',
    })
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="login-page">
    <!-- 背景效果 -->
    <div class="login-bg">
      <div class="bg-gradient" />
      <div class="bg-grid" />
      <div class="bg-glow" />
    </div>

    <div class="login-container">
      <div class="login-header">
        <div class="logo-wrapper">
          <span class="logo-icon">创</span>
        </div>
        <h1>欢迎回来</h1>
        <p>登录您的创启未来账号</p>
      </div>

      <form class="login-form" @submit.prevent="handleLogin">
        <div v-if="error" class="error-message">
          <span class="error-icon">!</span>
          {{ error }}
        </div>

        <div class="form-group">
          <label class="form-label">用户名</label>
          <div class="input-wrapper">
            <input
              v-model="form.username"
              type="text"
              class="form-input"
              placeholder="请输入用户名"
              required
            />
            <div class="input-focus-line" />
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">密码</label>
          <div class="input-wrapper">
            <input
              v-model="form.password"
              type="password"
              class="form-input"
              placeholder="请输入密码"
              required
            />
            <div class="input-focus-line" />
          </div>
        </div>

        <button type="submit" class="btn-primary login-btn" :disabled="loading" data-cursor>
          <span v-if="loading" class="loading-spinner" />
          <span v-else>登录</span>
        </button>
      </form>

      <div class="login-footer">
        <p>还没有账号？<router-link to="/register" data-cursor>立即注册</router-link></p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--ink);
  padding: 2rem;
  position: relative;
  overflow: hidden;
}

.login-bg {
  position: absolute;
  inset: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 30% 50%, rgba(233, 69, 96, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 70% 50%, rgba(116, 185, 255, 0.1) 0%, transparent 50%);
}

.bg-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 50px 50px;
}

.bg-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.2) 0%, transparent 70%);
  animation: glowPulse 6s ease-in-out infinite;
}

@keyframes glowPulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.5; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.8; }
}

.login-container {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 3rem;
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 10;
}

.login-header {
  text-align: center;
  margin-bottom: 2.5rem;
}

.logo-wrapper {
  width: 60px;
  height: 60px;
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
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
}

.login-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  color: white;
  margin-bottom: 0.5rem;
}

.login-header p {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.95rem;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.error-message {
  background: rgba(233, 69, 96, 0.1);
  border: 1px solid rgba(233, 69, 96, 0.3);
  color: var(--accent);
  padding: 1rem;
  border-radius: 12px;
  font-size: 0.9rem;
  display: flex;
  align-items: center;
  gap: 0.75rem;
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

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-label {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
}

.form-input {
  width: 100%;
  padding: 1rem 1.25rem;
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  color: white;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-input::placeholder {
  color: rgba(255, 255, 255, 0.3);
}

.form-input:focus {
  outline: none;
  border-color: var(--accent);
  background: rgba(233, 69, 96, 0.05);
  box-shadow: 0 0 0 3px rgba(233, 69, 96, 0.1);
}

.input-focus-line {
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: var(--accent);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.form-input:focus + .input-focus-line {
  width: 100%;
}

.login-btn {
  width: 100%;
  justify-content: center;
  padding: 1rem;
  font-size: 1rem;
  margin-top: 0.5rem;
  border-radius: 12px;
  position: relative;
  overflow: hidden;
}

.login-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.login-btn:hover::before {
  left: 100%;
}

.login-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.loading-spinner {
  width: 20px;
  height: 20px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.login-footer {
  text-align: center;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.login-footer p {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.9rem;
}

.login-footer a {
  color: var(--accent);
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s ease;
}

.login-footer a:hover {
  color: var(--accent-warm);
}
</style>
