# 宇视 GB28181 → H5 视频适配程序

这个程序用于替换原科达融合通信的 `index.html`，但保持 `index2.html` 的调用方式不变：

```text
/HiatmpView/video/index.html?deviceIds=34020000001320000001&deviceType=IPC
```

它不直接实现完整的 GB28181 SIP/媒体协议栈，而是采用成熟的开源组合：

```text
宇视摄像机/NVR
    │ GB28181（SIP + RTP/PS）
    ▼
WVP-GB28181-pro ── 点播、目录、云台
    │
    ▼
ZLMediaKit ── HTTP-FLV/HTTP-TS
    │
    ▼
本适配程序 ── 保持 deviceIds 接口 + 同源代理 + H5 播放
    │
    ▼
现有 index2.html（无需修改）
```

## 已实现

- 完全兼容 `deviceIds`、`deviceId` 和 `deviceType=IPC` 查询参数。
- 最多一次接收四个逗号分隔的通道编号；当前 `index2.html` 每个 iframe 传一路也可以。
- 调用 WVP `/api/play/start/{deviceId}/{channelId}` 启动国标点播。
- 使用 mpegts.js 播放 ZLMediaKit 返回的 HTTP-FLV，HTTP-TS 作为备用。
- 通过 `/media/*` 同源代理解决 CORS、HTTPS 混合内容和 WVP 返回容器内网地址问题。
- 支持双击视频全屏。
- 支持 GB28181 云台方向、变倍和停止命令。
- 云台打开时向父页面发送 `PTZ_OPEN`，兼容前面已经优化的 `index2.html` 自动画面放大逻辑。
- 页面关闭时销毁播放器并通知 WVP 停止点播；WVP 开启按需拉流时也会自动回收。

## 一、部署 WVP-GB28181-pro + ZLMediaKit

推荐直接使用 WVP 官方仓库当前的 Docker 编排，它已经组合 MySQL、Redis、WVP、
ZLMediaKit 和 Nginx：

```bash
git clone https://github.com/648540858/wvp-GB28181-pro.git
cd wvp-GB28181-pro/docker
```

参考本项目的 `deploy/wvp.env.example` 修改官方 `docker/.env`，至少确认：

- `Stream_IP`、`SDP_IP`、`SIP_ShowIP`：宇视设备和浏览器都能访问的视频服务器 IP。
- `SIP_Id`：WVP 作为上级国标平台的 20 位编号。
- `SIP_Domain`：通常使用国标编号前 10 位，必须与客户规划一致。
- `SIP_Port`：示例为 `8116`，TCP/UDP 均需放行。
- `SIP_Password`：宇视侧注册密码，生产环境必须修改。
- `MediaRtp`：示例为单端口 `10000`，TCP/UDP 均需放行。

启动：

```bash
docker compose up -d --build
```

当前官方源码编译要求 JDK 21；使用官方 Dockerfile 构建时由容器处理依赖。

### 宇视侧配置

在宇视 NVR/摄像机的 GB28181 配置中填写：

- 上级平台地址：视频服务器 IP。
- 上级平台端口：`SIP_Port`。
- 上级平台编号：`SIP_Id`。
- 平台域：`SIP_Domain`。
- 注册密码：`SIP_Password`。
- 本地设备国标编号：后面映射文件中的 `deviceId`。
- 视频通道国标编号：`index2.html` 最终传入的 `deviceIds`。

启动后先登录 WVP 管理页面，确认设备在线、目录已同步，并在 WVP 页面中手工点播成功。
如果这一步失败，H5 适配层也无法播放。

### WVP API 鉴权

当前 WVP 支持 `access-token` 和 `api-key`。建议登录 WVP 管理页面后创建一个只供适配程序使用的
API Key，填入本项目 `.env` 的 `WVP_API_KEY`，默认请求头为 `api-key`。

## 二、配置适配程序

```bash
cp .env.example .env
cp config/device-map.example.json config/device-map.json
```

编辑 `.env`：

```dotenv
WVP_BASE_URL=http://127.0.0.1:18978
WVP_API_KEY=从WVP后台生成的API_KEY
WVP_AUTH_HEADER=api-key
ZLM_BASE_URL=http://127.0.0.1:8080
DEFAULT_GB_DEVICE_ID=
PROXY_MEDIA=true
```

