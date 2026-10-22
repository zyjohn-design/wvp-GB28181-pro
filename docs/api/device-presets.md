# 按设备查询预置位

`GET /api/front-end/preset/query/{deviceId}?timeout=60`

使用现有接口鉴权。`deviceId` 为设备国标编号；`timeout` 为整体超时秒数，默认 60，允许 1–300。

接口实时逐个查询设备下所有视频通道（不含目录），单通道最多等待 5 秒，仅返回预置位列表非空的通道。预置位名称与单通道查询一致，优先使用平台名称备注。

```json
{
  "code": 0,
  "msg": "成功",
  "data": [
    {
      "channelId": "34020000001320000001",
      "channelName": "东门球机",
      "presets": [
        { "presetId": "1", "presetName": "入口" }
      ]
    }
  ]
}
```

全部查询成功且无预置位时返回 `code=0, data=[]`。

部分通道失败、超时或未完成时，返回非零 `code`，`data` 只包含已成功查询到的非空通道，`msg` 提示结果不完整。**只有 `code=0` 的结果可作为全量同步依据**，不可依据失败响应删除已有预置位。通道较多时可增加 `timeout`，整体超时后不再发起后续通道查询。

现有单通道接口 `GET /api/front-end/preset/query/{deviceId}/{channelId}` 保持不变。
