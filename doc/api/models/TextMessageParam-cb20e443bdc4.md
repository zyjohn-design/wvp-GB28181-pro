# TextMessageParam：人工确认报警消息参数

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/TextMessageParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `phoneNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端手机号 | `"<phoneNumber>"` |
| `sign` | object | `JTTextSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 标志 | `{}` |
| `textType` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 文本类型,1 = 通知 ，2 = 服务 | `1` |
| `content` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 消息内容，最长为1024字节 | `"<content>"` |

## 关联模型

- [JTTextSign](JTTextSign-022738bae3c2.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTTextSign.java`
