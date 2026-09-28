# API 模型字段索引

> 自动生成的历史模型索引，用于核查源码字段。完整模型字段及接口限制请使用[系统完整接口文档](../../第三方对接接口手册.md)。

本页汇总源码 `@Schema` 模型与 Controller 中使用的请求体、返回值模型。字段说明优先取源码 `@Schema`、`@ConfigAttribute` 或字段注释；其余采用生成脚本中的通用字段词典，具体取值、单位和行为仍以该模型源码及运行实例为准。继承模型还包含父类字段。

## InviteInfo

源码：`src/main/java/com/genersoft/iot/vmp/common/InviteInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | 设备国标编号 |
| `channelId` | `Integer` | 通道标识 |
| `stream` | `String` | 流 ID |
| `ssrcInfo` | `SSRCInfo` | RTP 流 SSRC 信息 |
| `receiveIp` | `String` | RTP 收流 IP |
| `receivePort` | `Integer` | RTP 收流端口 |
| `streamMode` | `String` | 媒体传输模式 |
| `type` | `InviteSessionType` | 业务类型；可选值见对应枚举 |
| `status` | `InviteSessionStatus` | 状态 |
| `streamInfo` | `StreamInfo` | 流信息对象 |
| `mediaServerId` | `String` | 流媒体节点 ID |
| `expirationTime` | `Long` | 到期时间 |
| `createTime` | `Long` | 创建时间 |

## StreamInfo：流信息

源码：`src/main/java/com/genersoft/iot/vmp/common/StreamInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `deviceId` | `String` | 设备编号 |
| `channelId` | `Integer` | 通道ID |
| `ip` | `String` | IP |
| `flv` | `StreamURL` | HTTP-FLV流地址 |
| `https_flv` | `StreamURL` | HTTPS-FLV流地址 |
| `ws_flv` | `StreamURL` | Websocket-FLV流地址 |
| `wss_flv` | `StreamURL` | Websockets-FLV流地址 |
| `fmp4` | `StreamURL` | HTTP-FMP4流地址 |
| `https_fmp4` | `StreamURL` | HTTPS-FMP4流地址 |
| `ws_fmp4` | `StreamURL` | Websocket-FMP4流地址 |
| `wss_fmp4` | `StreamURL` | Websockets-FMP4流地址 |
| `hls` | `StreamURL` | HLS流地址 |
| `https_hls` | `StreamURL` | HTTPS-HLS流地址 |
| `ws_hls` | `StreamURL` | Websocket-HLS流地址 |
| `wss_hls` | `StreamURL` | Websockets-HLS流地址 |
| `ts` | `StreamURL` | HTTP-TS流地址 |
| `https_ts` | `StreamURL` | HTTPS-TS流地址 |
| `ws_ts` | `StreamURL` | Websocket-TS流地址 |
| `wss_ts` | `StreamURL` | Websockets-TS流地址 |
| `rtmp` | `StreamURL` | RTMP流地址 |
| `rtmps` | `StreamURL` | RTMPS流地址 |
| `rtsp` | `StreamURL` | RTSP流地址 |
| `rtsps` | `StreamURL` | RTSPS流地址 |
| `rtc` | `StreamURL` | RTC流地址 |
| `rtcs` | `StreamURL` | RTCS流地址 |
| `mediaServer` | `MediaServer` | 流媒体节点 |
| `mediaInfo` | `MediaInfo` | 流编码信息 |
| `startTime` | `String` | 开始时间 |
| `endTime` | `String` | 结束时间 |
| `duration` | `Double` | 时长(回放时使用) |
| `progress` | `double` | 进度（录像下载使用） |
| `downLoadFilePath` | `DownloadFileInfo` | 文件下载地址（录像下载使用） |
| `callId` | `String` | 点播请求的callId |
| `pause` | `boolean` | 是否暂停（录像回放使用） |
| `originType` | `int` | 产生源类型，包括 unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7 |
| `originTypeStr` | `String` | originType的文本描述 |
| `transcodeStream` | `StreamInfo` | 转码后的视频流 |
| `serverId` | `String` | 使用的WVP ID |
| `key` | `String` | 流绑定的流媒体操作key |
| `transactionInfo` | `TransactionInfo` | SIP 事务信息 |

## StreamURL：流地址信息

源码：`src/main/java/com/genersoft/iot/vmp/common/StreamURL.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `protocol` | `String` | 协议 |
| `host` | `String` | 主机地址 |
| `port` | `int` | 端口 |
| `file` | `String` | 定位位置 |
| `url` | `String` | 拼接后的地址 |

## SystemAllInfo

源码：`src/main/java/com/genersoft/iot/vmp/common/SystemAllInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `cpu` | `List<Object>` | CPU 使用信息 |
| `mem` | `List<Object>` | 内存使用信息 |
| `net` | `List<Object>` | 网络使用信息 |
| `netTotal` | `long` | 网络累计流量 |
| `disk` | `Object` | 磁盘使用信息 |

## VersionPo

源码：`src/main/java/com/genersoft/iot/vmp/common/VersionPo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `GIT_Revision` | `String` | git的全版本号 |
| `Create_By` | `String` | maven版本 |
| `GIT_BRANCH` | `String` | git的分支 |
| `GIT_URL` | `String` | git的url |
| `BUILD_DATE` | `String` | 构建日期 |
| `GIT_DATE` | `String` | 构建日期 |
| `artifactId` | `String` | 项目名称 配合pom使用 |
| `GIT_Revision_SHORT` | `String` | git局部版本号 |
| `version` | `String` | 项目的版本如2.0.1.0 配合pom使用 |
| `project` | `String` | 子系统名称 |
| `Build_Jdk` | `String` | jdk版本 |

## LoginUser

源码：`src/main/java/com/genersoft/iot/vmp/conf/security/dto/LoginUser.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `user` | `User` | 用户 |
| `accessToken` | `String` | 登录后签发的 JWT |
| `serverId` | `String` | WVP 服务节点 ID |
| `loginTime` | `LocalDateTime` | 登录时间 |

## BasicParam：基础配置

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/BasicParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | 设备ID |
| `channelId` | `String` | 通道ID，如果时对设备配置直接设置同设备ID一样即可 |
| `name` | `String` | 名称 |
| `expiration` | `String` | 注册过期时间 |
| `heartBeatInterval` | `Integer` | 心跳间隔时间 |
| `heartBeatCount` | `Integer` | 心跳超时次数 |
| `positionCapability` | `Integer` | 定位功能支持情况。取值:0-不支持;1-支持 GPS定位;2-支持北斗定位(可选,默认取值为0)，用于接受配置查询结果， 基础配置时无效 |
| `longitude` | `Double` | 经度(可选)，用于接受配置查询结果， 基础配置时无效 |
| `latitude` | `Double` | 纬度(可选)，用于接受配置查询结果， 基础配置时无效 |

## CommonGBChannel：国标通道

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/CommonGBChannel.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `gbId` | `int` | 通用通道数据库自增ID；/api/common/channel 接口的 channelId 参数使用此值 |
| `gbDeviceId` | `String` | 国标-编码 |
| `gbName` | `String` | 国标-名称 |
| `gbManufacturer` | `String` | 国标-设备厂商 |
| `gbModel` | `String` | 国标-设备型号 |
| `gbOwner` | `String` | 国标-设备归属 |
| `gbCivilCode` | `String` | 国标-行政区域 |
| `gbBlock` | `String` | 国标-警区 |
| `gbAddress` | `String` | 国标-安装地址 |
| `gbParental` | `Integer` | 国标-是否有子设备 |
| `gbParentId` | `String` | 国标-父节点ID |
| `gbSafetyWay` | `Integer` | 国标-信令安全模式 |
| `gbRegisterWay` | `Integer` | 国标-注册方式 |
| `gbCertNum` | `String` | 国标-证书序列号 |
| `gbCertifiable` | `Integer` | 国标-证书有效标识 |
| `gbErrCode` | `Integer` | 国标-无效原因码(有证书且证书无效的设备必选) |
| `gbEndTime` | `String` | 国标-证书终止有效期(有证书且证书无效的设备必选) |
| `gbSecrecy` | `Integer` | 国标-保密属性(必选)缺省为0;0-不涉密,1-涉密 |
| `gbIpAddress` | `String` | 国标-设备/系统IPv4/IPv6地址 |
| `gbPort` | `Integer` | 国标-设备/系统端口 |
| `gbPassword` | `String` | 国标-设备口令 |
| `gbStatus` | `String` | 国标-设备状态 |
| `gbLongitude` | `Double` | 国标-经度 WGS-84坐标系 |
| `gbLatitude` | `Double` | 国标-纬度 WGS-84坐标系 |
| `gpsAltitude` | `Double` | GPS 海拔高度 |
| `gpsSpeed` | `Double` | GPS 速度 |
| `gpsDirection` | `Double` | GPS 方向角 |
| `gpsTime` | `String` | GPS 定位时间 |
| `gbBusinessGroupId` | `String` | 国标-虚拟组织所属的业务分组ID |
| `gbPtzType` | `Integer` | 国标-摄像机结构类型,标识摄像机类型: 1-球机; 2-半球; 3-固定枪机; 4-遥控枪机;5-遥控半球;6-多目设备的全景/拼接通道;7-多目设备的分割通道; 99-移动设备（非标）98-会议设备（非标） |
| `gbPositionType` | `Integer` | -摄像机位置类型扩展。1-省际检查站、2-党政机关、3-车站码头、4-中心广场、5-体育场馆、6-商业中心、7-宗教场所、8-校园周边、9-治安复杂区域、10-交通干线。当目录项为摄像机时可选。 |
| `gbRoomType` | `Integer` | 国标-摄像机安装位置室外、室内属性。1-室外、2-室内。 |
| `gbUseType` | `Integer` | 国标-用途属性 |
| `gbSupplyLightType` | `Integer` | 国标-摄像机补光属性。1-无补光;2-红外补光;3-白光补光;4-激光补光;9-其他 |
| `gbDirectionType` | `Integer` | 国标-摄像机监视方位(光轴方向)属性。1-东(西向东)、2-西(东向西)、3-南(北向南)、4-北(南向北)、5-东南(西北到东南)、6-东北(西南到东北)、7-西南(东北到西南)、8-西北(东南到西北) |
| `gbResolution` | `String` | 国标-摄像机支持的分辨率,可多值 |
| `gbDownloadSpeed` | `String` | 国标-下载倍速(可选),可多值 |
| `gbSvcSpaceSupportMod` | `Integer` | 国标-空域编码能力,取值0-不支持;1-1级增强(1个增强层);2-2级增强(2个增强层);3-3级增强(3个增强层) |
| `gbSvcTimeSupportMode` | `Integer` | 国标-时域编码能力,取值0-不支持;1-1级增强;2-2级增强;3-3级增强(可选) |
| `recordPLan` | `Long` | 二进制保存的录制计划, 每一位表示每个小时的前半个小时 |
| `dataType` | `Integer` | 关联的数据类型 |
| `dataDeviceId` | `Integer` | 关联的设备ID |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `streamId` | `String` | 流唯一编号，存在表示正在直播 |
| `enableBroadcast` | `Integer` | 是否支持对讲 1支持,0不支持 |
| `mapLevel` | `Integer` | 抽稀后的图层层级 |

## CommonRecordInfo

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/CommonRecordInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `startTime` | `String` | 开始时间 |
| `endTime` | `String` | 结束时间 |
| `fileSize` | `String` | 文件大小 |

