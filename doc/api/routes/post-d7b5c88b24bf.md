# POST /index/hook/on_play

[文档入口](../../第三方对接接口手册.md) · [所属模块](../modules/ZLMHttpHookListener.md) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | onPlay |
| 请求方法 | `POST` |
| 请求路径 | `/index/hook/on_play` |
| 认证方式 | 默认放行；媒体 Hook 另按媒体服务密钥校验；内部/回调接口，不建议第三方直接调用 |
| Content-Type | application/json |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/media/zlm/ZLMHttpHookListener.java` / `ZLMHttpHookListener` |
| 源码返回类型 | `HookResult` |

#### 请求头

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `access-token` | Header | string | 否 | 有效JWT | 默认放行 | 有效JWT；与api-key二选一；权限见基本信息 | `"<access-token>"` |
| `api-key` | Header | string | 否 | 已启用的API Key | 默认放行 | 已启用的API Key；与access-token二选一；权限见基本信息 | `"<api-key>"` |
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
| `$body` | JSON Body | object | 是 | JSON对象；字段见模型表 | 无；必须提供 | 请求体模型；字段见模型索引；整个JSON请求体，Java形参名param不是额外的JSON包装字段 | `{}` |
| `$body.id` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 数据库记录 ID | `"<id>"` |
| `$body.app` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 流应用名 | `"<app>"` |
| `$body.stream` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 流 ID | `"<stream>"` |
| `$body.ip` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | IP 地址 | `"<ip>"` |
| `$body.params` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 附加参数 | `"<params>"` |
| `$body.port` | JSON Body | integer | 源码未声明；见业务校验 | 整数 | 源码未声明默认值 | 网络端口 | `1` |
| `$body.schema` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 媒体流协议类型 | `"<schema>"` |
| `$body.vhost` | JSON Body | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 媒体服务虚拟主机 | `"<vhost>"` |

`$body`表示整个JSON值，`$body.name`表示根对象的name字段；不要发送名为$body或Java形参名的额外包装对象。

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
POST /index/hook/on_play HTTP/1.1
Host: <wvp-host>
Content-Type: application/json

{
  "id": "<id>",
  "app": "<app>",
  "stream": "<stream>",
  "ip": "<ip>"
}
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `response.code` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 结果或业务状态码（Java: int） | `按接口示例` |
| `response.msg` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 结果或错误信息（Java: String） | `按接口示例` |
| — | — | — | — | 此路由不经过统一响应包装，按对应媒体或兼容协议解析 | — |

#### 成功响应

```json
{
  "code": 1,
  "msg": "<msg>"
}
```

#### 错误响应

```http
HTTP/1.1 4xx/5xx
Content-Type: <以实际响应头为准>

<格式由该兼容协议或媒体服务决定>
```

#### 错误码

| code | msg | 说明 |
|---|---|---|
| HTTP 2xx | — | 成功；业务体格式依接口协议 |
| HTTP 4xx | — | 参数、鉴权或资源错误，按响应体处理 |
| HTTP 5xx | — | 服务端或媒体服务异常 |

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

- [HookResult](../models/HookResult-5492300b5601.md)：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/HookResult.java`
- [OnPlayHookParam](../models/OnPlayHookParam-42fc024fd744.md)：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnPlayHookParam.java`
