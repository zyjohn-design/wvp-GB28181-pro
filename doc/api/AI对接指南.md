# AI 工具对接指南

[文档入口](../第三方对接接口手册.md) · [业务接入指南](接入指南.md) · [接口索引](接口索引.md) · [模型索引](模型索引.md)

本指南供开发者和 AI 编程工具共同使用。先确定业务流程，再读取需要的接口字段表；示例里的设备、通道、凭证都需替换。字段表有“源码未声明”时表示尚未确认约束，不能据此生成一个保证可用的请求。

## 1. 按需读取文档

| 步骤 | 读取内容 | 产出 |
|---|---|---|
| 1 | 本指南 | 确定标识、请求编码、结果判断和重试规则 |
| 2 | 接入指南对应章节 | 选定播放、录像或控制流程，确认前置条件 |
| 3 | 接口索引或route-index.json | 用HTTP方法和完整路径定位接口详情文件 |
| 4 | 本次使用的接口详情 | 获取每个参数的位置、类型、必填、格式、默认值 |
| 5 | 接口页底部“关联模型” | 获取返回对象及嵌套字段；同名类按源码路径区分 |
| 6 | 实际部署联调响应 | 确认权限、模块开关、设备能力和媒体地址可达性 |

`route-index.json` 是文档检索索引，**不是 OpenAPI 规范**；不能直接当作 OpenAPI 输入生成 SDK。里面的 `required` 保留“是/否/未声明”等原始标记，不能按非空字符串转成布尔值。`document`、`models` 都相对 `doc/api/`。`ANY` 表示源码未限定方法，不是一个真实HTTP方法；需结合接口实现与部署验证选择。

按方法和路径精确查找一条接口：在仓库根目录执行，替换target的值。

```python
import json
from pathlib import Path

root = Path("doc/api")
index = json.loads((root / "route-index.json").read_text())
target = ("GET", "/api/common/channel/front-end/preset/query")
matches = [r for r in index["routes"] if (r["method"], r["path"]) == target]
assert len(matches) == 1, "请检查方法、路径或是否存在重复映射"
route = matches[0]
print((root / route["document"]).read_text())
for model in route["models"]:
    print("关联模型：", root / model)
```

不要把全部接口和模型一次性放入AI上下文；只读本次使用的接口与关联模型，可以减少上下文占用和错误匹配。

## 2. 对接前应明确的部署信息

| 信息 | 类型 / 格式 | 获取方式 | 使用规则 |
|---|---|---|---|
| API根地址 | 带协议、主机、可选端口及部署前缀的URL | 部署方提供 | 拼接文档路径时避免重复斜线或漏掉网关前缀 |
| api-key | 字符串 | 管理员发放并启用 | 请求头传递；控制接口需要对应权限 |
| access-token | JWT字符串 | 登录成功后的data.accessToken或响应头 | 与api-key选择一种；过期后更新 |
| 设备与通道清单 | 接口返回对象 | 分页查询 | 不使用文档示例ID作为真实资源 |
| 媒体协议与外部端口 | 现场配置 | 部署方提供，结合点播响应验证 | 播放地址以响应为准，不拼接默认端口 |
| 模块与设备能力 | 开关、支持的指令 | 部署方及设备实际测试 | 收录在文档中不代表所有部署和设备都支持 |
| 超时设置 | 客户端、代理、服务端各层时限 | 与部署方核对 | 异步HTTP请求仍是一次HTTP响应；不需要自行构造轮询地址 |
| 录像时间解释 | 接口时间格式和现场时区 | 接口字段表、部署方确认 | 未声明时区时不能擅自转换为UTC或加Z |

API认证与媒体认证分别处理。点播成功后媒体请求可能访问另一主机，按媒体协议要求认证；不要把API凭证自动附加到所有媒体URL上。

## 3. ID映射：按来源使用

| 标识 | 类型 / 来源 | 用途 | 易错点 |
|---|---|---|---|
| 设备deviceId | string；设备列表data.list[].deviceId | 直连接口路径的deviceId | 国标编号按字符串保存，保留全部数字和前导零 |
| 原始通道国标编号 | string；设备通道列表data.list[].deviceId | 直连接口路径的channelId | 列表字段叫deviceId，路径参数叫channelId，不代表设备编号 |
| 通用通道gbId | integer；通用通道列表data.list[].gbId | /api/common/channel接口的channelId参数 | 不能传gbDeviceId，也不能传设备deviceId |
| 通用通道gbDeviceId | string；通用通道对象 | 国标标识、资源展示 | 不能代替通用接口要求的整数gbId |
| app、stream、mediaServerId | string；点播或录像相关响应 | 对应会话、媒体或录像操作 | 保留响应原值；不是deviceId或gbId |
| 预置位presetId | 查询响应string；控制HTTP参数为文本 | 同一个通道内定位预置位 | 1–255；已知PresetID_N格式兼容，未知字符串禁止截尾取数字 |
| 录像recordId | integer；平台录像列表记录id | 平台录像路径等接口 | 不能用设备录像文件路径或流ID代替 |

