# InviteInfo

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/common/InviteInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备国标编号 | `"<deviceId>"` |
| `channelId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道标识 | `1` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流 ID | `"<stream>"` |
| `ssrcInfo` | object | `SSRCInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTP 流 SSRC 信息 | `{}` |
| `receiveIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTP 收流 IP | `"<receiveIp>"` |
| `receivePort` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | RTP 收流端口 | `1` |
| `streamMode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 媒体传输模式 | `"<streamMode>"` |
| `type` | object | `InviteSessionType` | 由具体接口决定 | JSON对象；字段见模型表；可选值见对应枚举 | 源码未声明默认值 | 业务类型；可选值见对应枚举 | `{}` |
| `status` | object | `InviteSessionStatus` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 状态 | `{}` |
| `streamInfo` | object | `StreamInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流信息对象 | `{}` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体节点 ID | `"<mediaServerId>"` |
| `expirationTime` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 到期时间 | `1` |
| `createTime` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 创建时间 | `1` |

## 关联模型

- [SSRCInfo](SSRCInfo-3f1d3bb00bf3.md)：`src/main/java/com/genersoft/iot/vmp/service/bean/SSRCInfo.java`
- [StreamInfo](StreamInfo-1e33dc6a66d0.md)：`src/main/java/com/genersoft/iot/vmp/common/StreamInfo.java`