`index2.html` 只传通道编号，但 WVP 点播需要“设备编号 + 通道编号”。适配程序按下面顺序补齐设备编号：

1. `config/device-map.json` 中该通道的 `deviceId`。
2. 映射文件的 `defaultDeviceId`。
3. `.env` 中的 `DEFAULT_GB_DEVICE_ID`。
4. 如果前三项均为空，自动查询WVP目录，找到通道所属的下级设备/平台编号。

宇视平台以GB28181下级平台级联时，可以让默认设备编号保持为空，由程序自动发现。
只有需要覆盖自动发现结果时才逐通道配置：

```json
{
  "defaultDeviceId": "34020000001110000001",
  "channels": {
    "34020000001320000001": {
      "deviceId": "34020000001110000001",
      "name": "一号门摄像机"
    },
    "34020000001320000002": {
      "deviceId": "34020000001110000002",
      "name": "停车场摄像机"
    }
  }
}
```

映射文件每次请求都会重新读取，修改后无需重启。

## 三、启动适配程序

### Docker（推荐 Linux）

```bash
docker compose up -d --build
curl http://127.0.0.1:8088/api/health
```

### 直接运行

```bash
npm ci
npm start
```

测试页面：

```text
http://视频服务器:8088/index.html?deviceIds=通道国标编号&deviceType=IPC
```

## 四、保持 index2.html 完全不动

你当前 `index2.html` 使用的是：

```text
http://10.73.191.167:7860/HiatmpView/video/index.html?deviceIds=...&deviceType=IPC
```

要做到一行都不修改，只需在 `10.73.191.167:7860` 对应的 Nginx `server {}` 中加入
`deploy/nginx-location.conf`，把 `/HiatmpView/video/` 反向代理到本程序的 `8088` 端口。

检查并重载：

```bash
nginx -t
nginx -s reload
```

注意：该 location 必须关闭 `proxy_buffering`，否则 HTTP-FLV 会出现延迟累积或长时间不出画面。

## 五、编码和网络要求

- 推荐把宇视子码流设置为 H.264。mpegts.js 可以解析 H.264/H.265 FLV/TS，但最终能否显示 H.265
  仍取决于浏览器、操作系统和 MSE 解码能力；H.264 的兼容性最好。
- WVP/ZLM 与宇视设备之间必须双向可达。SIP 注册成功但 RTP 端口不可达时，表现通常是“点播超时”或黑屏。
- HTTPS 页面必须通过本项目的同源 `/media/*` 代理或完整配置 ZLM HTTPS，否则浏览器会拦截 HTTP 视频流。
- 对公网部署时不要直接暴露 WVP 管理端口和 ZLM 管理 API；通过防火墙、Nginx 和 API Key 限制访问。

## 六、接口说明

| 接口 | 方法 | 说明 |
|---|---|---|
| `/index.html?deviceIds=...&deviceType=IPC` | GET | 与现有 index2.html 兼容的 H5 页面 |
| `/api/streams/start?deviceIds=...` | GET | 解析映射并调用 WVP 开始点播 |
| `/api/streams/stop` | POST | 停止一组通道 |
| `/api/ptz` | POST | 调用 WVP 国标云台控制 |
| `/media/*` | GET | 同源代理 ZLMediaKit 的 HTTP-FLV/TS |
| `/api/health` | GET | 健康检查 |

## 七、验收顺序

1. 宇视设备在 WVP 中显示在线。
2. WVP 已同步出正确的通道国标编号。
3. WVP 自带页面可以实时点播。
4. `/api/health` 返回 `ok: true`。
5. 直接打开本项目 `index.html?deviceIds=通道编号&deviceType=IPC` 可以播放。
6. 最后从原 `index2.html` 发起播放，确认单/双/四画面、列表切换、双击全屏和云台。

## 开源依据

- WVP 点播接口说明：<https://github.com/648540858/wvp-GB28181-pro/wiki/如何使用固定播放地址与自动点播>
- WVP 当前源码：<https://github.com/648540858/wvp-GB28181-pro>
- ZLMediaKit：<https://github.com/ZLMediaKit/ZLMediaKit>
- mpegts.js：<https://github.com/xqq/mpegts.js>
