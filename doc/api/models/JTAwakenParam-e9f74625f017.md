# JTAwakenParam

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTAwakenParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `wakeUpModeByCondition` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 休眠唤醒模式-条件唤醒 | `true` |
| `wakeUpModeByTime` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 休眠唤醒模式-定时唤醒 | `true` |
| `wakeUpModeByManual` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 休眠唤醒模式-手动唤醒 | `true` |
| `wakeUpConditionsByAlarm` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 唤醒条件类型-紧急报警 | `true` |
| `wakeUpConditionsByRollover` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 唤醒条件类型-碰撞侧翻报警 | `true` |
| `wakeUpConditionsByOpenTheDoor` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 唤醒条件类型-车辆开门 | `true` |
| `awakeningDayForMonday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周一 | `true` |
| `awakeningDayForTuesday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周二 | `true` |
| `awakeningDayForWednesday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周三 | `true` |
| `awakeningDayForThursday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周四 | `true` |
| `awakeningDayForFriday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周五 | `true` |
| `awakeningDayForSaturday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周六 | `true` |
| `awakeningDayForSunday` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时唤醒日设置-周日 | `true` |
| `time1Enable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 日定时唤醒-启用时间段1 | `true` |
| `time1StartTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段1开始时间 | `"<time1StartTime>"` |
| `time1EndTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段1结束时间 | `"<time1EndTime>"` |
| `time2Enable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 日定时唤醒-启用时间段2 | `true` |
| `time2StartTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段2开始时间 | `"<time2StartTime>"` |
| `time2EndTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段2结束时间 | `"<time2EndTime>"` |
| `time3Enable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 日定时唤醒-启用时间段3 | `true` |
| `time3StartTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段3开始时间 | `"<time3StartTime>"` |
| `time3EndTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段3结束时间 | `"<time3EndTime>"` |
| `time4Enable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 日定时唤醒-启用时间段4 | `true` |
| `time4StartTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段4开始时间 | `"<time4StartTime>"` |
| `time4EndTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 日定时唤醒-时间段4结束时间 | `"<time4EndTime>"` |
