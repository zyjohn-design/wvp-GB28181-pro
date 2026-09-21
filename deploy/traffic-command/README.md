# 宇视 GB28181 → H5（Linux AMD64 离线部署包）

> 本目录是纳入 Git 管理的源码版本。仓库不保存真实 `.env`、运行数据和约 1 GB 的离线镜像。
> 首次使用先执行 `cp .env.example .env` 并填写现场参数；在线开发环境可运行
> `./build-local-images.sh` 构建本仓库的 WVP 前后端及 H5 适配器镜像。正式离线发布时再将
> 镜像归档放入 `images/`。

本包把宇视设备的 GB28181 视频转换为浏览器可播放的 H5 视频，并保持现有
`index2.html` 的 iframe 地址和查询参数不变：

```text
/HiatmpView/video/index.html?deviceIds=通道国标编号&deviceType=IPC
```

整体链路：

```text
宇视摄像机/NVR ──GB28181(SIP + RTP/PS)──> WVP + ZLMediaKit
                                               │ HTTP-FLV/TS
                                               ▼
现有 index2.html <──原iframe接口── H5适配层 + Nginx网关
```

## 1. 服务器要求

### 最低配置（联调、小规模）

- CPU：64 位 x86_64/AMD64，4 核。
- 内存：8 GB。
- 系统盘：可用空间至少 30 GB；如果录像，必须另行规划录像盘。
- 网卡：千兆网卡，服务器、宇视设备/NVR之间双向可达。
- 系统：Ubuntu Server 22.04/24.04 LTS 或 Rocky Linux 9，建议 Linux 内核 5.10 以上。
- Docker：Docker Engine 24 以上；支持独立版 `docker-compose 1.29.2`，也支持
  Docker Compose v2.20 以上。
- 地址与时间：固定 IPv4 地址；必须开启 NTP，服务器与宇视设备时间保持一致。

### 推荐配置（生产、小于约100路接入、同时播放不超过16路）

- CPU：8 核 x86_64/AMD64。
- 内存：16 GB。
- 系统盘：100 GB SSD。
- 录像盘：按保留天数单独计算，建议独立 SSD/HDD 阵列。
- 网络：千兆或更高，并给媒体流留足上下行带宽。

本方案对 H.264 主要做封装转换，不做转码，因此一般不需要 GPU。宇视设备请优先配置
H.264 子码流。若必须把 H.265 实时转成 H.264，则属于转码场景，需要额外 CPU/GPU，
不在本包默认能力内。

容量估算：

- 网络峰值约为“同时播放路数 × 单路码率 × 1.3”。例如 16 路 × 2 Mbps，建议至少预留约 42 Mbps。
- 录像空间约为“码率 × 86400 ÷ 8”。2 Mbps 一路约 21.6 GB/天，4 Mbps 一路约 43.2 GB/天。

> 本离线包只能部署在原生 AMD64/x86_64 Linux 服务器。ARM64 服务器虽然可能通过模拟运行，
> 但性能和稳定性不适合生产。

## 2. 必须放行的端口

| 端口 | 协议 | 用途 | 建议访问来源 |
|---|---|---|---|
| `8116` | TCP + UDP | GB28181 SIP 注册、心跳、信令 | 宇视设备/NVR |
| `10000` | TCP + UDP | GB28181 RTP/PS 单端口媒体 | 宇视设备/NVR |
| `7860` | TCP | 现有 index2.html 的 H5 iframe 接口 | 业务客户端 |
| `18080` | TCP | WVP 管理页面 | 默认仅服务器本机 |
| `18082` | TCP | ZLM HTTP 媒体：MP4(FMP4)/HLS/TS/WebRTC 播放地址 | 播放客户端、对接方 |
| `10935` | TCP + UDP | RTMP（预留） | 按需放行 |
| `5540` | TCP + UDP | RTSP（预留） | 按需放行 |

MySQL、Redis、WVP 后端和 ZLMediaKit 管理接口不对外暴露。不要把 3306、6379、18978
开放到公网；`18081` 只用于本机调试，也不要对外放行。

### 2.1 修改媒体 HTTP 端口（`ZLM_HTTP_PORT`）

WVP 页面里 MP4（FMP4）、HLS、TS、WebRTC 的播放地址由 `ZLM_HTTP_PORT` 决定，
默认 `18082`。该端口同时用于 WVP 调用 ZLMediaKit 的接口，因此**三个文件必须保持一致**：

| 位置 | 配置项 |
|---|---|
| `.env` | `ZLM_HTTP_PORT=18082` |
| `config/zlm/config.ini` | `[http] port=18082`（ZLMediaKit 不支持变量，需手工同步） |
| `config/nginx/wvp-web.conf.template` | `proxy_pass http://polaris-media:18082`（需手工同步） |
| `config/wvp/application-docker.yml` | `media.http-port: ${ZLM_HTTP_PORT:80}`（已引用变量，无需再改） |

