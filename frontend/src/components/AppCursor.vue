<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import gsap from 'gsap'

const cursor = ref(null)
const cursorDot = ref(null)
const cursorText = ref('')
const isHovering = ref(false)
const isClicking = ref(false)
const isTextVisible = ref(false)
const hasMoved = ref(false)

onMounted(() => {
  // Initialize cursor to screen center to avoid flying in from (0,0)
  gsap.set(cursor.value, {
    x: window.innerWidth / 2,
    y: window.innerHeight / 2,
  })
  gsap.set(cursorDot.value, {
    x: window.innerWidth / 2,
    y: window.innerHeight / 2,
  })

  // Fallback: show cursor after 1.5s even without mouse movement
  const fallbackTimer = setTimeout(() => {
    if (!hasMoved.value) {
      gsap.to('.cursor, .cursor-dot', { opacity: 1, duration: 0.3 })
    }
  }, 1500)

  const handleMouseMove = (e) => {
    if (!hasMoved.value) {
      hasMoved.value = true
      // Snap to mouse position on first move, then fade in
      gsap.set(cursor.value, { x: e.clientX, y: e.clientY })
      gsap.set(cursorDot.value, { x: e.clientX, y: e.clientY })
      gsap.to('.cursor, .cursor-dot', { opacity: 1, duration: 0.2 })
    }

    gsap.to(cursor.value, {
      x: e.clientX,
      y: e.clientY,
      duration: 0.5,
      ease: 'power2.out',
    })

    gsap.to(cursorDot.value, {
      x: e.clientX,
      y: e.clientY,
      duration: 0.1,
    })
  }

  const handleMouseDown = () => {
    isClicking.value = true
    gsap.to(cursor.value, {
      scale: 0.8,
      duration: 0.2,
    })
  }

  const handleMouseUp = () => {
    isClicking.value = false
    gsap.to(cursor.value, {
      scale: 1,
      duration: 0.2,
    })
  }

  const handleMouseEnterLink = (e) => {
    const target = e.target.closest('a, button, [data-cursor]')
    if (target) {
      isHovering.value = true
      const text = target.dataset.cursorText
      if (text) {
        cursorText.value = text
        isTextVisible.value = true
      }
      gsap.to(cursor.value, {
        scale: 1.5,
        duration: 0.3,
      })
    }
  }

  const handleMouseLeaveLink = () => {
    isHovering.value = false
    isTextVisible.value = false
    cursorText.value = ''
    gsap.to(cursor.value, {
      scale: 1,
      duration: 0.3,
    })
  }

  window.addEventListener('mousemove', handleMouseMove)
  window.addEventListener('mousedown', handleMouseDown)
  window.addEventListener('mouseup', handleMouseUp)
  document.addEventListener('mouseover', handleMouseEnterLink)
  document.addEventListener('mouseout', handleMouseLeaveLink)

  onUnmounted(() => {
    clearTimeout(fallbackTimer)
    window.removeEventListener('mousemove', handleMouseMove)
    window.removeEventListener('mousedown', handleMouseDown)
    window.removeEventListener('mouseup', handleMouseUp)
    document.removeEventListener('mouseover', handleMouseEnterLink)
    document.removeEventListener('mouseout', handleMouseLeaveLink)
  })
})
</script>

<template>
  <div class="cursor-container">
    <!-- 主光标 -->
    <div
      ref="cursor"
      class="cursor"
      :class="{
        'cursor--hover': isHovering,
        'cursor--click': isClicking,
        'cursor--text': isTextVisible,
      }"
    >
      <span v-if="isTextVisible" class="cursor-text">{{ cursorText }}</span>
    </div>

    <!-- 光标点 -->
    <div ref="cursorDot" class="cursor-dot" />
  </div>
</template>

<style scoped>
.cursor-container {
  pointer-events: none;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 10000;
}

.cursor {
  position: fixed;
  top: -20px;
  left: -20px;
  width: 40px;
  height: 40px;
  border: 2px solid var(--accent);
  border-radius: 50%;
  pointer-events: none;
  transition: width 0.3s, height 0.3s, top 0.3s, left 0.3s, border-color 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  opacity: 0;
}

.cursor--hover {
  width: 60px;
  height: 60px;
  top: -30px;
  left: -30px;
  background: var(--accent);
  border-color: var(--accent);
}

.cursor--click {
  width: 30px;
  height: 30px;
  top: -15px;
  left: -15px;
}

.cursor--text {
  width: 120px;
  height: 120px;
  top: -60px;
  left: -60px;
  background: var(--ink);
  border-color: var(--ink);
  border-radius: 50%;
}

.cursor-text {
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  text-align: center;
  white-space: nowrap;
}

.cursor-dot {
  position: fixed;
  top: -4px;
  left: -4px;
  width: 8px;
  height: 8px;
  background: var(--accent);
  border-radius: 50%;
  pointer-events: none;
  opacity: 0;
}

/* 在触摸设备上隐藏自定义光标 */
@media (hover: none) and (pointer: coarse) {
  .cursor-container {
    display: none;
  }
}
</style>
