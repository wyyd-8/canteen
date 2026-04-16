<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>用户登录</h2>
        </div>
      </template>
      <el-form :model="loginForm" label-width="80px">
        <el-form-item label="手机号">
          <el-input v-model="loginForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">登录</el-button>
        </el-form-item>
        <div class="footer-links">
          <el-link type="primary" @click="$router.push('/user/register')">还没有账号？去注册</el-link>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)

const loginForm = reactive({
  phone: '',
  password: ''
})

const handleLogin = async () => {
  if (!loginForm.phone || !loginForm.password) {
    return ElMessage.warning('请填写完整信息')
  }

  // 简单的手机号格式验证
  if (!/^1[3-9]\d{9}$/.test(loginForm.phone)) {
    return ElMessage.warning('请输入正确的手机号')
  }

  loading.value = true
  try {
    const data = await request.post('/user/login', loginForm)
    // 存储登录信息到localStorage
    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', data.id || data.userId)
    localStorage.setItem('userName', data.userName || data.name || data.phone)
    localStorage.setItem('role', data.role)
    ElMessage.success('登录成功')
    
    if (data.role === 'ADMIN') {
      router.push('/admin/dashboard')
    } else {
      router.push('/')
    }
  } catch (error) {
    console.error(error)
    // 显示友好的错误信息
    const message = error.response?.data?.message
    if (message) {
      ElMessage.error(message)
    } else if (error.response?.status === 401) {
      ElMessage.error('手机号或密码错误')
    } else {
      ElMessage.error('登录失败，请稍后重试')
    }
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f7f8fa;
}
.login-card {
  width: 400px;
}
.card-header {
  text-align: center;
}
.login-btn {
  width: 100%;
  background-color: #FF6B6B;
  border-color: #FF6B6B;
}
.footer-links {
  text-align: center;
  margin-top: 15px;
}
</style>
