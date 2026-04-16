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

    <div class="cart-bar" v-if="cartTotal > 0" @click="goToCart">
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
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const foods = ref([])
const cart = ref({ items: [], totalAmount: 0 })
const loading = ref(false)

// 获取当前食堂ID
const canteenId = computed(() => route.params.id)

const getFoods = async () => {
  try {
    loading.value = true
    const data = await request.get(`/user/canteens/${canteenId.value}/foods`)
    foods.value = data
  } catch (error) {
    console.error(error)
    ElMessage.error('获取菜品列表失败')
  } finally {
    loading.value = false
  }
}

const getCart = async () => {
  try {
    const data = await request.get('/user/cart')
    cart.value = data
  } catch (error) {
    console.error(error)
    // 购物车获取失败不阻塞主流程
  }
}

const addToCart = async (food) => {
  if (!food || !food.id) {
    return ElMessage.error('菜品信息异常')
  }

  if (food.stock <= 0) {
    return ElMessage.warning('该菜品已售罄')
  }

  try {
    await request.post('/user/cart/items', {
      foodId: food.id,
      quantity: 1,
      canteenId: parseInt(canteenId.value)
    })
    ElMessage.success('已加入购物车')
    getCart()
  } catch (error) {
    console.error(error)
    ElMessage.error(error.response?.data?.message || '加入购物车失败')
  }
}

const goToCart = () => {
  router.push({
    path: '/user/cart',
    query: { canteenId: canteenId.value }
  })
}

const cartCount = computed(() => {
  if (!cart.value.items) return 0
  return cart.value.items.reduce((sum, item) => sum + item.quantity, 0)
})

const cartTotal = computed(() => cart.value.totalAmount || 0)

onMounted(() => {
  if (!canteenId.value) {
    ElMessage.error('缺少食堂ID参数')
    router.push('/user/canteens')
    return
  }
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
