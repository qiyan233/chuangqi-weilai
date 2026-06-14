<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'
import api from '@/api'

// 注册插件
gsap.registerPlugin(ScrollTrigger)

// 数据
const stats = ref({
  approvedProjects: 200,
  totalFunding: '5亿',
  partnerInvestors: 50,
  mentors: 30,
  jobsCreated: 1200,
})

// Canvas 粒子背景
const canvasRef = ref(null)
let animationId = null

onMounted(async () => {
  // 加载统计数据
  try {
    const response = await api.get('/stats')
    stats.value = response.data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }

  // 初始化粒子背景
  initParticles()

  // Hero 动画
  initHeroAnimations()

  // 滚动动画
  initScrollAnimations()
})

onUnmounted(() => {
  if (animationId) {
    cancelAnimationFrame(animationId)
  }
  ScrollTrigger.getAll().forEach((trigger) => trigger.kill())
})

function initParticles() {
  const canvas = canvasRef.value
  if (!canvas) return

  const ctx = canvas.getContext('2d')
  let particles = []
  const particleCount = 80

  function resize() {
    canvas.width = window.innerWidth
    canvas.height = window.innerHeight
  }

  resize()
  window.addEventListener('resize', resize)

  // 创建粒子
  for (let i = 0; i < particleCount; i++) {
    particles.push({
      x: Math.random() * canvas.width,
      y: Math.random() * canvas.height,
      vx: (Math.random() - 0.5) * 0.5,
      vy: (Math.random() - 0.5) * 0.5,
      size: Math.random() * 2 + 1,
      opacity: Math.random() * 0.5 + 0.1,
    })
  }

  function animate() {
    ctx.clearRect(0, 0, canvas.width, canvas.height)

    particles.forEach((p) => {
      p.x += p.vx
      p.y += p.vy

      if (p.x < 0 || p.x > canvas.width) p.vx *= -1
      if (p.y < 0 || p.y > canvas.height) p.vy *= -1

      ctx.beginPath()
      ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2)
      ctx.fillStyle = `rgba(233, 69, 96, ${p.opacity})`
      ctx.fill()
    })

    // 连接相邻粒子
    particles.forEach((p1, i) => {
      particles.slice(i + 1).forEach((p2) => {
        const dx = p1.x - p2.x
        const dy = p1.y - p2.y
        const dist = Math.sqrt(dx * dx + dy * dy)

        if (dist < 150) {
          ctx.beginPath()
          ctx.moveTo(p1.x, p1.y)
          ctx.lineTo(p2.x, p2.y)
          ctx.strokeStyle = `rgba(233, 69, 96, ${0.1 * (1 - dist / 150)})`
          ctx.lineWidth = 0.5
          ctx.stroke()
        }
      })
    })

    animationId = requestAnimationFrame(animate)
  }

  animate()
}

function initHeroAnimations() {
  const tl = gsap.timeline({ delay: 0.5 })

  // 标题动画
  tl.from('.hero-title .line', {
    y: 100,
    opacity: 0,
    duration: 1,
    stagger: 0.2,
    ease: 'power3.out',
  })

  // 高亮文字动画
  tl.from(
    '.hero-title .highlight',
    {
      backgroundSize: '0% 100%',
      duration: 0.8,
      ease: 'power2.inOut',
    },
    '-=0.5'
  )

  // 副标题动画
  tl.from(
    '.hero-subtitle',
    {
      y: 30,
      opacity: 0,
      duration: 0.8,
      ease: 'power2.out',
    },
    '-=0.3'
  )

  // 按钮动画
  tl.from(
    '.hero-buttons a',
    {
      y: 30,
      opacity: 0,
      duration: 0.6,
      stagger: 0.15,
      ease: 'power2.out',
    },
    '-=0.3'
  )

  // 轨道动画
  tl.from(
    '.orbit',
    {
      scale: 0,
      opacity: 0,
      duration: 1,
      stagger: 0.2,
      ease: 'back.out(1.7)',
    },
    '-=0.8'
  )

  // 轨道节点动画
  tl.from(
    '.orbit-node',
    {
      scale: 0,
      opacity: 0,
      duration: 0.5,
      stagger: 0.1,
      ease: 'back.out(1.7)',
    },
    '-=0.5'
  )

  // 中心枢纽动画
  tl.from(
    '.center-hub',
    {
      scale: 0,
      opacity: 0,
      duration: 0.8,
      ease: 'elastic.out(1, 0.5)',
    },
    '-=0.3'
  )
}

