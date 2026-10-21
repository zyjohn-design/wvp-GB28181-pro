# JTChannelListParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTChannelListParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `videoAndAudioCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音视频通道总数 | `1` |
| `audioCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 音频通道总数 | `1` |
| `videoCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 视频通道总数 | `1` |
| `chanelList` | array | `List<JTChanelConfig>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 源码未标注字段含义 | `[]` |

## 关联模型

- [JTChanelConfig](JTChanelConfig-026b323f84c8.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTChanelConfig.java`
