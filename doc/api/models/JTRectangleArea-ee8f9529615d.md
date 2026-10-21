# JTRectangleArea：矩形区域

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRectangleArea.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 区域 ID | `1` |
| `attribute` | object | `JTAreaAttribute` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 设备或终端属性 | `{}` |
| `latitudeForUpperLeft` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 左上点纬度 | `1.0` |
| `longitudeForUpperLeft` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 左上点经度 | `1.0` |
| `latitudeForLowerRight` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 右下点纬度 | `1.0` |
| `longitudeForLowerRight` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 右下点经度 | `1.0` |
| `startTime` | string | `String` | 由具体接口决定 | yyyy-MM-dd HH:mm:ss | 源码未声明默认值 | 起始时间, yyyy-MM-dd HH:mm:ss | `"2026-09-28 08:00:00"` |
| `endTime` | string | `String` | 由具体接口决定 | yyyy-MM-dd HH:mm:ss | 源码未声明默认值 | 结束时间, yyyy-MM-dd HH:mm:ss | `"2026-09-28 09:00:00"` |
| `maxSpeed` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 最高速度, 单位为千米每小时(km/h) | `1` |
| `overSpeedDuration` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 超速持续时间, 单位为秒(s) | `1` |
| `nighttimeMaxSpeed` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 夜间最高速度, 单位为千米每小时(km/h) | `1` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 区域的名称 | `"<name>"` |

## 关联模型

- [JTAreaAttribute](JTAreaAttribute-c13dfac6bc8d.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAreaAttribute.java`
