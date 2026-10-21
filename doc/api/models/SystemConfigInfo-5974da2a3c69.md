# SystemConfigInfo

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/SystemConfigInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `serverPort` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 服务监听端口 | `1` |
| `sip` | object | `SipConfig` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | SIP 配置信息 | `{}` |
| `addOn` | object | `UserSetting` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 附加配置信息 | `{}` |
| `version` | object | `VersionPo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 系统版本信息 | `{}` |
| `jt1078Config` | object | `JT1078Config` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | JT1078 配置信息 | `{}` |

## 关联模型

- [JT1078Config](JT1078Config-239f6b7fc02f.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/config/JT1078Config.java`
- [SipConfig](SipConfig-9bd696730e0a.md)：`src/main/java/com/genersoft/iot/vmp/conf/SipConfig.java`
- [UserSetting](UserSetting-fe2896dac17d.md)：`src/main/java/com/genersoft/iot/vmp/conf/UserSetting.java`
- [VersionPo](VersionPo-88e0ba8c1399.md)：`src/main/java/com/genersoft/iot/vmp/common/VersionPo.java`
