<template>
  <div class="cart-container">
    <el-card v-if="cart.items && cart.items.length > 0">
      <div v-for="item in cart.items" :key="item.foodId" class="cart-item">
        <div class="info">
          <h4>{{ item.foodName }}</h4>
          <p>￥{{ item.price }}</p>
        </div>
        <div class="controls">
          <el-input-number 
            v-model="item.quantity" 
            :min="0" 
            size="small" 
            @change="(val) => handleQuantityChange(item, val)"
          />
        </div>
      </div>
      <div class="total-section">
        <span>总计: ￥{{ cart.totalAmount }}</span>
        <el-button type="danger" size="large" @click="handleCheckout" :loading="loading">去下单</el-button>
      </div>
    </el-card>
    <el-empty v-else description="购物车是空的" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const route = useRoute()
const cart = ref({ items: [], totalAmount: 0 })
const loading = ref(false)

const getCart = async () => {
  try {
    const data = await request.get('/user/cart')
    cart.value = data
  } catch (error) {
    console.error(error)
    ElMessage.error('获取购物车失败')
  }
}

const handleQuantityChange = async (item, val) => {
  try {
    if (val === 0) {
      await request.delete(`/user/cart/items/${item.foodId}`)
      ElMessage.success('已移除商品')
    } else {
      await request.put(`/user/cart/items/${item.foodId}`, {
        quantity: val
      })
    }
    getCart()
  } catch (error) {
    console.error(error)
    ElMessage.error('更新购物车失败')
  }
}

const handleCheckout = async () => {
  if (!cart.value.items || cart.value.items.length === 0) {
    return ElMessage.warning('购物车是空的')
  }

  // 从路由参数获取canteenId，如果没有则尝试从购物车第一项获取
  let canteenId = route.query.canteenId
  if (!canteenId && cart.value.items.length > 0) {
    // 如果后端返回了canteenId信息
    canteenId = cart.value.items[0].canteenId
  }

  if (!canteenId) {
    ElMessage.warning('请先选择食堂')
    router.push('/user/canteens')
    return
  }

  try {
    loading.value = true
    const order = await request.post('/user/orders', {
      canteenId: parseInt(canteenId),
      paymentMethod: 'WECHAT'
    })
    ElMessage.success('下单成功')
    router.push(`/user/orders/${order.id}`)
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '下单失败，请重试')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  getCart()
})
</script>

<style scoped>
.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}
.total-section {
  margin-top: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
}
</style>
