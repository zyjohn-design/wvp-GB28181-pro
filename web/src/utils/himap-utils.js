/**
 * HiMap 电子地图通用工具方法。
 *
 * 这些方法全部作用在 HiMap 的 _MapApp 实例上（由 HiMap 组件 ready 事件返回），
 * 不依赖任何框架，方法签名与 traffic_brain/client-web 的 himap-utils 保持一致，
 * 方便两边共用同一套调用习惯。
 *
 * 迁移说明：
 * 1. 原 TS 实现中的 convertFtpic 依赖 traffic_brain 的 /hiface 后端接口，与本项目无关，未迁移。
 * 2. 原 drawLine 的 weight 入参被忽略（写死 5），此处已修正为使用入参。
 */

/**
 * 同时绘制区域的面与边线，并给面挂载点击事件
 * @param {Object} mapApp HiMap 实例（_MapApp）
 * @param {String} strcoords 坐标串，格式 "lon,lat,lon,lat,..."
 * @param {String} color 边框颜色，默认 #0000ff（minemap 引擎此属性不生效）
 * @param {Number} weight 边框宽度，默认 5（minemap 引擎此属性不生效）
 * @param {String} fillcolor 填充颜色，默认 red
 * @param {Number} opacity 透明度 0-1，默认 0.45
 * @param {Boolean} centable 是否居中，默认 false
 * @param {Boolean} isVisible 是否展示，默认 true
 * @param {Function} clickFunc 点击回调
 * @returns {{polygon: any, polyline: any}}
 */
export function drawArea(mapApp, strcoords, color, weight, fillcolor, opacity, centable, isVisible, clickFunc) {
  try {
    const polygonObj = {
      strcoords: strcoords, // 必须传
      infohtml: null, // 默认 null，传 '<h1>aaa</h1>' 则点击时有气泡显示
      color: color, // 边框颜色，默认 #0000ff；minemap(图吧) 此属性不生效
      weight: weight, // 边框宽度，默认 5；minemap(图吧) 此属性不生效
      fillcolor: fillcolor, // 填充颜色，默认 red
      opacity: opacity, // 透明度 0-1，默认 0.45
      centable: centable || false, // 是否居中，默认 false
      isVisible: isVisible || true // 是否展示，默认 true
    }
    const tempPolygon = mapApp.showPolygon(polygonObj)
    if (tempPolygon && clickFunc) {
      tempPolygon.addListener('click', clickFunc)
    }

    const polylineObj = {
      strcoords: strcoords,
      color: color, // 颜色，默认 gray
      weight: 2, // 宽度，默认 3
      opacity: 1, // 透明度 0-1，默认 1
      arrow: 0, // 0 无方向（默认）；1 正方向；-1 负方向；arcgis 此属性不生效
      linestyle: 'none', // minemap(图吧) 此属性不生效
      centable: centable || false, // 是否居中，默认 false
      isVisible: isVisible || true // 是否展示，默认 true
    }
    const tempPolyline = mapApp.showPolyline(polylineObj)
    return { polygon: tempPolygon, polyline: tempPolyline }
  } catch (e) {
    console.error('[HiMap] drawArea 绘制失败', e)
    return { polygon: null, polyline: null }
  }
}

/**
 * 绘制由分号拼接的多段区域，逐段画面 + 边线并挂载点击事件
 * @param {Object} mapApp HiMap 实例（_MapApp）
 * @param {String} strcoords 以 ";" 分隔的多段坐标串
 * @param {String} color 边框颜色
 * @param {Number} weight 边框宽度
 * @param {String} fillcolor 填充颜色
 * @param {Number} opacity 透明度 0-1
 * @param {Boolean} centable 是否居中
 * @param {Boolean} isVisible 是否展示
 * @param {Function} clickFunc 点击回调
 * @returns {{polygon: Array, polyline: Array}}
 */
export function drawAreaMerge(mapApp, strcoords, color, weight, fillcolor, opacity, centable, isVisible, clickFunc) {
  const result = { polygon: [], polyline: [] }
  try {
    if (!strcoords) {
      return result
    }
    const arr = strcoords.split(';')
    for (let i = 0; i < arr.length; i++) {
      const tempPolygon = mapApp.showPolygon({
        strcoords: arr[i],
        infohtml: null,
        color: color,
        weight: weight,
        fillcolor: fillcolor,
        opacity: opacity,
        centable: centable || false,
        isVisible: isVisible || true
      })
      if (tempPolygon) {
        if (clickFunc) {
          tempPolygon.addListener('click', clickFunc)
        }
        result.polygon.push(tempPolygon)
      }

      const tempPolyline = mapApp.showPolyline({
        strcoords: arr[i],
        color: color,
        weight: 2,
        opacity: 1,
        arrow: 0,
        linestyle: 'none',
        centable: centable || false,
        isVisible: isVisible || true
      })
      if (tempPolyline) {
        if (clickFunc) {
          tempPolyline.addListener('click', clickFunc)
        }
        result.polyline.push(tempPolyline)
      }
    }
    return result
  } catch (e) {
    console.error('[HiMap] drawAreaMerge 绘制失败', e)
    return result
  }
}

