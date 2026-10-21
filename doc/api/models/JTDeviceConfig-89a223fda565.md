# JTDeviceConfig：JT终端参数设置

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTDeviceConfig.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `keepaliveInterval` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 终端心跳发送间隔,单位为秒(s) | `1` |
| `tcpResponseTimeout` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | TCP消息应答超时时间,单位为秒(s) | `1` |
| `tcpRetransmissionCount` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | TCP消息重传次数 | `1` |
| `udpResponseTimeout` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | UDP消息应答超时时间,单位为秒(s) | `1` |
| `udpRetransmissionCount` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | UDP消息重传次数 | `1` |
| `smsResponseTimeout` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | SMS 消息应答超时时间,单位为秒(s) | `1` |
| `smsRetransmissionCount` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | SMS 消息重传次数 | `1` |
| `apnMaster` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 主服务器APN无线通信拨号访问点,若网络制式为 CDMA,则该处 为 PPP拨号号码 | `"<apnMaster>"` |
| `dialingUsernameMaster` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 主服务器无线通信拨号用户名 | `"<dialingUsernameMaster>"` |
| `dialingPasswordMaster` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 主服务器无线通信拨号密码 | `"<dialingPasswordMaster>"` |
| `addressMaster` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 主服务器地址IP或域名,以冒号分割主机和端口 多个服务器使用分号分割 | `"<addressMaster>"` |
| `apnBackup` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 备份服务器APN | `"<apnBackup>"` |
| `dialingUsernameBackup` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 备份服务器无线通信拨号用户名 | `"<dialingUsernameBackup>"` |
| `dialingPasswordBackup` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 备份服务器无线通信拨号密码 | `"<dialingPasswordBackup>"` |
| `addressBackup` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 备用服务器备份地址IP或域名,以冒号分割主机和端口 多个服务器使用分号分割 | `"<addressBackup>"` |
| `addressIcMaster` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 道路运输证IC卡认证主服务器IP地址或域名 | `"<addressIcMaster>"` |
| `tcpPortIcMaster` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 道路运输证IC卡认证主服务器TCP端口 | `1` |
| `udpPortIcMaster` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 道路运输证IC卡认证主服务器UDP端口 | `1` |
| `addressIcBackup` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 道路运输证IC卡认证备份服务器IP地址或域名,端口同主服务器 | `"<addressIcBackup>"` |
| `locationReportingStrategy` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 位置汇报策略, 0定时汇报 1定距汇报 2定时和定距汇报 | `1` |
| `locationReportingPlan` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 位置汇报方案,0根据ACC状态 1根据登录状态和ACC状态,先判断登录状态,若登录再根据ACC状态 | `1` |
| `reportingIntervalOffline` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 驾驶员未登录汇报时间间隔,单位为秒,值大于零 | `1` |
| `apnSlave` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 从服务器 APN# 该值为空时 !终端应使用主服务器相同配置 | `"<apnSlave>"` |
| `dialingUsernameSlave` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 从服务器无线通信拨号用户名 # 该值为空时 !终端应使用主服务器 相同配置 | `"<dialingUsernameSlave>"` |
| `dialingPasswordSlave` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 从服务器无线通信拨号密码 # 该值为空时 !终端应使用主服务器相 同配置 | `"<dialingPasswordSlave>"` |
| `addressSlave` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 从服务器备份地址 IP或域名 !主机和端口用冒号分割 !多个服务器 使用分号分割 | `"<addressSlave>"` |
| `reportingIntervalDormancy` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 休眠时汇报时间间隔 单位为秒 值大于0 | `1` |
| `reportingIntervalEmergencyAlarm` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 紧急报警时汇报时间间隔 单位为秒 值大于0 | `1` |
| `reportingIntervalDefault` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 缺省时间汇报间隔 单位为秒 值大于0 | `1` |
| `reportingDistanceDefault` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 缺省距离汇报间隔 单位为米 值大于0 | `1` |
| `reportingDistanceOffline` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 驾驶员未登录汇报距离间隔 单位为米 值大于0 | `1` |
| `reportingDistanceDormancy` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 休眠时汇报距离间隔 单位为米 值大于0 | `1` |
| `reportingDistanceEmergencyAlarm` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 紧急报警时汇报距离间隔 单位为米 值大于0 | `1` |
| `inflectionPointAngle` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 拐点补传角度 ,值小于180 | `1` |
| `fenceRadius` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 电子围栏半径(非法位移國值) ,单位为米(m) | `1` |
| `illegalDrivingPeriods` | object | `JTIllegalDrivingPeriods` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 违规行驶时段范围 ,精确到分 | `{}` |
| `platformPhoneNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 监控平台电话号码 | `"<platformPhoneNumber>"` |
| `phoneNumberForReset` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 复位电话号码 ,可采用此电话号码拨打终端电话让终端复位 | `"<phoneNumberForReset>"` |
| `phoneNumberForFactoryReset` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 恢复出厂设置电话号码 ,可采用此电话号码拨打终端电话让终端恢 复出厂设置 | `"<phoneNumberForFactoryReset>"` |
| `phoneNumberForSms` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 监控平台 SMS 电话号码 | `"<phoneNumberForSms>"` |
| `phoneNumberForReceiveTextAlarm` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 接收终端 SMS 文本报警号码 | `"<phoneNumberForReceiveTextAlarm>"` |
| `phoneAnsweringPolicy` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 终端电话接听策略 。0:自动接听；1:ACC ON时自动接听 ,OFF时手动接听 | `1` |
| `longestCallTimeForPerSession` | integer | `Long` | 由具体接口决定 | 整数；每次最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为不限制 | 源码未声明默认值 | 每次最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为不限制 | `1` |
| `longestCallTimeInMonth` | integer | `Long` | 由具体接口决定 | 整数；当月最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为 不限制 | 源码未声明默认值 | 当月最长通话时间 ,单位为秒(s) ,0 为不允许通话 ,0xFFFFFFFF为 不限制 | `1` |
| `phoneNumbersForListen` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 监听电话号码 | `"<phoneNumbersForListen>"` |
| `privilegedSMSNumber` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 监管平台特权短信号码 | `"<privilegedSMSNumber>"` |
| `alarmMaskingWord` | object | `JTAlarmSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 报警屏蔽字 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则相应报警被屏蔽 | `{}` |
| `alarmSendsTextSmsSwitch` | object | `JTAlarmSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 报警发送文本 SMS 开关 , 与位置信息汇报消息中的报警标志相对 应 ,相应位为1 则相应报警时发送文本 SMS | `{}` |
| `alarmShootingSwitch` | object | `JTAlarmSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 报警拍摄开关 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则相应报警时摄像头拍摄 | `{}` |
| `alarmShootingStorageFlags` | object | `JTAlarmSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 报警拍摄存储标志 ,与位置信息汇报消息中的报警标志相对应 ,相应 位为1 则对相应报警时拍的照片进行存储 ,否则实时上传 | `{}` |
| `KeySign` | object | `JTAlarmSign` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 关键标志 ,与位置信息汇报消息中的报警标志相对应 ,相应位为 1 则 对相应报警为关键报警 | `{}` |
| `maxSpeed` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 最高速度 ,单位为千米每小时(km/h) | `1` |
| `overSpeedDuration` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 超速持续时间 ,单位为秒(s) | `1` |
| `continuousDrivingTimeThreshold` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 连续驾驶时间门限 单位为秒(s) | `1` |
| `cumulativeDrivingTimeThresholdForTheDay` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 当天累计驾驶时间门限 单位为秒(s) | `1` |
| `minimumBreakTime` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 最小休息时间 单位为秒(s) | `1` |
| `maximumParkingTime` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 最长停车时间 单位为秒(s) | `1` |
| `overSpeedWarningDifference` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 超速预警差值 单位为1/10 千米每小时(1/10km/h) | `1` |
| `drowsyDrivingWarningDifference` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 疲劳驾驶预警差值 单位为秒 值大于零 | `1` |
| `collisionAlarmParams` | object | `JTCollisionAlarmParams` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 碰撞报警参数设置 | `{}` |
| `rolloverAlarm` | integer | `Integer` | 由具体接口决定 | 整数 | 30 | 侧翻报警参数设置:侧翻角度,单位为度,默认为30 | `1` |
| `cameraTimer` | object | `JTCameraTimer` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 定时拍照控制 | `{}` |
| `qualityForVideo` | integer | `Long` | 由具体接口决定 | 整数；1~10 | 源码未声明默认值 | 图像/视频质量 设置范围为1~10 1表示最优质量 | `1` |
| `brightness` | integer | `Long` | 由具体接口决定 | 整数；0 ~ 255 | 源码未声明默认值 | 亮度，设置范围为0 ~ 255 | `1` |
| `contrastRatio` | integer | `Long` | 由具体接口决定 | 整数；0 ~ 127 | 源码未声明默认值 | 对比度，设置范围为0 ~ 127 | `1` |
| `saturation` | integer | `Long` | 由具体接口决定 | 整数；0 ~ 127 | 源码未声明默认值 | 饱和度，设置范围为0 ~ 127 | `1` |
| `chroma` | integer | `Long` | 由具体接口决定 | 整数；0 ~ 255 | 源码未声明默认值 | 色度，设置范围为0 ~ 255 | `1` |
| `videoParam` | object | `JTVideoParam` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 音视频参数设置 | `{}` |
| `channelListParam` | object | `JTChannelListParam` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 音视频通道列表设置 | `{}` |
| `channelParam` | object | `JTChannelParam` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 单独视频通道参数设置 | `{}` |
| `alarmRecordingParam` | object | `JTAlarmRecordingParam` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 特殊报警录像参数设置 | `{}` |
| `videoAlarmBit` | object | `JTVideoAlarmBit` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 视频相关报警屏蔽字 | `{}` |
| `analyzeAlarmParam` | object | `JTAnalyzeAlarmParam` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 图像分析报警参数设置 | `{}` |
| `awakenParam` | object | `JTAwakenParam` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | 终端休眠唤醒模式设置 | `{}` |
| `mileage` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 车辆里程表读数，单位'1/10km | `1` |
| `provincialId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 车辆所在的省域ID | `1` |
| `cityId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 车辆所在的市域ID | `1` |
| `licensePlate` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 公安交通管理部门颁发的机动车号牌 | `"<licensePlate>"` |
| `licensePlateColor` | integer | `Short` | 由具体接口决定 | 整数 | 源码未声明默认值 | 车牌颜色,值按照JT/T697-7.2014中的规定,未上牌车辆填0 | `1` |
| `gnssPositioningMode` | object | `JTGnssPositioningMode` | 由具体接口决定 | JSON对象；字段见模型表 | 源码未声明默认值 | GNSS定位模式 | `{}` |
| `gnssBaudRate` | integer | `Short` | 由具体接口决定 | 整数 | 源码未声明默认值 | GNSS 波特率,定义如下: 0: 4800, 1:9600, 2：19200, 3:38400, 4:57600, 5:115200 | `1` |
| `gnssOutputFrequency` | integer | `Short` | 由具体接口决定 | 整数 | ) | GNSS 模块详细定位数据输出频率,定义如下: 0: 500ms, 1:1000ms(默认值), 2：2000ms, 3:3000ms, 4:4000ms | `1` |
| `gnssCollectionFrequency` | integer | `Long` | 由具体接口决定 | 整数 | 1 | GNSS 模块详细定位数据采集频率 ,单位为秒(s) ,默认为1 | `1` |
| `gnssDataUploadMethod` | integer | `Short` | 由具体接口决定 | 整数 | ) | GNSS 模块详细定位数据上传方式:,定义如下: 0: 本地存储 ,不上传(默认值) , 1:按时间间隔上传, 2：按距离间隔上传, 11:按累计时间上传 ,达到传输时间后自动停止上传, 12:按累计距离上传 ,达到距离后自动停止上传, 13:按累计条数上传 ,达到上传条数后自动停止上传 | `1` |
| `gnssDataUploadMethodUnit` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | GNSS 模块详细定位数据上传设置:,定义如下: 1:单位为秒(s), 2：单位为米(m) , 11:单位为 秒(s), 12:单位为米(m), 13:单位 为条 | `1` |
| `canCollectionTimeForChannel1` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | CAN总线通道1 采集时间间隔 ,单位为毫秒(ms) ,0 表示不采集 | `1` |
| `canUploadIntervalForChannel1` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | CAN总线通道1 上传时间间隔 ,单位为秒(s) ,0 表示不上传 | `1` |
| `canCollectionTimeForChannel2` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | CAN总线通道2 采集时间间隔 ,单位为毫秒(ms) ,0 表示不采集 | `1` |
| `canUploadIntervalForChannel2` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | CAN总线通道2 上传时间间隔 ,单位为秒(s) ,0 表示不上传 | `1` |

## 关联模型

- [JTAlarmRecordingParam](JTAlarmRecordingParam-ddb553433c4a.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTAlarmRecordingParam.java`
- [JTAlarmSign](JTAlarmSign-8b28a35c822e.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/JTAlarmSign.java`
- [JTAnalyzeAlarmParam](JTAnalyzeAlarmParam-8f4c509d76f2.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTAnalyzeAlarmParam.java`
- [JTAwakenParam](JTAwakenParam-e9f74625f017.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTAwakenParam.java`
- [JTCameraTimer](JTCameraTimer-15f55ec597cf.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTCameraTimer.java`
- [JTChannelListParam](JTChannelListParam-4eec22daea1d.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTChannelListParam.java`
- [JTChannelParam](JTChannelParam-86de8144b47a.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTChannelParam.java`
- [JTCollisionAlarmParams](JTCollisionAlarmParams-9c79469b998f.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTCollisionAlarmParams.java`
- [JTGnssPositioningMode](JTGnssPositioningMode-b4289170a9da.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTGnssPositioningMode.java`
- [JTIllegalDrivingPeriods](JTIllegalDrivingPeriods-3da6308307c1.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTIllegalDrivingPeriods.java`
- [JTVideoAlarmBit](JTVideoAlarmBit-046d7b305a03.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTVideoAlarmBit.java`
- [JTVideoParam](JTVideoParam-075301d7c821.md)：`src/main/java/com/genersoft/iot/vmp/jt1078/bean/config/JTVideoParam.java`
