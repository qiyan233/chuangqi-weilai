<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import gsap from 'gsap'

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

onMounted(() => {
  // 入场动画 - 使用 set + to 模式避免 from 动画导致的可见性问题
  const tl = gsap.timeline()

  // 先设置初始状态
  gsap.set(['.register-container', '.register-header h1', '.register-header p', '.role-btn', '.form-group'], {
    opacity: 0,
    y: 30,
  })

  // 然后执行入场动画
  tl.to('.register-container', {
    opacity: 1,
    y: 0,
    duration: 0.8,
    ease: 'power3.out',
  })

  tl.to('.register-header h1', {
    opacity: 1,
    y: 0,
    duration: 0.6,
    ease: 'power2.out',
  }, '-=0.4')

  tl.to('.register-header p', {
    opacity: 1,
    y: 0,
    duration: 0.5,
    ease: 'power2.out',
  }, '-=0.3')

  tl.to('.role-btn', {
    opacity: 1,
    y: 0,
    scale: 1,
    duration: 0.5,
    stagger: 0.15,
    ease: 'back.out(1.7)',
  }, '-=0.2')

  tl.to('.form-group', {
    opacity: 1,
    y: 0,
    duration: 0.5,
    stagger: 0.1,
    ease: 'power2.out',
  }, '-=0.3')
})

async function handleRegister() {
  loading.value = true
  error.value = ''

  if (form.value.password !== form.value.confirmPassword) {
    error.value = '两次输入的密码不一致'
    loading.value = false

    // 错误抖动动画
    gsap.to('.register-container', {
      x: [-10, 10, -10, 10, 0],
      duration: 0.4,
      ease: 'power2.inOut',
    })
    return
  }

  try {
    const { confirmPassword, ...registerData } = form.value
    await authStore.register(registerData)
    await authStore.login({
      username: form.value.username,
      password: form.value.password,
    })

    // 成功动画
    gsap.to('.register-container', {
      scale: 0.95,
      opacity: 0,
      duration: 0.3,
      onComplete: () => router.push('/'),
    })
  } catch (err) {
    error.value = err.response?.data?.message || '注册失败，请重试'

    // 错误抖动动画
    gsap.to('.register-container', {
      x: [-10, 10, -10, 10, 0],
      duration: 0.4,
      ease: 'power2.inOut',
    })
  } finally {
    loading.value = false
  }
}

function selectRole(role) {
  form.value.role = role

  // 选中动画
  gsap.to(`.role-btn.${role.toLowerCase()}`, {
    scale: 1.05,
    duration: 0.2,
    ease: 'back.out(1.7)',
    yoyo: true,
    repeat: 1,
  })
}
</script>