function initScrollAnimations() {
  // 统计数字动画
  gsap.from('.stat-item h3', {
    textContent: 0,
    duration: 2,
    ease: 'power1.out',
    snap: { textContent: 1 },
    scrollTrigger: {
      trigger: '.stats-bar',
      start: 'top 80%',
      toggleActions: 'play none none reverse',
    },
    stagger: 0.2,
    onUpdate: function () {
      // 保持原始格式
    },
  })

  // 统计卡片渐入
  gsap.from('.stat-item', {
    y: 60,
    opacity: 0,
    duration: 0.8,
    stagger: 0.15,
    ease: 'power3.out',
    scrollTrigger: {
      trigger: '.stats-bar',
      start: 'top 80%',
      toggleActions: 'play none none reverse',
    },
  })

  // 角色卡片动画
  gsap.from('.role-card', {
    y: 80,
    opacity: 0,
    duration: 0.8,
    stagger: 0.15,
    ease: 'power3.out',
    scrollTrigger: {
      trigger: '.roles-grid',
      start: 'top 80%',
      toggleActions: 'play none none reverse',
    },
  })

  // 流程步骤动画
  gsap.from('.step', {
    y: 60,
    opacity: 0,
    duration: 0.6,
    stagger: 0.1,
    ease: 'power2.out',
    scrollTrigger: {
      trigger: '.process-steps',
      start: 'top 80%',
      toggleActions: 'play none none reverse',
    },
  })

  // 工具卡片动画
  gsap.from('.tool-card', {
    y: 60,
    opacity: 0,
    duration: 0.6,
    stagger: 0.1,
    ease: 'power2.out',
    scrollTrigger: {
      trigger: '.tools-grid',
      start: 'top 80%',
      toggleActions: 'play none none reverse',
    },
  })

  // CTA 动画
  gsap.from('.cta-content', {
    y: 60,
    opacity: 0,
    duration: 1,
    ease: 'power3.out',
    scrollTrigger: {
      trigger: '.cta-section',
      start: 'top 80%',
      toggleActions: 'play none none reverse',
    },
  })

  // 视差效果 - Hero 背景
  gsap.to('.hero-grid', {
    y: 200,
    ease: 'none',
    scrollTrigger: {
      trigger: '.hero',
      start: 'top top',
      end: 'bottom top',
      scrub: true,
    },
  })

  // 视差效果 - 轨道
  gsap.to('.hero-visual', {
    y: -100,
    ease: 'none',
    scrollTrigger: {
      trigger: '.hero',
      start: 'top top',
      end: 'bottom top',
      scrub: true,
    },
  })
}

// 鼠标视差效果
function handleMouseMove(e) {
  const { clientX, clientY } = e
  const centerX = window.innerWidth / 2
  const centerY = window.innerHeight / 2
  const moveX = (clientX - centerX) / 50
  const moveY = (clientY - centerY) / 50

  gsap.to('.hero-visual', {
    x: moveX,
    y: moveY,
    duration: 0.8,
    ease: 'power2.out',
  })
}
</script>

