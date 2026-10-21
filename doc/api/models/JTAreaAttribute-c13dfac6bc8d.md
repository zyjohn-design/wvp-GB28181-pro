# JTAreaAttribute：区域属性

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAreaAttribute.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `ruleForTimeLimit` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用起始时间与结束时间的判断规则 ,false：否；true：是 | `true` |
| `ruleForSpeedLimit` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否启用最高速度、超速持续时间和夜间最高速度的判断规则 ,false：否；true：是 | `true` |
| `ruleForAlarmToDriverWhenEnter` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 进区域是否报警给驾驶员,false：否；true：是 | `true` |
| `ruleForAlarmToPlatformWhenEnter` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 进区域是否报警给平台 ,false：否；true：是 | `true` |
| `ruleForAlarmToDriverWhenExit` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 出区域是否报警给驾驶员,false：否；true：是 | `true` |
| `ruleForAlarmToPlatformWhenExit` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 出区域是否报警给平台 ,false：否；true：是 | `true` |
| `southLatitude` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false：北纬；true：南纬 | `true` |
| `westLongitude` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false：东经；true：西经 | `true` |
| `prohibitOpeningDoors` | boolean | `boolean` | 由具体接口决定 | true / false；false：允许开门 | 源码未声明默认值 | false：允许开门；true：禁止开门 | `true` |
| `ruleForTurnOffCommunicationWhenEnter` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false：进区域开启通信模块；true：进区域关闭通信模块 | `true` |
| `ruleForGnssWhenEnter` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false：进区域不采集 GNSS 详细定位数据；true：进区域采集 GNSS 详细定位数据 | `true` |
