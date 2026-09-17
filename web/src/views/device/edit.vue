<template>
  <div id="deviceEdit" v-loading="isLoging">
    <el-dialog
      v-el-drag-dialog
      :title="dialogTitle"
      width="40%"
      top="2rem"
      :close-on-click-modal="false"
      :visible.sync="showDialog"
      :destroy-on-close="true"
      @close="close()"
    >
      <div id="shared" style="margin-right: 50px;">
        <el-alert
          v-if="registerHint"
          :title="registerHint"
          type="warning"
          :closable="false"
          show-icon
          style="margin-bottom: 18px;"
        />
        <el-form ref="form" :rules="rules" :model="form" label-width="100px">
          <el-alert
            :title="accessType === 'PLATFORM' ? '下级平台配置说明' : '国标设备配置说明'"
            :description="accessTypeDescription"
            type="info"
            :closable="false"
            show-icon
            style="margin-bottom: 18px;"
          />
          <el-form-item :label="accessType === 'PLATFORM' ? '平台编号' : '设备编号'" prop="deviceId">
            <el-input v-if="isEdit" v-model="form.deviceId" disabled />
            <el-input v-if="!isEdit" v-model="form.deviceId" :placeholder="deviceIdPlaceholder" clearable />
            <div v-if="form.deviceId" class="device-id-hint">{{ deviceIdHint }}</div>
          </el-form-item>

          <el-form-item :label="accessType === 'PLATFORM' ? '平台名称' : '设备名称'" prop="name">
            <el-input v-model="form.name" clearable />
          </el-form-item>
          <el-form-item label="注册密码" prop="password">
            <el-input
              v-model="form.password"
              type="password"
              autocomplete="new-password"
              placeholder="填写下级实际使用的GB28181注册密码"
              show-password
              clearable
            />
          </el-form-item>
          <el-form-item label="收流IP" prop="sdpIp">
            <el-input v-model="form.sdpIp" type="sdpIp" clearable />
          </el-form-item>
          <el-form-item label="流媒体ID" prop="mediaServerId">
            <el-select v-model="form.mediaServerId" style="float: left; width: 100%">
              <el-option key="auto" label="自动负载最小" value="auto" />
              <el-option
                v-for="item in mediaServerList"
                :key="item.id"
                :label="item.id"
                :value="item.id"
              />
            </el-select>
          </el-form-item>

          <el-form-item label="字符集" prop="charset">
            <el-select v-model="form.charset" style="float: left; width: 100%">
              <el-option key="GB2312" label="GB2312" value="gb2312" />
              <el-option key="GB18030" label="GB18030（兼容GBK/GB2312，宇视推荐）" value="gb18030" />
              <el-option key="GBK" label="GBK" value="gbk" />
              <el-option key="UTF-8" label="UTF-8" value="utf-8" />
            </el-select>
          </el-form-item>
          <el-form-item label="坐标系" prop="geoCoordSys">
            <el-select v-model="form.geoCoordSys" style="float: left; width: 100%">
              <el-option key="WGS84" label="WGS84" value="WGS84" />
              <el-option key="GCJ02" label="GCJ02" value="GCJ02" />
            </el-select>
          </el-form-item>
          <el-form-item label="其他选项">
            <el-checkbox v-model="form.ssrcCheck" label="SSRC校验" style="float: left" />
            <el-checkbox v-model="form.asMessageChannel" label="作为消息通道" style="float: left" />
            <el-checkbox v-model="form.broadcastPushAfterAck" label="收到ACK后发流" style="float: left" />
          </el-form-item>
          <el-form-item>
            <div style="float: right;">
              <el-button type="primary" @click="onSubmit">确认</el-button>
              <el-button @click="close">取消</el-button>
            </div>

          </el-form-item>
        </el-form>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import elDragDialog from '@/directive/el-drag-dialog'