同一个 `channelId` 名称在不同路由中可能表示不同类型；SDK应建立不同变量，例如 `commonChannelId`、`gbChannelId`，避免复用一个含义不明的 `id`。

## 4. 请求字段如何编码

| 表中的位置 / 类型 | 发送方式 | 示例 / 规则 |
|---|---|---|
| Path | 替换路径占位符，作为一个路径段编码 | /api/play/start/{deviceId}/{channelId}；不可把花括号原样发送 |
| Query | URL查询参数；使用客户端参数编码器 | channelId=123；中文、空格、&等不能直接手工拼接 |
| Query / Form，GET | URL查询参数 | curl --get --data-urlencode 'presetName=东门入口' |
| Query / Form，POST | application/x-www-form-urlencoded表单，或按绑定方式放查询串 | POST不自动意味着JSON；修改名称就是表单接口 |
| JSON Body | application/json；字段名按模型JSON结构 | $body表示整个请求体；$body.name表示根对象name字段，不添加$body或Java形参名的包装层 |
| Multipart | multipart/form-data | 文件字段按接口名；让客户端自动生成boundary |
| Header | HTTP请求头 | api-key或access-token，不放入业务JSON |
| integer / number | 参数值为十进制文本；JSON体为数值 | 不加单位，不传NaN、空字符串或超范围值 |
| boolean | 按字段约定；通常true / false | 不自行替换成0 / 1或中文“是/否” |
| array | 以具体接口绑定规则为准 | 已明确的ZIP下载ids用重复参数ids=1&ids=2；不能推广至所有数组 |
| 日期时间 | 严格按字段表声明的格式 | yyyy-MM-dd HH:mm:ss时需编码空格；不要改为毫秒时间戳 |

| 输入状态 | 含义 | 处理 |
|---|---|---|
| 字段未传 | 服务端可能应用默认值 | 仅依赖明确记载的默认值 |
| 空字符串 | 显式传空值 | 不等同于未传；除允许空的字段外不要用它填满可选参数 |
| JSON null | 显式空值 | 是否支持由具体接口决定；Query中的字符串null不是JSON null |
| 数字0 / 布尔false | 有效的具体值 | 不用if(value)判断是否应传，避免误删0和false |
| Java初始值 | 源码对象创建时的值 | 不保证每条HTTP接口省略该字段时都应用它 |

表格示例中的字符串引号用于展示类型；实际Header和Query不额外带JSON引号。例如请求头应为 `api-key: 实际密钥`，不是 `api-key: "实际密钥"`。JSON体则按JSON语法保留字符串引号。

## 5. 响应判断与错误处理

普通统一响应使用以下字段；文件、媒体、Hook和部分兼容接口按各自详情处理。

| 字段 | 类型 | 判断规则 |
|---|---|---|
| HTTP状态 | integer | 非2xx先按HTTP失败处理；网关可能返回HTML而非JSON |
| code | integer | 在统一JSON响应中，只有0表示业务成功；HTTP200不代表业务成功 |
| msg | string | 人可读结果；真实字段名为msg，不是message；不要按中文内容写业务分支 |
| data | 依接口，可为null | 控制成功常为null；不能用data是否为真判断成功 |
| data.list | array | 分页结果；空页为[]，不是错误 |

推荐客户端处理顺序：接收HTTP响应 → 按状态和Content-Type分类 → 安全解码JSON → 若为统一响应则检查code → 按接口定义解析data。错误日志记录方法、路径、HTTP状态、code、msg和业务资源标识，避免记录凭证。

| 情况 | 结果解释 | 后续动作 |
|---|---|---|
| HTTP401或业务401 | 身份缺失/失效 | 更新凭证；控制请求仍应按调用结果决定是否再次发送 |
| HTTP403或业务403 | 权限不足或设备拒绝 | 核对角色及实际msg；单改请求参数不能解决权限问题 |
| 参数错误 | 请求未满足接口要求 | 按字段表修正；不要循环发送相同请求 |
| 设备离线、媒体失败 | 当前业务条件不满足 | 记录实际msg并检查设备/媒体状态 |
| 客户端断网、超时、网关超时 | 未知是否已经被服务端或设备执行 | 控制操作不自动重发；先查询相关状态、刷新列表或观察画面 |
| 预置位收到SIP200且code=0 | 设备接收，平台备注操作完成 | 不据此宣称镜头已到位或设备永久保存 |
| 设备已接收但备注保存失败 | 位置控制可能已经执行，平台名称未保存 | 调用修改名称接口补存，不重新设置当前位置 |

