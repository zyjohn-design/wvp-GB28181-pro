# BasicParam：基础配置

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/BasicParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备ID | `"<deviceId>"` |
| `channelId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 通道ID，如果时对设备配置直接设置同设备ID一样即可 | `"<channelId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `expiration` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 注册过期时间 | `"<expiration>"` |
| `heartBeatInterval` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 心跳间隔时间 | `1` |
| `heartBeatCount` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 心跳超时次数 | `1` |
| `positionCapability` | integer | `Integer` | 由具体接口决定 | 整数；取值:0-不支持；2-支持北斗定位(可选,默认取值为0)，用于接受配置查询结果， 基础配置时无效 | 取值为0) | 定位功能支持情况。取值:0-不支持;1-支持 GPS定位;2-支持北斗定位(可选,默认取值为0)，用于接受配置查询结果， 基础配置时无效 | `1` |
| `longitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度(可选)，用于接受配置查询结果， 基础配置时无效 | `1.0` |
| `latitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 纬度(可选)，用于接受配置查询结果， 基础配置时无效 | `1.0` |
