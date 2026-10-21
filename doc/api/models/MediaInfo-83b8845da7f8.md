# MediaInfo：视频信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `mediaServer` | object | `MediaServer` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流媒体节点 | `{}` |
| `schema` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 协议 | `"<schema>"` |
| `readerCount` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 观看人数 | `1` |
| `videoCodec` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 视频编码类型 | `"<videoCodec>"` |
| `width` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 视频宽度 | `1` |
| `height` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 视频高度 | `1` |
| `fps` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | FPS | `1` |
| `loss` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 丢包率 | `1` |
| `audioCodec` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 音频编码类型 | `"<audioCodec>"` |
| `audioChannels` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音频通道数 | `1` |
| `audioSampleRate` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音频采样率 | `1` |
| `duration` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 时长 | `1` |
| `online` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 在线 | `true` |
| `originType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7,rtc_push=8 | `1` |
| `originTypeStr` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | originType的文本描述 | `"<originTypeStr>"` |
| `originUrl` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 产生流的源流地址 | `"<originUrl>"` |
| `aliveSecond` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 存活时间，单位秒 | `1` |
| `bytesSpeed` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据产生速度，单位byte/s | `1` |
| `callId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 鉴权参数 | `"<callId>"` |
| `paramMap` | object | `Map<String, String>` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 额外参数 | `{}` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 服务ID | `"<serverId>"` |

## 关联模型

- [MediaServer](MediaServer-b3c8996f50c7.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaServer.java`
