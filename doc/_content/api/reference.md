# 全项目 HTTP 接口索引

> 自动生成的历史索引，用于核查源码覆盖面。完整的逐接口说明请使用[系统完整接口文档](../../第三方对接接口手册.md)。

本索引由 `scripts/generate-api-reference.py` 从当前 Java Controller 与 OpenAPI 注解生成。路径、方法、参数类型、必填标记、描述和返回类型可在源码中追溯。没有注解的参数保留‘未声明’，避免猜测。`ANY` 表示源码的 `@RequestMapping` 未限定 HTTP 方法。运行实例的 `/v3/api-docs` 可用于查看实际启用的接口。标为‘隐藏或内部接口’的路由通常供流媒体回调、兼容层或内部系统使用；标为‘需启用配置’的路由只有相应配置开启时才存在。

请求参数位置 `query/form`：GET 通常放查询串，POST 且未使用 `@RequestBody` 时通常以表单或查询参数提交。`JSON body` 的字段见[模型字段索引](models.md)。接口鉴权依运行配置，默认规则见[对接接口详解](integration.md)。响应一般包装为 `WVPResult`，流、文件和 `/api/v1/**` 等例外按各接口实际返回。

## 全局通道管理（ChannelController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/ChannelController.java`

### `GET /api/common/channel/one`

查询通道信息。源码返回类型：`CommonGBChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 通道的数据库自增Id |

### `GET /api/common/channel/industry/list`

获取行业编码列表。源码返回类型：`List<IndustryCodeType>`。

无业务请求参数。

### `GET /api/common/channel/type/list`

获取编码列表。源码返回类型：`List<DeviceType>`。

无业务请求参数。

### `GET /api/common/channel/network/identification/list`

获取编码列表。源码返回类型：`List<NetworkIdentificationType>`。

无业务请求参数。

### `POST /api/common/channel/update`

更新通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channel` | `CommonGBChannel` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/reset`

重置国标通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ResetParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/add`

增加通道。源码返回类型：`CommonGBChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channel` | `CommonGBChannel` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/common/channel/list`

获取通道列表。源码返回类型：`PageInfo<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `hasRecordPlan` | `Boolean` | query/form | 否 | 是否已设置录制计划 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |
| `civilCode` | `String` | query/form | 否 | 行政区划 |
| `parentDeviceId` | `String` | query/form | 否 | 父节点编码 |

### `GET /api/common/channel/civilcode/list`

获取关联行政区划通道列表。源码返回类型：`PageInfo<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |
| `civilCode` | `String` | query/form | 否 | 行政区划 |

### `GET /api/common/channel/civilCode/unusual/list`

存在行政区划但无法挂载的通道列表。源码返回类型：`PageInfo<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |

### `GET /api/common/channel/parent/unusual/list`

存在父节点编号但无法挂载的通道列表。源码返回类型：`PageInfo<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |

### `POST /api/common/channel/civilCode/unusual/clear`

清除存在行政区划但无法挂载的通道列表。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToRegionParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/parent/unusual/clear`

清除存在分组节点但无法挂载的通道列表。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToRegionParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/common/channel/parent/list`

获取关联业务分组通道列表。源码返回类型：`PageInfo<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |
| `groupDeviceId` | `String` | query/form | 否 | 业务分组下的父节点ID |

### `POST /api/common/channel/region/add`

通道设置行政区划。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToRegionParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/region/delete`

通道删除行政区划。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToRegionParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/region/device/add`

通道设置行政区划-根据国标设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToRegionByGbDeviceParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/region/device/delete`

通道删除行政区划-根据国标设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToRegionByGbDeviceParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/group/add`

通道设置业务分组。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToGroupParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/group/delete`

通道删除业务分组。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToGroupParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/group/device/add`

通道设置业务分组-根据国标设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToGroupByGbDeviceParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/common/channel/group/device/delete`

通道删除业务分组-根据国标设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ChannelToGroupByGbDeviceParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/common/channel/play`

播放通道。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |

### `GET /api/common/channel/play/stop`

停止播放通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |

### `GET /api/common/channel/talk/start`

开始对讲。源码返回类型：`AudioTalkResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 未声明 | 通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID |

### `GET /api/common/channel/talk/stop`

停止对讲。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 未声明 | 通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID |

### `GET /api/common/channel/broadcast/start`

开始喊话。源码返回类型：`AudioTalkResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 未声明 | 通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID |

### `GET /api/common/channel/broadcast/stop`

停止喊话。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 未声明 | 通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID |

### `GET /api/common/channel/playback/query`

录像查询。源码返回类型：`DeferredResult<WVPResult<List<CommonRecordInfo>>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `startTime` | `String` | query/form | 是 | 开始时间 |
| `endTime` | `String` | query/form | 是 | 结束时间 |

### `GET /api/common/channel/playback`

录像回放。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `startTime` | `String` | query/form | 是 | 开始时间 |
| `endTime` | `String` | query/form | 是 | 结束时间 |

### `GET /api/common/channel/playback/stop`

停止录像回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `stream` | `String` | query/form | 是 | 流ID |

### `GET /api/common/channel/playback/pause`

暂停录像回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `stream` | `String` | query/form | 是 | 流ID |

### `GET /api/common/channel/playback/resume`

恢复录像回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `stream` | `String` | query/form | 是 | 流ID |

### `GET /api/common/channel/playback/seek`

拖动录像回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `stream` | `String` | query/form | 是 | 流ID |
| `seekTime` | `Long` | query/form | 是 | 将要播放的时间 |

### `GET /api/common/channel/playback/speed`

拖动录像回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道ID |
| `stream` | `String` | query/form | 是 | 流ID |
| `speed` | `Double` | query/form | 是 | 倍速 |

### `GET /api/common/channel/map/list`

为地图获取通道列表。源码返回类型：`List<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `hasRecordPlan` | `Boolean` | query/form | 否 | 是否已设置录制计划 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |

### `POST /api/common/channel/map/reset-level`

为地图去除抽稀结果。源码返回类型：`void`。

无业务请求参数。

### `POST /api/common/channel/map/thin/draw`

执行抽稀。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `DrawThinParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/common/channel/map/thin/clear`

清除未保存的抽稀结果。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `String` | query/form | 是 | 抽稀ID |

### `GET /api/common/channel/map/thin/save`

保存的抽稀结果。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `String` | query/form | 是 | 抽稀ID |

### `GET /api/common/channel/map/thin/progress`

获取抽稀执行的进度。源码返回类型：`DrawThinProcess`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `String` | query/form | 是 | 抽稀ID |

### `GET /api/common/channel/map/tile/{z}/{x}/{y}`

为地图提供标准mvt图层。源码返回类型：`ResponseEntity<byte[]>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `z` | `int` | path | 是 | 地图瓦片缩放层级 |
| `x` | `int` | path | 是 | 地图瓦片横向坐标 |
| `y` | `int` | path | 是 | 地图瓦片纵向坐标 |
| `geoCoordSys` | `String` | query/form | 未声明 | 地理坐标系， WGS84/GCJ02 |

### `GET /api/common/channel/map/thin/tile/{z}/{x}/{y}`

为地图提供经过抽稀的标准mvt图层。源码返回类型：`ResponseEntity<byte[]>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `z` | `int` | path | 是 | 地图瓦片缩放层级 |
| `x` | `int` | path | 是 | 地图瓦片横向坐标 |
| `y` | `int` | path | 是 | 地图瓦片纵向坐标 |
| `geoCoordSys` | `String` | query/form | 未声明 | 地理坐标系， WGS84/GCJ02 |
| `thinId` | `String` | query/form | 否 | 抽稀结果ID |

## 全局通道前端控制（ChannelFrontEndController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/ChannelFrontEndController.java`

### `GET /api/common/channel/front-end/ptz`

云台控制。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `command` | `String` | query/form | 是 | 控制指令,允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop |
| `panSpeed` | `Integer` | query/form | 未声明 | 水平速度(0-100)，不传默认50 |
| `tiltSpeed` | `Integer` | query/form | 未声明 | 垂直速度(0-100)，不传默认50 |
| `zoomSpeed` | `Integer` | query/form | 未声明 | 缩放速度(0-100)，不传默认50 |

### `GET /api/common/channel/front-end/fi/iris`

光圈控制。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `command` | `String` | query/form | 是 | 控制指令,允许值: in, out, stop |
| `speed` | `Integer` | query/form | 未声明 | 光圈速度(0-100)，不传默认50 |

### `GET /api/common/channel/front-end/fi/focus`

聚焦控制。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `command` | `String` | query/form | 是 | 控制指令,允许值: near, far, stop |
| `speed` | `Integer` | query/form | 未声明 | 聚焦速度(0-100)，不传默认50 |

### `GET /api/common/channel/front-end/preset/query`

查询预置位。源码返回类型：`DeferredResult<WVPResult<List<Preset>>>`。`data[].presetId` 为设备原样返回的字符串，可能无法转换成控制接口所需的整数。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |

### `GET /api/common/channel/front-end/preset/add`

预置位指令-设置预置位。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `presetId` | `String` | query/form | 是 | 按十进制整数校验，范围1-255 |
| `presetName` | `String` | query/form | 是 | 预置位名称 |

### `GET /api/common/channel/front-end/preset/call`

预置位指令-调用预置位。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `presetId` | `String` | query/form | 是 | 按十进制整数校验，范围1-255 |

### `GET /api/common/channel/front-end/preset/delete`

预置位指令-删除预置位。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `presetId` | `String` | query/form | 是 | 按十进制整数校验，范围1-255 |

### `GET /api/common/channel/front-end/tour/point/add`

巡航指令-加入巡航点。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `tourId` | `Integer` | query/form | 是 | 巡航组号 |
| `presetId` | `Integer` | query/form | 是 | 预置位编号 |

### `GET /api/common/channel/front-end/tour/point/delete`

巡航指令-删除一个巡航点。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `tourId` | `Integer` | query/form | 是 | 巡航组号(1-100) |
| `presetId` | `Integer` | query/form | 是 | 预置位编号(0-100, 为0时删除整个巡航) |

### `GET /api/common/channel/front-end/tour/speed`

巡航指令-设置巡航速度。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `tourId` | `Integer` | query/form | 是 | 巡航组号(0-100) |
| `speed` | `Integer` | query/form | 是 | 巡航速度(1-4095) |
| `presetId` | `Integer` | query/form | 是 | 预置位编号 |

### `GET /api/common/channel/front-end/tour/time`

巡航指令-设置巡航停留时间。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `tourId` | `Integer` | query/form | 是 | 巡航组号 |
| `time` | `Integer` | query/form | 是 | 巡航停留时间(1-4095) |
| `presetId` | `Integer` | query/form | 是 | 预置位编号 |

### `GET /api/common/channel/front-end/tour/start`

巡航指令-开始巡航。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `tourId` | `Integer` | query/form | 是 | 巡航组号) |

### `GET /api/common/channel/front-end/tour/stop`

巡航指令-停止巡航。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `tourId` | `Integer` | query/form | 是 | 巡航组号 |

### `GET /api/common/channel/front-end/scan/start`