/**
 * 绘制折线
 * @param {Object} mapApp HiMap 实例（_MapApp）
 * @param {String} strcoords 坐标串 "lon,lat,lon,lat,..."
 * @param {String} color 颜色，默认 gray
 * @param {Number} weight 宽度，默认 5
 * @param {Number} opacity 透明度 0-1，默认 1
 * @param {Boolean} centable 是否居中，默认 false
 * @param {Boolean} isVisible 是否展示，默认 true
 * @param {Function} clickFunc 点击回调
 * @returns {{polyline: any}}
 */
export function drawLine(mapApp, strcoords, color, weight, opacity, centable, isVisible, clickFunc) {
  try {
    const polylineObj = {
      strcoords: strcoords,
      color: color,
      weight: weight || 5, // 原实现忽略入参写死 5，此处改为按入参生效
      opacity: opacity || 1,
      arrow: 0, // 0 无方向（默认）；1 正方向；-1 负方向；arcgis 此属性不生效
      linestyle: 'none',
      centable: centable || false,
      isVisible: isVisible || true
    }
    const tempPolyline = mapApp.showPolyline(polylineObj)
    if (tempPolyline && clickFunc) {
      tempPolyline.addListener('click', clickFunc)
    }
    return { polyline: tempPolyline }
  } catch (e) {
    console.error('[HiMap] drawLine 绘制失败', e)
    return { polyline: null }
  }
}

/**
 * 展示文字标注（引擎不支持 showTitle 时返回 undefined）
 * @param {Object} params
 * @param {Object} params.mapApp HiMap 实例（_MapApp）
 * @param {String} params.coordinate 标注位置 "lon,lat"
 * @param {String} params.name 标注文字
 */
export function showTitle({ mapApp, coordinate, name, color, bgcolor, bordercolor, fontSize, font, borderSize }) {
  if (!mapApp || !mapApp.showTitle) {
    return undefined
  }
  return mapApp.showTitle({
    name: name, // 必须传，标题内容
    strcoords: coordinate, // 必须传，标题位置
    color: color || '#ff0000', // 文字颜色，默认白色
    bgcolor: bgcolor || '#00ff00', // 背景颜色，arcgis、mapabc 无效
    bordercolor: bordercolor || '#0000ff', // 边框颜色，arcgis、mapabc 无效
    fontSize: fontSize || 16, // 字体大小，arcgis 无效
    font: font || '隶书', // 字体，arcgis 无效
    borderSize: borderSize || 30 // 边框宽度，arcgis、mapabc 无效
  })
}

/**
 * 取折线的中间点（经验算法：中点数值小于 100 视为纬度）
 * @param {String} coordinates 坐标串
 * @returns {String} "lon,lat"，失败返回 '' 或 null
 */
export function getCenterCoordinateOfLine(coordinates) {
  try {
    if (!coordinates || coordinates.indexOf(',') < 0) {
      return ''
    }
    const arr = coordinates.split(',')
    const indexLo = Math.round(arr.length / 2)
    const middle = arr[indexLo]
    let lo = ''
    let la = ''
    if (parseFloat(middle) < 100) {
      // 算出来的中点是纬度
      lo = arr[Math.max(indexLo - 1, 0)]
      la = middle
    } else {
      // 算出来的中点是经度
      lo = middle
      la = arr[indexLo + 1]
    }
    return `${lo},${la}`
  } catch (e) {
    return null
  }
}

/**
 * 按外接矩形求坐标串的几何中心
 * @param {String} coordinates 坐标串
 * @returns {String} "lon,lat"
 */
export function getCenterCoordinate(coordinates) {
  try {
    if (!coordinates || coordinates.indexOf(',') < 0) {
      return ''
    }
    const array = coordinates.split(',')
    let left = 99999
    let right = 0
    let top = 0
    let bottom = 99999
    for (let i = 0; i < array.length; i += 2) {
      if (parseFloat(array[i]) < left) {
        left = parseFloat(array[i])
      }
      if (parseFloat(array[i]) > right) {
        right = parseFloat(array[i])
      }
      if (parseFloat(array[i + 1]) > top) {
        top = parseFloat(array[i + 1])
      }
      if (parseFloat(array[i + 1]) < bottom) {
        bottom = parseFloat(array[i + 1])
      }
    }
    const x = (left + right) / 2
    const y = (top + bottom) / 2
    return x + ',' + y
  } catch (e) {
    return null
  }
}

