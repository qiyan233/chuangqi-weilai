<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const stats = ref({
  approvedProjects: 200,
  totalFunding: '5亿',
  partnerInvestors: 50,
  mentors: 30,
  jobsCreated: 1200,
})

onMounted(async () => {
  try {
    const response = await api.get('/stats')
    stats.value = response.data
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
})
</script>

<template>
  <div class="home-page">
    <!-- Hero Section -->
    <section class="hero">
      <div class="hero-grid"></div>
      <div class="hero-content">
        <div class="hero-text">
          <h1>让每一份创业梦想<br><span class="highlight">找到生长的土壤</span></h1>
          <p class="hero-subtitle">连接创业者、孵化器与投资人的一站式平台。从BP打磨到融资对接，我们陪伴每一步成长。</p>
          <div class="hero-buttons">
            <router-link to="/startup/apply" class="btn-primary">我是创业者 →</router-link>
            <router-link to="/investor/projects" class="btn-secondary">我是投资人/合作伙伴</router-link>
          </div>
        </div>
        <div class="hero-visual">
          <div class="orbit-container">
            <div class="orbit orbit-3">
              <div class="orbit-node">导师</div>
              <div class="orbit-node">政策</div>
              <div class="orbit-node">资源</div>
            </div>
            <div class="orbit orbit-2">
              <div class="orbit-node">投资人</div>
              <div class="orbit-node">孵化器</div>
            </div>
            <div class="orbit orbit-1">
              <div class="orbit-node">创业者</div>
            </div>
            <div class="center-hub">创启<br>未来</div>
          </div>
        </div>
      </div>
    </section>

    <!-- Stats Bar -->
    <section class="stats-bar">
      <div class="stats-grid">
        <div class="stat-item">
          <h3>{{ stats.approvedProjects }}+</h3>
          <p>在孵创业项目</p>
        </div>
        <div class="stat-item">
          <h3>¥{{ stats.totalFunding }}+</h3>
          <p>累计融资总额</p>
        </div>
        <div class="stat-item">
          <h3>{{ stats.partnerInvestors }}+</h3>
          <p>合作投资机构</p>
        </div>
        <div class="stat-item">
          <h3>{{ stats.mentors }}+</h3>
          <p>资深创业导师</p>
        </div>
      </div>
    </section>

    <!-- Roles Section -->
    <section class="section">
      <div class="section-header">
        <span class="section-label">平台定位</span>
        <h2 class="section-title">四位一体，连接创业生态</h2>
        <p class="section-desc">信息收集 + 内容展示 + 工具赋能 + 资源匹配，构建完整的创业服务闭环</p>
      </div>
      <div class="roles-grid">
        <div class="role-card">
          <div class="role-icon">🚀</div>
          <h3>创业者/初创团队</h3>
          <p>寻找合适孵化器、准备融资材料、对接投资人，让创业之路不再孤单</p>
        </div>
        <div class="role-card">
          <div class="role-icon">🏢</div>
          <h3>孵化器/加速器</h3>
          <p>筛选优质项目、展示孵化成果、招募创业团队，提升孵化效能</p>
        </div>
        <div class="role-card">
          <div class="role-icon">💰</div>
          <h3>投资人/VC</h3>
          <p>发现优质项目、跟踪融资动态、高效对接，把握早期投资机会</p>
        </div>
        <div class="role-card">
          <div class="role-icon">👨‍🏫</div>
          <h3>创业导师</h3>
          <p>展示专业背景、接受项目咨询、拓展影响力，赋能创业者成长</p>
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
        <div class="step">
          <div class="step-number">1</div>
          <h4>需求匹配</h4>
          <p>明确项目领域，筛选定位匹配的孵化基地</p>
        </div>
        <div class="step">
          <div class="step-number">2</div>
          <h4>在线申请</h4>
          <p>线上填报申请表，上传BP及相关材料</p>
        </div>
        <div class="step">
          <div class="step-number">3</div>
          <h4>材料初审</h4>
          <p>合规性与创新性双重校验</p>
        </div>
        <div class="step">
          <div class="step-number">4</div>
          <h4>实地考察</h4>
          <p>核查运营场景、团队状态、技术原型</p>
        </div>
        <div class="step">
          <div class="step-number">5</div>
          <h4>评审答辩</h4>
          <p>10-15分钟项目陈述 + 评委提问</p>
        </div>
        <div class="step">
          <div class="step-number">6</div>
          <h4>签约入驻</h4>
          <p>签订入驻协议，明确权利义务与考核机制</p>
        </div>
      </div>
    </section>

    <!-- Tools Section -->
    <section class="section">
      <div class="section-header">
        <span class="section-label">工具赋能</span>
        <h2 class="section-title">创业工具箱，助你一臂之力</h2>
        <p class="section-desc">从BP撰写到财务预测，提供全链条创业工具支持</p>
      </div>
      <div class="tools-grid">
        <div class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" style="background: rgba(233,69,96,0.1);">📄</div>
            <h3>BP工具</h3>
          </div>
          <ul>
            <li>BP模板下载</li>
            <li>在线编辑器</li>
            <li>自检清单</li>
            <li>评分工具</li>
          </ul>
        </div>
        <div class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" style="background: rgba(0,184,148,0.1);">📊</div>
            <h3>财务工具</h3>
          </div>
          <ul>
            <li>财务模型模板（3-5年预测）</li>
            <li>估值计算器</li>
            <li>LTV/CAC计算器</li>
            <li>烧钱率计算器</li>
          </ul>
        </div>
        <div class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" style="background: rgba(116,185,255,0.1);">🔍</div>
            <h3>市场分析</h3>
          </div>
          <ul>
            <li>行业报告数据库</li>
            <li>竞品分析模板</li>
            <li>市场规模计算器</li>
          </ul>
        </div>
        <div class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" style="background: rgba(244,162,97,0.1);">🎯</div>
            <h3>匹配工具</h3>
          </div>
          <ul>
            <li>项目-投资人智能匹配</li>
            <li>政策-企业匹配推荐</li>
          </ul>
        </div>
        <div class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" style="background: rgba(162,155,254,0.1);">✅</div>
            <h3>评估工具</h3>
          </div>
          <ul>
            <li>项目在线评估问卷</li>
            <li>创业 readiness 评估</li>
          </ul>
        </div>
        <div class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" style="background: rgba(253,203,110,0.1);">🎤</div>
            <h3>路演工具</h3>
          </div>
          <ul>
            <li>路演日程管理</li>
            <li>路演PPT模板</li>
            <li>Q&A预判准备</li>
          </ul>
        </div>
      </div>
    </section>

    <!-- CTA Section -->
    <section class="cta-section">
      <div class="cta-content">
        <h2>准备好开启你的创业之旅了吗？</h2>
        <p>无论你是创业者寻找孵化支持，还是投资人寻找优质项目，创启未来都是你最佳的合作伙伴</p>
        <div class="hero-buttons" style="justify-content: center;">
          <router-link to="/startup/apply" class="btn-primary">立即申请入驻</router-link>
          <router-link to="/investor/projects" class="btn-secondary">成为合作伙伴</router-link>
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

.hero::before {
  content: '';
  position: absolute;
  top: -50%;
  right: -20%;
  width: 80%;
  height: 200%;
  background: radial-gradient(ellipse, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
  animation: pulse 8s ease-in-out infinite;
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 0.5; }
  50% { transform: scale(1.1); opacity: 0.8; }
}

.hero-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: radial-gradient(ellipse at center, black 30%, transparent 70%);
}

