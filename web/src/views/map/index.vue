<template>
  <div id="devicePosition" style="height: calc(100vh - 84px); width: 100%;">
    <div style="height: 100%; display: grid; grid-template-columns: 360px auto">
      <DeviceTree ref="deviceTree" @clickEvent="treeChannelClickEvent" :showPosition="true" :contextmenu="getContextmenu()"/>
      <div class="hi-map-area">
        <!-- HiMap 电子地图：通过 iframe 加载前端管理的地图入口页 -->
        <HiMap
          ref="hiMap"
          :maptype="maptype"
          :init-map-obj="true"
          @loading="onMapLoading"
          @ready="onMapReady"
          @timeout="onMapTimeout"
          @loadwarning="onMapLoadWarning"
        />

        <div v-if="mapReady && (channelLoading || channelLoadError || channels.length === 0)" class="channel-load-status" role="status">
          <span>{{ channelLoading ? '正在加载摄像头点位…' : (channelLoadError || '暂无有效坐标的摄像头点位') }}</span>
          <el-button v-if="!channelLoading" type="text" @click="loadChannels">重新加载点位</el-button>
        </div>
        <!-- 通道信息框：定位在地图对应的经纬度上方 -->
        <transition name="el-zoom-in-center">
          <div ref="infoBox" class="map-infobox" :style="infoBoxStyle" v-if="channel">
            <div class="infobox-content">
              <el-descriptions class="margin-top" :title="channel.gbName" :column="1" :colon="true" size="mini" :labelStyle="labelStyle" >
                <el-descriptions-item label="编号" >{{channel.gbDeviceId}}</el-descriptions-item>
                <el-descriptions-item label="生产厂商">{{channel.gbManufacture}}</el-descriptions-item>
                <el-descriptions-item label="安装地址" >{{channel.gbAddress == null ? '未知' : channel.gbAddress}}</el-descriptions-item>
              </el-descriptions>
              <div style="padding-top: 10px; margin: 0 auto; width: fit-content;">
                <el-button v-bind:disabled="channel.gbStatus !== 'ON'" type="primary" size="small" title="播放" icon="el-icon-video-play" @click="play(channel)">播放</el-button>
                <el-button type="primary" size="small" title="编辑" icon="el-icon-edit" @click="edit(channel)">编辑</el-button>
                <el-button type="primary" size="small" title="位置" icon="el-icon-coordinate" @click="editPosition(channel)">位置</el-button>
              </div>
              <span class="infobox-close el-icon-close" @click="closeInfoBox"></span>
            </div>
          </div>
        </transition>

        <!-- 通道位置编辑框：HiMap 以 iframe 承载地图，无法直接拖拽图标，改为输入经纬度后保存 -->
        <transition name="el-zoom-in-center">
          <div ref="editBox" class="map-infobox" :style="editBoxStyle" v-if="dragChannel">
            <div class="infobox-edit-content">
              <div style="width: 100%; line-height: 1.5rem; font-size: 14px">{{dragChannel.gbName}}  ({{dragChannel.gbDeviceId}})</div>
              <span style="font-size: 14px">经度:</span> <el-input v-model="dragChannel.gbLongitude" placeholder="请输入经度" style="width: 7rem; margin-right: 10px"></el-input>
              <span style="font-size: 14px">纬度: </span> <el-input v-model="dragChannel.gbLatitude" placeholder="请输入纬度" style="width: 7rem; "></el-input>
              <el-button icon="el-icon-close" size="medium" type="text" @click="cancelEdit(dragChannel)" style="margin-left: 1rem; font-size: 18px; color: #2b2f3a"></el-button>
              <el-button icon="el-icon-check" size="medium" type="text" @click="submitEdit(dragChannel)" style="font-size: 18px; color: #0842e2"></el-button>
            </div>
          </div>
        </transition>
      </div>
    </div>
    <div class="map-tool-box-bottom-right">
      <div class="map-tool-btn-group">
        <div class="map-tool-btn" title="重新加载地图" @click="$refs.hiMap.reload()">
          <i class="el-icon-refresh"></i>
        </div>
      </div>
      <div class="map-tool-btn-group" v-if="mapReady">
        <el-dropdown placement="top" @command="changeMapType">
          <div class="el-dropdown-link map-tool-btn" title="底图切换">
            <i class="iconfont icon-mti-dituqiehuan"></i>
          </div>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="arcgis">
              <span v-if="maptype !== 'arcgis'">浅色底图</span>
              <span v-if="maptype === 'arcgis'" style="color: rgb(64, 158, 255);">浅色底图</span>
            </el-dropdown-item>
            <el-dropdown-item command="arcgisDark">
              <span v-if="maptype !== 'arcgisDark'">深色底图</span>
              <span v-if="maptype === 'arcgisDark'" style="color: rgb(64, 158, 255);">深色底图</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div class="map-tool-btn-group" v-if="mapReady">
        <div class="map-tool-btn" title="定位全部通道" @click="fitAllChannels">
          <i class="iconfont icon-guangquan"></i>
        </div>
      </div>
      <div class="map-tool-btn-group" v-if="mapReady">
        <div class="map-tool-btn" title="刷新通道" @click="refreshChannelLayer">
          <i class="iconfont icon-shuaxin3"></i>
        </div>
      </div>
      <div class="map-tool-btn-group" v-if="mapReady">
        <div class="map-tool-btn" title="放大" @click="zoomIn">
          <i class="iconfont icon-plus1"></i>
        </div>
        <div class="map-tool-btn" title="缩小" @click="zoomOut">
          <i class="iconfont icon-minus1"></i>
        </div>
      </div>
    </div>
    <player ref="player" ></player>
    <CommonChannelEditDialog ref="commonChannelEditDialog" ></CommonChannelEditDialog>
  </div>
