<script setup>
import { onMounted } from 'vue'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

onMounted(() => {
  // 页脚渐入动画
  // Footer entrance — set+to pattern
  gsap.set('.footer', { opacity: 0, y: 50 })
  gsap.set('.footer-col', { opacity: 0, y: 30 })

  gsap.to('.footer', {
    opacity: 1,
    y: 0,
    duration: 1,
    ease: 'power3.out',
    scrollTrigger: {
      trigger: '.footer',
      start: 'top 90%',
      toggleActions: 'play none none reverse',
    },
  })

  // Column stagger animation
  gsap.to('.footer-col', {
    opacity: 1,
    y: 0,
    duration: 0.8,
    stagger: 0.15,
    ease: 'power2.out',
    scrollTrigger: {
      trigger: '.footer-grid',
      start: 'top 85%',
      toggleActions: 'play none none reverse',
    },
  })
})
</script>

<template>
  <footer class="footer">
    <div class="footer-grid">
      <div class="footer-brand">
        <h3><span class="logo-highlight">创</span>启未来</h3>
        <p>大学初创团队孵化器与融资平台，致力于连接创业者、孵化器与投资人，打造一站式创业服务生态。</p>
        <div class="social-links">
          <a href="#" class="social-link" data-cursor data-cursor-text="微信">
            <span>微</span>
          </a>
          <a href="#" class="social-link" data-cursor data-cursor-text="微博">
            <span>博</span>
          </a>
          <a href="#" class="social-link" data-cursor data-cursor-text="知乎">
            <span>知</span>
          </a>
        </div>
      </div>
      <div class="footer-col">
        <h4>平台服务</h4>
        <ul>
          <li><router-link to="/startup/apply" data-cursor>创业者入驻</router-link></li>
          <li><router-link to="/investor/projects" data-cursor>投资人对接</router-link></li>
          <li><router-link to="/tools" data-cursor>工具赋能</router-link></li>
          <li><router-link to="/cases" data-cursor>成功案例</router-link></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>支持资源</h4>
        <ul>
          <li><a href="#" data-cursor>创业导师</a></li>
          <li><a href="#" data-cursor>政策资讯</a></li>
          <li><a href="#" data-cursor>活动日历</a></li>
          <li><a href="#" data-cursor>帮助中心</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>关于我们</h4>
        <ul>
          <li><a href="#" data-cursor>平台介绍</a></li>
          <li><a href="#" data-cursor>合作伙伴</a></li>
          <li><a href="#" data-cursor>联系我们</a></li>
          <li><a href="#" data-cursor>隐私政策</a></li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; 2026 创启未来 - 大学初创团队孵化器与融资平台. All rights reserved.</p>
      <div class="footer-badges">
        <span class="badge">Vue 3</span>
        <span class="badge">Spring Boot</span>
        <span class="badge">SQLite</span>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: linear-gradient(180deg, #0f0f1a 0%, #0a0a14 100%);
  padding: 6rem 4rem 3rem;
  color: rgba(255, 255, 255, 0.55);
  position: relative;
  overflow: hidden;
}

.footer::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 2px;
  background: linear-gradient(90deg, transparent 0%, rgba(233, 69, 96, 0.3) 20%, var(--accent) 50%, rgba(233, 69, 96, 0.3) 80%, transparent 100%);
}

.footer-grid {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 4rem;
}

.footer-brand h3 {
  font-family: 'Noto Serif SC', serif;
  color: white;
  font-size: 1.8rem;
  margin-bottom: 1.5rem;
}

.logo-highlight {
  color: var(--accent);
  display: inline-block;
  transition: transform 0.3s ease;
}

.footer-brand h3:hover .logo-highlight {
  transform: rotate(-10deg);
}

.footer-brand p {
  line-height: 1.8;
  font-size: 0.95rem;
  margin-bottom: 2rem;
}

.social-links {
  display: flex;
  gap: 1rem;
}

.social-link {
  width: 42px;
  height: 42px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.06);
  border: 1px solid rgba(255, 255, 255, 0.08);
  display: flex;
  align-items: center;
  justify-content: center;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  font-size: 0.8rem;
  font-weight: 600;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.social-link:hover {
  background: var(--accent);
  color: white;
  transform: translateY(-3px);
}

.footer-col h4 {
  color: white;
  font-size: 1rem;
  margin-bottom: 1.5rem;
  position: relative;
  padding-bottom: 0.75rem;
}

.footer-col h4::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  width: 30px;
  height: 2px;
  background: var(--accent);
  transition: width 0.3s ease;
}

.footer-col:hover h4::after {
  width: 50px;
}

.footer-col ul {
  list-style: none;
}

.footer-col li {
  margin-bottom: 0.75rem;
}

.footer-col a {
  color: rgba(255, 255, 255, 0.6);
  text-decoration: none;
  font-size: 0.9rem;
  transition: all 0.3s ease;
  display: inline-block;
}

.footer-col a:hover {
  color: var(--accent);
  transform: translateX(5px);
}

.footer-bottom {
  max-width: 1400px;
  margin: 4rem auto 0;
  padding-top: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 0.85rem;
}

.footer-badges {
  display: flex;
  gap: 0.75rem;
}

.badge {
  padding: 0.35rem 0.9rem;
  background: rgba(255, 255, 255, 0.04);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 20px;
  font-size: 0.7rem;
  color: rgba(255, 255, 255, 0.4);
  letter-spacing: 0.05em;
  transition: all 0.3s ease;
}

.badge:hover {
  border-color: var(--accent);
  color: var(--accent);
}

@media (max-width: 1024px) {
  .footer-grid {
    grid-template-columns: repeat(2, 1fr);
    gap: 3rem;
  }

  .footer-bottom {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
}

@media (max-width: 640px) {
  .footer {
    padding: 4rem 1.5rem 3rem;
  }

  .footer-grid {
    grid-template-columns: 1fr;
    gap: 2rem;
  }

  .footer-badges {
    flex-wrap: wrap;
    justify-content: center;
  }
}
</style>
