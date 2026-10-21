# Device：国标设备/平台

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Device.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据库自增ID | `1` |
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备国标编号 | `"<deviceId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `manufacturer` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 生产厂商 | `"<manufacturer>"` |
| `model` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 型号 | `"<model>"` |
| `firmware` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 固件版本 | `"<firmware>"` |
| `transport` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 传输协议（UDP/TCP） | `"<transport>"` |
| `streamMode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 数据流传输模式 | `"<streamMode>"` |
| `ip` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | IP | `"<ip>"` |
| `port` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 端口 | `1` |
| `hostAddress` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | wan地址 | `"<hostAddress>"` |
| `onLine` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否在线，true为在线，false为离线 | `true` |
| `registerTimeStamp` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 注册时间戳 | `1` |
| `keepaliveTimeStamp` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 心跳时间 | `1` |
| `heartBeatInterval` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 心跳间隔 | `1` |
| `heartBeatCount` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 心跳超时次数 | `1` |
| `positionCapability` | integer | `Integer` | 由具体接口决定 | 整数；取值:0-不支持；2-支持北斗定位(可选,默认取值为0 | 取值为0 | 定位功能支持情况。取值:0-不支持;1-支持 GPS定位;2-支持北斗定位(可选,默认取值为0 | `1` |
| `channelCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道个数 | `1` |
| `expires` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 注册有效期 | `1` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `updateTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | null | 设备使用的媒体id, 默认为null | `"<mediaServerId>"` |
| `charset` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 字符集, 支持 UTF-8、GB2312、GBK 与 GB18030 | `"<charset>"` |
| `subscribeCycleForCatalog` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 目录订阅周期，o为不订阅 | `1` |
| `subscribeCycleForMobilePosition` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 移动设备位置订阅周期，0为不订阅 | `1` |
| `mobilePositionSubmissionInterval` | integer | `int` | 由具体接口决定 | 整数 | 5（Java初始值；请求默认值依接口） | 移动设备位置信息上报时间间隔,单位:秒,默认值5；Java初始值：5 | `1` |
| `subscribeCycleForAlarm` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 报警心跳时间订阅周期，0为不订阅 | `1` |
| `ssrcCheck` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 是否开启ssrc校验，默认关闭，开启可以防止串流；Java初始值：false | `true` |
| `geoCoordSys` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 地理坐标系， 目前支持 WGS84,GCJ02 | `"<geoCoordSys>"` |
| `password` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 密码 | `"<32位MD5或该接口规定的密码>"` |
| `sdpIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 收流IP | `"<sdpIp>"` |
| `localIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | SIP交互IP（设备访问平台的IP） | `"<localIp>"` |
| `asMessageChannel` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否作为消息通道 | `true` |
| `sipTransactionInfo` | object | `SipTransactionInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 设备注册的事务信息 | `{}` |
| `broadcastPushAfterAck` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 控制语音对讲流程，释放收到ACK后发流 | `true` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 所属服务Id | `"<serverId>"` |
| `typeCode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标编号中的三位类型编码（派生字段） | `"<typeCode>"` |
| `typeName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标编号类型名称（派生字段） | `"<typeName>"` |
| `accessType` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 接入类型：DEVICE-独立设备，PLATFORM-下级平台（派生字段） | `"<accessType>"` |
| `accessTypeName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 接入类型名称（派生字段） | `"<accessTypeName>"` |

## 关联模型

- [SipTransactionInfo](SipTransactionInfo-7287446e4380.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/SipTransactionInfo.java`
