# 对接接口详解

> 历史资料。系统全部 HTTP 接口及第三方对接请使用持续维护的[系统完整接口文档](../../第三方对接接口手册.md)。

本文按当前源码说明外部系统最常用的 HTTP 接口。全项目每条路由的参数见[HTTP 接口索引](reference.md)，请求体及返回模型字段见[模型字段索引](models.md)。接口地址以部署环境的 Web/API 根地址为准，示例中的编号、端口均需换成现场值。

## 1. 请求、鉴权与返回约定

| 项目 | 约定 |
|---|---|
| 根地址 | `http(s)://<WVP主机>:<Web端口>`；下文路径均相对于根地址 |
| 请求体 | 标为 `@RequestBody` 的接口使用 `Content-Type: application/json`；其他 POST 参数通常使用表单或查询参数 |
| JWT | 登录后使用请求头 `access-token: <JWT>`；也支持同名 URL 参数，WebSocket 使用 `Sec-WebSocket-Protocol` |
| API Key | 服务调用使用请求头 `api-key: <API_KEY>`；该凭证不能放在 URL 参数中 |
| 权限 | 默认读取需登录；POST/PUT/PATCH/DELETE 和具有写动作的 GET 需 `ADMIN` 或 `OPERATOR`；用户、角色、日志、API Key 管理等需 `ADMIN` |
| 现场放行 | `user-settings.interface-authentication-excludes` 可以针对单个部署放行部分路径。武汉部署的放行清单见其节点文档，不能推广到其他部署 |

常规 JSON 响应为 `WVPResult<T>`：

```json
{"code":0,"msg":"成功","data":{}}
```

| 字段 | 类型 | 含义 |
|---|---|---|
| `code` | 整数 | `0` 成功；`100` 通用失败；`400` 参数或方法错误；`401` 未登录；`403` 无权限；`404` 资源不存在；`408` 请求超时；`486` 超时或无响应；`500` 系统异常。具体错误也可能由设备或媒体服务返回 |
| `msg` | 字符串 | 结果或失败原因，失败时优先展示此字段 |
| `data` | 对象、数组、字符串或 `null` | 业务数据。`void` 接口通常为 `null` |

HTTP 状态与业务 `code` 要分别检查：部分业务失败仍返回 HTTP 200；认证失败可能返回 HTTP 401/403。文件下载、媒体流、内部 Hook、兼容接口 `/api/v1/**` 等可能不走此包装。

分页 `data` 通常是 PageHelper 的 `PageInfo<T>`：

| 字段 | 类型 | 含义 |
|---|---|---|
| `list` | `T[]` | 当前页数据，空页为空数组 |
| `pageNum` | 整数 | 当前页码，从 1 开始 |
| `pageSize` | 整数 | 每页数量 |
| `total` | 整数 | 符合条件的总记录数 |
| `pages` | 整数 | 总页数 |
| `size` | 整数 | 本页实际记录数 |
| `hasNextPage`、`hasPreviousPage` | 布尔值 | 是否可翻页 |

### 1.1 登录 `GET/POST /api/user/login`

| 参数 | 位置 | 必填 | 说明 |
|---|---|---|---|
| `username` | 查询/表单 | 是 | WVP 用户名 |
| `password` | 查询/表单 | 是 | 该用户密码的 32 位 MD5 字符串；登录接口不接收明文密码 |

成功时从 `data.accessToken` 或响应头 `access-token` 取令牌。不要在日志或文档中保存真实密码、JWT 或 API Key。

```bash
curl -X POST 'http://<WVP主机>:<Web端口>/api/user/login' \
  --data-urlencode 'username=<用户名>' \
  --data-urlencode 'password=<32位MD5>'
```

### 1.2 API Key 管理（管理员）