<template>
  <div class="home-page" @mousemove="handleMouseMove">
    <!-- Hero Section -->
    <section class="hero">
      <!-- 粒子背景 -->
      <canvas ref="canvasRef" class="hero-particles" />

      <!-- 网格背景 -->
      <div class="hero-grid" />

      <!-- 光晕效果 -->
      <div class="hero-glow" />

      <div class="hero-content">
        <div class="hero-text">
          <h1 class="hero-title">
            <span class="line">让每一份创业梦想</span>
            <span class="line"><span class="highlight">找到生长的土壤</span></span>
          </h1>
          <p class="hero-subtitle">
            连接创业者、孵化器与投资人的一站式平台。<br />
            从BP打磨到融资对接，我们陪伴每一步成长。
          </p>
          <div class="hero-buttons">
            <router-link to="/startup/apply" class="btn-primary" data-cursor data-cursor-text="申请">
              我是创业者 →
            </router-link>
            <router-link to="/investor/projects" class="btn-secondary" data-cursor data-cursor-text="浏览">
              我是投资人/合作伙伴
            </router-link>
          </div>
        </div>

        <div class="hero-visual">
          <div class="orbit-container">
            <div class="orbit orbit-3">
              <div class="orbit-node" data-cursor>导师</div>
              <div class="orbit-node" data-cursor>政策</div>
              <div class="orbit-node" data-cursor>资源</div>
            </div>
            <div class="orbit orbit-2">
              <div class="orbit-node" data-cursor>投资人</div>
              <div class="orbit-node" data-cursor>孵化器</div>
            </div>
            <div class="orbit orbit-1">
              <div class="orbit-node" data-cursor>创业者</div>
            </div>
            <div class="center-hub">
              <span class="hub-text">创启</span>
              <span class="hub-text">未来</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 滚动提示 -->
      <div class="scroll-indicator">
        <div class="scroll-mouse">
          <div class="scroll-wheel" />
        </div>
        <span>向下滚动</span>
      </div>
    </section>

    <!-- Stats Bar -->
    <section class="stats-bar">
      <div class="stats-grid">
        <div class="stat-item">
          <h3 class="stat-number">{{ stats.approvedProjects }}</h3>
          <span class="stat-suffix">+</span>
          <p>在孵创业项目</p>
        </div>
        <div class="stat-item">
          <span class="stat-prefix">¥</span>
          <h3 class="stat-number">{{ stats.totalFunding }}</h3>
          <span class="stat-suffix">+</span>
          <p>累计融资总额</p>
        </div>
        <div class="stat-item">
          <h3 class="stat-number">{{ stats.partnerInvestors }}</h3>
          <span class="stat-suffix">+</span>
          <p>合作投资机构</p>
        </div>
        <div class="stat-item">
          <h3 class="stat-number">{{ stats.mentors }}</h3>
          <span class="stat-suffix">+</span>
          <p>资深创业导师</p>
        </div>
      </div>
    </section>

    <!-- Roles Section -->
    <section class="section roles-section">
      <div class="section-header">
        <span class="section-label">平台定位</span>
        <h2 class="section-title">四位一体，连接创业生态</h2>
        <p class="section-desc">信息收集 + 内容展示 + 工具赋能 + 资源匹配，构建完整的创业服务闭环</p>
      </div>
      <div class="roles-grid">
        <div class="role-card" data-cursor data-cursor-text="创业者">
          <div class="role-icon">🚀</div>
          <h3>创业者/初创团队</h3>
          <p>寻找合适孵化器、准备融资材料、对接投资人，让创业之路不再孤单</p>
          <div class="role-glow" />
        </div>
        <div class="role-card" data-cursor data-cursor-text="孵化器">
          <div class="role-icon">🏢</div>
          <h3>孵化器/加速器</h3>
          <p>筛选优质项目、展示孵化成果、招募创业团队，提升孵化效能</p>
          <div class="role-glow" />
        </div>
        <div class="role-card" data-cursor data-cursor-text="投资人">
          <div class="role-icon">💰</div>
          <h3>投资人/VC</h3>
          <p>发现优质项目、跟踪融资动态、高效对接，把握早期投资机会</p>
          <div class="role-glow" />
        </div>
        <div class="role-card" data-cursor data-cursor-text="导师">
          <div class="role-icon">👨‍🏫</div>
          <h3>创业导师</h3>
          <p>展示专业背景、接受项目咨询、拓展影响力，赋能创业者成长</p>
          <div class="role-glow" />
        </div>
      </div>
    </section>

    <!-- Process Section -->
    <section class="process-section">
      <div class="section-header">
        <span class="section-label">入驻流程</span>
        <h2 class="section-title">六步开启孵化之旅</h2>
        <p class="section-desc">从申请到入驻，标准化流程让对接更高效</p>
      </div>
      <div class="process-steps">
        <div class="step" v-for="i in 6" :key="i">
          <div class="step-number">{{ i }}</div>
          <h4>{{ ['需求匹配', '在线申请', '材料初审', '实地考察', '评审答辩', '签约入驻'][i - 1] }}</h4>
          <p>{{ ['明确项目领域，筛选定位匹配的孵化基地', '线上填报申请表，上传BP及相关材料', '合规性与创新性双重校验', '核查运营场景、团队状态、技术原型', '10-15分钟项目陈述 + 评委提问', '签订入驻协议，明确权利义务与考核机制'][i - 1] }}</p>
        </div>
      </div>
    </section>

    <!-- Tools Section -->
    <section class="section tools-section">
      <div class="section-header">
        <span class="section-label">工具赋能</span>
        <h2 class="section-title">创业工具箱，助你一臂之力</h2>
        <p class="section-desc">从BP撰写到财务预测，提供全链条创业工具支持</p>
      </div>
      <div class="tools-grid">
        <div class="tool-card" v-for="(tool, index) in [
          { icon: '📄', name: 'BP工具', items: ['BP模板下载', '在线编辑器', '自检清单', '评分工具'] },
          { icon: '📊', name: '财务工具', items: ['财务模型模板', '估值计算器', 'LTV/CAC计算器', '烧钱率计算器'] },
          { icon: '🔍', name: '市场分析', items: ['行业报告数据库', '竞品分析模板', '市场规模计算器'] },
          { icon: '🎯', name: '匹配工具', items: ['项目-投资人智能匹配', '政策-企业匹配推荐'] },
          { icon: '✅', name: '评估工具', items: ['项目在线评估问卷', '创业 readiness 评估'] },
          { icon: '🎤', name: '路演工具', items: ['路演日程管理', '路演PPT模板', 'Q&A预判准备'] },
        ]" :key="tool.name" :data-cursor="true" :data-cursor-text="tool.name">
          <div class="tool-header">
            <div class="tool-icon" :style="{ background: ['rgba(233,69,96,0.1)', 'rgba(0,184,148,0.1)', 'rgba(116,185,255,0.1)', 'rgba(244,162,97,0.1)', 'rgba(162,155,254,0.1)', 'rgba(253,203,110,0.1)'][index] }">
              {{ tool.icon }}
            </div>
            <h3>{{ tool.name }}</h3>
          </div>
          <ul>
            <li v-for="item in tool.items" :key="item">{{ item }}</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <div class="cta-content">
        <h2>准备好开启你的创业之旅了吗？</h2>
        <p>无论你是创业者寻找孵化支持，还是投资人寻找优质项目，创启未来都是你最佳的合作伙伴</p>
        <div class="hero-buttons" style="justify-content: center">
          <router-link to="/startup/apply" class="btn-primary" data-cursor>立即申请入驻</router-link>
          <router-link to="/investor/projects" class="btn-secondary" data-cursor>成为合作伙伴</router-link>
        </div>
      </div>
    </section>
  </div>
