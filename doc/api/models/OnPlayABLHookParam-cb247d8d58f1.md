# OnPlayABLHookParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnPlayABLHookParam.java`；继承 `ABLHookParam`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体节点 ID | `"<mediaServerId>"` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流id | `"<stream>"` |
| `key` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 媒体流来源编号，可以根据这个key进行关闭流媒体 可以调用delMediaStream或close_streams 函数进行关闭 | `"<key>"` |
| `networkType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 媒体流来源网络编号，可参考附表 | `1` |
| `ip` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | IP 地址 | `"<ip>"` |
| `port` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 网络端口 | `1` |
| `params` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 附加参数 | `"<params>"` |

## 关联模型

- [ABLHookParam](ABLHookParam-80b2341fc82a.md)：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/ABLHookParam.java`