/**
 * 求多段坐标的经纬度边界
 * @param {Array<String>} polygons 坐标串数组
 * @returns {{maxLon: Number, minLon: Number, maxLat: Number, minLat: Number}}
 */
export function getPolygonsBoundary(polygons) {
  let maxLon = 0
  let maxLat = 0
  let minLon = 1000
  let minLat = 1000
  if (polygons && polygons.length > 0) {
    for (let i = 0; i < polygons.length; i += 1) {
      const strcoords = polygons[i] || ''
      const coorArr = strcoords.split(',')
      for (let j = 0; j < coorArr.length; j += 1) {
        if (j % 2 === 0) {
          // 经度
          const lonTemp = parseFloat(coorArr[j])
          if (lonTemp > maxLon) {
            maxLon = lonTemp
          }
          if (lonTemp < minLon) {
            minLon = lonTemp
          }
        } else {
          // 纬度
          const latTemp = parseFloat(coorArr[j])
          if (latTemp > maxLat) {
            maxLat = latTemp
          }
          if (latTemp < minLat) {
            minLat = latTemp
          }
        }
      }
    }
  }
  return { maxLon, minLon, maxLat, minLat }
}

/**
 * 根据多边形范围推算合适的中心点与缩放级别
 * @param {Array<String>} polygons 坐标串数组
 * @param {Object} mapApp HiMap 实例（_MapApp），为空时仅计算中心点
 * @param {Number} customCorrectX 中心点经度方向修正量（屏幕比例）
 * @param {Number} customCorrectY 中心点纬度方向修正量（屏幕比例）
 * @returns {{center: String, level: Number}}
 */
export function getZoomLevelOfPolygons(polygons, mapApp, customCorrectX = 0, customCorrectY = 0) {
  let level = 0
  let center = ''
  try {
    const boundary = getPolygonsBoundary(polygons)
    // 求中心点
    const centerLon = boundary.minLon + (boundary.maxLon - boundary.minLon) / 2
    const centerLat = boundary.minLat + (boundary.maxLat - boundary.minLat) / 2
    center = `${centerLon},${centerLat}`

    if (mapApp) {
      // 求合适的缩放级别
      const curScale = mapApp.getScale()
      const curLevel = mapApp.getZoomLevel()
      const maxLevel = mapApp.getMaxLevel()
      const minLevel = 0
      let lods = []
      if (curLevel > minLevel) {
        for (let i = curLevel - 1; i >= minLevel; i -= 1) {
          lods.push({
            level: i,
            scaleX: curScale.scaleX * Math.pow(2, curLevel - i),
            scaleY: curScale.scaleY * Math.pow(2, curLevel - i)
          })
        }
      }
      for (let i = curLevel; i <= maxLevel; i += 1) {
        lods.push({
          level: i,
          scaleX: curScale.scaleX / Math.pow(2, i - curLevel),
          scaleY: curScale.scaleY / Math.pow(2, i - curLevel)
        })
      }
      lods = lods.sort((a, b) => b.level - a.level)

      const pScaleX = (boundary.maxLon - boundary.minLon) / mapApp.mapdiv.clientWidth
      const pScaleY = (boundary.maxLat - boundary.minLat) / mapApp.mapdiv.clientHeight
      for (let j = 0; j < lods.length; j += 1) {
        if ((pScaleX <= lods[j].scaleX && pScaleY <= lods[j].scaleY) || j === lods.length - 1) {
          level = lods[j].level
          const correctLon = lods[j].scaleX * customCorrectX
          const correctLat = lods[j].scaleY * customCorrectY
          center = `${centerLon - correctLon},${centerLat + correctLat}`
          break
        }
      }
    }

    if (boundary.maxLon === 0 && mapApp) {
      const centerObj = mapApp.getCenterLatLng()
      center = `${centerObj.x},${centerObj.y}`
      level = mapApp.getZoomLevel()
    }
  } catch (e) {
    console.error('[HiMap] getZoomLevelOfPolygons 计算失败', e)
  }
  return { level, center }
}

/**
 * 按图形范围居中并缩放到合适级别
 * @param {Object} mapApp HiMap 实例（_MapApp）
 * @param {String} coordinates 坐标串，多段用 ";" 分隔
 */
export function correctMapView(mapApp, coordinates) {
  if (!mapApp || !coordinates) {
    return
  }
  const coorArr = coordinates.split(';')
  const { center, level } = getZoomLevelOfPolygons(coorArr, mapApp)
  mapApp.centerAndZoom(center, level || 4)
}

export default {
  drawArea,
  drawAreaMerge,
  drawLine,
  showTitle,
  getCenterCoordinateOfLine,
  getCenterCoordinate,
  getPolygonsBoundary,
  getZoomLevelOfPolygons,
  correctMapView
}
