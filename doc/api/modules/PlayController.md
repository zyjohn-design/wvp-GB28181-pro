# 国标设备点播（PlayController）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| `GET` | [/api/play/start/{deviceId}/{channelId}](../routes/get-496238af3f45.md) | 开始点播 |
| `GET` | [/api/play/stop/{deviceId}/{channelId}](../routes/get-2fce30013f1c.md) | 停止点播 |
| `POST` | [/api/play/convertStop/{key}](../routes/post-b8898e495654.md) | 结束转码 |
| `GET` | [/api/play/broadcast/{deviceId}/{channelId}](../routes/get-aff874a598d6.md) | 语音广播命令 |
| `POST` | [/api/play/broadcast/{deviceId}/{channelId}](../routes/post-fc4c886f1cf6.md) | 语音广播命令 |
| `GET` | [/api/play/broadcast/stop/{deviceId}/{channelId}](../routes/get-9878874b2869.md) | 停止语音广播 |
| `POST` | [/api/play/broadcast/stop/{deviceId}/{channelId}](../routes/post-2784a50220ec.md) | 停止语音广播 |
| `GET` | [/api/play/ssrc](../routes/get-9236fd7afd2b.md) | 获取所有的ssrc |
| `GET` | [/api/play/snap](../routes/get-47b8827727b6.md) | 获取截图 |
