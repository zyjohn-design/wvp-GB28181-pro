# JTChanelConfig

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTChanelConfig.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `physicalChannelId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 物理通道号 单独 | `1` |
| `logicChannelId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 逻辑通道号 | `1` |
| `channelType` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道类型: 0:音视频; 1:音频 2:视频 | `1` |
| `ptzEnable` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 是否连接云台: 通道类型为 0 和 2 时,此字段有效 0:未连接;1:连接 | `1` |
