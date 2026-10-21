# CameraStreamContent

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/CameraStreamContent.java`；继承 `StreamContent`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `ip` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | IP | `"<ip>"` |
| `flv` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTP-FLV流地址 | `"<flv>"` |
| `https_flv` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTPS-FLV流地址 | `"<https_flv>"` |
| `ws_flv` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websocket-FLV流地址 | `"<ws_flv>"` |
| `wss_flv` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websockets-FLV流地址 | `"<wss_flv>"` |
| `fmp4` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTP-FMP4流地址 | `"<fmp4>"` |
| `https_fmp4` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTPS-FMP4流地址 | `"<https_fmp4>"` |
| `ws_fmp4` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websocket-FMP4流地址 | `"<ws_fmp4>"` |
| `wss_fmp4` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websockets-FMP4流地址 | `"<wss_fmp4>"` |
| `hls` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HLS流地址 | `"<hls>"` |
| `https_hls` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTPS-HLS流地址 | `"<https_hls>"` |
| `ws_hls` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websocket-HLS流地址 | `"<ws_hls>"` |
| `wss_hls` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websockets-HLS流地址 | `"<wss_hls>"` |
| `ts` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTP-TS流地址 | `"<ts>"` |
| `https_ts` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | HTTPS-TS流地址 | `"<https_ts>"` |
| `ws_ts` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websocket-TS流地址 | `"<ws_ts>"` |
| `wss_ts` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | Websockets-TS流地址 | `"<wss_ts>"` |
| `rtmp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTMP流地址 | `"<rtmp>"` |
| `rtmps` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTMPS流地址 | `"<rtmps>"` |
| `rtsp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTSP流地址 | `"<rtsp>"` |
| `rtsps` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTSPS流地址 | `"<rtsps>"` |
| `rtc` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTC流地址 | `"<rtc>"` |
| `rtcs` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | RTCS流地址 | `"<rtcs>"` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体ID | `"<mediaServerId>"` |
| `mediaInfo` | object | `MediaInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流编码信息 | `{}` |
| `startTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 开始时间 | `"2026-09-28 08:00:00"` |
| `endTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 结束时间 | `"2026-09-28 09:00:00"` |
| `duration` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 时长(回放时使用) | `1.0` |
| `downLoadFilePath` | object | `DownloadFileInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 文件下载地址（录像下载使用） | `{}` |
| `transcodeStream` | object | `StreamContent` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 转码后的视频流 | `{}` |
| `progress` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 处理进度 | `1.0` |
| `key` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 拉流代理返回的KEY | `"<key>"` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 使用的WVP ID | `"<serverId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `controlType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 控制类型 | `1` |

## 关联模型

- [DownloadFileInfo](DownloadFileInfo-e031dc1b3a4a.md)：`src/main/java/com/genersoft/iot/vmp/service/bean/DownloadFileInfo.java`
- [MediaInfo](MediaInfo-83b8845da7f8.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaInfo.java`
- [StreamContent](StreamContent-3b954a65f1fc.md)：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/StreamContent.java`
