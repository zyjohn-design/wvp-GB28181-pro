# Alarm：报警信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/Alarm.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据库id | `1` |
| `channelId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 关联通道的数据库id | `1` |
| `channelDeviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 关联通道国标编号 | `"<channelDeviceId>"` |
| `channelName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 关联通道国标名称 | `"<channelName>"` |
| `description` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警描述 | `"<description>"` |
| `snapPath` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警快照路径 | `"<snapPath>"` |
| `recordPath` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警录像路径 | `"<recordPath>"` |
| `longitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 报警附带的经度 | `1.0` |
| `latitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 报警附带的纬度 | `1.0` |
| `alarmType` | object | `AlarmType` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 报警类别 | `{}` |
| `alarmTime` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 报警时间 | `1` |

## 关联模型

- [AlarmType](AlarmType-1cbd9d59c620.md)：`src/main/java/com/genersoft/iot/vmp/service/bean/AlarmType.java`
