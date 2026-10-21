# RecordInfo

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/bean/RecordInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流 ID | `"<stream>"` |
| `fileName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 文件名称 | `"<fileName>"` |
| `filePath` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 文件路径 | `"<filePath>"` |
| `fileSize` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 文件大小 | `1` |
| `folder` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 文件夹路径 | `"<folder>"` |
| `url` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 资源 URL | `"<url>"` |
| `startTime` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 单位毫秒 | `"2026-09-28 08:00:00"` |
| `timeLen` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 单位毫秒 | `1.0` |
| `params` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 附加参数 | `"<params>"` |
