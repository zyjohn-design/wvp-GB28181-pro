# JTDeviceAttribute：JT终端属性

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceAttribute.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `type` | object | `JTDeviceType` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 终端类型 | `{}` |
| `makerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 制造商 ID | `"<makerId>"` |
| `deviceModel` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端型号 | `"<deviceModel>"` |
| `terminalId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端 ID | `"<terminalId>"` |
| `iccId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端 SIM卡 ICCID | `"<iccId>"` |
| `hardwareVersion` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端硬件版本号 | `"<hardwareVersion>"` |
| `firmwareVersion` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 固件版本号 | `"<firmwareVersion>"` |
| `gnssAttribute` | object | `JTGnssAttribute` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | GNSS 模块属性 | `{}` |
| `communicationModuleAttribute` | object | `JTCommunicationModuleAttribute` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 通信模块属性 | `{}` |

## 关联模型

- [JTCommunicationModuleAttribute](JTCommunicationModuleAttribute-55b242d324d3.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTCommunicationModuleAttribute.java`
- [JTDeviceType](JTDeviceType-a8bc4f2ca2f7.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceType.java`
- [JTGnssAttribute](JTGnssAttribute-cada62747d79.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTGnssAttribute.java`
