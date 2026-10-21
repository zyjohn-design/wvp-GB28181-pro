# 全局通道前端控制（ChannelFrontEndController）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| `GET` | [/api/common/channel/front-end/ptz](../routes/get-64378b489743.md) | 云台控制 |
| `GET` | [/api/common/channel/front-end/fi/iris](../routes/get-b0899693c115.md) | 光圈控制 |
| `GET` | [/api/common/channel/front-end/fi/focus](../routes/get-2b1a26fa1199.md) | 聚焦控制 |
| `GET` | [/api/common/channel/front-end/preset/query](../routes/get-5eb226bb0a6b.md) | 查询预置位 |
| `GET` | [/api/common/channel/front-end/preset/add](../routes/get-844bb9eafd85.md) | 预置位指令-设置预置位 |
| `POST` | [/api/common/channel/front-end/preset/remark](../routes/post-fd01d90e86c7.md) | 修改平台预置位名称备注（不下发设备） |
| `GET` | [/api/common/channel/front-end/preset/call](../routes/get-bcfff3e31926.md) | 预置位指令-调用预置位 |
| `GET` | [/api/common/channel/front-end/preset/delete](../routes/get-3c85feb7b638.md) | 预置位指令-删除预置位 |
| `GET` | [/api/common/channel/front-end/tour/point/add](../routes/get-c8eb867e259f.md) | 巡航指令-加入巡航点 |
| `GET` | [/api/common/channel/front-end/tour/point/delete](../routes/get-0c7735384d26.md) | 巡航指令-删除一个巡航点 |
| `GET` | [/api/common/channel/front-end/tour/speed](../routes/get-df496bef13f2.md) | 巡航指令-设置巡航速度 |
| `GET` | [/api/common/channel/front-end/tour/time](../routes/get-2c32a3a7f8c8.md) | 巡航指令-设置巡航停留时间 |
| `GET` | [/api/common/channel/front-end/tour/start](../routes/get-2c2ba50c9738.md) | 巡航指令-开始巡航 |
| `GET` | [/api/common/channel/front-end/tour/stop](../routes/get-a9450267c12f.md) | 巡航指令-停止巡航 |
| `GET` | [/api/common/channel/front-end/scan/start](../routes/get-157f91809f4d.md) | 扫描指令-开始自动扫描 |
| `GET` | [/api/common/channel/front-end/scan/stop](../routes/get-f3d12a9e2117.md) | 扫描指令-停止自动扫描 |
| `GET` | [/api/common/channel/front-end/scan/set/left](../routes/get-fa605d757570.md) | 扫描指令-设置自动扫描左边界 |
| `GET` | [/api/common/channel/front-end/scan/set/right](../routes/get-70be8d877390.md) | 扫描指令-设置自动扫描右边界 |
| `GET` | [/api/common/channel/front-end/scan/set/speed](../routes/get-705de8573e88.md) | 扫描指令-设置自动扫描速度 |
| `GET` | [/api/common/channel/front-end/wiper](../routes/get-5e3ddafa9507.md) | 辅助开关控制指令-雨刷控制 |
| `GET` | [/api/common/channel/front-end/auxiliary](../routes/get-5885c67e536a.md) | 辅助开关控制指令 |
| `GET` | [/api/common/channel/front-end/home_position](../routes/get-5bc7b73e293e.md) | 看守位设置 |
| `GET` | [/api/common/channel/front-end/drag_zoom_in](../routes/get-3c97803821ca.md) | 拉框放大 |
| `GET` | [/api/common/channel/front-end/drag_zoom_out](../routes/get-54f7808d3283.md) | 拉框缩小 |
