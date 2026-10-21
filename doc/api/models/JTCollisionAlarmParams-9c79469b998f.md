# JTCollisionAlarmParams

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTCollisionAlarmParams.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `collisionAlarmTime` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 碰撞时间 单位为毫秒(ms) | `1` |
| `collisionAcceleration` | integer | `int` | 由具体接口决定 | 整数；0~79 | 10 | 碰撞加速度 单位为0.1g,设置范围为0~79,默认为10 | `1` |
