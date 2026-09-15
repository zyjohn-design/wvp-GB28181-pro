<template>
  <div id="live" class="live-container">
    <div v-loading="loading" class="live-content" :class="{ 'sidebar-collapsed': !sidebarVisible }" element-loading-text="拼命加载中">
      <div class="device-tree-container-box" :class="{ 'device-tree-hidden': !sidebarVisible }">
        <DeviceTree @clickEvent="clickEvent" :context-menu-event="contextMenuEvent" />
      </div>
      <div class="video-container">
        <div class="control-bar">
          <div class="split-controls">
            <button class="icon-button sidebar-toggle" type="button" title="切换设备列表" @click="toggleSidebar">
              <i :class="sidebarVisible ? 'el-icon-s-fold' : 'el-icon-s-unfold'" />
            </button>
            <span class="divider" />
            <span class="control-label">画面布局</span>
            <button
              v-for="option in splitOptions"
              :key="option.value"
              class="split-button"
              :class="{ active: spiltIndex === option.value }"
              type="button"
              :title="option.label + ' 分屏'"
              :aria-label="option.label + ' 分屏'"
              @click="spiltIndex = option.value"
            >
              <span class="split-glyph" :class="'split-glyph-' + option.label">
                <span v-for="cell in option.cells" :key="cell" />
              </span>
              <span>{{ option.label }}</span>
            </button>
          </div>
          <div class="global-player-control">
            <span class="control-label">播放器</span>
            <el-select v-model="globalPlayer" size="mini" class="player-select">
              <el-option label="Jessibuca" value="jessibuca" />
              <el-option label="WebRTC" value="webRTC" />
              <el-option label="H265web" value="h265web" />
            </el-select>
          </div>
          <div class="fullscreen-control">
            <button class="action-button" type="button" title="全屏播放" @click="fullScreen()">
              <i class="el-icon-full-screen" /><span>全屏</span>
            </button>
            <button class="action-button" :class="{ active: ptzVisible }" type="button" title="云台控制" @click="togglePtzPanel">
              <i class="el-icon-s-operation" /><span>云台</span>
            </button>
          </div>
        </div>
        <div class="player-container">
          <div
            ref="playBox"
            class="play-grid"
            :style="liveStyle"
          >
            <div
              v-for="i in layout[spiltIndex].spilt"
              :key="i"
              class="play-box"
              :class="getPlayerClass(spiltIndex, i)"
              @click="playerIdx = (i-1)"
            >
              <div v-if="!streamInfo[i-1]" class="no-signal">{{ videoTip[i-1]?videoTip[i-1]:"无信号" }}</div>
              <PlayerTabs
                v-else
                :ref="'playerTabs' + i"
                :show-tab="false"
                :show-button="true"
              />
            </div>
          </div>
        </div>
      </div>
      <div class="ptz-panel" v-show="ptzVisible">
        <div class="ptz-panel-header">
          <span>云台控制</span>
          <i class="el-icon-close" @click="ptzVisible = false" />
        </div>
        <div class="ptz-panel-body">
          <template v-if="currentChannelId">
            <div class="ptz-preset-section">
              <div class="section-title">预置位</div>
              <LivePtzPreset :channel-id="currentChannelId" />
            </div>
            <div class="ptz-control-section">
              <div class="section-title">方向控制</div>
              <channelPtzPanel :channel-id="currentChannelId" @drag-zoom-start="handleDragZoom" />
            </div>
          </template>
          <div v-else class="ptz-empty-tip">请先在左侧选择通道</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>

import PlayerTabs from '../common/playerTabs.vue'
import DeviceTree from '../common/DeviceTree.vue'
import channelPtzPanel from '../channel/common/channelPtzPanel.vue'
import LivePtzPreset from './LivePtzPreset.vue'
import screenFull from 'screenfull'

