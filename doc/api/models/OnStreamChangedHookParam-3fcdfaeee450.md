# OnStreamChangedHookParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnStreamChangedHookParam.java`；继承 `HookParam`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `regist` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 注册/注销 | `true` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流id | `"<stream>"` |
| `callId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 推流鉴权Id | `"<callId>"` |
| `totalReaderCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 观看总人数，包括hls/rtsp/rtmp/http-flv/ws-flv | `1` |
| `schema` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 协议 包括hls/rtsp/rtmp/http-flv/ws-flv | `"<schema>"` |
| `originType` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 产生源类型， unknown = 0, rtmp_push=1, rtsp_push=2, rtp_push=3, pull=4, ffmpeg_pull=5, mp4_vod=6, device_chn=7 | `1` |
| `originSock` | object | `OriginSock` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 客户端和服务器网络信息，可能为null类型 | `{}` |
| `originTypeStr` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 产生源类型的字符串描述 | `"<originTypeStr>"` |
| `originUrl` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 产生源的url | `"<originUrl>"` |
| `severId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 服务器id | `"<severId>"` |
| `createStamp` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | GMT unix系统时间戳，单位秒 | `1` |
| `aliveSecond` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 存活时间，单位秒 | `1` |
| `bytesSpeed` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据产生速度，单位byte/s | `1` |
| `tracks` | array | `List<MediaTrack>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 音视频轨道 | `[]` |
| `vhost` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 音视频轨道 | `"<vhost>"` |
| `params` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 额外的参数字符串 | `"<params>"` |
| `paramMap` | object | `Map<String, String>` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 额外的参数 | `{}` |
| `docker` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否是docker部署， docker部署不会自动更新zlm使用的端口，需要自己手动修改 | `true` |
| `streamInfo` | object | `StreamContent` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流信息对象 | `{}` |

## 关联模型

- [StreamContent](StreamContent-3b954a65f1fc.md)：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/StreamContent.java`