扫描指令-开始自动扫描。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-100) |

### `GET /api/common/channel/front-end/scan/stop`

扫描指令-停止自动扫描。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-100) |

### `GET /api/common/channel/front-end/scan/set/left`

扫描指令-设置自动扫描左边界。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-100) |

### `GET /api/common/channel/front-end/scan/set/right`

扫描指令-设置自动扫描右边界。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-100) |

### `GET /api/common/channel/front-end/scan/set/speed`

扫描指令-设置自动扫描速度。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-100) |
| `speed` | `Integer` | query/form | 是 | 自动扫描速度(1-4095) |

### `GET /api/common/channel/front-end/wiper`

辅助开关控制指令-雨刷控制。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `command` | `String` | query/form | 是 | 控制指令,允许值: on, off |

### `GET /api/common/channel/front-end/auxiliary`

辅助开关控制指令。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `command` | `String` | query/form | 是 | 控制指令,允许值: on, off |
| `auxiliaryId` | `Integer` | query/form | 是 | 开关编号 |

### `GET /api/common/channel/front-end/home_position`

看守位设置。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `enabled` | `Boolean` | query/form | 是 | 是否开启看守位 |
| `resetTime` | `Integer` | query/form | 否 | 自动归位时间间隔（秒） |
| `presetIndex` | `Integer` | query/form | 否 | 调用预置位编号 |

### `GET /api/common/channel/front-end/drag_zoom_in`

拉框放大。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `length` | `int` | query/form | 是 | 播放窗口长度像素值 |
| `width` | `int` | query/form | 是 | 播放窗口宽度像素值 |
| `midPointX` | `int` | query/form | 是 | 拉框中心的横轴坐标像素值 |
| `midPointY` | `int` | query/form | 是 | 拉框中心的纵轴坐标像素值 |
| `lengthX` | `int` | query/form | 是 | 拉框长度像素值 |
| `lengthY` | `int` | query/form | 是 | 拉框宽度像素值 |

### `GET /api/common/channel/front-end/drag_zoom_out`

拉框缩小。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通用通道数据库整数ID（CommonGBChannel.gbId） |
| `length` | `Integer` | query/form | 是 | 播放窗口长度像素值 |
| `width` | `Integer` | query/form | 是 | 播放窗口宽度像素值 |
| `midPointX` | `Integer` | query/form | 是 | 拉框中心的横轴坐标像素值 |
| `midPointY` | `Integer` | query/form | 是 | 拉框中心的纵轴坐标像素值 |
| `lengthX` | `Integer` | query/form | 是 | 拉框长度像素值 |
| `lengthY` | `Integer` | query/form | 是 | 拉框宽度像素值 |

## 国标设备配置（DeviceConfig）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/DeviceConfig.java`

### `GET /api/device/config/set/basicParam`

设置基本配置。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `basicParam` | `BasicParam` | query/form | 是 | 基础配置参数 |

### `GET /api/device/config/set/videoParamOpt`

设置视频参数。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `videoParamOpt` | `VideoParamOpt` | query/form | 是 | 视频参数 |

### `GET /api/device/config/query/basicParam`

查询基本参数配置。源码返回类型：`DeferredResult<WVPResult<BasicParam>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 否 | 通道国标编号 |

### `GET /api/device/config/query/videoParamOpt`

查询视频参数范围。源码返回类型：`DeferredResult<WVPResult<VideoParamOpt>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 否 | 通道国标编号 |

### `GET /api/device/config/query/svacEncodeConfig`

查询SVAC编码配置。源码返回类型：`DeferredResult<WVPResult<SVACEncodeConfig>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 否 | 通道国标编号 |

### `GET /api/device/config/query/svacDecodeConfig`

查询SVAC解码配置。源码返回类型：`DeferredResult<WVPResult<SVACDecodeConfig>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 否 | 通道国标编号 |

## 国标设备控制（DeviceControl）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/DeviceControl.java`

### `GET /api/device/control/teleboot/{deviceId}`

远程启动。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/control/record`

录像控制。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `recordCmdStr` | `String` | query/form | 是 | 命令， 可选值：Record（手动录像），StopRecord（停止手动录像） |
| `channelId` | `String` | query/form | 是 | 通道国标编号 |

### `GET /api/device/control/guard`

布防/撤防。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `guardCmd` | `String` | query/form | 是 | 命令， 可选值：SetGuard（布防），ResetGuard（撤防） |

### `GET /api/device/control/reset_alarm`

报警复位。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 是 | 通道国标编号 |
| `alarmMethod` | `String` | query/form | 否 | 报警方式, 报警方式条件(可选),取值0为全部,1为电话报警,2为设备报警,3为短信报警,4为\nGPS报警,5为视频报警,6为设备故障报警,7其他报警;可以为直接组合如12为电话报警或设备报警 |
| `alarmType` | `String` | query/form | 否 | 报警类型, 报警类型。报警方式为2时,不携带 AlarmType为默认的报警设备报警,携带 AlarmType取值及对应报警类型如下:1-视频丢失报警;2-设备防拆报警;3-存储设备磁盘满报警;4-设备高温报警;5-设备低温报警。报警方式为5时,取值如下:1-人工视频报警;2-运动目标检测报警;3-遗留物检测报警;4-物体移除检测报警;5-绊线检测报警;6-入侵检测报警;7-逆行检测报警;8-徘徊检测报警;9-流量统计报警;10-密度检测报警;11-视频异常检测报警;12-快速移动报警。报警方式为6时,取值如下:1-存储设备磁盘故障报警;2-存储设备风扇故障报警 |

### `GET /api/device/control/i_frame`

强制关键帧。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 否 | 通道国标编号 |

### `GET /api/device/control/home_position`

看守位设置。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 是 | 通道国标编号 |
| `enabled` | `Boolean` | query/form | 是 | 是否开启看守位 |
| `resetTime` | `Integer` | query/form | 否 | 自动归位时间间隔 单位：秒 |
| `presetIndex` | `Integer` | query/form | 否 | 调用预置位编号 |

### `GET /api/device/control/drag_zoom/zoom_in`

拉框放大。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 是 | 通道国标编号 |
| `length` | `int` | query/form | 是 | 播放窗口长度像素值 |
| `width` | `int` | query/form | 是 | 播放窗口宽度像素值 |
| `midPointX` | `int` | query/form | 是 | 拉框中心的横轴坐标像素值 |
| `midPointY` | `int` | query/form | 是 | 拉框中心的纵轴坐标像素值 |
| `lengthX` | `int` | query/form | 是 | 拉框长度像素值 |
| `lengthY` | `int` | query/form | 是 | 拉框宽度像素值 |

### `GET /api/device/control/drag_zoom/zoom_out`

拉框缩小。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 否 | 通道国标编号 |
| `length` | `int` | query/form | 是 | 播放窗口长度像素值 |
| `width` | `int` | query/form | 是 | 播放窗口宽像素值 |
| `midPointX` | `int` | query/form | 是 | 拉框中心的横轴坐标像素值 |
| `midPointY` | `int` | query/form | 是 | 拉框中心的纵轴坐标像素值 |
| `lengthX` | `int` | query/form | 是 | 拉框长度像素值 |
| `lengthY` | `int` | query/form | 是 | 拉框宽度像素值 |

## 国标设备查询（DeviceQuery）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/DeviceQuery.java`

### `GET /api/device/query/register/attempts`

查询待处理的下级设备注册请求。源码返回类型：`List<DeviceRegisterAttempt>`。

无业务请求参数。

### `DELETE /api/device/query/register/attempts/{deviceId}`

忽略待处理的下级设备注册请求。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | REGISTER From头中的设备国标编号 |

### `GET /api/device/query/devices/{deviceId}`

查询国标设备。源码返回类型：`Device`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/device/{deviceId}`

查询国标设备。源码返回类型：`Device`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/devices`

分页查询国标设备。源码返回类型：`PageInfo<Device>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 未声明 | 搜索 |
| `status` | `Boolean` | query/form | 未声明 | 状态 |
| `accessType` | `String` | query/form | 未声明 | 接入类型：DEVICE-国标设备，PLATFORM-下级平台 |

### `GET /api/device/query/list`

分页查询国标设备。源码返回类型：`PageInfo<Device>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 未声明 | 搜索 |
| `status` | `Boolean` | query/form | 未声明 | 状态 |
| `accessType` | `String` | query/form | 未声明 | 接入类型：DEVICE-国标设备，PLATFORM-下级平台 |

### `GET /api/device/query/devices/{deviceId}/channels`

分页查询通道。源码返回类型：`PageInfo<DeviceChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Boolean` | query/form | 否 | 设备/子目录-> false/true |
| `resourceType` | `String` | query/form | 否 | 资源类型：CHANNEL-视频通道，DIRECTORY-目录节点 |

### `GET /api/device/query/device/{deviceId}/channels`

分页查询通道。源码返回类型：`PageInfo<DeviceChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Boolean` | query/form | 否 | 设备/子目录-> false/true |
| `resourceType` | `String` | query/form | 否 | 资源类型：CHANNEL-视频通道，DIRECTORY-目录节点 |

### `GET /api/device/query/streams`

分页查询存在流的通道。源码返回类型：`PageInfo<DeviceChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |

### `GET /api/device/query/devices/{deviceId}/sync`

同步设备通道。源码返回类型：`WVPResult<SyncStatus>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/device/{deviceId}/sync`

同步设备通道。源码返回类型：`WVPResult<SyncStatus>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `DELETE /api/device/query/devices/{deviceId}/delete`

移除设备。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `DELETE /api/device/query/device/{deviceId}/delete`

移除设备。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/sub_channels/{deviceId}/{channelId}/channels`

分页查询子目录通道。源码返回类型：`PageInfo<DeviceChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `channelType` | `Boolean` | query/form | 否 | 设备/子目录-> false/true |
| `resourceType` | `String` | query/form | 否 | 资源类型：CHANNEL-视频通道，DIRECTORY-目录节点 |

### `POST /api/device/query/channel/audio`

开启/关闭通道的音频。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道的数据库ID |
| `audio` | `Boolean` | query/form | 是 | 开启/关闭音频 |

### `POST /api/device/query/channel/stream/identification/update`

修改通道的码流类型。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channel` | `DeviceChannel` | query/form | 未声明 | 通道对象；字段见模型索引 |

### `GET /api/device/query/channel/one`

获取单个通道详情。源码返回类型：`DeviceChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备的国标编码 |
| `channelDeviceId` | `String` | query/form | 是 | 通道的国标编码 |

### `POST /api/device/query/transport/{deviceId}/{streamMode}`

修改数据流传输模式。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `streamMode` | `String` | path | 是 | 数据流传输模式, 取值：UDP（udp传输），TCP-ACTIVE（tcp主动模式），TCP-PASSIVE（tcp被动模式） |

### `POST /api/device/query/device/add`

添加设备信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `device` | `Device` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/device/query/device/update`

更新设备信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `device` | `Device` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/device/query/devices/{deviceId}/status`

设备状态查询。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/device/{deviceId}/status`

设备状态查询。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/alarm`

