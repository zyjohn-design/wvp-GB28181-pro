# POST /api/common/channel/front-end/preset/remark

[文档入口](../../第三方对接接口手册.md) · [所属模块条目](../modules/ChannelFrontEndController.md#route-post-fd01d90e86c7) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 修改平台预置位名称备注（不下发设备） |
| 请求方法 | `POST` |
| 请求路径 | `/api/common/channel/front-end/preset/remark` |
| 认证方式 | access-token 或 api-key；ADMIN / OPERATOR |
| Content-Type | application/x-www-form-urlencoded（也可按接口绑定方式使用 query） |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/gb28181/controller/ChannelFrontEndController.java` / `ChannelFrontEndController` |
| 源码返回类型 | `WVPResult<String>` |

#### 请求头

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `access-token` | Header | string | 二选一 | 有效JWT | 未传时须提供另一种凭证 | 有效JWT；与api-key二选一；权限见基本信息 | `"<access-token>"` |
| `api-key` | Header | string | 二选一 | 已启用的API Key | 未传时须提供另一种凭证 | 已启用的API Key；与access-token二选一；权限见基本信息 | `"<api-key>"` |
| `Content-Type` | Header | string | 是 | application/x-www-form-urlencoded（也可按接口绑定方式使用 query） | 无；必须提供 | 请求体编码：application/x-www-form-urlencoded（也可按接口绑定方式使用 query） | `"application/x-www-form-urlencoded"` |

#### 业务约定

参数、名称优先级、应答语义与完整示例见[接入指南第8.2节](../接入指南.md#82-预置位)。名称备注只保存到平台，不进入国标控制指令。

仅保存平台名称，不下发设备指令；成功data为最终名称，省略或留空名称恢复预置点+编号。

#### Path 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### Query 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `channelId` | Query / Form | integer | 是 | 整数 | 无；必须提供 | 通道数据库整数ID（CommonGBChannel.gbId），不是通道国标编号 | `1` |
| `presetId` | Query / Form | string | 是 | 1–255整数；0号预留；兼容PresetID_1至PresetID_255 | 无；必须提供 | 预置位编号1–255；兼容已知PresetID_N格式；0号预留 | `"1"` |
| `presetName` | Query / Form | string | 否 | 去除首尾空白；最多128个UTF-16编码单元 | 未传/空白：预置点+编号 | 平台名称备注，最多128个UTF-16编码单元；空白或未传默认预置点+编号；不下发设备 | `"<presetName>"` |

表中 Query / Form 字段由表单或查询串绑定；示例采用表单，不能改成 JSON body。

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
POST /api/common/channel/front-end/preset/remark HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
Content-Type: application/x-www-form-urlencoded

channelId=1&presetId=1
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 0表示平台名称备注已保存 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明 | `"成功"` |
| `data` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 最终保存的名称，空白输入生成预置点+编号 | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": "预置点1"
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
| v1.0 | 2026-10-21 | 根据当前源码首次收录；具体业务规则以源码和部署配置为准 | 项目维护者（未指定个人） |

---

## 解析与实现约定

先核对本页请求位置与Content-Type；Query/Form不能自动替换成JSON。示例只展示形状，可能省略业务字段；必填以字段表与业务说明共同判断。

普通统一响应同时检查HTTP状态和code；data为null不等于失败。文件、流、兼容返回按本页响应类型解析。通用错误码表不是本接口必然返回的穷举集合。

控制或配置操作即使使用GET也会改变状态；网络超时表示结果未知，重试策略见[AI对接指南](../AI对接指南.md)。源码未声明的要求应向部署方确认。

## 关联模型

- [WVPResult](../models/WVPResult-670c07f20df6.md)：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/WVPResult.java`
