#!/usr/bin/env sh
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"

required_images='redis:7.4-alpine
mysql:8.4
zlmediakit/zlmediakit:master
nginx:1.27-alpine
local/gb28181-wvp:fb45787-amd64
local/gb28181-wvp-web:fb45787-amd64
local/gb28181-h5-adapter:1.0.0-amd64'

normalize_arch() {
    case "$1" in
    x86_64 | amd64) printf 'amd64' ;;
    aarch64 | arm64) printf 'arm64' ;;
    *) printf '%s' "$1" ;;
    esac
}

# 期望架构以运行 Docker 的宿主机为准。离线包里一旦混入别架构的镜像，
# 容器启动只会抛出难以定位的 "exec format error"，这里提前拦截。
host_arch=$(normalize_arch "$(docker info --format '{{.Architecture}}' 2>/dev/null || true)")
if [ -z "$host_arch" ]; then
    echo "警告：无法获取Docker宿主机架构，跳过镜像架构检查。" >&2
fi

# 用 for 循环而不是管道 while：管道会派生 SubShell，exit 无法真正中断脚本。
for image in $required_images; do
    if ! docker image inspect "$image" >/dev/null 2>&1; then
        echo "缺少镜像：$image" >&2
        exit 1
    fi

    if [ -n "$host_arch" ]; then
        image_arch=$(normalize_arch "$(docker image inspect --format '{{.Architecture}}' "$image")")
        if [ "$image_arch" != "$host_arch" ]; then
            echo "错误：镜像 $image 的架构为 ${image_arch}，当前Docker宿主机为 ${host_arch}。" >&2
            echo "      离线包与宿主机架构不匹配，容器启动会报 exec format error。" >&2
            echo "      请更换与宿主机架构一致的离线包，或重新执行 ./load-images.sh 导入正确的镜像。" >&2
            exit 1
        fi
    fi
done

if [ "${1:-}" = "--local-only" ]; then
    exit 0
fi

if command -v sha256sum >/dev/null 2>&1; then
    (cd images && sha256sum -c SHA256SUMS)
elif command -v shasum >/dev/null 2>&1; then
    (cd images && shasum -a 256 -c SHA256SUMS)
else
    echo "警告：系统没有sha256sum或shasum，跳过离线文件校验。" >&2
fi

echo "镜像完整性与本地镜像检查通过。"