设备报警查询。源码返回类型：`DeferredResult<WVPResult<Object>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `startPriority` | `String` | query/form | 否 | 报警起始级别, 0为全部,1为一级警情,2为二级警情,3为三级警情,4为四级警情 |
| `endPriority` | `String` | query/form | 否 | 报警终止级别, ,0为全部,1为一级警情,2为二级警情,3为三级警情,4为四级警情 |
| `alarmMethod` | `String` | query/form | 否 | 报警方式条件,取值0为全部,1为电话报警,2为设备报警,3为短信报警,4为GPS报警,5为视频报警,6为设备故障报警,7其他报警;可以为直接组合如12为电话报警或设备报警 |
| `alarmType` | `String` | query/form | 否 | 报警类型 |
| `startTime` | `String` | query/form | 否 | 报警发生起始时间 |
| `endTime` | `String` | query/form | 否 | 报警发生终止时间 |

### `GET /api/device/query/info`

设备信息查询。源码返回类型：`DeferredResult<WVPResult<Object>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |

### `GET /api/device/query/{deviceId}/sync_status`

获取通道同步进度（此接口保留仅作为兼容，后续将移除，请迁移至 /sync_status?deviceId=）。源码返回类型：`WVPResult<SyncStatus>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/device/query/sync_status`

获取通道同步进度。源码返回类型：`WVPResult<SyncStatus>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |

### `GET /api/device/query/snap/{deviceId}/{channelId}`

请求截图。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `mark` | `String` | query/form | 否 | 标识 |

### `GET /api/device/query/channel/raw`

国标通道编辑时的数据回显。源码返回类型：`DeviceChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 通道的Id |

### `GET /api/device/query/subscribe/catalog`

开启/关闭目录订阅。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 通道的Id |
| `cycle` | `int` | query/form | 是 | 订阅周期 |

### `GET /api/device/query/subscribe/mobile-position`

开启/关闭移动位置订阅。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 通道的Id |
| `cycle` | `int` | query/form | 是 | 订阅周期 |
| `interval` | `int` | query/form | 是 | 报送间隔 |

### `GET /api/device/query/statistics/keepalive`

请求心跳统计。源码返回类型：`List<TimeStatistics>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `count` | `Integer` | query/form | 是 | 返回的数量，按时间正向排序，返回的最新的 |

### `GET /api/device/query/statistics/register`

请求注册统计。源码返回类型：`List<TimeStatistics>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `count` | `Integer` | query/form | 是 | 返回的数量，按时间正向排序，返回的最新的 |

### `GET /api/device/query/subscribe/alarm`

开启/关闭报警订阅。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 通道的Id |
| `cycle` | `int` | query/form | 是 | 订阅周期 |

## 国标录像（GBRecordController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/GBRecordController.java`

### `GET /api/gb_record/query/{deviceId}/{channelId}`

录像查询。源码返回类型：`DeferredResult<WVPResult<RecordInfo>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `startTime` | `String` | query/form | 是 | 开始时间 |
| `endTime` | `String` | query/form | 是 | 结束时间 |

### `GET /api/gb_record/download/start/{deviceId}/{channelId}`

开始历史媒体下载。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `startTime` | `String` | query/form | 是 | 开始时间 |
| `endTime` | `String` | query/form | 是 | 结束时间 |
| `downloadSpeed` | `String` | query/form | 是 | 下载倍速 |

### `GET /api/gb_record/download/stop/{deviceId}/{channelId}/{stream}`

停止历史媒体下载。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `stream` | `String` | path | 是 | 流ID |

### `GET /api/gb_record/download/progress/{deviceId}/{channelId}/{stream}`

获取历史媒体下载进度。源码返回类型：`StreamContent`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `stream` | `String` | path | 是 | 流ID |

## 分组管理（GroupController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/GroupController.java`

### `POST /api/group/add`

添加分组。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `group` | `Group` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/group/tree/list`

查询分组节点。源码返回类型：`List<GroupTree>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 要搜索的内容 |
| `parent` | `Integer` | query/form | 否 | 所属分组编号 |
| `hasChannel` | `Boolean` | query/form | 否 | 是否仅显示有通道的节点 |

### `GET /api/group/tree/query`

查询分组。源码返回类型：`PageInfo<Group>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 未声明 | 页码，从 1 开始 |
| `count` | `Integer` | query/form | 未声明 | 每页记录数 |
| `query` | `String` | query/form | 是 | 要搜索的内容 |

### `POST /api/group/update`

更新分组。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `group` | `Group` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/group/delete`

删除分组。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 分组id |

### `GET /api/group/path`

获取所属的行政区划下的行政区划。源码返回类型：`List<Group>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 当前的行政区划 |
| `businessGroup` | `String` | query/form | 未声明 | 业务分组编号 |

## 媒体流相关（MediaController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/MediaController.java`

### `GET /api/media/stream_info_by_app_and_stream`

根据应用名和流id获取播放地址。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流id |
| `mediaServerId` | `String` | query/form | 否 | 媒体服务器id |
| `callId` | `String` | query/form | 否 | 推流时携带的自定义鉴权ID |
| `useSourceIpAsStreamIp` | `Boolean` | query/form | 否 | 是否使用请求IP作为返回的地址IP |

### `GET /api/media/getPlayUrl`

获取推流播放地址。源码返回类型：`StreamContent`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流id |
| `mediaServerId` | `String` | query/form | 否 | 媒体服务器id |

## 位置信息管理（MobilePositionController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/MobilePositionController.java`

### `GET /api/position/history/{deviceId}`

查询历史轨迹。源码返回类型：`List<MobilePosition>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 未声明 | 通道的数据库ID |
| `start` | `String` | query/form | 否 | 开始时间 |
| `end` | `String` | query/form | 否 | 结束时间 |

### `GET /api/position/latest`

查询通道最新位置。源码返回类型：`MobilePosition`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channelId` | `Integer` | query/form | 是 | 通道的数据库ID |

### `GET /api/position/realtime/{deviceId}`

获取移动位置信息。源码返回类型：`DeferredResult<MobilePosition>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |

### `GET /api/position/subscribe/{deviceId}`

订阅位置信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `expires` | `String` | query/form | 是 | 订阅超时时间 |
| `interval` | `String` | query/form | 是 | 上报时间间隔 |

## 级联平台管理（PlatformController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/PlatformController.java`

### `GET /api/platform/server_config`

获取国标服务的配置。源码返回类型：`JSONObject`。

无业务请求参数。

### `GET /api/platform/info/{id}`

获取级联服务器信息。源码返回类型：`Platform`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `String` | path | 是 | 平台国标编号 |

### `GET /api/platform/query`

分页查询级联平台。源码返回类型：`PageInfo<Platform>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 未声明 | 当前页 |
| `count` | `int` | query/form | 未声明 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |

### `GET /api/platform/register/test`

测试注册, 立即向上级平台发送一次REGISTER并返回明确结果。源码返回类型：`PlatformRegisterResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 平台的数据库ID |

### `GET /api/platform/register/result`

查询最近一次注册结果。源码返回类型：`PlatformRegisterResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serverGBId` | `String` | query/form | 是 | 上级平台国标编号 |

### `POST /api/platform/add`

添加上级平台信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `platform` | `Platform` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/platform/update`

更新上级平台信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `parentPlatform` | `Platform` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/platform/delete`

删除上级平台。源码返回类型：`WVPResult<?>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 未声明 | 上级平台ID |

### `GET /api/platform/exit/{serverGBId}`

查询上级平台是否存在。源码返回类型：`Boolean`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serverGBId` | `String` | path | 是 | 上级平台的国标编号 |

### `GET /api/platform/channel/list`

分页查询级联平台的所有所有通道。源码返回类型：`PageInfo<PlatformChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页条数 |
| `platformId` | `Integer` | query/form | 否 | 上级平台的数据ID |
| `query` | `String` | query/form | 否 | 查询内容 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `hasShare` | `Boolean` | query/form | 否 | 是否已经共享 |

### `POST /api/platform/channel/add`

向上级平台添加国标通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `UpdateChannelParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/platform/channel/remove`

从上级平台移除国标通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `UpdateChannelParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/platform/channel/push`

推送通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 平台ID |

### `POST /api/platform/channel/device/add`

添加通道-通过设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `UpdateChannelParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/platform/channel/device/remove`

移除通道-通过设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `UpdateChannelParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/platform/channel/custom/update`

自定义共享通道信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channel` | `PlatformChannel` | JSON body | 是 | 请求体模型；字段见模型索引 |

## 国标设备点播（PlayController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/PlayController.java`

### `GET /api/play/start/{deviceId}/{channelId}`

开始点播。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |

### `GET /api/play/stop/{deviceId}/{channelId}`

停止点播。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |

### `POST /api/play/convertStop/{key}`

结束转码。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `key` | `String` | path | 是 | 视频流key |
| `mediaServerId` | `String` | query/form | 是 | 流媒体服务ID |

### `GET /api/play/broadcast/{deviceId}/{channelId}`

语音广播命令。源码返回类型：`AudioBroadcastResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 通道国标编号 |
| `channelId` | `String` | path | 是 | 通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID |
| `timeout` | `Integer` | query/form | 是 | 推流超时时间(秒) |
| `broadcastMode` | `Boolean` | query/form | 未声明 | 语音广播模式开关 |

### `POST /api/play/broadcast/{deviceId}/{channelId}`

语音广播命令。源码返回类型：`AudioBroadcastResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 通道国标编号 |
| `channelId` | `String` | path | 是 | 通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID |
| `timeout` | `Integer` | query/form | 是 | 推流超时时间(秒) |
| `broadcastMode` | `Boolean` | query/form | 未声明 | 语音广播模式开关 |

### `GET /api/play/broadcast/stop/{deviceId}/{channelId}`

停止语音广播。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备Id |
| `channelId` | `String` | path | 是 | 通道Id |

### `POST /api/play/broadcast/stop/{deviceId}/{channelId}`

停止语音广播。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备Id |
| `channelId` | `String` | path | 是 | 通道Id |

### `GET /api/play/ssrc`

获取所有的ssrc。源码返回类型：`JSONObject`。

无业务请求参数。

### `GET /api/play/snap`

获取截图。源码返回类型：`DeferredResult<String>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 是 | 设备国标编号 |
| `channelId` | `String` | query/form | 是 | 通道国标编号 |

## 视频回放（PlaybackController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/PlaybackController.java`

### `GET /api/playback/start/{deviceId}/{channelId}`

开始视频回放。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `startTime` | `String` | query/form | 是 | 开始时间 |
| `endTime` | `String` | query/form | 是 | 结束时间 |

### `GET /api/playback/stop/{deviceId}/{channelId}/{stream}`

停止视频回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `stream` | `String` | path | 是 | 流ID |

### `GET /api/playback/pause/{streamId}`

回放暂停。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `streamId` | `String` | path | 是 | 回放流ID |

### `GET /api/playback/resume/{streamId}`

回放恢复。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `streamId` | `String` | path | 是 | 回放流ID |

### `GET /api/playback/seek/{streamId}/{seekTime}`

回放拖动播放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `streamId` | `String` | path | 是 | 回放流ID |
| `seekTime` | `long` | path | 是 | 拖动偏移量，单位s |