| 方法与路径 | 参数 | 说明 |
|---|---|---|
| `POST /api/userApiKey/add` | `userId` 必填；`app`、`remark`、`expiresAt`、`enable` 可选 | `expiresAt` 格式 `yyyy-MM-dd HH:mm:ss`，不传为不过期；`enable` 不传时默认为 `false`，创建后需要启用 |
| `GET /api/userApiKey/userApiKeys` | `page`、`count` 必填；`userId` 可选 | 分页查询。返回对象包含凭证字段，调用方应限制展示和传播 |
| `POST /api/userApiKey/enable`、`/disable` | `id` 必填 | 启用或停用对应记录 |
| `POST /api/userApiKey/reset` | `id` 必填 | 重新签发，新的凭证在 `data` 中返回；调用方需更新保存值 |
| `POST /api/userApiKey/remark` | `id` 必填，`remark` 可选 | 更新备注 |
| `DELETE /api/userApiKey/delete` | `id` 必填 | 删除凭证 |

## 2. 编号与数据模型

WVP 有两种通道标识，不能混用：

| 标识 | 从哪里取得 | 用于 |
|---|---|---|
| 设备 `deviceId` | `/api/device/query/devices` 的 `deviceId` | `/api/device/query/**`、`/api/play/**`、`/api/front-end/**` 的设备路径参数 |
| 国标通道编号 | 设备通道列表的 `deviceId` | 直连点播、停播与云台接口的 `{channelId}` |
| 通用通道整数 ID | `/api/common/channel/list` 的 `gbId` | `/api/common/channel/**` 的 `channelId` 或 `/one?id=` |
| 通用通道国标编号 | 同一列表的 `gbDeviceId` | 业务展示、国标目录标识；不能传给通用接口的整数 `channelId` |

