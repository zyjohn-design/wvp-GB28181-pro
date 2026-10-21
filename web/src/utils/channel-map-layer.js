// HiMap 点图层适配：优先使用 SDK 的大量点 CanvasSymbolLayer，旧版 SDK 保留矢量层。
// 图层与通道索引都放在 Vue data 之外，拖动地图不复制通道、不重建图层。
export function createChannelMapLayer(mapApp, { iconBase, onClick }) {
  const layerId = 'gb-channel-layer'
  let canvasLayer = null
  let vectorAdded = false
  let canvasDisabled = typeof mapApp.addCanvasSymbolLayer !== 'function' ||
    typeof mapApp.removeCanvasSymbolLayer !== 'function'
  let lastSignature = null
  let channelsById = new Map()

  function clicked(payload) {
    const item = Array.isArray(payload) ? payload[0] : payload
    const params = item && (item.properties || (item.marker && item.marker.params) || item.params || item.channel || item)
    const id = params && (params.id !== undefined ? params.id : params.gbId)
    const channel = channelsById.get(String(id))
    if (channel) onClick(channel)
  }

  function removeCanvas() {
    const old = canvasLayer
    canvasLayer = null
    if (old) mapApp.removeCanvasSymbolLayer(old)
  }

  return {
    update(channels) {
      // 元数据可能改变而图标未变：更新点击索引即可，不触碰地图绘制。
      channelsById = new Map(channels.map(channel => [String(channel.gbId), channel]))
      const points = channels.map(channel => ({
        id: channel.gbId, title: channel.gbName,
        longitude: Number(channel.gbLongitude), latitude: Number(channel.gbLatitude),
        icon: channel.gbStatus === 'ON' ? 'camera1' : 'camera1-offline'
      }))
      const signature = JSON.stringify(points)
      if (signature === lastSignature) return
      if (!canvasLayer && !vectorAdded && points.length === 0) {
        lastSignature = signature
        return
      }

      if (!canvasDisabled) {
        try {
          if (!canvasLayer) {
            canvasLayer = mapApp.addCanvasSymbolLayer({
              sourceId: layerId + '-source', layerId,
              cluster: false, showText: false, defaultShowText: false
            })
            if (!canvasLayer || typeof canvasLayer.setData !== 'function' ||
                typeof canvasLayer.addClickListener !== 'function') throw new Error('CanvasSymbolLayer 接口不完整')
            canvasLayer.addClickListener(clicked)
          }
          canvasLayer.setData(points.map(point => ({
            type: 'Feature',
            geometry: { type: 'Point', coordinates: [point.longitude, point.latitude] },
            properties: {
              layerid: layerId, id: point.id, icon: point.icon,
              lon: point.longitude, lat: point.latitude,
              iconwidth: 28, iconheight: 32, iconUrl: iconBase
            }
          })))
          lastSignature = signature
          return
        } catch (error) {
          console.warn('[电子地图] Canvas 点图层不可用，回退矢量图层', error)
          removeCanvas()
          canvasDisabled = true
        }
      }

      const data = points.map(point => ({
        id: point.id, title: point.title,
        strcoords: point.longitude + ',' + point.latitude,
        imgurl: iconBase + point.icon + '.png',
        width: 28, height: 32, coordtype: '1', centable: false
      }))
      if (vectorAdded && typeof mapApp.freshVectorLayer === 'function') {
        mapApp.freshVectorLayer(layerId, data)
      } else {
        if (vectorAdded) mapApp.removeVectorLayer(layerId)
        mapApp.addVectorLayer({
          lid: layerId, data, coordtype: '1', centable: false,
          isOverlap: false, filterfunc: () => true, clicklistener: clicked
        })
        vectorAdded = true
      }
      lastSignature = signature
    },
    dispose() {
      channelsById.clear()
      lastSignature = null
      removeCanvas()
      if (vectorAdded) {
        vectorAdded = false
        mapApp.removeVectorLayer(layerId)
      }
    }
  }
}
