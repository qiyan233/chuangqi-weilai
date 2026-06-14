import { onMounted, onUnmounted, ref } from 'vue'
import Lenis from 'lenis'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

export function useLenis() {
  const lenis = ref(null)
  const scrollProgress = ref(0)
  let tickerCallback = null

  onMounted(() => {
    // 初始化 Lenis
    lenis.value = new Lenis({
      duration: 1.2,
      easing: (t) => Math.min(1, 1.001 - Math.pow(2, -10 * t)),
      orientation: 'vertical',
      gestureOrientation: 'vertical',
      smoothWheel: true,
      wheelMultiplier: 1,
      touchMultiplier: 2,
    })

    // 与 GSAP ScrollTrigger 集成
    lenis.value.on('scroll', ScrollTrigger.update)

    // 保存 ticker 回调引用以便清理
    tickerCallback = (time) => {
      lenis.value.raf(time * 1000)
    }
    gsap.ticker.add(tickerCallback)

    gsap.ticker.lagSmoothing(0)

    // 监听滚动进度
    lenis.value.on('scroll', ({ progress }) => {
      scrollProgress.value = progress
    })
  })

  onUnmounted(() => {
    // 移除 gsap.ticker 回调
    if (tickerCallback) {
      gsap.ticker.remove(tickerCallback)
      tickerCallback = null
    }
    if (lenis.value) {
      lenis.value.destroy()
      lenis.value = null
    }
  })

  function scrollTo(target, options = {}) {
    if (lenis.value) {
      lenis.value.scrollTo(target, options)
    }
  }

  function stop() {
    if (lenis.value) {
      lenis.value.stop()
    }
  }

  function start() {
    if (lenis.value) {
      lenis.value.start()
    }
  }

  return {
    lenis,
    scrollProgress,
    scrollTo,
    stop,
    start,
  }
}
