# LoginUser

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/conf/security/dto/LoginUser.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `user` | object | `User` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 用户 | `{}` |
| `accessToken` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 登录后签发的 JWT | `"<accessToken>"` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | WVP 服务节点 ID | `"<serverId>"` |
| `loginTime` | string | `LocalDateTime` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 登录时间 | `"<loginTime>"` |

## 关联模型

- [User](User-70f883efd108.md)：`src/main/java/com/genersoft/iot/vmp/storager/dao/dto/User.java`
