# 第三方接口（隐藏或内部接口）（需启用配置）（CameraChannelController）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| <a id="route-get-69443a3cd516"></a>`GET` | [/api/sy/camera/list](../routes/get-69443a3cd516.md) | 查询摄像机列表, 只查询当前虚拟组织下的 |
| <a id="route-get-ce8830ec3b5a"></a>`GET` | [/api/sy/camera/list-with-child](../routes/get-ce8830ec3b5a.md) | 查询摄像机列表, 查询当前虚拟组织下以及全部子节点 |
| <a id="route-get-40f2f989a16e"></a>`GET` | [/api/sy/camera/cont-with-child](../routes/get-40f2f989a16e.md) | 查询摄像机列表的总数和在线数 |
| <a id="route-get-673894745e3e"></a>`GET` | [/api/sy/camera/one](../routes/get-673894745e3e.md) | 查询单个摄像头信息 |
| <a id="route-get-023c8eeed06a"></a>`GET` | [/api/sy/camera/update](../routes/get-023c8eeed06a.md) | 更新摄像头信息 |
| <a id="route-post-cfae571072f7"></a>`POST` | [/api/sy/camera/list/ids](../routes/post-cfae571072f7.md) | 根据编号查询多个摄像头信息 |
| <a id="route-get-fbbc30527604"></a>`GET` | [/api/sy/camera/list/box](../routes/get-fbbc30527604.md) | 根据矩形查询摄像头 |
| <a id="route-post-929b7c191198"></a>`POST` | [/api/sy/camera/list/polygon](../routes/post-929b7c191198.md) | 根据多边形查询摄像头 |
| <a id="route-get-ca4bd7d02e87"></a>`GET` | [/api/sy/camera/list/circle](../routes/get-ca4bd7d02e87.md) | 根据圆范围查询摄像头 |
| <a id="route-get-295f21916ee3"></a>`GET` | [/api/sy/camera/list/address](../routes/get-295f21916ee3.md) | 根据安装地址和监视方位获取摄像头 |
| <a id="route-get-ac4774991fc7"></a>`GET` | [/api/sy/camera/control/play](../routes/get-ac4774991fc7.md) | 播放摄像头 |
| <a id="route-get-597d7e7347dc"></a>`GET` | [/api/sy/camera/control/stop](../routes/get-597d7e7347dc.md) | 停止播放摄像头 |
| <a id="route-get-2a407c8deb9d"></a>`GET` | [/api/sy/camera/control/ptz](../routes/get-2a407c8deb9d.md) | 云台控制 |
| <a id="route-get-81b1fd0509dd"></a>`GET` | [/api/sy/camera/list-for-mobile](../routes/get-81b1fd0509dd.md) | 查询移动设备摄像机列表 |
| <a id="route-get-e35c733ae1a6"></a>`GET` | [/api/sy/push/play](../routes/get-e35c733ae1a6.md) | 获取推流播放地址 |
| <a id="route-get-cc5731591048"></a>`GET` | [/api/sy/push/play-without-check](../routes/get-cc5731591048.md) | 获取推流播放地址（不做检查） |
| <a id="route-get-437877127577"></a>`GET` | [/api/sy/record/collect/add](../routes/get-437877127577.md) | 添加收藏 |
| <a id="route-get-ac1e0b83b7e0"></a>`GET` | [/api/sy/record/collect/delete](../routes/get-ac1e0b83b7e0.md) | 移除收藏 |
| <a id="route-get-ec751fa058ce"></a>`GET` | [/api/sy/record/zip](../routes/get-ec751fa058ce.md) | downloadZipFile |
| <a id="route-get-3d7596261c46"></a>`GET` | [/api/sy/record/list-url](../routes/get-3d7596261c46.md) | 分页查询云端录像 |
| <a id="route-get-ea151ac4d4f6"></a>`GET` | [/api/sy/forceClose](../routes/get-ea151ac4d4f6.md) | 强制停止推流 |
| <a id="route-get-d9cfde303e2c"></a>`GET` | [/api/sy/camera/meeting/list](../routes/get-d9cfde303e2c.md) | 查询会议设备 |
| <a id="route-get-a8add4c09667"></a>`GET` | [/api/sy/test](../routes/get-a8add4c09667.md) | test |
