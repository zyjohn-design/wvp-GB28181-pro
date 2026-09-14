# 指挥调度智能体·宇视 GB28181 视频接入实施记录

## 1. 文档说明

本文档整理本项目从现有 iframe 视频窗口优化，到宇视平台 GB28181 级联、H5 播放适配、
Docker Compose 离线部署及安全问题修复的主要问题、回复结论和已落地代码。

- 项目用途：交管局指挥调度智能体的视频调度接入。
- 宇视对接方式：宇视视频管理平台作为 GB28181 下级平台级联。
- 当前开发分支：本 Fork 的 `master`。
- 当前基线提交：`fb45787da01cb4f33a0b1dfaa613becf67391c17`。
- 自定义部署源码：`deploy/traffic-command/`。
- 原业务页面示例：`deploy/traffic-command/examples/index2.html`。
- H5 适配器：`deploy/traffic-command/adapter/`。

真实 `.env`、SIP 密码、数据库密码、运行数据和离线镜像不提交到 Git。部署时从
`.env.example` 复制并在服务器上填写。

## 2. 对话问题目录

1. iframe 增加标题和关闭按钮。
2. 调整 iframe 的 `top`、`left` 等位置。
3. 按 `title` 查找 `div` 并触发点击事件。
4. 支持单画面、双画面、四画面和视频列表切换。
5. 默认以四画面启动。
6. 视频标题改为设备名称，并支持拖动整个视频窗口。
7. 修复列表切换、单画面满屏/全屏事件和云台面板显示不全。
8. 单画面支持 iframe 放大和视频画面放大。
9. 处理 `dhuman_iframe` 遮挡、窗口默认靠左和双击全屏逻辑。
10. 将科达 H5 接口替换为宇视 GB28181 接入方案。
11. GB28181 视频如何转换为浏览器 H5 播放。
12. 是否必须逐台设备对接。
13. 整理宇视对接资料。
14. 制作 AMD64 Docker 离线部署包及服务器要求。
15. 核对部署所需 Docker 镜像。
16. 同时适配 `docker-compose` v1 和 `docker compose` v2。
17. Docker Nginx 如何加载模板配置。
18. H5 接口是否支持摄像机云台控制。
19. 宇视确认采用 GB28181 下级平台级联。
20. 填写 `.env`、生成 SIP 密码并确认 SIP 参数责任方。
21. WVP 界面能否继续接入更上一级平台。
22. WVP 管理界面的登录地址、用户和密码。
23. 首次登录修改默认密码失败且后台没有日志。
24. 是否应切换到稳定 Tag，以及先修复 master 的决定。
25. 编写交管局视频数据推送审批申请理由。
26. 将全部自定义代码和实施记录纳入 GitHub Fork。

## 3. 问题与回复结论

### 3.1 iframe 标题、关闭、定位和 DOM 操作

问题：为视频 iframe 增加“视频播放”标题和关闭按钮。

回复与实现：不直接把可视标题放进 iframe 属性，而是在 iframe 外创建视频窗口容器，标题栏
显示“视频播放”，关闭按钮负责隐藏或销毁窗口。iframe 自身保留无障碍 `title="视频播放"`。

问题：如何调整 iframe 的上、下、左、右位置。

回复：绝对定位时通过 `top`、`left`、`right`、`bottom` 控制位置。例如 `left: 20px` 表示
距离定位父容器左侧 20 像素。应确保父容器具有明确的定位上下文，避免直接依赖整个页面坐标。

问题：如何根据 `title` 查找 `div` 并触发点击。

回复：可使用属性选择器，例如：

```javascript
const element = document.querySelector('div[title="指定标题"]');
element?.click();
```

如果元素在另一个 iframe 内，只能在同源条件下通过 `iframe.contentDocument` 查询；跨域 iframe
不能直接操作内部 DOM，需要由双方页面通过 `postMessage` 协作。

### 3.2 视频窗口与分屏交互

问题：支持单画面、四画面，最多四路播放，并在 iframe 内加入视频列表。

回复与实现：H5 页面支持最多四路视频；列表展示设备名称、设备编号、播放状态和所在画面。
先点击目标画面，再点击列表项，即可把该设备切换到选中画面。代码位于：

- `deploy/traffic-command/adapter/public/index.html`
- `deploy/traffic-command/adapter/public/app.js`
- `deploy/traffic-command/adapter/public/styles.css`

问题：一开始默认四画面播放。

回复与实现：页面初始化布局设为四画面，单画面和双画面仍可在工具栏切换。

问题：增加双画面、标题使用设备名称、窗口支持拖动。

回复与实现：增加双画面布局；画面标题和右侧列表统一展示设备名称；视频弹窗通过标题栏拖动，
并限制拖动范围，防止整个窗口被拖出可视区域。

问题：列表无法切换、画面满屏/全屏按钮不触发、云台面板显示不全。

