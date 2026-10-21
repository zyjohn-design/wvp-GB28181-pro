# JTDriverInformation：驾驶员身份信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDriverInformation.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `status` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 0x01:从业资格证 IC卡插入( 驾驶员上班)；0x02:从 业资格证 IC卡拔出(驾驶员下班) | `1` |
| `time` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 插卡/拔卡时间 ,以下字段在状 态为0x01 时才有效并做填充 | `"<time>"` |
| `result` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | IC卡读取结果:0x00:IC卡读卡成功；0x01:读卡失败 ,原因为卡片密钥认证未通过；0x02:读卡失败 ,原因为卡片已被锁定；0x03:读卡失败 ,原因为卡片被拔出；0x04:读卡失败 ,原因为数据校验错误。以下字段在 IC卡读取结果等于0x00 时才有效 | `1` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 驾驶员姓名 | `"<name>"` |
| `certificateCode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 从业资格证编码 | `"<certificateCode>"` |
| `certificateIssuanceMechanismName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 发证机构名称 | `"<certificateIssuanceMechanismName>"` |
| `expire` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 证件有效期 | `"<expire>"` |
| `driverIdNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 驾驶员身份证号 | `"<driverIdNumber>"` |
