# GET /api/server/media_server/check

[文档入口](../../第三方对接接口手册.md) · [所属模块](../modules/ServerController.md) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 测试流媒体服务 |
| 请求方法 | `GET` |
| 请求路径 | `/api/server/media_server/check` |
| 认证方式 | access-token 或 api-key；已登录 |
| Content-Type | 无请求体，无须 Content-Type |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/vmanager/server/ServerController.java` / `ServerController` |
| 源码返回类型 | `MediaServer` |

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
| `ip` | Query / Form | string | 是 | 源码未声明格式或范围 | 无；必须提供 | 流媒体服务IP | `"<ip>"` |
| `port` | Query / Form | integer | 是 | 整数 | 无；必须提供 | 流媒体服务HTT端口 | `1` |
| `secret` | Query / Form | string | 是 | 源码未声明格式或范围 | 无；必须提供 | 流媒体服务secret | `"<secret>"` |
| `type` | Query / Form | string | 是 | 源码未声明格式或范围 | 无；必须提供 | 类型编码 | `"<type>"` |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
GET /api/server/media_server/check?ip=<ip>&port=1&secret=<secret>&type=<type> HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 业务结果，依接口返回类型变化 | `按接口示例` |
| `data.id` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | ID（Java: String） | `按接口示例` |
| `data.ip` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | IP（Java: String） | `按接口示例` |
| `data.hookIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | hook使用的IP（zlm访问WVP使用的IP）；Java初始值："127.0.0.1"（Java: String） | `按接口示例` |
| `data.sdpIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SDP IP（Java: String） | `按接口示例` |
| `data.streamIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 流IP（Java: String） | `按接口示例` |
| `data.httpPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | HTTP端口（Java: int） | `按接口示例` |
| `data.httpSSlPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | HTTPS端口（Java: int） | `按接口示例` |
| `data.rtmpPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | RTMP端口（Java: int） | `按接口示例` |
| `data.flvPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | flv端口（Java: int） | `按接口示例` |
| `data.flvSSLPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | https-flv端口（Java: int） | `按接口示例` |
| `data.mp4Port` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | mp4端口（Java: int） | `按接口示例` |
| `data.wsFlvPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | ws-flv端口（Java: int） | `按接口示例` |
| `data.wsFlvSSLPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | wss-flv端口（Java: int） | `按接口示例` |
| `data.rtmpSSlPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | RTMPS端口（Java: int） | `按接口示例` |
| `data.rtpProxyPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | RTP收流端口（单端口模式有用）（Java: int） | `按接口示例` |
| `data.jttProxyPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 1078收流端口（单端口模式有用）（Java: int） | `按接口示例` |
| `data.rtspPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | RTSP端口（Java: int） | `按接口示例` |
| `data.rtspSSLPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | RTSPS端口（Java: int） | `按接口示例` |
| `data.autoConfig` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否开启自动配置ZLM（Java: boolean） | `按接口示例` |
| `data.secret` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | ZLM鉴权参数（Java: String） | `按接口示例` |
| `data.hookAliveInterval` | number | 源码未声明可空约束；按实际响应处理 | 数值 | keepalive hook触发间隔,单位秒（Java: Float） | `按接口示例` |
| `data.rtpEnable` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否使用多端口模式（Java: boolean） | `按接口示例` |
| `data.status` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 状态（Java: boolean） | `按接口示例` |
| `data.rtpPortRange` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 多端口RTP收流端口范围（Java: String） | `按接口示例` |
| `data.sendRtpPortRange` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | RTP发流端口范围（Java: String） | `按接口示例` |
| `data.recordAssistPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | assist服务端口（Java: int） | `按接口示例` |
| `data.createTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 创建时间（Java: String） | `按接口示例` |
| `data.updateTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 更新时间（Java: String） | `按接口示例` |
| `data.lastKeepaliveTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 上次心跳时间（Java: String） | `按接口示例` |
| `data.defaultServer` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否是默认ZLM（Java: boolean） | `按接口示例` |
| `data.recordDay` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 录像存储时长（Java: int） | `按接口示例` |
| `data.recordPath` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 录像存储路径（Java: String） | `按接口示例` |
| `data.type` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 类型： zlm/abl（Java: String） | `按接口示例` |
| `data.transcodeSuffix` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 转码的前缀（Java: String） | `按接口示例` |
| `data.serverId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 服务Id（Java: String） | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "id": "<id>",
    "ip": "<ip>",
    "hookIp": "<hookIp>",
    "sdpIp": "<sdpIp>"
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

- [MediaServer](../models/MediaServer-b3c8996f50c7.md)：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaServer.java`
