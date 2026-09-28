# 武汉节点 视频流对接说明（RTSP / MP4）

> 此页仅记录特定现场信息；通用接口与字段以[系统完整接口文档](第三方对接接口手册.md)为准。

对接服务器：`27.10.170.1`  
文档版本：V2.1  
编制日期：2026-09-21

内容：**视频流地址路径**、**鉴权方式与 API Key**、**RTSP / MP4 对接**、**云台与预置位接口**。

## 1. 地址一览

| 协议 | 地址格式 | 需要鉴权 |
|---|---|---|
| RTSP | `rtsp://27.10.170.1:5540/{app}/{stream}` | 否 |
| RTMP | `rtmp://27.10.170.1:10935/{app}/{stream}` | 否 |
| HTTP-FLV | `http://27.10.170.1:18080/rtp/{stream}.live.flv` | 否 |
| MP4（FMP4） | `http://27.10.170.1:18082/rtp/{stream}.live.mp4` | 否 |
| HLS | `http://27.10.170.1:18082/rtp/{stream}/hls.m3u8` | 否 |
| TS | `http://27.10.170.1:18082/rtp/{stream}.live.ts` | 否 |
| WebRTC | `http://27.10.170.1:18082/index/api/webrtc?app=rtp&stream={stream}&type=play` | 否 |
| 取地址等 HTTP 接口 | `http://27.10.170.1:18080` | 见第 4 章 |

说明：

1. **武汉节点的设备（下级平台）编号统一为 `42010000012005000001`**，本文所有 `deviceId` 都填它；
   通道编号由查询接口获取，本文示例用 `42010000001320000052`。
2. 国标视频的 `{app}` 固定为 `rtp`；`{stream}` 请**使用接口返回值**，不要自行拼接。
3. HTTP-FLV 必须用 `app=rtp`；MP4/HLS/TS 不受此限制。
4. 播放地址本身不需要登录。仅当该路流是我方用带 `callId` 的推流地址推上来时，播放需要带相同的
   `?callId=xxx`（国标视频不会出现这种情况）。

## 2. RTSP 对接

### 2.1 贵方从我方取 RTSP 流

1. 调用取地址接口（国标通道）：

   ```http
   GET http://27.10.170.1:18080/api/play/start/42010000012005000001/42010000001320000052
   ```

   返回 `data.rtsp`，形如：

   ```text
   rtsp://27.10.170.1:5540/rtp/42010000012005000001_42010000001320000052
   ```

2. 用该地址播放（建议 RTSP over TCP）：

   ```bash
   ffplay -rtsp_transport tcp "rtsp://27.10.170.1:5540/rtp/<stream>"
   ```

3. 播放结束调用停止接口释放资源：

   ```http
   GET http://27.10.170.1:18080/api/play/stop/42010000012005000001/42010000001320000052
   ```

`deviceId` 为下级平台/设备的 20 位国标编号，**武汉节点为 `42010000012005000001`**；
`channelId` 为通道 20 位国标编号（示例用 `42010000001320000052`，请以查询结果为准）；
也可先查通道清单：

```http
GET http://27.10.170.1:18080/api/device/query/devices/42010000012005000001/channels?page=1&count=100&resourceType=CHANNEL
```

**鉴权**：`/api/play/start/**`、`/api/play/stop/**`、`/api/device/query/devices/**` 在本节点已放行，
不需要 token；RTSP 播放地址本身也不需要鉴权。

### 2.2 我方从贵方 RTSP 源拉流

贵方只需提供一条对我方 `27.10.170.1` 可达的 RTSP 源地址：

```text
rtsp://用户名:密码@源IP:端口/路径
例：rtsp://admin:密码@10.1.1.10:554/Streaming/Channels/101
```

要求：视频 H.264（建议子码流）；同时提供通道名称、用途。我方配置完成后，贵方可用第 1 章
的地址取流，取流方式同上（`/api/proxy/start?id={代理ID}` 返回的 `rtsp`/`fmp4` 字段，需鉴权，由我方调用）。

### 2.3 贵方推 RTSP 流到我方

推流地址由我方在后台生成，格式：

```text
rtsp://27.10.170.1:5540/{app}/{stream}?sign={签名}
```

**鉴权**：必须带 `sign`，无签名会被拒绝；`sign` 不可自行计算，请使用我方提供的完整地址。

```bash
ffmpeg -rtsp_transport tcp -i "rtsp://源地址" -c copy -f rtsp \
  "rtsp://27.10.170.1:5540/rtp/wh_push01?sign=由我方提供的签名"
```

## 3. MP4 对接

