# JTConfirmationAlarmMessageType：人工确认报警类型

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTConfirmationAlarmMessageType.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `urgent` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认紧急报警 | `true` |
| `alarmDangerous` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认危险预警 | `true` |
| `alarmRegion` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认进出区域报警 | `true` |
| `alarmRoute` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认进出路线报警 | `true` |
| `alarmTravelTime` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认路段行驶时间不足/过长报警 | `true` |
| `alarmIllegalIgnition` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认车辆非法点火报警 | `true` |
| `alarmIllegalDisplacement` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 确认车辆非法位移报警 | `true` |
