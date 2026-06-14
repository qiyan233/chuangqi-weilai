<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import api from '@/api'

const router = useRouter()
const authStore = useAuthStore()

const loading = ref(false)
const error = ref('')
const success = ref('')

const form = ref({
  // 项目信息
  name: '',
  industry: '',
  stage: '',
  description: '',
  painPoint: '',
  businessModel: '',
  coreTech: '',
  targetMarket: '',
  demoUrl: '',
  // 运营数据
  userCount: null,
  dauMau: '',
  mrr: '',
  arr: '',
  // 融资需求
  fundingRound: '',
  fundingAmount: '',
  valuation: '',
  equityPercent: '',
  fundUsage: '',
  // 团队信息
  teamMembers: '',
  equityStructure: '',
  teamTags: [],
})

const industries = [
  '人工智能', '企业服务', '消费零售', '医疗健康',
  '教育培训', '金融科技', '先进制造', '新能源', '其他'
]

const stages = ['概念阶段', '原型开发', 'MVP验证', '已上线运营', '规模化扩张']
const fundingRounds = ['种子轮', '天使轮', 'Pre-A轮', 'A轮', 'A+轮', 'B轮及以上']

const teamTagOptions = [
  '技术研发', '产品设计', '市场营销', '运营管理',
  '商务拓展', '财务管理', '法律合规', '供应链'
]

function toggleTeamTag(tag) {
  const index = form.value.teamTags.indexOf(tag)
  if (index === -1) {
    form.value.teamTags.push(tag)
  } else {
    form.value.teamTags.splice(index, 1)
  }
}

