# POST /api/platform/update

[文档入口](../../第三方对接接口手册.md) · [所属模块](../modules/PlatformController.md) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 更新上级平台信息 |
| 请求方法 | `POST` |
| 请求路径 | `/api/platform/update` |
| 认证方式 | access-token 或 api-key；ADMIN / OPERATOR |
| Content-Type | application/json |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/gb28181/controller/PlatformController.java` / `PlatformController` |
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
| `$body` | JSON Body | object | 是 | JSON对象；字段见模型表 | 无；必须提供 | 请求体模型；字段见模型索引；整个JSON请求体，Java形参名parentPlatform不是额外的JSON包装字段 | `{}` |
| `$body.id` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | ID(数据库中) | `1` |
| `$body.enable` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 是否启用 | `true` |
| `$body.name` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `$body.serverGBId` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | SIP服务国标编码 | `"<serverGBId>"` |
| `$body.serverGBDomain` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | SIP服务国标域 | `"<serverGBDomain>"` |
| `$body.serverIp` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | SIP服务IP | `"<serverIp>"` |
| `$body.serverPort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | SIP服务端口 | `1` |
| `$body.deviceGBId` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 设备国标编号 | `"<deviceGBId>"` |
| `$body.deviceIp` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 设备ip | `"<deviceIp>"` |
| `$body.devicePort` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 设备端口 | `1` |
| `$body.username` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 使用设备国标编号) | SIP认证用户名(默认使用设备国标编号) | `"<username>"` |
| `$body.password` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | SIP认证密码 | `"<32位MD5或该接口规定的密码>"` |
| `$body.expires` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 注册周期 (秒) | `1` |
| `$body.keepTimeout` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 心跳周期(秒) | `1` |
| `$body.transport` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 传输协议 | `"<transport>"` |
| `$body.characterSet` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 字符集 | `"<characterSet>"` |
| `$body.ptz` | JSON Body | boolean | 源码未声明；见业务校验 | true / false；允许云台控制 | 源码未声明默认值 | 允许云台控制 | `true` |
| `$body.rtcp` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | RTCP流保活 | `true` |
| `$body.status` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 在线状态 | `true` |
| `$body.channelCount` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 通道数量 | `1` |
| `$body.catalogSubscribe` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 已被订阅目录信息 | `true` |
| `$body.alarmSubscribe` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 已被订阅报警信息 | `true` |
| `$body.mobilePositionSubscribe` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 已被订阅移动位置信息 | `true` |
| `$body.catalogGroup` | JSON Body | integer | 源码未声明；见业务校验 | 整数；目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8 | 源码未声明默认值 | 目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8 | `1` |
| `$body.updateTime` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `$body.createTime` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `$body.asMessageChannel` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 是否作为消息通道 | `true` |
| `$body.sendStreamIp` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 点播回复200OK使用的IP | `"<sendStreamIp>"` |
| `$body.autoPushChannel` | JSON Body | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 是否自动推送通道变化 | `true` |
| `$body.catalogWithPlatform` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 目录信息包含平台信息, 0：关闭，1：打开 | `1` |
| `$body.catalogWithGroup` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 目录信息包含分组信息, 0：关闭，1：打开 | `1` |
| `$body.catalogWithRegion` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 目录信息包含行政区划, 0：关闭，1：打开 | `1` |
| `$body.civilCode` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 行政区划 | `"<civilCode>"` |
| `$body.manufacturer` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 平台厂商 | `"<manufacturer>"` |
| `$body.model` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 平台型号 | `"<model>"` |
| `$body.address` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 平台安装地址 | `"<address>"` |
| `$body.registerWay` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 1（Java初始值；请求默认值依接口） | 注册方式（必选）缺省为1； 1-符合IETF RFC 3261标准的认证注册模式；2-基于口令的双向认证注册模式；3-基于数字证书的双向认证注册模式(高安全级别要求)；4-基于数字证书的单向认证注册模式（高安全级别要求）；Java初始值：1 | `1` |
| `$body.secrecy` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 0（Java初始值；请求默认值依接口） | 保密属性（必选）缺省为0；0-不涉密，1-涉密；Java初始值：0 | `1` |
| `$body.serverId` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 执行注册的服务ID | `"<serverId>"` |
| `$body.lastRegisterResult` | JSON Body | object | 源码未声明；见业务校验 | JSON对象；字段见模型表 | 源码未声明默认值 | 最近一次注册结果(不存储于数据库) | `{}` |

`$body`表示整个JSON值，`$body.name`表示根对象的name字段；不要发送名为$body或Java形参名的额外包装对象。

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
POST /api/platform/update HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
Content-Type: application/json

{
  "id": 1,
  "enable": true,
  "name": "<name>",
  "serverGBId": "<serverGBId>"
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

- [Platform](../models/Platform-61204b4fa5a2.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Platform.java`
