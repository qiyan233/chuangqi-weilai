<script setup>
import AppNav from '@/components/layout/AppNav.vue'
import AppFooter from '@/components/layout/AppFooter.vue'
</script>

<template>
  <div class="app">
    <AppNav />
    <main class="main-content">
      <router-view />
    </main>
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

body {
  font-family: 'Noto Sans SC', -apple-system, BlinkMacSystemFont, 'Segoe UI', sans-serif;
  background: var(--cream);
  color: var(--text);
  overflow-x: hidden;
}

a {
  text-decoration: none;
  color: inherit;
}

button {
  cursor: pointer;
  font-family: inherit;
}

.main-content {
  min-height: calc(100vh - 80px);
}
</style>
