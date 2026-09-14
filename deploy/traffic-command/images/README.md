# 离线镜像目录

Git 仓库不保存大型 Docker 镜像归档。正式制作离线发布包时，将以下文件放入本目录：

- `gb28181-runtime-images-amd64.tar.gz`
- `gb28181-app-images-amd64.tar.gz`

`SHA256SUMS` 保存当前离线发布物的校验值。源码开发环境无需这些归档，可执行
`../build-local-images.sh` 构建三个业务镜像，Redis、MySQL、ZLMediaKit 和 Nginx 基础镜像
由 Docker Compose 在线拉取。