</template>

<style scoped>
/* Hero Section */
.hero {
  min-height: 100vh;
  background: var(--ink);
  position: relative;
  display: flex;
  align-items: center;
  overflow: hidden;
}

.hero-particles {
  position: absolute;
  inset: 0;
  z-index: 1;
}

.hero-grid {
  position: absolute;
  inset: 0;
  background-image: linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: radial-gradient(ellipse at center, black 30%, transparent 70%);
  z-index: 2;
}

.hero-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 800px;
  height: 800px;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
  animation: glowPulse 8s ease-in-out infinite;
  z-index: 2;
}

@keyframes glowPulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.5; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.8; }
}

.hero-content {
  position: relative;
  z-index: 3;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 4rem;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.hero-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 4.5rem;
  font-weight: 900;
  color: white;
  line-height: 1.2;
  margin-bottom: 2rem;
}

.hero-title .line {
  display: block;
}

.hero-title .highlight {
  color: var(--accent);
  background-image: linear-gradient(var(--accent), var(--accent));
  background-size: 100% 100%;
  background-position: left bottom;
  background-repeat: no-repeat;
  padding-bottom: 0.2em;
}

.hero-subtitle {
  font-size: 1.25rem;
  color: rgba(255, 255, 255, 0.7);
  line-height: 1.8;
  margin-bottom: 2.5rem;
  max-width: 500px;
}

