# GbStream：直播流关联国标上级平台

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/GbStream.java`；继承 `PlatformGbStream`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `gbStreamId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | ID | `1` |
| `platformId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 平台ID | `"<platformId>"` |
| `catalogId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 目录ID | `"<catalogId>"` |
| `app` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 应用名 | `"<app>"` |
| `stream` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流ID | `"<stream>"` |
| `gbId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标ID | `"<gbId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `mediaServerId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流媒体ID | `"<mediaServerId>"` |
| `longitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度 | `1.0` |
| `latitude` | number | `double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 纬度 | `1.0` |
| `streamType` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流类型（拉流/推流） | `"<streamType>"` |
| `status` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 状态 | `true` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |

## 关联模型

- [PlatformGbStream](PlatformGbStream-f85cdad584a1.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/PlatformGbStream.java`
