<template>
  <div class="dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="item in occupancyData" :key="item.canteenId">
        <el-card class="canteen-card" :class="{ 'warning': item.occupancyRate > 0.9 }">
          <template #header>
            <div class="card-header">
              <span>{{ getCanteenName(item.canteenId) }}</span>
              <el-tag :type="item.occupancyRate > 0.9 ? 'danger' : 'success'">
                {{ item.occupancyRate > 0.9 ? '拥挤' : '空闲' }}
              </el-tag>
            </div>
          </template>
          <div class="stats">
            <div class="stat-item">
              <span class="label">就餐人数</span>
              <span class="value">{{ item.currentDiningCount }}</span>
            </div>
            <div class="stat-item">
              <span class="label">预约待入</span>
              <span class="value">{{ item.reservedCountInCurrentSlot }}</span>
            </div>
            <div class="stat-item">
              <span class="label">总容量</span>
              <span class="value">{{ item.capacity }}</span>
            </div>
          </div>
          <el-divider />
          <div class="progress">
            <p>实时占用率: {{ (item.occupancyRate * 100).toFixed(1) }}%</p>
            <el-progress 
              :percentage="Math.min(100, item.occupancyRate * 100)" 
              :status="item.occupancyRate > 0.9 ? 'exception' : ''" 
            />
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import request from '@/utils/request'

const occupancyData = ref([])

const getStats = async () => {
  try {
    const data = await request.get('/admin/canteens/realtime-occupancy')
    occupancyData.value = data
  } catch (error) {
    console.error(error)
  }
}

const getCanteenName = (id) => {
  const names = { 1: '第一食堂', 2: '第二食堂' }
  return names[id] || `食堂 ${id}`
}

onMounted(() => {
  getStats()
  // 30秒轮询一次
  setInterval(getStats, 30000)
})
</script>

<style scoped>
.canteen-card { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.stats { display: flex; justify-content: space-between; margin-bottom: 10px; }
.stat-item { text-align: center; }
.stat-item .label { display: block; font-size: 12px; color: #999; margin-bottom: 5px; }
.stat-item .value { font-size: 18px; font-weight: bold; }
.warning { border: 2px solid #F56C6C; animation: blink 2s infinite; }
@keyframes blink {
  0% { box-shadow: 0 0 0px rgba(245, 108, 108, 0); }
  50% { box-shadow: 0 0 15px rgba(245, 108, 108, 0.5); }
  100% { box-shadow: 0 0 0px rgba(245, 108, 108, 0); }
}
</style>
