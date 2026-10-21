# JTDevice：jt808设备

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDevice.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据库记录 ID | `1` |
| `provinceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 省域ID | `"<provinceId>"` |
| `provinceText` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 省域文字描述 | `"<provinceText>"` |
| `cityId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 市县域ID | `"<cityId>"` |
| `cityText` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 市县域文字描述 | `"<cityText>"` |
| `makerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 制造商ID | `"<makerId>"` |
| `model` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端型号 | `"<model>"` |
| `phoneNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端手机号 | `"<phoneNumber>"` |
| `terminalId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端ID | `"<terminalId>"` |
| `plateColor` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 车牌颜色 | `1` |
| `plateNo` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 车牌 | `"<plateNo>"` |
| `longitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度 | `1.0` |
| `latitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 纬度 | `1.0` |
| `registerTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 注册时间 | `"<registerTime>"` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `updateTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `status` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 状态 | `true` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | null | 设备使用的媒体id, 默认为null | `"<mediaServerId>"` |
| `geoCoordSys` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 地理坐标系， 目前支持 WGS84,GCJ02 | `"<geoCoordSys>"` |
| `sdpIp` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 收流IP | `"<sdpIp>"` |
