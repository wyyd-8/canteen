<template>
  <div class="food-list-container">
    <div class="food-items">
      <el-card v-for="food in foods" :key="food.id" class="food-card" :body-style="{ padding: '10px' }">
        <div class="food-info">
          <div class="details">
            <h4 class="name">{{ food.foodName }}</h4>
            <p class="category">{{ food.category }}</p>
          </div>
          <div class="action">
            <div class="price-stock">
              <span class="price">￥{{ food.price }}</span>
              <span class="stock">库存: {{ food.stock }}</span>
            </div>
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
import { ref, onMounted, computed, watch } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const props = defineProps({
  canteenId: {
    type: [Number, String],
    required: true
  }
})

const router = useRouter()
const foods = ref([])
const cart = ref({ items: [], totalAmount: 0 })
const loading = ref(false)

const getFoods = async () => {
  if (!props.canteenId) return
  try {
    loading.value = true
    const data = await request.get(`/user/canteens/${props.canteenId}/foods`)
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
      canteenId: parseInt(props.canteenId)
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
    query: { canteenId: props.canteenId }
  })
}

const cartCount = computed(() => {
  if (!cart.value.items) return 0
  return cart.value.items.reduce((sum, item) => sum + item.quantity, 0)
})

const cartTotal = computed(() => cart.value.totalAmount || 0)

watch(() => props.canteenId, (newVal) => {
  if (newVal) {
    getFoods()
    getCart()
  }
}, { immediate: true })
</script>

<style scoped>
.food-list-container {
  position: relative;
  height: 100%;
}
.food-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 15px;
  padding-bottom: 20px;
}
.food-card {
  border-radius: 8px;
  transition: all 0.3s;
}
.food-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.food-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.name {
  margin: 0 0 5px 0;
  font-size: 16px;
  color: #303133;
}
.category {
  font-size: 12px;
  color: #909399;
  margin: 2px 0;
}
.action {
  display: flex;
  align-items: center;
  gap: 12px;
}
.price-stock {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}
.price {
  color: #F56C6C;
  font-weight: bold;
  font-size: 16px;
}
.stock {
  font-size: 12px;
  color: #C0C4CC;
  margin-top: 2px;
}
.cart-bar {
  position: sticky;
  bottom: 20px;
  left: 0;
  right: 0;
  height: 60px;
  background-color: #303133;
  border-radius: 8px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  color: #fff;
  box-shadow: 0 4px 15px rgba(0,0,0,0.3);
  cursor: pointer;
  z-index: 100;
  margin-top: 20px;
}
.cart-info {
  display: flex;
  align-items: center;
}
.total-price {
  margin-left: 15px;
  font-size: 18px;
  font-weight: bold;
  color: #FFF;
}
</style>
