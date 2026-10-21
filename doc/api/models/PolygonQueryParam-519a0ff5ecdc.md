# PolygonQueryParam：多边形检索摄像头参数

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/PolygonQueryParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `position` | array | `List<Point>` | 由具体接口决定 | JSON数组；元素类型见说明；多边形位置，格式： [{'lng':116.32, 'lat': 39: 39.2}, {'lng':115.32, 'lat': 39: 38.2}, {'lng':125.32, 'lat': 39: 38.2}] | 源码未声明默认值 | 多边形位置，格式： [{'lng':116.32, 'lat': 39: 39.2}, {'lng':115.32, 'lat': 39: 38.2}, {'lng':125.32, 'lat': 39: 38.2}] | `[]` |
| `level` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 地图级别 | `1` |
| `groupAlias` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 分组别名 | `"<groupAlias>"` |
| `geoCoordSys` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 坐标系类型：WGS84,GCJ02、BD09 | `"<geoCoordSys>"` |

## 关联模型

- [Point](Point-78fcb77d0775.md)：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/Point.java`
