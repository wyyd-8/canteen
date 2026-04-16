<template>
  <div class="qrcode-container">
    <el-card v-if="qrData" class="text-center">
      <h3>我的取餐码</h3>
      <div class="qr-wrapper">
        <el-image :src="qrData.qrImageUrl" class="qr-img" />
        <div v-if="qrData.status === 'USED'" class="overlay">已核销</div>
      </div>
      <p class="token">Token: {{ qrData.qrToken }}</p>
      <p class="expire">有效期至: {{ formatTime(qrData.expiresAt) }}</p>
      <el-divider />
      <div class="tips">
        <p>请向食堂工作人员展示此码</p>
        <p>核销后即可就餐</p>
      </div>
    </el-card>
    <el-empty v-else description="暂无可用的取餐码">
      <el-button type="primary" @click="$router.push('/')">去订餐</el-button>
    </el-empty>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const qrData = ref(null)
const loading = ref(false)

const getQrCode = async () => {
  // 从路由参数获取orderId和reservationId
  const orderId = route.query.orderId
  const reservationId = route.query.reservationId

  if (!orderId || !reservationId) {
    ElMessage.warning('缺少订单或预约信息')
    return
  }

  try {
    loading.value = true
    const data = await request.post('/user/pickup-qrcodes', {
      orderId: parseInt(orderId),
      reservationId: parseInt(reservationId)
    })
    qrData.value = data
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '获取取餐码失败')
  } finally {
    loading.value = false
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  // 后端返回的是数组 [2026,3,17,...]
  if (Array.isArray(time)) {
    return `${time[0]}-${time[1]}-${time[2]} ${time[3]}:${time[4]}`
  }
  return new Date(time).toLocaleString()
}

onMounted(() => {
  getQrCode()
})
</script>

<style scoped>
.text-center { text-align: center; }
.qr-wrapper {
  position: relative;
  display: inline-block;
  margin: 20px 0;
  padding: 10px;
  background: #fff;
  border: 1px solid #eee;
}
.qr-img { width: 200px; height: 200px; }
.overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.6);
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 24px;
  font-weight: bold;
}
.token { font-family: monospace; background: #f0f0f0; padding: 5px; margin: 10px 0; }
.expire { color: #999; font-size: 13px; }
.tips { color: #FF6B6B; font-size: 14px; }
</style>
