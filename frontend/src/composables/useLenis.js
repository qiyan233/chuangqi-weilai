import { onMounted, onUnmounted, ref } from 'vue'
import Lenis from 'lenis'
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

export function useLenis() {
  const lenis = ref(null)
  const scrollProgress = ref(0)

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

    gsap.ticker.add((time) => {
      lenis.value.raf(time * 1000)
    })

    gsap.ticker.lagSmoothing(0)

    // 监听滚动进度
    lenis.value.on('scroll', ({ progress }) => {
      scrollProgress.value = progress
    })
  })

  onUnmounted(() => {
    if (lenis.value) {
      lenis.value.destroy()
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