export default {
  name: 'Live',
  components: {
    PlayerTabs, DeviceTree, channelPtzPanel, LivePtzPreset
  },

  data() {
    return {
      streamInfo: [null],
      videoTip: [''],
      globalPlayer: 'jessibuca',
      splitOptions: [
        { value: 0, label: '1', cells: 1 },
        { value: 1, label: '4', cells: 4 },
        { value: 2, label: '6', cells: 6 },
        { value: 3, label: '9', cells: 9 }
      ],
      sidebarVisible: true, // 侧边栏
      ptzVisible: false, // 云台面板
      currentChannelId: null, // 当前选中通道
      spiltIndex: 2, // 分屏
      playerIdx: 0, // 激活播放器

      updateLooper: 0, // 数据刷新轮训标志
      count: 15,
      total: 0,

      // channel
      loading: false,
      layout: [
        {
          spilt: 1,
          columns: '1fr',
          rows: '1fr',
          style: function() {}
        },
        {
          spilt: 4,
          columns: '1fr 1fr',
          rows: '1fr 1fr',
          style: function() {}
        },
        {
          spilt: 6,
          columns: '1fr 1fr 1fr',
          rows: '1fr 1fr 1fr',
          style: function(index) {
            console.log(index)
            if (index === 0) {
              return {
                gridColumn: ' 1 / span 2',
                gridRow: ' 1 / span 2'
              }
            }
          }

        },
        {
          spilt: 9,
          columns: '1fr 1fr 1fr',
          rows: '1fr 1fr 1fr',
          style: function() {}
        }
      ]
    }
  },

  computed: {
    liveStyle() {
      return {
        display: 'grid',
        gridTemplateColumns: this.layout[this.spiltIndex].columns,
        gridTemplateRows: this.layout[this.spiltIndex].rows,
        gap: '3px',
        backgroundColor: '#1f2937'
      }
    }
  },
  watch: {
    spiltIndex(newValue) {
      console.log('切换画幅;' + newValue)
      const that = this
      for (let i = 1; i <= this.layout[newValue].spilt; i++) {
        if (!that.$refs['playerTabs' + i]) {
          continue
        }
        this.$nextTick(() => {
          const ref = that.$refs['playerTabs' + i]
          const instance = ref instanceof Array ? ref[0] : ref
          if (instance && instance.resize) {
            instance.resize()
          }
        })
      }
      window.localStorage.setItem('split', newValue)
    },
    globalPlayer(newKey) {
      for (let i = 1; i <= this.layout[this.spiltIndex].spilt; i++) {
        const ref = this.$refs['playerTabs' + i]
        if (ref) {
          const instance = ref instanceof Array ? ref[0] : ref
          instance.switchPlayer(newKey)
        }
      }
      window.localStorage.setItem('globalPlayer', newKey)
    },
    '$route.fullPath': 'checkPlayByParam'
  },
  mounted() {
    // Add window resize event listener to handle responsive behavior
    window.addEventListener('resize', this.handleResize)
    this.handleResize()
  },
  created() {
    this.checkPlayByParam()
  },
  destroyed() {
    clearTimeout(this.updateLooper)
    // Remove event listener when component is destroyed
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    toggleSidebar() {
      this.sidebarVisible = !this.sidebarVisible
      if (this.sidebarVisible) {
        this.ptzVisible = false
      }
    },
    handleDragZoom(direction) {
      const refName = 'playerTabs' + (this.playerIdx + 1)
      const ref = this.$refs[refName]
      if (!ref) return
      const instance = ref instanceof Array ? ref[0] : ref
      if (!instance || !instance.startDragZoom) return
      console.log('[live] handleDragZoom playerTabs:', refName, 'playerIdx:', this.playerIdx, 'direction:', direction)
      instance.startDragZoom((params) => {
        console.log('[live] dragZoom before channelId:', JSON.stringify(params))
        params.channelId = this.currentChannelId
        console.log('[live] dragZoom after channelId:', JSON.stringify(params))
        const action = direction === 'in' ? 'commonChanel/dragZoomIn' : 'commonChanel/dragZoomOut'
        const successMsg = direction === 'in' ? '拉框放大成功' : '拉框缩小成功'
        const failMsg = direction === 'in' ? '拉框放大失败' : '拉框缩小失败'
        this.$store.dispatch(action, params).then(() => {
          this.$message({ showClose: true, message: successMsg, type: 'success' })
        }).catch(() => {
          this.$message({ showClose: true, message: failMsg, type: 'error' })
        })
      })
    },
    togglePtzPanel() {
      this.ptzVisible = !this.ptzVisible
      if (this.ptzVisible) {
        this.sidebarVisible = false
      }
    },
    handleResize() {
      this.$forceUpdate()

      this.$nextTick(() => {
        for (let i = 0; i < this.layout[this.spiltIndex].spilt; i++) {
          const ref = this.$refs[`playerTabs${i + 1}`]
          if (ref) {
            const instance = ref instanceof Array ? ref[0] : ref
            instance.resize && instance.resize()
          }
        }
      })
    },
    clickEvent: function(channelId) {
      this.currentChannelId = channelId
      this.sendDevicePush(channelId)
    },
    getPlayerClass: function(splitIndex, i) {
      let classStr = 'play-box-' + splitIndex + '-' + i
      if (this.playerIdx === (i - 1)) {
        classStr += ' redborder'
      }
      return classStr
    },
    contextMenuEvent: function() {

    },
    // 通知设备上传媒体流
    sendDevicePush: function(channelId) {
      this.save(channelId)
      const idxTmp = this.playerIdx
      this.$set(this.streamInfo, idxTmp, null)
      this.$set(this.videoTip, idxTmp, '正在拉流...')
      this.$store.dispatch('commonChanel/playChannel', channelId)
        .then(data => {
          this.setPlayStream(data.transcodeStream || data, idxTmp)
        })
        .catch(err => {
          this.$set(this.videoTip, idxTmp, '播放失败: ' + err)
        })
        .finally(() => {
          this.loading = false
        })
    },
    setPlayStream(streamInfo, idx) {
      this.$set(this.streamInfo, idx, streamInfo)
      this.$nextTick(() => {
        const refName = 'playerTabs' + (idx + 1)
        const ref = this.$refs[refName]
        if (ref) {
          const instance = ref instanceof Array ? ref[0] : ref
          if (instance && instance.setStreamInfo) {
            instance.setStreamInfo(streamInfo)
          }
        }
      })
    },
    checkPlayByParam() {
      const query = this.$route.query
      if (query.channelId) {
        this.sendDevicePush(query.channelId)
      }
    },

    save(item) {
      const dataStr = window.localStorage.getItem('playData') || '[]'
      const data = JSON.parse(dataStr)
      data[this.playerIdx] = item
      window.localStorage.setItem('playData', JSON.stringify(data))
    },
    clear(idx) {
      const dataStr = window.localStorage.getItem('playData') || '[]'
      const data = JSON.parse(dataStr)
      data[idx - 1] = null
      console.log(data)
      window.localStorage.setItem('playData', JSON.stringify(data))
    },
    fullScreen: function() {
      if (screenFull.isEnabled) {
        screenFull.toggle(this.$refs.playBox)
      }
    }
  }
}
</script>
<style scoped>
.live-container {
  height: calc(100vh - 64px);
  width: 100%;
  padding: 18px;
  background: #f3f4f6;
}