.hero-buttons {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.btn-primary {
  background: var(--accent);
  color: white;
  padding: 1rem 2.5rem;
  border-radius: 50px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1rem;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  position: relative;
  overflow: hidden;
}

.btn-primary::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.5s ease;
}

.btn-primary:hover {
  background: var(--accent-warm);
  transform: translateY(-3px);
  box-shadow: 0 20px 40px rgba(233, 69, 96, 0.4);
}

.btn-primary:hover::before {
  left: 100%;
}

.btn-secondary {
  background: transparent;
  color: white;
  padding: 1rem 2.5rem;
  border-radius: 50px;
  text-decoration: none;
  font-weight: 600;
  font-size: 1rem;
  border: 2px solid rgba(255, 255, 255, 0.3);
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-secondary:hover {
  border-color: white;
  background: rgba(255, 255, 255, 0.1);
  transform: translateY(-3px);
}

/* Hero Visual */
.hero-visual {
  position: relative;
  height: 500px;
  transition: transform 0.3s ease;
}

.orbit-container {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
}

.orbit {
  position: absolute;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 50%;
  transition: all 0.3s ease;
}

.orbit:hover {
  border-color: rgba(233, 69, 96, 0.3);
}

.orbit-1 {
  width: 200px;
  height: 200px;
  animation: rotate 20s linear infinite;
}

.orbit-2 {
  width: 320px;
  height: 320px;
  animation: rotate 30s linear infinite reverse;
}

.orbit-3 {
  width: 440px;
  height: 440px;
  animation: rotate 40s linear infinite;
}

@keyframes rotate {
  from { transform: rotate(0deg); }
  to { transform: rotate(360deg); }
}

.orbit-node {
  position: absolute;
  width: 48px;
  height: 48px;
  background: var(--ink-light);
  border: 2px solid var(--accent);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 0.75rem;
  font-weight: 600;
  box-shadow: 0 0 20px rgba(233, 69, 96, 0.3);
  transition: all 0.3s ease;
}

.orbit-node:hover {
  transform: scale(1.2);
  box-shadow: 0 0 30px rgba(233, 69, 96, 0.5);
}

.orbit-1 .orbit-node {
  top: -24px;
  left: 50%;
  transform: translateX(-50%);
}

.orbit-1 .orbit-node:hover {
  transform: translateX(-50%) scale(1.2);
}

.orbit-2 .orbit-node:nth-child(1) {
  top: -24px;
  left: 50%;
  transform: translateX(-50%);
}

.orbit-2 .orbit-node:nth-child(2) {
  bottom: -24px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--gold);
  border-color: var(--gold);
}

.orbit-3 .orbit-node:nth-child(1) {
  top: -24px;
  left: 50%;
  transform: translateX(-50%);
}

.orbit-3 .orbit-node:nth-child(2) {
  bottom: 30%;
  right: -24px;
  background: var(--success);
  border-color: var(--success);
}

.orbit-3 .orbit-node:nth-child(3) {
  bottom: 30%;
  left: -24px;
  background: #74b9ff;
  border-color: #74b9ff;
}

.center-hub {
  position: absolute;
  width: 140px;
  height: 140px;
  background: linear-gradient(135deg, var(--accent), var(--accent-warm));
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: white;
  font-family: 'Noto Serif SC', serif;
  font-weight: 700;
  box-shadow: 0 0 60px rgba(233, 69, 96, 0.5);
  transition: all 0.3s ease;
}

.center-hub:hover {
  transform: scale(1.1);
  box-shadow: 0 0 80px rgba(233, 69, 96, 0.7);
}

.hub-text {
  font-size: 1.2rem;
  line-height: 1.3;
}

