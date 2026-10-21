# HookData

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/event/hook/HookData.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `mediaServer` | object | `MediaServer` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流媒体节点 | `{}` |
| `schema` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 协议 | `"<schema>"` |
| `mediaInfo` | object | `MediaInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 流信息 | `{}` |
| `recordInfo` | object | `RecordInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 录像信息 | `{}` |
| `params` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 推流的额外参数 | `"<params>"` |

## 关联模型

- [MediaInfo](MediaInfo-83b8845da7f8.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaInfo.java`
- [MediaServer](MediaServer-b3c8996f50c7.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaServer.java`
- [RecordInfo](RecordInfo-7751dc26be42.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/RecordInfo.java`