.live-content {
  height: 100%;
  display: flex;
  flex-direction: row;
  gap: 16px;
}

.device-tree-container-box {
  width: 320px;
  min-width: 260px;
  max-width: 420px;
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: auto;
  resize: horizontal;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
  transition: width 150ms ease-out, min-width 150ms ease-out, opacity 150ms ease-out;
}

.device-tree-hidden {
  width: 0 !important;
  min-width: 0 !important;
  overflow: hidden;
  resize: none;
  border: 0;
  opacity: 0;
}

@media (max-width: 768px) {
  .live-content {
    flex-direction: column;
  }

  .device-tree-container-box {
    width: 100%;
    max-width: 100%;
    height: 200px;
    min-height: 150px;
    max-height: 300px;
    resize: vertical;
  }
}

.video-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  background: #fff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
}

.control-bar {
  min-height: 62px;
  padding: 0 14px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  border-bottom: 1px solid #e5e7eb;
  font-size: 14px;
}

.split-controls {
  display: flex;
  align-items: center;
  gap: 6px;
}

.fullscreen-control {
  display: flex;
  align-items: center;
  gap: 6px;
}

.ptz-toggle-control {
  text-align: right;
  padding-right: 10px;
}

.ptz-toggle-control .btn.active {
  color: #409EFF;
}

.ptz-panel {
  width: 360px;
  min-width: 340px;
  background-color: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-shadow: 0 1px 2px rgba(15, 23, 42, 0.04);
}

.ptz-panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 17px 18px;
  border-bottom: 1px solid #e5e7eb;
  font-size: 15px;
  color: #111827;
  font-weight: 650;
}

.ptz-panel-header .el-icon-close {
  cursor: pointer;
  font-size: 18px;
  color: #9ca3af;
  transition: color 150ms ease-out, transform 150ms ease-out;
}

.ptz-panel-header .el-icon-close:hover {
  color: #4f46e5;
  transform: rotate(90deg);
}

.ptz-panel-body {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  padding: 16px 18px;
}

.ptz-preset-section {
  flex: 1;
  overflow-y: auto;
  margin-bottom: 8px;
}

