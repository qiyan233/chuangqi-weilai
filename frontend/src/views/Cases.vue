<script setup>
const cases = [
  {
    name: '智云科技',
    desc: '企业智能客服解决方案',
    logo: '智',
    color: '#e94560',
    stats: [
      { value: '¥5000万', label: '累计融资' },
      { value: '50+', label: '企业客户' },
      { value: '120%', label: '年增长率' },
    ],
    story: '入驻孵化6个月后完成A轮融资，平台提供BP优化、投资人对接及战略咨询支持，现已成为企业智能客服领域头部服务商。',
    tags: ['人工智能', 'SaaS', 'B2B'],
  },
  {
    name: '绿能未来',
    desc: '分布式光伏智能运维',
    logo: '绿',
    color: '#00b894',
    stats: [
      { value: '¥3000万', label: '累计融资' },
      { value: '200MW', label: '管理装机' },
      { value: '30%', label: '效率提升' },
    ],
    story: '从概念验证到规模化运营，平台全程陪伴，协助完成技术原型开发、商业模式验证及多轮融资对接。',
    tags: ['新能源', '物联网', '环保'],
  },
  {
    name: '医联智诊',
    desc: 'AI辅助医学影像诊断',
    logo: '医',
    color: '#f4a261',
    stats: [
      { value: '¥8000万', label: '累计融资' },
      { value: '100+', label: '合作医院' },
      { value: '95%', label: '诊断准确率' },
    ],
    story: '依托平台医疗资源对接，快速完成产品临床验证，获得医疗器械注册证，现服务全国100余家医疗机构。',
    tags: ['医疗健康', 'AI诊断', '医疗器械'],
  },
  {
    name: '启智学堂',
    desc: 'AI个性化学习平台',
    logo: '教',
    color: '#74b9ff',
    stats: [
      { value: '¥6000万', label: '累计融资' },
      { value: '50万+', label: '注册用户' },
      { value: '85%', label: '续费率' },
    ],
    story: '平台提供教育行业导师资源及渠道对接，帮助产品快速验证PMF，现已成为K12个性化学习领域领先品牌。',
    tags: ['教育科技', 'AI', 'C端'],
  },
]

const mentors = [
  { name: '张明远', title: '前阿里巴巴副总裁', desc: '20年互联网产品与管理经验，专注产品战略与商业化', avatar: '张', color: '#e94560', tags: ['产品战略', '商业化'] },
  { name: '李思涵', title: '红杉中国合伙人', desc: '15年投资经验，专注TMT与消费领域早期投资', avatar: '李', color: '#00b894', tags: ['融资策略', '投资对接'] },
  { name: '王建国', title: '前华为技术总监', desc: '25年技术研发经验，专注AI与云计算技术架构', avatar: '王', color: '#f4a261', tags: ['技术架构', 'AI'] },
  { name: '陈雅琴', title: '前麦肯锡资深顾问', desc: '18年战略咨询经验，专注市场分析与商业模式设计', avatar: '陈', color: '#74b9ff', tags: ['市场分析', '商业模式'] },
  { name: '刘志强', title: '连续创业者', desc: '3次成功创业经验，2次退出，专注创业实战指导', avatar: '刘', color: '#a29bfe', tags: ['创业实战', '团队管理'] },
  { name: '赵雪梅', title: '前腾讯市场总监', desc: '16年市场营销经验，专注品牌增长与用户运营', avatar: '赵', color: '#fd79a8', tags: ['市场营销', '用户增长'] },
  { name: '孙伟', title: '资深法务专家', desc: '20年企业法务经验，专注股权设计与合规管理', avatar: '孙', color: '#00cec9', tags: ['股权设计', '合规'] },
  { name: '周婷婷', title: '前字节跳动HRD', desc: '15年人力资源经验，专注组织建设与人才管理', avatar: '周', color: '#fab1a0', tags: ['组织建设', '人才管理'] },
]
</script>

