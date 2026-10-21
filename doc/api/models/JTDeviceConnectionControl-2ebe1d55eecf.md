# JTDeviceConnectionControl：终端控制

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceConnectionControl.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `switchOn` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false 表示切换到指定监管平台服务器 ,true 表示切换回原 缺省监控平台服务器 | `true` |
| `authentication` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 监管平台鉴权码 | `"<authentication>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 拨号点名称 | `"<name>"` |
| `username` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 拨号用户名 | `"<username>"` |
| `password` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 拨号密码 | `"<32位MD5或该接口规定的密码>"` |
| `address` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 地址 | `"<address>"` |
| `tcpPort` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | TCP端口 | `1` |
| `udpPort` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | UDP端口 | `1` |
| `timeLimit` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 连接到指定服务器时限 | `1` |
