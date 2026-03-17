<template>
  <div class="user-layout">
    <el-container>
      <el-header class="header">
        <div class="logo" @click="$router.push('/')">Canteen</div>
        <div class="nav">
          <el-button link @click="$router.push('/user/canteens')">食堂</el-button>
          <el-button link @click="$router.push('/user/cart')">购物车</el-button>
          <el-button link @click="$router.push('/user/qrcode')">取餐码</el-button>
          <el-button link type="danger" @click="handleLogout">退出</el-button>
        </div>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()

const handleLogout = () => {
  localStorage.removeItem('token')
  router.push('/user/login')
}
</script>

<style scoped>
.user-layout {
  min-height: 100vh;
  background-color: #f7f8fa;
}
.header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  position: sticky;
  top: 0;
  z-index: 100;
}
.logo {
  font-size: 20px;
  font-weight: bold;
  color: #FF6B6B;
  cursor: pointer;
}
.main {
  max-width: 600px;
  margin: 0 auto;
  padding: 15px;
}
</style>
