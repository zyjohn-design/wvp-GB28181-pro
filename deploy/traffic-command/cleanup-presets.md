# 国标预置位清理

WVP 的国标预置位查询发送 SIP PresetQuery，列表来自设备或下级平台，不在 WVP MySQL 表中持久化。删除 WVP 数据库或清空 Redis 不能清理源端预置位。

`PresetID_6` 不是合法的 1–255 数字编号。不要直接传给删除接口，也不要只凭后缀认定实际编号为 6。先在设备/下级平台确认编号映射；若无法确认，请在源端管理页面删除异常条目或修正 PresetQuery 返回值。

## 使用脚本（Python 3，无额外依赖）

将同目录 `cleanup-presets.py` 上传到服务器。通过 WVP Web 端口访问；地址以现场配置为准。国标编号可以在设备、下级平台、通道详情中查看。令牌使用登录后的 `access-token` 原值。

```sh
# 交互输入凭据，避免写进命令历史。API Key 与登录令牌任选一种。
read -r -s -p 'WVP API Key: ' WVP_API_KEY; echo
export WVP_API_KEY

# 默认只查询，并在当前目录保存原始列表。
python3 cleanup-presets.py --url http://127.0.0.1:18080 \
  --device-id 设备或下级平台的20位国标编号 --channel-id 通道的20位国标编号
```

如果没有 API Key，可使用 `read -r -s -p 'WVP access-token: ' WVP_ACCESS_TOKEN; echo`，然后 `export WVP_ACCESS_TOKEN`。

只有确认异常条目实际对应数字编号 **6** 时才使用以下命令。它会删除设备上的真实预置位 6；位置无法通过列表 JSON 恢复：

```sh
python3 cleanup-presets.py --url http://127.0.0.1:18080 \
  --device-id 设备或下级平台的20位国标编号 --channel-id 通道的20位国标编号 \
  --preset-id 6 --reported-id PresetID_6 --delete
```

脚本查询确认异常条目存在后，保存列表，发送一次删除命令，再最多查询三次。退出码 0 表示查询完成或删除经重新查询验证；1 表示接口、鉴权或文件错误；2 表示目标仍存在。保留输出的 JSON 用于排查。完成后执行 `unset WVP_API_KEY WVP_ACCESS_TOKEN`。

如果异常条目仍存在，应在返回该条目的设备或下级平台处理，而不是反复删除不同数字编号。页面关闭并重新打开播放窗口可重新查询列表。

## 现有接口

鉴权头为 `api-key: <API Key>` 或 `access-token: <登录令牌>`。

| 用途 | 方法及地址 | 编号含义 |
| --- | --- | --- |
| 查询国标通道 | `GET /api/front-end/preset/query/{deviceId}/{channelId}` | 两个路径参数都是国标编号 |
| 删除国标预置位 | `GET /api/front-end/preset/delete/{deviceId}/{channelId}?presetId=6` | presetId 为已核实的真实数字编号 |
| 查询通用通道 | `GET /api/common/channel/front-end/preset/query?channelId=123` | channelId 为 WVP 通用通道数据库整数 ID |
| 删除通用通道预置位 | `GET /api/common/channel/front-end/preset/delete?channelId=123&presetId=6` | 同上；presetId 为 1–255 |

删除接口成功只表示 WVP 已提交命令，不保证设备执行成功；需要再次查询验证。脚本可以直接用于已有版本，无需重新安装前后端更新包。

## 第三方通过兼容接口添加的预置位

若第三方调用的是 `GET /api/v1/control/preset?serial=设备国标编号&code=通道国标编号&command=set&preset=6`，此接口也是向源端发送 SIP 预置位设置指令，不会在 WVP 数据库中插入预置位记录。

使用原添加请求中的 `serial`、`code` 和数字 `preset`，将 `command=set` 改为 `command=remove` 即可发送删除命令：

```http
GET /api/v1/control/preset?serial=设备国标编号&code=通道国标编号&command=remove&preset=6
```

数字 `6` 仅为示例，应以原添加请求为准，不要以异常返回值的后缀为准。请显式填写 `code`；当前兼容实现没有将 `channel` 通道序号转换成 `code`。删除后用前述查询接口或脚本核实。若第三方使用其他接口，先核对原请求 URL 和参数以确认清理位置。
