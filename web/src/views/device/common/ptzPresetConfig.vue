<template>
  <div style="height: 100%; display: flex; flex-direction: column;">
    <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 6px;">
      <div>
        <el-button type="primary" :disabled="showAddForm" @click="openAdd">添加预置点</el-button>
        <el-button :loading="clearing" :disabled="clearing" @click="clearAll">清空</el-button>
      </div>
      <el-button icon="el-icon-refresh-right" circle @click="getPresetList" />
    </div>
    <el-form v-if="showAddForm" size="small" inline style="margin-bottom: 6px; padding: 16px 8px; border: 1px solid #e6e6e6; border-radius: 4px; display: flex; align-items: center; flex-wrap: wrap; gap: 12px;">
      <el-form-item label="序号" style="margin-bottom: 0; margin-right: 2rem">
        <el-input-number v-model="addPresetId" :min="1" :max="255" controls-position="right" style="width: 180px" />
      </el-form-item>
      <el-form-item label="名称备注" style="margin-bottom: 0;">
        <el-input v-model="addPresetName" :maxlength="128" :placeholder="'不填自动生成：预置点' + (addPresetId || '')" clearable style="width: 240px" />
      </el-form-item>
      <el-form-item style="margin-bottom: 0;">
        <el-button type="primary" :loading="submitting" :disabled="submitting" @click="confirmAdd">确定</el-button>
        <el-button @click="cancelAdd">取消</el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="presetList"
      border
      stripe
      max-height="100%"
      height="100%"
      style="flex: 1"
    >
      <el-table-column prop="presetId" label="序号" align="center" />
      <el-table-column label="名称备注">
        <template v-slot="{ row }">
          <span>{{ row.presetName || ('预置点' + row.presetId) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="220" align="center">
        <template v-slot="{ row }">
          <el-button size="mini" type="text" @click="callPreset(row)">调用</el-button>
          <el-button size="mini" type="text" @click="editPresetName(row)">修改名称</el-button>
          <el-button size="mini" type="text" style="color: #F56C6C" :loading="deletingId === row.presetId" :disabled="deletingId !== null" @click="delPreset(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { getNextAvailablePresetId, parsePresetId } from '@/utils/presetId'

export default {
  name: 'PtzPresetConfig',
  props: {
    deviceId: { type: String, default: null },
    channelDeviceId: { type: String, default: null }
  },
  data() {
    return {
      presetList: [],
      showAddForm: false,
      addPresetId: 1,
      addPresetName: '',
      submitting: false,
      clearing: false,
      deletingId: null
    }
  },
  created() {
    this.getPresetList()
  },
  methods: {
    getPresetList() {
      this.$store.dispatch('frontEnd/queryPreset', [this.deviceId, this.channelDeviceId])
        .then(data => {
          this.presetList = data || []
        })
        .catch(error => {
          console.log(error)
        })
    },
    openAdd() {
      this.addPresetName = ''
      this.addPresetId = this.getNextAvailableId()
      this.showAddForm = true
      if (this.addPresetId === null) {
        this.$message({ showClose: true, message: '无法自动分配序号，请手动输入1-255的整数', type: 'warning' })
      }
    },
    cancelAdd() {
      this.showAddForm = false
      this.addPresetId = 1
      this.addPresetName = ''
    },
    confirmAdd() {
      const id = parsePresetId(this.addPresetId)
      if (id === null) {
        this.$message({ showClose: true, message: '预置位编号必须为1-255之间的整数', type: 'warning' })
        return
      }
      const exists = this.presetList.some(p => parsePresetId(p.presetId) === id)
      if (exists) {
        this.$message({ showClose: true, message: '序号 ' + id + ' 已存在', type: 'warning' })
        return
      }
      this.submitting = true
      this.$store.dispatch('frontEnd/addPreset', [this.deviceId, this.channelDeviceId, id, this.addPresetName])
        .then(() => {
          this.showAddForm = false
          setTimeout(() => {
            this.getPresetList()
          }, 600)
        }).catch(error => {
          this.$message({ showClose: true, message: error, type: 'error' })
        }).finally(() => {
          this.submitting = false
        })
    },
    editPresetName(preset) {
      const id = parsePresetId(preset.presetId)
      if (id === null) {
        this.$message({ showClose: true, message: '预置位编号无效，无法修改名称', type: 'warning' })
        return
      }
      this.$prompt('填写位置或标志信息，不填则自动生成名称', '修改预置位名称备注', {
        inputValue: preset.presetName || '',
        inputValidator: value => !value || value.trim().length <= 128 || '名称备注不能超过128个字符',
        confirmButtonText: '保存',
        cancelButtonText: '取消'
      }).then(({ value }) => {
        return this.$store.dispatch('frontEnd/setPresetRemark', [this.deviceId, this.channelDeviceId, id, value])
      }).then(() => {
        this.$message({ showClose: true, message: '名称备注已保存', type: 'success' })
        this.getPresetList()
      }).catch(error => {
        if (error !== 'cancel' && error !== 'close') {
          this.$message({ showClose: true, message: error, type: 'error' })
        }
      })
    },
    callPreset(preset) {
      const id = parsePresetId(preset.presetId)
      if (id === null) {
        this.$message({ showClose: true, message: '设备返回的预置位编号不是1-255的整数，无法调用', type: 'warning' })
        return
      }
      this.$store.dispatch('frontEnd/callPreset', [this.deviceId, this.channelDeviceId, id])
        .then(() => {
          this.$message({ showClose: true, message: '设备已接收预置位调用指令', type: 'success' })
        }).catch(error => {
          this.$message({ showClose: true, message: error, type: 'error' })
        })
    },
    delPreset(preset) {
      const id = parsePresetId(preset.presetId)
      if (id === null) {
        this.$message({ showClose: true, message: '设备返回的预置位编号不是1-255的整数，无法删除', type: 'warning' })
        return
      }
      this.$confirm('确定删除此预置位', '提示', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deletingId = preset.presetId
        this.$store.dispatch('frontEnd/deletePreset', [this.deviceId, this.channelDeviceId, id])
          .then(() => {
            this.getPresetList()
          }).catch(error => {
            this.$message({ showClose: true, message: error, type: 'error' })
          }).finally(() => {
            this.deletingId = null
          })
      }).catch(() => {})
    },
    clearAll() {
      if (this.presetList.length === 0) return
      if (this.presetList.some(p => parsePresetId(p.presetId) === null)) {
        this.$message({ showClose: true, message: '列表含无效预置位编号，无法清空', type: 'warning' })
        return
      }
      this.$confirm('确定清空所有预置点?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.clearing = true
        const promises = this.presetList.map(p =>
          this.$store.dispatch('frontEnd/deletePreset', [this.deviceId, this.channelDeviceId, parsePresetId(p.presetId)])
        )
        Promise.all(promises).then(() => {
          this.getPresetList()
          this.$message({ showClose: true, message: '设备已接收全部预置位删除指令', type: 'success' })
        }).catch(() => {
          this.$message({ showClose: true, message: '清空失败', type: 'error' })
        }).finally(() => {
          this.clearing = false
        })
      }).catch(() => {})
    },
    getNextAvailableId() {
      return getNextAvailablePresetId(this.presetList)
    }
  }
}
</script>
