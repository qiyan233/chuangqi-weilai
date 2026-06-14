<script setup>
const tools = [
  {
    icon: '📄',
    iconBg: 'rgba(233,69,96,0.1)',
    name: 'BP工具',
    items: ['BP模板下载（七大核心页面结构）', '在线编辑器，实时协作', 'BP自检清单与评分工具', '投资人视角优化建议'],
  },
  {
    icon: '📊',
    iconBg: 'rgba(0,184,148,0.1)',
    name: '财务工具',
    items: ['财务模型模板（3-5年预测）', '估值计算器（多种方法）', 'LTV/CAC计算器', '烧钱率与 runway 计算器'],
  },
  {
    icon: '🔍',
    iconBg: 'rgba(116,185,255,0.1)',
    name: '市场分析',
    items: ['行业报告数据库', '竞品分析模板', '市场规模计算器（TAM/SAM/SOM）', '用户画像生成器'],
  },
  {
    icon: '🎯',
    iconBg: 'rgba(244,162,97,0.1)',
    name: '匹配工具',
    items: ['项目-投资人智能匹配', '政策-企业匹配推荐', '孵化器适配度评估', '导师领域匹配'],
  },
  {
    icon: '✅',
    iconBg: 'rgba(162,155,254,0.1)',
    name: '评估工具',
    items: ['项目在线评估问卷', '创业 readiness 评估', '投资人六大维度评估', '团队能力矩阵分析'],
  },
  {
    icon: '🎤',
    iconBg: 'rgba(253,203,110,0.1)',
    name: '路演工具',
    items: ['路演PPT模板库', '路演日程管理', 'Q&A预判与准备', '路演视频录制与回放'],
  },
]

const bpSteps = [
  { num: 1, title: '市场空间与痛点机会', desc: '行业规模、真实痛点、数据论证。用数据说话，证明这是一个值得投入的大市场。' },
  { num: 2, title: '团队背景与分工能力', desc: '角色+贡献、互补性、背书资源。投资人投的是人，展示团队执行力与专业度。' },
  { num: 3, title: '产品/服务核心价值', desc: '独特性、竞品对比、可量化结果。清晰传达产品解决了什么问题，为什么是你的团队能做。' },
  { num: 4, title: '商业模式（盈利闭环）', desc: '收入来源、成本结构、渠道。展示清晰的盈利路径和可持续的商业模式。' },
  { num: 5, title: '财务预测（真实可验证）', desc: '3年收入/成本/现金流预测、关键假设。数据要真实，假设要合理，经得起拷问。' },
  { num: 6, title: '融资计划与用途分解', desc: '金额、用途明细、里程碑指标。让投资人清楚知道钱会花在哪里，能产生什么价值。' },
  { num: 7, title: '退出逻辑', desc: '并购/IPO/回购路径、同赛道退出案例。给投资人一个清晰的退出预期和回报路径。' },
]

const fundingMethods = [
  { method: '天使投资', stage: '种子/天使轮', feature: '金额小、决策快，个人投资者为主', scale: '100-500万' },
  { method: '风险投资(VC)', stage: 'A轮及以后', feature: '金额大、要求高，专业投资机构', scale: '1000万-数亿' },
  { method: '众筹', stage: '早期验证', feature: '门槛低，可验证市场需求', scale: '10-100万' },
  { method: '政府补助', stage: '各阶段', feature: '无稀释、流程长，政策导向', scale: '10-500万' },
  { method: '战略投资', stage: 'B轮及以后', feature: '产业资源协同，可能有业务绑定', scale: '数千万-数亿' },
]
</script>

