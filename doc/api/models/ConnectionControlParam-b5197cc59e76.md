# ConnectionControlParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/ConnectionControlParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `phoneNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端手机号 | `"<phoneNumber>"` |
| `control` | object | `JTDeviceConnectionControl` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 控制选项 | `{}` |

## 关联模型

- [JTDeviceConnectionControl](JTDeviceConnectionControl-2ebe1d55eecf.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceConnectionControl.java`