/* Scroll Indicator */
.scroll-indicator {
  position: absolute;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
  color: rgba(255, 255, 255, 0.5);
  font-size: 0.75rem;
  animation: fadeInUp 1s ease infinite;
  z-index: 3;
}

.scroll-mouse {
  width: 24px;
  height: 40px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-radius: 12px;
  position: relative;
}

.scroll-wheel {
  width: 4px;
  height: 8px;
  background: var(--accent);
  border-radius: 2px;
  position: absolute;
  top: 8px;
  left: 50%;
  transform: translateX(-50%);
  animation: scrollWheel 2s ease infinite;
}

@keyframes scrollWheel {
  0% { opacity: 1; top: 8px; }
  100% { opacity: 0; top: 24px; }
}

@keyframes fadeInUp {
  0%, 100% { opacity: 0.5; transform: translateX(-50%) translateY(0); }
  50% { opacity: 1; transform: translateX(-50%) translateY(-10px); }
}

/* Stats Bar */
.stats-bar {
  background: var(--ink-light);
  padding: 4rem;
  position: relative;
  z-index: 10;
}

.stats-grid {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  text-align: center;
}

.stat-item {
  position: relative;
}

.stat-item h3 {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  color: var(--accent);
  display: inline;
}

.stat-prefix, .stat-suffix {
  font-family: 'Noto Serif SC', serif;
  font-size: 2rem;
  color: var(--accent);
}

.stat-item p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  margin-top: 0.5rem;
}

/* Section Common */
.section {
  padding: 8rem 4rem;
  max-width: 1400px;
  margin: 0 auto;
}

.section-header {
  text-align: center;
  margin-bottom: 5rem;
}

.section-label {
  display: inline-block;
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
  padding: 0.5rem 1.5rem;
  border-radius: 30px;
  font-size: 0.85rem;
  font-weight: 600;
  margin-bottom: 1.5rem;
  letter-spacing: 2px;
  text-transform: uppercase;
}

.section-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  color: var(--ink);
  margin-bottom: 1.5rem;
}

.section-desc {
  color: var(--text-light);
  font-size: 1.15rem;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.8;
}

/* Roles Section */
.roles-section {
  background: var(--cream);
  max-width: 100%;
  padding: 8rem 4rem;
}

.roles-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.role-card {
  background: white;
  border-radius: 20px;
  padding: 3rem 2rem;
  text-align: center;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid var(--stone);
  position: relative;
  overflow: hidden;
}

.role-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 4px;
  background: linear-gradient(90deg, var(--accent), var(--accent-warm));
  transform: scaleX(0);
  transition: transform 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.role-card:hover {
  transform: translateY(-15px);
  box-shadow: 0 30px 60px rgba(0, 0, 0, 0.15);
}

.role-card:hover::before {
  transform: scaleX(1);
}

.role-glow {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, rgba(233, 69, 96, 0.1) 0%, transparent 70%);
  opacity: 0;
  transition: opacity 0.5s ease;
  z-index: 0;
}

.role-card:hover .role-glow {
  opacity: 1;
}

.role-icon {
  width: 80px;
  height: 80px;
  background: rgba(233, 69, 96, 0.1);
  border-radius: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 2rem;
  font-size: 2rem;
  transition: all 0.3s ease;
  position: relative;
  z-index: 1;
}

.role-card:hover .role-icon {
  transform: scale(1.1) rotate(-5deg);
  background: rgba(233, 69, 96, 0.2);
}

.role-card h3 {
  font-size: 1.3rem;
  color: var(--ink);
  margin-bottom: 1rem;
  position: relative;
  z-index: 1;
}

.role-card p {
  color: var(--text-light);
  font-size: 0.95rem;
  line-height: 1.7;
  position: relative;
  z-index: 1;
}

/* Process Section */
.process-section {
  background: var(--ink);
  padding: 8rem 4rem;
  position: relative;
  overflow: hidden;
}

.process-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(ellipse at center, rgba(233, 69, 96, 0.1) 0%, transparent 70%);
}

.process-section .section-title {
  color: white;
}

.process-section .section-desc {
  color: rgba(255, 255, 255, 0.6);
}

