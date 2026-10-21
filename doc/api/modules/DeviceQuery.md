# 国标设备查询（DeviceQuery）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| <a id="route-get-35cbfb9577f1"></a>`GET` | [/api/device/query/register/attempts](../routes/get-35cbfb9577f1.md) | 查询待处理的下级设备注册请求 |
| <a id="route-delete-7adc464bca7a"></a>`DELETE` | [/api/device/query/register/attempts/{deviceId}](../routes/delete-7adc464bca7a.md) | 忽略待处理的下级设备注册请求 |
| <a id="route-get-54be476af1d1"></a>`GET` | [/api/device/query/devices/{deviceId}](../routes/get-54be476af1d1.md) | 查询国标设备 |
| <a id="route-get-abf3746720c9"></a>`GET` | [/api/device/query/device/{deviceId}](../routes/get-abf3746720c9.md) | 查询国标设备 |
| <a id="route-get-08dbae62822e"></a>`GET` | [/api/device/query/devices](../routes/get-08dbae62822e.md) | 分页查询国标设备 |
| <a id="route-get-336429f64fa1"></a>`GET` | [/api/device/query/list](../routes/get-336429f64fa1.md) | 分页查询国标设备 |
| <a id="route-get-fdbbcaae0e03"></a>`GET` | [/api/device/query/devices/{deviceId}/channels](../routes/get-fdbbcaae0e03.md) | 分页查询通道 |
| <a id="route-get-9ff7ff7c0e7e"></a>`GET` | [/api/device/query/device/{deviceId}/channels](../routes/get-9ff7ff7c0e7e.md) | 分页查询通道 |
| <a id="route-get-3452aa830660"></a>`GET` | [/api/device/query/streams](../routes/get-3452aa830660.md) | 分页查询存在流的通道 |
| <a id="route-get-38cf02b48393"></a>`GET` | [/api/device/query/devices/{deviceId}/sync](../routes/get-38cf02b48393.md) | 同步设备通道 |
| <a id="route-get-0ae6191ef318"></a>`GET` | [/api/device/query/device/{deviceId}/sync](../routes/get-0ae6191ef318.md) | 同步设备通道 |
| <a id="route-delete-b918d588f28c"></a>`DELETE` | [/api/device/query/devices/{deviceId}/delete](../routes/delete-b918d588f28c.md) | 移除设备 |
| <a id="route-delete-5f2c878139ba"></a>`DELETE` | [/api/device/query/device/{deviceId}/delete](../routes/delete-5f2c878139ba.md) | 移除设备 |
| <a id="route-get-6a98a4fe432e"></a>`GET` | [/api/device/query/sub_channels/{deviceId}/{channelId}/channels](../routes/get-6a98a4fe432e.md) | 分页查询子目录通道 |
| <a id="route-post-a99074ae6265"></a>`POST` | [/api/device/query/channel/audio](../routes/post-a99074ae6265.md) | 开启/关闭通道的音频 |
| <a id="route-post-876226f19438"></a>`POST` | [/api/device/query/channel/stream/identification/update](../routes/post-876226f19438.md) | 修改通道的码流类型 |
| <a id="route-get-5036400fee9a"></a>`GET` | [/api/device/query/channel/one](../routes/get-5036400fee9a.md) | 获取单个通道详情 |
| <a id="route-post-28620f7c30ae"></a>`POST` | [/api/device/query/transport/{deviceId}/{streamMode}](../routes/post-28620f7c30ae.md) | 修改数据流传输模式 |
| <a id="route-post-73b12aec4ea1"></a>`POST` | [/api/device/query/device/add](../routes/post-73b12aec4ea1.md) | 添加设备信息 |
| <a id="route-post-1b8304332a79"></a>`POST` | [/api/device/query/device/update](../routes/post-1b8304332a79.md) | 更新设备信息 |
| <a id="route-get-dc0ed0f67070"></a>`GET` | [/api/device/query/devices/{deviceId}/status](../routes/get-dc0ed0f67070.md) | 设备状态查询 |
| <a id="route-get-b665b64c257e"></a>`GET` | [/api/device/query/device/{deviceId}/status](../routes/get-b665b64c257e.md) | 设备状态查询 |
| <a id="route-get-dc30b57d7b13"></a>`GET` | [/api/device/query/alarm](../routes/get-dc30b57d7b13.md) | 设备报警查询 |
| <a id="route-get-6430aeda8385"></a>`GET` | [/api/device/query/info](../routes/get-6430aeda8385.md) | 设备信息查询 |
| <a id="route-get-22d1dc7eddfa"></a>`GET` | [/api/device/query/{deviceId}/sync_status](../routes/get-22d1dc7eddfa.md) | 获取通道同步进度（此接口保留仅作为兼容，后续将移除，请迁移至 /sync_status?deviceId=） |
| <a id="route-get-2a1b1e3201ac"></a>`GET` | [/api/device/query/sync_status](../routes/get-2a1b1e3201ac.md) | 获取通道同步进度 |
| <a id="route-get-900c577f6392"></a>`GET` | [/api/device/query/snap/{deviceId}/{channelId}](../routes/get-900c577f6392.md) | 请求截图 |
| <a id="route-get-c79dc8b7d187"></a>`GET` | [/api/device/query/channel/raw](../routes/get-c79dc8b7d187.md) | 国标通道编辑时的数据回显 |
| <a id="route-get-b99a633d5ac4"></a>`GET` | [/api/device/query/subscribe/catalog](../routes/get-b99a633d5ac4.md) | 开启/关闭目录订阅 |
| <a id="route-get-a2467d52ebdf"></a>`GET` | [/api/device/query/subscribe/mobile-position](../routes/get-a2467d52ebdf.md) | 开启/关闭移动位置订阅 |
| <a id="route-get-4c54f379c03f"></a>`GET` | [/api/device/query/statistics/keepalive](../routes/get-4c54f379c03f.md) | 请求心跳统计 |
| <a id="route-get-f9dee37d1783"></a>`GET` | [/api/device/query/statistics/register](../routes/get-f9dee37d1783.md) | 请求注册统计 |
| <a id="route-get-fdcdee0dff3d"></a>`GET` | [/api/device/query/subscribe/alarm](../routes/get-fdcdee0dff3d.md) | 开启/关闭报警订阅 |
