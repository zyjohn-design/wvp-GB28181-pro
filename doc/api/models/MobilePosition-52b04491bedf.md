# MobilePosition

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/MobilePosition.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `channelId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道数据库自增Id | `1` |
| `channelDeviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 通道国标编号 | `"<channelDeviceId>"` |
| `timestamp` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通知时间 | `1` |
| `longitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度 | `1.0` |
| `latitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 纬度 | `1.0` |
| `altitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 海拔高度 | `1.0` |
| `speed` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 速度 | `1.0` |
| `direction` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 方向 | `1.0` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
