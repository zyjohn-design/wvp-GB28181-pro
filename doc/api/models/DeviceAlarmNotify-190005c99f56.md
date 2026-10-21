# DeviceAlarmNotify：报警通知

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DeviceAlarmNotify.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备的国标编号 | `"<deviceId>"` |
| `deviceName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备名称 | `"<deviceName>"` |
| `channelId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 通道的国标编号 | `"<channelId>"` |
| `alarmPriority` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警级别, 1为一级警情, 2为二级警情, 3为三级警情, 4为四级警情 | `"<alarmPriority>"` |
| `alarmPriorityDescription` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警级别, 1为一级警情, 2为二级警情, 3为三级警情, 4为四级警情 | `"<alarmPriorityDescription>"` |
| `alarmMethod` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 报警方式 , 1为电话报警, 2为设备报警, 3为短信报警, 4为 GPS报警, 5为视频报警, 6为设备故障报警,\n\t * 7其他报警;可以为直接组合如12为电话报警或设备报警 | `1` |
| `alarmMethodDescription` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警方式说明 | `"<alarmMethodDescription>"` |
| `alarmTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警时间 | `"<alarmTime>"` |
| `alarmDescription` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警内容描述 | `"<alarmDescription>"` |
| `longitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度 | `1.0` |
| `latitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 纬度 | `1.0` |
| `alarmType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 报警类型 | `1` |
| `eventType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 事件类型, 在入侵检测报警时可携带 | `1` |
| `alarmTypeDescription` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 报警类型描述 | `"<alarmTypeDescription>"` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
