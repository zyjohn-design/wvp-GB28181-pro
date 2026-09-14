#!/usr/bin/env sh
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"
./preflight.sh

# 首次启动或镜像被清理后才导入离线镜像，日常重启不会重复导入大文件。
if ! ./verify-images.sh --local-only >/dev/null 2>&1; then
    ./load-images.sh
fi

./compose.sh up -d
./compose.sh ps
