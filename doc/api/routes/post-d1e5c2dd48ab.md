# POST /api/server/media_server/save

[文档入口](../../第三方对接接口手册.md) · [所属模块](../modules/ServerController.md) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 保存流媒体服务 |
| 请求方法 | `POST` |
| 请求路径 | `/api/server/media_server/save` |
| 认证方式 | access-token 或 api-key；ADMIN / OPERATOR |
| Content-Type | application/json |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/vmanager/server/ServerController.java` / `ServerController` |
| 源码返回类型 | `void` |

#### 请求头

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `access-token` | Header | string | 二选一 | 有效JWT | 未传时须提供另一种凭证 | 有效JWT；与api-key二选一；权限见基本信息 | `"<access-token>"` |
| `api-key` | Header | string | 二选一 | 已启用的API Key | 未传时须提供另一种凭证 | 已启用的API Key；与access-token二选一；权限见基本信息 | `"<api-key>"` |
| `Content-Type` | Header | string | 是 | application/json | 无；必须提供 | 请求体编码：application/json | `"application/json"` |

#### Path 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### Query 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `$body` | JSON Body | object | 是 | JSON对象；字段见模型表 | 无；必须提供 | 请求体模型；字段见模型索引；整个JSON请求体，Java形参名mediaServer不是额外的JSON包装字段 | `{}` |
| `$body.id` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | ID | `"<id>"` |
| `$body.ip` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | IP | `"<ip>"` |
| `$body.hookIp` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | "127.0.0.1"（Java初始值；请求默认值依接口） | hook使用的IP（zlm访问WVP使用的IP）；Java初始值："127.0.0.1" | `"<hookIp>"` |
| `$body.sdpIp` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | SDP IP | `"<sdpIp>"` |
| `$body.streamIp` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 流IP | `"<streamIp>"` |
| `$body.httpPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | HTTP端口 | `1` |
| `$body.httpSSlPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | HTTPS端口 | `1` |
| `$body.rtmpPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | RTMP端口 | `1` |
| `$body.flvPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | flv端口 | `1` |
| `$body.flvSSLPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | https-flv端口 | `1` |
| `$body.mp4Port` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | mp4端口 | `1` |
| `$body.wsFlvPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | ws-flv端口 | `1` |
| `$body.wsFlvSSLPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | wss-flv端口 | `1` |
| `$body.rtmpSSlPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | RTMPS端口 | `1` |
| `$body.rtpProxyPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | RTP收流端口（单端口模式有用） | `1` |
| `$body.jttProxyPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 1078收流端口（单端口模式有用） | `1` |
| `$body.rtspPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | RTSP端口 | `1` |
| `$body.rtspSSLPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | RTSPS端口 | `1` |
| `$body.autoConfig` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 是否开启自动配置ZLM | `true` |
| `$body.secret` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | ZLM鉴权参数 | `"<secret>"` |
| `$body.hookAliveInterval` | JSON Body | number | 源码未声明；见业务校验 | 数值 | 源码未声明默认值 | keepalive hook触发间隔,单位秒 | `1.0` |
| `$body.rtpEnable` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 是否使用多端口模式 | `true` |
| `$body.status` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 状态 | `true` |
| `$body.rtpPortRange` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 多端口RTP收流端口范围 | `"<rtpPortRange>"` |
| `$body.sendRtpPortRange` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | RTP发流端口范围 | `"<sendRtpPortRange>"` |
| `$body.recordAssistPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | assist服务端口 | `1` |
| `$body.createTime` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `$body.updateTime` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `$body.lastKeepaliveTime` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 上次心跳时间 | `"<lastKeepaliveTime>"` |
| `$body.defaultServer` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | ZLM | 是否是默认ZLM | `true` |
| `$body.recordDay` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 录像存储时长 | `1` |
| `$body.recordPath` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 录像存储路径 | `"<recordPath>"` |
| `$body.type` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 类型： zlm/abl | `"<type>"` |
| `$body.transcodeSuffix` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 转码的前缀 | `"<transcodeSuffix>"` |
| `$body.serverId` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 服务Id | `"<serverId>"` |

`$body`表示整个JSON值，`$body.name`表示根对象的name字段；不要发送名为$body或Java形参名的额外包装对象。

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
POST /api/server/media_server/save HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
Content-Type: application/json

{
  "id": "<id>",
  "ip": "<ip>",
  "hookIp": "<hookIp>",
  "sdpIp": "<sdpIp>"
}
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | null | 固定null | JSON null | 无业务返回值 | `null` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": null
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
