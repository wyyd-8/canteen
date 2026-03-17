<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <h2>用户注册</h2>
        </div>
      </template>
      <el-form :model="registerForm" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="registerForm.userName" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="registerForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="registerForm.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" class="login-btn" @click="handleRegister" :loading="loading">注册</el-button>
        </el-form-item>
        <div class="footer-links">
          <el-link type="primary" @click="$router.push('/user/login')">已有账号？去登录</el-link>
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

const registerForm = reactive({
  userName: '',
  phone: '',
  password: ''
})

const handleRegister = async () => {
  if (!registerForm.userName || !registerForm.phone || !registerForm.password) {
    return ElMessage.warning('请填写完整信息')
  }
  loading.value = true
  try {
    await request.post('/user/register', registerForm)
    ElMessage.success('注册成功，请登录')
    router.push('/user/login')
  } catch (error) {
    console.error(error)
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