HTTP库常按GET自动重试。预置位、云台、启停等GET操作有副作用，客户端要按业务用途关闭自动重试和缓存。查询重试采用有限次数和退避，需结合服务负载；文档没有声明接口支持幂等键，不能自行添加一个字段就假设实现了去重。

## 6. 场景：实时播放

| 顺序 | 接口 / 操作 | 输入来源 | 成功后保存 / 判断 |
|---|---|---|---|
| 1 | GET /api/common/channel/list | page=1、count=20等过滤条件 | 读取data.list；后续按分页继续，不假设第一页是全量 |
| 2 | 选择可播放且在线的通道 | 列表对象 | 保存整数gbId；目录节点不能点播 |
| 3 | GET /api/common/channel/play | channelId=gbId | code=0后读取StreamContent；无可用URL时继续排查媒体 |
| 4 | 播放响应中的URL | 选择客户端支持的非空协议字段 | 检查网络可达、HTTPS/WSS、编码支持；WebRTC还需信令/ICE |
| 5 | 关闭画面、释放播放 | GET /api/common/channel/play/stop，channelId仍为同一gbId | 停止前确认本业务对共享播放的管理方式 |

如果选用直连接口，就从设备列表、设备通道列表获取两个国标编号，再调用 `/api/play/start/{deviceId}/{channelId}`。不要把通用通道整数ID放进这两个路径参数。

## 7. 场景：预置位完整生命周期

设置预置位表示把**摄像机当前所在位置**保存到编号；它没有接收经纬度或云台坐标的参数。要先把镜头移动到目标位置、停止并确认画面，再设置预置位。同号可能覆盖现有位置，使用前查询并由业务决定是否覆盖。

按设备同步全部通道的预置位，可使用 [GET /api/front-end/preset/query/{deviceId}](routes/get-45b3877f720f.md)，可选 `timeout=60`（秒，范围1–300）。返回 `data=[{channelId,channelName,presets}]`，只含预置位非空的视频通道；`channelId` 是国标编号。必须先检查 `code=0`，才可把结果当成全量快照；非零时即使存在 `data` 也只是部分结果，不能用于删除同步。全部成功且无预置位时返回空数组。


| 顺序 | 动作 | 方法与路径 | 输入 / 结果 |
|---|---|---|---|
| 1 | 查询设备现有预置位 | GET /api/common/channel/front-end/preset/query | channelId=整数gbId；data为数组，presetId按原字符串保存 |
| 2 | 选择编号、移动镜头 | 云台控制或人工操作 | 编号1–255；运动后发送stop，观察位置 |
| 3 | 保存当前位置 | GET /api/common/channel/front-end/preset/add | channelId、presetId必填；presetName可选 |
| 4 | 等待完成并刷新 | 等待同一个HTTP请求响应后再query | code=0表示收到设备SIP200；失败不能把本地列表改成成功 |
| 5 | 只修改名称 | POST /api/common/channel/front-end/preset/remark | Query/Form；不发送设备控制，不改变位置 |
| 6 | 调用已保存位置 | GET /api/common/channel/front-end/preset/call | channelId、presetId；接收成功后观察画面判断是否到位 |
| 7 | 删除该位置 | GET /api/common/channel/front-end/preset/delete | 成功后重新查询；不要仅靠本地删行判断设备状态 |

| 字段 / 行为 | 明确规则 |
|---|---|
| presetId输入 | 十进制1–255；已知PresetID_1至PresetID_255兼容；0预留。未知字符串保留显示与诊断，不通过提取尾数强行控制 |
| presetName输入 | 平台名称备注；去除首尾空白；最多128个Java UTF-16编码单元，部分emoji占两个单元 |
| 新增未填或全空白名称 | 自动保存为预置点+编号，例如预置点1 |
| 修改名称未填或全空白 | 重置为自动名称，不代表删除备注后恢复设备名称 |
| 名称展示 | 平台备注优先，然后设备名称，然后自动名称 |
| 查询列表的范围 | 设备实际返回的预置位；平台备注不会凭空生成设备预置位 |
| 只修改名称的边界 | 仅操作平台元数据，不能当作创建设备位置；应针对已查询到的预置位调用 |
| 设置/删除失败 | 不根据失败响应乐观修改平台备注或列表 |
| 控制等待时限 | 控制器有30秒HTTP异步兜底；SIP/跨节点超时还受部署配置影响，不是固定等待30秒 |
| 查询失败 | 与成功空列表区分；保留上次已知快照并标记同步失败，不清空现有业务记录 |

