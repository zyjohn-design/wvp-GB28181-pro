# Platform：平台信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Platform.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | ID(数据库中) | `1` |
| `enable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用 | `true` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `serverGBId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | SIP服务国标编码 | `"<serverGBId>"` |
| `serverGBDomain` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | SIP服务国标域 | `"<serverGBDomain>"` |
| `serverIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | SIP服务IP | `"<serverIp>"` |
| `serverPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | SIP服务端口 | `1` |
| `deviceGBId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备国标编号 | `"<deviceGBId>"` |
| `deviceIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备ip | `"<deviceIp>"` |
| `devicePort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 设备端口 | `1` |
| `username` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 使用设备国标编号) | SIP认证用户名(默认使用设备国标编号) | `"<username>"` |
| `password` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | SIP认证密码 | `"<32位MD5或该接口规定的密码>"` |
| `expires` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 注册周期 (秒) | `1` |
| `keepTimeout` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 心跳周期(秒) | `1` |
| `transport` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 传输协议 | `"<transport>"` |
| `characterSet` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 字符集 | `"<characterSet>"` |
| `ptz` | boolean | `boolean` | 由具体接口决定 | true / false；允许云台控制 | 源码未声明默认值 | 允许云台控制 | `true` |
| `rtcp` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | RTCP流保活 | `true` |
| `status` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 在线状态 | `true` |
| `channelCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道数量 | `1` |
| `catalogSubscribe` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 已被订阅目录信息 | `true` |
| `alarmSubscribe` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 已被订阅报警信息 | `true` |
| `mobilePositionSubscribe` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 已被订阅移动位置信息 | `true` |
| `catalogGroup` | integer | `int` | 由具体接口决定 | 整数；目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8 | 源码未声明默认值 | 目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8 | `1` |
| `updateTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `asMessageChannel` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否作为消息通道 | `true` |
| `sendStreamIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 点播回复200OK使用的IP | `"<sendStreamIp>"` |
| `autoPushChannel` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否自动推送通道变化 | `true` |
| `catalogWithPlatform` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 目录信息包含平台信息, 0：关闭，1：打开 | `1` |
| `catalogWithGroup` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 目录信息包含分组信息, 0：关闭，1：打开 | `1` |
| `catalogWithRegion` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 目录信息包含行政区划, 0：关闭，1：打开 | `1` |
| `civilCode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 行政区划 | `"<civilCode>"` |
| `manufacturer` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 平台厂商 | `"<manufacturer>"` |
| `model` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 平台型号 | `"<model>"` |
| `address` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 平台安装地址 | `"<address>"` |
| `registerWay` | integer | `int` | 由具体接口决定 | 整数 | 1（Java初始值；请求默认值依接口） | 注册方式（必选）缺省为1； 1-符合IETF RFC 3261标准的认证注册模式；2-基于口令的双向认证注册模式；3-基于数字证书的双向认证注册模式(高安全级别要求)；4-基于数字证书的单向认证注册模式（高安全级别要求）；Java初始值：1 | `1` |
| `secrecy` | integer | `int` | 由具体接口决定 | 整数 | 0（Java初始值；请求默认值依接口） | 保密属性（必选）缺省为0；0-不涉密，1-涉密；Java初始值：0 | `1` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 执行注册的服务ID | `"<serverId>"` |
| `lastRegisterResult` | object | `PlatformRegisterResult` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 最近一次注册结果(不存储于数据库) | `{}` |

## 关联模型

- [PlatformRegisterResult](PlatformRegisterResult-25c9f699d964.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/PlatformRegisterResult.java`