回复与实现：统一画面选中状态和列表点击事件；将全屏事件绑定到实际视频容器；云台面板改为
相对于视频窗口定位，并进行边界检测，靠近窗口边缘时自动翻转或收缩，避免被 `overflow` 裁切。

问题：单画面需要同时支持 iframe 放大和视频画面放大。

回复与实现：区分两级放大：外层 iframe/视频窗口放大负责扩大业务页面里的弹窗；播放器全屏
使用浏览器 Fullscreen API。双击视频保留为画面全屏快捷操作。

问题：`dhuman_iframe` 遮挡视频右侧点击区域，且视频窗口应默认靠左。

回复与实现：视频窗口使用更高且明确的层叠上下文；非交互遮罩区域设置合适的
`pointer-events`；视频窗口初始化 `left` 调整到页面左侧。放大逻辑不再重复接管双击全屏，避免
iframe 放大和播放器全屏冲突。

### 3.3 科达 H5 与宇视 GB28181 适配方案

问题：原 `index2.html` 调用科达融合通信 H5 接口，宇视仅支持 GB28181，且不希望修改
`index2.html`。

回复与实现：新增兼容网关，让原 iframe 路径和参数保持不变：

```text
/HiatmpView/video/index.html?deviceIds=通道国标编号&deviceType=IPC
```

实际链路：

```text
宇视视频管理平台
  → GB28181 SIP注册、目录、点播、PTZ
  → WVP-GB28181-pro
  → ZLMediaKit接收RTP/PS并输出HTTP-FLV/TS
  → H5适配器与Nginx
  → 原index2.html iframe
```

问题：怎样支持 H5 接入。

回复：浏览器不能直接播放 GB28181 的 SIP/RTP/PS。WVP 负责国标信令和点播，ZLMediaKit
负责接收媒体并转换封装，H5 适配器选择浏览器可播放地址并通过 Nginx 做同源代理，前端使用
`mpegts.js` 播放。

问题：是否必须逐台摄像机对接。

回复：不需要。宇视视频管理平台或 NVR 作为一个 GB28181 下级平台/设备注册后，可一次上报
其下属通道目录。只有不经过平台或 NVR 的独立 IPC，才需要分别配置。

### 3.4 GB28181 平台级联与参数责任

问题：宇视确认以 GB28181 下级平台方式级联。

回复：当前 WVP 作为上级平台，宇视平台注册到 WVP；宇视一次推送授权范围内的设备目录，
业务点播时再传输实时视频流。

问题：SIP 信息是否都由我方提供。

回复：上级平台 IP、SIP 端口、上级平台域、上级平台编号和注册密码原则上由我方/项目方提供
给宇视，但平台域和国标编号必须由客户或国标编码管理方确认唯一。宇视提供下级平台编号、
通道编号、目录及其信令/媒体出口地址。双方共同确认 UDP/TCP、路由、防火墙、NAT 和校时。

问题：`DEFAULT_GB_DEVICE_ID` 是否必须。

回复与实现：平台级联时不必须，默认留空。适配器会先查询 WVP 设备列表，再查询各下级平台
通道目录，根据通道国标编号自动找到所属平台，并缓存五分钟。显式的 `device-map.json` 映射
仍具有最高优先级。实现和测试位于：

- `deploy/traffic-command/adapter/lib.js`
- `deploy/traffic-command/adapter/server.js`
- `deploy/traffic-command/adapter/test/adapter.test.js`

问题：当前平台界面能否接入另一个上级平台。

回复：可以。WVP 可同时接收宇视作为下级平台，也可在“国标级联”界面把经授权的通道共享
给更上一级平台。必须区分“宇视注册到当前 WVP”的参数和“当前 WVP 注册到更上一级平台”
的参数，两套国标身份不能混用。

### 3.5 Docker、Nginx 与运行环境

问题：制作 AMD64 Docker 包，源码需要挂载以便更新；服务器有什么要求。

回复与实现：提供 Linux AMD64 Compose 部署，H5 适配器 HTML、CSS、JavaScript 和 Node.js
源码从宿主机只读挂载。建议生产服务器至少 8 核、16 GB 内存、100 GB SSD、千兆网络；录像
容量另行规划。默认不转码，H.264 场景不需要 GPU。

问题：所需镜像是否齐全。

回复：离线发布物包含 Redis、MySQL、ZLMediaKit、Nginx、WVP 后端、WVP 前端和 H5 适配器
七个 AMD64 镜像。Git 仓库不保存大型镜像归档，只保存构建源码、校验记录和生成脚本。

问题：服务器使用旧版 `docker-compose`，没有 `docker compose`。

回复与实现：Compose 文件使用兼容格式，并提供 `compose.sh` 自动检测两种命令；包内同时保留
`docker-compose.yml`。日常统一使用 `./compose.sh`。

问题：Docker Nginx 如何加载两个模板。