> nginx 模板里写的是固定端口，不使用 `${ZLM_HTTP_PORT}` 变量。官方 nginx 镜像的
> envsubst 只会替换**容器环境里存在**的变量，若容器没有重建（例如只执行了
> `./compose.sh restart polaris-wvp-web` 或只同步了模板文件），变量取不到就会在配置里
> 原样保留，nginx 启动报 `unknown "ZLM_HTTP_PORT" variable` 并导致 web 容器起不来。

修改后重建相关容器并放行新端口（三个文件要一起改）：

```bash
vi .env                    # 改 ZLM_HTTP_PORT
vi config/zlm/config.ini   # 同步 [http] port
vi config/nginx/wvp-web.conf.template   # 同步 proxy_pass 端口
./compose.sh up -d --force-recreate polaris-media polaris-wvp polaris-wvp-web polaris-adapter
curl -I http://127.0.0.1:18082/index/api/getServerConfig?secret=su6TiedN2rVAmBbIDX0aa0QTiBJLBdcf
```

FLV 的端口由 `WVP_WEB_PORT`（18080，经 WVP Web 网关代理）决定，与 `ZLM_HTTP_PORT` 相互独立。
不要在宿主机上占用 80 端口来暴露 ZLM，改用本节方式指定独立端口即可。

如果单端口 RTP 与某些网络设备不兼容，可以后续切换多端口模式，并放行一个明确的
TCP/UDP 端口范围（例如 `30000-30500`）。默认包先采用更容易维护的单端口模式。

## 3. 首次部署

离线发布包上传到 AMD64 Linux 服务器后执行：

```bash
cd /opt/gb28181-h5
# 正式发布包可以附带预配置.env；仍须核对服务器IP和正式国标编码。
vi .env
./load-images.sh
./verify-images.sh
./start.sh
```

从 Git 仓库进行在线构建和启动：

```bash
cd deploy/traffic-command
cp .env.example .env
vi .env
./build-local-images.sh
./start.sh
```

启动脚本会自动检测 Compose 命令：服务器只有旧版 `docker-compose` 时会直接使用它；安装了
新版插件时则使用 `docker compose`。包内同时提供 `docker-compose.yml`，因此也可以手工执行：

```bash
docker-compose up -d
docker-compose ps
```

为了让同一套运维命令适用于两种版本，建议日常统一使用 `./compose.sh`，例如
`./compose.sh logs -f polaris-wvp`。

`.env` 至少需要修改：

- `SERVER_IP`：宇视设备和业务客户端均可访问的服务器固定 IPv4，不能填 `127.0.0.1`。
- `SIP_ID`、`SIP_DOMAIN`、`SIP_PORT`、`SIP_PASSWORD`：必须与宇视侧配置完全一致。
- `MYSQL_ROOT_PASSWORD`、`MYSQL_WVP_PASSWORD`：改成强密码。
- `DEFAULT_GB_DEVICE_ID`：平台级联时可以留空，适配层会根据通道编号自动查询所属下级平台。

本部署包已经生成并填写了一套可启动的密码。当前 `SERVER_IP=10.73.191.167`，如果这不是
宇视平台实际能够访问的部署服务器地址，必须在启动前修改。`SIP_DOMAIN=4201060000`、
`SIP_ID=42010600002000000001` 是按武昌区行政区划生成的预配置值，仍须由项目方或国标编码
管理方确认未被占用；不能仅因为格式正确就直接认定为正式编号。

离线镜像已经放在 `images/` 中。`start.sh` 会在本机缺少镜像时自动导入，后续重启不会
重复导入。首次启动 MySQL 需要初始化数据库，通常等待 1～3 分钟：

```bash
./status.sh
curl http://127.0.0.1:7860/health
```

停止和再次启动：

```bash
./stop.sh
./start.sh
```

`./stop.sh` 不会删除 `data/` 中的数据库、Redis 数据和录像。不要执行
`./compose.sh down -v`，也不要直接删除 `data/`。

## 4. 宇视侧对接参数

本系统作为 GB28181 **上级平台**，以下信息原则上由我方/项目方确定，再提供给宇视配置：

- 上级平台服务器 IP、SIP 监听端口；
- 上级平台域、20 位上级平台国标编号；
- 注册鉴权密码 `SIP_PASSWORD`（应通过安全渠道交付）；
- 允许接入的网络范围、传输协议和 RTP 端口。

其中平台域和国标编号应由客户的国标编码管理方统一规划并确认唯一，不能由实施人员随意
长期使用。宇视侧需要提供下级平台/设备的国标编号、通道国标编号、通道目录，以及其信令和
媒体出口 IP。双方共同确认 UDP/TCP、路由、防火墙、NAT 和时间同步。

在宇视 NVR 或摄像机的“国标/GB28181/平台接入”页面填写：

- 上级平台 IP：`.env` 中的 `SERVER_IP`。
- 上级平台端口：`.env` 中的 `SIP_PORT`，默认 `8116`。
- 上级平台编号：`.env` 中的 `SIP_ID`。
- 上级平台域：`.env` 中的 `SIP_DOMAIN`。
- 注册密码：`.env` 中的 `SIP_PASSWORD`。
- 传输协议：现场优先测试 UDP；网络复杂或 UDP 丢包明显时再测试 TCP。
- 设备国标编号：设备或 NVR 的 20 位编号，必须唯一。
- 通道国标编号：每个摄像机通道的 20 位编号，必须唯一。
- 心跳周期：建议 60 秒；注册有效期建议 3600 秒。
- 视频编码：优先 H.264 子码流。

