import { createRouter, createWebHistory } from 'vue-router'

const routes = [

  {
    path: '/login',
    name: '登录',
    component: () => import('../views/login.vue')
  },
  {
    path: '/',
    name: '主页',
    component: () => import('../views/main.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
