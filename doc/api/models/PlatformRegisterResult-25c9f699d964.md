# PlatformRegisterResult：国标级联注册结果

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/PlatformRegisterResult.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `platformId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 上级平台国标编号 | `"<platformId>"` |
| `success` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否成功 | `true` |
| `stage` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 所处阶段: LOCAL_SEND/REGISTER/REGISTER_AUTH/KEEPALIVE | `"<stage>"` |
| `statusCode` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | SIP状态码, -1024表示本地等待超时 | `1` |
| `message` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 结果描述 | `"<message>"` |
| `suggestion` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 处理建议 | `"<suggestion>"` |
| `time` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 发生时间 | `"<time>"` |
| `source` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 来源: TEST-手动测试注册, AUTO-平台自动注册 | `"<source>"` |
