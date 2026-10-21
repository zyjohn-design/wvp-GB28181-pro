# 部标设备控制（需启用配置）（JT1078Controller）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| <a id="route-get-bf10dd256774"></a>`GET` | [/api/jt1078/live/start](../routes/get-bf10dd256774.md) | JT-开始点播 |
| <a id="route-get-36d937faa5ad"></a>`GET` | [/api/jt1078/live/stop](../routes/get-36d937faa5ad.md) | JT-结束点播 |
| <a id="route-get-96ba6f325dd5"></a>`GET` | [/api/jt1078/talk/start](../routes/get-96ba6f325dd5.md) | JT-语音对讲 |
| <a id="route-get-0703d6e49a78"></a>`GET` | [/api/jt1078/talk/stop](../routes/get-0703d6e49a78.md) | JT-结束对讲 |
| <a id="route-get-abdad40d92f2"></a>`GET` | [/api/jt1078/live/pause](../routes/get-abdad40d92f2.md) | JT-暂停点播 |
| <a id="route-get-2ff5e94adc52"></a>`GET` | [/api/jt1078/live/continue](../routes/get-2ff5e94adc52.md) | JT-继续点播 |
| <a id="route-get-423e0c1da500"></a>`GET` | [/api/jt1078/live/switch](../routes/get-423e0c1da500.md) | JT-切换码流类型 |
| <a id="route-get-f47792471317"></a>`GET` | [/api/jt1078/record/list](../routes/get-f47792471317.md) | JT-录像-查询资源列表 |
| <a id="route-get-021848628953"></a>`GET` | [/api/jt1078/playback/start](../routes/get-021848628953.md) | JT-录像-开始回放 |
| <a id="route-get-ebc5d828cab8"></a>`GET` | [/api/jt1078/playback/control](../routes/get-ebc5d828cab8.md) | JT-录像-回放控制 |
| <a id="route-get-da80706bcb10"></a>`GET` | [/api/jt1078/playback/stop](../routes/get-da80706bcb10.md) | JT-录像-结束回放 |
| <a id="route-get-594b58ac908e"></a>`GET` | [/api/jt1078/playback/downloadUrl](../routes/get-594b58ac908e.md) | JT-录像-获取下载地址 |
| <a id="route-get-15fcb30155dd"></a>`GET` | [/api/jt1078/playback/download](../routes/get-15fcb30155dd.md) | JT-录像-下载 |
| <a id="route-get-ed50827c0c6e"></a>`GET` | [/api/jt1078/ptz](../routes/get-ed50827c0c6e.md) | JT-云台控制 |
| <a id="route-get-ddcbd5c35274"></a>`GET` | [/api/jt1078/fill-light](../routes/get-ddcbd5c35274.md) | JT-补光灯开关 |
| <a id="route-get-c9a3b8f510c0"></a>`GET` | [/api/jt1078/wiper](../routes/get-c9a3b8f510c0.md) | JT-雨刷开关 |
| <a id="route-get-55ab822f37ac"></a>`GET` | [/api/jt1078/config/get](../routes/get-55ab822f37ac.md) | JT-查询终端参数 |
| <a id="route-post-93844cf42b58"></a>`POST` | [/api/jt1078/config/set](../routes/post-93844cf42b58.md) | JT-设置终端参数 |
| <a id="route-post-80dd835b71fe"></a>`POST` | [/api/jt1078/control/connection](../routes/post-80dd835b71fe.md) | 终端控制-连接指定的服务器 |
| <a id="route-post-29aa2fc800f7"></a>`POST` | [/api/jt1078/control/reset](../routes/post-29aa2fc800f7.md) | JT-终端控制-复位 |
| <a id="route-post-ae9ead759f72"></a>`POST` | [/api/jt1078/control/factory-reset](../routes/post-ae9ead759f72.md) | JT-终端控制-恢复出厂设置 |
| <a id="route-get-e8ea07998510"></a>`GET` | [/api/jt1078/attribute](../routes/get-e8ea07998510.md) | JT-查询终端属性 |
| <a id="route-get-50b393905b5f"></a>`GET` | [/api/jt1078/position-info](../routes/get-50b393905b5f.md) | JT-查询位置信息 |
| <a id="route-get-2ecab9d0baca"></a>`GET` | [/api/jt1078/control/temp-position-tracking](../routes/get-2ecab9d0baca.md) | JT-临时位置跟踪控制 |
| <a id="route-post-dba6b493eddc"></a>`POST` | [/api/jt1078/confirmation-alarm-message](../routes/post-dba6b493eddc.md) | JT-人工确认报警消息 |
| <a id="route-get-515699dfffd2"></a>`GET` | [/api/jt1078/link-detection](../routes/get-515699dfffd2.md) | JT-链路检测 |
| <a id="route-post-e98eded31aac"></a>`POST` | [/api/jt1078/text-msg](../routes/post-e98eded31aac.md) | JT-文本信息下发 |
| <a id="route-get-f19f1082518a"></a>`GET` | [/api/jt1078/telephone-callback](../routes/get-f19f1082518a.md) | JT-电话回拨 |
| <a id="route-post-50a5e01adc5d"></a>`POST` | [/api/jt1078/set-phone-book](../routes/post-50a5e01adc5d.md) | JT-设置电话本 |
| <a id="route-get-772b4c36338f"></a>`GET` | [/api/jt1078/control/door](../routes/get-772b4c36338f.md) | JT-车门控制 |
| <a id="route-post-d756309b58b2"></a>`POST` | [/api/jt1078/area/circle/update](../routes/post-d756309b58b2.md) | JT-更新圆形区域 |
| <a id="route-post-31bf70fd13a5"></a>`POST` | [/api/jt1078/area/circle/add](../routes/post-31bf70fd13a5.md) | JT-追加圆形区域 |
| <a id="route-post-3beb7a9bc31f"></a>`POST` | [/api/jt1078/area/circle/edit](../routes/post-3beb7a9bc31f.md) | JT-修改圆形区域 |
| <a id="route-get-15f557c2a431"></a>`GET` | [/api/jt1078/area/circle/delete](../routes/get-15f557c2a431.md) | JT-删除圆形区域 |
| <a id="route-get-ebe45837e38e"></a>`GET` | [/api/jt1078/area/circle/query](../routes/get-ebe45837e38e.md) | JT-查询圆形区域 |
| <a id="route-post-1e12b61e2a73"></a>`POST` | [/api/jt1078/area/rectangle/update](../routes/post-1e12b61e2a73.md) | JT-更新矩形区域 |
| <a id="route-post-fa72abb86b9a"></a>`POST` | [/api/jt1078/area/rectangle/add](../routes/post-fa72abb86b9a.md) | JT-追加矩形区域 |
| <a id="route-post-893eec6b8eb3"></a>`POST` | [/api/jt1078/area/rectangle/edit](../routes/post-893eec6b8eb3.md) | JT-修改矩形区域 |
| <a id="route-get-b13dcdffa68a"></a>`GET` | [/api/jt1078/area/rectangle/delete](../routes/get-b13dcdffa68a.md) | JT-删除矩形区域 |
| <a id="route-get-a4c7ae0cd733"></a>`GET` | [/api/jt1078/area/rectangle/query](../routes/get-a4c7ae0cd733.md) | JT-查询矩形区域 |
| <a id="route-post-2bf7782ec462"></a>`POST` | [/api/jt1078/area/polygon/set](../routes/post-2bf7782ec462.md) | JT-设置多边形区域 |
| <a id="route-get-7546a10acaf4"></a>`GET` | [/api/jt1078/area/polygon/delete](../routes/get-7546a10acaf4.md) | JT-删除多边形区域 |
| <a id="route-get-3a0c54378fab"></a>`GET` | [/api/jt1078/area/polygon/query](../routes/get-3a0c54378fab.md) | JT-查询多边形区域 |
| <a id="route-post-6c9dc26ddc29"></a>`POST` | [/api/jt1078/route/set](../routes/post-6c9dc26ddc29.md) | JT-设置路线 |
| <a id="route-get-e056efcffb18"></a>`GET` | [/api/jt1078/route/delete](../routes/get-e056efcffb18.md) | JT-删除路线 |
| <a id="route-get-4667991a5ec9"></a>`GET` | [/api/jt1078/route/query](../routes/get-4667991a5ec9.md) | JT-查询路线 |
| <a id="route-get-f988283206b1"></a>`GET` | [/api/jt1078/driver-information](../routes/get-f988283206b1.md) | JT-上报驾驶员身份信息请求 |
| <a id="route-post-d986960c0e46"></a>`POST` | [/api/jt1078/shooting](../routes/post-d986960c0e46.md) | JT-摄像头立即拍摄命令 |
| <a id="route-get-98cdff8de0a7"></a>`GET` | [/api/jt1078/snap](../routes/get-98cdff8de0a7.md) | JT-抓图 |
| <a id="route-post-0b755b50c958"></a>`POST` | [/api/jt1078/media/list](../routes/post-0b755b50c958.md) | JT-存储多媒体数据检索 |
| <a id="route-get-146b8a2eb6fb"></a>`GET` | [/api/jt1078/media/upload/one/upload](../routes/get-146b8a2eb6fb.md) | JT-单条存储多媒体数据上传 |
| <a id="route-get-162af454482e"></a>`GET` | [/api/jt1078/media/upload/one/delete](../routes/get-162af454482e.md) | JT-单条存储多媒体数据删除 |
| <a id="route-post-6e018d70f076"></a>`POST` | [/api/jt1078/media-data-upload](../routes/post-6e018d70f076.md) | JT-开始录音 |
| <a id="route-get-fca5d9139bbc"></a>`GET` | [/api/jt1078/record/start](../routes/get-fca5d9139bbc.md) | JT-开始录音 |
| <a id="route-get-d28e0bef3166"></a>`GET` | [/api/jt1078/record/stop](../routes/get-d28e0bef3166.md) | JT-停止录音 |
| <a id="route-get-b91c152bad72"></a>`GET` | [/api/jt1078/media/attribute](../routes/get-b91c152bad72.md) | JT-查询终端音视频属性 |
