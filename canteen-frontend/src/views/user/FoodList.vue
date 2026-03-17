<template>
  <div class="food-list-container">
    <div class="food-items">
      <el-card v-for="food in foods" :key="food.id" class="food-card" :body-style="{ padding: '10px' }">
        <div class="food-info">
          <div class="details">
            <h4 class="name">{{ food.foodName }}</h4>
            <p class="category">{{ food.category }}</p>
            <p class="price">￥{{ food.price }}</p>
            <p class="stock">库存: {{ food.stock }}</p>
          </div>
          <div class="action">
            <el-button type="danger" circle icon="Plus" @click="addToCart(food)" :disabled="food.stock <= 0" />
          </div>
        </div>
      </el-card>
    </div>

    <div class="cart-bar" v-if="cartTotal > 0" @click="$router.push('/user/cart')">
      <div class="cart-info">
        <el-badge :value="cartCount" class="item">
          <el-icon :size="24"><ShoppingCart /></el-icon>
        </el-badge>
        <span class="total-price">合计: ￥{{ cartTotal }}</span>
      </div>
      <el-button type="danger" round>去结算</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const foods = ref([])
const cart = ref({ items: [], totalAmount: 0 })

const getFoods = async () => {
  try {
    const data = await request.get(`/user/canteens/${route.params.id}/foods`)
    foods.value = data
  } catch (error) {
    console.error(error)
  }
}

const getCart = async () => {
  try {
    const data = await request.get('/user/cart')
    cart.value = data
  } catch (error) {
    console.error(error)
  }
}

const addToCart = async (food) => {
  try {
    await request.post('/user/cart/items', {
      foodId: food.id,
      quantity: 1
    })
    ElMessage.success('已加入购物车')
    getCart()
  } catch (error) {
    console.error(error)
  }
}

const cartCount = computed(() => {
  return cart.value.items.reduce((sum, item) => sum + item.quantity, 0)
})

const cartTotal = computed(() => cart.value.totalAmount)

onMounted(() => {
  getFoods()
  getCart()
})
</script>

<style scoped>
.food-list-container {
  padding-bottom: 80px;
}
.food-card {
  margin-bottom: 10px;
}
.food-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.name {
  margin: 0 0 5px 0;
  font-size: 16px;
}
.category {
  font-size: 12px;
  color: #999;
  margin: 2px 0;
}
.price {
  color: #FF6B6B;
  font-weight: bold;
  margin: 5px 0;
}
.stock {
  font-size: 12px;
  color: #b2bec3;
}
.cart-bar {
  position: fixed;
  bottom: 20px;
  left: 50%;
  transform: translateX(-50%);
  width: 90%;
  max-width: 540px;
  height: 60px;
  background-color: #2d3436;
  border-radius: 30px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  color: #fff;
  box-shadow: 0 4px 15px rgba(0,0,0,0.3);
  cursor: pointer;
  z-index: 1000;
}
.cart-info {
  display: flex;
  align-items: center;
}
.total-price {
  margin-left: 15px;
  font-size: 18px;
  font-weight: bold;
}
</style>
