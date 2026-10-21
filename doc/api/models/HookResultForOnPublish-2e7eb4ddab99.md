# HookResultForOnPublish

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/HookResultForOnPublish.java`；继承 `HookResult`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `code` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 结果或业务状态码 | `1` |
| `msg` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 结果或错误信息 | `"<msg>"` |
| `enable_audio` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用音频 | `true` |
| `enable_mp4` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用 MP4 录制 | `true` |
| `mp4_max_second` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 单段 MP4 最大时长，秒 | `1` |
| `mp4_save_path` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | MP4 保存路径 | `"<mp4_save_path>"` |
| `stream_replace` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 替换后的流标识 | `"<stream_replace>"` |
| `modify_stamp` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 是否修改时间戳 | `1` |

## 关联模型

- [HookResult](HookResult-5492300b5601.md)：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/HookResult.java`
