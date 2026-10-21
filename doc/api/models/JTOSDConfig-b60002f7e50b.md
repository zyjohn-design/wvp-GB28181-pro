# JTOSDConfig

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTOSDConfig.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `time` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 日期和时间 | `true` |
| `licensePlate` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 车牌号码 | `true` |
| `channelId` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 逻辑通道号 | `true` |
| `position` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 经纬度 | `true` |
| `speed` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 行驶记录速度 | `true` |
| `speedForGPS` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 卫星定位速度 | `true` |
| `drivingTime` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 连续驾驶时间 | `true` |