以下命令只演示结构。每步分别执行并检查HTTP状态、code、msg；**不要将整段当作无人检查的批处理脚本**。查询后选定可用编号，设置操作会保存当前镜头位置；删除示例只在确需删除时执行。

```bash
WVP='https://<wvp-host>'
API_KEY='<部署方提供的API Key>'
CHANNEL_ID='<通用通道gbId整数>'
PRESET_ID='<已选定的1到255编号>'

# 查询
curl -sS --get "$WVP/api/common/channel/front-end/preset/query" \
  -H "api-key: $API_KEY" --data-urlencode "channelId=$CHANNEL_ID"

# 在镜头已停止于目标位置后，保存当前位置和平台备注
curl -sS --max-time 40 --get "$WVP/api/common/channel/front-end/preset/add" \
  -H "api-key: $API_KEY" --data-urlencode "channelId=$CHANNEL_ID" \
  --data-urlencode "presetId=$PRESET_ID" --data-urlencode 'presetName=东门入口'

# 只改备注，curl的--data-urlencode会采用POST表单
curl -sS "$WVP/api/common/channel/front-end/preset/remark" \
  -H "api-key: $API_KEY" --data-urlencode "channelId=$CHANNEL_ID" \
  --data-urlencode "presetId=$PRESET_ID" --data-urlencode 'presetName=东门车行入口'

# 调用
curl -sS --max-time 40 --get "$WVP/api/common/channel/front-end/preset/call" \
  -H "api-key: $API_KEY" --data-urlencode "channelId=$CHANNEL_ID" \
  --data-urlencode "presetId=$PRESET_ID"

# 删除：确需删除时执行，然后重新query
curl -sS --max-time 40 --get "$WVP/api/common/channel/front-end/preset/delete" \
  -H "api-key: $API_KEY" --data-urlencode "channelId=$CHANNEL_ID" \
  --data-urlencode "presetId=$PRESET_ID"
```

40秒只是上面curl的客户端示例值，不是平台统一超时承诺。详情里的成功响应示例值也不是设备状态保证。控制成功通常 `data=null`；改名成功 `data` 是最终名称字符串；查询成功 `data` 是数组，三者必须分别建模。

## 8. 场景：录像与下载

| 目标 | 选用流程 | 不能混淆的内容 |
|---|---|---|
| 查设备侧录像 | 国标设备与通道 → /api/gb_record/query/{deviceId}/{channelId} → RecordInfo.recordList | 设备录像与平台云端录像不是同一清单 |
| 回放设备录像 | 查询到时间范围 → /api/playback/start/{deviceId}/{channelId} → 返回流 → 对应stop | 时间格式、流标识按详情；不是实时播放的停止接口 |
| 查平台已有录像 | /api/cloud/record/list或list-url → PageInfo记录 | 使用平台录像id，不使用设备文件路径 |
| 获取平台录像路径 | /api/cloud/record/play/path，recordId来自列表id | 返回对象按DownloadFileInfo解析，不假设响应本身是MP4字节 |
| 下载ZIP | /api/cloud/record/download/zip，按详情传ids | 响应为文件；错误响应仍要按HTTP与Content-Type处理 |

录像完整参数和停止、进度、定位、倍速规则见[接入指南第7节](接入指南.md#7-录像查询回放与下载)及对应接口；不同录像类型不能共用一套ID和停止逻辑。

## 9. 可直接交给AI工具的对接要求

```text
请按本仓库doc/第三方对接接口手册.md和doc/api/AI对接指南.md实现对接。
先说明采用的业务流程和每种ID的来源；按route-index.json定位所用接口，
逐个读取接口字段表及关联模型，不要一次加载全部文档。
按接口声明的Path、Query/Form、JSON Body发送参数，禁止把所有POST都转为JSON。
普通统一响应同时检查HTTP状态和code，使用msg字段，允许成功data为null。
国标编号作为字符串保留；通用通道channelId使用列表返回的整数gbId。
查询与控制分别配置重试；带副作用的GET不自动重试、不缓存。
对未声明的约束列出需要部署方确认的事项，不编造枚举、默认值或接口路径。
示例ID与凭证替换为实际值，并在完成后报告实际联调范围和未验证项。
```