## Device：国标设备/平台

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Device.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库自增ID |
| `deviceId` | `String` | 设备国标编号 |
| `name` | `String` | 名称 |
| `manufacturer` | `String` | 生产厂商 |
| `model` | `String` | 型号 |
| `firmware` | `String` | 固件版本 |
| `transport` | `String` | 传输协议（UDP/TCP） |
| `streamMode` | `String` | 数据流传输模式 |
| `ip` | `String` | IP |
| `port` | `int` | 端口 |
| `hostAddress` | `String` | wan地址 |
| `onLine` | `boolean` | 是否在线，true为在线，false为离线 |
| `registerTimeStamp` | `Long` | 注册时间戳 |
| `keepaliveTimeStamp` | `Long` | 心跳时间 |
| `heartBeatInterval` | `Integer` | 心跳间隔 |
| `heartBeatCount` | `Integer` | 心跳超时次数 |
| `positionCapability` | `Integer` | 定位功能支持情况。取值:0-不支持;1-支持 GPS定位;2-支持北斗定位(可选,默认取值为0 |
| `channelCount` | `int` | 通道个数 |
| `expires` | `int` | 注册有效期 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `mediaServerId` | `String` | 设备使用的媒体id, 默认为null |
| `charset` | `String` | 字符集, 支持 UTF-8、GB2312、GBK 与 GB18030 |
| `subscribeCycleForCatalog` | `int` | 目录订阅周期，o为不订阅 |
| `subscribeCycleForMobilePosition` | `int` | 移动设备位置订阅周期，0为不订阅 |
| `mobilePositionSubmissionInterval` | `int` | 移动设备位置信息上报时间间隔,单位:秒,默认值5 |
| `subscribeCycleForAlarm` | `int` | 报警心跳时间订阅周期，0为不订阅 |
| `ssrcCheck` | `boolean` | 是否开启ssrc校验，默认关闭，开启可以防止串流 |
| `geoCoordSys` | `String` | 地理坐标系， 目前支持 WGS84,GCJ02 |
| `password` | `String` | 密码 |
| `sdpIp` | `String` | 收流IP |
| `localIp` | `String` | SIP交互IP（设备访问平台的IP） |
| `asMessageChannel` | `boolean` | 是否作为消息通道 |
| `sipTransactionInfo` | `SipTransactionInfo` | 设备注册的事务信息 |
| `broadcastPushAfterAck` | `boolean` | 控制语音对讲流程，释放收到ACK后发流 |
| `serverId` | `String` | 所属服务Id |
| `typeCode` | `String` | 国标编号中的三位类型编码（派生字段） |
| `typeName` | `String` | 国标编号类型名称（派生字段） |
| `accessType` | `String` | 接入类型：DEVICE-独立设备，PLATFORM-下级平台（派生字段） |
| `accessTypeName` | `String` | 接入类型名称（派生字段） |

## DeviceAlarmNotify：报警通知

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DeviceAlarmNotify.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | 设备的国标编号 |
| `deviceName` | `String` | 设备名称 |
| `channelId` | `String` | 通道的国标编号 |
| `alarmPriority` | `String` | 报警级别, 1为一级警情, 2为二级警情, 3为三级警情, 4为四级警情 |
| `alarmPriorityDescription` | `String` | 报警级别, 1为一级警情, 2为二级警情, 3为三级警情, 4为四级警情 |
| `alarmMethod` | `Integer` | 报警方式 , 1为电话报警, 2为设备报警, 3为短信报警, 4为 GPS报警, 5为视频报警, 6为设备故障报警,\n\t * 7其他报警;可以为直接组合如12为电话报警或设备报警 |
| `alarmMethodDescription` | `String` | 报警方式说明 |
| `alarmTime` | `String` | 报警时间 |
| `alarmDescription` | `String` | 报警内容描述 |
| `longitude` | `double` | 经度 |
| `latitude` | `double` | 纬度 |
| `alarmType` | `Integer` | 报警类型 |
| `eventType` | `Integer` | 事件类型, 在入侵检测报警时可携带 |
| `alarmTypeDescription` | `String` | 报警类型描述 |
| `createTime` | `String` | 创建时间 |

## DeviceChannel：通道信息

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DeviceChannel.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库自增ID |
| `parentDeviceId` | `String` | 父设备编码 |
| `parentName` | `String` | 父设备名称 |
| `deviceId` | `String` | 编码 |
| `name` | `String` | 名称 |
| `manufacturer` | `String` | 设备厂商 |
| `model` | `String` | 设备型号 |
| `owner` | `String` | 设备归属 |
| `civilCode` | `String` | 行政区域 |
| `block` | `String` | 警区 |
| `address` | `String` | 安装地址 |
| `parental` | `Integer` | 是否有子设备(必选)1有,0没有 |
| `parentId` | `String` | 父节点ID |
| `safetyWay` | `Integer` | 信令安全模式 |
| `registerWay` | `Integer` | 注册方式 |
| `certNum` | `String` | 证书序列号 |
| `certifiable` | `Integer` | 证书有效标识, 缺省为0;证书有效标识:0:无效 1:有效 |
| `errCode` | `Integer` | 无效原因码(有证书且证书无效的设备必选) |
| `endTime` | `String` | 证书终止有效期(有证书且证书无效的设备必选) |
| `secrecy` | `Integer` | 保密属性(必选)缺省为0;0-不涉密,1-涉密 |
| `ipAddress` | `String` | 设备/系统IPv4/IPv6地址 |
| `port` | `Integer` | 设备/系统端口 |
| `password` | `String` | 设备口令 |
| `status` | `String` | 设备状态 |
| `longitude` | `Double` | 经度 WGS-84坐标系 |
| `latitude` | `Double` | ,纬度 WGS-84坐标系 |
| `ptzType` | `Integer` | 摄像机结构类型,标识摄像机类型: 1-球机; 2-半球; 3-固定枪机; 4-遥控枪机;5-遥控半球;6-多目设备的全景/拼接通道;7-多目设备的分割通道 |
| `positionType` | `Integer` | 摄像机位置类型扩展。1-省际检查站、2-党政机关、3-车站码头、4-中心广场、5-体育场馆、6-商业中心、7-宗教场所、8-校园周边、9-治安复杂区域、10-交通干线 |
| `roomType` | `Integer` | 摄像机安装位置室外、室内属性。1-室外、2-室内。 |
| `useType` | `Integer` | 用途属性， 1-治安、2-交通、3-重点。 |
| `supplyLightType` | `Integer` | 摄像机补光属性。1-无补光;2-红外补光;3-白光补光;4-激光补光;9-其他 |
| `directionType` | `Integer` | 摄像机监视方位(光轴方向)属性。1-东(西向东)、2-西(东向西)、3-南(北向南)、4-北(南向北)、5-东南(西北到东南)、6-东北(西南到东北)、7-西南(东北到西南)、8-西北(东南到西北) |
| `resolution` | `String` | 摄像机支持的分辨率,可多值 |
| `businessGroupId` | `String` | 虚拟组织所属的业务分组ID |
| `downloadSpeed` | `String` | 下载倍速(可选),可多值 |
| `svcSpaceSupportMod` | `Integer` | 空域编码能力,取值0-不支持;1-1级增强(1个增强层);2-2级增强(2个增强层);3-3级增强(3个增强层) |
| `svcTimeSupportMode` | `Integer` | 时域编码能力,取值0-不支持;1-1级增强;2-2级增强;3-3级增强(可选) |
| `ptzTypeText` | `String` | 云台类型描述字符串 |
| `subCount` | `int` | 子设备数 |
| `hasAudio` | `boolean` | 是否含有音频 |
| `gpsTime` | `String` | GPS的更新时间 |
| `streamIdentification` | `String` | 码流标识，优先级高于设备中码流标识，用于选择码流时组成码流标识。默认为null，不设置。可选值: stream/streamnumber/streamprofile/streamMode |
| `channelType` | `int` | 资源类型，0：视频通道，1：行政区划，2：业务分组/虚拟组织，3：平台节点，4：普通目录 |
| `dbKey` | `String` | 数据库或缓存键 |
| `dataType` | `Integer` | 关联的数据来源类型 |
| `resourceType` | `String` | 资源类型标识（派生字段） |
| `resourceTypeName` | `String` | 资源类型名称（派生字段） |
| `playable` | `boolean` | 是否为可点播的视频通道（派生字段） |
| `playDisabledReason` | `String` | 不可点播原因（派生字段） |

## DeviceRegisterAttempt：下级设备待处理注册请求

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DeviceRegisterAttempt.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | REGISTER From头中的设备国标编号 |
| `remoteIp` | `String` | 注册请求来源IP |
| `remotePort` | `int` | 注册请求来源端口 |
| `transport` | `String` | SIP传输协议 |
| `authUsername` | `String` | 认证用户名，不包含密码 |
| `status` | `String` | 状态: PENDING_CONFIG-待配置, AUTH_FAILED-认证失败 |
| `statusCode` | `int` | 返回给下级的SIP状态码 |
| `message` | `String` | 失败原因 |
| `suggestion` | `String` | 处理建议 |
| `firstAttemptTime` | `String` | 首次尝试时间 |
| `lastAttemptTime` | `String` | 最后尝试时间 |
| `attemptCount` | `int` | 尝试次数 |

## DeviceType

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DeviceType.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 编号 |
| `code` | `String` | 名称 |
| `ownerName` | `String` | 归属名称 |

## DragZoomParam：拉框放大/缩小控制参数

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DragZoomParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `length` | `Integer` | 播放窗口长度像素值(必选) |
| `width` | `Integer` | 播放窗口宽度像素值(必选) |
| `midPointX` | `Integer` | 拉框中心的横轴坐标像素值(必选) |
| `midPointY` | `Integer` | 拉框中心的纵轴坐标像素值(必选) |
| `lengthX` | `Integer` | 拉框长度像素值(必选) |
| `lengthY` | `Integer` | 拉框宽度像素值(必选) |

## DrawThinProcess

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DrawThinProcess.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `process` | `double` | 处理进度值 |
| `msg` | `String` | 结果或错误信息 |

## GbCode：国标编码对象

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/GbCode.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `centerCode` | `String` | 中心编码,由监控中心所在地的行政区划代码确定,符合GB/T2260—2007的要求 |
| `industryCode` | `String` | 行业编码 |
| `typeCode` | `String` | 类型编码 |
| `netCode` | `String` | 网络标识 |
| `sn` | `String` | 序号 |

## GbStream：直播流关联国标上级平台

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/GbStream.java`；继承 `PlatformGbStream`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `gbStreamId` | `int` | ID |
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `gbId` | `String` | 国标ID |
| `name` | `String` | 名称 |
| `mediaServerId` | `String` | 流媒体ID |
| `longitude` | `double` | 经度 |
| `latitude` | `double` | 纬度 |
| `streamType` | `String` | 流类型（拉流/推流） |
| `status` | `boolean` | 状态 |
| `createTime` | `String` | 创建时间 |

## Group：业务分组

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Group.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库自增ID |
| `deviceId` | `String` | 区域国标编号 |
| `name` | `String` | 区域名称 |
| `parentId` | `Integer` | 父分组ID |
| `parentDeviceId` | `String` | 父区域国标ID |
| `businessGroup` | `String` | 所属的业务分组国标编号 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `civilCode` | `String` | 行政区划 |
| `alias` | `String` | 别名， 此别名为唯一值，可以对接第三方是存储对方的ID |

## GroupTree：业务分组树

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/GroupTree.java`；继承 `Group`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `treeId` | `String` | 树节点ID |
| `isLeaf` | `boolean` | 是否有子节点 |
| `type` | `int` | 类型, 行政区划:0 摄像头: 1 |
| `status` | `String` | 在线状态 |

## IndustryCodeType

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/IndustryCodeType.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 接入类型码 |
| `code` | `String` | 名称 |
| `notes` | `String` | 备注 |

## MobilePosition

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/MobilePosition.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `channelId` | `Integer` | 通道数据库自增Id |
| `channelDeviceId` | `String` | 通道国标编号 |
| `timestamp` | `long` | 通知时间 |
| `longitude` | `double` | 经度 |
| `latitude` | `double` | 纬度 |
| `altitude` | `double` | 海拔高度 |
| `speed` | `double` | 速度 |
| `direction` | `double` | 方向 |
| `createTime` | `String` | 创建时间 |

## NetworkIdentificationType

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/NetworkIdentificationType.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 接入类型码 |
| `code` | `String` | 名称 |

## Platform：平台信息

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Platform.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `Integer` | ID(数据库中) |
| `enable` | `boolean` | 是否启用 |
| `name` | `String` | 名称 |
| `serverGBId` | `String` | SIP服务国标编码 |
| `serverGBDomain` | `String` | SIP服务国标域 |
| `serverIp` | `String` | SIP服务IP |
| `serverPort` | `int` | SIP服务端口 |
| `deviceGBId` | `String` | 设备国标编号 |
| `deviceIp` | `String` | 设备ip |
| `devicePort` | `int` | 设备端口 |
| `username` | `String` | SIP认证用户名(默认使用设备国标编号) |
| `password` | `String` | SIP认证密码 |
| `expires` | `int` | 注册周期 (秒) |
| `keepTimeout` | `int` | 心跳周期(秒) |
| `transport` | `String` | 传输协议 |
| `characterSet` | `String` | 字符集 |
| `ptz` | `boolean` | 允许云台控制 |
| `rtcp` | `boolean` | RTCP流保活 |
| `status` | `boolean` | 在线状态 |
| `channelCount` | `int` | 通道数量 |
| `catalogSubscribe` | `boolean` | 已被订阅目录信息 |
| `alarmSubscribe` | `boolean` | 已被订阅报警信息 |
| `mobilePositionSubscribe` | `boolean` | 已被订阅移动位置信息 |
| `catalogGroup` | `int` | 目录分组-每次向上级发送通道信息时单个包携带的通道数量，取值1,2,4,8 |
| `updateTime` | `String` | 更新时间 |
| `createTime` | `String` | 创建时间 |
| `asMessageChannel` | `boolean` | 是否作为消息通道 |
| `sendStreamIp` | `String` | 点播回复200OK使用的IP |
| `autoPushChannel` | `Boolean` | 是否自动推送通道变化 |
| `catalogWithPlatform` | `int` | 目录信息包含平台信息, 0：关闭，1：打开 |
| `catalogWithGroup` | `int` | 目录信息包含分组信息, 0：关闭，1：打开 |
| `catalogWithRegion` | `int` | 目录信息包含行政区划, 0：关闭，1：打开 |
| `civilCode` | `String` | 行政区划 |
| `manufacturer` | `String` | 平台厂商 |
| `model` | `String` | 平台型号 |
| `address` | `String` | 平台安装地址 |
| `registerWay` | `int` | 注册方式（必选）缺省为1； 1-符合IETF RFC 3261标准的认证注册模式；2-基于口令的双向认证注册模式；3-基于数字证书的双向认证注册模式(高安全级别要求)；4-基于数字证书的单向认证注册模式（高安全级别要求） |
| `secrecy` | `int` | 保密属性（必选）缺省为0；0-不涉密，1-涉密 |
| `serverId` | `String` | 执行注册的服务ID |
| `lastRegisterResult` | `PlatformRegisterResult` | 最近一次注册结果(不存储于数据库) |

## PlatformChannel

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/PlatformChannel.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | Id |
| `platformId` | `int` | 平台ID |
| `customDeviceId` | `String` | 国标-编码 |
| `customName` | `String` | 国标-名称 |
| `customManufacturer` | `String` | 国标-设备厂商 |
| `customModel` | `String` | 国标-设备型号 |
| `customOwner` | `String` | 国标-设备归属 |
| `customCivilCode` | `String` | 国标-行政区域 |
| `customBlock` | `String` | 国标-警区 |
| `customAddress` | `String` | 国标-安装地址 |
| `customParental` | `Integer` | 国标-是否有子设备 |
| `customParentId` | `String` | 国标-父节点ID |
| `customSafetyWay` | `Integer` | 国标-信令安全模式 |
| `customRegisterWay` | `Integer` | 国标-注册方式 |
| `customCertNum` | `Integer` | 国标-证书序列号 |
| `customCertifiable` | `Integer` | 国标-证书有效标识 |
| `customErrCode` | `Integer` | 国标-无效原因码(有证书且证书无效的设备必选) |
| `customEndTime` | `Integer` | 国标-证书终止有效期(有证书且证书无效的设备必选) |
| `customSecurityLevelCode` | `String` | 国标-摄像机安全能力等级代码 |
| `customSecrecy` | `Integer` | 国标-保密属性(必选)缺省为0;0-不涉密,1-涉密 |
| `customIpAddress` | `String` | 国标-设备/系统IPv4/IPv6地址 |
| `customPort` | `Integer` | 国标-设备/系统端口 |
| `customPassword` | `String` | 国标-设备口令 |
| `customStatus` | `String` | 国标-设备状态 |
| `customLongitude` | `Double` | 国标-经度 WGS-84坐标系 |
| `customLatitude` | `Double` | 国标-纬度 WGS-84坐标系 |
| `customBusinessGroupId` | `String` | 国标-虚拟组织所属的业务分组ID |
| `customPtzType` | `Integer` | 国标-摄像机结构类型,标识摄像机类型: 1-球机; 2-半球; 3-固定枪机; 4-遥控枪机;5-遥控半球;6-多目设备的全景/拼接通道;7-多目设备的分割通道 |
| `customPositionType` | `Integer` | -摄像机位置类型扩展。1-省际检查站、2-党政机关、3-车站码头、4-中心广场、5-体育场馆、6-商业中心、7-宗教场所、8-校园周边、9-治安复杂区域、10-交通干线。当目录项为摄像机时可选。 |
| `customPhotoelectricImagingTyp` | `String` | 国标-摄像机光电成像类型。1-可见光成像;2-热成像;3-雷达成像;4-X光成像;5-深度光场成像;9-其他。可多值, |
| `customCapturePositionType` | `String` | 国标-摄像机采集部位类型 |
| `customRoomType` | `Integer` | 国标-摄像机安装位置室外、室内属性。1-室外、2-室内。 |
| `customUseType` | `Integer` | 国标-用途属性 |
| `customSupplyLightType` | `Integer` | 国标-摄像机补光属性。1-无补光;2-红外补光;3-白光补光;4-激光补光;9-其他 |
| `customDirectionType` | `Integer` | 国标-摄像机监视方位(光轴方向)属性。1-东(西向东)、2-西(东向西)、3-南(北向南)、4-北(南向北)、5-东南(西北到东南)、6-东北(西南到东北)、7-西南(东北到西南)、8-西北(东南到西北) |
| `customResolution` | `String` | 国标-摄像机支持的分辨率,可多值 |
| `customStreamNumberList` | `String` | 国标-摄像机支持的码流编号列表,用于实时点播时指定码流编号(可选) |
| `customDownloadSpeed` | `String` | 国标-下载倍速(可选),可多值 |
| `customSvcSpaceSupportMod` | `Integer` | 国标-空域编码能力,取值0-不支持;1-1级增强(1个增强层);2-2级增强(2个增强层);3-3级增强(3个增强层) |
| `customSvcTimeSupportMode` | `Integer` | 国标-时域编码能力,取值0-不支持;1-1级增强;2-2级增强;3-3级增强(可选) |
| `customSsvcRatioSupportList` | `String` | 国标- SSVC增强层与基本层比例能力 |
| `customMobileDeviceType` | `Integer` | 国标-移动采集设备类型(仅移动采集设备适用,必选);1-移动机器人载摄像机;2-执法记录仪;3-移动单兵设备;4-车载视频记录设备;5-无人机载摄像机;9-其他 |
| `customHorizontalFieldAngle` | `Double` | 国标-摄像机水平视场角(可选),取值范围大于0度小于等于360度 |
| `customVerticalFieldAngle` | `Double` | 国标-摄像机竖直视场角(可选),取值范围大于0度小于等于360度 |
| `customMaxViewDistance` | `Double` | 国标-摄像机可视距离(可选),单位:米 |
| `customGrassrootsCode` | `String` | 国标-基层组织编码(必选,非基层建设时为“000000”) |
| `customPoType` | `Integer` | 国标-监控点位类型(当为摄像机时必选),1-一类视频监控点;2-二类视频监控点;3-三类视频监控点;9-其他点位。 |
| `customPoCommonName` | `String` | 国标-点位俗称 |
| `customMac` | `String` | 国标-设备MAC地址(可选),用“XX-XX-XX-XX-XX-XX”格式表达 |
| `customFunctionType` | `String` | 国标-摄像机卡口功能类型,01-人脸卡口;02-人员卡口;03-机动车卡口;04-非机动车卡口;05-物品卡口;99-其他 |
| `customEncodeType` | `String` | 国标-摄像机视频编码格式 |
| `customInstallTime` | `String` | 国标-摄像机安装使用时间 |
| `customManagementUnit` | `String` | 国标-摄像机所属管理单位名称 |
| `customContactInfo` | `String` | 国标-摄像机所属管理单位联系人的联系方式(电话号码,可多值,用英文半角“/”分割) |
| `customRecordSaveDays` | `Integer` | 国标-录像保存天数(可选) |
| `customIndustrialClassification` | `String` | 国标-国民经济行业分类代码(可选) |

## PlatformGbStream

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/PlatformGbStream.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `gbStreamId` | `int` | ID |
| `platformId` | `String` | 平台ID |
| `catalogId` | `String` | 目录ID |

## PlatformRegisterResult：国标级联注册结果

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/PlatformRegisterResult.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `platformId` | `String` | 上级平台国标编号 |
| `success` | `boolean` | 是否成功 |
| `stage` | `String` | 所处阶段: LOCAL_SEND/REGISTER/REGISTER_AUTH/KEEPALIVE |
| `statusCode` | `int` | SIP状态码, -1024表示本地等待超时 |
| `message` | `String` | 结果描述 |
| `suggestion` | `String` | 处理建议 |
| `time` | `String` | 发生时间 |
| `source` | `String` | 来源: TEST-手动测试注册, AUTO-平台自动注册 |

## Preset

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Preset.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `presetId` | `String` | 设备原样返回的预置位编号，可能不是数字；控制接口只接受1-255的整数 |
| `presetName` | `String` | 预置位名称 |

## RecordInfo：设备录像查询结果信息

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/RecordInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | 设备编号 |
| `channelId` | `String` | 通道编号 |
| `sn` | `String` | 命令序列号 |
| `name` | `String` | 设备名称 |
| `sumNum` | `int` | 列表总数 |
| `count` | `int` | 记录数量 |
| `lastTime` | `Instant` | 最近一次时间 |
| `recordList` | `List<RecordItem>` | 录像记录列表 |

## RecordItem：设备录像详情

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/RecordItem.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | 设备编号 |
| `name` | `String` | 名称 |
| `filePath` | `String` | 文件路径名 (可选) |
| `fileSize` | `String` | 录像文件大小,单位:Byte(可选) |
| `address` | `String` | 录像地址(可选) |
| `startTime` | `String` | 录像开始时间(可选) |
| `endTime` | `String` | 录像结束时间(可选) |
| `secrecy` | `int` | 保密属性(必选)缺省为0;0:不涉密,1:涉密 |
| `type` | `String` | 录像产生类型(可选)time或alarm 或 manual |
| `recorderId` | `String` | 录像触发者ID(可选) |

## Region：区域

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/Region.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库自增ID |
| `deviceId` | `String` | 区域国标编号 |
| `name` | `String` | 区域名称 |
| `parentId` | `Integer` | 父区域ID |
| `parentDeviceId` | `String` | 父区域国标ID |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |

## RegionTree：区域树

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/RegionTree.java`；继承 `Region`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `treeId` | `String` | 树节点ID |
| `isLeaf` | `boolean` | 是否有子节点 |
| `type` | `int` | 类型, 行政区划:0 摄像头: 1 |
| `status` | `String` | 在线状态 |

## SVACDecodeConfig：SVAC解码配置

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/SVACDecodeConfig.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `svcParam` | `SVCParam` | SVC参数 |
| `surveillanceParam` | `SurveillanceParam` | 监控专用信息参数 |

## SVACEncodeConfig：SVAC编码配置

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/SVACEncodeConfig.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `roiParam` | `ROIParam` | 感兴趣区域参数 |
| `svcParam` | `SVCParam` | SVC参数 |
| `surveillanceParam` | `SurveillanceParam` | 监控专用信息参数 |
| `audioParam` | `AudioParam` | 音频参数 |

## ShareGBChannel：国标共享通道

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/ShareGBChannel.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `platformId` | `int` | 平台ID |

## SyncStatus：摄像机同步状态

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/SyncStatus.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `total` | `Integer` | 总数 |
| `current` | `Integer` | 当前更新多少 |
| `errorMsg` | `String` | 错误描述 |
| `syncIng` | `Boolean` | 是否同步中 |
| `time` | `Instant` | 时间 |

## TimeStatistics：时间统计信息

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/TimeStatistics.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `time` | `String` | 时间 |
| `timeDiff` | `Long` | 时间差 |

## VideoParamOpt：视频参数范围

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/VideoParamOpt.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceId` | `String` | 设备ID |
| `downloadSpeed` | `String` | 下载倍速范围，各可选参数以 '/' 分隔 |
| `resolution` | `String` | 摄像机支持的分辨率，多个分辨率值以 '/' 分隔 |

## ChannelReduce：精简的channel信息展示

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ChannelReduce.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | deviceChannel的数据库自增ID |
| `channelId` | `String` | 通道国标编号 |
| `deviceId` | `String` | 设备国标编号 |
| `name` | `String` | 通道名 |
| `manufacturer` | `String` | 生产厂商 |
| `hostAddress` | `String` | wan地址 |
| `subCount` | `int` | 子节点数 |
| `platformId` | `String` | 平台上级国标编号 |
| `catalogId` | `String` | 目录国标编号 |

## ChannelToGroupByGbDeviceParam

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ChannelToGroupByGbDeviceParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceIds` | `List<Integer>` | 设备编号集合 |
| `parentId` | `String` | 父节点编号 |
| `businessGroup` | `String` | 业务分组编号 |

## ChannelToGroupParam

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ChannelToGroupParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `parentId` | `String` | 父节点编号 |
| `businessGroup` | `String` | 业务分组编号 |
| `channelIds` | `List<Integer>` | 通道 ID 集合 |

## ChannelToRegionByGbDeviceParam

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ChannelToRegionByGbDeviceParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceIds` | `List<Integer>` | 设备编号集合 |
| `civilCode` | `String` | 行政区划编码 |

## ChannelToRegionParam：提交行政区划关联多个通道的参数

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ChannelToRegionParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `civilCode` | `String` | 行政区划编号 |
| `channelIds` | `List<Integer>` | 选择的通道， 和all参数二选一 |
| `all` | `Boolean` | 所有通道， 和channelIds参数二选一 |

## DrawThinParam

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/DrawThinParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `zoomParam` | `Map<Integer, Double>` | 地图缩放参数 |
| `extent` | `Extent` | 地图经纬度范围 |
| `geoCoordSys` | `String` | 地理坐标系， WGS84/GCJ02， 用来标识 extent 参数的坐标系 |

## ResetParam

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/ResetParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `Integer` | 数据库记录 ID |
| `chanelFields` | `List<String>` | 需要重置的通道字段名列表 |

## UpdateChannelParam：通道关联参数

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/controller/bean/UpdateChannelParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `platformId` | `Integer` | 上级平台的数据库ID |
| `all` | `boolean` | 关联所有通道 |

## JTAlarmSign：报警标志

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAlarmSign.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `urgent` | `boolean` | 紧急报警，触动报警开关后触发 |
| `alarmSpeeding` | `boolean` | 超速报警 |
| `alarmTired` | `boolean` | 疲劳驾驶报警 |
| `alarmDangerous` | `boolean` | 危险驾驶行为报警 |
| `alarmGnssFault` | `boolean` | GNSS 模块发生故障报警 |
| `alarmGnssBreak` | `boolean` | GNSS 天线未接或被剪断报警 |
| `alarmGnssShortCircuited` | `boolean` | GNSS 天线短路报警 |
| `alarmUnderVoltage` | `boolean` | 终端主电源欠压报警 |
| `alarmPowerOff` | `boolean` | 终端主电源掉电报警 |
| `alarmLCD` | `boolean` | 终端 LCD或显示器故障报警 |
| `alarmTtsFault` | `boolean` | TTS 模块故障报警 |
| `alarmCameraFault` | `boolean` | 摄像头故障报警 |
| `alarmIcFault` | `boolean` | 道路运输证 IC卡模块故障报警 |
| `warningSpeeding` | `boolean` | 超速预警 |
| `warningTired` | `boolean` | 疲劳驾驶预警 |
| `alarmWrong` | `boolean` | 违规行驶报警 |
| `warningTirePressure` | `boolean` | 胎压预警 |
| `alarmBlindZone` | `boolean` | 右转盲区异常报警 |
| `alarmDrivingTimeout` | `boolean` | 当天累计驾驶超时报警 |
| `alarmParkingTimeout` | `boolean` | 超时停车报警 |
| `alarmRegion` | `boolean` | 进出区域报警 |
| `alarmRoute` | `boolean` | 进出路线报警 |
| `alarmTravelTime` | `boolean` | 路段行驶时间不足/过长报警 |
| `alarmRouteDeviation` | `boolean` | 路线偏离报警 |
| `alarmVSS` | `boolean` | 车辆 VSS 故障 |
| `alarmOil` | `boolean` | 车辆油量异常报警 |
| `alarmStolen` | `boolean` | 车辆被盗报警(通过车辆防盗器) |
| `alarmIllegalIgnition` | `boolean` | 车辆非法点火报警 |
| `alarmIllegalDisplacement` | `boolean` | 车辆非法位移报警 |
| `alarmRollover` | `boolean` | 碰撞侧翻报警 |
| `warningRollover` | `boolean` | 侧翻预警 |

## JTAreaAttribute：区域属性

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAreaAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `ruleForTimeLimit` | `boolean` | 是否启用起始时间与结束时间的判断规则 ,false：否；true：是 |
| `ruleForSpeedLimit` | `boolean` | 是否启用最高速度、超速持续时间和夜间最高速度的判断规则 ,false：否；true：是 |
| `ruleForAlarmToDriverWhenEnter` | `boolean` | 进区域是否报警给驾驶员,false：否；true：是 |
| `ruleForAlarmToPlatformWhenEnter` | `boolean` | 进区域是否报警给平台 ,false：否；true：是 |
| `ruleForAlarmToDriverWhenExit` | `boolean` | 出区域是否报警给驾驶员,false：否；true：是 |
| `ruleForAlarmToPlatformWhenExit` | `boolean` | 出区域是否报警给平台 ,false：否；true：是 |
| `southLatitude` | `boolean` | false：北纬；true：南纬 |
| `westLongitude` | `boolean` | false：东经；true：西经 |
| `prohibitOpeningDoors` | `boolean` | false：允许开门；true：禁止开门 |
| `ruleForTurnOffCommunicationWhenEnter` | `boolean` | false：进区域开启通信模块；true：进区域关闭通信模块 |
| `ruleForGnssWhenEnter` | `boolean` | false：进区域不采集 GNSS 详细定位数据；true：进区域采集 GNSS 详细定位数据 |

## JTChannel：jt808通道

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTChannel.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库自增ID |
| `name` | `String` | 名称 |
| `terminalDbId` | `int` | 设备的数据库ID |
| `channelId` | `Integer` | 通道ID |
| `hasAudio` | `boolean` | 是否含有音频 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `stream` | `String` | 流信息 |
| `dataType` | `Integer` | 关联的数据来源类型 |

## JTCircleArea：圆形区域

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTCircleArea.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 区域 ID |
| `attribute` | `JTAreaAttribute` | 设备或终端属性 |
| `latitude` | `Double` | 中心点纬度 |
| `longitude` | `Double` | 中心点经度 |
| `radius` | `long` | 半径,单位为米(m) |
| `startTime` | `String` | 起始时间, yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | 结束时间, yyyy-MM-dd HH:mm:ss |
| `maxSpeed` | `int` | 最高速度, 单位为千米每小时(km/h) |
| `overSpeedDuration` | `int` | 超速持续时间, 单位为秒(s) |
| `nighttimeMaxSpeed` | `int` | 夜间最高速度, 单位为千米每小时(km/h) |
| `name` | `String` | 区域的名称 |

## JTCommunicationModuleAttribute：JT通信模块属性

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTCommunicationModuleAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `gprs` | `boolean` | 是否支持 GPRS |
| `cdma` | `boolean` | 是否支持 CDMA |
| `tdScdma` | `boolean` | 是否支持 TD-SCDMA |
| `wcdma` | `boolean` | 是否支持 WCDMA |
| `cdma2000` | `boolean` | 是否支持 CDMA2000 |
| `tdLte` | `boolean` | 是否支持 TD-LTE |
| `other` | `boolean` | 其他通信能力 |

## JTConfirmationAlarmMessageType：人工确认报警类型

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTConfirmationAlarmMessageType.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `urgent` | `boolean` | 确认紧急报警 |
| `alarmDangerous` | `boolean` | 确认危险预警 |
| `alarmRegion` | `boolean` | 确认进出区域报警 |
| `alarmRoute` | `boolean` | 确认进出路线报警 |
| `alarmTravelTime` | `boolean` | 确认路段行驶时间不足/过长报警 |
| `alarmIllegalIgnition` | `boolean` | 确认车辆非法点火报警 |
| `alarmIllegalDisplacement` | `boolean` | 确认车辆非法位移报警 |

## JTDevice：jt808设备

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDevice.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库记录 ID |
| `provinceId` | `String` | 省域ID |
| `provinceText` | `String` | 省域文字描述 |
| `cityId` | `String` | 市县域ID |
| `cityText` | `String` | 市县域文字描述 |
| `makerId` | `String` | 制造商ID |
| `model` | `String` | 终端型号 |
| `phoneNumber` | `String` | 终端手机号 |
| `terminalId` | `String` | 终端ID |
| `plateColor` | `int` | 车牌颜色 |
| `plateNo` | `String` | 车牌 |
| `longitude` | `Double` | 经度 |
| `latitude` | `Double` | 纬度 |
| `registerTime` | `String` | 注册时间 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `status` | `boolean` | 状态 |
| `mediaServerId` | `String` | 设备使用的媒体id, 默认为null |
| `geoCoordSys` | `String` | 地理坐标系， 目前支持 WGS84,GCJ02 |
| `sdpIp` | `String` | 收流IP |

## JTDeviceAttribute：JT终端属性

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `type` | `JTDeviceType` | 终端类型 |
| `makerId` | `String` | 制造商 ID |
| `deviceModel` | `String` | 终端型号 |
| `terminalId` | `String` | 终端 ID |
| `iccId` | `String` | 终端 SIM卡 ICCID |
| `hardwareVersion` | `String` | 终端硬件版本号 |
| `firmwareVersion` | `String` | 固件版本号 |
| `gnssAttribute` | `JTGnssAttribute` | GNSS 模块属性 |
| `communicationModuleAttribute` | `JTCommunicationModuleAttribute` | 通信模块属性 |

## JTDeviceConfig：JT终端参数设置

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceConfig.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `keepaliveInterval` | `Long` | 终端心跳发送间隔,单位为秒(s) |
| `tcpResponseTimeout` | `Long` | TCP消息应答超时时间,单位为秒(s) |
| `tcpRetransmissionCount` | `Long` | TCP消息重传次数 |
| `udpResponseTimeout` | `Long` | UDP消息应答超时时间,单位为秒(s) |
| `udpRetransmissionCount` | `Long` | UDP消息重传次数 |
| `smsResponseTimeout` | `Long` | SMS 消息应答超时时间,单位为秒(s) |
| `smsRetransmissionCount` | `Long` | SMS 消息重传次数 |
| `apnMaster` | `String` | 主服务器APN无线通信拨号访问点,若网络制式为 CDMA,则该处 为 PPP拨号号码 |
| `dialingUsernameMaster` | `String` | 主服务器无线通信拨号用户名 |
| `dialingPasswordMaster` | `String` | 主服务器无线通信拨号密码 |
| `addressMaster` | `String` | 主服务器地址IP或域名,以冒号分割主机和端口 多个服务器使用分号分割 |
| `apnBackup` | `String` | 备份服务器APN |
| `dialingUsernameBackup` | `String` | 备份服务器无线通信拨号用户名 |
| `dialingPasswordBackup` | `String` | 备份服务器无线通信拨号密码 |
| `addressBackup` | `String` | 备用服务器备份地址IP或域名,以冒号分割主机和端口 多个服务器使用分号分割 |
| `addressIcMaster` | `String` | 道路运输证IC卡认证主服务器IP地址或域名 |
| `tcpPortIcMaster` | `Long` | 道路运输证IC卡认证主服务器TCP端口 |
| `udpPortIcMaster` | `Long` | 道路运输证IC卡认证主服务器UDP端口 |
| `addressIcBackup` | `String` | 道路运输证IC卡认证备份服务器IP地址或域名,端口同主服务器 |
| `locationReportingStrategy` | `Long` | 位置汇报策略, 0定时汇报 1定距汇报 2定时和定距汇报 |
| `locationReportingPlan` | `Long` | 位置汇报方案,0根据ACC状态 1根据登录状态和ACC状态,先判断登录状态,若登录再根据ACC状态 |
| `reportingIntervalOffline` | `Long` | 驾驶员未登录汇报时间间隔,单位为秒,值大于零 |
| `apnSlave` | `String` | 从服务器 APN# 该值为空时 !终端应使用主服务器相同配置 |
| `dialingUsernameSlave` | `String` | 从服务器无线通信拨号用户名 # 该值为空时 !终端应使用主服务器 相同配置 |
| `dialingPasswordSlave` | `String` | 从服务器无线通信拨号密码 # 该值为空时 !终端应使用主服务器相 同配置 |
| `addressSlave` | `String` | 从服务器备份地址 IP或域名 !主机和端口用冒号分割 !多个服务器 使用分号分割 |
| `reportingIntervalDormancy` | `Long` | 休眠时汇报时间间隔 单位为秒 值大于0 |
| `reportingIntervalEmergencyAlarm` | `Long` | 紧急报警时汇报时间间隔 单位为秒 值大于0 |
| `reportingIntervalDefault` | `Long` | 缺省时间汇报间隔 单位为秒 值大于0 |
| `reportingDistanceDefault` | `Long` | 缺省距离汇报间隔 单位为米 值大于0 |
| `reportingDistanceOffline` | `Long` | 驾驶员未登录汇报距离间隔 单位为米 值大于0 |
| `reportingDistanceDormancy` | `Long` | 休眠时汇报距离间隔 单位为米 值大于0 |
| `reportingDistanceEmergencyAlarm` | `Long` | 紧急报警时汇报距离间隔 单位为米 值大于0 |
| `inflectionPointAngle` | `Long` | 拐点补传角度 ,值小于180 |
| `fenceRadius` | `Integer` | 电子围栏半径(非法位移國值) ,单位为米(m) |
| `illegalDrivingPeriods` | `JTIllegalDrivingPeriods` | 违规行驶时段范围 ,精确到分 |
| `platformPhoneNumber` | `String` | 监控平台电话号码 |
| `phoneNumberForReset` | `String` | 复位电话号码 ,可采用此电话号码拨打终端电话让终端复位 |
| `phoneNumberForFactoryReset` | `String` | 恢复出厂设置电话号码 ,可采用此电话号码拨打终端电话让终端恢 复出厂设置 |
| `phoneNumberForSms` | `String` | 监控平台 SMS 电话号码 |
| `phoneNumberForReceiveTextAlarm` | `String` | 接收终端 SMS 文本报警号码 |
| `phoneAnsweringPolicy` | `Long` | 终端电话接听策略 。0:自动接听；1:ACC ON时自动接听 ,OFF时手动接听 |
| `longestCallTimeForPerSession` | `Long` | 每次最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为不限制 |
| `longestCallTimeInMonth` | `Long` | 当月最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为 不限制 |
| `phoneNumbersForListen` | `String` | 监听电话号码 |
| `privilegedSMSNumber` | `String` | 监管平台特权短信号码 |
| `alarmMaskingWord` | `JTAlarmSign` | 报警屏蔽字 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则相应报警被屏蔽 |
| `alarmSendsTextSmsSwitch` | `JTAlarmSign` | 报警发送文本 SMS 开关 , 与位置信息汇报消息中的报警标志相对 应 ,相应位为1 则相应报警时发送文本 SMS |
| `alarmShootingSwitch` | `JTAlarmSign` | 报警拍摄开关 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则相应报警时摄像头拍摄 |
| `alarmShootingStorageFlags` | `JTAlarmSign` | 报警拍摄存储标志 ,与位置信息汇报消息中的报警标志相对应 ,相应 位为1 则对相应报警时拍的照片进行存储 ,否则实时上传 |
| `KeySign` | `JTAlarmSign` | 关键标志 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则 对相应报警为关键报警 |
| `maxSpeed` | `Long` | 最高速度 ,单位为千米每小时(km/h) |
| `overSpeedDuration` | `Long` | 超速持续时间 ,单位为秒(s) |
| `continuousDrivingTimeThreshold` | `Long` | 连续驾驶时间门限 单位为秒(s) |
| `cumulativeDrivingTimeThresholdForTheDay` | `Long` | 当天累计驾驶时间门限 单位为秒(s) |
| `minimumBreakTime` | `Long` | 最小休息时间 单位为秒(s) |
| `maximumParkingTime` | `Long` | 最长停车时间 单位为秒(s) |
| `overSpeedWarningDifference` | `Integer` | 超速预警差值 单位为1/10 千米每小时(1/10km/h) |
| `drowsyDrivingWarningDifference` | `Integer` | 疲劳驾驶预警差值 单位为秒 值大于零 |
| `collisionAlarmParams` | `JTCollisionAlarmParams` | 碰撞报警参数设置 |
| `rolloverAlarm` | `Integer` | 侧翻报警参数设置:侧翻角度,单位为度,默认为30 |
| `cameraTimer` | `JTCameraTimer` | 定时拍照控制 |
| `qualityForVideo` | `Long` | 图像/视频质量 设置范围为1~10 1表示最优质量 |
| `brightness` | `Long` | 亮度，设置范围为0 ~ 255 |
| `contrastRatio` | `Long` | 对比度，设置范围为0 ~ 127 |
| `saturation` | `Long` | 饱和度，设置范围为0 ~ 127 |
| `chroma` | `Long` | 色度，设置范围为0 ~ 255 |
| `videoParam` | `JTVideoParam` | 音视频参数设置 |
| `channelListParam` | `JTChannelListParam` | 音视频通道列表设置 |
| `channelParam` | `JTChannelParam` | 单独视频通道参数设置 |
| `alarmRecordingParam` | `JTAlarmRecordingParam` | 特殊报警录像参数设置 |
| `videoAlarmBit` | `JTVideoAlarmBit` | 视频相关报警屏蔽字 |
| `analyzeAlarmParam` | `JTAnalyzeAlarmParam` | 图像分析报警参数设置 |
| `awakenParam` | `JTAwakenParam` | 终端休眠唤醒模式设置 |
| `mileage` | `Long` | 车辆里程表读数，单位'1/10km |
| `provincialId` | `Integer` | 车辆所在的省域ID |
| `cityId` | `Integer` | 车辆所在的市域ID |
| `licensePlate` | `String` | 公安交通管理部门颁发的机动车号牌 |
| `licensePlateColor` | `Short` | 车牌颜色,值按照JT/T697-7.2014中的规定,未上牌车辆填0 |
| `gnssPositioningMode` | `JTGnssPositioningMode` | GNSS定位模式 |
| `gnssBaudRate` | `Short` | GNSS 波特率,定义如下: 0: 4800, 1:9600, 2：19200, 3:38400, 4:57600, 5:115200 |
| `gnssOutputFrequency` | `Short` | GNSS 模块详细定位数据输出频率,定义如下: 0: 500ms, 1:1000ms(默认值), 2：2000ms, 3:3000ms, 4:4000ms |
| `gnssCollectionFrequency` | `Long` | GNSS 模块详细定位数据采集频率 ,单位为秒(s) ,默认为1 |
| `gnssDataUploadMethod` | `Short` | GNSS 模块详细定位数据上传方式:,定义如下: 0: 本地存储 ,不上传(默认值) , 1:按时间间隔上传, 2：按距离间隔上传, 11:按累计时间上传 ,达到传输时间后自动停止上传, 12:按累计距离上传 ,达到距离后自动停止上传, 13:按累计条数上传 ,达到上传条数后自动停止上传 |
| `gnssDataUploadMethodUnit` | `Long` | GNSS 模块详细定位数据上传设置:,定义如下: 1:单位为秒(s), 2：单位为米(m) , 11:单位为 秒(s), 12:单位为米(m), 13:单位 为条 |
| `canCollectionTimeForChannel1` | `Long` | CAN总线通道1 采集时间间隔 ,单位为毫秒(ms) ,0 表示不采集 |
| `canUploadIntervalForChannel1` | `Integer` | CAN总线通道1 上传时间间隔 ,单位为秒(s) ,0 表示不上传 |
| `canCollectionTimeForChannel2` | `Long` | CAN总线通道2 采集时间间隔 ,单位为毫秒(ms) ,0 表示不采集 |
| `canUploadIntervalForChannel2` | `Integer` | CAN总线通道2 上传时间间隔 ,单位为秒(s) ,0 表示不上传 |

## JTDeviceConnectionControl：终端控制

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceConnectionControl.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `switchOn` | `Boolean` | false 表示切换到指定监管平台服务器 ,true 表示切换回原 缺省监控平台服务器 |
| `authentication` | `String` | 监管平台鉴权码 |
| `name` | `String` | 拨号点名称 |
| `username` | `String` | 拨号用户名 |
| `password` | `String` | 拨号密码 |
| `address` | `String` | 地址 |
| `tcpPort` | `Integer` | TCP端口 |
| `udpPort` | `Integer` | UDP端口 |
| `timeLimit` | `Long` | 连接到指定服务器时限 |

## JTDeviceType：JT终端参数设置

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceType.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `passengerVehicles` | `boolean` | 适用客运车辆 |
| `dangerousGoodsVehicles` | `boolean` | 适用危险品车辆 |
| `freightVehicles` | `boolean` | 普通货运车辆 |
| `rentalVehicles` | `boolean` | 出租车辆 |
| `hardDiskRecording` | `boolean` | 支持硬盘录像 |
| `splittingMachine` | `boolean` | false：一体机 ,true：分体机 |
| `trailer` | `boolean` | 适用挂车 |

## JTDriverInformation：驾驶员身份信息

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDriverInformation.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `status` | `int` | 0x01:从业资格证 IC卡插入( 驾驶员上班)；0x02:从 业资格证 IC卡拔出(驾驶员下班) |
| `time` | `String` | 插卡/拔卡时间 ,以下字段在状 态为0x01 时才有效并做填充 |
| `result` | `Integer` | IC卡读取结果:0x00:IC卡读卡成功；0x01:读卡失败 ,原因为卡片密钥认证未通过；0x02:读卡失败 ,原因为卡片已被锁定；0x03:读卡失败 ,原因为卡片被拔出；0x04:读卡失败 ,原因为数据校验错误。以下字段在 IC卡读取结果等于0x00 时才有效 |
| `name` | `String` | 驾驶员姓名 |
| `certificateCode` | `String` | 从业资格证编码 |
| `certificateIssuanceMechanismName` | `String` | 发证机构名称 |
| `expire` | `String` | 证件有效期 |
| `driverIdNumber` | `String` | 驾驶员身份证号 |

## JTGnssAttribute：JTGNSS 模块属性

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTGnssAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `gps` | `boolean` | 是否支持 GPS |
| `beidou` | `boolean` | 是否支持北斗定位 |
| `glonass` | `boolean` | 是否支持 GLONASS 定位 |
| `gaLiLeo` | `boolean` | 是否支持 Galileo 定位 |

## JTMediaAttribute

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTMediaAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `audioEncoder` | `int` | 输入音频编码方式: 1 G. 721 2 G. 722 3 G. 723 4 G. 728 5 G. 729 6 G. 711A 7 G. 711U 8 G. 726 9 G. 729A 10 DVI4_3 11 DVI4_4 12 DVI4_8K 13 DVI4_16K 14 LPC 15 S16BE_STEREO 16 S16BE_MONO 17 MPEGAUDIO 18 LPCM 19 AAC 20 WMA9STD 21 HEAAC 22 PCM_VOICE 23 PCM_AUDIO 24 AACLC 25 MP3 26 ADPCMA 27 MP4AUDIO 28 AMR |
| `audioChannels` | `int` | 输入音频声道数 |
| `audioSamplingRate` | `int` | 输入音频采样率: 0:8 kHz; 1:22. 05 kHz; 2:44. 1 kHz; 3:48 kHz |
| `audioSamplingBits` | `int` | 输入音频采样位数: 0:8 位; 1:16 位; 2:32 位 |
| `audioFrameLength` | `int` | 音频帧长度: 范围 1 ~ 4 294 967 295 |
| `audioOutputEnable` | `int` | 是否支持音频输出: 0:不支持;1:支持 |
| `videoEncoder` | `int` | 视频编码方式: 98 H. 264 99 H. 265 100 AVS 101 SVAC |
| `audioChannelMax` | `int` | 终端支持的最大音频物理通道数量: |
| `videoChannelMax` | `int` | 终端支持的最大视频物理通道数量: |

## JTMediaDataInfo：多媒体检索项数据

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTMediaDataInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 多媒体数据 ID |
| `type` | `int` | 多媒体类型, 0：图像；1：音频；2：视频 |
| `eventCode` | `int` | 事件项编码: 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰 撞侧翻报警触发；4：门开拍照；5：门关拍照；6：车门由开 变关 ,车速从小于20km到超过20km；7：定距拍照 |
| `channelId` | `int` | 通道 ID |
| `positionBaseInfo` | `JTPositionBaseInfo` | 表示拍摄或录制的起始时刻的汇报消息 |

## JTMediaEventInfo：多媒体事件信息

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTMediaEventInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 多媒体数据 ID |
| `type` | `int` | 多媒体类型, 0：图像；1：音频；2：视频 |
| `code` | `int` | 多媒体格式编码, 0：JPEG；1：TIF；2：MP3；3：WAV；4：WMV；其他保留 |
| `eventCode` | `int` | 事件项编码: 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰 撞侧翻报警触发；4：门开拍照；5：门关拍照；6：车门由开 变关 ,车速从小于20km到超过20km；7：定距拍照 |
| `channelId` | `int` | 通道 ID |
| `mediaData` | `byte[]` | 媒体数据 |
| `positionBaseInfo` | `JTPositionBaseInfo` | 位置信息汇报 |

## JTPhoneBookContact：电话本联系人

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPhoneBookContact.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `sign` | `int` | 1:呼入,2:呼出,3:呼入/呼出 |
| `phoneNumber` | `String` | 电话号码 |
| `contactName` | `String` | 联系人 |

## JTPolygonArea：多边形区域

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPolygonArea.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 区域 ID |
| `attribute` | `JTAreaAttribute` | 设备或终端属性 |
| `startTime` | `String` | 起始时间, yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | 结束时间, yyyy-MM-dd HH:mm:ss |
| `maxSpeed` | `int` | 最高速度, 单位为千米每小时(km/h) |
| `overSpeedDuration` | `int` | 超速持续时间, 单位为秒(s) |
| `polygonPoints` | `List<JTPolygonPoint>` | 区域顶点 |
| `nighttimeMaxSpeed` | `int` | 夜间最高速度, 单位为千米每小时(km/h) |
| `name` | `String` | 区域的名称 |

## JTPolygonPoint：多边形区域的顶点

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPolygonPoint.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `latitude` | `Double` | 顶点纬度 |
| `longitude` | `Double` | 顶点经度 |

## JTPositionAdditionalInfo：位置附加信息

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPositionAdditionalInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `mileage` | `int` | 里程, 单位为1/10km, 对应车上里程表读数 |
| `oil` | `int` | 油量, 单位为1/10L, 对应车上油量表读数 |
| `speed` | `int` | 行驶记录功能获取的速度,单位为1/10km/h |
| `alarmId` | `int` | 报警事件的 ID |
| `carriageTemperature` | `int` | 车厢温度 ,单位为摄氏度 |

## JTPositionBaseInfo：位置基本信息

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPositionBaseInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `alarmSign` | `JTAlarmSign` | 报警标志 |
| `status` | `JTStatus` | 状态 |
| `longitude` | `Double` | 经度 |
| `latitude` | `Double` | 纬度 |
| `altitude` | `Integer` | 高程 |
| `speed` | `Integer` | 速度 |
| `direction` | `Integer` | 方向 |
| `time` | `String` | 时间 |
| `videoAlarm` | `JTVideoAlarm` | 视频报警 |

## JTPositionInfo：位置信息

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTPositionInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `base` | `JTPositionBaseInfo` | 位置基本信息 |
| `additional` | `JTPositionAdditionalInfo` | 位置附加信息 |

## JTQueryMediaDataCommand：存储多媒体数据

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTQueryMediaDataCommand.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `type` | `int` | 多媒体类型: 0：图像；1：音频；2：视频 |
| `chanelId` | `int` | 通道 ID, 0 表示检索该媒体类型的所有通道 |
| `event` | `int` | 事件项编码: 0：平台下发指令；1：定时动作；2：抢劫报警触发；3：碰 撞侧翻报警触发；其他保留 |
| `startTime` | `String` | 开始时间 |
| `endTime` | `String` | 结束时间 |
| `delete` | `Integer` | 删除标志, 0:保留；1:删除, 存储多媒体数据上传命令中使用 |

## JTRectangleArea：矩形区域

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRectangleArea.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 区域 ID |
| `attribute` | `JTAreaAttribute` | 设备或终端属性 |
| `latitudeForUpperLeft` | `Double` | 左上点纬度 |
| `longitudeForUpperLeft` | `Double` | 左上点经度 |
| `latitudeForLowerRight` | `Double` | 右下点纬度 |
| `longitudeForLowerRight` | `Double` | 右下点经度 |
| `startTime` | `String` | 起始时间, yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | 结束时间, yyyy-MM-dd HH:mm:ss |
| `maxSpeed` | `int` | 最高速度, 单位为千米每小时(km/h) |
| `overSpeedDuration` | `int` | 超速持续时间, 单位为秒(s) |
| `nighttimeMaxSpeed` | `int` | 夜间最高速度, 单位为千米每小时(km/h) |
| `name` | `String` | 区域的名称 |

## JTRoute：路线

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRoute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 路线 ID |
| `attribute` | `JTRouteAttribute` | 路线属性 |
| `startTime` | `String` | 起始时间, yyyy-MM-dd HH:mm:ss |
| `endTime` | `String` | 结束时间, yyyy-MM-dd HH:mm:ss |
| `routePointList` | `List<JTRoutePoint>` | 路线拐点 |
| `name` | `String` | 区域的名称 |

## JTRouteAttribute：路线属性

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRouteAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `ruleForTimeLimit` | `boolean` | 是否启用起始时间与结束时间的判断规则 ,false：否；true：是 |
| `ruleForAlarmToDriverWhenEnter` | `boolean` | 进区域是否报警给驾驶员,false：否；true：是 |
| `ruleForAlarmToPlatformWhenEnter` | `boolean` | 进区域是否报警给平台 ,false：否；true：是 |
| `ruleForAlarmToDriverWhenExit` | `boolean` | 出区域是否报警给驾驶员,false：否；true：是 |
| `ruleForAlarmToPlatformWhenExit` | `boolean` | 出区域是否报警给平台 ,false：否；true：是 |

## JTRoutePoint：路线拐点

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRoutePoint.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `long` | 拐点 ID |
| `routeSectionId` | `long` | 路段 ID |
| `latitude` | `Double` | 拐点纬度 |
| `longitude` | `Double` | 拐点经度 |
| `routeSectionAttributeWidth` | `int` | 路段宽度 |
| `routeSectionAttribute` | `JTRouteSectionAttribute` | 路段属性 |
| `routeSectionMaxLength` | `int` | 路段行驶过长國值 |
| `routeSectionMinLength` | `int` | 路段行驶不足國值 |
| `routeSectionMaxSpeed` | `int` | 路段最高速度 |
| `routeSectionOverSpeedDuration` | `int` | 路段超速持续时间 |
| `routeSectionNighttimeMaxSpeed` | `int` | 路段夜间最高速度 |

## JTRouteSectionAttribute：路段属性

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTRouteSectionAttribute.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `ruleForTimeLimit` | `boolean` | 行驶时间 ,false：否；true：是 |
| `ruleForSpeedLimit` | `boolean` | 限速 ,false：否；true：是 |
| `southLatitude` | `boolean` | false：北纬；true：南纬 |
| `westLongitude` | `boolean` | false：东经；true：西经 |

## JTShootingCommand：拍摄命令参数

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTShootingCommand.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `chanelId` | `int` | 通道 ID |
| `command` | `int` | 0:停止拍摄；0xFFFF:录像；其他:拍照张数 |
| `time` | `int` | 拍照间隔/录像时间, 单位为秒(s) ,0 表示按最小间隔拍照或一直录像 |
| `save` | `int` | 1:保存； 0:实时上传 |
| `resolvingPower` | `int` | 分辨率: 0x00:最低分辨率0x01:320 x240；0x02:640 x480；0x03:800 x600；0x04:1024 x768；0x05:176 x144；0x06:352 x288；0x07:704 x288；0x08:704 x576；0xff:最高分辨率 |
| `quality` | `int` | 图像/视频质量: 取值范围为 1 ~ 10 ,1 代表质量损失最小 ,10 表示压缩 比最大 |
| `brightness` | `int` | 亮度, 0 ~ 255 |
| `contrastRatio` | `int` | 对比度,0 ~ 127 |
| `saturation` | `int` | 饱和度,0 ~ 127 |
| `chroma` | `int` | 色度,0 ~ 255 |

## JTStatus：状态信息

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTStatus.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `acc` | `boolean` | false:ACC关；true: ACC开 |
| `positioning` | `boolean` | false:未定位；true: 定位 |
| `southLatitude` | `boolean` | false:北纬；true: 南纬 |
| `wesLongitude` | `boolean` | false:东经；true: 西经 |
| `outage` | `boolean` | false:运营状态；true: 停运状态 |
| `positionEncryption` | `boolean` | false:经纬度未经保密插件加密；true: 经纬度已经保密插件加密 |
| `warningFrontCrash` | `boolean` | true: 紧急刹车系统采集的前撞预警 |
| `warningShifting` | `boolean` | true: 车道偏移预警 |
| `load` | `int` | 00:空车；01:半载；10:保留；11:满载。可表示客车的空载状态 ,重车及货车的空载、满载状态 ,该状态可由人工输入或传感器获取 |
| `oilWayBreak` | `boolean` | false:车辆油路正常；true: 车辆油路断开 |
| `circuitBreak` | `boolean` | false:车辆电路正常；true: 车辆电路断开 |
| `doorLocking` | `boolean` | false:车门解锁；true: 车门加锁 |
| `door1Open` | `boolean` | false:门1 关；true: 门1 开(前门) |
| `door2Open` | `boolean` | false:门2 关；true: 门2 开(中门) |
| `door3Open` | `boolean` | false:门3 关；true: 门3 开(后门) |
| `door4Open` | `boolean` | false:门4 关；true: 门4 开(驾驶席门) |
| `door5Open` | `boolean` | false:门5 关；true: 门5 开(自定义) |
| `gps` | `boolean` | false:未使用 GPS 卫星进行定位；true:使用 GPS 卫星进行定位 |
| `beidou` | `boolean` | false:未使用北斗卫星进行定位；true:使用北斗卫星进行定位 |
| `glonass` | `boolean` | false:未使用GLONASS 卫星进行定位；true:使用GLONASS 卫星进行定位 |
| `gaLiLeo` | `boolean` | false:未使用GaLiLeo 卫星进行定位；true:使用GaLiLeo 卫星进行定位 |
| `driving` | `boolean` | false:车辆处于停止状态；true:车辆处于行驶状态 |

## JTTextSign：文本信息标志

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTTextSign.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `type` | `int` | 1紧急,2服务,3通知 |
| `terminalDisplay` | `boolean` | 1终端显示器显示 |
| `adScreen` | `boolean` | 1广告屏显示 |
| `tts` | `boolean` | 1终端 TTS 播读 |
| `source` | `boolean` | false: 中心导航信息 true CAN故障码信息 |

## JTVideoAlarm：视频报警上报

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTVideoAlarm.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `videoLossChannels` | `List<Integer>` | 视频信号丢失报警的通道 |
| `videoOcclusionChannels` | `List<Integer>` | 视频信号遮挡报警的通道 |
| `storageFaultAlarm` | `List<Integer>` | 存储器故障报警状态，第 1-12 个主存储器，12-15 分别表示第 1-4 个灾备存储装置 |
| `drivingForFatigue` | `boolean` | 异常驾驶行为-疲劳 |
| `drivingForCall` | `boolean` | 异常驾驶行为-打电话 |
| `drivingSmoking` | `boolean` | 异常驾驶行为-抽烟 |
| `otherDeviceFailure` | `boolean` | 其他视频设备故障 |
| `overcrowding` | `boolean` | 客车超员报警 |
| `specialRecordFull` | `boolean` | 特殊报警录像达到存储阈值报警 |

## ConfirmationAlarmMessageParam：人工确认报警消息参数

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/ConfirmationAlarmMessageParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `alarmPackageNo` | `int` | 报警消息流水号 |
| `alarmMessageType` | `JTConfirmationAlarmMessageType` | 人工确认报警类型 |

## ConnectionControlParam

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/ConnectionControlParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `control` | `JTDeviceConnectionControl` | 控制选项 |

## QueryMediaDataParam：存储多媒体数据参数

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/QueryMediaDataParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `mediaId` | `Long` | 多媒体 ID, 单条存储多媒体数据检索上传时有效 |
| `delete` | `int` | 删除标志, 单条存储多媒体数据检索上传时有效 |
| `queryMediaDataCommand` | `JTQueryMediaDataCommand` | 存储多媒体数据参数 |

## SetAreaParam：设置区域参数

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/SetAreaParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `circleAreaList` | `List<JTCircleArea>` | 圆形区域项 |
| `rectangleAreas` | `List<JTRectangleArea>` | 矩形区域项 |
| `polygonArea` | `JTPolygonArea` | 多边形区域 |
| `route` | `JTRoute` | 路线 |

## SetConfigParam：终端参数设置

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/SetConfigParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `config` | `JTDeviceConfig` | 终端参数设置 |

## SetPhoneBookParam：设置电话本

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/SetPhoneBookParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `type` | `int` | 设置类型:\n0: 删除终端上所有存储的联系人,\n1: 表示更新电话本, 删除终端中已有全部联系人并追加消息中的联系人,\n2: 表示追加电话本,\n3: 表示修改电话本$以联系人为索引 |
| `phoneBookContactList` | `List<JTPhoneBookContact>` | 联系人 |

## ShootingParam：摄像头立即拍摄命令参数

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/ShootingParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `shootingCommand` | `JTShootingCommand` | 拍摄命令参数 |

## TextMessageParam：人工确认报警消息参数

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/controller/bean/TextMessageParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `phoneNumber` | `String` | 终端手机号 |
| `sign` | `JTTextSign` | 标志 |
| `textType` | `int` | 文本类型,1 = 通知 ，2 = 服务 |
| `content` | `String` | 消息内容，最长为1024字节 |

## J1205

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/proc/request/J1205.java`；继承 `Re`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `recordList` | `List<JRecordItem>` | 录像记录列表 |

## ABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/ABLHookParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `mediaServerId` | `String` | 流媒体节点 ID |
| `app` | `String` | 应用名 |
| `stream` | `String` | 流id |
| `key` | `String` | 媒体流来源编号，可以根据这个key进行关闭流媒体 可以调用delMediaStream或close_streams 函数进行关闭 |
| `networkType` | `Integer` | 媒体流来源网络编号，可参考附表 |

## OnPlayABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnPlayABLHookParam.java`；继承 `ABLHookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `ip` | `String` | IP 地址 |
| `port` | `Integer` | 网络端口 |
| `params` | `String` | 附加参数 |

## OnPublishABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnPublishABLHookParam.java`；继承 `ABLHookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `ip` | `String` | IP 地址 |
| `port` | `Integer` | 网络端口 |
| `params` | `String` | 附加参数 |

## OnRecordMp4ABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnRecordMp4ABLHookParam.java`；继承 `ABLHookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `fileName` | `String` | 文件名称 |
| `startTime` | `String` | 开始时间 |
| `endTime` | `String` | 结束时间 |
| `fileSize` | `long` | 文件大小 |

## OnRecordProgressABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnRecordProgressABLHookParam.java`；继承 `OnRecordMp4ABLHookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `currentFileDuration` | `Integer` | 当前录像文件时长 |
| `TotalVideoDuration` | `Integer` | 累计录像时长 |
| `startTime` | `String` | 开始时间 |
| `endTime` | `String` | 结束时间 |

## OnServerKeepaliveABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnServerKeepaliveABLHookParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `localipAddress` | `String` | 本地 IP 地址 |
| `mediaServerId` | `String` | 流媒体节点 ID |
| `datetime` | `String` | 日期时间 |

## OnServerStaredABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnServerStaredABLHookParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `localipAddress` | `String` | 本地 IP 地址 |
| `mediaServerId` | `String` | 流媒体节点 ID |
| `datetime` | `String` | 日期时间 |

## OnStreamArriveABLHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/abl/bean/hook/OnStreamArriveABLHookParam.java`；继承 `ABLHookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `callId` | `String` | 推流鉴权Id |
| `status` | `Boolean` | 状态 |
| `enableHls` | `Boolean` | 是否启用 HLS |
| `transcodingStatus` | `Boolean` | 转码状态 |
| `sourceURL` | `String` | 源流 URL |
| `readerCount` | `Integer` | 观看人数 |
| `noneReaderDuration` | `Integer` | 无观看者持续时间 |
| `videoCodec` | `String` | 视频编码格式 |
| `videoFrameSpeed` | `Integer` | 视频帧率 |
| `width` | `Integer` | 画面宽度 |
| `height` | `Integer` | 画面高度 |
| `videoBitrate` | `Integer` | 视频码率 |
| `audioCodec` | `String` | 音频编码格式 |
| `audioChannels` | `Integer` | 音频声道数 |
| `audioSampleRate` | `Integer` | 音频采样率 |
| `audioBitrate` | `Integer` | 音频码率 |
| `url` | `ABLUrls` | 资源 URL |

## MediaInfo：视频信息

源码：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `mediaServer` | `MediaServer` | 流媒体节点 |
| `schema` | `String` | 协议 |
| `readerCount` | `Integer` | 观看人数 |
| `videoCodec` | `String` | 视频编码类型 |
| `width` | `Integer` | 视频宽度 |
| `height` | `Integer` | 视频高度 |
| `fps` | `Integer` | FPS |
| `loss` | `Integer` | 丢包率 |
| `audioCodec` | `String` | 音频编码类型 |
| `audioChannels` | `Integer` | 音频通道数 |
| `audioSampleRate` | `Integer` | 音频采样率 |
| `duration` | `Long` | 时长 |
| `online` | `Boolean` | 在线 |
| `originType` | `Integer` | unknown = 0,rtmp_push=1,rtsp_push=2,rtp_push=3,pull=4,ffmpeg_pull=5,mp4_vod=6,device_chn=7,rtc_push=8 |
| `originTypeStr` | `String` | originType的文本描述 |
| `originUrl` | `String` | 产生流的源流地址 |
| `aliveSecond` | `Long` | 存活时间，单位秒 |
| `bytesSpeed` | `Long` | 数据产生速度，单位byte/s |
| `callId` | `String` | 鉴权参数 |
| `paramMap` | `Map<String, String>` | 额外参数 |
| `serverId` | `String` | 服务ID |

## MediaServer：流媒体服务信息

源码：`src/main/java/com/genersoft/iot/vmp/media/bean/MediaServer.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `String` | ID |
| `ip` | `String` | IP |
| `hookIp` | `String` | hook使用的IP（zlm访问WVP使用的IP） |
| `sdpIp` | `String` | SDP IP |
| `streamIp` | `String` | 流IP |
| `httpPort` | `int` | HTTP端口 |
| `httpSSlPort` | `int` | HTTPS端口 |
| `rtmpPort` | `int` | RTMP端口 |
| `flvPort` | `int` | flv端口 |
| `flvSSLPort` | `int` | https-flv端口 |
| `mp4Port` | `int` | mp4端口 |
| `wsFlvPort` | `int` | ws-flv端口 |
| `wsFlvSSLPort` | `int` | wss-flv端口 |
| `rtmpSSlPort` | `int` | RTMPS端口 |
| `rtpProxyPort` | `int` | RTP收流端口（单端口模式有用） |
| `jttProxyPort` | `int` | 1078收流端口（单端口模式有用） |
| `rtspPort` | `int` | RTSP端口 |
| `rtspSSLPort` | `int` | RTSPS端口 |
| `autoConfig` | `boolean` | 是否开启自动配置ZLM |
| `secret` | `String` | ZLM鉴权参数 |
| `hookAliveInterval` | `Float` | keepalive hook触发间隔,单位秒 |
| `rtpEnable` | `boolean` | 是否使用多端口模式 |
| `status` | `boolean` | 状态 |
| `rtpPortRange` | `String` | 多端口RTP收流端口范围 |
| `sendRtpPortRange` | `String` | RTP发流端口范围 |
| `recordAssistPort` | `int` | assist服务端口 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `lastKeepaliveTime` | `String` | 上次心跳时间 |
| `defaultServer` | `boolean` | 是否是默认ZLM |
| `recordDay` | `int` | 录像存储时长 |
| `recordPath` | `String` | 录像存储路径 |
| `type` | `String` | 类型： zlm/abl |
| `transcodeSuffix` | `String` | 转码的前缀 |
| `serverId` | `String` | 服务Id |

## RecordInfo

源码：`src/main/java/com/genersoft/iot/vmp/media/bean/RecordInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `fileName` | `String` | 文件名称 |
| `filePath` | `String` | 文件路径 |
| `fileSize` | `long` | 文件大小 |
| `folder` | `String` | 文件夹路径 |
| `url` | `String` | 资源 URL |
| `startTime` | `long` | 单位毫秒 |
| `timeLen` | `double` | 单位毫秒 |
| `params` | `String` | 附加参数 |

## Hook

源码：`src/main/java/com/genersoft/iot/vmp/media/event/hook/Hook.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `hookType` | `HookType` | 媒体 Hook 事件类型 |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `expireTime` | `Long` | 到期时间 |

## HookData

源码：`src/main/java/com/genersoft/iot/vmp/media/event/hook/HookData.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `mediaServer` | `MediaServer` | 流媒体节点 |
| `schema` | `String` | 协议 |
| `mediaInfo` | `MediaInfo` | 流信息 |
| `recordInfo` | `RecordInfo` | 录像信息 |
| `params` | `String` | 推流的额外参数 |

## HookResult

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/HookResult.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `code` | `int` | 结果或业务状态码 |
| `msg` | `String` | 结果或错误信息 |

## HookResultForOnPublish

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/HookResultForOnPublish.java`；继承 `HookResult`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `enable_audio` | `Boolean` | 是否启用音频 |
| `enable_mp4` | `Boolean` | 是否启用 MP4 录制 |
| `mp4_max_second` | `Integer` | 单段 MP4 最大时长，秒 |
| `mp4_save_path` | `String` | MP4 保存路径 |
| `stream_replace` | `String` | 替换后的流标识 |
| `modify_stamp` | `Integer` | 是否修改时间戳 |

## OnPlayHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnPlayHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `String` | 数据库记录 ID |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `ip` | `String` | IP 地址 |
| `params` | `String` | 附加参数 |
| `port` | `int` | 网络端口 |
| `schema` | `String` | 媒体流协议类型 |
| `vhost` | `String` | 媒体服务虚拟主机 |

## OnPublishHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnPublishHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `String` | 数据库记录 ID |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `ip` | `String` | IP 地址 |
| `params` | `String` | 附加参数 |
| `port` | `int` | 网络端口 |
| `schema` | `String` | 媒体流协议类型 |
| `vhost` | `String` | 媒体服务虚拟主机 |

## OnRecordMp4HookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnRecordMp4HookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `file_name` | `String` | 文件名称 |
| `file_path` | `String` | 文件路径 |
| `file_size` | `long` | 文件大小 |
| `folder` | `String` | 文件夹路径 |
| `url` | `String` | 资源 URL |
| `vhost` | `String` | 媒体服务虚拟主机 |
| `start_time` | `long` | 开始时间 |
| `time_len` | `double` | 时长 |
| `params` | `String` | 附加参数 |

## OnRtpServerTimeoutHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnRtpServerTimeoutHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `local_port` | `int` | 本地端口 |
| `stream_id` | `String` | 流 ID |
| `tcpMode` | `int` | TCP 收流模式 |
| `re_use_port` | `boolean` | 是否复用端口 |
| `ssrc` | `String` | RTP SSRC |

## OnSendRtpStoppedHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnSendRtpStoppedHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |

## OnServerKeepaliveHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnServerKeepaliveHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `data` | `ServerKeepaliveData` | 事件数据对象 |

## OnStreamChangedHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnStreamChangedHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `regist` | `boolean` | 注册/注销 |
| `app` | `String` | 应用名 |
| `stream` | `String` | 流id |
| `callId` | `String` | 推流鉴权Id |
| `totalReaderCount` | `int` | 观看总人数，包括hls/rtsp/rtmp/http-flv/ws-flv |
| `schema` | `String` | 协议 包括hls/rtsp/rtmp/http-flv/ws-flv |
| `originType` | `int` | 产生源类型， unknown = 0, rtmp_push=1, rtsp_push=2, rtp_push=3, pull=4, ffmpeg_pull=5, mp4_vod=6, device_chn=7 |
| `originSock` | `OriginSock` | 客户端和服务器网络信息，可能为null类型 |
| `originTypeStr` | `String` | 产生源类型的字符串描述 |
| `originUrl` | `String` | 产生源的url |
| `severId` | `String` | 服务器id |
| `createStamp` | `Long` | GMT unix系统时间戳，单位秒 |
| `aliveSecond` | `Long` | 存活时间，单位秒 |
| `bytesSpeed` | `Long` | 数据产生速度，单位byte/s |
| `tracks` | `List<MediaTrack>` | 音视频轨道 |
| `vhost` | `String` | 音视频轨道 |
| `params` | `String` | 额外的参数字符串 |
| `paramMap` | `Map<String, String>` | 额外的参数 |
| `docker` | `boolean` | 是否是docker部署， docker部署不会自动更新zlm使用的端口，需要自己手动修改 |
| `streamInfo` | `StreamContent` | 流信息对象 |

## OnStreamNoneReaderHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnStreamNoneReaderHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `schema` | `String` | 媒体流协议类型 |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `vhost` | `String` | 媒体服务虚拟主机 |

## OnStreamNotFoundHookParam

源码：`src/main/java/com/genersoft/iot/vmp/media/zlm/dto/hook/OnStreamNotFoundHookParam.java`；继承 `HookParam`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `String` | 数据库记录 ID |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `ip` | `String` | IP 地址 |
| `params` | `String` | 附加参数 |
| `port` | `int` | 网络端口 |
| `schema` | `String` | 媒体流协议类型 |
| `vhost` | `String` | 媒体服务虚拟主机 |

## Alarm：报警信息

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/Alarm.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `Long` | 数据库id |
| `channelId` | `int` | 关联通道的数据库id |
| `channelDeviceId` | `String` | 关联通道国标编号 |
| `channelName` | `String` | 关联通道国标名称 |
| `description` | `String` | 报警描述 |
| `snapPath` | `String` | 报警快照路径 |
| `recordPath` | `String` | 报警录像路径 |
| `longitude` | `Double` | 报警附带的经度 |
| `latitude` | `Double` | 报警附带的纬度 |
| `alarmType` | `AlarmType` | 报警类别 |
| `alarmTime` | `Long` | 报警时间 |

## CloudRecordItem：云端录像记录

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/CloudRecordItem.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 录像记录数据库 ID |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `callId` | `String` | 该次录像关联的会话 Call-ID |
| `startTime` | `long` | 媒体节点上报的录像开始时间戳 |
| `endTime` | `long` | 录像结束时间戳；由开始时间与录像时长计算 |
| `mediaServerId` | `String` | 流媒体节点 ID |
| `fileName` | `String` | 录像文件名 |
| `filePath` | `String` | 录像文件路径 |
| `folder` | `String` | 录像所在文件夹 |
| `collect` | `Boolean` | 是否收藏 |
| `reserve` | `Boolean` | 是否保留 |
| `fileSize` | `long` | 录像文件大小，字节 |
| `timeLen` | `double` | 媒体节点上报的录像时长；单位需按所用媒体节点核对 |
| `serverId` | `String` | 所属 WVP 服务节点 ID |

## DownloadFileInfo：录像文件访问地址

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/DownloadFileInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `httpPath` | `String` | HTTP 文件地址 |
| `httpsPath` | `String` | HTTPS 文件地址 |
| `httpDomainPath` | `String` | 带域名的 HTTP 文件地址 |
| `httpsDomainPath` | `String` | 带域名的 HTTPS 文件地址 |

## LogFileInfo

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/LogFileInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `fileName` | `String` | 文件名称 |
| `fileSize` | `Long` | 文件大小 |
| `startTime` | `Long` | 开始时间 |
| `endTime` | `Long` | 结束时间 |

## MediaServerLoad

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/MediaServerLoad.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `String` | 数据库记录 ID |
| `push` | `int` | 推流负载 |
| `proxy` | `int` | 拉流代理负载 |
| `gbReceive` | `int` | 国标收流负载 |
| `gbSend` | `int` | 国标发流负载 |

## RecordPlan：录制计划

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/RecordPlan.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 计划数据库ID |
| `name` | `String` | 计划名称 |
| `channelCount` | `int` | 计划关联通道数量 |
| `snap` | `Boolean` | 是否开启定时截图 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `planItemList` | `List<RecordPlanItem>` | 计划内容 |

## RecordPlanItem：录制计划项

源码：`src/main/java/com/genersoft/iot/vmp/service/bean/RecordPlanItem.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 计划项数据库ID |
| `start` | `Integer` | 计划开始时间的序号， 从0点开始，每半个小时增加1 |
| `stop` | `Integer` | 计划结束时间的序号， 从0点开始，每半个小时增加1 |
| `weekDay` | `Integer` | 计划周几执行 |
| `planId` | `Integer` | 所属计划ID |

## RecordInfo

源码：`src/main/java/com/genersoft/iot/vmp/storager/dao/dto/RecordInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | ID |
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `mediaServerId` | `String` | 对应的zlm流媒体的ID |
| `createTime` | `String` | 创建时间 |
| `type` | `int` | 类型 对应zlm的 originType unknown = 0, rtmp_push=1, rtsp_push=2, rtp_push=3, pull=4, ffmpeg_pull=5, mp4_vod=6, device_chn=7, rtc_push=8 |
| `deviceId` | `String` | 国标录像时的设备ID |
| `channelId` | `String` | 国标录像时的通道ID |
| `name` | `String` | 拉流代理录像时的名称 |

## Role

源码：`src/main/java/com/genersoft/iot/vmp/storager/dao/dto/Role.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库记录 ID |
| `name` | `String` | 名称 |
| `authority` | `String` | 角色权限集合 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |

## User

源码：`src/main/java/com/genersoft/iot/vmp/storager/dao/dto/User.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库记录 ID |
| `username` | `String` | 用户名 |
| `password` | `String` | 密码等敏感字段；不得写入日志或对外示例 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `pushKey` | `String` | 推流鉴权键 |
| `role` | `Role` | 用户角色对象 |
| `defaultPassword` | `boolean` | 是否使用默认密码（首次登录需强制修改密码） |

## UserApiKey：用户ApiKey信息

源码：`src/main/java/com/genersoft/iot/vmp/storager/dao/dto/UserApiKey.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | Id |
| `userId` | `int` | 用户Id |
| `app` | `String` | 应用名 |
| `apiKey` | `String` | ApiKey |
| `expiredAt` | `long` | 过期时间（null=永不过期） |
| `remark` | `String` | 备注信息 |
| `enable` | `boolean` | 是否启用 |
| `createTime` | `String` | 创建时间 |
| `updateTime` | `String` | 更新时间 |
| `username` | `String` | 用户名 |

## StreamProxy：拉流代理的信息

源码：`src/main/java/com/genersoft/iot/vmp/streamProxy/bean/StreamProxy.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `int` | 数据库自增ID |
| `type` | `String` | 类型，取值，default： 流媒体直接拉流（默认），ffmpeg： ffmpeg实现拉流 |
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `mediaServerId` | `String` | 当前拉流使用的流媒体服务ID |
| `relatesMediaServerId` | `String` | 固定选择的流媒体服务ID |
| `serverId` | `String` | 服务ID |
| `srcUrl` | `String` | 拉流地址 |
| `timeout` | `int` | 超时时间:秒 |
| `ffmpegCmdKey` | `String` | ffmpeg模板KEY |
| `rtspType` | `String` | rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播 |
| `enable` | `boolean` | 是否启用 |
| `enableAudio` | `boolean` | 是否启用音频 |
| `enableMp4` | `boolean` | 是否启用MP4 |
| `enableDisableNoneReader` | `boolean` | 是否 无人观看时自动停用 |
| `streamKey` | `String` | 拉流代理时zlm返回的key，用于停止拉流代理 |
| `pulling` | `Boolean` | 拉流状态 |

## StreamProxyParam：拉流代理的信息

源码：`src/main/java/com/genersoft/iot/vmp/streamProxy/bean/StreamProxyParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `type` | `String` | 类型，取值，default： 流媒体直接拉流（默认），ffmpeg： ffmpeg实现拉流 |
| `app` | `String` | 应用名 |
| `name` | `String` | 名称 |
| `stream` | `String` | 流ID |
| `mediaServerId` | `String` | 流媒体服务ID |
| `url` | `String` | 拉流地址 |
| `timeoutMs` | `int` | 超时时间:秒 |
| `ffmpegCmdKey` | `String` | ffmpeg模板KEY |
| `rtpType` | `String` | rtsp拉流时，拉流方式，0：tcp，1：udp，2：组播 |
| `enable` | `boolean` | 是否启用 |
| `enableAudio` | `boolean` | 是否启用音频 |
| `enableMp4` | `boolean` | 是否启用MP4 |
| `enableDisableNoneReader` | `boolean` | 是否 无人观看时自动停用 |

## BatchRemoveParam

源码：`src/main/java/com/genersoft/iot/vmp/streamPush/bean/BatchRemoveParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `ids` | `Set<Integer>` | 记录 ID 列表 |

## StreamPush：推流信息

源码：`src/main/java/com/genersoft/iot/vmp/streamPush/bean/StreamPush.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `id` | `Integer` | id |
| `app` | `String` | 应用名 |
| `stream` | `String` | 流id |
| `mediaServerId` | `String` | 使用的流媒体ID |
| `serverId` | `String` | 使用的服务ID |
| `pushTime` | `String` | 推流时间 |
| `updateTime` | `String` | 更新时间 |
| `createTime` | `String` | 创建时间 |
| `pushing` | `boolean` | 是否正在推流 |
| `startOfflinePush` | `boolean` | 拉起离线推流 |
| `gpsSpeed` | `Double` | GPS的速度 |
| `gpsDirection` | `Double` | GPS的方向 |
| `gpsAltitude` | `Double` | GPS的海拔高度 |
| `gpsTime` | `String` | GPS的更新时间 |
| `uniqueKey` | `String` | 记录唯一键 |
| `dataType` | `Integer` | 关联的数据来源类型 |

## StreamPushExcelDto

源码：`src/main/java/com/genersoft/iot/vmp/streamPush/bean/StreamPushExcelDto.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 名称 |
| `app` | `String` | 流应用名 |
| `stream` | `String` | 流 ID |
| `gbDeviceId` | `String` | 国标通道编号 |
| `status` | `boolean` | 状态 |
| `longitude` | `Double` | 经度 WGS-84坐标系 |
| `latitude` | `Double` | 纬度 WGS-84坐标系 |

## AudioBroadcastResult

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/AudioBroadcastResult.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `streamInfo` | `StreamContent` | 推流的各个方式流地址 |
| `codec` | `String` | 编码格式 |
| `app` | `String` | 向zlm推流的应用名 |
| `stream` | `String` | 向zlm推流的流ID |
| `playStreamInfo` | `StreamContent` | 播放流地址（设备音频通过ZLM播放给浏览器），对讲时设置 |

## AudioTalkResult：对讲信息

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/AudioTalkResult.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `pushStream` | `StreamContent` | 推流地址（浏览器 WebRTC推流到ZLM） |
| `playStream` | `StreamContent` | 播放地址（设备音频通过ZLM播放给浏览器），喊话时为null |

## BatchGBStreamParam：多个推流信息

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/BatchGBStreamParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `gbStreams` | `List<GbStream>` | 推流信息列表 |

## MapConfig

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/MapConfig.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 名称 |
| `coordinateSystem` | `String` | 地图坐标系 |
| `tilesUrl` | `String` | 地图瓦片 URL |
| `tileSize` | `Integer` | 地图瓦片尺寸 |
| `zoom` | `Integer` | 地图缩放级别 |
| `center` | `Double[]` | 地图中心坐标 |
| `maxZoom` | `Integer` | 地图最大缩放级别 |
| `minZoom` | `Integer` | 地图最小缩放级别 |

## MapModelIcon

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/MapModelIcon.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 名称 |
| `alias` | `String` | 别名 |
| `path` | `String` | 路径 |

## OtherPsSendInfo

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/OtherPsSendInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `sendLocalIp` | `String` | 发流IP |
| `sendLocalPort` | `int` | 发流端口 |
| `receiveIp` | `String` | 收流IP |
| `receivePort` | `int` | 收流端口 |
| `callId` | `String` | 会话ID |
| `stream` | `String` | 流ID |
| `pushApp` | `String` | 推流应用名 |
| `pushStream` | `String` | 推流流ID |
| `pushSSRC` | `String` | 推流SSRC |

## OtherRtpSendInfo

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/OtherRtpSendInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `sendLocalIp` | `String` | 发流IP |
| `sendLocalPortForAudio` | `int` | 音频发流端口 |
| `sendLocalPortForVideo` | `int` | 视频发流端口 |
| `receiveIp` | `String` | 收流IP |
| `receivePortForAudio` | `int` | 音频收流端口 |
| `receivePortForVideo` | `int` | 视频收流端口 |
| `callId` | `String` | 会话ID |
| `stream` | `String` | 流ID |
| `pushApp` | `String` | 推流应用名 |
| `pushStream` | `String` | 推流流ID |
| `pushSSRC` | `String` | 推流SSRC |

## ResourceInfo

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/ResourceInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `device` | `ResourceBaseInfo` | 设备信息对象 |
| `channel` | `ResourceBaseInfo` | 通道信息对象 |
| `push` | `ResourceBaseInfo` | 推流负载 |
| `proxy` | `ResourceBaseInfo` | 拉流代理负载 |

## SnapPath：截图地址信息

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/SnapPath.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `path` | `String` | 相对地址 |
| `absoluteFilePath` | `String` | 绝对地址 |
| `url` | `String` | 请求地址 |

## StreamContent：流信息

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/StreamContent.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `app` | `String` | 应用名 |
| `stream` | `String` | 流ID |
| `ip` | `String` | IP |
| `flv` | `String` | HTTP-FLV流地址 |
| `https_flv` | `String` | HTTPS-FLV流地址 |
| `ws_flv` | `String` | Websocket-FLV流地址 |
| `wss_flv` | `String` | Websockets-FLV流地址 |
| `fmp4` | `String` | HTTP-FMP4流地址 |
| `https_fmp4` | `String` | HTTPS-FMP4流地址 |
| `ws_fmp4` | `String` | Websocket-FMP4流地址 |
| `wss_fmp4` | `String` | Websockets-FMP4流地址 |
| `hls` | `String` | HLS流地址 |
| `https_hls` | `String` | HTTPS-HLS流地址 |
| `ws_hls` | `String` | Websocket-HLS流地址 |
| `wss_hls` | `String` | Websockets-HLS流地址 |
| `ts` | `String` | HTTP-TS流地址 |
| `https_ts` | `String` | HTTPS-TS流地址 |
| `ws_ts` | `String` | Websocket-TS流地址 |
| `wss_ts` | `String` | Websockets-TS流地址 |
| `rtmp` | `String` | RTMP流地址 |
| `rtmps` | `String` | RTMPS流地址 |
| `rtsp` | `String` | RTSP流地址 |
| `rtsps` | `String` | RTSPS流地址 |
| `rtc` | `String` | RTC流地址 |
| `rtcs` | `String` | RTCS流地址 |
| `mediaServerId` | `String` | 流媒体ID |
| `mediaInfo` | `MediaInfo` | 流编码信息 |
| `startTime` | `String` | 开始时间 |
| `endTime` | `String` | 结束时间 |
| `duration` | `Double` | 时长(回放时使用) |
| `downLoadFilePath` | `DownloadFileInfo` | 文件下载地址（录像下载使用） |
| `transcodeStream` | `StreamContent` | 转码后的视频流 |
| `progress` | `double` | 处理进度 |
| `key` | `String` | 拉流代理返回的KEY |
| `serverId` | `String` | 使用的WVP ID |

## SystemConfigInfo

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/SystemConfigInfo.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `serverPort` | `int` | 服务监听端口 |
| `sip` | `SipConfig` | SIP 配置信息 |
| `addOn` | `UserSetting` | 附加配置信息 |
| `version` | `VersionPo` | 系统版本信息 |
| `jt1078Config` | `JT1078Config` | JT1078 配置信息 |

## WVPResult：统一返回结果

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/bean/WVPResult.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `code` | `int` | 错误码，0为成功 |
| `msg` | `String` | 描述，错误时描述错误原因 |
| `data` | `T` | 数据 |

## CloudRecordUrl：云端录像及其播放、下载地址

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/cloudRecord/bean/CloudRecordUrl.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `filePath` | `String` | 源文件路径；list-url 接口当前不填充此字段 |
| `playUrl` | `String` | 录像播放地址 |
| `downloadUrl` | `String` | 录像下载地址 |
| `fileName` | `String` | 源文件名；list-url 接口当前不填充此字段 |
| `id` | `int` | 录像记录数据库 ID；play/path 的 recordId 使用此值 |

## RecordPlanParam：录制计划-添加/编辑参数

源码：`src/main/java/com/genersoft/iot/vmp/vmanager/recordPlan/bean/RecordPlanParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `channelIds` | `List<Integer>` | 关联的通道ID |
| `deviceDbIds` | `List<Integer>` | 关联的设备ID，会为设备下的所有通道关联此录制计划，channelId存在是此项不生效， |
| `allLink` | `Boolean` | 全部关联/全部取消关联 |
| `planId` | `Integer` | 录制计划ID, ID为空是删除关联的计划 |

## CameraChannel：摄像头信息

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/CameraChannel.java`；继承 `CommonGBChannel`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceCode` | `String` | 摄像头设备国标编号 |
| `icon` | `String` | 图标路径 |
| `groupAlias` | `String` | 所属组织结构别名 |
| `topGroupGAlias` | `String` | 所属业务分组别名 |

## CameraCount

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/CameraCount.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `groupAlias` | `String` | 业务分组别名 |
| `deviceId` | `String` | 设备国标编号 |
| `allCount` | `Long` | 总数量 |
| `onlineCount` | `Long` | 在线数量 |

## CameraStreamContent

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/CameraStreamContent.java`；继承 `StreamContent`，还包含父类字段

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `name` | `String` | 名称 |
| `controlType` | `Integer` | 控制类型 |

## ChannelParam：通道信息

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/ChannelParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceCode` | `String` | 摄像头设备国标编号, 对于非国标摄像头可以不设置此参数 |
| `deviceId` | `String` | 通道编号 |

## IdsQueryParam：根据多个ID获取摄像头列表

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/IdsQueryParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `deviceIds` | `List<String>` | 通道编号列表 |
| `geoCoordSys` | `String` | 坐标系类型：WGS84,GCJ02、BD09 |

## Point：坐标

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/Point.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `lng` | `double` | 经度 |
| `lat` | `double` | 纬度 |

## PolygonQueryParam：多边形检索摄像头参数

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/PolygonQueryParam.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `position` | `List<Point>` | 多边形位置，格式： [{'lng':116.32, 'lat': 39: 39.2}, {'lng':115.32, 'lat': 39: 38.2}, {'lng':125.32, 'lat': 39: 38.2}] |
| `level` | `Integer` | 地图级别 |
| `groupAlias` | `String` | 分组别名 |
| `geoCoordSys` | `String` | 坐标系类型：WGS84,GCJ02、BD09 |

## SYMember

源码：`src/main/java/com/genersoft/iot/vmp/web/custom/bean/SYMember.java`

| 字段 | Java 类型 | 说明 |
|---|---|---|
| `no` | `String` | 成员编号 |
| `unicodeNo` | `Long` | 统一编码编号 |
| `blockId` | `Long` | 区块或区域 ID |
| `unitNo` | `String` | 单元编号 |
| `terminalMemberStatus` | `String` | 终端成员状态 |
| `channelDeviceId` | `String` | 通道国标编号 |

共收录 149 个模型。
