# JT1078Config

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/config/JT1078Config.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `port` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 网络端口 | `1` |
| `password` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 密码等敏感字段；不得写入日志或对外示例 | `"<32位MD5或该接口规定的密码>"` |
| `record` | boolean | `Boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 源码未标注字段含义；Java初始值：false | `true` |
| `readerIdleTime` | integer | `Integer` | 由具体接口决定 | 整数 | 0（Java初始值；请求默认值依接口） | IDLE状态超时时间，单位：秒，默认0表示不启用，启用后当连接进入IDLE状态超过该时间时将被断开连接 连接进入IDLE状态的条件是：在readerIdleTime时间内没有收到任何数据包，并且在writerIdleTime时间内没有发送任何数据包；Java初始值：0 | `1` |
