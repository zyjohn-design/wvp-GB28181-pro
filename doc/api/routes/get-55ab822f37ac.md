# GET /api/jt1078/config/get

[文档入口](../../第三方对接接口手册.md) · [所属模块条目](../modules/JT1078Controller.md#route-get-55ab822f37ac) · [AI 对接指南](../AI对接指南.md)

#### 基本信息

| 项 | 值 |
|---|---|
| 接口名称 | JT-查询终端参数 |
| 请求方法 | `GET` |
| 请求路径 | `/api/jt1078/config/get` |
| 认证方式 | access-token 或 api-key；已登录；需启用对应配置 |
| Content-Type | 无请求体，无须 Content-Type |
| 负责人 | 仓库未指定；由对应模块维护者确认 |
| 源码位置 | `src/main/java/com/genersoft/iot/vmp/jt1078/controller/JT1078Controller.java` / `JT1078Controller` |
| 源码返回类型 | `JTDeviceConfig` |

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
| `params` | Query / Form | array | 源码未声明；见业务校验 | JSON数组；元素类型见说明 | 源码未声明默认值 | 请求参数集合 | `[]` |

#### Body 参数

| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| — | — | — | — | — | — | 无字段 | — |

#### 请求示例

示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。

```http
GET /api/jt1078/config/get?phoneNumber=<phoneNumber>&params=[] HTTP/1.1
Host: <wvp-host>
access-token: <JWT>
```

#### 响应参数

| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |
|---|---|---|---|---|---|
| `code` | integer | 统一响应时提供 | 0成功；非0失败 | 业务状态码：0 成功，非 0 失败 | `0` |
| `msg` | string | 统一WVPResult中为非空说明 | 源码未声明格式或范围 | 结果说明；本系统实际字段为 msg | `"成功"` |
| `data` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 业务结果，依接口返回类型变化 | `按接口示例` |
| `data.keepaliveInterval` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 终端心跳发送间隔,单位为秒(s)（Java: Long） | `按接口示例` |
| `data.tcpResponseTimeout` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | TCP消息应答超时时间,单位为秒(s)（Java: Long） | `按接口示例` |
| `data.tcpRetransmissionCount` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | TCP消息重传次数（Java: Long） | `按接口示例` |
| `data.udpResponseTimeout` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | UDP消息应答超时时间,单位为秒(s)（Java: Long） | `按接口示例` |
| `data.udpRetransmissionCount` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | UDP消息重传次数（Java: Long） | `按接口示例` |
| `data.smsResponseTimeout` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | SMS 消息应答超时时间,单位为秒(s)（Java: Long） | `按接口示例` |
| `data.smsRetransmissionCount` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | SMS 消息重传次数（Java: Long） | `按接口示例` |
| `data.apnMaster` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 主服务器APN无线通信拨号访问点,若网络制式为 CDMA,则该处 为 PPP拨号号码（Java: String） | `按接口示例` |
| `data.dialingUsernameMaster` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 主服务器无线通信拨号用户名（Java: String） | `按接口示例` |
| `data.dialingPasswordMaster` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 主服务器无线通信拨号密码（Java: String） | `按接口示例` |
| `data.addressMaster` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 主服务器地址IP或域名,以冒号分割主机和端口 多个服务器使用分号分割（Java: String） | `按接口示例` |
| `data.apnBackup` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 备份服务器APN（Java: String） | `按接口示例` |
| `data.dialingUsernameBackup` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 备份服务器无线通信拨号用户名（Java: String） | `按接口示例` |
| `data.dialingPasswordBackup` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 备份服务器无线通信拨号密码（Java: String） | `按接口示例` |
| `data.addressBackup` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 备用服务器备份地址IP或域名,以冒号分割主机和端口 多个服务器使用分号分割（Java: String） | `按接口示例` |
| `data.addressIcMaster` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 道路运输证IC卡认证主服务器IP地址或域名（Java: String） | `按接口示例` |
| `data.tcpPortIcMaster` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 道路运输证IC卡认证主服务器TCP端口（Java: Long） | `按接口示例` |
| `data.udpPortIcMaster` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 道路运输证IC卡认证主服务器UDP端口（Java: Long） | `按接口示例` |
| `data.addressIcBackup` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 道路运输证IC卡认证备份服务器IP地址或域名,端口同主服务器（Java: String） | `按接口示例` |
| `data.locationReportingStrategy` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 位置汇报策略, 0定时汇报 1定距汇报 2定时和定距汇报（Java: Long） | `按接口示例` |
| `data.locationReportingPlan` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 位置汇报方案,0根据ACC状态 1根据登录状态和ACC状态,先判断登录状态,若登录再根据ACC状态（Java: Long） | `按接口示例` |
| `data.reportingIntervalOffline` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 驾驶员未登录汇报时间间隔,单位为秒,值大于零（Java: Long） | `按接口示例` |
| `data.apnSlave` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 从服务器 APN# 该值为空时 !终端应使用主服务器相同配置（Java: String） | `按接口示例` |
| `data.dialingUsernameSlave` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 从服务器无线通信拨号用户名 # 该值为空时 !终端应使用主服务器 相同配置（Java: String） | `按接口示例` |
| `data.dialingPasswordSlave` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 从服务器无线通信拨号密码 # 该值为空时 !终端应使用主服务器相 同配置（Java: String） | `按接口示例` |
| `data.addressSlave` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 从服务器备份地址 IP或域名 !主机和端口用冒号分割 !多个服务器 使用分号分割（Java: String） | `按接口示例` |
| `data.reportingIntervalDormancy` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 休眠时汇报时间间隔 单位为秒 值大于0（Java: Long） | `按接口示例` |
| `data.reportingIntervalEmergencyAlarm` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 紧急报警时汇报时间间隔 单位为秒 值大于0（Java: Long） | `按接口示例` |
| `data.reportingIntervalDefault` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 缺省时间汇报间隔 单位为秒 值大于0（Java: Long） | `按接口示例` |
| `data.reportingDistanceDefault` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 缺省距离汇报间隔 单位为米 值大于0（Java: Long） | `按接口示例` |
| `data.reportingDistanceOffline` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 驾驶员未登录汇报距离间隔 单位为米 值大于0（Java: Long） | `按接口示例` |
| `data.reportingDistanceDormancy` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 休眠时汇报距离间隔 单位为米 值大于0（Java: Long） | `按接口示例` |
| `data.reportingDistanceEmergencyAlarm` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 紧急报警时汇报距离间隔 单位为米 值大于0（Java: Long） | `按接口示例` |
| `data.inflectionPointAngle` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 拐点补传角度 ,值小于180（Java: Long） | `按接口示例` |
| `data.fenceRadius` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 电子围栏半径(非法位移國值) ,单位为米(m)（Java: Integer） | `按接口示例` |
| `data.illegalDrivingPeriods` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 违规行驶时段范围 ,精确到分（Java: JTIllegalDrivingPeriods） | `按接口示例` |
| `data.platformPhoneNumber` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 监控平台电话号码（Java: String） | `按接口示例` |
| `data.phoneNumberForReset` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 复位电话号码 ,可采用此电话号码拨打终端电话让终端复位（Java: String） | `按接口示例` |
| `data.phoneNumberForFactoryReset` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 恢复出厂设置电话号码 ,可采用此电话号码拨打终端电话让终端恢 复出厂设置（Java: String） | `按接口示例` |
| `data.phoneNumberForSms` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 监控平台 SMS 电话号码（Java: String） | `按接口示例` |
| `data.phoneNumberForReceiveTextAlarm` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 接收终端 SMS 文本报警号码（Java: String） | `按接口示例` |
| `data.phoneAnsweringPolicy` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 终端电话接听策略 。0:自动接听；1:ACC ON时自动接听 ,OFF时手动接听（Java: Long） | `按接口示例` |
| `data.longestCallTimeForPerSession` | integer | 源码未声明可空约束；按实际响应处理 | 整数；每次最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为不限制（Java: Long） | 每次最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为不限制（Java: Long） | `按接口示例` |
| `data.longestCallTimeInMonth` | integer | 源码未声明可空约束；按实际响应处理 | 整数；当月最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为 不限制（Java: Long） | 当月最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为 不限制（Java: Long） | `按接口示例` |
| `data.phoneNumbersForListen` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 监听电话号码（Java: String） | `按接口示例` |
| `data.privilegedSMSNumber` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 监管平台特权短信号码（Java: String） | `按接口示例` |
| `data.alarmMaskingWord` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 报警屏蔽字 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则相应报警被屏蔽（Java: JTAlarmSign） | `按接口示例` |
| `data.alarmSendsTextSmsSwitch` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 报警发送文本 SMS 开关 , 与位置信息汇报消息中的报警标志相对 应 ,相应位为1 则相应报警时发送文本 SMS（Java: JTAlarmSign） | `按接口示例` |
| `data.alarmShootingSwitch` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 报警拍摄开关 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则相应报警时摄像头拍摄（Java: JTAlarmSign） | `按接口示例` |
| `data.alarmShootingStorageFlags` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 报警拍摄存储标志 ,与位置信息汇报消息中的报警标志相对应 ,相应 位为1 则对相应报警时拍的照片进行存储 ,否则实时上传（Java: JTAlarmSign） | `按接口示例` |
| `data.KeySign` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 关键标志 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则 对相应报警为关键报警（Java: JTAlarmSign） | `按接口示例` |
| `data.maxSpeed` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 最高速度 ,单位为千米每小时(km/h)（Java: Long） | `按接口示例` |
| `data.overSpeedDuration` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 超速持续时间 ,单位为秒(s)（Java: Long） | `按接口示例` |
| `data.continuousDrivingTimeThreshold` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 连续驾驶时间门限 单位为秒(s)（Java: Long） | `按接口示例` |
| `data.cumulativeDrivingTimeThresholdForTheDay` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 当天累计驾驶时间门限 单位为秒(s)（Java: Long） | `按接口示例` |
| `data.minimumBreakTime` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 最小休息时间 单位为秒(s)（Java: Long） | `按接口示例` |
| `data.maximumParkingTime` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 最长停车时间 单位为秒(s)（Java: Long） | `按接口示例` |
| `data.overSpeedWarningDifference` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 超速预警差值 单位为1/10 千米每小时(1/10km/h)（Java: Integer） | `按接口示例` |
| `data.drowsyDrivingWarningDifference` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 疲劳驾驶预警差值 单位为秒 值大于零（Java: Integer） | `按接口示例` |
| `data.collisionAlarmParams` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 碰撞报警参数设置（Java: JTCollisionAlarmParams） | `按接口示例` |
| `data.rolloverAlarm` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 侧翻报警参数设置:侧翻角度,单位为度,默认为30（Java: Integer） | `按接口示例` |
| `data.cameraTimer` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 定时拍照控制（Java: JTCameraTimer） | `按接口示例` |
| `data.qualityForVideo` | integer | 源码未声明可空约束；按实际响应处理 | 整数；1~10 | 图像/视频质量 设置范围为1~10 1表示最优质量（Java: Long） | `按接口示例` |
| `data.brightness` | integer | 源码未声明可空约束；按实际响应处理 | 整数；0 ~ 255 | 亮度，设置范围为0 ~ 255（Java: Long） | `按接口示例` |
| `data.contrastRatio` | integer | 源码未声明可空约束；按实际响应处理 | 整数；0 ~ 127 | 对比度，设置范围为0 ~ 127（Java: Long） | `按接口示例` |
| `data.saturation` | integer | 源码未声明可空约束；按实际响应处理 | 整数；0 ~ 127 | 饱和度，设置范围为0 ~ 127（Java: Long） | `按接口示例` |
| `data.chroma` | integer | 源码未声明可空约束；按实际响应处理 | 整数；0 ~ 255 | 色度，设置范围为0 ~ 255（Java: Long） | `按接口示例` |
| `data.videoParam` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 音视频参数设置（Java: JTVideoParam） | `按接口示例` |
| `data.channelListParam` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 音视频通道列表设置（Java: JTChannelListParam） | `按接口示例` |
| `data.channelParam` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 单独视频通道参数设置（Java: JTChannelParam） | `按接口示例` |
| `data.alarmRecordingParam` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 特殊报警录像参数设置（Java: JTAlarmRecordingParam） | `按接口示例` |
| `data.videoAlarmBit` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 视频相关报警屏蔽字（Java: JTVideoAlarmBit） | `按接口示例` |
| `data.analyzeAlarmParam` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 图像分析报警参数设置（Java: JTAnalyzeAlarmParam） | `按接口示例` |
| `data.awakenParam` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | 终端休眠唤醒模式设置（Java: JTAwakenParam） | `按接口示例` |
| `data.mileage` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 车辆里程表读数，单位'1/10km（Java: Long） | `按接口示例` |
| `data.provincialId` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 车辆所在的省域ID（Java: Integer） | `按接口示例` |
| `data.cityId` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 车辆所在的市域ID（Java: Integer） | `按接口示例` |
| `data.licensePlate` | string | 源码未声明可空约束；按实际响应处理 | 源码未声明格式或范围 | 公安交通管理部门颁发的机动车号牌（Java: String） | `按接口示例` |
| `data.licensePlateColor` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | 车牌颜色,值按照JT/T697-7.2014中的规定,未上牌车辆填0（Java: Short） | `按接口示例` |
| `data.gnssPositioningMode` | object | 源码未声明可空约束；按实际响应处理 | JSON对象；字段见模型表 | GNSS定位模式（Java: JTGnssPositioningMode） | `按接口示例` |
| `data.gnssBaudRate` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | GNSS 波特率,定义如下: 0: 4800, 1:9600, 2：19200, 3:38400, 4:57600, 5:115200（Java: Short） | `按接口示例` |
| `data.gnssOutputFrequency` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | GNSS 模块详细定位数据输出频率,定义如下: 0: 500ms, 1:1000ms(默认值), 2：2000ms, 3:3000ms, 4:4000ms（Java: Short） | `按接口示例` |
| `data.gnssCollectionFrequency` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | GNSS 模块详细定位数据采集频率 ,单位为秒(s) ,默认为1（Java: Long） | `按接口示例` |
| `data.gnssDataUploadMethod` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | GNSS 模块详细定位数据上传方式:,定义如下: 0: 本地存储 ,不上传(默认值) , 1:按时间间隔上传, 2：按距离间隔上传, 11:按累计时间上传 ,达到传输时间后自动停止上传, 12:按累计距离上传 ,达到距离后自动停止上传, 13:按累计条数上传 ,达到上传条数后自动停止上传（Java: Short） | `按接口示例` |
| `data.gnssDataUploadMethodUnit` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | GNSS 模块详细定位数据上传设置:,定义如下: 1:单位为秒(s), 2：单位为米(m) , 11:单位为 秒(s), 12:单位为米(m), 13:单位 为条（Java: Long） | `按接口示例` |
| `data.canCollectionTimeForChannel1` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | CAN总线通道1 采集时间间隔 ,单位为毫秒(ms) ,0 表示不采集（Java: Long） | `按接口示例` |
| `data.canUploadIntervalForChannel1` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | CAN总线通道1 上传时间间隔 ,单位为秒(s) ,0 表示不上传（Java: Integer） | `按接口示例` |
| `data.canCollectionTimeForChannel2` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | CAN总线通道2 采集时间间隔 ,单位为毫秒(ms) ,0 表示不采集（Java: Long） | `按接口示例` |
| `data.canUploadIntervalForChannel2` | integer | 源码未声明可空约束；按实际响应处理 | 整数 | CAN总线通道2 上传时间间隔 ,单位为秒(s) ,0 表示不上传（Java: Integer） | `按接口示例` |

#### 成功响应

```json
{
  "code": 0,
  "msg": "成功",
  "data": {
    "keepaliveInterval": 1,
    "tcpResponseTimeout": 1,
    "tcpRetransmissionCount": 1,
    "udpResponseTimeout": 1
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

- [JTDeviceConfig](../models/JTDeviceConfig-89a223fda565.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceConfig.java`
