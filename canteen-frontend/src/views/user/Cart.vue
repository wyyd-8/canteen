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
        <el-button type="danger" size="large" @click="handleCheckout">去下单</el-button>
      </div>
    </el-card>
    <el-empty v-else description="购物车是空的" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const cart = ref({ items: [], totalAmount: 0 })

const getCart = async () => {
  try {
    const data = await request.get('/user/cart')
    cart.value = data
  } catch (error) {
    console.error(error)
  }
}

const handleQuantityChange = async (item, val) => {
  try {
    if (val === 0) {
      await request.delete(`/user/cart/items/${item.foodId}`)
    } else {
      await request.put(`/user/cart/items/${item.foodId}`, {
        quantity: val
      })
    }
    getCart()
  } catch (error) {
    console.error(error)
  }
}

const handleCheckout = async () => {
  try {
    // 假设下单到第一个食堂，实际应从上下文获取或让用户选
    const order = await request.post('/user/orders', {
      canteenId: 1, 
      paymentMethod: 'WECHAT'
    })
    ElMessage.success('下单成功')
    router.push(`/user/orders/${order.id}`)
  } catch (error) {
    console.error(error)
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
