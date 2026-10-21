# JTRoutePoint：路线拐点

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRoutePoint.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 拐点 ID | `1` |
| `routeSectionId` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段 ID | `1` |
| `latitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 拐点纬度 | `1.0` |
| `longitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 拐点经度 | `1.0` |
| `routeSectionAttributeWidth` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段宽度 | `1` |
| `routeSectionAttribute` | object | `JTRouteSectionAttribute` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 路段属性 | `{}` |
| `routeSectionMaxLength` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段行驶过长國值 | `1` |
| `routeSectionMinLength` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段行驶不足國值 | `1` |
| `routeSectionMaxSpeed` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段最高速度 | `1` |
| `routeSectionOverSpeedDuration` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段超速持续时间 | `1` |
| `routeSectionNighttimeMaxSpeed` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 路段夜间最高速度 | `1` |

## 关联模型

- [JTRouteSectionAttribute](JTRouteSectionAttribute-001f70a37ca0.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRouteSectionAttribute.java`