### 3.1 取 MP4（FMP4）播放地址

与 RTSP 使用同一套取地址方式，从接口返回中直接取字段：

| 想用的协议 | 取返回字段 | 地址示例 |
|---|---|---|
| MP4（FMP4） | `fmp4` | `http://27.10.170.1:18082/rtp/<stream>.live.mp4` |
| HLS | `hls` | `http://27.10.170.1:18082/rtp/<stream>/hls.m3u8` |
| TS | `ts` | `http://27.10.170.1:18082/rtp/<stream>.live.ts` |
| HTTP-FLV | `flv` | `http://27.10.170.1:18080/rtp/<stream>.live.flv` |

**鉴权**：不需要。

### 3.2 贵方提供 MP4 文件

由我方把 MP4 直链接成一路视频（FFmpeg 拉转），贵方只需提供：

1. 服务器 `27.10.170.1` 可直接 `GET` 的 `http://` 或 `https://` 直链（返回 200/206，无需登录态）；
2. 编码 H.264 + AAC 的标准 MP4 文件。

行为说明：按 1 倍速播放，文件播完该路流结束；多客户端共享同一进度，**不支持各自拖动进度条**。
需要回看/拖动请使用 3.3。

### 3.3 取录像 MP4（需要鉴权）

| 用途 | 接口 |
|---|---|
| 查询录像并带播放地址 | `GET /api/cloud/record/list-url?page=1&count=20&app=rtp&stream={stream}&remoteHost=http%3A%2F%2F27.10.170.1%3A18082` |
| 单个录像播放地址 | `GET /api/cloud/record/play/path?recordId={录像ID}` |
| 打包下载 | `GET /api/cloud/record/download/zip?ids={id1},{id2}` |

**鉴权**：需要，见第 4 章。

## 4. 鉴权

### 4.1 鉴权一览

| 用途 | 是否需要鉴权 | 方式 |
|---|---|---|
| 播放地址本身（RTSP 5540 / RTMP 10935 / FLV、MP4、HLS、TS 18080、18082） | 否 | — |
| 查询设备、通道 `/api/device/query/devices/**` | 否（本节点已放行） | — |
| 国标点播取地址 `/api/play/start/**`、停播 `/api/play/stop/**` | 否 | — |
| 国标云台 `/api/front-end/ptz/**` | 否 | — |
| 统一通道接口 `/api/common/channel/**` | 是 | `access-token` 或 `api-key` |
| 拉流代理 `/api/proxy/**` | 是 | 同上（由我方操作） |
| 云端录像 `/api/cloud/record/**` | 是 | 同上 |
| 推流地址 `?sign=` | 需要签名 | 由我方生成 |
| 其它 `/api/**` 写操作 | 是 | 需管理员/操作员角色 |

### 4.2 API Key（由管理员单独发放）

请求头名固定为 `api-key`（**只支持请求头，不能放在 URL 参数里**）。

```bash
API_KEY='<由管理员单独发放的API Key>'

curl -sS "http://27.10.170.1:18080/api/cloud/record/list-url?page=1&count=20&app=rtp&stream=<stream>&remoteHost=http%3A%2F%2F27.10.170.1%3A18082" \
  -H "api-key: $API_KEY"
```

应按对接方分别申请独立、最小权限的 Key，并通过安全渠道发放；到期时间以管理员创建时的设置为准。

### 4.3 登录令牌 access-token（可选）

不想用长期 Key 时，可登录获取 30 分钟有效的令牌：

```bash
curl -sS -X POST "http://27.10.170.1:18080/api/user/login" \
  -H 'Content-Type: application/x-www-form-urlencoded' \
  --data-urlencode "username=<用户名>" \
  --data-urlencode "password=<32位小写MD5>"
```

返回 `data.accessToken`，后续请求带请求头 `access-token: <token>`；也可放在 URL 参数里
`?access-token=<token>`（浏览器直接下载录像 ZIP 时用这种方式，因为 `api-key` 不支持 URL 参数）。

### 4.4 调用示例

```bash
API_KEY='<由管理员单独发放的API Key>'

# 国标点播取地址（无需鉴权）
curl -sS "http://27.10.170.1:18080/api/play/start/42010000012005000001/42010000001320000052"

# 查询统一通道列表（需要鉴权）
curl -sS "http://27.10.170.1:18080/api/common/channel/list?page=1&count=20&channelType=0" \
  -H "api-key: $API_KEY"

# 取录像播放地址（需要鉴权）
curl -sS "http://27.10.170.1:18080/api/cloud/record/list-url?page=1&count=20&app=rtp&stream=<stream>&remoteHost=http%3A%2F%2F27.10.170.1%3A18082" \
  -H "api-key: $API_KEY"
```

