# 前端设备控制（PtzController）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| `GET` | [/api/front-end/common/{deviceId}/{channelId}](../routes/get-3194e8cd47e5.md) | 通用前端控制命令(参考国标文档A.3.1指令格式) |
| `GET` | [/api/front-end/ptz/{deviceId}/{channelId}](../routes/get-d9a98429a477.md) | 云台控制 |
| `GET` | [/api/front-end/fi/iris/{deviceId}/{channelId}](../routes/get-98367c9e294b.md) | 光圈控制 |
| `GET` | [/api/front-end/fi/focus/{deviceId}/{channelId}](../routes/get-5e9267054d4c.md) | 聚焦控制 |
| `GET` | [/api/front-end/preset/query/{deviceId}/{channelId}](../routes/get-8292b5288e78.md) | 查询预置位 |
| `GET` | [/api/front-end/preset/add/{deviceId}/{channelId}](../routes/get-507d4e453fab.md) | 预置位指令-设置预置位 |
| `POST` | [/api/front-end/preset/remark/{deviceId}/{channelId}](../routes/post-e1542d744ac0.md) | 修改平台预置位名称备注（不下发设备） |
| `GET` | [/api/front-end/preset/call/{deviceId}/{channelId}](../routes/get-f8bd3b75e784.md) | 预置位指令-调用预置位 |
| `GET` | [/api/front-end/preset/delete/{deviceId}/{channelId}](../routes/get-6fc276ea22c4.md) | 预置位指令-删除预置位 |
| `GET` | [/api/front-end/cruise/point/add/{deviceId}/{channelId}](../routes/get-96d2ab831609.md) | 巡航指令-加入巡航点 |
| `GET` | [/api/front-end/cruise/point/delete/{deviceId}/{channelId}](../routes/get-cd39c8b01b8e.md) | 巡航指令-删除一个巡航点 |
| `GET` | [/api/front-end/cruise/speed/{deviceId}/{channelId}](../routes/get-5d99b348b15b.md) | 巡航指令-设置巡航速度 |
| `GET` | [/api/front-end/cruise/time/{deviceId}/{channelId}](../routes/get-9335f24f9c42.md) | 巡航指令-设置巡航停留时间 |
| `GET` | [/api/front-end/cruise/start/{deviceId}/{channelId}](../routes/get-e1e9dcf77fba.md) | 巡航指令-开始巡航 |
| `GET` | [/api/front-end/cruise/stop/{deviceId}/{channelId}](../routes/get-316017b2f8d6.md) | 巡航指令-停止巡航 |
| `GET` | [/api/front-end/scan/start/{deviceId}/{channelId}](../routes/get-fc2923e6fa1b.md) | 扫描指令-开始自动扫描 |
| `GET` | [/api/front-end/scan/stop/{deviceId}/{channelId}](../routes/get-c6f1fec7dab7.md) | 扫描指令-停止自动扫描 |
| `GET` | [/api/front-end/scan/set/left/{deviceId}/{channelId}](../routes/get-aec765a85a96.md) | 扫描指令-设置自动扫描左边界 |
| `GET` | [/api/front-end/scan/set/right/{deviceId}/{channelId}](../routes/get-459c12cec5e8.md) | 扫描指令-设置自动扫描右边界 |
| `GET` | [/api/front-end/scan/set/speed/{deviceId}/{channelId}](../routes/get-7a0cdf7090fb.md) | 扫描指令-设置自动扫描速度 |
| `GET` | [/api/front-end/wiper/{deviceId}/{channelId}](../routes/get-0cfe3601c1b2.md) | 辅助开关控制指令-雨刷控制 |
| `GET` | [/api/front-end/auxiliary/{deviceId}/{channelId}](../routes/get-8863657d70cf.md) | 辅助开关控制指令 |