对接不是只能“一台摄像机一台摄像机地配置”。推荐宇视 NVR 作为一个 GB28181 设备接入，
由 NVR 一次上报其下属通道目录；只有独立 IPC 不经过 NVR 时，才分别配置每台 IPC。

## 5. 多个 NVR 与通道映射

现有 `index2.html` 只传通道编号，而 WVP 点播需要“设备编号 + 通道编号”。

平台级联时建议让 `DEFAULT_GB_DEVICE_ID` 保持为空。适配层会查询WVP设备目录，自动找到每个
通道所属的宇视下级平台，并缓存5分钟，因此一个或多个下级平台均可使用。

只有需要手工覆盖自动查询结果，或者现场存在重复/异常目录时，才编辑
`config/device-map.json`：

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

映射文件每次请求时都会重新读取，修改后不需要重启容器。

## 6. 保持 index2.html 不变

默认入口为：

```text
http://服务器IP:7860/HiatmpView/video/index.html?deviceIds=通道国标编号&deviceType=IPC
```

如果原业务服务器的 `7860` 已经被现有 Nginx 占用：

1. 把 `.env` 的 `H5_PUBLIC_PORT` 改成 `17860`。
2. 把 `config/nginx-existing-location.conf` 中的 location 合并到原 Nginx 的 `server {}`。
3. 执行 `nginx -t`，确认无误后重载原 Nginx。

这样原 `index2.html` 的 URL 一行都不用改。

## 7. 登录 WVP 管理页面

管理端口默认只监听服务器 `127.0.0.1`，通过 SSH 隧道访问最安全：

```bash
ssh -L 18080:127.0.0.1:18080 用户名@服务器IP
```

随后在本机浏览器打开 `http://127.0.0.1:18080/`。初始化数据库中的默认账号/密码是
`admin/admin`，首次登录会提示修改；生产部署必须立即改成强密码。

如果旧部署在修改默认密码时只提示“失败”，且后台没有对应日志，请检查
`config/wvp/application-docker.yml`：`interface-authentication` 必须为 `true`。设为 `false`
虽然会放行接口，但不会建立当前登录用户上下文，修改密码接口因此无法识别用户。本包已经
开启管理鉴权，并只对 H5 适配器需要的设备查询、点播、停止和云台接口做最小范围放行。
修改配置后执行：

```bash
./compose.sh restart polaris-wvp
```

然后退出页面、清除旧登录状态或使用无痕窗口重新登录，再修改密码。新密码至少 10 位，必须
同时包含大写字母、小写字母、数字和特殊符号，并且不能包含用户名 `admin`。

如果必须从管理网直接访问，可把 `.env` 的 `WVP_BIND_IP` 改成服务器管理网 IP，并只在
防火墙中允许可信管理网段。不要改成 `0.0.0.0` 后直接暴露到公网。

## 8. 更新挂载出来的代码

适配层的 HTML、CSS、JavaScript 和 Node.js 源码位于宿主机 `adapter/`，容器通过只读目录
挂载运行。修改文件后执行：

```bash
./restart-adapter.sh
```

仅修改 `config/device-map.json` 不需要重启。修改 `package.json` 或 `package-lock.json`、需要
新增 npm 依赖时，则必须重新构建适配层镜像，因为 `node_modules` 固定保存在镜像内部。

更新前建议复制一份当前目录并备份 `data/mysql`。不要在运行时修改 WVP 镜像内的文件；
WVP 配置位于宿主机 `config/wvp/`，修改后重启 `polaris-wvp` 即可。

## 9. 验收顺序

1. `./status.sh` 中所有核心容器处于 `Up`/`healthy`。
2. WVP 页面显示宇视设备在线，能够同步出通道目录。
3. 在 WVP 页面手工点播一路视频成功。
4. `curl http://127.0.0.1:7860/health` 返回正常。
5. 直接打开 H5 iframe URL 能播放。
6. 最后从原 `index2.html` 验证单、双、四画面、列表切换、双击全屏和云台控制。

常用排查命令：

```bash
./status.sh
./compose.sh logs -f polaris-wvp
./compose.sh logs -f polaris-media
./compose.sh logs -f polaris-adapter
```

典型问题：设备在线但黑屏，一般先检查 `10000/TCP+UDP`、防火墙/NAT 和服务器 `SERVER_IP`；
设备无法注册，则先核对 SIP 平台编号、域、端口、密码和双方系统时间。

## 10. 包内组件

- WVP-GB28181-pro：GB28181 SIP、目录、点播、云台控制。
- ZLMediaKit：接收 RTP/PS，并输出 HTTP-FLV/TS 等 H5 媒体。
- H5 适配层：兼容现有 iframe 参数、播放器、云台及媒体同源代理。
- MySQL 8.4、Redis 7.4、Nginx 1.27。

具体版本、源码地址和许可证见 `THIRD_PARTY_SOURCES.md`。
