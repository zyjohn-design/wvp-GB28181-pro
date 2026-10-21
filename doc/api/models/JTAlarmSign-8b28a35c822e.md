# JTAlarmSign：报警标志

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAlarmSign.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `urgent` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 紧急报警，触动报警开关后触发 | `true` |
| `alarmSpeeding` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 超速报警 | `true` |
| `alarmTired` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 疲劳驾驶报警 | `true` |
| `alarmDangerous` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 危险驾驶行为报警 | `true` |
| `alarmGnssFault` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | GNSS 模块发生故障报警 | `true` |
| `alarmGnssBreak` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | GNSS 天线未接或被剪断报警 | `true` |
| `alarmGnssShortCircuited` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | GNSS 天线短路报警 | `true` |
| `alarmUnderVoltage` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 终端主电源欠压报警 | `true` |
| `alarmPowerOff` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 终端主电源掉电报警 | `true` |
| `alarmLCD` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 终端 LCD或显示器故障报警 | `true` |
| `alarmTtsFault` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | TTS 模块故障报警 | `true` |
| `alarmCameraFault` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像头故障报警 | `true` |
| `alarmIcFault` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 道路运输证 IC卡模块故障报警 | `true` |
| `warningSpeeding` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 超速预警 | `true` |
| `warningTired` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 疲劳驾驶预警 | `true` |
| `alarmWrong` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 违规行驶报警 | `true` |
| `warningTirePressure` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 胎压预警 | `true` |
| `alarmBlindZone` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 右转盲区异常报警 | `true` |
| `alarmDrivingTimeout` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 当天累计驾驶超时报警 | `true` |
| `alarmParkingTimeout` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 超时停车报警 | `true` |
| `alarmRegion` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 进出区域报警 | `true` |
| `alarmRoute` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 进出路线报警 | `true` |
| `alarmTravelTime` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 路段行驶时间不足/过长报警 | `true` |
| `alarmRouteDeviation` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 路线偏离报警 | `true` |
| `alarmVSS` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 车辆 VSS 故障 | `true` |
| `alarmOil` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 车辆油量异常报警 | `true` |
| `alarmStolen` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 车辆被盗报警(通过车辆防盗器) | `true` |
| `alarmIllegalIgnition` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 车辆非法点火报警 | `true` |
| `alarmIllegalDisplacement` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 车辆非法位移报警 | `true` |
| `alarmRollover` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 碰撞侧翻报警 | `true` |
| `warningRollover` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 侧翻预警 | `true` |