### `GET /api/playback/speed/{streamId}/{speed}`

回放倍速播放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `streamId` | `String` | path | 是 | 回放流ID |
| `speed` | `Double` | path | 是 | 倍速0.25 0.5 1、2、4、8 |

## 前端设备控制（PtzController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/PtzController.java`

### `GET /api/front-end/common/{deviceId}/{channelId}`

通用前端控制命令(参考国标文档A.3.1指令格式)。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cmdCode` | `Integer` | query/form | 是 | 指令码(对应国标文档指令格式中的字节4) |
| `parameter1` | `Integer` | query/form | 是 | 数据一(对应国标文档指令格式中的字节5, 范围0-255) |
| `parameter2` | `Integer` | query/form | 是 | 数据二(对应国标文档指令格式中的字节6, 范围0-255) |
| `combindCode2` | `Integer` | query/form | 是 | 组合码二(对应国标文档指令格式中的字节7, 范围0-15) |

### `GET /api/front-end/ptz/{deviceId}/{channelId}`

云台控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop |
| `horizonSpeed` | `Integer` | query/form | 未声明 | 水平速度(0-255)，不传默认100 |
| `verticalSpeed` | `Integer` | query/form | 未声明 | 垂直速度(0-255)，不传默认100 |
| `zoomSpeed` | `Integer` | query/form | 未声明 | 缩放速度(0-15)；建议显式传值 |

### `GET /api/front-end/fi/iris/{deviceId}/{channelId}`

光圈控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: in, out, stop |
| `speed` | `Integer` | query/form | 是 | 光圈速度(0-255) |

### `GET /api/front-end/fi/focus/{deviceId}/{channelId}`

聚焦控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: near, far, stop |
| `speed` | `Integer` | query/form | 是 | 聚焦速度(0-255) |

### `GET /api/front-end/preset/query/{deviceId}/{channelId}`

查询预置位。源码返回类型：`DeferredResult<WVPResult<Object>>`。`data[].presetId` 为设备原样返回的字符串，可能无法转换成控制接口所需的整数。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |

### `GET /api/front-end/preset/add/{deviceId}/{channelId}`

预置位指令-设置预置位。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `presetId` | `String` | query/form | 是 | 按十进制整数校验，范围1-255 |

### `GET /api/front-end/preset/call/{deviceId}/{channelId}`

预置位指令-调用预置位。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `presetId` | `String` | query/form | 是 | 按十进制整数校验，范围1-255 |

### `GET /api/front-end/preset/delete/{deviceId}/{channelId}`

预置位指令-删除预置位。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `presetId` | `String` | query/form | 是 | 按十进制整数校验，范围1-255 |

### `GET /api/front-end/cruise/point/add/{deviceId}/{channelId}`

巡航指令-加入巡航点。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cruiseId` | `Integer` | query/form | 是 | 巡航组号(0-255) |
| `presetId` | `Integer` | query/form | 是 | 预置位编号(1-255) |

### `GET /api/front-end/cruise/point/delete/{deviceId}/{channelId}`

巡航指令-删除一个巡航点。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cruiseId` | `Integer` | query/form | 是 | 巡航组号(1-255) |
| `presetId` | `Integer` | query/form | 是 | 预置位编号(0-255, 为0时删除整个巡航) |

### `GET /api/front-end/cruise/speed/{deviceId}/{channelId}`

巡航指令-设置巡航速度。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cruiseId` | `Integer` | query/form | 是 | 巡航组号(0-255) |
| `speed` | `Integer` | query/form | 是 | 巡航速度(1-4095) |

### `GET /api/front-end/cruise/time/{deviceId}/{channelId}`

巡航指令-设置巡航停留时间。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cruiseId` | `Integer` | query/form | 是 | 巡航组号 |
| `time` | `Integer` | query/form | 是 | 巡航停留时间(1-4095) |

### `GET /api/front-end/cruise/start/{deviceId}/{channelId}`

巡航指令-开始巡航。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cruiseId` | `Integer` | query/form | 是 | 巡航组号) |

### `GET /api/front-end/cruise/stop/{deviceId}/{channelId}`

巡航指令-停止巡航。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `cruiseId` | `Integer` | query/form | 是 | 巡航组号 |

### `GET /api/front-end/scan/start/{deviceId}/{channelId}`

扫描指令-开始自动扫描。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-255) |

### `GET /api/front-end/scan/stop/{deviceId}/{channelId}`

扫描指令-停止自动扫描。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-255) |

### `GET /api/front-end/scan/set/left/{deviceId}/{channelId}`

扫描指令-设置自动扫描左边界。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-255) |

### `GET /api/front-end/scan/set/right/{deviceId}/{channelId}`

扫描指令-设置自动扫描右边界。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-255) |

### `GET /api/front-end/scan/set/speed/{deviceId}/{channelId}`

扫描指令-设置自动扫描速度。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `scanId` | `Integer` | query/form | 是 | 扫描组号(0-255) |
| `speed` | `Integer` | query/form | 是 | 自动扫描速度(1-4095) |

### `GET /api/front-end/wiper/{deviceId}/{channelId}`

辅助开关控制指令-雨刷控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: on, off |

### `GET /api/front-end/auxiliary/{deviceId}/{channelId}`

辅助开关控制指令。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | path | 是 | 设备国标编号 |
| `channelId` | `String` | path | 是 | 通道国标编号 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: on, off |
| `switchId` | `Integer` | query/form | 是 | 开关编号 |

## 区域管理（RegionController）

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/RegionController.java`

### `POST /api/region/add`

添加区域。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `region` | `Region` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/region/page/list`

查询区域。源码返回类型：`PageInfo<Region>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 要搜索的内容 |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |

### `GET /api/region/tree/list`

查询区域节点。源码返回类型：`List<RegionTree>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `parent` | `Integer` | query/form | 否 | 所属行政区划编号 |
| `hasChannel` | `Boolean` | query/form | 否 | 是否查询通道 |

### `GET /api/region/tree/query`

查询区域。源码返回类型：`PageInfo<Region>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 未声明 | 页码，从 1 开始 |
| `count` | `Integer` | query/form | 未声明 | 每页记录数 |
| `query` | `String` | query/form | 是 | 要搜索的内容 |

### `POST /api/region/update`

更新区域。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `region` | `Region` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/region/delete`

删除区域。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 区域ID |

### `GET /api/region/one`

根据区域Id查询区域。源码返回类型：`Region`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `regionDeviceId` | `String` | query/form | 是 | 行政区划节点编号 |

### `GET /api/region/base/child/list`

获取所属的行政区划下的行政区划。源码返回类型：`List<Region>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `parent` | `String` | query/form | 否 | 所属的行政区划 |

### `GET /api/region/path`

获取所属的行政区划下的行政区划。源码返回类型：`List<Region>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 当前的行政区划 |

### `GET /api/region/sync`

从通道中同步行政区划。源码返回类型：`void`。

无业务请求参数。

### `GET /api/region/description`

根据行政区划编号从文件中查询层级和描述。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `civilCode` | `String` | query/form | 未声明 | 行政区划编码 |

### `GET /api/region/addByCivilCode`

根据行政区划编号从文件中查询层级并添加。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `civilCode` | `String` | query/form | 未声明 | 行政区划编码 |

## 部标设备控制（需启用配置）（JT1078Controller）

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/JT1078Controller.java`

### `GET /api/jt1078/live/start`

JT-开始点播。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道编号, 一般为从1开始的数字 |
| `type` | `Integer` | query/form | 否 | 类型：0:音视频,1:视频,3:音频 |

### `GET /api/jt1078/live/stop`

JT-结束点播。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |

### `GET /api/jt1078/talk/start`

JT-语音对讲。源码返回类型：`StreamContent`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |

### `GET /api/jt1078/talk/stop`

JT-结束对讲。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |

### `GET /api/jt1078/live/pause`

JT-暂停点播。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |

### `GET /api/jt1078/live/continue`

JT-继续点播。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |

### `GET /api/jt1078/live/switch`

JT-切换码流类型。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `streamType` | `Integer` | query/form | 是 | 0:主码流; 1:子码流 |

### `GET /api/jt1078/record/list`

JT-录像-查询资源列表。源码返回类型：`WVPResult<List<J1205.JRecordItem>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `startTime` | `String` | query/form | 是 | 开始时间,格式： yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | query/form | 是 | 结束时间,格式： yyyy-MM-dd HH:mm:ss |

### `GET /api/jt1078/playback/start`

JT-录像-开始回放。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `startTime` | `String` | query/form | 是 | 开始时间,格式： yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | query/form | 是 | 结束时间,格式： yyyy-MM-dd HH:mm:ss |
| `type` | `Integer` | query/form | 否 | 0.音视频 1.音频 2.视频 3.视频或音视频 |
| `rate` | `Integer` | query/form | 否 | 0.所有码流 1.主码流 2.子码流(如果此通道只传输音频,此字段置0) |
| `playbackType` | `Integer` | query/form | 否 | 0.正常回放 1.快进回放 2.关键帧快退回放 3.关键帧播放 4.单帧上传 |
| `playbackSpeed` | `Integer` | query/form | 否 | 0.无效 1.1倍 2.2倍 3.4倍 4.8倍 5.16倍 (回放控制为1和2时,此字段内容有效,否则置0) |

### `GET /api/jt1078/playback/control`

JT-录像-回放控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `command` | `Integer` | query/form | 否 | 0:开始回放; 1:暂停回放; 2:结束回放; 3:快进回放; 4:关键帧快退回放; 5:拖动回放; 6:关键帧播放 |
| `time` | `String` | query/form | 否 | 拖动回放位置(时间) |
| `playbackSpeed` | `Integer` | query/form | 否 | 0.无效 1.1倍 2.2倍 3.4倍 4.8倍 5.16倍 (回放控制为3和4时,此字段内容有效,否则置0) |

### `GET /api/jt1078/playback/stop`

JT-录像-结束回放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |

### `GET /api/jt1078/playback/downloadUrl`

JT-录像-获取下载地址。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `startTime` | `String` | query/form | 是 | 开始时间,格式： yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | query/form | 是 | 结束时间,格式： yyyy-MM-dd HH:mm:ss |
| `alarmSign` | `Integer` | query/form | 否 | 报警标志 |
| `mediaType` | `Integer` | query/form | 否 | 音视频资源类型： 0.音视频 1.音频 2.视频 3.视频或音视频 |
| `streamType` | `Integer` | query/form | 否 | 码流类型：0.所有码流 1.主码流 2.子码流(如果此通道只传输音频,此字段置0) |
| `storageType` | `Integer` | query/form | 否 | 存储器类型 |

### `GET /api/jt1078/playback/download`

JT-录像-下载。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `path` | `String` | query/form | 是 | 临时下载路径 |

### `GET /api/jt1078/ptz`

JT-云台控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: left, right, up, down, zoomin, zoomout, irisin, irisout, focusnear, focusfar, stop |
| `speed` | `int` | query/form | 是 | 速度(0-255)， command,值 left, right, up, down时有效 |

### `GET /api/jt1078/fill-light`

JT-补光灯开关。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: on off |

### `GET /api/jt1078/wiper`

