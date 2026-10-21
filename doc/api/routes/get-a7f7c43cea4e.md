# GET /api/platform/info/{id}

[文档入口](../../第三方对接接口手册.md) · [所属模块](../modules/PlatformController.md) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 获取级联服务器信息 |
| 请求方法 | `GET` |
| 请求路径 | `/api/platform/info/{id}` |
| 认证方式 | access-token 或 api-key；已登录 |
| Content-Type | 无请求体，无须 Content-Type |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/gb28181/controller/PlatformController.java` / `PlatformController` |
| 源码返回类型 | `Platform` |

#### 请求头

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `access-token` | Header | string | 二选一 | 有效JWT | 未传时须提供另一种凭证 | 有效JWT；与api-key二选一；权限见基本信息 | `"<access-token>"` |
| `api-key` | Header | string | 二选一 | 已启用的API Key | 未传时须提供另一种凭证 | 已启用的API Key；与access-token二选一；权限见基本信息 | `"<api-key>"` |

#### Path 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `id` | Path | string | 是 | 源码未声明格式或范围 | 无；必须提供 | 平台国标编号 | `"<id>"` |

#### Query 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
GET /api/platform/info/<id> HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 业务结果，依接口返回类型变化 | `按接口示例` |
| `data.id` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | ID(数据库中)（Java: Integer） | `按接口示例` |
| `data.enable` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否启用（Java: boolean） | `按接口示例` |
| `data.name` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 名称（Java: String） | `按接口示例` |
| `data.serverGBId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SIP服务国标编码（Java: String） | `按接口示例` |
| `data.serverGBDomain` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SIP服务国标域（Java: String） | `按接口示例` |
| `data.serverIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SIP服务IP（Java: String） | `按接口示例` |
| `data.serverPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | SIP服务端口（Java: int） | `按接口示例` |
| `data.deviceGBId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 设备国标编号（Java: String） | `按接口示例` |
| `data.deviceIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 设备ip（Java: String） | `按接口示例` |
| `data.devicePort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 设备端口（Java: int） | `按接口示例` |
| `data.username` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SIP认证用户名(默认使用设备国标编号)（Java: String） | `按接口示例` |
| `data.password` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SIP认证密码（Java: String） | `按接口示例` |
| `data.expires` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 注册周期 (秒)（Java: int） | `按接口示例` |
| `data.keepTimeout` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 心跳周期(秒)（Java: int） | `按接口示例` |
| `data.transport` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 传输协议（Java: String） | `按接口示例` |
| `data.characterSet` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 字符集（Java: String） | `按接口示例` |
| `data.ptz` | boolean | 源码未声明可空约束；按实际响应处理 | true / false；允许云台控制（Java: boolean） | 允许云台控制（Java: boolean） | `按接口示例` |
| `data.rtcp` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | RTCP流保活（Java: boolean） | `按接口示例` |
| `data.status` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 在线状态（Java: boolean） | `按接口示例` |
| `data.channelCount` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 通道数量（Java: int） | `按接口示例` |
| `data.catalogSubscribe` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 已被订阅目录信息（Java: boolean） | `按接口示例` |
| `data.alarmSubscribe` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 已被订阅报警信息（Java: boolean） | `按接口示例` |
| `data.mobilePositionSubscribe` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 已被订阅移动位置信息（Java: boolean） | `按接口示例` |
| `data.catalogGroup` | integer | 源码未声明可空约束；按实际响应处理 | 整数；目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8（Java: int） | 目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8（Java: int） | `按接口示例` |
| `data.updateTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 更新时间（Java: String） | `按接口示例` |
| `data.createTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 创建时间（Java: String） | `按接口示例` |
| `data.asMessageChannel` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否作为消息通道（Java: boolean） | `按接口示例` |
| `data.sendStreamIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 点播回复200OK使用的IP（Java: String） | `按接口示例` |
| `data.autoPushChannel` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否自动推送通道变化（Java: Boolean） | `按接口示例` |
| `data.catalogWithPlatform` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 目录信息包含平台信息, 0：关闭，1：打开（Java: int） | `按接口示例` |
| `data.catalogWithGroup` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 目录信息包含分组信息, 0：关闭，1：打开（Java: int） | `按接口示例` |
| `data.catalogWithRegion` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 目录信息包含行政区划, 0：关闭，1：打开（Java: int） | `按接口示例` |
| `data.civilCode` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 行政区划（Java: String） | `按接口示例` |
| `data.manufacturer` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 平台厂商（Java: String） | `按接口示例` |
| `data.model` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 平台型号（Java: String） | `按接口示例` |
| `data.address` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 平台安装地址（Java: String） | `按接口示例` |
| `data.registerWay` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 注册方式（必选）缺省为1； 1-符合IETF RFC 3261标准的认证注册模式；2-基于口令的双向认证注册模式；3-基于数字证书的双向认证注册模式(高安全级别要求)；4-基于数字证书的单向认证注册模式（高安全级别要求）；Java初始值：1（Java: int） | `按接口示例` |
| `data.secrecy` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 保密属性（必选）缺省为0；0-不涉密，1-涉密；Java初始值：0（Java: int） | `按接口示例` |
| `data.serverId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 执行注册的服务ID（Java: String） | `按接口示例` |
| `data.lastRegisterResult` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 最近一次注册结果(不存储于数据库)（Java: PlatformRegisterResult） | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "id": 1,
    "enable": true,
    "name": "<name>",
    "serverGBId": "<serverGBId>"
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

- [Platform](../models/Platform-61204b4fa5a2.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Platform.java`
