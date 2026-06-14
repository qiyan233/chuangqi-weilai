<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'
import AppNav from '@/components/layout/AppNav.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
import AppCursor from '@/components/AppCursor.vue'
import { useLenis } from '@/composables/useLenis'

// 注册 GSAP 插件
gsap.registerPlugin(ScrollTrigger)

// 初始化 Lenis 平滑滚动
const { scrollProgress } = useLenis()

const router = useRouter()

// 页面转场动画
onMounted(() => {
  router.beforeEach((to, from, next) => {
    const content = document.querySelector('.main-content')
    if (content) {
      gsap.to(content, {
        opacity: 0,
        y: 20,
        duration: 0.3,
        onComplete: () => {
          next()
          gsap.fromTo(
            content,
            { opacity: 0, y: -20 },
            { opacity: 1, y: 0, duration: 0.4, ease: 'power2.out' }
          )
        },
      })
    } else {
      next()
    }
  })
})
</script>

<template>
  <div class="app">
    <!-- 自定义光标 -->
    <AppCursor />

    <!-- 滚动进度条 -->
    <div class="scroll-progress" :style="{ transform: `scaleX(${scrollProgress})` }" />

    <!-- 导航栏 -->
    <AppNav />

    <!-- 主内容区 -->
    <main class="main-content">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- 页脚 -->
    <AppFooter />
  </div>
</template>

<style>
:root {
  --ink: #1a1a2e;
  --ink-light: #16213e;
  --accent: #e94560;
  --accent-warm: #ff6b6b;
  --gold: #f4a261;
  --cream: #faf8f5;
  --stone: #e8e4df;
  --text: #2d3436;
  --text-light: #636e72;
  --success: #00b894;
  --warning: #fdcb6e;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html.lenis {
  height: auto;
}

.lenis.lenis-smooth {
  scroll-behavior: auto !important;
}

.lenis.lenis-smooth [data-lenis-prevent] {
  overscroll-behavior: contain;
}

body {
  font-family: 'Noto Sans SC', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: var(--cream);
  color: var(--text);
  overflow-x: hidden;
  cursor: none;
}

a {
  text-decoration: none;
  color: inherit;
  cursor: none;
}

button {
  cursor: none;
  font-family: inherit;
}

input, select, textarea {
  cursor: none;
}

.main-content {
  min-height: calc(100vh - 80px);
}

/* 滚动进度条 */
.scroll-progress {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 3px;
  background: var(--accent);
  transform-origin: left;
  z-index: 10001;
  transition: transform 0.1s linear;
}

/* 页面转场动画 */
.page-enter-active,
.page-leave-active {
  transition: opacity 0.3s ease, transform 0.3s ease;
}

.page-enter-from {
  opacity: 0;
  transform: translateY(-20px);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(20px);
}

/* 通用动画类 */
.fade-in {
  opacity: 0;
  transform: translateY(30px);
  transition: opacity 0.6s ease, transform 0.6s ease;
}

.fade-in.visible {
  opacity: 1;
  transform: translateY(0);
}

/* 自定义滚动条 */
::-webkit-scrollbar {
  width: 8px;
}

::-webkit-scrollbar-track {
  background: var(--ink);
}

::-webkit-scrollbar-thumb {
  background: var(--accent);
  border-radius: 4px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--accent-warm);
}

/* 文字选择颜色 */
::selection {
  background: var(--accent);
  color: white;
}
</style>