.process-steps {
  max-width: 1400px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  position: relative;
  padding-top: 2rem;
}

.process-steps::before {
  content: '';
  position: absolute;
  top: 50px;
  left: 10%;
  right: 10%;
  height: 2px;
  background: linear-gradient(90deg, transparent, var(--accent), transparent);
}

.step {
  text-align: center;
  flex: 1;
  position: relative;
  transition: transform 0.3s ease;
}

.step:hover {
  transform: translateY(-10px);
}

.step-number {
  width: 60px;
  height: 60px;
  background: linear-gradient(135deg, var(--accent), var(--accent-warm));
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.2rem;
  margin: 0 auto 1.5rem;
  position: relative;
  z-index: 2;
  box-shadow: 0 10px 30px rgba(233, 69, 96, 0.3);
}

.step h4 {
  color: white;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.step p {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.85rem;
  max-width: 180px;
  margin: 0 auto;
  line-height: 1.5;
}

/* Tools Section */
.tools-section {
  background: var(--cream);
  max-width: 100%;
  padding: 8rem 4rem;
}

.tools-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 2rem;
  max-width: 1400px;
  margin: 0 auto;
}

.tool-card {
  background: white;
  border-radius: 20px;
  padding: 2.5rem;
  border: 1px solid var(--stone);
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
}

.tool-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(135deg, rgba(233, 69, 96, 0.05) 0%, transparent 100%);
  opacity: 0;
  transition: opacity 0.3s ease;
}

.tool-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.tool-card:hover::before {
  opacity: 1;
}

.tool-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
  position: relative;
  z-index: 1;
}

.tool-icon {
  width: 60px;
  height: 60px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  transition: transform 0.3s ease;
}

.tool-card:hover .tool-icon {
  transform: scale(1.1) rotate(-5deg);
}

.tool-card h3 {
  font-size: 1.2rem;
  color: var(--ink);
}

.tool-card ul {
  list-style: none;
  padding: 0;
  position: relative;
  z-index: 1;
}

.tool-card li {
  padding: 0.5rem 0;
  color: var(--text-light);
  font-size: 0.9rem;
  padding-left: 1.5rem;
  position: relative;
  transition: all 0.3s ease;
}

.tool-card li::before {
  content: '→';
  position: absolute;
  left: 0;
  color: var(--accent);
  transition: transform 0.3s ease;
}

.tool-card:hover li::before {
  transform: translateX(5px);
}

/* CTA Section */
.cta-section {
  background: var(--ink);
  padding: 8rem 4rem;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.cta-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(ellipse at center, rgba(233, 69, 96, 0.2) 0%, transparent 70%);
}

.cta-content {
  position: relative;
  z-index: 2;
  max-width: 700px;
  margin: 0 auto;
}

.cta-section h2 {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  color: white;
  margin-bottom: 1.5rem;
}

.cta-section p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.15rem;
  margin-bottom: 3rem;
  line-height: 1.8;
}

/* Responsive */
@media (max-width: 1024px) {
  .hero-content {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .hero-title {
    font-size: 3.5rem;
  }

  .hero-subtitle {
    margin: 0 auto 2rem;
  }

  .hero-buttons {
    justify-content: center;
  }

  .hero-visual {
    display: none;
  }

  .roles-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .tools-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .process-steps {
    flex-wrap: wrap;
    gap: 2rem;
  }

  .process-steps::before {
    display: none;
  }

  .step {
    flex: 0 0 45%;
  }
}

@media (max-width: 640px) {
  .hero-title {
    font-size: 2.5rem;
  }

  .section {
    padding: 5rem 1.5rem;
  }

  .roles-section,
  .tools-section,
  .process-section,
  .cta-section {
    padding: 5rem 1.5rem;
  }

  .roles-grid {
    grid-template-columns: 1fr;
  }

  .tools-grid {
    grid-template-columns: 1fr;
  }

  .stats-grid {
    grid-template-columns: 1fr 1fr;
  }

  .step {
    flex: 0 0 100%;
  }

  .section-title {
    font-size: 2rem;
  }

  .cta-section h2 {
    font-size: 2rem;
  }
}
</style>
