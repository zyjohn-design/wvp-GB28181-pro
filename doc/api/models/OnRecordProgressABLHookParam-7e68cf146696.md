# OnRecordProgressABLHookParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnRecordProgressABLHookParam.java`；继承 `OnRecordMp4ABLHookParam`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体节点 ID | `"<mediaServerId>"` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流id | `"<stream>"` |
| `key` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 媒体流来源编号，可以根据这个key进行关闭流媒体 可以调用delMediaStream或close_streams 函数进行关闭 | `"<key>"` |
| `networkType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 媒体流来源网络编号，可参考附表 | `1` |
| `fileName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 文件名称 | `"<fileName>"` |
| `startTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 开始时间 | `"2026-09-28 08:00:00"` |
| `endTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 结束时间 | `"2026-09-28 09:00:00"` |
| `fileSize` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 文件大小 | `1` |
| `currentFileDuration` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 当前录像文件时长 | `1` |
| `TotalVideoDuration` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 累计录像时长 | `1` |

## 关联模型

- [OnRecordMp4ABLHookParam](OnRecordMp4ABLHookParam-676d1efa033b.md)：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnRecordMp4ABLHookParam.java`