.ptz-control-section {
  flex-shrink: 0;
}

.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #374151;
  margin-bottom: 10px;
  padding-bottom: 6px;
  border-bottom: 1px solid #e5e7eb;
}

.ptz-divider {
  height: 1px;
  background-color: #e4e7ed;
  margin: 12px 0;
}

.ptz-empty-tip {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  color: #9ca3af;
  font-size: 14px;
}

.global-player-control {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.control-label {
  color: #6b7280;
  font-size: 12px;
  font-weight: 600;
}

.player-select {
  width: 122px;
}

.player-container {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 12px;
  overflow: hidden;
  background: #f9fafb;
}

.play-grid {
  width: 100%;
  height: 100%;
  max-height: calc(100vh - 202px);
  aspect-ratio: 16/9;
  border: 3px solid #1f2937;
  border-radius: 8px;
  overflow: hidden;
}

.icon-button,
.split-button,
.action-button {
  font-family: inherit;
  cursor: pointer;
  transition: color 150ms ease-out, background-color 150ms ease-out, border-color 150ms ease-out, transform 150ms ease-out;
}

.icon-button:active,
.split-button:active,
.action-button:active {
  transform: scale(0.97);
}

.icon-button:focus-visible,
.split-button:focus-visible,
.action-button:focus-visible {
  outline: 2px solid rgba(99, 102, 241, 0.45);
  outline-offset: 2px;
}

.icon-button {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 34px;
  height: 34px;
  padding: 0;
  color: #6b7280;
  background: #fff;
  border: 1px solid #d1d5db;
  border-radius: 7px;
}

.icon-button:hover {
  color: #4f46e5;
  background: #eef2ff;
  border-color: #c7d2fe;
}

.sidebar-toggle {
  flex: 0 0 auto;
  font-size: 16px;
}

.divider {
  display: inline-block;
  width: 1px;
  height: 16px;
  background-color: #e5e7eb;
  margin: 0 5px;
  vertical-align: middle;
}

.split-button {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  height: 34px;
  padding: 0 9px;
  color: #6b7280;
  background: #fff;
  border: 1px solid #d1d5db;
  border-radius: 7px;
  font-size: 12px;
  font-weight: 600;
}

.split-button:hover {
  color: #4f46e5;
  border-color: #a5b4fc;
}

.split-button.active {
  color: #4338ca;
  background: #eef2ff;
  border-color: #a5b4fc;
}

.split-glyph {
  display: grid;
  width: 16px;
  height: 14px;
  gap: 1px;
}

.split-glyph > span {
  min-width: 0;
  min-height: 0;
  background: currentColor;
  border-radius: 1px;
  opacity: 0.82;
}

.split-glyph-1 {
  grid-template-columns: 1fr;
  grid-template-rows: 1fr;
}

.split-glyph-4 {
  grid-template-columns: repeat(2, 1fr);
  grid-template-rows: repeat(2, 1fr);
}

.split-glyph-6 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(2, 1fr);
}

.split-glyph-9 {
  grid-template-columns: repeat(3, 1fr);
  grid-template-rows: repeat(3, 1fr);
}

.action-button {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  height: 34px;
  padding: 0 10px;
  color: #4b5563;
  background: #fff;
  border: 1px solid #d1d5db;
  border-radius: 7px;
  font-size: 13px;
  font-weight: 500;
}

.action-button:hover,
.action-button.active {
  color: #4338ca;
  background: #eef2ff;
  border-color: #c7d2fe;
}

.redborder {
  position: relative;
  z-index: 1;
  box-shadow: inset 0 0 0 3px #6366f1;
}

.play-box {
  background-color: #0f172a;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.no-signal {
  color: #94a3b8;
  font-size: 13px;
  font-weight: 500;
  letter-spacing: 0.02em;
}

.play-box-2-1 {
  grid-column: 1 / span 2;
  grid-row: 1 / span 2;
}

/* Responsive adjustments for smaller screens */
@media (max-width: 576px) {
  .live-container {
    height: auto;
    min-height: calc(100vh - 64px);
    padding: 10px;
  }

  .control-bar {
    flex-direction: column;
    height: auto;
    align-items: stretch;
    padding: 10px;
  }

  .split-controls, .fullscreen-control {
    width: 100%;
    justify-content: center;
  }

  .global-player-control {
    justify-content: center;
  }

  .control-label,
  .divider {
    display: none;
  }

  .split-button {
    padding: 0 7px;
  }
}


</style>