JT-雨刷开关。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `channelId` | `Integer` | query/form | 是 | 通道国标编号, 一般为从1开始的数字 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: on off |

### `GET /api/jt1078/config/get`

JT-查询终端参数。源码返回类型：`JTDeviceConfig`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `params` | `String[]` | query/form | 未声明 | 请求参数集合 |

### `POST /api/jt1078/config/set`

JT-设置终端参数。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `config` | `SetConfigParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/control/connection`

终端控制-连接指定的服务器。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `control` | `ConnectionControlParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/control/reset`

JT-终端控制-复位。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |

### `POST /api/jt1078/control/factory-reset`

JT-终端控制-恢复出厂设置。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |

### `GET /api/jt1078/attribute`

JT-查询终端属性。源码返回类型：`JTDeviceAttribute`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |

### `GET /api/jt1078/position-info`

JT-查询位置信息。源码返回类型：`JTPositionBaseInfo`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |

### `GET /api/jt1078/control/temp-position-tracking`

JT-临时位置跟踪控制。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `timeInterval` | `Integer` | query/form | 是 | 时间间隔,单位为秒,时间间隔为0 时停止跟踪,停止跟踪无需带后继字段 |
| `validityPeriod` | `Long` | query/form | 是 | 位置跟踪有效期, 单位为秒,终端在接收到位置跟踪控制消息后,在有效期截止时间之前依据消息中的时间间隔发送位置汇报 |

### `POST /api/jt1078/confirmation-alarm-message`

JT-人工确认报警消息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ConfirmationAlarmMessageParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/link-detection`

JT-链路检测。源码返回类型：`Integer`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |

### `POST /api/jt1078/text-msg`

JT-文本信息下发。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `textMessageParam` | `TextMessageParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/telephone-callback`

JT-电话回拨。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `sign` | `Integer` | query/form | 是 | 标志: 0:普通通话,1:监听 |
| `destPhoneNumber` | `String` | query/form | 是 | 回拨电话号码 |

### `POST /api/jt1078/set-phone-book`

JT-设置电话本。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `setPhoneBookParam` | `SetPhoneBookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/control/door`

JT-车门控制。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `open` | `Boolean` | query/form | 是 | 开启车门 |

### `POST /api/jt1078/area/circle/update`

JT-更新圆形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/area/circle/add`

JT-追加圆形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/area/circle/edit`

JT-修改圆形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/area/circle/delete`

JT-删除圆形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 待删除圆形区域的id，例如1,2,3 |

### `GET /api/jt1078/area/circle/query`

JT-查询圆形区域。源码返回类型：`WVPResult<List<JTAreaOrRoute>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 记录 ID 集合 |

### `POST /api/jt1078/area/rectangle/update`

JT-更新矩形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/area/rectangle/add`

JT-追加矩形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/area/rectangle/edit`

JT-修改矩形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/area/rectangle/delete`

JT-删除矩形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 待删除圆形区域的id，例如1,2,3 |

### `GET /api/jt1078/area/rectangle/query`

JT-查询矩形区域。源码返回类型：`WVPResult<List<JTAreaOrRoute>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 记录 ID 集合 |

### `POST /api/jt1078/area/polygon/set`

JT-设置多边形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/area/polygon/delete`

JT-删除多边形区域。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 待删除圆形区域的id，例如1,2,3 |

### `GET /api/jt1078/area/polygon/query`

JT-查询多边形区域。源码返回类型：`WVPResult<List<JTAreaOrRoute>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 记录 ID 集合 |

### `POST /api/jt1078/route/set`

JT-设置路线。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `areaParam` | `SetAreaParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/route/delete`

JT-删除路线。源码返回类型：`WVPResult<Integer>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 待删除圆形区域的id，例如1,2,3 |

### `GET /api/jt1078/route/query`

JT-查询路线。源码返回类型：`WVPResult<List<JTAreaOrRoute>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `ids` | `List<Long>` | query/form | 否 | 记录 ID 集合 |

### `GET /api/jt1078/driver-information`

JT-上报驾驶员身份信息请求。源码返回类型：`WVPResult<JTDriverInformation>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |

### `POST /api/jt1078/shooting`

JT-摄像头立即拍摄命令。源码返回类型：`WVPResult<List<Long>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ShootingParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/snap`

JT-抓图。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `channelId` | `Integer` | query/form | 是 | 通道编号 |

### `POST /api/jt1078/media/list`

JT-存储多媒体数据检索。源码返回类型：`WVPResult<List<JTMediaDataInfo>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `QueryMediaDataParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/jt1078/media/upload/one/upload`

JT-单条存储多媒体数据上传。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `mediaId` | `Long` | query/form | 是 | 多媒体ID |

### `GET /api/jt1078/media/upload/one/delete`

JT-单条存储多媒体数据删除。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备编号 |
| `mediaId` | `Long` | query/form | 是 | 多媒体ID |

### `POST /api/jt1078/media-data-upload`

JT-开始录音。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `time` | `Integer` | query/form | 否 | 录音时间，单位为秒(s) ,0 表示一直录音 |
| `save` | `Integer` | query/form | 否 | 0:实时上传；1:保存 |
| `samplingRate` | `Integer` | query/form | 否 | 音频采样率， 0:8K；1:11K；2:23K；3:32K |

### `GET /api/jt1078/record/start`

JT-开始录音。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `time` | `Integer` | query/form | 否 | 录音时间，单位为秒(s) ,0 表示一直录音 |
| `save` | `Integer` | query/form | 否 | 0:实时上传；1:保存 |
| `samplingRate` | `Integer` | query/form | 否 | 音频采样率， 0:8K；1:11K；2:23K；3:32K |

### `GET /api/jt1078/record/stop`

JT-停止录音。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |
| `time` | `Integer` | query/form | 否 | 录音时间，单位为秒(s) ,0 表示一直录音 |
| `save` | `Integer` | query/form | 否 | 0:实时上传；1:保存 |
| `samplingRate` | `Integer` | query/form | 否 | 音频采样率， 0:8K；1:11K；2:23K；3:32K |

### `GET /api/jt1078/media/attribute`

JT-查询终端音视频属性。源码返回类型：`JTMediaAttribute`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |

## 部标终端以及通道管理（需启用配置）（JT1078TerminalController）

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/JT1078TerminalController.java`

### `GET /api/jt1078/terminal/list`

JT-分页查询部标设备。源码返回类型：`PageInfo<JTDevice>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |

### `POST /api/jt1078/terminal/update`

更新设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `device` | `JTDevice` | query/form | 是 | 设备 |

### `POST /api/jt1078/terminal/add`

JT-新增设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `device` | `JTDevice` | query/form | 是 | 设备 |

### `DELETE /api/jt1078/terminal/delete`

删除设备。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `phoneNumber` | `String` | query/form | 是 | 设备手机号 |

### `GET /api/jt1078/terminal/query`

查询设备。源码返回类型：`JTDevice`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `Integer` | query/form | 未声明 | 设备或下级平台国标编号 |

### `GET /api/jt1078/terminal/channel/list`

JT-查询部标通道。源码返回类型：`PageInfo<JTChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `deviceId` | `Integer` | query/form | 是 | 设备ID |
| `query` | `String` | query/form | 否 | 查询内容 |

### `GET /api/jt1078/terminal/channel/one`

JT-查询单个部标通道。源码返回类型：`JTChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 通道数据库ID |

### `POST /api/jt1078/terminal/channel/update`

JT-更新通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channel` | `JTChannel` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/jt1078/terminal/channel/add`

JT-新增通道。源码返回类型：`JTChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `channel` | `JTChannel` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/jt1078/terminal/channel/delete`

JT-删除通道。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 通道的数据库ID |

## ABLHttpHookListener（隐藏或内部接口）（ABLHttpHookListener）

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/ABLHttpHookListener.java`

### `POST /index/hook/abl/on_server_keepalive`

onServerKeepalive。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnServerKeepaliveABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_play`

onPlay。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnPlayABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_publish`

onPublish。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnPublishABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_record_progress`

onRecordProgress。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnRecordProgressABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_stream_not_arrive`

onStreamNotArrive。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_delete_record_mp4`

onDeleteRecordMp4。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnRecordMp4ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_stream_arrive`

onStreamArrive。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnStreamArriveABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_stream_none_reader`

onStreamNoneReader。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_stream_not_found`

onStreamNotFound。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_server_started`

onServerStarted。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnServerStaredABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_send_rtp_stopped`

onRecordMp4。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnRecordMp4ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_record_mp4`

onRecordMp4。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnRecordMp4ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/abl/on_stream_disconnect`

onRecordMp4。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `ABLHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

## ZLMHttpHookListener（隐藏或内部接口）（ZLMHttpHookListener）

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/ZLMHttpHookListener.java`

### `POST /index/hook/on_server_keepalive`

onServerKeepalive。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnServerKeepaliveHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_play`

onPlay。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnPlayHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_publish`

onPublish。源码返回类型：`HookResultForOnPublish`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnPublishHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_stream_changed`

onStreamChanged。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnStreamChangedHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_stream_none_reader`

onStreamNoneReader。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnStreamNoneReaderHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_stream_not_found`

onStreamNotFound。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnStreamNotFoundHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_server_started`

onServerStarted。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `jsonObject` | `JSONObject` | JSON body | 是 | 动态 JSON 对象；字段由对应回调协议定义 |

### `POST /index/hook/on_send_rtp_stopped`

onSendRtpStopped。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnSendRtpStoppedHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_rtp_server_timeout`

onRtpServerTimeout。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnRtpServerTimeoutHookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /index/hook/on_record_mp4`

onRecordMp4。源码返回类型：`HookResult`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `OnRecordMp4HookParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

## 拉流代理（StreamProxyController）

源码：`src/main/java/com/genersoft/iot/vmp/streamProxy/controller/StreamProxyController.java`

### `GET /api/proxy/list`

分页查询流代理。源码返回类型：`PageInfo<StreamProxy>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 否 | 当前页 |
| `count` | `Integer` | query/form | 否 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `pulling` | `Boolean` | query/form | 否 | 是否正在拉流 |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |

### `GET /api/proxy/one`

查询流代理。源码返回类型：`StreamProxy`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 未声明 | 应用名 |
| `stream` | `String` | query/form | 未声明 | 流Id |

### `POST /api/proxy/add`

新增代理。源码返回类型：`StreamProxy`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `StreamProxy` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/proxy/update`

更新代理。源码返回类型：`StreamProxy`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `StreamProxy` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/proxy/ffmpeg_cmd/list`

获取ffmpeg.cmd模板。源码返回类型：`Map<String, String>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `mediaServerId` | `String` | query/form | 是 | 流媒体ID |

### `DELETE /api/proxy/del`

移除代理。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流id |

### `DELETE /api/proxy/delete`

移除代理。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 代理ID |

### `GET /api/proxy/start`

播放代理。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 代理Id |

### `GET /api/proxy/stop`

停止播放。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 代理Id |

## 推流信息管理（StreamPushController）

源码：`src/main/java/com/genersoft/iot/vmp/streamPush/controller/StreamPushController.java`

### `GET /api/push/list`

推流列表查询。源码返回类型：`PageInfo<StreamPush>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 否 | 当前页 |
| `count` | `Integer` | query/form | 否 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `pushing` | `Boolean` | query/form | 否 | 是否正在推流 |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |

