<template>
  <div class="statistics">
    <el-card class="mb-20">
      <template #header>
        <div class="card-header">
          <span>销量统计</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
            @change="fetchData"
          />
        </div>
      </template>
      <div ref="chartRef" style="height: 400px"></div>
    </el-card>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>菜品销量排行</template>
          <el-table :data="salesData" stripe>
            <el-table-column prop="foodName" label="菜品" />
            <el-table-column prop="soldQuantity" label="销量" width="100" />
            <el-table-column prop="soldAmount" label="销售额" width="120">
              <template #default="{ row }">￥{{ row.soldAmount }}</template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <template #header>就餐人数统计</template>
          <el-table :data="diningData" stripe>
            <el-table-column prop="date" label="日期" />
            <el-table-column prop="diningCount" label="实际就餐" />
            <el-table-column prop="reservationCount" label="预约人数" />
          </el-card>
        </el-col>
      </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import * as echarts from 'echarts'
import request from '@/utils/request'

const dateRange = ref(['2026-03-01', '2026-03-31'])
const salesData = ref([])
const diningData = ref([])
const chartRef = ref(null)
let myChart = null

const initChart = () => {
  if (chartRef.value) {
    myChart = echarts.init(chartRef.value)
    updateChart()
  }
}

const updateChart = () => {
  const option = {
    tooltip: { trigger: 'axis' },
    xAxis: { type: 'category', data: salesData.value.map(i => i.foodName) },
    yAxis: { type: 'value' },
    series: [{
      data: salesData.value.map(i => i.soldQuantity),
      type: 'bar',
      itemStyle: { color: '#FF6B6B' }
    }]
  }
  myChart.setOption(option)
}

const fetchData = async () => {
  if (!dateRange.value) return
  try {
    const [sales, dining] = await Promise.all([
      request.get('/admin/statistics/food-sales', { params: { startDate: dateRange.value[0], endDate: dateRange.value[1] } }),
      request.get('/admin/statistics/dining', { params: { startDate: dateRange.value[0], endDate: dateRange.value[1] } })
    ])
    salesData.value = sales
    diningData.value = dining
    updateChart()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  initChart()
  fetchData()
})

window.addEventListener('resize', () => myChart && myChart.resize())
</script>

<style scoped>
.mb-20 { margin-bottom: 20px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
