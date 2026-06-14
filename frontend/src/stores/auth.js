import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import api from '@/api'

export const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || '')

  // 安全解析 JSON，防止 localStorage 数据损坏导致应用崩溃
  let parsedUser = null
  try {
    parsedUser = JSON.parse(localStorage.getItem('user') || 'null')
  } catch (e) {
    console.warn('Failed to parse user from localStorage:', e)
    localStorage.removeItem('user')
  }
  const user = ref(parsedUser)

  const isAuthenticated = computed(() => !!token.value)
  const isEntrepreneur = computed(() => user.value?.role === 'ENTREPRENEUR')
  const isInvestor = computed(() => user.value?.role === 'INVESTOR')
  const isAdmin = computed(() => user.value?.role === 'ADMIN')

  async function register(userData) {
    const response = await api.post('/auth/register', userData)
    return response.data
  }

  async function login(credentials) {
    const response = await api.post('/auth/login', credentials)
    const { token: newToken, user: newUser } = response.data
    token.value = newToken
    user.value = newUser
    localStorage.setItem('token', newToken)
    localStorage.setItem('user', JSON.stringify(newUser))
    return response.data
  }

  function logout() {
    token.value = ''
    user.value = null
    localStorage.removeItem('token')
    localStorage.removeItem('user')
  }

  async function fetchProfile() {
    try {
      const response = await api.get('/auth/me')
      user.value = response.data
      localStorage.setItem('user', JSON.stringify(response.data))
    } catch (error) {
      logout()
      throw error
    }
  }

  async function updateProfile(profileData) {
    const response = await api.put('/auth/profile', profileData)
    user.value = response.data
    localStorage.setItem('user', JSON.stringify(response.data))
    return response.data
  }

  return {
    token,
    user,
    isAuthenticated,
    isEntrepreneur,
    isInvestor,
    isAdmin,
    register,
    login,
    logout,
    fetchProfile,
    updateProfile,
  }
})
