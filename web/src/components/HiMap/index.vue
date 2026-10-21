<template>
  <div class="hi-map" :style="myStyle">
    <iframe
      :key="loadToken"
      :id="mapid"
      ref="frame"
      :src="iframeSrc"
      :style="ifmStyle"
      frameborder="0"
      @load="onFrameLoad"
      @error="failLoading('地图页面加载失败')"
    />
    <div v-if="!ready" class="map-status" role="status">
      <span>{{ errorMessage || (retryCount ? '正在重新加载地图…' : '正在加载地图…') }}</span>
      <button v-if="errorMessage" type="button" @click="reload()">重新加载地图</button>
    </div>
  </div>
</template>

<script>
/** HiMap iframe：确认底图就绪后再通知父页面；失败可重建 iframe，无需清浏览器缓存。 */
export default {
  name: 'HiMap',
  props: {
    // 地图类型：'arcgis' 浅色（默认）、'arcgisDark' 深色
    maptype: {
      type: String,
      default: 'arcgis'
    },
    // iframe 的 id，页面上存在多个地图实例时需要区分
    mapid: {
      type: String,
      default: 'mapIframeSub'
    },
    // 是否把地图实例写入 window.MAP_OBJ / window.CONTENT_DOCUMENT
    initMapObj: {
      type: Boolean,
      default: false
    },
    // iframe 样式
    ifmStyle: {
      type: Object,
      default: () => ({
        border: 'none',
        width: '100%',
        height: '100%'
      })
    },
    // 外层容器样式
    myStyle: {
      type: Object,
      default: () => ({
        width: '100%',
        height: '100%',
        position: 'relative',
        top: 0,
        background: 'radial-gradient(#f7f9fc 20%, #eef2f8 45%, #e3e9f2 80%)'
      })
    },
    // 轮询间隔（毫秒）
    pollInterval: {
      type: Number,
      default: 500
    },
    // 就绪超时（毫秒），超时后停止轮询并抛出 timeout 事件
    readyTimeout: {
      type: Number,
      default: 20000
    },
    // 底图未完成时的提示等待时间，不能代替真实的就绪信号
    loadGracePeriod: {
      type: Number,
      default: 3000
    }
  },
  data() {
    return {
      ready: false,
      loadToken: Date.now().toString(36) + '-' + Math.random().toString(36).slice(2),
      retryCount: 0,
      errorMessage: ''
    }
  },
  computed: {
    // 静态入口页放在 public/map-entry 下，随前端版本发布
    iframeSrc() {
      const base = process.env.BASE_URL || '/'
      const page = this.maptype === 'arcgisDark' ? 'arcgis-dark.html' : 'arcgis.html'
      // 入口随前端发布；现场挂载的 /map 只提供配置，不再遮住新版入口 HTML。
      return `${base}map-entry/${page}?v=${encodeURIComponent(this.loadToken)}`
    }
  },
  watch: {
    // 切换底图类型时重新加载 iframe 并重新等待就绪
    maptype() {
      this.reload()
    }
  },
  created() {
    // 定时器不需要响应式，这里显式初始化避免未定义判断
    // 地图 SDK 对象不能进入 Vue 的深层响应式观察。
    this.mapApp = null
    this.timer = null
    this.timeoutTimer = null
    // 首次发现 _MapApp 的时间戳，用于底图加载宽限期判断
    this.mapAppFoundAt = null
    this.warningSent = false
    this.retryTimer = null
    this.disposed = false
    this.active = true
  },
  mounted() {
    // 首次挂载时如果 load 事件已经错过，这里兜底启动一次轮询
    this.startPolling()
  },
  activated() {
    this.active = true
    if (this.errorMessage) this.reload()
    else if (!this.ready) this.startPolling()
    else if (this.mapApp && this.mapApp.map && typeof this.mapApp.map.resize === 'function') this.mapApp.map.resize()
  },
  deactivated() {
    this.active = false
    this.stopPolling()
    clearTimeout(this.retryTimer)
    this.retryTimer = null
  },
  beforeDestroy() {
    this.disposed = true
    this.stopPolling()
    clearTimeout(this.retryTimer)
    this.clearMapApp()
  },
  methods: {
    /**
     * iframe 每次加载（含切换底图）完成后重新开始轮询
     */
    onFrameLoad() {
      this.startPolling()
    },
    /**
     * 开始轮询 iframe 内的地图实例
     */
    startPolling() {
      // 已就绪后不再重复启动轮询（例如 iframe 迟到的 load 事件）
      if (this.ready || this.timer || this.errorMessage || this.disposed || !this.active) return
      this.mapAppFoundAt = null
      this.warningSent = false
      this.timer = setInterval(() => {
        this.checkMapApp()
      }, this.pollInterval)
      // 超时兜底：HiMap 服务不可达时避免无限轮询
      this.timeoutTimer = setTimeout(() => {
        if (!this.ready) {
          this.failLoading('地图加载超时，请检查地图服务和网络')
        }
      }, this.readyTimeout)
      // 立即检测一次，避免等待一个轮询周期
      this.checkMapApp()
    },
    /**
     * 停止轮询与超时定时器
     */
    stopPolling() {
      if (this.timer) {
        clearInterval(this.timer)
        this.timer = null
      }
      if (this.timeoutTimer) {
        clearTimeout(this.timeoutTimer)
        this.timeoutTimer = null
      }
    },
    failLoading(message) {
      if (this.disposed || this.errorMessage) return
      this.stopPolling()
      this.errorMessage = message
      console.error('[HiMap]', message, this.iframeSrc)
      this.$emit('timeout', message)
      // 每次进入/手动刷新最多自动重试两次，不无限刷新有故障的服务。
      if (this.retryCount < 2) {
        this.retryTimer = setTimeout(() => this.reload(true), 1500)
      }
    },
    checkMapApp() {
      if (this.ready || this.errorMessage) return
      try {
        const frame = this.$refs.frame
        const contentWindow = frame && frame.contentWindow
        // Vue 更新 src 到 iframe 完成导航之间，可能仍读到上一个页面的实例。
        if (!contentWindow || new URLSearchParams(contentWindow.location.search).get('v') !== this.loadToken) return
        const state = contentWindow._MapLoadState
        if (state && state.error) {
          this.failLoading(state.message)
          return
        }
        const mapApp = contentWindow._MapApp
        if (!mapApp) return
        if (this.mapAppFoundAt === null) this.mapAppFoundAt = Date.now()
        if (this.isMapLoaded(mapApp)) {
          this.handleReady(mapApp, contentWindow)
        } else if (!this.warningSent && Date.now() - this.mapAppFoundAt >= this.loadGracePeriod) {
          this.warningSent = true
          this.$emit('loadwarning')
        }
      } catch (error) {
        this.failLoading('地图初始化失败：' + error.message)
      }
    },
    /**
     * 判断底图是否已加载完成，兼容不同引擎暴露的加载状态入口
     * @param {Object} mapApp HiMap 实例
     * @returns {Boolean}
     */
    isMapLoaded(mapApp) {
      const map = mapApp && mapApp.map
      if (!map) {
        return false
      }
      try {
        if (map._loaded === true || map.loaded === true) {
          return true
        }
        if (typeof map.loaded === 'function' && map.loaded() === true) {
          return true
        }
        if (typeof map.isStyleLoaded === 'function' && map.isStyleLoaded() === true) {
          return true
        }
      } catch (e) {
        // 不同引擎实现存在差异，判定失败时按未完成处理
        console.warn('[HiMap] 底图加载状态判定失败', e)
      }
      return false
    },
    /**
     * 地图就绪处理：停止轮询、缓存实例、抛出 ready 事件
     * @param {Object} mapApp HiMap 实例
     * @param {Window} contentWindow iframe 的 window
     */
    handleReady(mapApp, contentWindow) {
      if (this.ready) {
        return
      }
      this.stopPolling()
      this.mapApp = mapApp
      this.ready = true

      const contentDocument = contentWindow.document
      if (this.initMapObj) {
        window.MAP_OBJ = mapApp
        window.CONTENT_DOCUMENT = contentDocument
      }
      this.$emit('ready', mapApp, contentDocument)
    },
    /**
     * 重新加载地图（切换底图或服务异常后重试）
     */
    clearMapApp() {
      if (this.initMapObj && window.MAP_OBJ === this.mapApp) {
        delete window.MAP_OBJ
        delete window.CONTENT_DOCUMENT
      }
      this.mapApp = null
    },
    reload(automatic = false) {
      if (this.disposed) return
      this.stopPolling()
      clearTimeout(this.retryTimer)
      this.retryTimer = null
      this.$emit('loading')
      this.clearMapApp()
      this.ready = false
      this.errorMessage = ''
      this.mapAppFoundAt = null
      this.retryCount = automatic === true ? this.retryCount + 1 : 0
      // 同一底图也创建全新 iframe，并让入口、配置和 SDK 使用新请求地址。
      this.loadToken = Date.now().toString(36) + '-' + Math.random().toString(36).slice(2)
      this.$nextTick(() => this.startPolling())
    },
    getMapApp() {
      return this.mapApp
    },
    getContentDocument() {
      const frame = this.$refs.frame
      return frame ? frame.contentDocument : null
    },
    // ---------------- 常用地图操作，内部都转发到 HiMap 实例 ----------------
    zoomIn() {
      this.mapApp && this.mapApp.zoomIn()
    },
    zoomOut() {
      this.mapApp && this.mapApp.zoomOut()
    },
    zoomTo(level) {
      this.mapApp && this.mapApp.zoomTo(level)
    },
    getMaxLevel() {
      return this.mapApp ? this.mapApp.getMaxLevel() : 0
    },
    /**
     * 定位到指定坐标并缩放到指定级别
     * @param {String} coordinate "lon,lat"
     * @param {Number} level 缩放级别
     */
    centerAndZoom(coordinate, level) {
      this.mapApp && this.mapApp.centerAndZoom(coordinate, level)
    }
  }
}
</script>

<style lang="scss" scoped>
.hi-map {
  width: 100%;
  height: 100%;
  overflow: hidden;
}
.map-status {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: rgba(247, 249, 252, .9);
  color: #606266;
  button { padding: 8px 14px; cursor: pointer; }
}
</style>
