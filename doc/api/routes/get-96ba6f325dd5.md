# GET /api/jt1078/talk/start

[文档入口](../../第三方对接接口手册.md) · [所属模块](../modules/JT1078Controller.md) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | JT-语音对讲 |
| 请求方法 | `GET` |
| 请求路径 | `/api/jt1078/talk/start` |
| 认证方式 | access-token 或 api-key；ADMIN / OPERATOR；需启用对应配置 |
| Content-Type | 无请求体，无须 Content-Type |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/jt1078/controller/JT1078Controller.java` / `JT1078Controller` |
| 源码返回类型 | `StreamContent` |

#### 请求头

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `access-token` | Header | string | 二选一 | 有效JWT | 未传时须提供另一种凭证 | 有效JWT；与api-key二选一；权限见基本信息 | `"<access-token>"` |
| `api-key` | Header | string | 二选一 | 已启用的API Key | 未传时须提供另一种凭证 | 已启用的API Key；与access-token二选一；权限见基本信息 | `"<api-key>"` |

#### Path 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### Query 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `phoneNumber` | Query / Form | string | 是 | 源码未声明格式或范围 | 无；必须提供 | 设备手机号 | `"<phoneNumber>"` |
| `channelId` | Query / Form | integer | 是 | 整数 | 无；必须提供 | 通道国标编号, 一般为从1开始的数字 | `1` |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
GET /api/jt1078/talk/start?phoneNumber=<phoneNumber>&channelId=1 HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 业务结果，依接口返回类型变化 | `按接口示例` |
| `data.app` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 应用名（Java: String） | `按接口示例` |
| `data.stream` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 流ID（Java: String） | `按接口示例` |
| `data.ip` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | IP（Java: String） | `按接口示例` |
| `data.flv` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTP-FLV流地址（Java: String） | `按接口示例` |
| `data.https_flv` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTPS-FLV流地址（Java: String） | `按接口示例` |
| `data.ws_flv` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websocket-FLV流地址（Java: String） | `按接口示例` |
| `data.wss_flv` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websockets-FLV流地址（Java: String） | `按接口示例` |
| `data.fmp4` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTP-FMP4流地址（Java: String） | `按接口示例` |
| `data.https_fmp4` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTPS-FMP4流地址（Java: String） | `按接口示例` |
| `data.ws_fmp4` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websocket-FMP4流地址（Java: String） | `按接口示例` |
| `data.wss_fmp4` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websockets-FMP4流地址（Java: String） | `按接口示例` |
| `data.hls` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HLS流地址（Java: String） | `按接口示例` |
| `data.https_hls` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTPS-HLS流地址（Java: String） | `按接口示例` |
| `data.ws_hls` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websocket-HLS流地址（Java: String） | `按接口示例` |
| `data.wss_hls` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websockets-HLS流地址（Java: String） | `按接口示例` |
| `data.ts` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTP-TS流地址（Java: String） | `按接口示例` |
| `data.https_ts` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | HTTPS-TS流地址（Java: String） | `按接口示例` |
| `data.ws_ts` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websocket-TS流地址（Java: String） | `按接口示例` |
| `data.wss_ts` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | Websockets-TS流地址（Java: String） | `按接口示例` |
| `data.rtmp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTMP流地址（Java: String） | `按接口示例` |
| `data.rtmps` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTMPS流地址（Java: String） | `按接口示例` |
| `data.rtsp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTSP流地址（Java: String） | `按接口示例` |
| `data.rtsps` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTSPS流地址（Java: String） | `按接口示例` |
| `data.rtc` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTC流地址（Java: String） | `按接口示例` |
| `data.rtcs` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTCS流地址（Java: String） | `按接口示例` |
| `data.mediaServerId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 流媒体ID（Java: String） | `按接口示例` |
| `data.mediaInfo` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 流编码信息（Java: MediaInfo） | `按接口示例` |
| `data.startTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 开始时间（Java: String） | `按接口示例` |
| `data.endTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 结束时间（Java: String） | `按接口示例` |
| `data.duration` | number | 源码未声明可空约束；按实际响应处理 | 数值 | 时长(回放时使用)（Java: Double） | `按接口示例` |
| `data.downLoadFilePath` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 文件下载地址（录像下载使用）（Java: DownloadFileInfo） | `按接口示例` |
| `data.transcodeStream` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 转码后的视频流（Java: StreamContent） | `按接口示例` |
| `data.progress` | number | 源码未声明可空约束；按实际响应处理 | 数值 | 处理进度（Java: double） | `按接口示例` |
| `data.key` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 拉流代理返回的KEY（Java: String） | `按接口示例` |
| `data.serverId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 使用的WVP ID（Java: String） | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "app": "<app>",
    "stream": "<stream>",
    "ip": "<ip>",
    "flv": "<flv>"
  }
}
```

#### 错误响应

```json
{
  "code": 100,
  "msg": "失败：请检查具体错误说明",
  "data": null
}
```

#### 错误码

| code | msg | 说明 |
|---|---|---|
| 0 | 成功 | 业务成功 |
| 100 | 失败 | 通用业务失败，读取实际 `msg` |
| 400 | 参数或方法错误 | 参数不合法或调用方式错误 |
| 401 | 请登录后重新请求 | 身份失效或缺失 |
| 403 | 无权限操作 | 当前身份权限不足 |
| 404 | 资源未找到 | 设备、通道或记录不存在 |
| 408 / 486 | 超时或无响应 | 等待设备或媒体服务超时 |
| 500 | 系统异常 | 服务器内部异常；也可能返回 HTTP 500 |

#### 变更记录

| 版本 | 日期 | 说明 | 作者 |
|---|---|---|---|
| v1.0 | 2026-09-28 | 根据当前源码首次收录；具体业务规则以源码和部署配置为准 | 项目维护者（未指定个人） |

---

## 解析与实现约定

先核对本页请求位置与Content-Type；Query/Form不能自动替换成JSON。示例只展示形状，可能省略业务字段；必填以字段表与业务说明共同判断。

普通统一响应同时检查HTTP状态和code；data为null不等于失败。文件、流、兼容返回按本页响应类型解析。通用错误码表不是本接口必然返回的穷举集合。

控制或配置操作即使使用GET也会改变状态；网络超时表示结果未知，重试策略见[AI对接指南](../AI对接指南.md)。源码未声明的要求应向部署方确认。

## 关联模型

- [StreamContent](../models/StreamContent-3b954a65f1fc.md)：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/StreamContent.java`
