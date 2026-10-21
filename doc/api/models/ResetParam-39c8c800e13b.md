# ResetParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ResetParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据库记录 ID | `1` |
| `chanelFields` | array | `List<String>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 需要重置的通道字段名列表 | `[]` |
