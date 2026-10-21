# CloudRecordItem：云端录像记录

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/CloudRecordItem.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 录像记录数据库 ID | `1` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流 ID | `"<stream>"` |
| `callId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 该次录像关联的会话 Call-ID | `"<callId>"` |
| `startTime` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 媒体节点上报的录像开始时间戳 | `"2026-09-28 08:00:00"` |
| `endTime` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 录像结束时间戳；由开始时间与录像时长计算 | `"2026-09-28 09:00:00"` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体节点 ID | `"<mediaServerId>"` |
| `fileName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像文件名 | `"<fileName>"` |
| `filePath` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像文件路径 | `"<filePath>"` |
| `folder` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像所在文件夹 | `"<folder>"` |
| `collect` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否收藏 | `true` |
| `reserve` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否保留 | `true` |
| `fileSize` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 录像文件大小，字节 | `1` |
| `timeLen` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 媒体节点上报的录像时长；单位需按所用媒体节点核对 | `1.0` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 所属 WVP 服务节点 ID | `"<serverId>"` |
