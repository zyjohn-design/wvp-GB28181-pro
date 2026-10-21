# RecordInfo

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/storager/dao/dto/RecordInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | ID | `1` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 对应的zlm流媒体的ID | `"<mediaServerId>"` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `type` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 类型 对应zlm的 originType unknown = 0, rtmp_push=1, rtsp_push=2, rtp_push=3, pull=4, ffmpeg_pull=5, mp4_vod=6, device_chn=7, rtc_push=8 | `1` |
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标录像时的设备ID | `"<deviceId>"` |
| `channelId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标录像时的通道ID | `"<channelId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 拉流代理录像时的名称 | `"<name>"` |
