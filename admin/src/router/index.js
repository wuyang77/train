import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    component: () => import('../views/login.vue'),
  },

  {
    path: '/',
    component: () => import('../views/main.vue'),
    meta: {
      loginRequire: true
    },
    children:[{
      path: 'welcome',
      component: () => import('../views/main/welcome.vue')
    }, {
      path: 'about',
      component: () => import('../views/main/about.vue')
    },
    {
        path: 'business/',
        children: [{
          path: 'daily_train',
          name: 'daily_train',
          component: () => import('../views/business/daily-train.vue')
        }]
      },
    {
      path: 'batch/',
      children: [{
        path: 'job',
        name: 'job',
        component: () => import('../views/batch/job.vue')
      }]
    },
    {
      path: 'base/',
      children: [{
        path: 'station',
        component: () => import('../views/base/station.vue')
      },{
        path: 'train',
        component: () => import('../views/base/train.vue')
      },{
        path: 'train-station',
        component: () => import('../views/base/train-station.vue')
      },{
        path: "train-carriage",
        component: () => import('../views/base/train-carriage.vue')
      },{
        path: "train-seat",
        component: () => import('../views/base/train-seat.vue')
      }]
    }]
  },
  {
    path: '',
    redirect: '/welcome'
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