<template>
  <div class="tools-page">
    <!-- Hero -->
    <section class="page-hero">
      <div class="page-hero-content">
        <h1>创业工具箱</h1>
        <p>从BP撰写到财务预测，从市场分析到路演准备，提供全链条创业工具支持，让创业更高效</p>
      </div>
    </section>

    <!-- Tools -->
    <section class="tools-section">
      <div class="section-header">
        <span class="section-label">工具赋能</span>
        <h2 class="section-title">全链条创业工具</h2>
      </div>
      <div class="tools-grid">
        <div v-for="tool in tools" :key="tool.name" class="tool-card">
          <div class="tool-header">
            <div class="tool-icon" :style="{ background: tool.iconBg }">{{ tool.icon }}</div>
            <h3>{{ tool.name }}</h3>
          </div>
          <ul>
            <li v-for="item in tool.items" :key="item">{{ item }}</li>
          </ul>
          <a href="#" class="tool-btn">立即使用</a>
        </div>
      </div>
    </section>

    <!-- BP Structure -->
    <section class="bp-section">
      <div class="bp-container">
        <div class="section-header">
          <span class="section-label" style="background: rgba(233,69,96,0.2); color: var(--accent-warm);">BP指南</span>
          <h2 class="section-title">商业计划书七大核心页面</h2>
        </div>
        <div class="bp-timeline">
          <div v-for="step in bpSteps" :key="step.num" class="bp-step">
            <div class="bp-step-number">{{ step.num }}</div>
            <div class="bp-step-content">
              <h4>{{ step.title }}</h4>
              <p>{{ step.desc }}</p>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Funding Compare -->
    <section class="compare-section">
      <div class="section-header">
        <span class="section-label">融资指南</span>
        <h2 class="section-title">融资方式对比</h2>
      </div>
      <table class="compare-table">
        <thead>
          <tr>
            <th>融资方式</th>
            <th>适用阶段</th>
            <th>特点</th>
            <th>典型规模</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in fundingMethods" :key="item.method">
            <td><strong>{{ item.method }}</strong></td>
            <td>{{ item.stage }}</td>
            <td>{{ item.feature }}</td>
            <td>{{ item.scale }}</td>
          </tr>
        </tbody>
      </table>
    </section>
  </div>
</template>

<style scoped>
.page-hero {
  background: var(--ink);
  padding: 8rem 4rem 4rem;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.page-hero::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: radial-gradient(ellipse at center, rgba(233, 69, 96, 0.15) 0%, transparent 70%);
}

.page-hero-content {
  position: relative;
  z-index: 2;
  max-width: 800px;
  margin: 0 auto;
}

.page-hero h1 {
  font-family: 'Noto Serif SC', serif;
  font-size: 3rem;
  color: white;
  margin-bottom: 1rem;
}

.page-hero p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.1rem;
  line-height: 1.7;
}

.section-header {
  text-align: center;
  margin-bottom: 3rem;
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
  font-size: 2.2rem;
  color: var(--ink);
  margin-bottom: 0.5rem;
}

/* Tools */
.tools-section {
  padding: 4rem;
  max-width: 1400px;
  margin: 0 auto;
}

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
  transform: translateY(-6px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.tool-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.tool-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
}

.tool-card h3 {
  font-size: 1.2rem;
  color: var(--ink);
}

.tool-card ul {
  list-style: none;
  margin-bottom: 1.5rem;
}

.tool-card li {
  padding: 0.5rem 0;
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

.tool-btn {
  display: inline-block;
  padding: 0.6rem 1.5rem;
  background: var(--accent);
  color: white;
  text-decoration: none;
  border-radius: 25px;
  font-size: 0.85rem;
  font-weight: 600;
  transition: all 0.3s;
}

.tool-btn:hover {
  background: var(--accent-warm);
  transform: translateY(-2px);
}

/* BP Structure */
.bp-section {
  background: var(--ink);
  padding: 5rem 4rem;
}

.bp-container {
  max-width: 1200px;
  margin: 0 auto;
}

.bp-section .section-title {
  color: white;
}

.bp-timeline {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.bp-step {
  display: flex;
  gap: 1.5rem;
  align-items: flex-start;
}

.bp-step-number {
  width: 48px;
  height: 48px;
  background: var(--accent);
  color: white;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 1.1rem;
  flex-shrink: 0;
}

.bp-step-content {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 12px;
  padding: 1.5rem;
  flex: 1;
}

.bp-step-content h4 {
  color: white;
  font-size: 1.1rem;
  margin-bottom: 0.5rem;
}

.bp-step-content p {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.9rem;
  line-height: 1.6;
}

/* Funding Compare */
.compare-section {
  padding: 5rem 4rem;
  max-width: 1200px;
  margin: 0 auto;
}

.compare-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 2rem;
}

.compare-table th {
  background: var(--ink);
  color: white;
  padding: 1rem;
  text-align: left;
  font-weight: 600;
}

.compare-table td {
  padding: 1rem;
  border-bottom: 1px solid var(--stone);
  background: white;
}

.compare-table tr:hover td {
  background: var(--cream);
}

/* Responsive */
@media (max-width: 1024px) {
  .tools-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .page-hero {
    padding: 6rem 1.5rem 3rem;
  }

  .page-hero h1 {
    font-size: 2rem;
  }

  .tools-section {
    padding: 2rem 1.5rem;
  }

  .tools-grid {
    grid-template-columns: 1fr;
  }

  .bp-section {
    padding: 3rem 1.5rem;
  }

  .compare-section {
    padding: 3rem 1.5rem;
  }

  .compare-table {
    font-size: 0.85rem;
  }
}
</style>