### `POST /api/push/remove`

删除。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `int` | query/form | 是 | 应用名 |

### `POST /api/push/upload`

uploadChannelFile。源码返回类型：`DeferredResult<ResponseEntity<WVPResult<Object>>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `file` | `MultipartFile` | multipart | 未声明 | 上传文件 |

### `POST /api/push/add`

添加推流信息。源码返回类型：`StreamPush`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `stream` | `StreamPush` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/push/update`

更新推流信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `stream` | `StreamPush` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/push/batchRemove`

删除多个推流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ids` | `BatchRemoveParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/push/start`

开始播放。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 未声明 | 数据库记录 ID |

### `GET /api/push/forceClose`

强制停止推流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 未声明 | 流应用名 |
| `stream` | `String` | query/form | 未声明 | 流 ID |

## TestController（TestController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/TestController.java`

### `GET /api/test/hook/list`

all。源码返回类型：`List<Hook>`。

无业务请求参数。

### `GET /api/test/redis`

redis。源码返回类型：`List<InviteInfo>`。

无业务请求参数。

## 报警管理接口（AlarmController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/alarm/AlarmController.java`

### `GET /api/alarm/list`

分页查询报警列表。源码返回类型：`PageInfo<Alarm>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 是 | 当前页 |
| `count` | `Integer` | query/form | 是 | 每页查询数量 |
| `alarmType` | `List<AlarmType>` | query/form | 否 | 报警类型列表，多个类型用逗号分隔 |
| `beginTime` | `String` | query/form | 否 | 开始时间，格式：yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | query/form | 否 | 结束时间，格式：yyyy-MM-dd HH:mm:ss |

### `DELETE /api/alarm/delete`

删除报警信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ids` | `List<Long>` | JSON body | 是 | JSON 整数数组，元素为记录 ID |

### `DELETE /api/alarm/clear`

按筛选条件清空报警信息。源码返回类型：`int`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `alarmType` | `List<AlarmType>` | query/form | 否 | 报警类型列表，为空则不限类型 |
| `beginTime` | `String` | query/form | 否 | 开始时间，格式：yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | query/form | 否 | 结束时间，格式：yyyy-MM-dd HH:mm:ss |

### `GET /api/alarm/snap/{id}`

获取报警快照图片。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Long` | path | 是 | 报警ID |

## 云端录像接口（CloudRecordController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/cloudRecord/CloudRecordController.java`

### `GET /api/cloud/record/date/list`

查询存在云端录像的日期。源码返回类型：`List<String>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流ID |
| `year` | `Integer` | query/form | 否 | 年，置空则查询当年 |
| `month` | `Integer` | query/form | 否 | 月，置空则查询当月 |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID，置空则查询全部 |

### `GET /api/cloud/record/list`

分页查询云端录像。源码返回类型：`PageInfo<CloudRecordItem>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 检索内容 |
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `startTime` | `String` | query/form | 否 | 开始时间(yyyy-MM-dd HH:mm:ss) |
| `endTime` | `String` | query/form | 否 | 结束时间(yyyy-MM-dd HH:mm:ss) |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID，置空则查询全部流媒体 |
| `callId` | `String` | query/form | 否 | 每次录像的唯一标识，置空则查询全部流媒体 |
| `ascOrder` | `Boolean` | query/form | 否 | 是否升序排序， 升序： true， 降序： false |

### `GET /api/cloud/record/task/add`

添加合并任务。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |
| `startTime` | `String` | query/form | 否 | 鉴权ID |
| `endTime` | `String` | query/form | 否 | 鉴权ID |
| `callId` | `String` | query/form | 否 | 鉴权ID |
| `remoteHost` | `String` | query/form | 否 | 用于拼接播放地址的完整外部地址，含协议、主机和端口，如 http://host:18082 |

### `GET /api/cloud/record/task/list`

查询合并任务。源码返回类型：`JSONArray`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 流应用名 |
| `stream` | `String` | query/form | 否 | 流 ID |
| `callId` | `String` | query/form | 否 | 本次 SIP 会话或录像的唯一标识 |
| `taskId` | `String` | query/form | 否 | 任务Id |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |
| `isEnd` | `Boolean` | query/form | 否 | 是否结束 |

### `GET /api/cloud/record/collect/add`

添加收藏。源码返回类型：`int`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |
| `startTime` | `String` | query/form | 否 | 鉴权ID |
| `endTime` | `String` | query/form | 否 | 鉴权ID |
| `callId` | `String` | query/form | 否 | 鉴权ID |
| `recordId` | `Integer` | query/form | 否 | 录像记录的ID，用于精准收藏一个视频文件 |

### `GET /api/cloud/record/collect/delete`

移除收藏。源码返回类型：`int`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |
| `startTime` | `String` | query/form | 否 | 鉴权ID |
| `endTime` | `String` | query/form | 否 | 鉴权ID |
| `callId` | `String` | query/form | 否 | 鉴权ID |
| `recordId` | `Integer` | query/form | 否 | 录像记录的ID，用于精准精准移除一个视频文件的收藏 |

### `GET /api/cloud/record/play/path`

获取播放地址。源码返回类型：`DownloadFileInfo`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `recordId` | `Integer` | query/form | 是 | 录像记录的ID |

### `GET /api/cloud/record/loadRecord`

加载录像文件形成播放地址。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流ID |
| `cloudRecordId` | `int` | query/form | 是 | 云端录像ID |

### `GET /api/cloud/record/seek`

定位录像播放到制定位置。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `mediaServerId` | `String` | query/form | 是 | 使用的节点Id |
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流ID |
| `seek` | `Double` | query/form | 是 | 要定位的时间位置，从录像开始的时间算起 |
| `schema` | `String` | query/form | 否 | 请求使用的 URL 协议方案 |

### `GET /api/cloud/record/speed`

设置录像播放速度。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `mediaServerId` | `String` | query/form | 是 | 使用的节点Id |
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流ID |
| `speed` | `Integer` | query/form | 是 | 要设置的录像倍速 |
| `schema` | `String` | query/form | 否 | 请求使用的 URL 协议方案 |

### `DELETE /api/cloud/record/delete`

删除录像文件。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ids` | `BatchRemoveParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/cloud/record/download/zip`

downloadZipFileFromUrl。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ids` | `Integer[]` | query/form | 未声明 | 记录 ID 集合 |

### `GET /api/cloud/record/zip`

downloadZipFile。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 按名称或编号筛选，具体范围由该接口实现决定 |
| `app` | `String` | query/form | 否 | 流应用名 |
| `stream` | `String` | query/form | 否 | 流 ID |
| `startTime` | `String` | query/form | 否 | 查询起始时间 |
| `endTime` | `String` | query/form | 否 | 查询结束时间 |
| `mediaServerId` | `String` | query/form | 否 | 流媒体节点 ID |
| `callId` | `String` | query/form | 否 | 本次 SIP 会话或录像的唯一标识 |
| `ids` | `List<Integer>` | query/form | 否 | 记录 ID 集合 |

### `GET /api/cloud/record/list-url`

分页查询云端录像。源码返回类型：`PageInfo<CloudRecordUrl>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 检索内容 |
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `startTime` | `String` | query/form | 否 | 开始时间(yyyy-MM-dd HH:mm:ss) |
| `endTime` | `String` | query/form | 否 | 结束时间(yyyy-MM-dd HH:mm:ss) |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID，置空则查询全部流媒体 |
| `callId` | `String` | query/form | 否 | 每次录像的唯一标识，置空则查询全部流媒体 |
| `remoteHost` | `String` | query/form | 否 | 用于拼接播放/下载地址的完整外部地址，含协议、主机和端口，如 http://host:18082；不传时使用默认媒体节点地址 |

## 日志文件查询接口（LogController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/log/LogController.java`

### `GET /api/log/list`

分页查询日志文件。源码返回类型：`List<LogFileInfo>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 检索内容 |
| `startTime` | `String` | query/form | 否 | 开始时间(yyyy-MM-dd HH:mm:ss) |
| `endTime` | `String` | query/form | 否 | 结束时间(yyyy-MM-dd HH:mm:ss) |

### `GET /api/log/file/{fileName}`

downloadFile。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `fileName` | `String` | path | 是 | 文件名 |

## 第三方PS服务对接（PsController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/ps/PsController.java`

### `GET /api/ps/receive/open`

开启收流和获取发流信息。源码返回类型：`OtherPsSendInfo`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `isSend` | `Boolean` | query/form | 是 | 是否发送，false时只开启收流， true同时返回推流信息 |
| `ssrc` | `String` | query/form | 否 | 来源流的SSRC，不传则不校验来源ssrc |
| `callId` | `String` | query/form | 是 | 整个过程的唯一标识，为了与后续接口关联 |
| `stream` | `String` | query/form | 是 | 形成的流的ID |
| `tcpMode` | `Integer` | query/form | 是 | 收流模式， 0为UDP， 1为TCP被动 |
| `callBack` | `String` | query/form | 是 | 回调地址，如果收流超时会通道回调通知，回调为get请求，参数为callId |

### `GET /api/ps/receive/close`

关闭收流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `stream` | `String` | query/form | 是 | 流的ID |

### `GET /api/ps/send/start`

发送流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ssrc` | `String` | query/form | 是 | 发送流的SSRC |
| `dstIp` | `String` | query/form | 是 | 目标收流IP |
| `dstPort` | `Integer` | query/form | 是 | 目标收流端口 |
| `app` | `String` | query/form | 是 | 待发送应用名 |
| `stream` | `String` | query/form | 是 | 待发送流Id |
| `callId` | `String` | query/form | 是 | 整个过程的唯一标识，不传则使用随机端口发流 |
| `isUdp` | `Boolean` | query/form | 是 | 是否为UDP |

### `GET /api/ps/send/stop`

关闭发送流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `callId` | `String` | query/form | 是 | 整个过程的唯一标识，不传则使用随机端口发流 |

### `GET /api/ps/getTestPort`

getTestPort。源码返回类型：`int`。

无业务请求参数。

## 录制计划（RecordPlanController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/recordPlan/RecordPlanController.java`

### `POST /api/record/plan/add`

添加录制计划。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `plan` | `RecordPlan` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `POST /api/record/plan/link`

通道关联录制计划。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `RecordPlanParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/record/plan/get`

查询录制计划。源码返回类型：`RecordPlan`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `planId` | `Integer` | query/form | 是 | 计划ID |

### `GET /api/record/plan/query`

查询录制计划列表。源码返回类型：`PageInfo<RecordPlan>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 检索内容 |
| `page` | `Integer` | query/form | 是 | 当前页 |
| `count` | `Integer` | query/form | 是 | 每页查询数量 |

### `GET /api/record/plan/channel/list`

分页查询录制计划关联的所有通道。源码返回类型：`PageInfo<CommonGBChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页条数 |
| `planId` | `Integer` | query/form | 否 | 录制计划ID |
| `query` | `String` | query/form | 否 | 查询内容 |
| `channelType` | `Integer` | query/form | 否 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 |
| `online` | `Boolean` | query/form | 否 | 是否在线 |
| `hasLink` | `Boolean` | query/form | 否 | 是否已经关联 |