</template>

<script>
import DeviceTree from '../common/DeviceTree.vue'
import HiMap from '@/components/HiMap/index.vue'
import player from '../channel/player.vue'
import CommonChannelEditDialog from '../dialog/commonChannelEditDialog.vue'
import { getZoomLevelOfPolygons } from '@/utils/himap-utils'
import { createChannelMapLayer } from '@/utils/channel-map-layer'

// 定位单个通道时的目标层级，实际会被引擎最大层级截断
const LOCATE_LEVEL = 16

export default {
  name: 'Map',
  components: {
    CommonChannelEditDialog,
    DeviceTree,
    HiMap,
    player
  },
  data() {
    return {
      // 底图类型，浅色为默认
      maptype: 'arcgis',
      // 地图是否已就绪
      mapReady: false,
      // 有位置的通道列表
      channels: [],
      channelLoading: false,
      channelLoadError: '',
      // 信息框当前展示的通道
      channel: null,
      // 位置编辑中的通道
      dragChannel: null,
      // 信息框锚点经纬度与换算后的屏幕坐标
      infoBoxPoint: null,
      infoBoxPixel: null,
      editBoxPoint: null,
      editBoxPixel: null,
      labelStyle: {
        width: '56px'
      }
    }
  },
  computed: {
    infoBoxStyle() {
      return this.buildBoxStyle(this.infoBoxPixel)
    },
    editBoxStyle() {
      return this.buildBoxStyle(this.editBoxPixel)
    }
  },
  created() {
    this.channelRequestId = 0
    this.mapApp = null
    this.channelLayer = null
    this.boxFrame = null
  },
  activated() {
    // 页面被 KeepAlive 缓存后再次进入，重取点位，恢复上一次失败的接口请求。
    if (this.mapReady) {
      this.attachMapListener()
      this.loadChannels()
    }
  },
  deactivated() {
    this.channelRequestId++
    this.detachMapListener()
  },
  beforeDestroy() {
    this.channelRequestId++
    this.detachMapListener()
    this.disposeChannelLayer()
    this.mapApp = null
  },
  methods: {
    /**
     * 把屏幕像素坐标转换为弹窗的绝对定位样式
     */
    buildBoxStyle(pixel) {
      if (!pixel) {
        return { display: 'none' }
      }
      return {
        position: 'absolute',
        left: '0',
        top: '0',
        transform: `translate3d(${pixel.x}px, ${pixel.y - 12}px, 0) translate(-50%, -100%)`,
        zIndex: 10
      }
    },
    /**
     * 地图就绪：注册状态变化监听并加载通道点位
     */
    detachMapListener() {
      if (this.boxFrame !== null) window.cancelAnimationFrame(this.boxFrame)
      this.boxFrame = null
      if (this.mapApp && this.mapChangeHandler && typeof this.mapApp.removeMapChangeListener === 'function') {
        try {
          this.mapApp.removeMapChangeListener(this.mapChangeHandler)
        } catch (error) {
          console.warn('[电子地图] 移除旧地图监听失败', error)
        }
      }
      this.mapChangeHandler = null
    },
    onMapLoading() {
      this.channelRequestId++
      this.detachMapListener()
      this.disposeChannelLayer()
      this.closeInfoBox()
      this.dragChannel = null
      this.mapApp = null
      this.mapReady = false
      this.channelLoading = false
      this.channelLoadError = ''
    },
    onMapReady(mapApp) {
      this.detachMapListener()
      this.disposeChannelLayer()
      this.mapApp = mapApp
      this.mapReady = true
      this.attachMapListener()
      this.loadChannels()
    },
    attachMapListener() {
      if (!this.mapApp || this.mapChangeHandler) return
      this.mapChangeHandler = () => this.scheduleBoxPosition()
      try {
        if (typeof this.mapApp.addMapChangeListener === 'function') this.mapApp.addMapChangeListener(this.mapChangeHandler)
      } catch (error) {
        console.warn('[电子地图] 注册地图移动监听失败', error)
      }
      this.scheduleBoxPosition()
    },
    scheduleBoxPosition() {
      if ((!this.infoBoxPoint && !this.editBoxPoint) || this.boxFrame !== null) return
      this.boxFrame = window.requestAnimationFrame(() => {
        this.boxFrame = null
        this.repositionBoxes(false)
      })
    },
    disposeChannelLayer() {
      if (!this.channelLayer) return
      const layer = this.channelLayer
      this.channelLayer = null
      try { layer.dispose() } catch (error) { console.warn('[电子地图] 移除点图层失败', error) }
    },
    /**
     * 地图加载超时，提示用户检查 HiMap 服务
     */
    onMapTimeout() {
      this.mapReady = false
    },
    onMapLoadWarning() {
      console.warn('[电子地图] 底图仍在加载，尚未请求摄像头点位')
    },
    /**
     * 重新计算信息框与编辑框的屏幕位置
     */
    repositionBoxes(updateState = true) {
      if (!this.mapApp) return
      for (const [point, refName, pixelName] of [
        [this.infoBoxPoint, 'infoBox', 'infoBoxPixel'], [this.editBoxPoint, 'editBox', 'editBoxPixel']
      ]) {
        if (!point) continue
        const pixel = this.mapApp.mapCoordToPixel(point[0], point[1])
        if (updateState) {
          this[pixelName] = pixel
        } else {
          // 拖动中只更新可见弹窗的合成变换，不触发 Vue 重渲染设备树和整页。
          const element = this.$refs[refName]
          if (element) {
            element.style.display = pixel ? '' : 'none'
            if (pixel) element.style.transform = this.buildBoxStyle(pixel).transform
          }
        }
      }
    },
    /**
     * 判断通道是否具备可用位置
     */
    hasPosition(channel) {
      if (!channel) return false
      const longitude = Number(channel.gbLongitude)
      const latitude = Number(channel.gbLatitude)
      return Number.isFinite(longitude) && Number.isFinite(latitude) &&
        longitude > 0 && longitude <= 180 && latitude > 0 && latitude <= 90
    },
    /**
     * 加载有位置的通道并绘制到地图上
     */
    loadChannels() {
      if (!this.mapReady || !this.mapApp) return Promise.resolve()
      const requestId = ++this.channelRequestId
      const mapApp = this.mapApp
      this.channelLoading = true
      this.channelLoadError = ''
      return this.$store.dispatch('commonChanel/getAllForMap', {})
        .then(data => {
          if (requestId !== this.channelRequestId || mapApp !== this.mapApp) return
          if (!Array.isArray(data)) throw new Error('点位接口返回格式异常')
          this.channels = Object.freeze(data.filter(channel => this.hasPosition(channel)))
          this.drawChannels()
        })
        .catch(error => {
          if (requestId !== this.channelRequestId || mapApp !== this.mapApp) return
          this.channelLoadError = '摄像头点位加载失败，请重试'
          console.error('[电子地图] 点位请求或绘制失败', error)
        })
        .finally(() => {
          if (requestId === this.channelRequestId) this.channelLoading = false
        })
    },
    /** 图层只创建一次；相同点位无需重绘，拖动完全交给地图引擎。 */
    drawChannels() {
      if (!this.mapApp) return
      if (!this.channelLayer) {
        const base = process.env.BASE_URL || '/'
        this.channelLayer = createChannelMapLayer(this.mapApp, {
          iconBase: base + 'static/images/gis/',
          onClick: channel => this.$nextTick(() => this.showChannelInfo(channel))
        })
      }
      this.channelLayer.update(this.channels)
    },
    /**
     * 重新拉取通道并重绘点位
     */
    refreshChannelLayer() {
      this.closeInfoBox()
      this.loadChannels()
    },
    /**
     * 定位到全部有位置的通道
     */
    fitAllChannels() {
      if (!this.mapApp || this.channels.length === 0) {
        this.$message.warning({
          showClose: true,
          message: '暂无可定位的通道'
        })
        return
      }
      const coords = this.channels.map(channel => channel.gbLongitude + ',' + channel.gbLatitude)
      const { center, level } = getZoomLevelOfPolygons(coords, this.mapApp)
      if (center) {
        this.mapApp.centerAndZoom(center, level || 4)
      }
    },
    /**
     * 左侧设备树点击通道：定位并展示信息框
     */
    treeChannelClickEvent: function (id) {
      this.closeInfoBox()
      this.$store.dispatch('commonChanel/queryOne', id)
        .then(data => {
          if (!this.hasPosition(data)) {
            this.$message.warning({
              showClose: true,
              message: '无位置信息'
            })
            return
          }
          const maxLevel = (this.mapApp && this.mapApp.getMaxLevel()) || LOCATE_LEVEL
          const level = Math.min(LOCATE_LEVEL, maxLevel)
          this.mapApp && this.mapApp.centerAndZoom(data.gbLongitude + ',' + data.gbLatitude, level)
          this.showChannelInfo(data)
        })
        .catch(error => {
          this.$message({
            showClose: true,
            message: error,
            type: 'error'
          })
        })
    },
    zoomIn: function() {
      this.$refs.hiMap.zoomIn()
    },
    zoomOut: function() {
      this.$refs.hiMap.zoomOut()
    },
    /**
     * 切换浅色/深色底图，HiMap 组件会重新加载 iframe 并再次抛出 ready
     */
    changeMapType(command) {
      if (command === this.maptype) {
        return
      }
      this.onMapLoading()
      this.maptype = command
    },
    getContextmenu: function (event) {
        return [
          {
            label: '播放通道',
            icon: 'el-icon-video-play',
            type: 1,
            onClick: (event, data, node) => {
              this.$store.dispatch('commonChanel/queryOne', data.id)
                .then(data => {
                  this.play(data)
                })
                .catch((error) => {
                  this.$message({
                    showClose: true,
                    message: error,
                    type: 'error'
                  })
                })
            }
          },
          {
            label: '修改位置',
            icon: 'el-icon-coordinate',
            type: 1,
            onClick: (event, data, node) => {
              this.$store.dispatch('commonChanel/queryOne', data.id)
                .then(data => {
                  this.editPosition(data)
                })
                .catch((error) => {
                  this.$message({
                    showClose: true,
                    message: error,
                    type: 'error'
                  })
                })
            }
          },
          {
            label: '编辑通道',
            icon: 'el-icon-edit',
            type: 1,
            onClick: (event, data, node) => {
              this.$store.dispatch('commonChanel/queryOne', data.id)
                .then(data => {
                  this.edit(data)
                })
                .catch((error) => {
                  this.$message({
                    showClose: true,
                    message: error,
                    type: 'error'
                  })
                })
            }
          }
        ]
    },
    showChannelInfo: function(data) {
      this.channel = data
      this.infoBoxPoint = [Number(data.gbLongitude), Number(data.gbLatitude)]
      this.repositionBoxes()
    },
    closeInfoBox: function () {
      this.channel = null
      this.dragChannel = null
      this.infoBoxPoint = null
      this.infoBoxPixel = null
      this.editBoxPoint = null
      this.editBoxPixel = null
    },
    play: function (channel) {
      const loading = this.$loading({
        lock: true,
        text: '正在请求视频',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      })
      this.$store.dispatch('commonChanel/playChannel', channel.gbId)
        .then((data) => {
          this.$refs.player.openDialog('media', channel.gbId, {
            streamInfo: data,
            hasAudio: channel.hasAudio
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
          loading.close()
        })
    },
    edit: function (channel) {
      this.$refs.commonChannelEditDialog.openDialog(channel.gbId)
    },
    /**
     * 修改位置：HiMap 无法直接拖拽 iframe 内图标，改为在弹窗中输入经纬度
     */
    editPosition: function (channel) {
      this.closeInfoBox()
      channel['oldLongitude'] = channel.gbLongitude
      channel['oldLatitude'] = channel.gbLatitude
      if (!this.hasPosition(channel) && this.mapApp && this.mapApp.getCenterLatLng) {
        // 没有位置的通道默认落在当前地图中心
        const center = this.mapApp.getCenterLatLng()
        channel.gbLongitude = center.x
        channel.gbLatitude = center.y
      }
      this.showEditInfo(channel)
    },
    showEditInfo: function(data) {
      this.dragChannel = data
      this.editBoxPoint = [Number(data.gbLongitude), Number(data.gbLatitude)]
      this.repositionBoxes()
    },
    cancelEdit: function(channel) {
      channel.gbLongitude = channel.oldLongitude
      channel.gbLatitude = channel.oldLatitude
      this.dragChannel = null
      this.editBoxPoint = null
      this.editBoxPixel = null
    },
    submitEdit: function(channel) {
      const longitude = Number(channel.gbLongitude)
      const latitude = Number(channel.gbLatitude)
      if (!longitude || !latitude || isNaN(longitude) || isNaN(latitude)) {
        this.$message.warning({
          showClose: true,
          message: '请输入正确的经纬度'
        })
        return
      }
      this.$store.dispatch('commonChanel/update', channel)
        .then(() => {
          this.$message.success({
            showClose: true,
            message: '保存成功'
          })
          this.dragChannel = null
          this.editBoxPoint = null
          this.editBoxPixel = null
          // 刷新左侧树菜单与地图点位
          this.$refs.deviceTree.refresh('channel' + channel.gbId)
          this.loadChannels()
        })
        .catch((error) => {
          this.$message({
            showClose: true,
            message: error,
            type: 'error'
          })
        })
    }
  }
}
</script>

<style>
.channel-load-status {
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 5;
  padding: 6px 12px;
  background: rgba(255, 255, 255, .95);
  color: #606266;
  border-radius: 4px;
}
.hi-map-area {
  position: relative;
  height: 100%;
  overflow: hidden;
}
.map-infobox {
  /* 拖动只更新合成变换，避免每帧变更 left/top 触发布局。 */
  will-change: transform;
  pointer-events: auto;
}
.map-tool-box-bottom-right {
  position: absolute;
  right: 20px;
  bottom: 20px;
}
.map-tool-box-top-right {
  position: absolute;
  right: 20px;
  top: 20px;
}
.map-tool-btn-group {
  background-color: #FFFFFF;
  border-radius: 3px;
  user-select: none;
  box-shadow: 0 2px 2px rgba(0, 0, 0, .15);
  margin-bottom: 10px;
}
.map-tool-btn {
  border-bottom: 1px #dfdfdf solid;
  border-right: 1px #dfdfdf solid;
  width: fit-content;
  min-width: 33px;
  height: 36px;
  cursor: pointer;
  text-align: center;
  line-height: 36px;
  font-size: 14px;
}
.map-tool-btn i {
  font-size: 14px;
}
.map-tool-btn-group:last-child {
  border-bottom: none;
  border-right: none;
}

.infobox-content{
  width: 270px;
  background-color: #FFFFFF;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #868686;
}

.infobox-content::after {
  position: absolute;
  bottom: -11px;
  left: calc(50% - 8px);
  display: block;
  content: "";
  width: 16px;
  height: 16px;
  background: url('/static/images/arrow.png') no-repeat center;
}

.infobox-edit-content{
  width: 400px;
  background-color: #FFFFFF;
  padding: 10px;
  border-radius: 10px;
  border: 1px solid #868686;
}

.infobox-edit-content::after {
  position: absolute;
  bottom: -11px;
  left: calc(50% - 8px);
  display: block;
  content: "";
  width: 16px;
  height: 16px;
  background: url('/static/images/arrow.png') no-repeat center;
}
.infobox-close {
  position: absolute;
  right: 1rem;
  top: 1rem;
  color: #000000;
  cursor:pointer
}
.el-descriptions__title {
  font-size: 1rem;
  font-weight: 700;
  padding: 20px 20px 0px 23px;
  text-align: center;
  width: 100%;
}
</style>
