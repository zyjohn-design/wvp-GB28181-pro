<template>
  <div id="app" style="height: calc(100vh - 124px);">
    <el-form :inline="true" size="mini">
      <el-form-item label="搜索">
        <el-input
          v-model="searchStr"
          style="margin-right: 1rem; width: auto;"
          placeholder="关键字"
          prefix-icon="el-icon-search"
          clearable
          @input="initData"
        />
      </el-form-item>
      <el-form-item label="在线状态">
        <el-select
          v-model="online"
          style="width: 8rem; margin-right: 1rem;"
          placeholder="请选择"
          default-first-option
          @change="initData"
        >
          <el-option label="全部" value="" />
          <el-option label="在线" value="true" />
          <el-option label="离线" value="false" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-plus" style="margin-right: 1rem;" type="primary" @click="add">{{ addButtonText }}</el-button>
        <el-button icon="el-icon-info" style="margin-right: 1rem;" @click="showInfo()">接入信息
        </el-button>
      </el-form-item>
      <el-form-item style="float: right;">
        <el-button
          icon="el-icon-refresh-right"
          circle
          :loading="getDeviceListLoading"
          @click="refreshAll()"
        />
      </el-form-item>
    </el-form>
    <el-alert
      :title="accessGuideTitle"
      :description="accessGuideDescription"
      type="info"
      :closable="false"
      show-icon
      class="access-guide"
    />
    <el-card v-if="registerAttempts.length" class="register-attempt-card" shadow="never">
      <div slot="header" class="register-attempt-header">
        <span><i class="el-icon-warning-outline" /> 发现 {{ registerAttempts.length }} 个待处理注册请求</span>
        <span class="register-attempt-subtitle">请求已到达本服务，但尚未完成接入；注册成功后会自动消失</span>
      </div>
      <el-table :data="registerAttempts" size="mini" max-height="210">
        <el-table-column label="状态" width="90">
          <template v-slot:default="scope">
            <el-tag :type="scope.row.status === 'AUTH_FAILED' ? 'danger' : 'warning'" size="mini">
              {{ scope.row.status === 'AUTH_FAILED' ? '认证失败' : '待配置' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="deviceId" label="REGISTER设备编号" min-width="180" />
        <el-table-column label="来源" min-width="180">
          <template v-slot:default="scope">
            {{ (scope.row.transport || 'SIP').toLowerCase() }}://{{ scope.row.remoteIp }}:{{ scope.row.remotePort }}
          </template>
        </el-table-column>
        <el-table-column label="认证用户名" min-width="170">
          <template v-slot:default="scope">{{ scope.row.authUsername || '—' }}</template>
        </el-table-column>
        <el-table-column prop="message" label="原因" min-width="260" />
        <el-table-column prop="suggestion" label="处理指引" min-width="360" />
        <el-table-column label="最近尝试" min-width="185">
          <template v-slot:default="scope">
            {{ scope.row.lastAttemptTime }}（{{ scope.row.attemptCount }}次）
          </template>
        </el-table-column>
        <el-table-column label="操作" width="145" fixed="right">
          <template v-slot:default="scope">
            <el-button type="text" size="mini" @click="configureRegisterAttempt(scope.row)">配置接入</el-button>
            <el-divider direction="vertical" />
            <el-button type="text" size="mini" class="danger-text" @click="ignoreRegisterAttempt(scope.row)">忽略</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <!--设备列表-->
    <el-table
      size="small"
      :data="deviceList"
      :height="deviceTableHeight"
      header-row-class-name="table-header"
    >
      <el-table-column prop="name" label="名称" min-width="160" />
      <el-table-column prop="deviceId" :label="idColumnLabel" min-width="190" />
      <el-table-column label="接入类型" min-width="150">
        <template v-slot:default="scope">
          <el-tag :type="scope.row.accessType === 'PLATFORM' ? 'warning' : 'success'" size="medium">
            {{ scope.row.accessTypeName }}
          </el-tag>
          <div class="type-name">{{ scope.row.typeCode }} · {{ scope.row.typeName }}</div>
        </template>
      </el-table-column>
      <el-table-column label="地址" min-width="180">
        <template v-slot:default="scope">
          <div slot="reference" class="name-wrapper">
            <el-tag v-if="scope.row.hostAddress" size="medium">{{ scope.row.transport.toLowerCase() }}://{{ scope.row.hostAddress }}</el-tag>
            <el-tag v-if="!scope.row.hostAddress" size="medium">未知</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="manufacturer" label="厂家" min-width="100" />
      <el-table-column label="流传输模式" min-width="160">
        <template v-slot:default="scope">
          <el-select
            v-model="scope.row.streamMode"
            size="mini"
            placeholder="请选择"
            style="width: 120px"
            @change="transportChange(scope.row)"
          >
            <el-option key="UDP" label="UDP" value="UDP" />
            <el-option key="TCP-ACTIVE" label="TCP主动模式" value="TCP-ACTIVE" />
            <el-option key="TCP-PASSIVE" label="TCP被动模式" value="TCP-PASSIVE" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="通道数" min-width="80">
        <template v-slot:default="scope">
          <span style="font-size: 1rem">{{ scope.row.channelCount }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="80">
        <template v-slot:default="scope">
          <div slot="reference" class="name-wrapper">
            <el-tag
              v-if="scope.row.onLine && myServerId !== scope.row.serverId"
              size="medium"
              style="border-color: #ecf1af"
            >在线
            </el-tag>
            <el-tag v-if="scope.row.onLine && myServerId === scope.row.serverId" size="medium">在线
            </el-tag>
            <el-tag v-if="!scope.row.onLine" size="medium" type="info">离线</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="订阅" min-width="240">
        <template v-slot:default="scope">
          <el-checkbox
            label="目录"
            :checked="scope.row.subscribeCycleForCatalog > 0"
            @change="(e)=>subscribeForCatalog(scope.row.id, e)"
          />
          <el-checkbox
            label="位置"
            :checked="scope.row.subscribeCycleForMobilePosition > 0"
            @change="(e)=>subscribeForMobilePosition(scope.row.id, e)"
          />
          <el-checkbox
            label="报警"
            :checked="scope.row.subscribeCycleForAlarm > 0"
            @change="(e)=>subscribeForAlarm(scope.row.id, e)"
          />
        </template>
      </el-table-column>
      <el-table-column label="统计" min-width="140">
        <template v-slot:default="scope">
          <el-button
            type="text"
            size="mini"
            :disabled="scope.row.online===0"
            icon="iconfont-14 icon-xintiao"
            title="心跳时间统计"
            @click="getKeepaliveTimeStatistics(scope.row.deviceId)"
          >心跳
          </el-button>
          <el-button
            type="text"
            size="mini"
            :disabled="scope.row.online===0"
            icon="iconfont-14 icon-register"
            title="注册时间统计"
            @click="getRegisterTimeStatistics(scope.row.deviceId)"
          >注册
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="260" fixed="right">
        <template v-slot:default="scope">
          <el-button
            type="text"
            size="medium"
            :disabled="scope.row.online===0"
            icon="el-icon-refresh"
            @click="refDevice(scope.row)"
            @mouseover="getTooltipContent(scope.row.deviceId)"
          >刷新
          </el-button>
          <el-divider direction="vertical" />
          <el-button
            type="text"
            size="medium"
            icon="el-icon-video-camera"
            @click="showChannelList(scope.row)"
          >{{ scope.row.accessType === 'PLATFORM' ? '资源' : '通道' }}
          </el-button>
          <el-divider direction="vertical" />
          <el-button size="medium" icon="el-icon-edit" type="text" @click="edit(scope.row)">编辑</el-button>
          <el-divider direction="vertical" />
          <el-button size="medium" type="text" style="color: #f56c6c" @click="deleteDevice(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      style="text-align: right"
      :current-page="currentPage"
      :page-size="count"
      :page-sizes="[15, 25, 35, 50]"
      layout="total, sizes, prev, pager, next"
      :total="total"
      @size-change="handleSizeChange"
      @current-change="currentChange"
    />
    <deviceEdit ref="deviceEdit" :access-type="accessType" />
    <syncChannelProgress ref="syncChannelProgress" />
    <configInfo ref="configInfo" />
    <timeStatistics ref="timeStatistics" />
  </div>
</template>

<script>
import deviceEdit from './edit.vue'
import syncChannelProgress from './dialog/SyncChannelProgress.vue'
import configInfo from '../dialog/configInfo.vue'
import timeStatistics from './dialog/timeStatistics.vue'
import Vue from 'vue'

export default {
  name: 'App',
  components: {
    configInfo,
    deviceEdit,
    syncChannelProgress,
    timeStatistics
  },
  props: {
    accessType: {
      type: String,
      default: 'DEVICE'
    }
  },
  data() {
    return {
      deviceList: [], // 设备列表
      currentDevice: {}, // 当前操作设备对象
      searchStr: '',
      online: null,
      videoComponentList: [],
      updateLooper: 0, // 数据刷新轮训标志
      currentDeviceChannelsLength: 0,
      currentPage: 1,
      count: 15,
      total: 0,
      getDeviceListLoading: false,
      registerAttempts: [],
      refreshInProgress: false
    }
  },
  computed: {
    Vue() {
      return Vue
    },
    myServerId() {
      return this.$store.getters.serverId
    },
    deviceTableHeight() {
      return this.registerAttempts.length ? 'calc(100% - 410px)' : 'calc(100% - 124px)'
    },
    isPlatformPage() {
      return this.accessType === 'PLATFORM'
    },
    addButtonText() {
      return this.isPlatformPage ? '添加下级平台' : '添加国标设备'
    },
    idColumnLabel() {
      return this.isPlatformPage ? '下级平台编号' : '设备编号'
    },
    accessGuideTitle() {
      return this.isPlatformPage ? '这里管理向本系统注册的下级平台' : '这里管理直接接入的 IPC、NVR 等国标设备'
    },
    accessGuideDescription() {
      return this.isPlatformPage
        ? '在宇视等下级平台中配置本系统为“上级平台”，完成平台注册后，还必须配置资源共享；平台上报的目录不能播放，只有共享的在线摄像机通道可以播放。'
        : '设备需使用自身20位国标编号向本系统注册。独立摄像机或NVR在这里管理；需要接入组织目录和大量摄像机的平台，请到“下级平台”。'
    }
  },
  watch: {
    accessType() {
      this.currentPage = 1
      this.refreshAll()
    }
  },
  mounted() {
    this.initData()
    this.updateLooper = setInterval(this.refreshAll, 10000)
  },
  destroyed() {
    this.$destroy('videojs')
    clearInterval(this.updateLooper)
  },
  methods: {
    initData: function() {
      this.currentPage = 1
      this.total = 0
      this.refreshAll()
    },
    currentChange: function(val) {
      this.currentPage = val
      this.getDeviceList()
    },
    handleSizeChange: function(val) {
      this.count = val
      this.getDeviceList()
    },
    getDeviceList: function() {
      this.getDeviceListLoading = true
      return this.$store.dispatch('device/queryDevices', {
        page: this.currentPage,
        count: this.count,
        query: this.searchStr,
        status: this.online,
        accessType: this.accessType
      }).then((data) => {
        this.total = data.total
        this.deviceList = data.list
      }).catch((error) => {
          this.$message({
            showClose: true,
            message: error,
            type: 'error'
          })
        }).finally(() => {
        this.getDeviceListLoading = false
      })
    },
    getRegisterAttempts: function() {
      return this.$store.dispatch('device/queryRegisterAttempts')
        .then((data) => {
          this.registerAttempts = (data || []).filter(item => this.getAccessType(item.deviceId) === this.accessType)
        })
        .catch(() => {
          this.registerAttempts = []
        })
    },
    refreshAll: function() {
      if (this.refreshInProgress) {
        return Promise.resolve()
      }
      this.refreshInProgress = true
      // 两个轮询请求串行执行，避免部分现场网络设备重置并发复用的长连接。
      return this.getDeviceList()
        .then(() => this.getRegisterAttempts())
        .finally(() => {
          this.refreshInProgress = false
        })
    },
    getAccessType: function(deviceId) {
      if (!/^\d{20}$/.test(deviceId || '')) {
        return 'DEVICE'
      }
      return Number(deviceId.substring(10, 13)) >= 200 ? 'PLATFORM' : 'DEVICE'
    },
    configureRegisterAttempt: function(row) {
      const hint = `${row.message}。${row.suggestion}`
      this.$store.dispatch('device/queryDeviceOne', row.deviceId)
        .then((device) => {
          const callback = () => {
            this.$refs.deviceEdit.close()
            this.$message.success('配置已保存，正在等待下级重新注册；成功后该状态会自动消失')
            setTimeout(this.refreshAll, 500)
          }
          if (device && device.id) {
            this.$refs.deviceEdit.openDialog(device, callback, hint)
          } else {
            this.$refs.deviceEdit.openDialogForAdd({
              deviceId: row.deviceId,
              name: `${this.isPlatformPage ? '待接入平台' : '待接入设备'} ${row.deviceId}`
            }, callback, hint)
          }
        })
        .catch((error) => {
          this.$message.error(error.message || error)
        })
    },
    ignoreRegisterAttempt: function(row) {
      this.$confirm(`忽略设备 ${row.deviceId} 的这条注册提示？下次注册失败时仍会重新出现。`, '忽略注册提示', {
        type: 'warning'
      }).then(() => {
        return this.$store.dispatch('device/deleteRegisterAttempt', row.deviceId)
      }).then(() => {
        this.getRegisterAttempts()
      })
    },
    deleteDevice: function(row) {
      let msg = '确定删除此设备？'
      if (row.online !== 0) {
        msg = '在线设备删除后仍可通过注册再次上线。<br/>如需彻底删除请先将设备离线。<br/><strong>确定删除此设备？</strong>'
      }
      this.$confirm(msg, '提示', {
        dangerouslyUseHTMLString: true,
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        center: true,
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('device/deleteDevice', row.deviceId)
          .then((data) => {
            this.getDeviceList()
          })
          .catch((error) => {
            this.$message({
              showClose: true,
              message: error,
              type: 'error'
            })
          })
      })
    },
    showChannelList: function(row) {
      this.$emit('show-channel', row.deviceId)
      // this.$router.push(`/device/?deviceId=${row.deviceId}`)
    },
    showDevicePosition: function(row) {
      this.$router.push(`/map?deviceId=${row.deviceId}`)
    },

    // gb28181平台对接
    // 刷新设备信息
    refDevice: function(itemData) {
      console.log('刷新对应设备:' + itemData.deviceId)
      this.$store.dispatch('device/sync', itemData.deviceId)
        .then(data => {
          if (data && data.errorMsg) {
            this.$message({
              showClose: true,
              message: data.errorMsg,
              type: 'error'
            })
            return
          }

          this.$refs.syncChannelProgress.openDialog(itemData.deviceId, () => {
            this.getDeviceList()
          })
        })
        .catch((error) => {
          this.$message({
            showClose: true,
            message: error,
            type: 'error'
          })
        })
        .finally(() => {
          this.getDeviceList()
        })
    },

    getTooltipContent: async function(deviceId) {
      let result = ''
      await this.$store.dispatch('device/queryDeviceSyncStatus', deviceId)
        .then((data) => {
          if (data.errorMsg !== null) {
            result = data.errorMsg
          }
          result = `同步中...[${data.current}/${data.total}]`
        }).catch(error => {
          result = error
        })
      return result
    },
    transportChange: function(row) {
      console.log(`修改传输方式为 ${row.streamMode}：${row.deviceId} `)
      console.log(row.streamMode)
      this.$store.dispatch('device/updateDeviceTransport', [row.deviceId, row.streamMode])
    },
    edit: function(row) {
      this.$refs.deviceEdit.openDialog(row, () => {
        this.$refs.deviceEdit.close()
        this.$message({
          showClose: true,
          message: '设备修改成功，通道字符集将在下次更新生效',
          type: 'success'
        })
        setTimeout(this.getDeviceList, 200)
      })
    },
    add: function() {
      this.$refs.deviceEdit.openDialog(null, () => {
        this.$refs.deviceEdit.close()
        this.$message({
          showClose: true,
          message: '添加成功',
          type: 'success'
        })
        setTimeout(this.getDeviceList, 200)
      })
    },
    showInfo: function() {
      this.$store.dispatch('server/getSystemConfig')
        .then((data) => {
          this.serverId = data.addOn.serverId
          this.$refs.configInfo.openDialog(data)
        })
    },

    subscribeForCatalog: function(data, value) {
      this.$store.dispatch('device/subscribeCatalog', {
        id: data,
        cycle: value ? 60 : 0
      }).then((data) => {
        this.$message.success({
          showClose: true,
          message: value ? '订阅成功' : '取消订阅成功'
        })
      }).catch((error) => {
        this.$message.error({
          showClose: true,
          message: error.message
        })
      })
    },
    subscribeForMobilePosition: function(data, value) {
      this.$store.dispatch('device/subscribeMobilePosition', {
        id: data,
        cycle: value ? 60 : 0,
        interval: value ? 5 : 0
      }).then((data) => {
        this.$message.success({
          showClose: true,
          message: value ? '订阅成功' : '取消订阅成功'
        })
      }).catch((error) => {
        this.$message.error({
          showClose: true,
          message: error.message
        })
      })
    },
    subscribeForAlarm: function(data, value) {
      this.$store.dispatch('device/subscribeForAlarm', {
        id: data,
        cycle: value ? 60 : 0
      }).then((data) => {
        this.$message.success({
          showClose: true,
          message: value ? '订阅成功' : '取消订阅成功'
        })
      }).catch((error) => {
        this.$message.error({
          showClose: true,
          message: error.message
        })
      })
    },
    getKeepaliveTimeStatistics: function(deviceId) {
      this.$refs.timeStatistics.openDialog('心跳时间统计', 'device/getKeepaliveTimeStatistics', deviceId, 60)
    },
    getRegisterTimeStatistics: function(deviceId) {
      this.$refs.timeStatistics.openDialog('注册时间统计', 'device/getRegisterTimeStatistics', deviceId, 10)
    }
  }
}
</script>

<style scoped>
.register-attempt-card {
  margin-bottom: 12px;
  border-color: #f3d19e;
}

.access-guide {
  margin-bottom: 12px;
}

.type-name {
  margin-top: 4px;
  color: #909399;
  font-size: 11px;
}

.register-attempt-card ::v-deep .el-card__header {
  padding: 10px 16px;
  background: #fdf6ec;
}

.register-attempt-card ::v-deep .el-card__body {
  padding: 0;
}

.register-attempt-header {
  color: #b26a00;
  font-weight: 600;
}

.register-attempt-subtitle {
  margin-left: 16px;
  color: #8c8c8c;
  font-size: 12px;
  font-weight: 400;
}

.danger-text {
  color: #f56c6c;
}
</style>
