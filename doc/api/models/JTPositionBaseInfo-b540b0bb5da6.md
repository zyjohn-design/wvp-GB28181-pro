# JTPositionBaseInfo：位置基本信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPositionBaseInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `alarmSign` | object | `JTAlarmSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 报警标志 | `{}` |
| `status` | object | `JTStatus` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 状态 | `{}` |
| `longitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度 | `1.0` |
| `latitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 纬度 | `1.0` |
| `altitude` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 高程 | `1` |
| `speed` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 速度 | `1` |
| `direction` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 方向 | `1` |
| `time` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 时间 | `"<time>"` |
| `videoAlarm` | object | `JTVideoAlarm` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 视频报警 | `{}` |

## 关联模型

- [JTAlarmSign](JTAlarmSign-8b28a35c822e.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAlarmSign.java`
- [JTStatus](JTStatus-c5548abf055e.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTStatus.java`
- [JTVideoAlarm](JTVideoAlarm-6d12ebdf4e99.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTVideoAlarm.java`
