# JTPositionAdditionalInfo：位置附加信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPositionAdditionalInfo.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `mileage` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 里程, 单位为1/10km, 对应车上里程表读数 | `1` |
| `oil` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 油量, 单位为1/10L, 对应车上油量表读数 | `1` |
| `speed` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 行驶记录功能获取的速度,单位为1/10km/h | `1` |
| `alarmId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 报警事件的 ID | `1` |
| `carriageTemperature` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 车厢温度 ,单位为摄氏度 | `1` |