回复：官方 Nginx 容器启动脚本会处理 `/etc/nginx/templates/*.template`，通过 Compose 卷
挂载到该目录后生成运行配置。对应文件是：

- `config/nginx/h5-gateway.conf.template`
- `config/nginx/wvp-web.conf.template`

问题：H5 接口是否支持云台控制。

回复与实现：支持。H5 前端将方向、变倍和停止命令提交给适配器，适配器调用 WVP PTZ 接口，
WVP 再通过 GB28181 DeviceControl 发给宇视。是否最终生效取决于宇视平台是否开放该通道的
PTZ 权限。

### 3.6 管理登录与密码问题

问题：管理界面的地址、账号和密码是什么。

回复：默认管理地址为 `http://127.0.0.1:18080/`，账号/密码为 `admin/admin`。管理端口默认
只绑定服务器本机，远程管理建议使用 SSH 隧道。`7860` 是 H5 接口，不是管理后台。

问题：首次登录提示修改默认密码，但点击修改时报错；退出日志显示
`[退出登录成功] - [null]`，修改密码没有后台日志。

回复与修复：根因是自定义部署配置曾设置：

```yaml
interface-authentication: false
```

该设置会放行请求，却不会建立登录用户上下文，而修改密码接口需要取得当前用户，因此返回
`code=100`。`[null]` 只是退出接口没有业务返回值，不是故障原因。

现已改为开启管理鉴权，并仅放行 H5 适配器使用的内部接口：

```yaml
interface-authentication: true
interface-authentication-excludes:
  - /api/device/query/devices
  - /api/device/query/devices/**
  - /api/play/start/**
  - /api/play/stop/**
  - /api/front-end/ptz/**
```

实测结果：登录 `code=0`、修改密码 `code=0`、新密码重新登录 `code=0`、默认密码标记变为
`false`，同时设备目录查询和 H5 健康检查保持正常。

问题：是否应改用 `v2.7.4-20260107` 稳定 Tag。

回复：生产发布通常应固定稳定 Tag，但本次密码问题是配置造成的，仅切换 Tag 并不能解决。
根据当前决定，先保留 Fork 的 `master/fb45787da` 并提交已验证的配置修复；稳定 Tag 迁移在完成
接口、数据库和宇视现场回归测试后再单独进行。

### 3.7 数据推送审批申请理由

问题：交管局进行宇视视频数据推送需要审批，如何说明申请理由。

建议文本：

> 为建设指挥调度智能体，提升交通事件研判、现场核实和应急指挥调度效率，申请通过
> GB28181 标准协议接入宇视视频管理平台。接入内容包括审批范围内的视频设备目录、设备状态
> 及按需点播的实时视频流，主要用于道路巡查、事件核实、应急处置和协同调度。系统遵循最小
> 授权、按需调用、安全可控和全程留痕原则，视频数据仅在交管局指定网络和服务器环境中使用，
> 不向未经授权的第三方传输，并对视频访问、点播和云台控制操作进行日志审计。现申请批准
> 宇视平台以 GB28181 下级平台方式向指挥调度智能体视频接入平台进行级联。

## 4. 当前代码目录

```text
deploy/traffic-command/
├── adapter/                    # H5播放器、兼容接口、自动通道发现、PTZ
├── config/
│   ├── nginx/                  # H5网关和WVP管理页面代理
│   ├── redis/                  # Redis配置
│   ├── wvp/                    # WVP Docker运行配置及鉴权修复
│   ├── zlm/                    # ZLMediaKit配置
│   └── device-map.json         # 可选的人工通道映射
├── database/init.sql           # MySQL初始化结构
├── examples/index2.html        # 原业务视频弹窗集成示例
├── compose.yaml                # AMD64服务编排
├── compose.sh                  # Compose v1/v2兼容入口
├── build-local-images.sh       # 从本Fork构建业务镜像
├── start.sh / stop.sh          # 启停脚本
└── README.md                   # 部署与宇视对接说明
```

## 5. 安全与配置原则

1. 不把真实 `.env` 和密码提交到 Git。
2. WVP 管理接口保持鉴权开启，不允许使用 `/api/**` 全量免鉴权。
3. WVP、MySQL、Redis 和 ZLMediaKit 管理端口不暴露到公网。
4. 宇视设备目录和视频通道按审批范围最小化授权。
5. 视频以按需点播为主，不默认批量录像或复制。
6. 云台控制、点播和管理登录应保留审计日志。
7. 正式 SIP 域和平台编号必须经客户编码管理方确认唯一。

## 6. 验证命令

```bash
cd deploy/traffic-command
node --test adapter/test/adapter.test.js
./compose.sh config --quiet
sh -n ./*.sh
```

生产验收还应依次验证：宇视平台注册、目录同步、WVP 单路点播、H5 单/双/四画面、设备列表
切换、双击全屏、窗口拖动、云台控制、日志审计及异常断线恢复。
