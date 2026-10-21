# JTAloneChanel

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTAloneChanel.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `logicChannelId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 逻辑通道号 | `1` |
| `liveStreamCodeRateType` | integer | `int` | 由具体接口决定 | 整数；100 ~ 127 | 源码未声明默认值 | 实时流编码模式 0:CBR( 固定码率) ; 1:VBR( 可变码率) ; 2:ABR( 平均码率) ; 100 ~ 127:自定义 | `1` |
| `liveStreamResolving` | integer | `int` | 由具体接口决定 | 整数；100 ~ 127 | 源码未声明默认值 | 实时流分辨率 0:QCIF; 1:CIF; 2:WCIF; 3:D1; 4:WD1; 5:720P; 6:1 080P; 100 ~ 127:自定义 | `1` |
| `liveStreamIInterval` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 实时流关键帧间隔, 范围(1 ~ 1 000) 帧 | `1` |
| `liveStreamFrameRate` | integer | `int` | 由具体接口决定 | 整数；1 ~ 120 | 源码未声明默认值 | 实时流目标帧率,范围(1 ~ 120) 帧 / s | `1` |
| `liveStreamCodeRate` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 实时流目标码率,单位为千位每秒( kbps) | `1` |
| `storageStreamCodeRateType` | integer | `int` | 由具体接口决定 | 整数；100 ~ 127 | 源码未声明默认值 | 存储流编码模式 0:CBR( 固定码率) ; 1:VBR( 可变码率) ; 2:ABR( 平均码率) ; 100 ~ 127:自定义 | `1` |
| `storageStreamResolving` | integer | `int` | 由具体接口决定 | 整数；100 ~ 127 | 源码未声明默认值 | 存储流分辨率 0:QCIF; 1:CIF; 2:WCIF; 3:D1; 4:WD1; 5:720P; 6:1 080P; 100 ~ 127:自定义 | `1` |
| `storageStreamIInterval` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 存储流关键帧间隔, 范围(1 ~ 1 000) 帧 | `1` |
| `storageStreamFrameRate` | integer | `int` | 由具体接口决定 | 整数；1 ~ 120 | 源码未声明默认值 | 存储流目标帧率,范围(1 ~ 120) 帧 / s | `1` |
| `storageStreamCodeRate` | integer | `long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 存储流目标码率,单位为千位每秒( kbps) | `1` |
| `osd` | object | `JTOSDConfig` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 字幕叠加设置 | `{}` |

## 关联模型

- [JTOSDConfig](JTOSDConfig-b60002f7e50b.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTOSDConfig.java`
