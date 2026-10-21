# StreamProxyParam：拉流代理的信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/streamProxy/bean/StreamProxyParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `type` | string | `String` | 由具体接口决定 | 类型，取值，default： 流媒体直接拉流（默认），ffmpeg： ffmpeg实现拉流 | ） | 类型，取值，default： 流媒体直接拉流（默认），ffmpeg： ffmpeg实现拉流 | `"<type>"` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体服务ID | `"<mediaServerId>"` |
| `url` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 拉流地址 | `"<url>"` |
| `timeoutMs` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 超时时间:秒 | `1` |
| `ffmpegCmdKey` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | ffmpeg模板KEY | `"<ffmpegCmdKey>"` |
| `rtpType` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播 | `"<rtpType>"` |
| `enable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用 | `true` |
| `enableAudio` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用音频 | `true` |
| `enableMp4` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用MP4 | `true` |
| `enableDisableNoneReader` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否 无人观看时自动停用 | `true` |
