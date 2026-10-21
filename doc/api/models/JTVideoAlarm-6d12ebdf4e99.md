# JTVideoAlarm：视频报警上报

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTVideoAlarm.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `videoLossChannels` | array | `List<Integer>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 视频信号丢失报警的通道 | `[]` |
| `videoOcclusionChannels` | array | `List<Integer>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 视频信号遮挡报警的通道 | `[]` |
| `storageFaultAlarm` | array | `List<Integer>` | 由具体接口决定 | JSON数组；元素类型见说明；1-12；12-15；1-4 | 源码未声明默认值 | 存储器故障报警状态，第 1-12 个主存储器，12-15 分别表示第 1-4 个灾备存储装置 | `[]` |
| `drivingForFatigue` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 异常驾驶行为-疲劳 | `true` |
| `drivingForCall` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 异常驾驶行为-打电话 | `true` |
| `drivingSmoking` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 异常驾驶行为-抽烟 | `true` |
| `otherDeviceFailure` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 其他视频设备故障 | `true` |
| `overcrowding` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 客车超员报警 | `true` |
| `specialRecordFull` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 特殊报警录像达到存储阈值报警 | `true` |
