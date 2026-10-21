# SipConfig

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/conf/SipConfig.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `ip` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | IP 地址 | `"<ip>"` |
| `showIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 源码未标注字段含义 | `"<showIp>"` |
| `monitorIps` | array | `List<String>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 源码未标注字段含义 | `[]` |
| `port` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 网络端口 | `1` |
| `domain` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 源码未标注字段含义 | `"<domain>"` |
| `id` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 数据库记录 ID | `"<id>"` |
| `password` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 密码等敏感字段；不得写入日志或对外示例 | `"<32位MD5或该接口规定的密码>"` |
| `alarm` | boolean | `boolean` | 由具体接口决定 | true / false | true（Java初始值；请求默认值依接口） | 源码未标注字段含义；Java初始值：true | `true` |
| `timeout` | integer | `long` | 由具体接口决定 | 整数 | 1000（Java初始值；请求默认值依接口） | 源码未标注字段含义；Java初始值：1000 | `1` |
