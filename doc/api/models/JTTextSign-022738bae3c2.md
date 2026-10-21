# JTTextSign：文本信息标志

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTTextSign.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `type` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 1紧急,2服务,3通知 | `1` |
| `terminalDisplay` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 1终端显示器显示 | `true` |
| `adScreen` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 1广告屏显示 | `true` |
| `tts` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 1终端 TTS 播读 | `true` |
| `source` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false: 中心导航信息 true CAN故障码信息 | `true` |