export default {
  name: 'DeviceEdit',
  directives: { elDragDialog },
  props: {
    accessType: {
      type: String,
      default: 'DEVICE'
    }
  },
  data() {
    const validateDeviceId = (rule, value, callback) => {
      if (!/^\d{20}$/.test(value || '')) {
        callback(new Error('请输入20位数字国标编号'))
        return
      }
      const isPlatformId = Number(value.substring(10, 13)) >= 200
      if (this.accessType === 'PLATFORM' && !isPlatformId) {
        callback(new Error('该编号属于设备类型，请到“国标设备”中添加'))
        return
      }
      if (this.accessType === 'DEVICE' && isPlatformId) {
        callback(new Error('该编号属于平台类型，请到“下级平台”中添加'))
        return
      }
      callback()
    }
    const validateRegisterPassword = (rule, value, callback) => {
      if (this.registerHint && !value) {
        callback(new Error('待处理注册请求必须填写下级实际使用的注册密码'))
        return
      }
      callback()
    }
    return {
      listChangeCallback: null,
      showDialog: false,
      isLoging: false,
      hostNames: [],
      mediaServerList: [], // 滅体节点列表
      form: {},
      isEdit: false,
      registerHint: '',
      rules: {
        deviceId: [{ required: true, validator: validateDeviceId, trigger: 'blur' }],
        password: [{ validator: validateRegisterPassword, trigger: 'blur' }]
      }
    }
  },
  computed: {
    dialogTitle() {
      const target = this.accessType === 'PLATFORM' ? '下级平台' : '国标设备'
      return this.isEdit ? `编辑${target}` : `配置${target}接入`
    },
    accessTypeDescription() {
      return this.accessType === 'PLATFORM'
        ? '填写下级平台 REGISTER 使用的20位平台编号和密码。平台注册成功后，还需要在下级平台侧选择并共享摄像机资源。'
        : '填写 IPC、NVR 等设备 REGISTER 使用的20位国标编号和密码；请勿在这里添加中心信令服务器或下级平台编号。'
    },
    deviceIdPlaceholder() {
      return this.accessType === 'PLATFORM' ? '例如类型码为200的20位平台编号' : '请输入20位国标设备编号'
    },
    deviceIdHint() {
      if (!/^\d{20}$/.test(this.form.deviceId || '')) {
        return '应为20位数字国标编号'
      }
      const typeCode = Number(this.form.deviceId.substring(10, 13))
      const detected = typeCode >= 200 ? '下级平台' : '国标设备'
      return `识别结果：${detected}（类型码 ${String(typeCode).padStart(3, '0')}）`
    }
  },
  created() {},
  methods: {
    openDialog: function(row, callback, registerHint) {
      this.showDialog = true
      this.isEdit = Boolean(row)
      this.registerHint = registerHint || ''
      this.listChangeCallback = callback
      this.form = row ? Object.assign({}, row) : this.getDefaultForm()
      this.getMediaServerList()
    },
    openDialogForAdd: function(preset, callback, registerHint) {
      this.showDialog = true
      this.isEdit = false
      this.registerHint = registerHint || ''
      this.listChangeCallback = callback
      this.form = Object.assign(this.getDefaultForm(), preset || {})
      this.getMediaServerList()
    },
    getDefaultForm: function() {
      return {
        deviceId: '',
        name: '',
        password: '',
        mediaServerId: 'auto',
        charset: 'gb2312',
        geoCoordSys: 'WGS84',
        streamMode: 'TCP-PASSIVE'
      }
    },
    getMediaServerList: function() {
      this.$store.dispatch('server/getOnlineMediaServerList')
        .then((data) => {
          this.mediaServerList = data
        })
    },
    onSubmit: function() {
      this.$refs.form.validate((valid) => {
        if (!valid) {
          return
        }
        const action = this.isEdit ? 'device/update' : 'device/add'
        this.$store.dispatch(action, this.form)
          .then(() => {
            if (this.listChangeCallback) {
              this.listChangeCallback()
            }
          })
          .catch((error) => {
            this.$message.error(error.message || error)
          })
      })
    },
    close: function() {
      this.showDialog = false
      this.registerHint = ''
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    }
  }
}
</script>

<style scoped>
.device-id-hint {
  color: #909399;
  font-size: 12px;
  line-height: 22px;
}
</style>
