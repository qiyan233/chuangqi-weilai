import { onMounted, onUnmounted } from 'vue'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

// 注册 GSAP 插件
gsap.registerPlugin(ScrollTrigger)

export function useGsap() {
  const ctx = gsap.context(() => {})
  const cleanupFns = []

  onUnmounted(() => {
    // 清理所有手动注册的事件监听器
    cleanupFns.forEach(fn => fn())
    cleanupFns.length = 0
    ctx.revert()
  })

  // 滚动渐入动画
  function scrollReveal(selector, options = {}) {
    const {
      y = 60,
      x = 0,
      opacity = 0,
      duration = 1,
      delay = 0,
      ease = 'power3.out',
      stagger = 0.1,
      start = 'top 85%',
      ...rest
    } = options

    ctx.add(() => {
      gsap.from(selector, {
        y,
        x,
        opacity,
        duration,
        delay,
        ease,
        stagger,
        scrollTrigger: {
          trigger: selector,
          start,
          toggleActions: 'play none none reverse',
        },
        ...rest,
      })
    })
  }

  // 文字逐字动画
  function splitTextReveal(selector, options = {}) {
    const { duration = 0.8, stagger = 0.03, ease = 'power3.out' } = options

    ctx.add(() => {
      const elements = document.querySelectorAll(selector)
      elements.forEach((el) => {
        const chars = el.textContent.split('')
        el.innerHTML = chars
          .map((char) =>
            char === ' '
              ? `<span class="char">&nbsp;</span>`
              : `<span class="char">${char}</span>`
          )
          .join('')

        gsap.from(el.querySelectorAll('.char'), {
          opacity: 0,
          y: 40,
          rotateX: -90,
          duration,
          stagger,
          ease,
          scrollTrigger: {
            trigger: el,
            start: 'top 85%',
            toggleActions: 'play none none reverse',
          },
        })
      })
    })
  }

  // 数字滚动计数
  function countUp(selector, endValue, options = {}) {
    const { duration = 2, ease = 'power1.out' } = options

    ctx.add(() => {
      const el = document.querySelector(selector)
      if (!el) return

      const obj = { value: 0 }
      gsap.to(obj, {
        value: endValue,
        duration,
        ease,
        scrollTrigger: {
          trigger: el,
          start: 'top 80%',
          toggleActions: 'play none none reverse',
        },
        onUpdate: () => {
          el.textContent = Math.floor(obj.value).toLocaleString()
        },
      })
    })
  }

  // 鼠标视差效果
  function mouseParallax(selector, intensity = 0.1) {
    const el = document.querySelector(selector)
    if (!el) return

    const handleMouseMove = (e) => {
      const { clientX, clientY } = e
      const centerX = window.innerWidth / 2
      const centerY = window.innerHeight / 2
      const moveX = (clientX - centerX) * intensity
      const moveY = (clientY - centerY) * intensity

      gsap.to(el, {
        x: moveX,
        y: moveY,
        duration: 0.8,
        ease: 'power2.out',
      })
    }

    window.addEventListener('mousemove', handleMouseMove)
    cleanupFns.push(() => window.removeEventListener('mousemove', handleMouseMove))
  }

  // 固定元素滚动动画
  function pinAnimation(selector, options = {}) {
    const { start = 'top top', end = '+=100%', scrub = true } = options

    ctx.add(() => {
      ScrollTrigger.create({
        trigger: selector,
        start,
        end,
        scrub,
        pin: true,
      })
    })
  }

  // 交错动画
  function staggerAnimation(selector, options = {}) {
    const {
      y = 40,
      opacity = 0,
      duration = 0.8,
      stagger = 0.1,
      ease = 'power3.out',
    } = options

    ctx.add(() => {
      gsap.from(selector, {
        y,
        opacity,
        duration,
        stagger,
        ease,
        scrollTrigger: {
          trigger: selector,
          start: 'top 85%',
          toggleActions: 'play none none reverse',
        },
      })
    })
  }

  return {
    gsap,
    ScrollTrigger,
    ctx,
    scrollReveal,
    splitTextReveal,
    countUp,
    mouseParallax,
    pinAnimation,
    staggerAnimation,
  }
}
