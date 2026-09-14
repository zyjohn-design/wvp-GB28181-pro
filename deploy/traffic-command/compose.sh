#!/usr/bin/env sh
set -eu

# 同时兼容两种Compose安装方式：
# 1. Docker Compose v2插件：docker compose
# 2. 旧版独立程序：docker-compose
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
COMPOSE_FILE="$SCRIPT_DIR/compose.yaml"

if docker compose version >/dev/null 2>&1; then
    exec docker compose -p gb28181-h5 -f "$COMPOSE_FILE" "$@"
fi

if command -v docker-compose >/dev/null 2>&1; then
    exec docker-compose -p gb28181-h5 -f "$COMPOSE_FILE" "$@"
fi

echo "错误：未找到Docker Compose。请安装docker-compose 1.29.2或Docker Compose v2。" >&2
exit 1
