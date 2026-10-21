# OnStreamArriveABLHookParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnStreamArriveABLHookParam.java`；继承 `ABLHookParam`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体节点 ID | `"<mediaServerId>"` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流id | `"<stream>"` |
| `key` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 媒体流来源编号，可以根据这个key进行关闭流媒体 可以调用delMediaStream或close_streams 函数进行关闭 | `"<key>"` |
| `networkType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 媒体流来源网络编号，可参考附表 | `1` |
| `callId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 推流鉴权Id | `"<callId>"` |
| `status` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 状态 | `true` |
| `enableHls` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用 HLS | `true` |
| `transcodingStatus` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 转码状态 | `true` |
| `sourceURL` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 源流 URL | `"<sourceURL>"` |
| `readerCount` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 观看人数 | `1` |
| `noneReaderDuration` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 无观看者持续时间 | `1` |
| `videoCodec` | string | `String` | 由具体接口决定 | 视频编码格式 | 源码未声明默认值 | 视频编码格式 | `"<videoCodec>"` |
| `videoFrameSpeed` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 视频帧率 | `1` |
| `width` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 画面宽度 | `1` |
| `height` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 画面高度 | `1` |
| `videoBitrate` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 视频码率 | `1` |
| `audioCodec` | string | `String` | 由具体接口决定 | 音频编码格式 | 源码未声明默认值 | 音频编码格式 | `"<audioCodec>"` |
| `audioChannels` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音频声道数 | `1` |
| `audioSampleRate` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音频采样率 | `1` |
| `audioBitrate` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音频码率 | `1` |
| `url` | object | `ABLUrls` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 资源 URL | `{}` |

## 关联模型

- [ABLHookParam](ABLHookParam-80b2341fc82a.md)：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/ABLHookParam.java`
- [ABLUrls](ABLUrls-0cfc20c1aced.md)：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/ABLUrls.java`