### `POST /api/record/plan/update`

更新录制计划。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `plan` | `RecordPlan` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/record/plan/delete`

删除录制计划。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `planId` | `Integer` | query/form | 是 | 计划ID |

## 第三方服务对接（RtpController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/rtp/RtpController.java`

### `GET /api/rtp/receive/open`

开启收流和获取发流信息。源码返回类型：`OtherRtpSendInfo`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `isSend` | `Boolean` | query/form | 是 | 是否发送，false时只开启收流， true同时返回推流信息 |
| `ssrc` | `String` | query/form | 否 | 来源流的SSRC，不传则不校验来源ssrc |
| `callId` | `String` | query/form | 是 | 整个过程的唯一标识，为了与后续接口关联 |
| `stream` | `String` | query/form | 是 | 形成的流的ID |
| `tcpMode` | `Integer` | query/form | 是 | 收流模式， 0为UDP， 1为TCP被动 |
| `callBack` | `String` | query/form | 是 | 回调地址，如果收流超时会通道回调通知，回调为get请求，参数为callId |

### `GET /api/rtp/receive/close`

关闭收流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `stream` | `String` | query/form | 是 | 流的ID |

### `GET /api/rtp/send/start`

发送流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ssrc` | `String` | query/form | 是 | 发送流的SSRC |
| `dstIpForAudio` | `String` | query/form | 否 | 目标音频收流IP |
| `dstIpForVideo` | `String` | query/form | 否 | 目标视频收流IP |
| `dstPortForAudio` | `Integer` | query/form | 否 | 目标音频收流端口 |
| `dstPortForVideo` | `Integer` | query/form | 否 | 目标视频收流端口 |
| `app` | `String` | query/form | 是 | 待发送应用名 |
| `stream` | `String` | query/form | 是 | 待发送流Id |
| `callId` | `String` | query/form | 是 | 整个过程的唯一标识，不传则使用随机端口发流 |
| `isUdp` | `Boolean` | query/form | 是 | 是否为UDP |
| `ptForAudio` | `Integer` | query/form | 否 | rtp的音频pt |
| `ptForVideo` | `Integer` | query/form | 否 | rtp的视频pt |

### `GET /api/rtp/send/stop`

关闭发送流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `callId` | `String` | query/form | 是 | 整个过程的唯一标识，不传则使用随机端口发流 |

## 服务控制（ServerController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/server/ServerController.java`

### `GET /api/server/media_server/list`

流媒体服务列表。源码返回类型：`List<MediaServer>`。

无业务请求参数。

### `GET /api/server/media_server/online/list`

在线流媒体服务列表。源码返回类型：`List<MediaServer>`。

无业务请求参数。

### `GET /api/server/media_server/one/{id}`

停止视频回放。源码返回类型：`MediaServer`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `String` | path | 是 | 流媒体服务ID |

### `GET /api/server/media_server/check`

测试流媒体服务。源码返回类型：`MediaServer`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ip` | `String` | query/form | 是 | 流媒体服务IP |
| `port` | `int` | query/form | 是 | 流媒体服务HTT端口 |
| `secret` | `String` | query/form | 是 | 流媒体服务secret |
| `type` | `String` | query/form | 未声明 | 类型编码 |

### `GET /api/server/media_server/record/check`

测试流媒体录像管理服务。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `ip` | `String` | query/form | 是 | 流媒体服务IP |
| `port` | `int` | query/form | 是 | 流媒体服务HTT端口 |

### `POST /api/server/media_server/save`

保存流媒体服务。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `mediaServer` | `MediaServer` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `DELETE /api/server/media_server/delete`

移除流媒体服务。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `String` | query/form | 是 | 流媒体ID |

### `GET /api/server/media_server/media_info`

获取流信息。源码返回类型：`MediaInfo`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流ID |
| `mediaServerId` | `String` | query/form | 是 | 流媒体ID |

### `GET /api/server/shutdown`

关闭服务。源码返回类型：`void`。

无业务请求参数。

### `GET /api/server/system/configInfo`

获取系统配置信息。源码返回类型：`SystemConfigInfo`。

无业务请求参数。

### `GET /api/server/version`

获取版本信息。源码返回类型：`VersionPo`。

无业务请求参数。

### `GET /api/server/config`

获取配置信息。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `type` | `String` | query/form | 是 | 配置类型（sip, base） |

### `GET /api/server/system/info`

获取系统信息。源码返回类型：`SystemAllInfo`。

无业务请求参数。

### `GET /api/server/media_server/load`

获取负载信息。源码返回类型：`List<MediaServerLoad>`。

无业务请求参数。

### `GET /api/server/resource/info`

获取负载信息。源码返回类型：`ResourceInfo`。

无业务请求参数。

### `GET /api/server/info`

获取系统信息。源码返回类型：`Map<String, Map<String, String>>`。

无业务请求参数。

### `GET /api/server/map/config`

获取地图配置。源码返回类型：`List<MapConfig>`。

无业务请求参数。

### `GET /api/server/map/model-icon/list`

获取地图配置图标。源码返回类型：`List<MapModelIcon>`。

无业务请求参数。

## 角色管理（RoleController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/user/RoleController.java`

### `POST /api/role/add`

添加角色。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `name` | `String` | query/form | 是 | 角色名 |
| `authority` | `String` | query/form | 否 | 权限：0/admin=管理员，1/operator/write=操作员，2或其他值=只读 |

### `DELETE /api/role/delete`

删除角色。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户Id |

### `GET /api/role/all`

查询角色。源码返回类型：`List<Role>`。

无业务请求参数。

## 用户ApiKey管理（UserApiKeyController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/user/UserApiKeyController.java`

### `POST /api/userApiKey/add`

添加用户ApiKey。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `userId` | `int` | query/form | 是 | 用户Id |
| `app` | `String` | query/form | 否 | 应用名称 |
| `remark` | `String` | query/form | 否 | 备注信息 |
| `expiresAt` | `String` | query/form | 否 | 过期时间，格式 yyyy-MM-dd HH:mm:ss；不传代表永不过期 |
| `enable` | `Boolean` | query/form | 否 | 是否启用；不传默认 false |

### `GET /api/userApiKey/userApiKeys`

分页查询用户ApiKey。源码返回类型：`PageInfo<UserApiKey>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `userId` | `Integer` | query/form | 否 | 用户数据库 ID |

### `POST /api/userApiKey/enable`

启用用户ApiKey。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户ApiKeyId |

### `POST /api/userApiKey/disable`

停用用户ApiKey。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户ApiKeyId |

### `POST /api/userApiKey/reset`

重置用户ApiKey。源码返回类型：`WVPResult<String>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户ApiKeyId |

### `POST /api/userApiKey/remark`

备注用户ApiKey。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户ApiKeyId |
| `remark` | `String` | query/form | 否 | 用户ApiKey备注 |

### `DELETE /api/userApiKey/delete`

删除用户ApiKey。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户ApiKeyId |

## 用户管理（UserController）

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/user/UserController.java`

### `GET /api/user/login`

登录。源码返回类型：`LoginUser`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `username` | `String` | query/form | 是 | 用户名 |
| `password` | `String` | query/form | 是 | 密码（32位md5加密） |

### `POST /api/user/login`

登录。源码返回类型：`LoginUser`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `username` | `String` | query/form | 是 | 用户名 |
| `password` | `String` | query/form | 是 | 密码（32位md5加密） |

### `POST /api/user/changePassword`

修改密码。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `oldPassword` | `String` | query/form | 未声明 | 旧密码的 32 位 MD5 字符串 |
| `password` | `String` | query/form | 是 | 新密码（未md5加密的密码） |

### `POST /api/user/add`

添加用户。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `username` | `String` | query/form | 是 | 用户名 |
| `password` | `String` | query/form | 是 | 密码（未md5加密的密码） |
| `roleId` | `Integer` | query/form | 是 | 角色ID |

### `DELETE /api/user/delete`

删除用户。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `id` | `Integer` | query/form | 是 | 用户Id |

### `GET /api/user/all`

查询全部用户。源码返回类型：`List<User>`。

无业务请求参数。

### `GET /api/user/users`

分页查询用户。源码返回类型：`PageInfo<User>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |

### `ANY /api/user/changePushKey`

修改pushkey。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `userId` | `Integer` | query/form | 是 | 用户Id |
| `pushKey` | `String` | query/form | 是 | 新的pushKey |

### `POST /api/user/changePasswordForAdmin`

管理员修改普通用户密码。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `userId` | `int` | query/form | 是 | 用户id |
| `password` | `String` | query/form | 是 | 新密码（未md5加密的密码） |

### `POST /api/user/userInfo`

查询当前登录用户信息。源码返回类型：`LoginUser`。

无业务请求参数。

## 第三方接口（隐藏或内部接口）（需启用配置）（CameraChannelController）

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/CameraChannelController.java`

### `GET /api/sy/camera/list`

查询摄像机列表, 只查询当前虚拟组织下的。源码返回类型：`PageInfo<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 否 | 当前页 |
| `count` | `Integer` | query/form | 否 | 每页查询数量 |
| `groupAlias` | `String` | query/form | 未声明 | 分组别名 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |
| `status` | `Boolean` | query/form | 否 | 摄像头状态 |

### `GET /api/sy/camera/list-with-child`

查询摄像机列表, 查询当前虚拟组织下以及全部子节点。源码返回类型：`PageInfo<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 否 | 当前页 |
| `count` | `Integer` | query/form | 否 | 每页查询数量 |
| `query` | `String` | query/form | 否 | 查询内容 |
| `sortName` | `String` | query/form | 否 | 排序字段名 |
| `order` | `Boolean` | query/form | 否 | 排序方式（true: 升序 或 false: 降序 ） |
| `groupAlias` | `String` | query/form | 否 | 分组别名 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |
| `status` | `Boolean` | query/form | 否 | 摄像头状态 |

### `GET /api/sy/camera/cont-with-child`

查询摄像机列表的总数和在线数。源码返回类型：`List<CameraCount>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `groupAlias` | `String` | query/form | 未声明 | 分组别名 |

### `GET /api/sy/camera/one`

查询单个摄像头信息。源码返回类型：`CameraChannel`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 通道编号 |
| `deviceCode` | `String` | query/form | 否 | 摄像头设备国标编号, 对于非国标摄像头可以不设置此参数 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |

### `GET /api/sy/camera/update`

更新摄像头信息。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 通道编号 |
| `deviceCode` | `String` | query/form | 否 | 摄像头设备国标编号, 对于非国标摄像头可以不设置此参数 |
| `name` | `String` | query/form | 否 | 通道名称 |
| `longitude` | `Double` | query/form | 否 | 经度 |
| `latitude` | `Double` | query/form | 否 | 纬度 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |

### `POST /api/sy/camera/list/ids`

