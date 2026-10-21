# DeviceChannel：通道信息

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/DeviceChannel.java`；继承 `CommonGBChannel`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `gbId` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 通用通道数据库自增ID；/api/common/channel 接口的 channelId 参数使用此值 | `1` |
| `gbDeviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-编码 | `"<gbDeviceId>"` |
| `gbName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-名称 | `"<gbName>"` |
| `gbManufacturer` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-设备厂商 | `"<gbManufacturer>"` |
| `gbModel` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-设备型号 | `"<gbModel>"` |
| `gbOwner` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-设备归属 | `"<gbOwner>"` |
| `gbCivilCode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-行政区域 | `"<gbCivilCode>"` |
| `gbBlock` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-警区 | `"<gbBlock>"` |
| `gbAddress` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-安装地址 | `"<gbAddress>"` |
| `gbParental` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-是否有子设备 | `1` |
| `gbParentId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-父节点ID | `"<gbParentId>"` |
| `gbSafetyWay` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-信令安全模式 | `1` |
| `gbRegisterWay` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-注册方式 | `1` |
| `gbCertNum` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-证书序列号 | `"<gbCertNum>"` |
| `gbCertifiable` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-证书有效标识 | `1` |
| `gbErrCode` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-无效原因码(有证书且证书无效的设备必选) | `1` |
| `gbEndTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-证书终止有效期(有证书且证书无效的设备必选) | `"<gbEndTime>"` |
| `gbSecrecy` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-保密属性(必选)缺省为0;0-不涉密,1-涉密 | `1` |
| `gbIpAddress` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-设备/系统IPv4/IPv6地址 | `"<gbIpAddress>"` |
| `gbPort` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-设备/系统端口 | `1` |
| `gbPassword` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-设备口令 | `"<gbPassword>"` |
| `gbStatus` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-设备状态 | `"<gbStatus>"` |
| `gbLongitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 国标-经度 WGS-84坐标系 | `1.0` |
| `gbLatitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 国标-纬度 WGS-84坐标系 | `1.0` |
| `gpsAltitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | GPS 海拔高度 | `1.0` |
| `gpsSpeed` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | GPS 速度 | `1.0` |
| `gpsDirection` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | GPS 方向角 | `1.0` |
| `gpsTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | GPS的更新时间 | `"<gpsTime>"` |
| `gbBusinessGroupId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-虚拟组织所属的业务分组ID | `"<gbBusinessGroupId>"` |
| `gbPtzType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-摄像机结构类型,标识摄像机类型: 1-球机; 2-半球; 3-固定枪机; 4-遥控枪机;5-遥控半球;6-多目设备的全景/拼接通道;7-多目设备的分割通道; 99-移动设备（非标）98-会议设备（非标） | `1` |
| `gbPositionType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | -摄像机位置类型扩展。1-省际检查站、2-党政机关、3-车站码头、4-中心广场、5-体育场馆、6-商业中心、7-宗教场所、8-校园周边、9-治安复杂区域、10-交通干线。当目录项为摄像机时可选。 | `1` |
| `gbRoomType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-摄像机安装位置室外、室内属性。1-室外、2-室内。 | `1` |
| `gbUseType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-用途属性 | `1` |
| `gbSupplyLightType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-摄像机补光属性。1-无补光;2-红外补光;3-白光补光;4-激光补光;9-其他 | `1` |
| `gbDirectionType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 国标-摄像机监视方位(光轴方向)属性。1-东(西向东)、2-西(东向西)、3-南(北向南)、4-北(南向北)、5-东南(西北到东南)、6-东北(西南到东北)、7-西南(东北到西南)、8-西北(东南到西北) | `1` |
| `gbResolution` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-摄像机支持的分辨率,可多值 | `"<gbResolution>"` |
| `gbDownloadSpeed` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 国标-下载倍速(可选),可多值 | `"<gbDownloadSpeed>"` |
| `gbSvcSpaceSupportMod` | integer | `Integer` | 由具体接口决定 | 整数；国标-空域编码能力,取值0-不支持 | 源码未声明默认值 | 国标-空域编码能力,取值0-不支持;1-1级增强(1个增强层);2-2级增强(2个增强层);3-3级增强(3个增强层) | `1` |
| `gbSvcTimeSupportMode` | integer | `Integer` | 由具体接口决定 | 整数；国标-时域编码能力,取值0-不支持 | 源码未声明默认值 | 国标-时域编码能力,取值0-不支持;1-1级增强;2-2级增强;3-3级增强(可选) | `1` |
| `recordPLan` | integer | `Long` | 由具体接口决定 | 整数 | 源码未声明默认值 | 二进制保存的录制计划, 每一位表示每个小时的前半个小时 | `1` |
| `dataType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 关联的数据来源类型 | `1` |
| `dataDeviceId` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 关联的设备ID | `1` |
| `createTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 创建时间 | `"<createTime>"` |
| `updateTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 更新时间 | `"<updateTime>"` |
| `streamId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 流唯一编号，存在表示正在直播 | `"<streamId>"` |
| `enableBroadcast` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 是否支持对讲 1支持,0不支持 | `1` |
| `mapLevel` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 抽稀后的图层层级 | `1` |
| `id` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 数据库自增ID | `1` |
| `parentDeviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 父设备编码 | `"<parentDeviceId>"` |
| `parentName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 父设备名称 | `"<parentName>"` |
| `deviceId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 编码 | `"<deviceId>"` |
| `name` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 名称 | `"<name>"` |
| `manufacturer` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备厂商 | `"<manufacturer>"` |
| `model` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备型号 | `"<model>"` |
| `owner` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备归属 | `"<owner>"` |
| `civilCode` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 行政区域 | `"<civilCode>"` |
| `block` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 警区 | `"<block>"` |
| `address` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 安装地址 | `"<address>"` |
| `parental` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 是否有子设备(必选)1有,0没有 | `1` |
| `parentId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 父节点ID | `"<parentId>"` |
| `safetyWay` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 信令安全模式 | `1` |
| `registerWay` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 注册方式 | `1` |
| `certNum` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 证书序列号 | `"<certNum>"` |
| `certifiable` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 证书有效标识, 缺省为0;证书有效标识:0:无效 1:有效 | `1` |
| `errCode` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 无效原因码(有证书且证书无效的设备必选) | `1` |
| `endTime` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 证书终止有效期(有证书且证书无效的设备必选) | `"2026-09-28 09:00:00"` |
| `secrecy` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 保密属性(必选)缺省为0;0-不涉密,1-涉密 | `1` |
| `ipAddress` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备/系统IPv4/IPv6地址 | `"<ipAddress>"` |
| `port` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 设备/系统端口 | `1` |
| `password` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备口令 | `"<32位MD5或该接口规定的密码>"` |
| `status` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 设备状态 | `"<status>"` |
| `longitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | 经度 WGS-84坐标系 | `1.0` |
| `latitude` | number | `Double` | 由具体接口决定 | 数值 | 源码未声明默认值 | ,纬度 WGS-84坐标系 | `1.0` |
| `ptzType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 摄像机结构类型,标识摄像机类型: 1-球机; 2-半球; 3-固定枪机; 4-遥控枪机;5-遥控半球;6-多目设备的全景/拼接通道;7-多目设备的分割通道 | `1` |
| `positionType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 摄像机位置类型扩展。1-省际检查站、2-党政机关、3-车站码头、4-中心广场、5-体育场馆、6-商业中心、7-宗教场所、8-校园周边、9-治安复杂区域、10-交通干线 | `1` |
| `roomType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 摄像机安装位置室外、室内属性。1-室外、2-室内。 | `1` |
| `useType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 用途属性， 1-治安、2-交通、3-重点。 | `1` |
| `supplyLightType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 摄像机补光属性。1-无补光;2-红外补光;3-白光补光;4-激光补光;9-其他 | `1` |
| `directionType` | integer | `Integer` | 由具体接口决定 | 整数 | 源码未声明默认值 | 摄像机监视方位(光轴方向)属性。1-东(西向东)、2-西(东向西)、3-南(北向南)、4-北(南向北)、5-东南(西北到东南)、6-东北(西南到东北)、7-西南(东北到西南)、8-西北(东南到西北) | `1` |
| `resolution` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 摄像机支持的分辨率,可多值 | `"<resolution>"` |
| `businessGroupId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 虚拟组织所属的业务分组ID | `"<businessGroupId>"` |
| `downloadSpeed` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 下载倍速(可选),可多值 | `"<downloadSpeed>"` |
| `svcSpaceSupportMod` | integer | `Integer` | 由具体接口决定 | 整数；空域编码能力,取值0-不支持 | 源码未声明默认值 | 空域编码能力,取值0-不支持;1-1级增强(1个增强层);2-2级增强(2个增强层);3-3级增强(3个增强层) | `1` |
| `svcTimeSupportMode` | integer | `Integer` | 由具体接口决定 | 整数；时域编码能力,取值0-不支持 | 源码未声明默认值 | 时域编码能力,取值0-不支持;1-1级增强;2-2级增强;3-3级增强(可选) | `1` |
| `ptzTypeText` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 云台类型描述字符串 | `"<ptzTypeText>"` |
| `subCount` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 子设备数 | `1` |
| `hasAudio` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否含有音频 | `true` |
| `streamIdentification` | string | `String` | 由具体接口决定 | 可选值: stream/streamnumber/streamprofile/streamMode | null | 码流标识，优先级高于设备中码流标识，用于选择码流时组成码流标识。默认为null，不设置。可选值: stream/streamnumber/streamprofile/streamMode | `"<streamIdentification>"` |
| `channelType` | integer | `int` | 由具体接口决定 | 整数 | 源码未声明默认值 | 资源类型，0：视频通道，1：行政区划，2：业务分组/虚拟组织，3：平台节点，4：普通目录 | `1` |
| `dbKey` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 数据库或缓存键 | `"<dbKey>"` |
| `resourceType` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 资源类型标识（派生字段） | `"<resourceType>"` |
| `resourceTypeName` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 资源类型名称（派生字段） | `"<resourceTypeName>"` |
| `playable` | boolean | `boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否为可点播的视频通道（派生字段） | `true` |
| `playDisabledReason` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | 不可点播原因（派生字段） | `"<playDisabledReason>"` |

## 关联模型

- [CommonGBChannel](CommonGBChannel-44b704531f25.md)：`src/main/java/com/genersoft/iot/vmp/gb28181/bean/CommonGBChannel.java`
