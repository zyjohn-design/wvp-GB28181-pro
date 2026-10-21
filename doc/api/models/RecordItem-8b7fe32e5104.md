# RecordItem：设备录像详情

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/RecordItem.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备编号 | `"<deviceId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `filePath` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 文件路径名 (可选) | `"<filePath>"` |
| `fileSize` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像文件大小,单位:Byte(可选) | `"<fileSize>"` |
| `address` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像地址(可选) | `"<address>"` |
| `startTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像开始时间(可选) | `"2026-09-28 08:00:00"` |
| `endTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像结束时间(可选) | `"2026-09-28 09:00:00"` |
| `secrecy` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 保密属性(必选)缺省为0;0:不涉密,1:涉密 | `1` |
| `type` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像产生类型(可选)time或alarm 或 manual | `"<type>"` |
| `recorderId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 录像触发者ID(可选) | `"<recorderId>"` |
