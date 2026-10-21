# JTCameraTimer

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTCameraTimer.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `switchForChannel1` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道1 定时拍照开关标志 | `true` |
| `switchForChannel2` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道2 定时拍照开关标志 | `true` |
| `switchForChannel3` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道3 定时拍照开关标志 | `true` |
| `switchForChannel4` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道4 定时拍照开关标志 | `true` |
| `switchForChannel5` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道5 定时拍照开关标志 | `true` |
| `storageFlagsForChannel1` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道1 定时拍照存储标志, true: 上传， false： 存储 | `true` |
| `storageFlagsForChannel2` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道2 定时拍照存储标志 true: 上传， false： 存储 | `true` |
| `storageFlagsForChannel3` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道3 定时拍照存储标志 true: 上传， false： 存储 | `true` |
| `storageFlagsForChannel4` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道4 定时拍照存储标志 true: 上传， false： 存储 | `true` |
| `storageFlagsForChannel5` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 摄像通道5 定时拍照存储标志 true: 上传， false： 存储 | `true` |
| `timeUnit` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 定时时间单位,true: 分， false： 秒，当数值小于5s时，终端按5s处理 | `true` |
| `timeInterval` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 定时时间间隔 | `1` |
