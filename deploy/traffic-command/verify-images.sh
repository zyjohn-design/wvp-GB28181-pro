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

missing=0
echo "$required_images" | while IFS= read -r image; do
    if ! docker image inspect "$image" >/dev/null 2>&1; then
        echo "缺少镜像：$image" >&2
        missing=1
    fi
done

# POSIX管道中的while可能运行在子Shell中，因此再做一次无输出的最终检查。
echo "$required_images" | while IFS= read -r image; do
    docker image inspect "$image" >/dev/null 2>&1 || exit 1
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
