# MediaServer：流媒体服务信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaServer.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | ID | `"<id>"` |
| `ip` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | IP | `"<ip>"` |
| `hookIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | "127.0.0.1"（Java初始值；请求默认值依接口） | hook使用的IP（zlm访问WVP使用的IP）；Java初始值："127.0.0.1" | `"<hookIp>"` |
| `sdpIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | SDP IP | `"<sdpIp>"` |
| `streamIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流IP | `"<streamIp>"` |
| `httpPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | HTTP端口 | `1` |
| `httpSSlPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | HTTPS端口 | `1` |
| `rtmpPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | RTMP端口 | `1` |
| `flvPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | flv端口 | `1` |
| `flvSSLPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | https-flv端口 | `1` |
| `mp4Port` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | mp4端口 | `1` |
| `wsFlvPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | ws-flv端口 | `1` |
| `wsFlvSSLPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | wss-flv端口 | `1` |
| `rtmpSSlPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | RTMPS端口 | `1` |
| `rtpProxyPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | RTP收流端口（单端口模式有用） | `1` |
| `jttProxyPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 1078收流端口（单端口模式有用） | `1` |
| `rtspPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | RTSP端口 | `1` |
| `rtspSSLPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | RTSPS端口 | `1` |
| `autoConfig` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否开启自动配置ZLM | `true` |
| `secret` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | ZLM鉴权参数 | `"<secret>"` |
| `hookAliveInterval` | number | `Float` | 由具体接口决定 | 数值 | 源码未声明默认值 | keepalive hook触发间隔,单位秒 | `1.0` |
| `rtpEnable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否使用多端口模式 | `true` |
| `status` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 状态 | `true` |
| `rtpPortRange` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 多端口RTP收流端口范围 | `"<rtpPortRange>"` |
| `sendRtpPortRange` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTP发流端口范围 | `"<sendRtpPortRange>"` |
| `recordAssistPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | assist服务端口 | `1` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `updateTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `lastKeepaliveTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 上次心跳时间 | `"<lastKeepaliveTime>"` |
| `defaultServer` | boolean | `boolean` | 由具体接口决定 | true / false | ZLM | 是否是默认ZLM | `true` |
| `recordDay` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 录像存储时长 | `1` |
| `recordPath` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像存储路径 | `"<recordPath>"` |
| `type` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 类型： zlm/abl | `"<type>"` |
| `transcodeSuffix` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 转码的前缀 | `"<transcodeSuffix>"` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 服务Id | `"<serverId>"` |
