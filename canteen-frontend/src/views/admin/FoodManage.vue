<template>
  <div class="food-manage">
    <el-card>
      <template #header>
        <div class="card-header">
          <el-radio-group v-model="currentCanteen" @change="getFoods">
            <el-radio-button :label="1">第一食堂</el-radio-button>
            <el-radio-button :label="2">第二食堂</el-radio-button>
          </el-radio-group>
          <el-button type="primary" icon="Plus" @click="handleAdd">新增菜品</el-button>
        </div>
      </template>

      <el-table :data="foods" style="width: 100%">
        <el-table-column prop="foodName" label="菜品名称" />
        <el-table-column prop="category" label="分类" />
        <el-table-column prop="price" label="价格">
          <template #default="{ row }">￥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="stock" label="库存" />
        <el-table-column prop="isOnSale" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.isOnSale ? 'success' : 'info'">
              {{ row.isOnSale ? '上架中' : '已下架' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <el-button size="small" @click="handleEdit(row)">编辑</el-button>
            <el-button 
              size="small" 
              :type="row.isOnSale ? 'warning' : 'success'" 
              @click="handleToggleSale(row)"
            >
              {{ row.isOnSale ? '下架' : '上架' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑对话框 -->
    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑菜品' : '新增菜品'">
      <el-form :model="form" label-width="100px">
        <el-form-item label="菜品名称">
          <el-input v-model="form.foodName" />
        </el-form-item>
        <el-form-item label="分类">
          <el-input v-model="form.category" />
        </el-form-item>
        <el-form-item label="价格">
          <el-input-number v-model="form.price" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item label="库存">
          <el-input-number v-model="form.stock" :min="0" />
        </el-form-item>
        <el-form-item label="是否上架">
          <el-switch v-model="form.isOnSale" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const currentCanteen = ref(1)
const foods = ref([])
const dialogVisible = ref(false)
const form = reactive({
  id: null,
  foodName: '',
  category: '',
  price: 0,
  stock: 0,
  isOnSale: 1
})

const getFoods = async () => {
  try {
    const data = await request.get(`/admin/canteens/${currentCanteen.value}/foods`)
    foods.value = data
  } catch (error) {
    console.error(error)
  }
}

const handleAdd = () => {
  Object.assign(form, { id: null, foodName: '', category: '', price: 0, stock: 0, isOnSale: 1 })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  Object.assign(form, row)
  dialogVisible.value = true
}

const handleToggleSale = async (row) => {
  try {
    const updated = { ...row, isOnSale: row.isOnSale ? 0 : 1 }
    await request.put(`/admin/canteens/${currentCanteen.value}/foods/${row.id}`, updated)
    ElMessage.success('操作成功')
    getFoods()
  } catch (error) {
    console.error(error)
  }
}

const submitForm = async () => {
  try {
    if (form.id) {
      await request.put(`/admin/canteens/${currentCanteen.value}/foods/${form.id}`, form)
    } else {
      await request.post(`/admin/canteens/${currentCanteen.value}/foods`, form)
    }
    ElMessage.success('保存成功')
    dialogVisible.value = false
    getFoods()
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  getFoods()
})
</script>

<style scoped>
.card-header { display: flex; justify-content: space-between; align-items: center; }
</style>
