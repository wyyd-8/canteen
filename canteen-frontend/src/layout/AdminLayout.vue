<template>
  <div class="admin-layout">
    <el-container style="height: 100vh">
      <el-aside width="200px" class="aside">
        <div class="logo">Canteen Admin</div>
        <el-menu
          router
          :default-active="$route.path"
          class="menu"
          background-color="#2d3436"
          text-color="#dfe6e9"
          active-text-color="#FF6B6B"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><Monitor /></el-icon>
            <span>实时看板</span>
          </el-menu-item>
          <el-menu-item index="/admin/verify">
            <el-icon><FullScreen /></el-icon>
            <span>扫码核销</span>
          </el-menu-item>
          <el-menu-item index="/admin/foods">
            <el-icon><Food /></el-icon>
            <span>菜品管理</span>
          </el-menu-item>
          <el-menu-item index="/admin/statistics">
            <el-icon><PieChart /></el-icon>
            <span>统计报表</span>
          </el-menu-item>
          <el-menu-item @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            <span>退出登录</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div class="breadcrumb">{{ $route.meta.title }}</div>
          <div class="user-info">管理员</div>
        </el-header>
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
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
.aside {
  background-color: #2d3436;
}
.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid #636e72;
}
.menu {
  border-right: none;
}
.header {
  background-color: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}
.main {
  background-color: #f7f8fa;
}
</style>
