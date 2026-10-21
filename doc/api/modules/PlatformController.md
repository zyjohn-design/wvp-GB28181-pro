# 级联平台管理（PlatformController）

[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)

| 方法 | 路径 | 功能 |
|---|---|---|
| `GET` | [/api/platform/server_config](../routes/get-be206559023d.md) | 获取国标服务的配置 |
| `GET` | [/api/platform/info/{id}](../routes/get-a7f7c43cea4e.md) | 获取级联服务器信息 |
| `GET` | [/api/platform/query](../routes/get-e125faea6802.md) | 分页查询级联平台 |
| `GET` | [/api/platform/register/test](../routes/get-692c2e374e54.md) | 测试注册, 立即向上级平台发送一次REGISTER并返回明确结果 |
| `GET` | [/api/platform/register/result](../routes/get-232b23fe8d64.md) | 查询最近一次注册结果 |
| `POST` | [/api/platform/add](../routes/post-0c5f01bf2a9d.md) | 添加上级平台信息 |
| `POST` | [/api/platform/update](../routes/post-cb25bf296b27.md) | 更新上级平台信息 |
| `DELETE` | [/api/platform/delete](../routes/delete-18646ef407f9.md) | 删除上级平台 |
| `GET` | [/api/platform/exit/{serverGBId}](../routes/get-43a3a2fa9972.md) | 查询上级平台是否存在 |
| `GET` | [/api/platform/channel/list](../routes/get-85c5d2a4e2ef.md) | 分页查询级联平台的所有所有通道 |
| `POST` | [/api/platform/channel/add](../routes/post-eba4560bbc93.md) | 向上级平台添加国标通道 |
| `DELETE` | [/api/platform/channel/remove](../routes/delete-14d59e4b3540.md) | 从上级平台移除国标通道 |
| `GET` | [/api/platform/channel/push](../routes/get-2caa59401cc1.md) | 推送通道 |
| `POST` | [/api/platform/channel/device/add](../routes/post-47a45b093569.md) | 添加通道-通过设备 |
| `POST` | [/api/platform/channel/device/remove](../routes/post-2e311e1067c8.md) | 移除通道-通过设备 |
| `POST` | [/api/platform/channel/custom/update](../routes/post-d49289b26849.md) | 自定义共享通道信息 |
