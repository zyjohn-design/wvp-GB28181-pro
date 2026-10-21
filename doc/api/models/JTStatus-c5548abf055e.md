# JTStatus：状态信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTStatus.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `acc` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:ACC关；true: ACC开 | `true` |
| `positioning` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:未定位；true: 定位 | `true` |
| `southLatitude` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:北纬；true: 南纬 | `true` |
| `wesLongitude` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:东经；true: 西经 | `true` |
| `outage` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:运营状态；true: 停运状态 | `true` |
| `positionEncryption` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:经纬度未经保密插件加密；true: 经纬度已经保密插件加密 | `true` |
| `warningFrontCrash` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | true: 紧急刹车系统采集的前撞预警 | `true` |
| `warningShifting` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | true: 车道偏移预警 | `true` |
| `load` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 00:空车；01:半载；10:保留；11:满载。可表示客车的空载状态 ,重车及货车的空载、满载状态 ,该状态可由人工输入或传感器获取 | `1` |
| `oilWayBreak` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:车辆油路正常；true: 车辆油路断开 | `true` |
| `circuitBreak` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:车辆电路正常；true: 车辆电路断开 | `true` |
| `doorLocking` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:车门解锁；true: 车门加锁 | `true` |
| `door1Open` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:门1 关；true: 门1 开(前门) | `true` |
| `door2Open` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:门2 关；true: 门2 开(中门) | `true` |
| `door3Open` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:门3 关；true: 门3 开(后门) | `true` |
| `door4Open` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:门4 关；true: 门4 开(驾驶席门) | `true` |
| `door5Open` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:门5 关；true: 门5 开(自定义) | `true` |
| `gps` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:未使用 GPS 卫星进行定位；true:使用 GPS 卫星进行定位 | `true` |
| `beidou` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:未使用北斗卫星进行定位；true:使用北斗卫星进行定位 | `true` |
| `glonass` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:未使用GLONASS 卫星进行定位；true:使用GLONASS 卫星进行定位 | `true` |
| `gaLiLeo` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:未使用GaLiLeo 卫星进行定位；true:使用GaLiLeo 卫星进行定位 | `true` |
| `driving` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | false:车辆处于停止状态；true:车辆处于行驶状态 | `true` |