.hero-content {
  position: relative;
  z-index: 2;
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 4rem;
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 4rem;
  align-items: center;
}

.hero-text h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 4rem;
  font-weight: 900;
  color: white;
  line-height: 1.2;
  margin-bottom: 1.5rem;
}

.hero-text h1 .highlight {
  color: var(--accent);
  position: relative;
  display: inline-block;
}

.hero-text h1 .highlight::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 8px;
  background: rgba(233, 69, 96, 0.3);
  border-radius: 4px;
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
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-primary:hover {
  background: var(--accent-warm);
  transform: translateY(-3px);
  box-shadow: 0 10px 30px rgba(233, 69, 96, 0.4);
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
  transition: all 0.3s;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
}

.btn-secondary:hover {
  border-color: white;
  background: rgba(255, 255, 255, 0.1);
}

/* Hero Visual */
.hero-visual {
  position: relative;
  height: 500px;
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
}

.orbit-1 { width: 200px; height: 200px; animation: rotate 20s linear infinite; }
.orbit-2 { width: 320px; height: 320px; animation: rotate 30s linear infinite reverse; }
.orbit-3 { width: 440px; height: 440px; animation: rotate 40s linear infinite; }

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
}

.orbit-1 .orbit-node { top: -24px; left: 50%; transform: translateX(-50%); }
.orbit-2 .orbit-node:nth-child(1) { top: -24px; left: 50%; transform: translateX(-50%); }
.orbit-2 .orbit-node:nth-child(2) { bottom: -24px; left: 50%; transform: translateX(-50%); background: var(--gold); border-color: var(--gold); }
.orbit-3 .orbit-node:nth-child(1) { top: -24px; left: 50%; transform: translateX(-50%); }
.orbit-3 .orbit-node:nth-child(2) { bottom: 30%; right: -24px; background: var(--success); border-color: var(--success); }
.orbit-3 .orbit-node:nth-child(3) { bottom: 30%; left: -24px; background: #74b9ff; border-color: #74b9ff; }

.center-hub {
  position: absolute;
  width: 120px;
  height: 120px;
  background: var(--accent);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-family: 'Noto Serif SC', serif;
  font-size: 1.1rem;
  font-weight: 700;
  box-shadow: 0 0 60px rgba(233, 69, 96, 0.5);
}

/* Stats Bar */
.stats-bar {
  background: var(--ink-light);
  padding: 3rem 4rem;
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

.stat-item h3 {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: var(--accent);
  margin-bottom: 0.5rem;
}

.stat-item p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

/* Section Common */
.section {
  padding: 6rem 4rem;
  max-width: 1400px;
  margin: 0 auto;
}

.section-header {
  text-align: center;
  margin-bottom: 4rem;
}

.section-label {
  display: inline-block;
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
  padding: 0.4rem 1.2rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  margin-bottom: 1rem;
  letter-spacing: 1px;
}

.section-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 2.5rem;
  color: var(--ink);
  margin-bottom: 1rem;
}

.section-desc {
  color: var(--text-light);
  font-size: 1.1rem;
  max-width: 600px;
  margin: 0 auto;
  line-height: 1.7;
}

/* Roles Section */
.roles-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
}

