# SyncStatus：摄像机同步状态

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/SyncStatus.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `total` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 总数 | `1` |
| `current` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 当前更新多少 | `1` |
| `errorMsg` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 错误描述 | `"<errorMsg>"` |
| `syncIng` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否同步中 | `true` |
| `time` | string | `Instant` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 时间 | `"<time>"` |
