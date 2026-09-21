<template>
  <div class="hi-map" :style="myStyle">
    <iframe
      :id="mapid"
      ref="frame"
      :src="iframeSrc"
      :style="ifmStyle"
      frameborder="0"
      @load="onFrameLoad"
    />
  </div>
</template>

<script>
/**
 * HiMap 电子地图组件（Vue 2 版）
 *
 * 由 traffic_brain/client-web 的 React 版 HiMap 迁移而来，核心行为保持一致：
 * 通过 iframe 加载 public/map/ 下的静态地图入口页，轮询 iframe 内的 window._MapApp，
 * 就绪后以 ready 事件把 HiMap 实例抛给父组件（对应 React 版的 onready 回调）。
 *
 * 与 React 版的差异：
 * 1. 去掉了 traffic_brain 的 intentManager 能力注册（那是大模型意图链路专有逻辑，本项目没有该链路）。
 * 2. 轮询增加了超时保护，避免 HiMap 服务不可达时无限轮询。
 * 3. 用 iframe 的 load 事件驱动轮询，避免切换底图时读到上一个实例。
 *
 * 用法：
 *   <HiMap ref="hiMap" maptype="arcgis" :init-map-obj="true" @ready="onMapReady" />
 *   onMapReady(mapApp) { mapApp.centerAndZoom('114.26,30.58', 8) }
 */
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
    // 发现 _MapApp 后，最多再等多久确认底图加载完成（毫秒），超时则先按就绪处理
    loadGracePeriod: {
      type: Number,
      default: 3000
    }
  },
  data() {
    return {
      // HiMap 实例（iframe 内的 window._MapApp）
      mapApp: null,
      // 地图是否已就绪
      ready: false
    }
  },
  computed: {
    // 静态入口页放在 public/map 下，随 Vue CLI 的 public 目录原样发布
    iframeSrc() {
      const base = process.env.BASE_URL || '/'
      const page = this.maptype === 'arcgisDark' ? 'arcgis-dark.html' : 'arcgis.html'
      return `${base}map/${page}`
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
    this.timer = null
    this.timeoutTimer = null
    // 首次发现 _MapApp 的时间戳，用于底图加载宽限期判断
    this.mapAppFoundAt = null
  },
  mounted() {
    // 首次挂载时如果 load 事件已经错过，这里兜底启动一次轮询
    this.startPolling()
  },
  beforeDestroy() {
    this.stopPolling()
    this.mapApp = null
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
      if (this.ready) {
        return
      }
      this.stopPolling()
      this.mapAppFoundAt = null
      this.timer = setInterval(() => {
        this.checkMapApp()
      }, this.pollInterval)
      // 超时兜底：HiMap 服务不可达时避免无限轮询
      this.timeoutTimer = setTimeout(() => {
        if (!this.ready) {
          this.stopPolling()
          console.error('[HiMap] 地图加载超时，请检查 HiMap 服务配置')
          this.$emit('timeout')
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
    /**
     * 检测 iframe 内的 _MapApp 是否可用
     *
     * 就绪判定相对 React 版做了加固：minemap 的 `map._loaded` 只有在底图样式
     * 加载成功后才会被置为 true，底图样式服务异常时它会一直不成立。原实现只等
     * `_loaded`，一旦底图服务不可用就会永远等不到 onready、组件静默卡死。
     * 这里改为：优先等引擎的加载完成信号（`_loaded` / `loaded()` / `isStyleLoaded()`），
     * 超过 loadGracePeriod 仍拿不到就先按就绪处理，保证父组件一定能拿到地图实例。
     */
    checkMapApp() {
      if (this.ready) {
        return
      }
      const frame = this.$refs.frame
      const contentWindow = frame && frame.contentWindow
      if (!contentWindow || !contentWindow._MapApp) {
        return
      }
      const mapApp = contentWindow._MapApp
      if (!this.mapAppFoundAt) {
        this.mapAppFoundAt = Date.now()
      }
      const loaded = this.isMapLoaded(mapApp)
      const graceExpired = Date.now() - this.mapAppFoundAt >= this.loadGracePeriod
      if (loaded || graceExpired) {
        this.handleReady(mapApp, contentWindow, loaded)
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
        if (map._loaded === true) {
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
     * @param {Boolean} loaded 底图是否确认加载完成
     */
    handleReady(mapApp, contentWindow, loaded) {
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
      if (!loaded) {
        // 地图实例可用但底图尚未确认加载完成，通常是 HiMap 服务或样式服务异常
        console.warn('[HiMap] 底图未确认加载完成，请检查 HiMap 服务与底图样式配置')
        this.$emit('loadwarning')
      }
    },
    /**
     * 重新加载地图（切换底图或服务异常后重试）
     */
    reload() {
      this.stopPolling()
      this.mapApp = null
      this.ready = false
      this.mapAppFoundAt = null
      // maptype 变化会让 iframe 重新加载，加载完成后 onFrameLoad 会重启轮询
      if (this.$refs.frame) {
        this.$nextTick(() => {
          this.startPolling()
        })
      }
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
</style>
