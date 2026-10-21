# 全局通道管理（ChannelController）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| `GET` | [/api/common/channel/one](../routes/get-5750e8fcf893.md) | 查询通道信息 |
| `GET` | [/api/common/channel/industry/list](../routes/get-2319424bb7c6.md) | 获取行业编码列表 |
| `GET` | [/api/common/channel/type/list](../routes/get-3c9c8941b8f6.md) | 获取编码列表 |
| `GET` | [/api/common/channel/network/identification/list](../routes/get-17fa58d83042.md) | 获取编码列表 |
| `POST` | [/api/common/channel/update](../routes/post-bb61ebd72acd.md) | 更新通道 |
| `POST` | [/api/common/channel/reset](../routes/post-efb5a9f96daa.md) | 重置国标通道 |
| `POST` | [/api/common/channel/add](../routes/post-63cd49fd2acb.md) | 增加通道 |
| `GET` | [/api/common/channel/list](../routes/get-4a6977eb4970.md) | 获取通道列表 |
| `GET` | [/api/common/channel/civilcode/list](../routes/get-46c6e3e3b8ff.md) | 获取关联行政区划通道列表 |
| `GET` | [/api/common/channel/civilCode/unusual/list](../routes/get-be9cf236e66c.md) | 存在行政区划但无法挂载的通道列表 |
| `GET` | [/api/common/channel/parent/unusual/list](../routes/get-698bd1993a28.md) | 存在父节点编号但无法挂载的通道列表 |
| `POST` | [/api/common/channel/civilCode/unusual/clear](../routes/post-401f0d1a5b1c.md) | 清除存在行政区划但无法挂载的通道列表 |
| `POST` | [/api/common/channel/parent/unusual/clear](../routes/post-a799a0d7e6a7.md) | 清除存在分组节点但无法挂载的通道列表 |
| `GET` | [/api/common/channel/parent/list](../routes/get-8a0c75e42772.md) | 获取关联业务分组通道列表 |
| `POST` | [/api/common/channel/region/add](../routes/post-b85bf34050a1.md) | 通道设置行政区划 |
| `POST` | [/api/common/channel/region/delete](../routes/post-48b105c39179.md) | 通道删除行政区划 |
| `POST` | [/api/common/channel/region/device/add](../routes/post-034495b69d2d.md) | 通道设置行政区划-根据国标设备 |
| `POST` | [/api/common/channel/region/device/delete](../routes/post-f9b8df51a156.md) | 通道删除行政区划-根据国标设备 |
| `POST` | [/api/common/channel/group/add](../routes/post-5047605b8cc5.md) | 通道设置业务分组 |
| `POST` | [/api/common/channel/group/delete](../routes/post-1cb4ed774444.md) | 通道删除业务分组 |
| `POST` | [/api/common/channel/group/device/add](../routes/post-6cb370ec04e0.md) | 通道设置业务分组-根据国标设备 |
| `POST` | [/api/common/channel/group/device/delete](../routes/post-287d20142bf2.md) | 通道删除业务分组-根据国标设备 |
| `GET` | [/api/common/channel/play](../routes/get-5d1a5ef7abf5.md) | 播放通道 |
| `GET` | [/api/common/channel/play/stop](../routes/get-0b07595beddb.md) | 停止播放通道 |
| `GET` | [/api/common/channel/talk/start](../routes/get-fea0a5a909cb.md) | 开始对讲 |
| `GET` | [/api/common/channel/talk/stop](../routes/get-801066a8da2d.md) | 停止对讲 |
| `GET` | [/api/common/channel/broadcast/start](../routes/get-418dd1abc5a0.md) | 开始喊话 |
| `GET` | [/api/common/channel/broadcast/stop](../routes/get-43f123495a77.md) | 停止喊话 |
| `GET` | [/api/common/channel/playback/query](../routes/get-d1ca0a94d4d2.md) | 录像查询 |
| `GET` | [/api/common/channel/playback](../routes/get-2da835f1478b.md) | 录像回放 |
| `GET` | [/api/common/channel/playback/stop](../routes/get-e0275a5b3a33.md) | 停止录像回放 |
| `GET` | [/api/common/channel/playback/pause](../routes/get-2e4252dba881.md) | 暂停录像回放 |
| `GET` | [/api/common/channel/playback/resume](../routes/get-3e80293aec9d.md) | 恢复录像回放 |
| `GET` | [/api/common/channel/playback/seek](../routes/get-a575e8a63d3e.md) | 拖动录像回放 |
| `GET` | [/api/common/channel/playback/speed](../routes/get-288325fdf409.md) | 拖动录像回放 |
| `GET` | [/api/common/channel/map/list](../routes/get-c60a65078b58.md) | 为地图获取通道列表 |
| `POST` | [/api/common/channel/map/reset-level](../routes/post-0d762c69b2e3.md) | 为地图去除抽稀结果 |
| `POST` | [/api/common/channel/map/thin/draw](../routes/post-737104042c11.md) | 执行抽稀 |
| `GET` | [/api/common/channel/map/thin/clear](../routes/get-967061db683e.md) | 清除未保存的抽稀结果 |
| `GET` | [/api/common/channel/map/thin/save](../routes/get-0f4f54ac9123.md) | 保存的抽稀结果 |
| `GET` | [/api/common/channel/map/thin/progress](../routes/get-4e9d8f7fee77.md) | 获取抽稀执行的进度 |
| `GET` | [/api/common/channel/map/tile/{z}/{x}/{y}](../routes/get-0b4b68ada868.md) | 为地图提供标准mvt图层 |
| `GET` | [/api/common/channel/map/thin/tile/{z}/{x}/{y}](../routes/get-21826bd87cb7.md) | 为地图提供经过抽稀的标准mvt图层 |
