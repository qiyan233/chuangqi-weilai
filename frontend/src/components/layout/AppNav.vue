<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()
const scrolled = ref(false)
const mobileMenuOpen = ref(false)

function handleScroll() {
  scrolled.value = window.scrollY > 50
}

function logout() {
  authStore.logout()
  router.push('/')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
})
</script>

<template>
  <nav class="nav" :class="{ scrolled }">
    <router-link to="/" class="nav-logo">
      <span class="logo-highlight">创</span>启未来
    </router-link>

    <button class="mobile-menu-btn" @click="mobileMenuOpen = !mobileMenuOpen">
      <span></span>
      <span></span>
      <span></span>
    </button>

    <ul class="nav-links" :class="{ open: mobileMenuOpen }">
      <li><router-link to="/" @click="mobileMenuOpen = false">首页</router-link></li>
      <li><router-link to="/startup/apply" @click="mobileMenuOpen = false">创业者入驻</router-link></li>
      <li><router-link to="/investor/projects" @click="mobileMenuOpen = false">项目大厅</router-link></li>
      <li><router-link to="/tools" @click="mobileMenuOpen = false">工具赋能</router-link></li>
      <li><router-link to="/cases" @click="mobileMenuOpen = false">成功案例</router-link></li>
    </ul>

    <div class="nav-actions">
      <template v-if="authStore.isAuthenticated">
        <router-link
          :to="authStore.isEntrepreneur ? '/startup/dashboard' : '/investor/dashboard'"
          class="nav-dashboard"
        >
          {{ authStore.user?.realName || authStore.user?.username }}
        </router-link>
        <button class="nav-logout" @click="logout">退出</button>
      </template>
      <template v-else>
        <router-link to="/login" class="nav-login">登录</router-link>
        <router-link to="/register" class="nav-cta">注册</router-link>
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
  padding: 1rem 4rem;
  display: flex;
  justify-content: space-between;
  align-items: center;
  transition: all 0.4s ease;
}

.nav.scrolled {
  background: rgba(26, 26, 46, 0.95);
  backdrop-filter: blur(20px);
  padding: 0.8rem 4rem;
}

.nav-logo {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.5rem;
  font-weight: 700;
  color: white;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo-highlight {
  color: var(--accent);
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
  transition: color 0.3s;
  position: relative;
}

.nav-links a::after {
  content: '';
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background: var(--accent);
  transition: width 0.3s;
}

.nav-links a:hover,
.nav-links a.router-link-active {
  color: white;
}

.nav-links a:hover::after,
.nav-links a.router-link-active::after {
  width: 100%;
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
  transition: color 0.3s;
}

.nav-login:hover {
  color: white;
}

.nav-cta {
  background: var(--accent);
  color: white;
  padding: 0.6rem 1.5rem;
  border-radius: 30px;
  text-decoration: none;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.3s;
}

.nav-cta:hover {
  background: var(--accent-warm);
  transform: translateY(-2px);
}

.nav-dashboard {
  color: rgba(255, 255, 255, 0.85);
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.3s;
}

.nav-dashboard:hover {
  color: white;
}

.nav-logout {
  background: transparent;
  color: rgba(255, 255, 255, 0.6);
  border: 1px solid rgba(255, 255, 255, 0.3);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.8rem;
  transition: all 0.3s;
}

.nav-logout:hover {
  color: white;
  border-color: white;
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
  transition: all 0.3s;
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
    flex-direction: column;
    padding: 1rem 2rem;
    gap: 1rem;
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
</style>
