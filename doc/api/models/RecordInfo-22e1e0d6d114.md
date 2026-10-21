# RecordInfo：设备录像查询结果信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/RecordInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备编号 | `"<deviceId>"` |
| `channelId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 通道编号 | `"<channelId>"` |
| `sn` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 命令序列号 | `"<sn>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备名称 | `"<name>"` |
| `sumNum` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 列表总数 | `1` |
| `count` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 记录数量 | `20` |
| `lastTime` | string | `Instant` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 最近一次时间 | `"<lastTime>"` |
| `recordList` | array | `List<RecordItem>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 录像记录列表 | `[]` |

## 关联模型

- [RecordItem](RecordItem-8b7fe32e5104.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/RecordItem.java`
