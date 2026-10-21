# GET /api/device/query/devices

[文档入口](../../第三方对接接口手册.md) · [所属模块条目](../modules/DeviceQuery.md#route-get-08dbae62822e) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 分页查询国标设备 |
| 请求方法 | `GET` |
| 请求路径 | `/api/device/query/devices` |
| 认证方式 | access-token 或 api-key；已登录 |
| Content-Type | 无请求体，无须 Content-Type |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/gb28181/controller/DeviceQuery.java` / `DeviceQuery` |
| 源码返回类型 | `PageInfo<Device>` |

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
| `page` | Query / Form | integer | 是 | 整数 | 无；必须提供 | 当前页 | `1` |
| `count` | Query / Form | integer | 是 | 整数 | 无；必须提供 | 每页查询数量 | `20` |
| `query` | Query / Form | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 搜索 | `"<query>"` |
| `status` | Query / Form | boolean | 源码未声明；见业务校验 | true / false | 源码未声明默认值 | 状态 | `true` |
| `accessType` | Query / Form | string | 源码未声明；见业务校验 | 源码未声明格式或范围 | 源码未声明默认值 | 接入类型：DEVICE-国标设备，PLATFORM-下级平台 | `"<accessType>"` |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
GET /api/device/query/devices?page=1&count=20&query=<query>&status=true&accessType=<accessType> HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 业务结果，依接口返回类型变化 | `按接口示例` |
| `data.list` | array | 源码未声明可空约束；按实际响应处理 | JSON数组；元素类型见说明 | 本页 Device 列表 | `按接口示例` |
| `data.total` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 总记录数 | `按接口示例` |
| `data.pageNum` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 当前页码，从 1 开始 | `按接口示例` |
| `data.pageSize` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 每页数量 | `按接口示例` |
| `data.pages` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 总页数 | `按接口示例` |
| `data.size` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 本页条数 | `按接口示例` |
| `data.hasNextPage` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否还有下一页 | `按接口示例` |
| `data.hasPreviousPage` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否有上一页 | `按接口示例` |
| `data.list[].id` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 数据库自增ID（Java: int） | `按接口示例` |
| `data.list[].deviceId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 设备国标编号（Java: String） | `按接口示例` |
| `data.list[].name` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 名称（Java: String） | `按接口示例` |
| `data.list[].manufacturer` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 生产厂商（Java: String） | `按接口示例` |
| `data.list[].model` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 型号（Java: String） | `按接口示例` |
| `data.list[].firmware` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 固件版本（Java: String） | `按接口示例` |
| `data.list[].transport` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 传输协议（UDP/TCP）（Java: String） | `按接口示例` |
| `data.list[].streamMode` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 数据流传输模式（Java: String） | `按接口示例` |
| `data.list[].ip` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | IP（Java: String） | `按接口示例` |
| `data.list[].port` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 端口（Java: int） | `按接口示例` |
| `data.list[].hostAddress` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | wan地址（Java: String） | `按接口示例` |
| `data.list[].onLine` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否在线，true为在线，false为离线（Java: boolean） | `按接口示例` |
| `data.list[].registerTimeStamp` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 注册时间戳（Java: Long） | `按接口示例` |
| `data.list[].keepaliveTimeStamp` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 心跳时间（Java: Long） | `按接口示例` |
| `data.list[].heartBeatInterval` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 心跳间隔（Java: Integer） | `按接口示例` |
| `data.list[].heartBeatCount` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 心跳超时次数（Java: Integer） | `按接口示例` |
| `data.list[].positionCapability` | integer | 源码未声明可空约束；按实际响应处理 | 整数；取值:0-不支持；2-支持北斗定位(可选,默认取值为0（Java: Integer） | 定位功能支持情况。取值:0-不支持;1-支持 GPS定位;2-支持北斗定位(可选,默认取值为0（Java: Integer） | `按接口示例` |
| `data.list[].channelCount` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 通道个数（Java: int） | `按接口示例` |
| `data.list[].expires` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 注册有效期（Java: int） | `按接口示例` |
| `data.list[].createTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 创建时间（Java: String） | `按接口示例` |
| `data.list[].updateTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 更新时间（Java: String） | `按接口示例` |
| `data.list[].mediaServerId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 设备使用的媒体id, 默认为null（Java: String） | `按接口示例` |
| `data.list[].charset` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 字符集, 支持 UTF-8、GB2312、GBK 与 GB18030（Java: String） | `按接口示例` |
| `data.list[].subscribeCycleForCatalog` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 目录订阅周期，o为不订阅（Java: int） | `按接口示例` |
| `data.list[].subscribeCycleForMobilePosition` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 移动设备位置订阅周期，0为不订阅（Java: int） | `按接口示例` |
| `data.list[].mobilePositionSubmissionInterval` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 移动设备位置信息上报时间间隔,单位:秒,默认值5；Java初始值：5（Java: int） | `按接口示例` |
| `data.list[].subscribeCycleForAlarm` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 报警心跳时间订阅周期，0为不订阅（Java: int） | `按接口示例` |
| `data.list[].ssrcCheck` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否开启ssrc校验，默认关闭，开启可以防止串流；Java初始值：false（Java: boolean） | `按接口示例` |
| `data.list[].geoCoordSys` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 地理坐标系， 目前支持 WGS84,GCJ02（Java: String） | `按接口示例` |
| `data.list[].password` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 密码（Java: String） | `按接口示例` |
| `data.list[].sdpIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 收流IP（Java: String） | `按接口示例` |
| `data.list[].localIp` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | SIP交互IP（设备访问平台的IP）（Java: String） | `按接口示例` |
| `data.list[].asMessageChannel` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否作为消息通道（Java: boolean） | `按接口示例` |
| `data.list[].sipTransactionInfo` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 设备注册的事务信息（Java: SipTransactionInfo） | `按接口示例` |
| `data.list[].broadcastPushAfterAck` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 控制语音对讲流程，释放收到ACK后发流（Java: boolean） | `按接口示例` |
| `data.list[].serverId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 所属服务Id（Java: String） | `按接口示例` |
| `data.list[].typeCode` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标编号中的三位类型编码（派生字段）（Java: String） | `按接口示例` |
| `data.list[].typeName` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标编号类型名称（派生字段）（Java: String） | `按接口示例` |
| `data.list[].accessType` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 接入类型：DEVICE-独立设备，PLATFORM-下级平台（派生字段）（Java: String） | `按接口示例` |
| `data.list[].accessTypeName` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 接入类型名称（派生字段）（Java: String） | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "list": [
      {
        "id": 1,
        "deviceId": "<deviceId>",
        "name": "<name>",
        "manufacturer": "<manufacturer>"
      }
    ],
    "total": 1,
    "pageNum": 1,
    "pageSize": 20,
    "pages": 1,
    "size": 1,
    "hasNextPage": false,
    "hasPreviousPage": false
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

- [Device](../models/Device-7f29a193b9b3.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Device.java`
