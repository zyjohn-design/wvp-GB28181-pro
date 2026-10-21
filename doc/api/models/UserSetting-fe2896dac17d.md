# UserSetting

[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)

源码：`src/main/java/com/genersoft/iot/vmp/conf/UserSetting.java`

模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。

| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |
|---|---|---|---|---|---|---|---|
| `savePositionHistory` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否保存位置的历史记录(轨迹) | `true` |
| `autoApplyPlay` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否开始自动点播: 请求流为未拉起的流时,自动开启点播, 需要rtp.enable=true | `true` |
| `seniorSdp` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | [可选] 部分设备需要扩展SDP，需要打开此设置,一般设备无需打开 | `true` |
| `playTimeout` | integer | `Integer` | 由具体接口决定 | 整数 | 10000（Java初始值；请求默认值依接口） | 点播/录像回放 等待超时时间,单位：毫秒；Java初始值：10000 | `1` |
| `recordInfoTimeout` | integer | `Integer` | 由具体接口决定 | 整数 | 15000（Java初始值；请求默认值依接口） | 获取设备录像数据超时时间,单位：毫秒；Java初始值：15000 | `1` |
| `platformPlayTimeout` | integer | `int` | 由具体接口决定 | 整数 | 20000（Java初始值；请求默认值依接口） | 上级点播等待超时时间,单位：毫秒；Java初始值：20000 | `1` |
| `interfaceAuthentication` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否开启接口鉴权 | `true` |
| `interfaceAuthenticationExcludes` | array | `List<String>` | 由具体接口决定 | JSON数组；元素类型见说明 | 源码未声明默认值 | 接口鉴权例外的接口, 即不进行接口鉴权的接口,尽量详细书写，尽量不用/**，至少两级目录 | `[]` |
| `recordPushLive` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 推流直播是否录制 | `true` |
| `recordSip` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 国标是否录制 | `true` |
| `usePushingAsStatus` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 使用推流状态作为推流通道状态 | `true` |
| `useSourceIpAsStreamIp` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 使用来源请求ip作为streamIp,当且仅当你只有zlm节点它与wvp在一起的情况下开启 | `true` |
| `sipUseSourceIpAsRemoteAddress` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否使用设备来源Ip作为回复IP， 不设置则为 false | `true` |
| `streamOnDemand` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 国标点播 按需拉流, true：有人观看拉流，无人观看释放， false：拉起后不自动释放 | `true` |
| `pushAuthority` | boolean | `Boolean` | 由具体接口决定 | true / false | 开启 | 推流鉴权， 默认开启 | `true` |
| `syncChannelOnDeviceOnline` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 设备上线时是否自动同步通道 | `true` |
| `sipLog` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否开启sip日志 | `true` |
| `sqlLog` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 是否开启mybatis-sql日志 | `true` |
| `sendToPlatformsWhenIdLost` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 消息通道功能-缺少国标ID是否给所有上级发送消息 | `true` |
| `refuseChannelStatusChannelFormNotify` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 保持通道状态，不接受notify通道状态变化， 兼容海康平台发送错误消息 | `true` |
| `deviceStatusNotify` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 设备/通道状态变化时发送消息 | `true` |
| `useCustomSsrcForParentInvite` | boolean | `Boolean` | 由具体接口决定 | true / false | 源码未声明默认值 | 上级平台点播时不使用上级平台指定的ssrc，使用自定义的ssrc，参考国标文档-点播外域设备媒体流SSRC处理方式 | `true` |
| `ssrcRandom` | boolean | `Boolean` | 由具体接口决定 | true / false；多端口模式使用随机SSRC，端口区分流，SSRC允许重复 | 源码未声明默认值 | 多端口模式使用随机SSRC，端口区分流，SSRC允许重复 | `true` |
| `docEnable` | boolean | `Boolean` | 由具体接口决定 | true / false | 开启 | 开启接口文档页面。 默认开启，生产环境建议关闭，遇到swagger相关的漏洞时也可以关闭 | `true` |
| `serverId` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | "000000"（Java初始值；请求默认值依接口） | 服务ID，不写则为000000；Java初始值："000000" | `"<serverId>"` |
| `broadcastForPlatform` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | "UDP"（Java初始值；请求默认值依接口） | 国标级联语音喊话发流模式 * UDP:udp传输 TCP-ACTIVE：tcp主动模式 TCP-PASSIVE：tcp被动模式；Java初始值："UDP" | `"<broadcastForPlatform>"` |
| `civilCodeFile` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | "classpath:civilCode.csv"（Java初始值；请求默认值依接口） | 行政区划信息文件,系统启动时会加载到系统里；Java初始值："classpath:civilCode.csv" | `"<civilCodeFile>"` |
| `allowedOrigins` | array | `List<String>` | 由具体接口决定 | JSON数组；元素类型见说明；跨域配置，不配置此项则允许所有跨域请求，配置后则只允许配置的页面的地址请求， 可以配置多个 | 源码未声明默认值 | 跨域配置，不配置此项则允许所有跨域请求，配置后则只允许配置的页面的地址请求， 可以配置多个 | `[]` |
| `maxNotifyCountQueue` | integer | `int` | 由具体接口决定 | 整数；设置notify缓存队列最大长度，超过此长度的数据将返回486 BUSY_HERE，消息丢弃, 默认100000 | 100000（Java初始值；请求默认值依接口） | 设置notify缓存队列最大长度，超过此长度的数据将返回486 BUSY_HERE，消息丢弃, 默认100000；Java初始值：100000 | `1` |
| `registerAgainAfterTime` | integer | `int` | 由具体接口决定 | 整数 | 60（Java初始值；请求默认值依接口） | 国标级联离线后多久重试一次注册；Java初始值：60 | `1` |
| `registerKeepIntDialog` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 国标续订方式，true为续订，每次注册在同一个会话里，false为重新注册，每次使用新的会话；Java初始值：false | `true` |
| `gbDeviceOnline` | integer | `int` | 由具体接口决定 | 整数 | 1（Java初始值；请求默认值依接口） | 国标设备离线后的上线策略， 0： 国标标准实现，设备离线后不回复心跳，直到设备重新注册上线， 1（默认）： 对于离线设备，收到心跳就把设备设置为上线，并更新注册时间为上次这次心跳的时间。防止过期时间判断异常；Java初始值：1 | `1` |
| `loginTimeout` | integer | `long` | 由具体接口决定 | 整数 | 60（Java初始值；请求默认值依接口） | 登录超时时间(分钟)，；Java初始值：60 | `1` |
| `jwkFile` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 源码未声明默认值 | jwk文件路径，若不指定则使用resources目录下的jwk.json | `"<jwkFile>"` |
| `autoRegisterPlatform` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | wvp集群模式下如果注册向上级的wvp奔溃，则自动选择一个其他wvp继续注册到上级；Java初始值：false | `true` |
| `sendPositionOnDemand` | boolean | `boolean` | 由具体接口决定 | true / false | true（Java初始值；请求默认值依接口） | 按需发送推流设备位置， 默认发送移动位置订阅时如果位置不变则不发送， 设置为false按照国标间隔持续发送；Java初始值：true | `true` |
| `sipCacheServerConnections` | boolean | `boolean` | 由具体接口决定 | true / false；这允许服务器防止客户端发起的基于 TCP 的拒绝服务攻击（即发起数百个客户端事务） | true（Java初始值；请求默认值依接口） | 部分设备会在短时间内发送大量注册， 导致协议栈内存溢出， 开启此项可以防止这部分设备注册， 避免服务崩溃，但是会降低系统性能， 描述如下 默认值为 true。 将此设置为 false 会使 Stack 在 Server Transaction 进入 TERMINATED 状态后关闭服务器套接字。 这允许服务器防止客户端发起的基于 TCP 的拒绝服务攻击（即发起数百个客户端事务）。 如果为 true（默认作），则堆栈将保持套接字打开，以便以牺牲线程和内存资源为代价来最大化性能 - 使自身容易受到 DOS 攻击。；Java初始值：true | `true` |
| `disableDateHeader` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 禁用date头，变相禁用了校时；Java初始值：false | `true` |
| `groupSyncDeviceTemplate` | string | `String` | 由具体接口决定 | 源码未声明格式或范围 | 参考当前的sip域信息生成 | 同步业务分组时自动生成分组国标编号的模板，不配置则默认参考当前的sip域信息生成 | `"<groupSyncDeviceTemplate>"` |
| `useAliasForGroupSync` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 与第三方进行分组同步时使用别名而不是分组ID， 如果没有设置此项为true，那么分组编号就是必须传递的。如果是设置为true则，自动为别名的分组生成新的编号；Java初始值：false | `true` |
| `deviceIdStrict` | boolean | `boolean` | 由具体接口决定 | true / false | true（Java初始值；请求默认值依接口） | 设备ID严格模式，开启后设备注册时如果设备ID不符合规范则拒绝注册, 默认开启；Java初始值：true | `true` |
| `subscribeMobilePosition` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 对于识别为设备的国标设备的，是否默认开启位置订阅；Java初始值：false | `true` |
| `alarmCatchSize` | integer | `long` | 由具体接口决定 | 整数 | 10000（Java初始值；请求默认值依接口） | 处理报警消息时，会缓存通道数据，如果超出则丢弃低热度消息，被丢弃的通道下次使用就需要重新查询数据库，默认10000， 建议根据实际情况调整，过大可能会占用较多内存，过小可能会增加数据库查询压力；Java初始值：10000 | `1` |
| `alarmSnapByStream` | boolean | `boolean` | 由具体接口决定 | true / false | false（Java初始值；请求默认值依接口） | 是否使用拉流的方式获取快照，默认false，避免流量大规模消耗，开启后则使用拉流的方式获取快照；Java初始值：false | `true` |
| `allowedAlarmType` | array | `List<AlarmType>` | 由具体接口决定 | JSON数组；元素类型见说明 | 不设置则不限制 | 报警订阅白名单，设置后只有在此列表中的上级平台才会接收报警订阅消息，默认不设置则不限制 | `[]` |
| `alarmKeepDays` | integer | `int` | 由具体接口决定 | 整数 | 7（Java初始值；请求默认值依接口） | 报警记录保留天数，超过此天数的报警记录将在每天凌晨自动清理，默认30天，设置为0则不自动清理；Java初始值：7 | `1` |
| `deviceRegisterTimeMaxCount` | integer | `int` | 由具体接口决定 | 整数 | 100（Java初始值；请求默认值依接口） | 设备注册时间列表最大保留条数，默认100；Java初始值：100 | `1` |
| `deviceRegisterTimeTtlHours` | integer | `int` | 由具体接口决定 | 整数 | 3（Java初始值；请求默认值依接口） | 设备注册时间列表过期时间（小时），默认3，设置为0则永不过期；Java初始值：3 | `1` |
| `deviceKeepaliveTimeMaxCount` | integer | `int` | 由具体接口决定 | 整数 | 100（Java初始值；请求默认值依接口） | 设备心跳时间列表最大保留条数，默认100；Java初始值：100 | `1` |
| `deviceKeepaliveTimeTtlHours` | integer | `int` | 由具体接口决定 | 整数 | 1（Java初始值；请求默认值依接口） | 设备心跳时间列表过期时间（小时），默认1，设置为0则永不过期；Java初始值：1 | `1` |

## 关联模型

- [AlarmType](AlarmType-1cbd9d59c620.md)：`src/main/java/com/genersoft/iot/vmp/service/bean/AlarmType.java`