<template>
  <div class="cases-page">
    <!-- Hero -->
    <section class="page-hero">
      <div class="page-hero-content">
        <h1>成功案例与导师</h1>
        <p>见证创业者的成长历程，与行业顶尖导师同行</p>
      </div>
    </section>

    <!-- Cases -->
    <section class="cases-section">
      <div class="section-header">
        <span class="section-label">孵化成果</span>
        <h2 class="section-title">已孵化企业展示</h2>
      </div>
      <div class="cases-grid">
        <div v-for="caseItem in cases" :key="caseItem.name" class="case-card">
          <div class="case-header">
            <div class="case-logo" :style="{ background: caseItem.color }">{{ caseItem.logo }}</div>
            <h3>{{ caseItem.name }}</h3>
            <p>{{ caseItem.desc }}</p>
          </div>
          <div class="case-body">
            <div class="case-stats">
              <div v-for="stat in caseItem.stats" :key="stat.label" class="case-stat">
                <h4>{{ stat.value }}</h4>
                <span>{{ stat.label }}</span>
              </div>
            </div>
            <p class="case-desc">{{ caseItem.story }}</p>
            <div class="case-tags">
              <span v-for="tag in caseItem.tags" :key="tag" class="case-tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- Mentors -->
    <section class="mentors-section">
      <div class="mentors-container">
        <div class="section-header">
          <span class="section-label" style="background: rgba(233,69,96,0.2); color: var(--accent-warm);">导师资源</span>
          <h2 class="section-title">资深创业导师</h2>
        </div>
        <div class="mentors-grid">
          <div v-for="mentor in mentors" :key="mentor.name" class="mentor-card">
            <div class="mentor-avatar" :style="{ background: mentor.color }">{{ mentor.avatar }}</div>
            <h4>{{ mentor.name }}</h4>
            <p class="mentor-title">{{ mentor.title }}</p>
            <p>{{ mentor.desc }}</p>
            <div class="mentor-tags">
              <span v-for="tag in mentor.tags" :key="tag" class="mentor-tag">{{ tag }}</span>
            </div>
          </div>
        </div>
      </div>
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

/* Cases */
.cases-section {
  padding: 4rem;
  max-width: 1400px;
  margin: 0 auto;
}

.cases-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 2rem;
}

.case-card {
  background: white;
  border-radius: 16px;
  overflow: hidden;
  border: 1px solid var(--stone);
  transition: all 0.3s;
}

.case-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 15px 35px rgba(0, 0, 0, 0.1);
}

.case-header {
  background: var(--ink);
  padding: 2rem;
  color: white;
}

.case-logo {
  width: 64px;
  height: 64px;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  font-weight: 700;
  margin-bottom: 1rem;
  color: white;
}

.case-header h3 {
  font-size: 1.3rem;
  margin-bottom: 0.5rem;
}

.case-header p {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
}

.case-body {
  padding: 1.5rem 2rem;
}

.case-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 1rem;
  margin-bottom: 1.5rem;
}

.case-stat {
  text-align: center;
  padding: 1rem;
  background: var(--cream);
  border-radius: 10px;
}

.case-stat h4 {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.3rem;
  color: var(--accent);
  margin-bottom: 0.25rem;
}

.case-stat span {
  font-size: 0.8rem;
  color: var(--text-light);
}

.case-desc {
  color: var(--text-light);
  font-size: 0.9rem;
  line-height: 1.7;
  margin-bottom: 1rem;
}

.case-tags {
  display: flex;
  gap: 0.5rem;
  flex-wrap: wrap;
}

.case-tag {
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
  padding: 0.3rem 0.8rem;
  border-radius: 12px;
  font-size: 0.8rem;
  font-weight: 600;
}

/* Mentors */
.mentors-section {
  background: var(--ink);
  padding: 5rem 4rem;
}

.mentors-container {
  max-width: 1400px;
  margin: 0 auto;
}

.mentors-section .section-title {
  color: white;
}

.mentors-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
}

.mentor-card {
  background: rgba(255, 255, 255, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  transition: all 0.3s;
}

.mentor-card:hover {
  background: rgba(255, 255, 255, 0.08);
  transform: translateY(-4px);
}

.mentor-avatar {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 1rem;
  color: white;
  font-size: 1.8rem;
  font-weight: 700;
}

.mentor-card h4 {
  color: white;
  font-size: 1.1rem;
  margin-bottom: 0.25rem;
}

.mentor-title {
  color: var(--accent);
  font-size: 0.85rem;
  margin-bottom: 0.75rem;
}

.mentor-card p {
  color: rgba(255, 255, 255, 0.6);
  font-size: 0.85rem;
  line-height: 1.5;
}

.mentor-tags {
  display: flex;
  gap: 0.4rem;
  justify-content: center;
  margin-top: 1rem;
  flex-wrap: wrap;
}

.mentor-tag {
  background: rgba(255, 255, 255, 0.1);
  color: rgba(255, 255, 255, 0.8);
  padding: 0.2rem 0.6rem;
  border-radius: 8px;
  font-size: 0.75rem;
}

/* Responsive */
@media (max-width: 1024px) {
  .cases-grid {
    grid-template-columns: 1fr;
  }

  .mentors-grid {
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

  .cases-section {
    padding: 2rem 1.5rem;
  }

  .mentors-section {
    padding: 3rem 1.5rem;
  }

  .mentors-grid {
    grid-template-columns: 1fr;
  }

  .case-stats {
    grid-template-columns: 1fr;
  }
}
</style>
