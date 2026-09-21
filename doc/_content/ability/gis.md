<!-- 电子地图 -->

# 电子地图

WVP 的电子地图用于设备/通道的定位展示，当前基于 HiMap 地图引擎（海信地图）开发。
地图通过 iframe 加载 `web/public/map/` 下的静态入口页，再由入口页加载远端 HiMap 引擎，
前端页面通过 `HiMap` 组件拿到地图实例后绘制通道点位。

### 查看设备定位

1. 左侧设备树点击通道，会自动定位到该通道坐标并弹出通道信息框；
2. 地图上点击通道图标，同样弹出通道信息框；
3. 信息框内可以「播放 / 编辑 / 位置」，其中「位置」用于修改通道经纬度。

### 底图切换

地图右下角工具栏提供「底图切换」，可在浅色（`arcgis`）与深色（`arcgisDark`）之间切换。
默认使用浅色底图。

工具栏另外提供：定位全部通道、刷新通道点位、放大、缩小。

### 目录与组件

```
web/public/map/
├── arcgis.html              # 浅色地图入口页
├── arcgis-dark.html         # 深色地图入口页
├── himap-config-light.js    # 浅色底图配置
└── himap-config-dark.js     # 深色底图配置
web/src/components/HiMap/index.vue   # HiMap 组件（iframe + 实例轮询）
web/src/utils/himap-utils.js         # 画点/线/面、视野计算等工具方法
web/src/views/map/index.vue          # 电子地图页面
```

`HiMap` 组件对外接口：

| 名称 | 类型 | 说明 |
| --- | --- | --- |
| `maptype` | prop | `arcgis` 浅色（默认）/ `arcgisDark` 深色 |
| `init-map-obj` | prop | 为 `true` 时把地图实例写入 `window.MAP_OBJ` |
| `ready` | event | `(mapApp, contentDocument)`，地图就绪后触发一次 |
| `timeout` | event | 一直拿不到地图实例时触发（默认 20s） |
| `loadwarning` | event | 地图实例可用但底图未确认加载完成时触发 |
| `zoomIn` / `zoomOut` / `zoomTo` / `centerAndZoom` | method | 转发到 HiMap 实例的常用操作 |

### 更换底图服务地址

底图服务地址配置在 `web/public/map/himap-config-light.js` 与 `himap-config-dark.js`：

- `HOSTNAME` / `gkmapHOSTNAME`：HiMap 引擎与管控扩展的部署地址（当前为 `http://219.147.31.25:30001/HiMap/`）；
- `CENTERPOINT` / `INITLEVEL`：默认中心点与初始层级；
- `GETTILEURL`：矢量瓦片地址（浅色为 `vectors/white/`，深色为 `vectors/dark/`）；
- `arcgisCS`：坐标系，当前为 `WGS84`。

修改后重新编译前端即可生效。注意：这些文件位于 `public/` 下，构建时会原样拷贝到产物目录。

### 引擎与就绪判定说明

`maptype` 在入口页里固定写为 `minemap`，因此实际加载的是 minemap 引擎（`himap.js` 据此选择引擎实现），
底图瓦片路径由 `GETTILEURL` 决定。

minemap 的 `map._loaded` 只有在底图样式加载成功后才会被置为 `true`；底图样式服务异常时它一直不成立。
`HiMap` 组件因此按 `_loaded` / `map.loaded()` / `map.isStyleLoaded()` 综合判断底图是否加载完成，
并设有宽限期（默认 3s）：超时后仍会把地图实例交给页面，避免因底图服务异常导致地图组件静默卡死。

底图样式由 `solution` 决定，地址为 `<serviceUrl>/map/solu/style/<solution>`，该值必须与 minemap
服务端已存在的样式一致，否则样式接口返回 400、底图空白。当前浅色与深色配置均使用 `11003`。

### 与旧版 OpenLayers 地图的关系

`web/src/views/common/MapComponent.vue` 是迁移前的 OpenLayers 版地图组件，现已不再被电子地图页面引用。
原先基于 OpenLayers 的「图层抽稀」「MVT 矢量瓦片图层」「拖拽图标改点」能力在 HiMap 方案下未保留：
HiMap 以 iframe 承载地图，无法把 OpenLayers 图层叠加进去，也无法直接拖拽 iframe 内的图标，
因此「修改位置」改为在弹窗中输入经纬度。后端对应的抽稀接口（`/api/common/channel/map/thin/*`）仍然保留，未被改动。

### 旧版 OpenLayers 底图配置（保留说明）

迁移前的地图基于开源地图引擎 OpenLayers，底图配置在 `web_src/static/js/config.js` 中：

```javascript
window.mapParam = {
  // 开启/关闭地图功能
  enable: true,
  // 坐标系 GCJ-02 WGS-84,
  coordinateSystem: "GCJ-02",
  // 地图瓦片地址
  tilesUrl: "http://webrd0{1-4}.is.autonavi.com/appmaptile?x={x}&y={y}&z={z}&lang=zh_cn&size=1&scale=1&style=8",
  // 瓦片大小
  tileSize: 256,
  // 默认层级
  zoom:10,
  // 默认地图中心点
  center:[116.41020, 39.915119],
  // 地图最大层级
  maxZoom:18,
  // 地图最小层级
  minZoom: 3
}
```
