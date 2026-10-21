# Group：业务分组

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Group.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据库自增ID | `1` |
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 区域国标编号 | `"<deviceId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 区域名称 | `"<name>"` |
| `parentId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 父分组ID | `1` |
| `parentDeviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 父区域国标ID | `"<parentDeviceId>"` |
| `businessGroup` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 所属的业务分组国标编号 | `"<businessGroup>"` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `updateTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `civilCode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 行政区划 | `"<civilCode>"` |
| `alias` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 别名， 此别名为唯一值，可以对接第三方是存储对方的ID | `"<alias>"` |
