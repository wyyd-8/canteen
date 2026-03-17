<template>
  <div class="verify-container">
    <el-card class="verify-card">
      <template #header>
        <div class="card-header">
          <span>取餐核销</span>
        </div>
      </template>
      <div class="input-area">
        <el-input 
          v-model="qrToken" 
          placeholder="请输入或扫描取餐码" 
          @keyup.enter="handleVerify"
          clearable
        >
          <template #append>
            <el-button @click="handleVerify">核销</el-button>
          </template>
        </el-input>
      </div>

      <div v-if="result" class="result-area" :class="{ 'error': !result.verified }">
        <el-alert 
          :title="result.message" 
          :type="result.verified ? 'success' : 'error'" 
          show-icon 
          :closable="false"
        />
        
        <div v-if="result.verified" class="details">
          <div class="section">
            <h4>用户信息</h4>
            <p>用户: {{ result.userName }} (ID: {{ result.userId }})</p>
          </div>
          <div class="section">
            <h4>就餐位置</h4>
            <div class="seat-info">
              <span class="seat">{{ result.seatNo }}</span>
              <span class="canteen">{{ result.canteenName }}</span>
              <span class="time">{{ result.timeSlot }}</span>
            </div>
          </div>
          <div class="section">
            <h4>订单详情</h4>
            <p>订单号: {{ result.orderNo }}</p>
            <el-table :data="result.items" size="small" border>
              <el-table-column prop="foodNameSnapshot" label="菜品" />
              <el-table-column prop="quantity" label="数量" width="60" />
              <el-table-column prop="subtotalAmount" label="小计" width="80" />
            </el-table>
          </div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const qrToken = ref('')
const result = ref(null)

const handleVerify = async () => {
  if (!qrToken.value) return
  try {
    const data = await request.post('/admin/pickup-qrcodes/scan', {
      qrToken: qrToken.value,
      operatorId: 1 // 实际应从 store 获取
    })
    result.value = data
    if (data.verified) {
      ElMessage.success('核销成功')
      qrToken.value = '' // 清空输入框
    } else {
      ElMessage.error(data.message)
    }
  } catch (error) {
    console.error(error)
  }
}
</script>

<style scoped>
.verify-container { max-width: 800px; margin: 0 auto; }
.input-area { margin-bottom: 20px; }
.result-area { margin-top: 20px; }
.details { margin-top: 20px; }
.section { margin-bottom: 20px; }
.section h4 { border-left: 4px solid #FF6B6B; padding-left: 10px; margin-bottom: 10px; }
.seat-info { background: #fff; padding: 20px; text-align: center; border-radius: 8px; border: 1px dashed #FF6B6B; }
.seat { display: block; font-size: 48px; font-weight: bold; color: #FF6B6B; }
.canteen { font-size: 18px; color: #636e72; }
.time { display: block; color: #999; margin-top: 5px; }
</style>
