<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

const router = useRouter()
const authStore = useAuthStore()
const scrolled = ref(false)
const mobileMenuOpen = ref(false)
const navRef = ref(null)

function handleScroll() {
  scrolled.value = window.scrollY > 50
}

function logout() {
  authStore.logout()
  router.push('/')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)

  // 导航栏入场动画 — 使用 set + to 避免 from 动画卡住
  gsap.set(navRef.value, { y: -100, opacity: 0 })
  gsap.set('.nav-links li', { y: -20, opacity: 0 })

  gsap.to(navRef.value, {
    y: 0,
    opacity: 1,
    duration: 1,
    ease: 'power3.out',
    delay: 0.3,
  })

  // 导航链接交错动画
  gsap.to('.nav-links li', {
    y: 0,
    opacity: 1,
    duration: 0.6,
    stagger: 0.1,
    ease: 'power2.out',
    delay: 0.5,
  })
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <nav ref="navRef" class="nav" :class="{ scrolled }">
    <router-link to="/" class="nav-logo" data-cursor data-cursor-text="Home">
      <span class="logo-highlight">创</span>启未来
    </router-link>

    <button class="mobile-menu-btn" @click="mobileMenuOpen = !mobileMenuOpen">
      <span></span>
      <span></span>
      <span></span>
    </button>

    <ul class="nav-links" :class="{ open: mobileMenuOpen }">
      <li><router-link to="/" @click="mobileMenuOpen = false" data-cursor>首页</router-link></li>
      <li><router-link to="/startup/apply" @click="mobileMenuOpen = false" data-cursor>创业者入驻</router-link></li>
      <li><router-link to="/investor/projects" @click="mobileMenuOpen = false" data-cursor>项目大厅</router-link></li>
      <li><router-link to="/tools" @click="mobileMenuOpen = false" data-cursor>工具赋能</router-link></li>
      <li><router-link to="/cases" @click="mobileMenuOpen = false" data-cursor>成功案例</router-link></li>
      <li v-if="authStore.isAdmin"><router-link to="/admin" @click="mobileMenuOpen = false" data-cursor class="admin-link">管理后台</router-link></li>
    </ul>

    <div class="nav-actions">
      <template v-if="authStore.isAuthenticated">
        <router-link
          :to="authStore.isAdmin ? '/admin' : (authStore.isEntrepreneur ? '/startup/dashboard' : '/investor/dashboard')"
          class="nav-dashboard"
          data-cursor
        >
          {{ authStore.user?.realName || authStore.user?.username }}
        </router-link>
        <button class="nav-logout" @click="logout" data-cursor>退出</button>
      </template>
      <template v-else>
        <router-link to="/login" class="nav-login" data-cursor>登录</router-link>
        <router-link to="/register" class="nav-cta" data-cursor data-cursor-text="注册">注册</router-link>
      </template>
    </div>
  </nav>
</template>

<style scoped>
.nav {
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 1000;
  padding: 1.5rem 4rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  background: transparent;
}

.nav.scrolled {
  background: rgba(26, 26, 46, 0.9);
  backdrop-filter: blur(30px) saturate(180%);
  padding: 1rem 4rem;
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.2);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.nav-logo {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.4rem;
  font-weight: 700;
  color: white;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 0.35rem;
  transition: transform 0.3s ease;
  letter-spacing: 0.03em;
}

.nav-logo:hover {
  transform: scale(1.05);
}

.logo-highlight {
  color: var(--accent);
  display: inline-block;
  transition: transform 0.3s ease;
}

.nav-logo:hover .logo-highlight {
  transform: rotate(-10deg) scale(1.1);
}

.nav-links {
  display: flex;
  gap: 2.5rem;
  list-style: none;
}

.nav-links a {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  position: relative;
  padding: 0.5rem 0;
}

.nav-links a::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 2px;
  background: var(--accent);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.nav-links a:hover,
.nav-links a.router-link-active {
  color: white;
}

.nav-links a:hover::before,
.nav-links a.router-link-active::before {
  width: 100%;
}

/* 链接悬停时的光晕效果 */
.nav-links a::after {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
  border-radius: 50%;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  transform: translate(-50%, -50%);
  z-index: -1;
  pointer-events: none;
}

.nav-links a:hover::after {
  width: 100px;
  height: 100px;
}

.nav-actions {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.nav-login {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  padding: 0.5rem 1rem;
  border-radius: 8px;
}

.nav-login:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.nav-cta {
  background: var(--accent);
  color: white;
  padding: 0.55rem 1.4rem;
  border-radius: 30px;
  text-decoration: none;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  box-shadow: 0 4px 15px rgba(233, 69, 96, 0.3);
  letter-spacing: 0.02em;
}

.nav-cta::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.nav-cta:hover {
  background: var(--accent-warm);
  transform: translateY(-2px);
  box-shadow: 0 10px 30px rgba(233, 69, 96, 0.4);
}

.nav-cta:hover::before {
  left: 100%;
}

.nav-dashboard {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: all 0.3s ease;
  padding: 0.5rem 1rem;
  border-radius: 8px;
}

.nav-dashboard:hover {
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.nav-logout {
  background: transparent;
  color: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  transition: all 0.3s ease;
}

.nav-logout:hover {
  color: white;
  border-color: white;
  background: rgba(255, 255, 255, 0.1);
}

.mobile-menu-btn {
  display: none;
  background: none;
  border: none;
  cursor: pointer;
  padding: 0.5rem;
  flex-direction: column;
  gap: 5px;
}

.mobile-menu-btn span {
  display: block;
  width: 24px;
  height: 2px;
  background: white;
  transition: all 0.3s ease;
}

@media (max-width: 1024px) {
  .nav {
    padding: 1rem 2rem;
  }

  .nav.scrolled {
    padding: 0.8rem 2rem;
  }

  .mobile-menu-btn {
    display: flex;
  }

  .nav-links {
    display: none;
    position: absolute;
    top: 100%;
    left: 0;
    right: 0;
    background: rgba(26, 26, 46, 0.98);
    backdrop-filter: blur(20px);
    flex-direction: column;
    padding: 1rem 2rem;
    gap: 0.5rem;
  }

  .nav-links.open {
    display: flex;
  }

  .nav-actions {
    display: none;
  }

  .nav-links.open + .nav-actions {
    display: flex;
    position: absolute;
    top: calc(100% + 150px);
    left: 0;
    right: 0;
    background: rgba(26, 26, 46, 0.98);
    padding: 1rem 2rem;
  }
}

/* Admin link — subtle accent indicator */
.admin-link {
  color: rgba(255, 215, 0, 0.85) !important;
}

.admin-link::before {
  content: '';
  display: inline-block;
  width: 6px;
  height: 6px;
  background: #ffd700;
  border-radius: 50%;
  margin-right: 0.4rem;
  vertical-align: middle;
  position: static;
  box-shadow: 0 0 6px rgba(255, 215, 0, 0.6);
}

.admin-link:hover,
.admin-link.router-link-active {
  color: #ffd700 !important;
}

.admin-link:hover::before,
.admin-link.router-link-active::before {
  box-shadow: 0 0 10px rgba(255, 215, 0, 0.8);
}

/* Nav entrance safety — ensure visibility even if GSAP delays */
.app-nav {
  will-change: transform, opacity;
}
</style>
