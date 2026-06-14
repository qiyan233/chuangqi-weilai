<script setup>
</script>

<template>
  <footer class="footer">
    <div class="footer-grid">
      <div class="footer-brand">
        <h3><span class="logo-highlight">创</span>启未来</h3>
        <p>大学初创团队孵化器与融资平台，致力于连接创业者、孵化器与投资人，打造一站式创业服务生态。</p>
      </div>
      <div class="footer-col">
        <h4>平台服务</h4>
        <ul>
          <li><router-link to="/startup/apply">创业者入驻</router-link></li>
          <li><router-link to="/investor/projects">投资人对接</router-link></li>
          <li><router-link to="/tools">工具赋能</router-link></li>
          <li><router-link to="/cases">成功案例</router-link></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>支持资源</h4>
        <ul>
          <li><a href="#">创业导师</a></li>
          <li><a href="#">政策资讯</a></li>
          <li><a href="#">活动日历</a></li>
          <li><a href="#">帮助中心</a></li>
        </ul>
      </div>
      <div class="footer-col">
        <h4>关于我们</h4>
        <ul>
          <li><a href="#">平台介绍</a></li>
          <li><a href="#">合作伙伴</a></li>
          <li><a href="#">联系我们</a></li>
          <li><a href="#">隐私政策</a></li>
        </ul>
      </div>
    </div>
    <div class="footer-bottom">
      <p>&copy; 2026 创启未来 - 大学初创团队孵化器与融资平台. All rights reserved.</p>
    </div>
  </footer>
</template>

<style scoped>
.footer {
  background: #0f0f1a;
  padding: 4rem;
  color: rgba(255, 255, 255, 0.6);
}

.footer-grid {
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 2fr 1fr 1fr 1fr;
  gap: 3rem;
}

.footer-brand h3 {
  font-family: 'Noto Serif SC', serif;
  color: white;
  font-size: 1.5rem;
  margin-bottom: 1rem;
}

.logo-highlight {
  color: var(--accent);
}

.footer-brand p {
  line-height: 1.7;
  font-size: 0.9rem;
}

.footer-col h4 {
  color: white;
  font-size: 1rem;
  margin-bottom: 1.5rem;
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
  transition: color 0.3s;
}

.footer-col a:hover {
  color: var(--accent);
}

.footer-bottom {
  max-width: 1400px;
  margin: 3rem auto 0;
  padding-top: 2rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
  text-align: center;
  font-size: 0.85rem;
}

@media (max-width: 1024px) {
  .footer-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .footer {
    padding: 3rem 1.5rem;
  }

  .footer-grid {
    grid-template-columns: 1fr;
  }
}
</style>
