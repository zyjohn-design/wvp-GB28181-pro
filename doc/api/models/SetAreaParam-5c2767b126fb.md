# SetAreaParam：设置区域参数

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/SetAreaParam.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `phoneNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 终端手机号 | `"<phoneNumber>"` |
| `circleAreaList` | array | `List<JTCircleArea>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 圆形区域项 | `[]` |
| `rectangleAreas` | array | `List<JTRectangleArea>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 矩形区域项 | `[]` |
| `polygonArea` | object | `JTPolygonArea` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 多边形区域 | `{}` |
| `route` | object | `JTRoute` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 路线 | `{}` |

## 关联模型

- [JTCircleArea](JTCircleArea-62b1cf90a614.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTCircleArea.java`
- [JTPolygonArea](JTPolygonArea-d95faa2a004f.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPolygonArea.java`
- [JTRectangleArea](JTRectangleArea-ee8f9529615d.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRectangleArea.java`
- [JTRoute](JTRoute-ec8224419149.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRoute.java`
