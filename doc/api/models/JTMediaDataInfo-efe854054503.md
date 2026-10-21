# JTMediaDataInfo：多媒体检索项数据

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTMediaDataInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 多媒体数据 ID | `1` |
| `type` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 多媒体类型, 0：图像；1：音频；2：视频 | `1` |
| `eventCode` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 事件项编码: 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰 撞侧翻报警触发；4：门开拍照；5：门关拍照；6：车门由开 变关 ,车速从小于20km到超过20km；7：定距拍照 | `1` |
| `channelId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通道 ID | `1` |
| `positionBaseInfo` | object | `JTPositionBaseInfo` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 表示拍摄或录制的起始时刻的汇报消息 | `{}` |

## 关联模型

- [JTPositionBaseInfo](JTPositionBaseInfo-b540b0bb5da6.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPositionBaseInfo.java`