> 标记“已放行”的接口仅向我方约定的来源 IP 开放，请勿对外扩散地址。

## 5. 云台与预置位接口

预置位共有两套接口：**直连国标通道**（用 20 位国标编号）和**统一通道**（用数据库整数 ID）。
预置位接口需要鉴权（见 4.2/4.3），其中 `add`/`call`/`delete` 三个写动作还需要管理员/操作员角色。

### 5.1 直连国标通道（20 位编号，推荐）

（`deviceId` 固定填 `42010000012005000001`，`channelId` 用通道国标编号）

| 用途 | 接口 | 参数 |
|---|---|---|
| 查询预置位 | `GET /api/front-end/preset/query/{deviceId}/{channelId}` | 无，`data` 为 `[{presetId,presetName}]` |
| 设置预置位 | `GET /api/front-end/preset/add/{deviceId}/{channelId}?presetId=1` | `presetId` 1-255 |
| 调用预置位 | `GET /api/front-end/preset/call/{deviceId}/{channelId}?presetId=1` | `presetId` 1-255 |
| 删除预置位 | `GET /api/front-end/preset/delete/{deviceId}/{channelId}?presetId=1` | `presetId` 1-255 |

```bash
API_KEY='<由管理员单独发放的API Key>'

# 调用 1 号预置位
curl -sS "http://27.10.170.1:18080/api/front-end/preset/call/42010000012005000001/42010000001320000052?presetId=1" \
  -H "api-key: $API_KEY"

# 查询该通道已有预置位
curl -sS "http://27.10.170.1:18080/api/front-end/preset/query/42010000012005000001/42010000001320000052" \
  -H "api-key: $API_KEY"
```

### 5.2 统一通道（整数 channelId）

| 用途 | 接口 | 参数 |
|---|---|---|
| 查询预置位 | `GET /api/common/channel/front-end/preset/query?channelId=123` | `channelId` 为数据库整数 ID，不是国标编号 |
| 设置预置位 | `GET /api/common/channel/front-end/preset/add?channelId=123&presetId=1&presetName=大门` | `presetName` 必填 |
| 调用预置位 | `GET /api/common/channel/front-end/preset/call?channelId=123&presetId=1` | — |
| 删除预置位 | `GET /api/common/channel/front-end/preset/delete?channelId=123&presetId=1` | — |

### 5.3 说明

1. 返回统一为 `{"code":0,"msg":"成功","data":...}`，`code` 非 0 时把 `msg` 展示给用户即可。
2. 预置位下发是异步指令，返回成功表示指令已发出，是否到位以画面为准。
3. 云台方向/变倍接口 `/api/front-end/ptz/{deviceId}/{channelId}?command=left|right|up|down|zoomin|zoomout|stop&horizonSpeed=0-255&verticalSpeed=0-255&zoomSpeed=0-15`
   **不需要鉴权**；预置位接口与它不同，必须带 token 或 api-key。
4. 设置、调用、删除接口的 `presetId` 均须为 `1-255` 的十进制整数（设备侧可能支持更小范围）。查询结果中的 `presetId` 是设备原样返回的字符串，可能是 `PresetID_1` 等非数字值；这种值不能直接用于控制接口，应先确认设备编号映射，不要自动截取末尾数字。

> 对接方需要云台方向控制时，请使用“按下方向键 → 松开必须发 `stop`”的方式，避免镜头持续转动。

## 6. 视频列表查询接口

以下均为 GET 接口。分页列表的 `data` 为 `{list, pageNum, pageSize, total, pages}`；单条查询的 `data` 是对象，日期等接口返回数组。各返回字段见[对接接口详解](_content/api/integration.md)。
鉴权统一用请求头 `api-key`（见 4.2），只有标注“免鉴权”的接口不需要。

### 6.1 国标设备与通道（`/api/device/query`）

| 用途 | 接口 | 参数 |
|---|---|---|
| 分页查询设备/下级平台 | `GET /api/device/query/devices` | `page`、`count` 必填；`query`、`status`、`accessType=DEVICE|PLATFORM` 可选（免鉴权） |
| 查询单个设备 | `GET /api/device/query/devices/{deviceId}` | 路径参数 `deviceId` 为设备/平台国标编号（免鉴权） |
| 查询设备下的通道 | `GET /api/device/query/devices/{deviceId}/channels` | `page`、`count` 必填；`query`、`online`、`resourceType=CHANNEL|DIRECTORY`、`channelType` 可选（免鉴权） |
| 查询子目录下的通道 | `GET /api/device/query/sub_channels/{deviceId}/{channelId}/channels` | 路径 `deviceId` 为设备国标编号、`channelId` 为父目录国标编号；`page`、`count` 必填；`query`、`online`、`channelType`、`resourceType` 可选 |
| 查询单个通道详情 | `GET /api/device/query/channel/one?deviceId=&channelDeviceId=` | 设备编号 + 通道国标编号 |