.role-card {
  background: white;
  border-radius: 16px;
  padding: 2.5rem 2rem;
  text-align: center;
  transition: all 0.4s;
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
  background: var(--accent);
  transform: scaleX(0);
  transition: transform 0.4s;
}

.role-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.role-card:hover::before {
  transform: scaleX(1);
}

.role-icon {
  width: 64px;
  height: 64px;
  background: rgba(233, 69, 96, 0.1);
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1.5rem;
  font-size: 1.8rem;
}

.role-card h3 {
  font-size: 1.2rem;
  color: var(--ink);
  margin-bottom: 0.75rem;
}

.role-card p {
  color: var(--text-light);
  font-size: 0.9rem;
  line-height: 1.6;
}

/* Process Section */
.process-section {
  background: var(--ink);
  padding: 6rem 4rem;
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
  background: rgba(255, 255, 255, 0.1);
}

.step {
  text-align: center;
  flex: 1;
  position: relative;
}

.step-number {
  width: 50px;
  height: 50px;
  background: var(--accent);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.1rem;
  margin: 0 auto 1.5rem;
  position: relative;
  z-index: 2;
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
.tools-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1.5rem;
}

.tool-card {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  border: 1px solid var(--stone);
  transition: all 0.3s;
}

.tool-card:hover {
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  transform: translateY(-4px);
}

.tool-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1rem;
}

.tool-icon {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.3rem;
}

.tool-card h3 {
  font-size: 1.1rem;
  color: var(--ink);
}

.tool-card ul {
  list-style: none;
  padding: 0;
}

.tool-card li {
  padding: 0.4rem 0;
  color: var(--text-light);
  font-size: 0.9rem;
  padding-left: 1.2rem;
  position: relative;
}

.tool-card li::before {
  content: '→';
  position: absolute;
  left: 0;
  color: var(--accent);
}

/* CTA Section */
.cta-section {
  background: var(--ink);
  padding: 5rem 4rem;
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
  font-size: 2.5rem;
  color: white;
  margin-bottom: 1rem;
}

.cta-section p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
  margin-bottom: 2rem;
}

/* Responsive */
@media (max-width: 1024px) {
  .hero-content {
    grid-template-columns: 1fr;
    text-align: center;
  }

  .hero-text h1 {
    font-size: 3rem;
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
  .hero-text h1 {
    font-size: 2.2rem;
  }

  .section {
    padding: 4rem 1.5rem;
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
}
</style>