根据编号查询多个摄像头信息。源码返回类型：`List<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `IdsQueryParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/sy/camera/list/box`

根据矩形查询摄像头。源码返回类型：`List<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `minLongitude` | `Double` | query/form | 未声明 | 最小经度 |
| `maxLongitude` | `Double` | query/form | 未声明 | 最大经度 |
| `minLatitude` | `Double` | query/form | 未声明 | 最小纬度 |
| `maxLatitude` | `Double` | query/form | 未声明 | 最大纬度 |
| `level` | `Integer` | query/form | 否 | 地图级别 |
| `groupAlias` | `String` | query/form | 未声明 | 分组别名 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |

### `POST /api/sy/camera/list/polygon`

根据多边形查询摄像头。源码返回类型：`List<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `param` | `PolygonQueryParam` | JSON body | 是 | 请求体模型；字段见模型索引 |

### `GET /api/sy/camera/list/circle`

根据圆范围查询摄像头。源码返回类型：`List<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `centerLongitude` | `Double` | query/form | 未声明 | 圆心经度 |
| `centerLatitude` | `Double` | query/form | 未声明 | 圆心纬度 |
| `radius` | `Double` | query/form | 未声明 | 查询范围的半径，单位米 |
| `groupAlias` | `String` | query/form | 未声明 | 分组别名 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |
| `level` | `Integer` | query/form | 否 | 地图级别 |

### `GET /api/sy/camera/list/address`

根据安装地址和监视方位获取摄像头。源码返回类型：`List<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `address` | `String` | query/form | 未声明 | 安装地址 |
| `directionType` | `Integer` | query/form | 否 | 监视方位 |
| `geoCoordSys` | `String` | query/form | 否 | 坐标系类型：WGS84,GCJ02、BD09 |

### `GET /api/sy/camera/control/play`

播放摄像头。源码返回类型：`DeferredResult<WVPResult<CameraStreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 通道编号 |
| `deviceCode` | `String` | query/form | 否 | 摄像头设备国标编号, 对于非国标摄像头可以不设置此参数 |

### `GET /api/sy/camera/control/stop`

停止播放摄像头。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 通道编号 |
| `deviceCode` | `String` | query/form | 否 | 摄像头设备国标编号, 对于非国标摄像头可以不设置此参数 |

### `GET /api/sy/camera/control/ptz`

云台控制。源码返回类型：`DeferredResult<WVPResult<String>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `deviceId` | `String` | query/form | 未声明 | 通道编号 |
| `deviceCode` | `String` | query/form | 否 | 摄像头设备国标编号, 对于非国标摄像头可以不设置此参数 |
| `command` | `String` | query/form | 是 | 控制指令,允许值: left, right, up, down, upleft, upright, downleft, downright, zoomin, zoomout, stop |
| `speed` | `Integer` | query/form | 是 | 速度(0-100) |

### `GET /api/sy/camera/list-for-mobile`

查询移动设备摄像机列表。源码返回类型：`PageInfo<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `page` | `Integer` | query/form | 否 | 当前页 |
| `count` | `Integer` | query/form | 否 | 每页查询数量 |
| `topGroupAlias` | `String` | query/form | 否 | 分组别名 |

### `GET /api/sy/push/play`

获取推流播放地址。源码返回类型：`DeferredResult<WVPResult<StreamContent>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流id |
| `callId` | `String` | query/form | 是 | 推流时携带的自定义鉴权ID |

### `GET /api/sy/push/play-without-check`

获取推流播放地址（不做检查）。源码返回类型：`StreamContent`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 是 | 应用名 |
| `stream` | `String` | query/form | 是 | 流id |
| `callId` | `String` | query/form | 是 | 推流时携带的自定义鉴权ID |

### `GET /api/sy/record/collect/add`

添加收藏。源码返回类型：`int`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |
| `startTime` | `String` | query/form | 否 | 鉴权ID |
| `endTime` | `String` | query/form | 否 | 鉴权ID |
| `callId` | `String` | query/form | 否 | 鉴权ID |
| `recordId` | `Integer` | query/form | 否 | 录像记录的ID，用于精准收藏一个视频文件 |

### `GET /api/sy/record/collect/delete`

移除收藏。源码返回类型：`int`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID |
| `startTime` | `String` | query/form | 否 | 鉴权ID |
| `endTime` | `String` | query/form | 否 | 鉴权ID |
| `callId` | `String` | query/form | 否 | 鉴权ID |
| `recordId` | `Integer` | query/form | 否 | 录像记录的ID，用于精准精准移除一个视频文件的收藏 |

### `GET /api/sy/record/zip`

downloadZipFile。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 否 | 流应用名 |
| `stream` | `String` | query/form | 否 | 流 ID |
| `callId` | `String` | query/form | 否 | 本次 SIP 会话或录像的唯一标识 |

### `GET /api/sy/record/list-url`

分页查询云端录像。源码返回类型：`PageInfo<CloudRecordUrl>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `query` | `String` | query/form | 否 | 检索内容 |
| `app` | `String` | query/form | 否 | 应用名 |
| `stream` | `String` | query/form | 否 | 流ID |
| `page` | `int` | query/form | 是 | 当前页 |
| `count` | `int` | query/form | 是 | 每页查询数量 |
| `startTime` | `String` | query/form | 否 | 开始时间(yyyy-MM-dd HH:mm:ss) |
| `endTime` | `String` | query/form | 否 | 结束时间(yyyy-MM-dd HH:mm:ss) |
| `mediaServerId` | `String` | query/form | 否 | 流媒体ID，置空则查询全部流媒体 |
| `callId` | `String` | query/form | 否 | 每次录像的唯一标识，置空则查询全部流媒体 |
| `remoteHost` | `String` | query/form | 否 | 外部可访问的完整媒体服务地址，含协议、主机和端口 |

### `GET /api/sy/forceClose`

强制停止推流。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `app` | `String` | query/form | 未声明 | 流应用名 |
| `stream` | `String` | query/form | 未声明 | 流 ID |

### `GET /api/sy/camera/meeting/list`

查询会议设备。源码返回类型：`List<CameraChannel>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `topGroupAlias` | `String` | query/form | 未声明 | 分组别名 |

### `GET /api/sy/test`

test。源码返回类型：`SYMember`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `device` | `String` | query/form | 未声明 | 设备对象；字段见模型索引 |

## ApiControlController（隐藏或内部接口）（ApiControlController）

源码：`src/main/java/com/genersoft/iot/vmp/web/gb28181/ApiControlController.java`

### `GET /api/v1/control/ptz`

ptz。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `command` | `String` | query/form | 未声明 | 控制命令，取值见对应接口说明 |
| `channel` | `Integer` | query/form | 否 | 通道对象；字段见模型索引 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `speed` | `Integer` | query/form | 否 | 控制速度，范围见对应接口 |

### `GET /api/v1/control/preset`

list。源码返回类型：`void`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `command` | `String` | query/form | 未声明 | 控制命令，取值见对应接口说明 |
| `channel` | `Integer` | query/form | 否 | 通道对象；字段见模型索引 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `name` | `String` | query/form | 否 | 名称 |
| `preset` | `Integer` | query/form | 否 | 预置位对象 |

## ApiController（隐藏或内部接口）（ApiController）

源码：`src/main/java/com/genersoft/iot/vmp/web/gb28181/ApiController.java`

### `GET /api/v1/getserverinfo`

getserverinfo。源码返回类型：`JSONObject`。

无业务请求参数。

### `GET /api/v1/userinfo`

userinfo。源码返回类型：`JSONObject`。

无业务请求参数。

### `GET /api/v1/login`

login。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `username` | `String` | query/form | 未声明 | 用户名 |
| `password` | `String` | query/form | 未声明 | 密码；登录为 32 位 MD5，管理接口按各接口说明传值 |

## ApiDeviceController（隐藏或内部接口）（ApiDeviceController）

源码：`src/main/java/com/genersoft/iot/vmp/web/gb28181/ApiDeviceController.java`

### `GET /api/v1/device/list`

list。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `start` | `Integer` | query/form | 否 | 分页起始偏移量 |
| `limit` | `Integer` | query/form | 否 | 分页数量上限 |
| `q` | `String` | query/form | 否 | 搜索关键字 |
| `online` | `Boolean` | query/form | 否 | 在线状态过滤，true 为在线 |

### `GET /api/v1/device/channellist`

channellist。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `channel_type` | `String` | query/form | 否 | 通道类型过滤 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `dir_serial` | `String` | query/form | 否 | 目录节点序列号 |
| `start` | `Integer` | query/form | 否 | 分页起始偏移量 |
| `limit` | `Integer` | query/form | 否 | 分页数量上限 |
| `q` | `String` | query/form | 否 | 搜索关键字 |
| `online` | `Boolean` | query/form | 否 | 在线状态过滤，true 为在线 |

### `GET /api/v1/device/fetchpreset`

list。源码返回类型：`DeferredResult<WVPResult<Object>>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `channel` | `Integer` | query/form | 否 | 通道对象；字段见模型索引 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `fill` | `Boolean` | query/form | 否 | 是否填充地图区域 |
| `timeout` | `Integer` | query/form | 否 | 超时时间；单位见接口实现 |

## ApiStreamController（隐藏或内部接口）（ApiStreamController）

源码：`src/main/java/com/genersoft/iot/vmp/web/gb28181/ApiStreamController.java`

### `GET /api/v1/stream/start`

start。源码返回类型：`DeferredResult<JSONObject>`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `channel` | `Integer` | query/form | 否 | 通道对象；字段见模型索引 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `cdn` | `String` | query/form | 否 | CDN 相关选项 |
| `audio` | `String` | query/form | 否 | 是否启用音频 |
| `transport` | `String` | query/form | 否 | 传输协议 |
| `checkchannelstatus` | `String` | query/form | 否 | 是否检查通道状态 |
| `transportmode` | `String` | query/form | 否 | 媒体传输模式 |
| `timeout` | `String` | query/form | 否 | 超时时间；单位见接口实现 |

### `GET /api/v1/stream/stop`

stop。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `channel` | `Integer` | query/form | 否 | 通道对象；字段见模型索引 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `check_outputs` | `String` | query/form | 否 | 是否检查输出状态 |

### `GET /api/v1/stream/touch`

touch。源码返回类型：`JSONObject`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `serial` | `String` | query/form | 未声明 | 设备或通道序列号 |
| `t` | `String` | query/form | 未声明 | 时间或防缓存参数，见接口实现 |
| `channel` | `Integer` | query/form | 否 | 通道对象；字段见模型索引 |
| `code` | `String` | query/form | 否 | 业务编码或验证码；具体格式见对应接口实现 |
| `autorestart` | `String` | query/form | 否 | 是否自动重启 |
| `audio` | `String` | query/form | 否 | 是否启用音频 |
| `cdn` | `String` | query/form | 否 | CDN 相关选项 |

## AuthController（隐藏或内部接口）（AuthController）

源码：`src/main/java/com/genersoft/iot/vmp/web/gb28181/AuthController.java`

### `GET /auth/login`

devices。源码返回类型：`String`。

| 参数 | Java 类型 | 位置 | 必填 | 说明 |
|---|---|---|---|---|
| `name` | `String` | query/form | 未声明 | 名称 |
| `passwd` | `String` | query/form | 未声明 | 认证密码；请勿记录明文 |

共收录 410 条路由映射，来自 37 个 Controller。