<template>
  <div class="register-page">
    <!-- 背景效果 -->
    <div class="register-bg">
      <div class="bg-gradient" />
      <div class="bg-grid" />
      <div class="bg-glow" />
    </div>

    <div class="register-container">
      <div class="register-header">
        <div class="logo-wrapper">
          <span class="logo-icon">创</span>
        </div>
        <h1>加入创启未来</h1>
        <p>创建您的账号，开启创业之旅</p>
      </div>

      <form class="register-form" @submit.prevent="handleRegister">
        <div v-if="error" class="error-message">
          <span class="error-icon">!</span>
          {{ error }}
        </div>

        <div class="role-selector">
          <button
            type="button"
            class="role-btn entrepreneur"
            :class="{ active: form.role === 'ENTREPRENEUR' }"
            @click="selectRole('ENTREPRENEUR')"
            data-cursor
          >
            <span class="role-icon">🚀</span>
            <span class="role-text">我是创业者</span>
          </button>
          <button
            type="button"
            class="role-btn investor"
            :class="{ active: form.role === 'INVESTOR' }"
            @click="selectRole('INVESTOR')"
            data-cursor
          >
            <span class="role-icon">💰</span>
            <span class="role-text">我是投资人</span>
          </button>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">用户名 <span class="required">*</span></label>
            <div class="input-wrapper">
              <input
                v-model="form.username"
                type="text"
                class="form-input"
                placeholder="3-50个字符"
                required
              />
              <div class="input-focus-line" />
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">邮箱 <span class="required">*</span></label>
            <div class="input-wrapper">
              <input
                v-model="form.email"
                type="email"
                class="form-input"
                placeholder="your@email.com"
                required
              />
              <div class="input-focus-line" />
            </div>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">密码 <span class="required">*</span></label>
            <div class="input-wrapper">
              <input
                v-model="form.password"
                type="password"
                class="form-input"
                placeholder="至少6位"
                required
              />
              <div class="input-focus-line" />
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">确认密码 <span class="required">*</span></label>
            <div class="input-wrapper">
              <input
                v-model="form.confirmPassword"
                type="password"
                class="form-input"
                placeholder="再次输入密码"
                required
              />
              <div class="input-focus-line" />
            </div>
          </div>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label class="form-label">真实姓名</label>
            <div class="input-wrapper">
              <input
                v-model="form.realName"
                type="text"
                class="form-input"
                placeholder="请输入真实姓名"
              />
              <div class="input-focus-line" />
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">手机号码</label>
            <div class="input-wrapper">
              <input
                v-model="form.phone"
                type="tel"
                class="form-input"
                placeholder="11位手机号"
              />
              <div class="input-focus-line" />
            </div>
          </div>
        </div>

        <div class="form-group">
          <label class="form-label">所在城市</label>
          <div class="input-wrapper">
            <input
              v-model="form.city"
              type="text"
              class="form-input"
              placeholder="如：北京、上海、深圳"
            />
            <div class="input-focus-line" />
          </div>
        </div>

        <button type="submit" class="btn-primary register-btn" :disabled="loading" data-cursor>
          <span v-if="loading" class="loading-spinner" />
          <span v-else>注册</span>
        </button>
      </form>

      <div class="register-footer">
        <p>已有账号？<router-link to="/login" data-cursor>立即登录</router-link></p>
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
  position: relative;
  overflow: hidden;
}

.register-bg {
  position: absolute;
  inset: 0;
}

.bg-gradient {
  position: absolute;
  inset: 0;
  background: radial-gradient(ellipse at 70% 30%, rgba(233, 69, 96, 0.15) 0%, transparent 50%),
    radial-gradient(ellipse at 30% 70%, rgba(116, 185, 255, 0.1) 0%, transparent 50%);
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
  width: 800px;
  height: 800px;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
  animation: glowPulse 8s ease-in-out infinite;
}

@keyframes glowPulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.5; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.8; }
}

.register-container {
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 24px;
  padding: 3rem;
  width: 100%;
  max-width: 700px;
  position: relative;
  z-index: 10;
}

.register-header {
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

.register-header h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  color: white;
  margin-bottom: 0.5rem;
}

.register-header p {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.95rem;
}

.register-form {
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

.role-selector {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.role-btn {
  padding: 1.25rem;
  border: 2px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  background: rgba(255, 255, 255, 0.05);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.75rem;
  transition: all 0.3s ease;
}

.role-btn:hover {
  border-color: var(--accent);
  background: rgba(233, 69, 96, 0.1);
}

.role-btn.active {
  border-color: var(--accent);
  background: rgba(233, 69, 96, 0.15);
  box-shadow: 0 0 20px rgba(233, 69, 96, 0.2);
}

.role-icon {
  font-size: 2rem;
}

.role-text {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.9rem;
  font-weight: 500;
}

.role-btn.active .role-text {
  color: white;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
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

.required {
  color: var(--accent);
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

.register-btn {
  width: 100%;
  justify-content: center;
  padding: 1rem;
  font-size: 1rem;
  margin-top: 0.5rem;
  border-radius: 12px;
  position: relative;
  overflow: hidden;
}

.register-btn::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.register-btn:hover::before {
  left: 100%;
}

.register-btn:disabled {
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

.register-footer {
  text-align: center;
  margin-top: 2rem;
  padding-top: 1.5rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.register-footer p {
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.9rem;
}

.register-footer a {
  color: var(--accent);
  font-weight: 600;
  text-decoration: none;
  transition: color 0.3s ease;
}

.register-footer a:hover {
  color: var(--accent-warm);
}

@media (max-width: 1024px) {
  .form-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .register-container {
    padding: 2rem;
  }

  .role-selector {
    grid-template-columns: 1fr;
  }
}
</style>
