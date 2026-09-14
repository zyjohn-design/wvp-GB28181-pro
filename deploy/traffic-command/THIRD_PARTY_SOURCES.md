# 第三方组件与源码

本离线包中的镜像均以 `linux/amd64` 平台保存。开源组件的名称、来源与构建依据如下：

| 组件 | 镜像/版本 | 源码或官方地址 | 许可证 |
|---|---|---|---|
| WVP-GB28181-pro | commit `fb45787da01cb4f33a0b1dfaa613becf67391c17` | <https://github.com/648540858/wvp-GB28181-pro/tree/fb45787da01cb4f33a0b1dfaa613becf67391c17> | MIT |
| ZLMediaKit | `zlmediakit/zlmediakit:master` | <https://github.com/ZLMediaKit/ZLMediaKit> | MIT |
| mpegts.js | 由适配层 `package-lock.json` 锁定 | <https://github.com/xqq/mpegts.js> | Apache-2.0 |
| MySQL | `mysql:8.4` | <https://hub.docker.com/_/mysql> | GPL-2.0 / 商业双许可，详见官方镜像 |
| Redis | `redis:7.4-alpine` | <https://hub.docker.com/_/redis> | 详见对应 Redis 7.4 发行版与官方镜像 |
| Nginx | `nginx:1.27-alpine` | <https://hub.docker.com/_/nginx> | BSD-2-Clause |

WVP 镜像使用上述固定提交的仓库 Dockerfile 在 `linux/amd64` 平台从源码构建；没有使用可能
滞后的第三方 WVP 二进制镜像。适配层依赖的完整版本以 `adapter/package-lock.json` 为准。

各镜像包含的软件可能同时带有其传递依赖许可证。生产分发前，请结合贵方合规制度审阅
镜像内随附的许可证及各上游项目最新许可声明。
