<template>
  <div class="canteen-view-container">
    <div class="left-pane">
      <div v-for="item in canteens" :key="item.canteenId" class="canteen-card" @click="goToFoods(item)">
        <el-card shadow="hover" :class="{ 'active-card': selectedCanteenId === item.canteenId }">
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
    
    <div class="right-pane">
      <div v-if="selectedCanteenId">
        <h2 class="section-title">菜品列表</h2>
        <FoodList :canteenId="selectedCanteenId" />
      </div>
      <div v-else class="empty-state">
        <el-empty description="请先选择左侧的食堂" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import FoodList from './FoodList.vue'

const router = useRouter()
const canteens = ref([])
const loading = ref(false)
const selectedCanteenId = ref(null)

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
    if (data && data.length > 0) {
      selectedCanteenId.value = data[0].canteenId
    }
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
  selectedCanteenId.value = canteen.canteenId
}

onMounted(() => {
  getCanteens()
})
</script>

<style scoped>
.canteen-view-container {
  display: flex;
  gap: 20px;
  /* Instead of simple height: 100%, let's give it a defined max height for scrolling */
  height: calc(100vh - 100px);
}
.left-pane {
  width: 40%;
  overflow-y: auto;
  padding-right: 10px;
}
/* Customize scrollbar for left pane */
.left-pane::-webkit-scrollbar {
  width: 6px;
}
.left-pane::-webkit-scrollbar-thumb {
  background-color: #dcdfe6;
  border-radius: 4px;
}
.right-pane {
  width: 60%;
  overflow-y: auto;
  padding-left: 10px;
  position: relative;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 12px rgba(0,0,0,0.05);
  padding: 20px;
}
.active-card {
  border-color: #409EFF;
  background-color: #f0f9eb;
  transform: scale(1.02);
  transition: all 0.3s ease;
}
.section-title {
  margin-top: 0;
  margin-bottom: 20px;
  color: #303133;
  font-size: 20px;
  font-weight: 600;
  border-bottom: 1px solid #ebeef5;
  padding-bottom: 10px;
}
.empty-state {
  margin-top: 100px;
}
.canteen-card {
  margin-bottom: 15px;
  cursor: pointer;
  transition: all 0.3s ease;
}
.canteen-card:hover {
  transform: translateY(-2px);
}
.card-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.name {
  margin: 0 0 10px 0;
  color: #2d3436;
  font-size: 18px;
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
