# MapConfig

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/MapConfig.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `coordinateSystem` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 地图坐标系 | `"<coordinateSystem>"` |
| `tilesUrl` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 地图瓦片 URL | `"<tilesUrl>"` |
| `tileSize` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 地图瓦片尺寸 | `1` |
| `zoom` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 地图缩放级别 | `1` |
| `center` | array | `Double[]` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 地图中心坐标 | `[]` |
| `maxZoom` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 地图最大缩放级别 | `1` |
| `minZoom` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 地图最小缩放级别 | `1` |