### 6.2 统一通道（`/api/common/channel`，推荐）

统一通道把国标设备、推流、拉流代理汇总到一张表，返回的 `gbId` 是**整数**，用于
`/api/common/channel/*` 的播放、云台、预置位接口；`gbDeviceId` 才是国标编号。

| 用途 | 接口 | 参数 |
|---|---|---|
| 分页查询通道列表 | `GET /api/common/channel/list` | `page`、`count` 必填；`query`、`online`、`channelType`（0 国标 / 1 推流 / 2 拉流代理）、`civilCode`、`parentDeviceId`、`hasRecordPlan` |
| 查询单个通道 | `GET /api/common/channel/one?id={整数gbId}` | 通道数据库 ID |
| 按行政区查询 | `GET /api/common/channel/civilcode/list` | `page`、`count`；`query`、`online`、`channelType`、`civilCode` |
| 按业务分组查询 | `GET /api/common/channel/parent/list` | `page`、`count`；`query`、`online`、`channelType`、`groupDeviceId` |

### 6.3 推流与拉流代理

| 用途 | 接口 | 参数 |
|---|---|---|
| 推流列表 | `GET /api/push/list` | `page`、`count`；`query`、`pushing`、`mediaServerId` |
| 拉流代理列表 | `GET /api/proxy/list` | `page`、`count`；`query`、`pulling`、`mediaServerId` |
| 查询单个拉流代理 | `GET /api/proxy/one?app=&stream=` | 应用名 + 流 ID |

### 6.4 录像查询

| 用途 | 接口 | 参数 |
|---|---|---|
| 有录像的日期 | `GET /api/cloud/record/date/list` | `app`、`stream` 必填；`year`、`month` 可选（数组） |
| 云端录像列表 | `GET /api/cloud/record/list` | `page`、`count` 必填；`query`、`app`、`stream`、`startTime`、`endTime`、`mediaServerId`、`callId`、`ascOrder` |
| 云端录像列表（带播放地址） | `GET /api/cloud/record/list-url` | 同上，另加 `remoteHost`（如 `http://27.10.170.1:18082`，传入 URL 时需编码） |
| 设备侧录像查询（国标） | `GET /api/common/channel/playback/query` | `channelId`（整数）、`startTime`、`endTime`，时间格式 `yyyy-MM-dd HH:mm:ss` |

### 6.5 组织结构与上级平台（可选）

| 用途 | 接口 | 参数 |
|---|---|---|
| 行政区树 | `GET /api/region/tree/list` | `parent`、`hasChannel`（数组） |
| 行政区列表 | `GET /api/region/page/list` | `page`、`count` 必填；`query` 可选 |
| 上级平台列表 | `GET /api/platform/query` | `page`、`count`；`query` |
| 已共享给上级平台的通道 | `GET /api/platform/channel/list` | `page`、`count`、`platformId` 必填；`query`、`channelType`、`online`、`hasShare` |

### 6.6 典型调用

```bash
API_KEY='<由管理员单独发放的API Key>'

# 1) 查下级平台 / 设备（免鉴权）
curl -sS "http://27.10.170.1:18080/api/device/query/devices?page=1&count=50&accessType=PLATFORM"

# 2) 查某设备下的视频通道（免鉴权；只要通道，不要目录）
curl -sS "http://27.10.170.1:18080/api/device/query/devices/42010000012005000001/channels?page=1&count=100&resourceType=CHANNEL&online=true"

# 3) 统一通道列表（拿到整数 gbId，后续播放/云台/预置位都用它）
curl -sS "http://27.10.170.1:18080/api/common/channel/list?page=1&count=100&channelType=0&online=true" \
  -H "api-key: $API_KEY"

# 4) 云端录像列表（带可播放地址）
curl -sS "http://27.10.170.1:18080/api/cloud/record/list-url?page=1&count=20&app=rtp&stream=<stream>&remoteHost=http%3A%2F%2F27.10.170.1%3A18082" \
  -H "api-key: $API_KEY"
```


完整参数、返回字段与错误码说明见[对接接口详解](_content/api/integration.md)；全项目路由见[HTTP 接口索引](_content/api/reference.md)。
