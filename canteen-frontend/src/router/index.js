import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/user/canteens'
  },
  {
    path: '/user/login',
    name: 'UserLogin',
    component: () => import('@/views/user/Login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/user/register',
    name: 'UserRegister',
    component: () => import('@/views/user/Register.vue'),
    meta: { title: '注册' }
  },
  {
    path: '/user',
    component: () => import('@/layout/UserLayout.vue'),
    children: [
      {
        path: 'canteens',
        name: 'CanteenList',
        component: () => import('@/views/user/CanteenList.vue'),
        meta: { title: '食堂列表' }
      },
      {
        path: 'canteens/:id/foods',
        name: 'FoodList',
        component: () => import('@/views/user/FoodList.vue'),
        meta: { title: '选餐' }
      },
      {
        path: 'cart',
        name: 'Cart',
        component: () => import('@/views/user/Cart.vue'),
        meta: { title: '购物车' }
      },
      {
        path: 'orders/:id',
        name: 'OrderDetails',
        component: () => import('@/views/user/OrderDetails.vue'),
        meta: { title: '订单详情' }
      },
      {
        path: 'qrcode',
        name: 'MyQrcode',
        component: () => import('@/views/user/MyQrcode.vue'),
        meta: { title: '取餐码' }
      }
    ]
  },
  {
    path: '/admin',
    component: () => import('@/layout/AdminLayout.vue'),
    children: [
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '实时看板' }
      },
      {
        path: 'verify',
        name: 'ScanVerify',
        component: () => import('@/views/admin/ScanVerify.vue'),
        meta: { title: '扫码核销' }
      },
      {
        path: 'foods',
        name: 'AdminFoodManage',
        component: () => import('@/views/admin/FoodManage.vue'),
        meta: { title: '菜品管理' }
      },
      {
        path: 'statistics',
        name: 'AdminStatistics',
        component: () => import('@/views/admin/Statistics.vue'),
        meta: { title: '统计报表' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  document.title = `${to.meta.title || '食堂管理'} - Canteen`
  const token = localStorage.getItem('token')
  if (!token && to.path !== '/user/login' && to.path !== '/user/register') {
    next('/user/login')
  } else {
    next()
  }
})

export default router