**设备 `Device` 常用返回字段**：`id` 为数据库 ID；`deviceId` 为国标编号；`name` 为名称；`onLine` 为布尔在线状态；`accessType` 为 `DEVICE`（独立设备）或 `PLATFORM`（下级平台）；`transport` 为 SIP 的 `UDP/TCP`；`streamMode` 为媒体传输模式；`ip`/`port` 为注册来源；`channelCount` 为通道数；`registerTimeStamp`、`keepaliveTimeStamp` 为时间戳；`mediaServerId` 为指定媒体节点。全部字段见[Device 模型](models.md#device国标设备平台)。

**设备通道 `DeviceChannel` 常用字段**：`id` 是原始设备通道数据库 ID；`deviceId` 是通道国标编号；`name` 是名称；`parentDeviceId`/`parentId` 是父节点标识；`status` 是设备上报状态；`channelType` 的 `0/1/2/3/4` 分别表示视频通道、行政区划、业务分组、平台节点、普通目录；派生字段 `resourceType` 对应 `CHANNEL/REGION/GROUP/PLATFORM/DIRECTORY`，`playable` 标示是否可点播。`ptzType` 表示摄像机结构类型；`longitude`/`latitude` 使用 WGS-84。继承的国标扩展字段见[DeviceChannel 模型](models.md#devicechannel通道信息)和[CommonGBChannel 模型](models.md#commongbchannel国标通道)。

**通用通道 `CommonGBChannel` 常用字段**：`gbId` 为通用接口使用的整数 ID；`gbDeviceId` 为国标编号；`gbName` 为名称；`gbStatus` 为国标状态；`gbPtzType` 为摄像机类型；`dataType` 为关联数据来源；`dataDeviceId` 为来源记录 ID；`streamId` 有值表示存在直播流。推流/代理类型由列表查询参数 `channelType` 的 `0/1/2` 区分。

## 3. 设备与通道查询

### 3.1 `GET /api/device/query/devices`

返回 `WVPResult<PageInfo<Device>>`，别名路径为 `/api/device/query/list`。

| 参数 | 必填 | 说明 |
|---|---|---|
| `page` | 是 | 页码，从 1 开始 |
| `count` | 是 | 每页数量 |
| `query` | 否 | 名称或编号检索；空字符串按不过滤处理 |
| `status` | 否 | 布尔在线状态过滤；返回对象中的字段名是 `onLine` |
| `accessType` | 否 | `DEVICE` 或 `PLATFORM`；其他值报错 |

`GET /api/device/query/devices/{deviceId}` 查询单个设备，无查询参数，返回 `Device` 或 `null`。

### 3.2 `GET /api/device/query/devices/{deviceId}/channels`

返回 `WVPResult<PageInfo<DeviceChannel>>`。

| 参数 | 必填 | 说明 |
|---|---|---|
| 路径 `deviceId` | 是 | 设备或下级平台国标编号 |
| `page`、`count` | 是 | 从 1 开始的页码及每页数量 |
| `query` | 否 | 通道名称或编号搜索 |
| `online` | 否 | 布尔在线过滤 |
| `channelType` | 否 | 旧版布尔过滤：`false` 视频设备、`true` 子目录；推荐用 `resourceType` |
| `resourceType` | 否 | `CHANNEL` 视频通道或 `DIRECTORY` 目录节点等资源类型；点播前使用 `CHANNEL` |

`GET /api/device/query/sub_channels/{deviceId}/{channelId}/channels` 用于查询子目录，`channelId` 是父目录的国标编号，其他查询参数相同。`GET /api/device/query/channel/one?deviceId=...&channelDeviceId=...` 返回单个原始通道。

### 3.3 `GET /api/common/channel/list`

返回 `WVPResult<PageInfo<CommonGBChannel>>`，后续通用点播/云台使用每项的 `gbId`。

| 参数 | 必填 | 说明 |
|---|---|---|
| `page`、`count` | 是 | 页码和每页数量 |
| `query` | 否 | 通道搜索文本 |
| `online` | 否 | 在线过滤 |
| `hasRecordPlan` | 否 | 是否已设置录制计划 |
| `channelType` | 否 | `0` 国标设备，`1` 推流设备，`2` 拉流代理 |
| `civilCode` | 否 | 行政区划编码 |
| `parentDeviceId` | 否 | 父节点编号 |

`GET /api/common/channel/one?id=<gbId>` 返回单个通用通道。`/civilcode/list` 与 `/parent/list` 是按行政区和父节点查询的变体，参数逐项见[接口索引](reference.md)。

## 4. 实时视频与云台

| 方法与路径 | 参数 | 成功数据 | 用途 |
|---|---|---|---|
| `GET /api/play/start/{deviceId}/{channelId}` | 两个路径参数均为国标编号 | `StreamContent` | 向国标设备点播；目录、离线通道不可播放 |
| `GET /api/play/stop/{deviceId}/{channelId}` | 与 start 相同 | `{deviceId,channelId}` | 关闭直连点播 |
| `GET /api/common/channel/play?channelId=<gbId>` | 整数 `gbId` | `StreamContent` | 统一通道点播 |
| `GET /api/common/channel/play/stop?channelId=<gbId>` | 整数 `gbId` | `null` | 关闭统一通道点播 |

`StreamContent` 返回字段：

| 字段 | 类型 | 说明 |
|---|---|---|
| `app`、`stream` | 字符串 | 流应用名和流 ID。调用方直接使用返回地址，不自行拼接流 ID |
| `flv`、`https_flv`、`ws_flv`、`wss_flv` | 字符串 URL | HTTP(S)/WebSocket FLV 播放地址 |
| `fmp4`、`https_fmp4`、`ws_fmp4`、`wss_fmp4` | 字符串 URL | FMP4 播放地址 |
| `hls`、`https_hls`、`ws_hls`、`wss_hls` | 字符串 URL | HLS 播放地址 |
| `ts`、`https_ts`、`ws_ts`、`wss_ts` | 字符串 URL | TS 播放地址 |
| `rtsp`、`rtsps`、`rtmp`、`rtmps`、`rtc`、`rtcs` | 字符串 URL | 其他协议播放地址 |
| `mediaServerId`、`serverId` | 字符串 | 流媒体节点和 WVP 服务节点 ID |
| `mediaInfo` | 对象 | 视频、音频编码信息；可用性依赖媒体服务探测结果 |
| `startTime`、`endTime`、`duration` | 字符串、字符串、数值 | 回放起止时间及时长；实时流可能为空 |
| `downLoadFilePath`、`progress` | 对象、数值 | 录像下载地址与进度；实时流通常不使用 |
| `transcodeStream` | `StreamContent` | 转码后的播放流，未转码时为空 |
| `key` | 字符串 | 拉流代理等操作使用的键，按具体接口保存 |

返回哪些 URL 取决于流媒体节点协议、端口及实际启用情况。关闭播放器、切换通道或离开页面时调用匹配的 stop 接口。

### 4.1 PTZ 方向与变倍

| 接口 | 标识 | 速度参数 |
|---|---|---|
| `GET /api/front-end/ptz/{deviceId}/{channelId}` | 两个国标编号 | `horizonSpeed`、`verticalSpeed` 为 `0–255`，`zoomSpeed` 为 `0–15` |
| `GET /api/common/channel/front-end/ptz?channelId=<gbId>` | 通用通道整数 ID | `panSpeed`、`tiltSpeed`、`zoomSpeed` 为 `0–100`，省略时各默认 `50` |

两套接口的 `command` 均为必填，允许 `left`、`right`、`up`、`down`、`upleft`、`upright`、`downleft`、`downright`、`zoomin`、`zoomout`、`stop`。按下方向键发送移动命令，松开时发送 `stop`。返回成功表示指令已处理或已下发，设备是否动作需结合画面确认。

预置位：直连路径 `/api/front-end/preset/query|add|call|delete/{deviceId}/{channelId}`；通用路径 `/api/common/channel/front-end/preset/query|add|call|delete?channelId=<gbId>`。`add/call/delete` 的 `presetId` 必须是 `1–255` 的十进制整数；通用 `add` 还需 `presetName`。查询结果中的 `presetId` 是设备原样返回的字符串，非数字值不能直接用于控制接口。所有动作实际使用 GET，但属于写操作，需相应角色。其他光圈、聚焦、巡航、扫描、看守位参数逐条见[接口索引](reference.md)。

## 5. 云端录像

| 方法与路径 | 参数 | 返回数据 |
|---|---|---|
| `GET /api/cloud/record/date/list` | `app`、`stream` 必填；`year`、`month`、`mediaServerId` 可选，年月不传用当前年月 | 存在录像的日期字符串数组 |
| `GET /api/cloud/record/list` | `page`、`count` 必填；`query`、`app`、`stream`、`startTime`、`endTime`、`mediaServerId`、`callId`、`ascOrder` 可选 | `PageInfo<CloudRecordItem>` |
| `GET /api/cloud/record/list-url` | 与 `list` 相同，但没有 `ascOrder`；另有可选 `remoteHost` | `PageInfo<CloudRecordUrl>` |
| `GET /api/cloud/record/play/path` | `recordId` 必填，为录像记录整数 ID | `DownloadFileInfo`，包含 `httpPath`、`httpsPath` 等 |
| `GET /api/cloud/record/download/zip` | `ids` 必填，为逗号分隔的录像 ID | ZIP 二进制文件 |

`startTime/endTime` 格式为 `yyyy-MM-dd HH:mm:ss`。`remoteHost` 必须是客户端可访问的完整协议、主机与端口，例如 `http://<媒体主机>:18082`；服务端直接在其后拼接 `/index/api/downloadFile`。`CloudRecordUrl` 的 `id` 为录像 ID，`playUrl` 为播放地址，`downloadUrl` 为下载地址；`filePath`、`fileName` 在当前实现中未赋值，不能依赖。`CloudRecordItem` 包含 `id/app/stream/callId/startTime/endTime/mediaServerId/fileName/filePath/folder/fileSize/timeLen/collect/reserve/serverId`，其中时间值由媒体节点上报，调用方应以返回值和现场录像校验单位。

## 6. 推荐调用顺序

1. 登录或配置专用 API Key。
2. 查询设备及通道，保留完整国标编号；仅对 `resourceType=CHANNEL` 且 `playable=true` 的通道点播。
3. 若使用通用接口，从通道列表取 `gbId`。
4. 调用点播接口，从 `data` 中读取客户端可用的播放 URL。
5. 控制云台时区分直连国标编号与通用整数 ID；方向键松开后发送 `stop`。
6. 关闭画面时调用对应停播接口，并同时检查 HTTP 状态、`code` 和 `msg`。

GB28181 SIP 平台级联参数和交互流程见[宇视平台级联对接资料](../../宇视GB28181平台级联对接信息-对外版.md)；武汉现场端口和放行策略见[武汉节点对接说明](../../武汉节点-视频流对接说明（RTSP-MP4）.md)。
