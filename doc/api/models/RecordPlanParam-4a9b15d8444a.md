# RecordPlanParam：录制计划-添加/编辑参数

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/recordPlan/bean/RecordPlanParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `channelIds` | array | `List<Integer>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 关联的通道ID | `[]` |
| `deviceDbIds` | array | `List<Integer>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 关联的设备ID，会为设备下的所有通道关联此录制计划，channelId存在是此项不生效， | `[]` |
| `allLink` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 全部关联/全部取消关联 | `true` |
| `planId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 录制计划ID, ID为空是删除关联的计划 | `1` |
