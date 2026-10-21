# GET /api/common/channel/civilCode/unusual/list

[文档入口](../../第三方对接接口手册.md) · [所属模块条目](../modules/ChannelController.md#route-get-be9cf236e66c) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | 存在行政区划但无法挂载的通道列表 |
| 请求方法 | `GET` |
| 请求路径 | `/api/common/channel/civilCode/unusual/list` |
| 认证方式 | access-token 或 api-key；已登录 |
| Content-Type | 无请求体，无须 Content-Type |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/gb28181/controller/ChannelController.java` / `ChannelController` |
| 源码返回类型 | `PageInfo<CommonGBChannel>` |

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
| `query` | Query / Form | string | 否 | 源码未声明格式或范围 | 源码未声明默认值 | 查询内容 | `"<query>"` |
| `online` | Query / Form | boolean | 否 | true / false | 源码未声明默认值 | 是否在线 | `true` |
| `channelType` | Query / Form | integer | 否 | 整数 | 源码未声明默认值 | 通道类型， 0：国标设备，1：推流设备，2：拉流代理 | `1` |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
GET /api/common/channel/civilCode/unusual/list?page=1&count=20 HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 业务结果，依接口返回类型变化 | `按接口示例` |
| `data.list` | array | 源码未声明可空约束；按实际响应处理 | JSON数组；元素类型见说明 | 本页 CommonGBChannel 列表 | `按接口示例` |
| `data.total` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 总记录数 | `按接口示例` |
| `data.pageNum` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 当前页码，从 1 开始 | `按接口示例` |
| `data.pageSize` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 每页数量 | `按接口示例` |
| `data.pages` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 总页数 | `按接口示例` |
| `data.size` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 本页条数 | `按接口示例` |
| `data.hasNextPage` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否还有下一页 | `按接口示例` |
| `data.hasPreviousPage` | boolean | 源码未声明可空约束；按实际响应处理 | true / false | 是否有上一页 | `按接口示例` |
| `data.list[].gbId` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 通用通道数据库自增ID；/api/common/channel 接口的 channelId 参数使用此值（Java: int） | `按接口示例` |
| `data.list[].gbDeviceId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-编码（Java: String） | `按接口示例` |
| `data.list[].gbName` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-名称（Java: String） | `按接口示例` |
| `data.list[].gbManufacturer` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-设备厂商（Java: String） | `按接口示例` |
| `data.list[].gbModel` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-设备型号（Java: String） | `按接口示例` |
| `data.list[].gbOwner` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-设备归属（Java: String） | `按接口示例` |
| `data.list[].gbCivilCode` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-行政区域（Java: String） | `按接口示例` |
| `data.list[].gbBlock` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-警区（Java: String） | `按接口示例` |
| `data.list[].gbAddress` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-安装地址（Java: String） | `按接口示例` |
| `data.list[].gbParental` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-是否有子设备（Java: Integer） | `按接口示例` |
| `data.list[].gbParentId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-父节点ID（Java: String） | `按接口示例` |
| `data.list[].gbSafetyWay` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-信令安全模式（Java: Integer） | `按接口示例` |
| `data.list[].gbRegisterWay` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-注册方式（Java: Integer） | `按接口示例` |
| `data.list[].gbCertNum` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-证书序列号（Java: String） | `按接口示例` |
| `data.list[].gbCertifiable` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-证书有效标识（Java: Integer） | `按接口示例` |
| `data.list[].gbErrCode` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-无效原因码(有证书且证书无效的设备必选)（Java: Integer） | `按接口示例` |
| `data.list[].gbEndTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-证书终止有效期(有证书且证书无效的设备必选)（Java: String） | `按接口示例` |
| `data.list[].gbSecrecy` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-保密属性(必选)缺省为0;0-不涉密,1-涉密（Java: Integer） | `按接口示例` |
| `data.list[].gbIpAddress` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-设备/系统IPv4/IPv6地址（Java: String） | `按接口示例` |
| `data.list[].gbPort` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-设备/系统端口（Java: Integer） | `按接口示例` |
| `data.list[].gbPassword` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-设备口令（Java: String） | `按接口示例` |
| `data.list[].gbStatus` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-设备状态（Java: String） | `按接口示例` |
| `data.list[].gbLongitude` | number | 源码未声明可空约束；按实际响应处理 | 数值 | 国标-经度 WGS-84坐标系（Java: Double） | `按接口示例` |
| `data.list[].gbLatitude` | number | 源码未声明可空约束；按实际响应处理 | 数值 | 国标-纬度 WGS-84坐标系（Java: Double） | `按接口示例` |
| `data.list[].gpsAltitude` | number | 源码未声明可空约束；按实际响应处理 | 数值 | GPS 海拔高度（Java: Double） | `按接口示例` |
| `data.list[].gpsSpeed` | number | 源码未声明可空约束；按实际响应处理 | 数值 | GPS 速度（Java: Double） | `按接口示例` |
| `data.list[].gpsDirection` | number | 源码未声明可空约束；按实际响应处理 | 数值 | GPS 方向角（Java: Double） | `按接口示例` |
| `data.list[].gpsTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | GPS 定位时间（Java: String） | `按接口示例` |
| `data.list[].gbBusinessGroupId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-虚拟组织所属的业务分组ID（Java: String） | `按接口示例` |
| `data.list[].gbPtzType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-摄像机结构类型,标识摄像机类型: 1-球机; 2-半球; 3-固定枪机; 4-遥控枪机;5-遥控半球;6-多目设备的全景/拼接通道;7-多目设备的分割通道; 99-移动设备（非标）98-会议设备（非标）（Java: Integer） | `按接口示例` |
| `data.list[].gbPositionType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | -摄像机位置类型扩展。1-省际检查站、2-党政机关、3-车站码头、4-中心广场、5-体育场馆、6-商业中心、7-宗教场所、8-校园周边、9-治安复杂区域、10-交通干线。当目录项为摄像机时可选。（Java: Integer） | `按接口示例` |
| `data.list[].gbRoomType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-摄像机安装位置室外、室内属性。1-室外、2-室内。（Java: Integer） | `按接口示例` |
| `data.list[].gbUseType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-用途属性（Java: Integer） | `按接口示例` |
| `data.list[].gbSupplyLightType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-摄像机补光属性。1-无补光;2-红外补光;3-白光补光;4-激光补光;9-其他（Java: Integer） | `按接口示例` |
| `data.list[].gbDirectionType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 国标-摄像机监视方位(光轴方向)属性。1-东(西向东)、2-西(东向西)、3-南(北向南)、4-北(南向北)、5-东南(西北到东南)、6-东北(西南到东北)、7-西南(东北到西南)、8-西北(东南到西北)（Java: Integer） | `按接口示例` |
| `data.list[].gbResolution` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-摄像机支持的分辨率,可多值（Java: String） | `按接口示例` |
| `data.list[].gbDownloadSpeed` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 国标-下载倍速(可选),可多值（Java: String） | `按接口示例` |
| `data.list[].gbSvcSpaceSupportMod` | integer | 源码未声明可空约束；按实际响应处理 | 整数；国标-空域编码能力,取值0-不支持 | 国标-空域编码能力,取值0-不支持;1-1级增强(1个增强层);2-2级增强(2个增强层);3-3级增强(3个增强层)（Java: Integer） | `按接口示例` |
| `data.list[].gbSvcTimeSupportMode` | integer | 源码未声明可空约束；按实际响应处理 | 整数；国标-时域编码能力,取值0-不支持 | 国标-时域编码能力,取值0-不支持;1-1级增强;2-2级增强;3-3级增强(可选)（Java: Integer） | `按接口示例` |
| `data.list[].recordPLan` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 二进制保存的录制计划, 每一位表示每个小时的前半个小时（Java: Long） | `按接口示例` |
| `data.list[].dataType` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 关联的数据类型（Java: Integer） | `按接口示例` |
| `data.list[].dataDeviceId` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 关联的设备ID（Java: Integer） | `按接口示例` |
| `data.list[].createTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 创建时间（Java: String） | `按接口示例` |
| `data.list[].updateTime` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 更新时间（Java: String） | `按接口示例` |
| `data.list[].streamId` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 流唯一编号，存在表示正在直播（Java: String） | `按接口示例` |
| `data.list[].enableBroadcast` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 是否支持对讲 1支持,0不支持（Java: Integer） | `按接口示例` |
| `data.list[].mapLevel` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 抽稀后的图层层级（Java: Integer） | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "list": [
      {
        "gbId": 1,
        "gbDeviceId": "<gbDeviceId>",
        "gbName": "<gbName>",
        "gbManufacturer": "<gbManufacturer>"
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

- [CommonGBChannel](../models/CommonGBChannel-44b704531f25.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/CommonGBChannel.java`
