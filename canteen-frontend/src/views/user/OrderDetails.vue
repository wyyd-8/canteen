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
          <el-select v-model="reserveForm.timeSlotId" placeholder="请选择时段" @change="handleSlotChange">
            <el-option 
              v-for="slot in timeSlots" 
              :key="slot.id" 
              :label="`${slot.startTime} - ${slot.endTime}`" 
              :value="slot.id" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="座位">
          <el-select v-model="reserveForm.seatId" placeholder="请选择座位" :disabled="!reserveForm.timeSlotId">
            <el-option 
              v-for="seat in availableSeats" 
              :key="seat.id" 
              :label="`${seat.seatNo} (${seat.seatArea})`" 
              :value="seat.id" 
            />
          </el-select>
        </el-form-item>
        <el-button type="primary" class="w-100" @click="handleReserve" :disabled="!reserveForm.seatId">确认预约</el-button>
      </el-form>
    </el-card>

    <el-card v-if="reservation" class="mb-15 text-center">
      <h3>预约成功!</h3>
      <p>预约号: {{ reservation.reservationCode }}</p>
      <el-button type="danger" @click="goToQrcode">查看取餐码</el-button>
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
const timeSlots = ref([])
const availableSeats = ref([])
const loading = ref(false)

const reserveForm = reactive({
  timeSlotId: '',
  seatId: ''
})

const getOrder = async () => {
  const orderId = route.params.id
  if (!orderId) {
    ElMessage.error('缺少订单ID')
    router.push('/user/canteens')
    return
  }

  try {
    loading.value = true
    const data = await request.get(`/user/orders/${orderId}`)
    orderData.value = data
    if (data.order && data.order.orderStatus === 'PAID') {
      getTimeSlots(data.order.canteenId)
    }
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '获取订单详情失败')
  } finally {
    loading.value = false
  }
}

const getTimeSlots = async (canteenId) => {
  if (!canteenId) return

  try {
    const data = await request.get(`/user/canteens/${canteenId}/time-slots`)
    timeSlots.value = data || []
  } catch (error) {
    console.error(error)
    ElMessage.error('获取时段列表失败')
  }
}

const handleSlotChange = async (slotId) => {
  reserveForm.seatId = ''
  availableSeats.value = []

  if (!slotId) return

  if (!orderData.value || !orderData.value.order || !orderData.value.order.canteenId) {
    ElMessage.error('订单信息异常')
    return
  }

  try {
    const data = await request.get(`/user/canteens/${orderData.value.order.canteenId}/available-seats`, {
      params: { timeSlotId: slotId }
    })
    availableSeats.value = data || []
  } catch (error) {
    console.error(error)
    ElMessage.error('获取座位列表失败')
  }
}

const handlePay = async () => {
  if (!orderData.value || !orderData.value.order || !orderData.value.order.id) {
    return ElMessage.error('订单信息异常')
  }

  try {
    loading.value = true
    await request.post('/user/orders/payment-callback', {
      orderId: orderData.value.order.id
    })
    ElMessage.success('支付成功')
    // 支付成功后重新获取订单详情，触发时段加载
    getOrder()
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '支付失败，请重试')
  } finally {
    loading.value = false
  }
}

const handleReserve = async () => {
  if (!reserveForm.timeSlotId || !reserveForm.seatId) {
    return ElMessage.warning('请选择时段和座位')
  }

  if (!orderData.value || !orderData.value.order) {
    return ElMessage.error('订单信息异常')
  }

  try {
    loading.value = true
    const data = await request.post('/user/reservations', {
      orderId: orderData.value.order.id,
      canteenId: orderData.value.order.canteenId,
      ...reserveForm
    })
    reservation.value = data
    ElMessage.success('预约成功')
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '预约失败，请重试')
  } finally {
    loading.value = false
  }
}

// 跳转到取餐码页面，携带必要的参数
const goToQrcode = () => {
  if (!orderData.value || !orderData.value.order) {
    ElMessage.error('订单信息异常')
    return
  }

  if (!reservation.value || !reservation.value.id) {
    ElMessage.error('预约信息异常')
    return
  }

  router.push({
    path: '/user/qrcode',
    query: {
      orderId: orderData.value.order.id,
      reservationId: reservation.value.id
    }
  })
}

const formatTime = (time) => {
  if (!time) return '-'
  // 处理后端返回的时间格式（可能是数组或标准时间）
  if (Array.isArray(time)) {
    return `${time[0]}-${String(time[1]).padStart(2, '0')}-${String(time[2]).padStart(2, '0')} ${time[3]}:${time[4]}`
  }
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
