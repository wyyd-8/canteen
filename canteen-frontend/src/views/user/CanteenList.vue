<template>
  <div class="canteen-list">
    <div v-for="item in canteens" :key="item.canteenId" class="canteen-card" @click="goToFoods(item)">
      <el-card shadow="hover">
        <div class="card-content">
          <div class="info">
            <h3 class="name">{{ item.canteenName }}</h3>
            <p class="capacity">容纳人数: {{ item.capacity }}</p>
            <p class="reserved">当前预约: {{ item.reservedCount }}</p>
          </div>
          <div class="status">
            <el-progress 
              type="circle" 
              :percentage="calculateRate(item)" 
              :color="customColors"
              :width="60"
            />
            <span class="rate-text">占用率</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const router = useRouter()
const canteens = ref([])
const loading = ref(false)

const customColors = [
  { color: '#67C23A', percentage: 40 },
  { color: '#E6A23C', percentage: 70 },
  { color: '#F56C6C', percentage: 100 }
]

const calculateRate = (item) => {
  if (!item.capacity) return 0
  return Math.round((item.reservedCount / item.capacity) * 100)
}

const getCanteens = async () => {
  try {
    loading.value = true
    const data = await request.get('/user/canteens')
    canteens.value = data
  } catch (error) {
    console.error(error)
    ElMessage.error('获取食堂列表失败')
  } finally {
    loading.value = false
  }
}

const goToFoods = (canteen) => {
  if (!canteen || !canteen.canteenId) {
    ElMessage.error('食堂信息异常')
    return
  }
  router.push({
    path: `/user/canteens/${canteen.canteenId}/foods`,
    query: { canteenName: canteen.canteenName }
  })
}

onMounted(() => {
  getCanteens()
})
</script>

<style scoped>
.canteen-card {
  margin-bottom: 15px;
  cursor: pointer;
}
.card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.name {
  margin: 0 0 10px 0;
  color: #2d3436;
}
.capacity, .reserved {
  margin: 5px 0;
  font-size: 14px;
  color: #636e72;
}
.status {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}
.rate-text {
  font-size: 12px;
  color: #b2bec3;
  margin-top: 5px;
}
</style>
