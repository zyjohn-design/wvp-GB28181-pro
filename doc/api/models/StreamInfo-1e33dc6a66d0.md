# StreamInfo：流信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/common/StreamInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备编号 | `"<deviceId>"` |
| `channelId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道ID | `1` |
| `ip` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | IP | `"<ip>"` |
| `flv` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTP-FLV流地址 | `{}` |
| `https_flv` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTPS-FLV流地址 | `{}` |
| `ws_flv` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websocket-FLV流地址 | `{}` |
| `wss_flv` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websockets-FLV流地址 | `{}` |
| `fmp4` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTP-FMP4流地址 | `{}` |
| `https_fmp4` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTPS-FMP4流地址 | `{}` |
| `ws_fmp4` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websocket-FMP4流地址 | `{}` |
| `wss_fmp4` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websockets-FMP4流地址 | `{}` |
| `hls` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HLS流地址 | `{}` |
| `https_hls` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTPS-HLS流地址 | `{}` |
| `ws_hls` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websocket-HLS流地址 | `{}` |
| `wss_hls` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websockets-HLS流地址 | `{}` |
| `ts` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTP-TS流地址 | `{}` |
| `https_ts` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | HTTPS-TS流地址 | `{}` |
| `ws_ts` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websocket-TS流地址 | `{}` |
| `wss_ts` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | Websockets-TS流地址 | `{}` |
| `rtmp` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTMP流地址 | `{}` |
| `rtmps` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTMPS流地址 | `{}` |
| `rtsp` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTSP流地址 | `{}` |
| `rtsps` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTSPS流地址 | `{}` |
| `rtc` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTC流地址 | `{}` |
| `rtcs` | object | `StreamURL` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | RTCS流地址 | `{}` |
| `mediaServer` | object | `MediaServer` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流媒体节点 | `{}` |
| `mediaInfo` | object | `MediaInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流编码信息 | `{}` |
| `startTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 开始时间 | `"2026-09-28 08:00:00"` |
| `endTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 结束时间 | `"2026-09-28 09:00:00"` |
| `duration` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 时长(回放时使用) | `1.0` |
| `progress` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 进度（录像下载使用） | `1.0` |
| `downLoadFilePath` | object | `DownloadFileInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 文件下载地址（录像下载使用） | `{}` |
| `callId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 点播请求的callId | `"<callId>"` |
| `pause` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否暂停（录像回放使用） | `true` |
| `originType` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 产生源类型，包括 unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7 | `1` |
| `originTypeStr` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | originType的文本描述 | `"<originTypeStr>"` |
| `transcodeStream` | object | `StreamInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 转码后的视频流 | `{}` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 使用的WVP ID | `"<serverId>"` |
| `key` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流绑定的流媒体操作key | `"<key>"` |
| `transactionInfo` | object | `TransactionInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | SIP 事务信息 | `{}` |

## 关联模型

- [DownloadFileInfo](DownloadFileInfo-e031dc1b3a4a.md)：`src/main/java/com/genersoft/iot/vmp/service/bean/DownloadFileInfo.java`
- [MediaInfo](MediaInfo-83b8845da7f8.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaInfo.java`
- [MediaServer](MediaServer-b3c8996f50c7.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaServer.java`
- [StreamURL](StreamURL-7d546efcc75a.md)：`src/main/java/com/genersoft/iot/vmp/common/StreamURL.java`