async function handleSubmit() {
  loading.value = true
  error.value = ''
  success.value = ''

  try {
    console.log('开始提交申请...')

    // 创建项目
    console.log('1. 创建项目...')
    const projectResponse = await api.post('/projects', {
      name: form.value.name,
      industry: form.value.industry,
      stage: form.value.stage,
      description: form.value.description,
      painPoint: form.value.painPoint,
      businessModel: form.value.businessModel,
      coreTech: form.value.coreTech,
      targetMarket: form.value.targetMarket,
      demoUrl: form.value.demoUrl,
      userCount: form.value.userCount,
      dauMau: form.value.dauMau,
      mrr: form.value.mrr,
      arr: form.value.arr,
      fundingRound: form.value.fundingRound,
      fundingAmount: form.value.fundingAmount,
      valuation: form.value.valuation,
      equityPercent: form.value.equityPercent,
      fundUsage: form.value.fundUsage,
    })
    console.log('项目创建成功:', projectResponse.data)

    // 创建入驻申请
    console.log('2. 创建入驻申请...')
    const appResponse = await api.post('/applications', {
      projectId: projectResponse.data.id,
      teamMembers: form.value.teamMembers,
      equityStructure: form.value.equityStructure,
      teamTags: JSON.stringify(form.value.teamTags),
    })
    console.log('申请创建成功:', appResponse.data)

    // 提交项目审核
    console.log('3. 提交项目审核...')
    const submitResponse = await api.post(`/projects/${projectResponse.data.id}/submit`)
    console.log('项目提交审核成功:', submitResponse.data)

    success.value = '申请已提交！我们的团队将在3个工作日内与您联系。'
    setTimeout(() => {
      router.push('/startup/dashboard')
    }, 2000)
  } catch (err) {
    console.error('提交失败详细错误:', err)
    console.error('错误响应:', err.response?.data)
    console.error('错误状态:', err.response?.status)
    console.error('错误头:', err.response?.headers)

    // 显示详细的错误信息
    const errorData = err.response?.data
    if (errorData) {
      // 后端返回的错误可能是 error 或 message 字段
      error.value = errorData.error || errorData.message || JSON.stringify(errorData)
    } else if (err.message) {
      error.value = err.message
    } else {
      error.value = '提交失败，请重试'
    }
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="apply-page">
    <!-- Hero -->
    <section class="page-hero">
      <div class="page-hero-content">
        <h1>创业者入驻申请</h1>
        <p>填写以下信息，让我们的专业团队为您匹配最合适的孵化资源与投资机会</p>
      </div>
    </section>

    <!-- 申请表单 -->
    <section class="apply-section">
      <div class="apply-container">
        <div v-if="error" class="error-message">{{ error }}</div>
        <div v-if="success" class="success-message">{{ success }}</div>

        <form @submit.prevent="handleSubmit">
          <!-- 项目信息 -->
          <h3 class="form-section-title">一、项目信息</h3>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">项目名称 <span class="required">*</span></label>
              <input v-model="form.name" type="text" class="form-input" placeholder="请输入项目名称" required />
            </div>
            <div class="form-group">
              <label class="form-label">所属行业/赛道 <span class="required">*</span></label>
              <select v-model="form.industry" class="form-select" required>
                <option value="">请选择</option>
                <option v-for="ind in industries" :key="ind" :value="ind">{{ ind }}</option>
              </select>
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">一句话描述项目 <span class="required">*</span></label>
            <input v-model="form.description" type="text" class="form-input" placeholder="用一句话概括您的项目核心价值" required />
          </div>

          <div class="form-row">
            <div class="form-group">
              <label class="form-label">发展阶段 <span class="required">*</span></label>
              <select v-model="form.stage" class="form-select" required>
                <option value="">请选择</option>
                <option v-for="s in stages" :key="s" :value="s">{{ s }}</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">产品Demo/原型链接</label>
              <input v-model="form.demoUrl" type="url" class="form-input" placeholder="https://..." />
            </div>
          </div>

          <div class="form-group">
            <label class="form-label">目标市场痛点描述 <span class="required">*</span></label>
            <textarea v-model="form.painPoint" class="form-textarea" placeholder="请描述您解决的核心痛点，以及痛点的真实性验证" required></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">商业模式 <span class="required">*</span></label>
            <textarea v-model="form.businessModel" class="form-textarea" placeholder="请描述收入来源、定价策略、获客渠道、成本结构" required></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">核心产品功能与技术壁垒</label>
            <textarea v-model="form.coreTech" class="form-textarea" placeholder="请描述产品的核心功能、技术创新点及竞争壁垒"></textarea>
          </div>

          <!-- 团队信息 -->
          <h3 class="form-section-title">二、团队构成</h3>
          <div class="form-group">
            <label class="form-label">核心成员列表（角色+贡献） <span class="required">*</span></label>
            <textarea v-model="form.teamMembers" class="form-textarea" placeholder="请描述核心团队成员及其分工" required></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">股权结构</label>
            <textarea v-model="form.equityStructure" class="form-textarea" placeholder="请简要描述当前股权分配情况"></textarea>
          </div>

          <div class="form-group">
            <label class="form-label">团队能力标签</label>
            <div class="checkbox-group">
              <button
                v-for="tag in teamTagOptions"
                :key="tag"
                type="button"
                class="checkbox-item"
                :class="{ active: form.teamTags.includes(tag) }"
                @click="toggleTeamTag(tag)"
              >
                {{ tag }}
              </button>
            </div>
          </div>

          <!-- 运营数据 -->
          <h3 class="form-section-title">三、运营数据（如有）</h3>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">用户总数</label>
              <input v-model.number="form.userCount" type="number" class="form-input" placeholder="0" />
            </div>
            <div class="form-group">
              <label class="form-label">DAU/MAU</label>
              <input v-model="form.dauMau" type="text" class="form-input" placeholder="如日活/月活" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">月经常性收入(MRR)</label>
              <input v-model="form.mrr" type="text" class="form-input" placeholder="¥0" />
            </div>
            <div class="form-group">
              <label class="form-label">年经常性收入(ARR)</label>
              <input v-model="form.arr" type="text" class="form-input" placeholder="¥0" />
            </div>
          </div>

          <!-- 融资需求 -->
          <h3 class="form-section-title">四、融资需求</h3>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">融资轮次 <span class="required">*</span></label>
              <select v-model="form.fundingRound" class="form-select" required>
                <option value="">请选择</option>
                <option v-for="r in fundingRounds" :key="r" :value="r">{{ r }}</option>
              </select>
            </div>
            <div class="form-group">
              <label class="form-label">目标融资金额 <span class="required">*</span></label>
              <input v-model="form.fundingAmount" type="text" class="form-input" placeholder="如：500万人民币" required />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label class="form-label">估值预期</label>
              <input v-model="form.valuation" type="text" class="form-input" placeholder="如：3000万人民币" />
            </div>
            <div class="form-group">
              <label class="form-label">出让股权比例</label>
              <input v-model="form.equityPercent" type="text" class="form-input" placeholder="如：10%-15%" />
            </div>
          </div>
          <div class="form-group">
            <label class="form-label">资金用途明细 <span class="required">*</span></label>
            <textarea v-model="form.fundUsage" class="form-textarea" placeholder="请详细说明融资资金的具体用途及分配比例" required></textarea>
          </div>

          <button type="submit" class="btn-primary submit-btn" :disabled="loading">
            {{ loading ? '提交中...' : '提交入驻申请' }}
          </button>
        </form>
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

.apply-section {
  background: white;
  padding: 5rem 4rem;
}

.apply-container {
  max-width: 900px;
  margin: 0 auto;
}

.form-section-title {
  font-family: 'Noto Serif SC', serif;
  font-size: 1.3rem;
  color: var(--ink);
  margin: 2.5rem 0 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--stone);
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1.5rem;
}

.form-label {
  display: block;
  font-weight: 600;
  color: var(--ink);
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
}

.required {
  color: var(--accent);
}

.form-input,
.form-select,
.form-textarea {
  width: 100%;
  padding: 0.85rem 1rem;
  border: 1.5px solid var(--stone);
  border-radius: 10px;
  font-family: inherit;
  font-size: 0.95rem;
  transition: all 0.3s;
  background: var(--cream);
}

.form-input:focus,
.form-select:focus,
.form-textarea:focus {
  outline: none;
  border-color: var(--accent);
  box-shadow: 0 0 0 3px rgba(233, 69, 96, 0.1);
}

.form-textarea {
  resize: vertical;
  min-height: 120px;
}

.checkbox-group {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 0.75rem;
}

.checkbox-item {
  padding: 0.6rem 1rem;
  background: var(--cream);
  border: 1.5px solid var(--stone);
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  font-size: 0.9rem;
  color: var(--text);
  text-align: center;
}

.checkbox-item:hover {
  border-color: var(--accent);
}

.checkbox-item.active {
  background: rgba(233, 69, 96, 0.1);
  border-color: var(--accent);
  color: var(--accent);
}

.submit-btn {
  display: block;
  width: 100%;
  justify-content: center;
  padding: 1rem;
  font-size: 1rem;
  margin-top: 2rem;
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.error-message {
  background: rgba(233, 69, 96, 0.1);
  color: var(--accent);
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

.success-message {
  background: rgba(0, 184, 148, 0.1);
  color: var(--success);
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
}

@media (max-width: 1024px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .checkbox-group {
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

  .apply-section {
    padding: 3rem 1.5rem;
  }

  .checkbox-group {
    grid-template-columns: 1fr;
  }
}
</style>
