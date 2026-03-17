<template>
  <div class="order-details" v-if="orderData">
    <el-card class="mb-15">
      <template #header>订单状态: {{ orderData.order.orderStatus }}</template>
      <div class="info-row">订单编号: {{ orderData.order.orderNo }}</div>
      <div class="info-row">下单时间: {{ formatTime(orderData.order.createdAt) }}</div>
      <div class="info-row total">合计金额: ￥{{ orderData.order.totalAmount }}</div>
    </el-card>

    <el-card v-if="orderData.order.orderStatus === 'CREATED'" class="mb-15">
      <el-button type="success" size="large" class="w-100" @click="handlePay">模拟支付</el-button>
    </el-card>

    <el-card v-if="orderData.order.orderStatus === 'PAID'" class="mb-15">
      <template #header>预约就餐</template>
      <el-form label-width="80px">
        <el-form-item label="时段">
          <el-select v-model="reserveForm.timeSlotId" placeholder="请选择时段">
            <el-option label="11:00 - 12:00" :value="1" />
            <el-option label="12:00 - 13:00" :value="2" />
          </el-select>
        </el-form-item>
        <el-form-item label="座位">
          <el-select v-model="reserveForm.seatId" placeholder="请选择座位">
            <el-option label="A-001 (靠窗)" :value="1" />
            <el-option label="A-002" :value="2" />
          </el-select>
        </el-form-item>
        <el-button type="primary" class="w-100" @click="handleReserve">确认预约</el-button>
      </el-form>
    </el-card>

    <el-card v-if="reservation" class="mb-15 text-center">
      <h3>预约成功!</h3>
      <p>预约号: {{ reservation.reservationCode }}</p>
      <el-button type="danger" @click="$router.push('/user/qrcode')">查看取餐码</el-button>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const orderData = ref(null)
const reservation = ref(null)

const reserveForm = reactive({
  timeSlotId: '',
  seatId: ''
})

const getOrder = async () => {
  try {
    const data = await request.get(`/user/orders/${route.params.id}`)
    orderData.value = data
  } catch (error) {
    console.error(error)
  }
}

const handlePay = async () => {
  try {
    await request.post('/user/orders/payment-callback', {
      orderId: orderData.value.order.id
    })
    ElMessage.success('支付成功')
    getOrder()
  } catch (error) {
    console.error(error)
  }
}

const handleReserve = async () => {
  if (!reserveForm.timeSlotId || !reserveForm.seatId) {
    return ElMessage.warning('请选择时段和座位')
  }
  try {
    const data = await request.post('/user/reservations', {
      orderId: orderData.value.order.id,
      canteenId: orderData.value.order.canteenId,
      ...reserveForm
    })
    reservation.value = data
    ElMessage.success('预约成功')
  } catch (error) {
    console.error(error)
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString()
}

onMounted(() => {
  getOrder()
})
</script>

<style scoped>
.mb-15 { margin-bottom: 15px; }
.w-100 { width: 100%; }
.info-row { margin: 10px 0; color: #636e72; }
.total { font-size: 18px; font-weight: bold; color: #FF6B6B; }
.text-center { text-align: center; }
</style>
