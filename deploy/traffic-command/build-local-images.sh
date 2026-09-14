#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPOSITORY_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
TARGET_PLATFORM=${TARGET_PLATFORM:-linux/amd64}

echo "构建WVP后端镜像（$TARGET_PLATFORM）"
docker build \
    --platform "$TARGET_PLATFORM" \
    -f "$REPOSITORY_ROOT/docker/wvp/Dockerfile" \
    -t local/gb28181-wvp:fb45787-amd64 \
    "$REPOSITORY_ROOT"

echo "构建WVP前端镜像（$TARGET_PLATFORM）"
docker build \
    --platform "$TARGET_PLATFORM" \
    -f "$REPOSITORY_ROOT/docker/nginx/Dockerfile" \
    -t local/gb28181-wvp-web:fb45787-amd64 \
    "$REPOSITORY_ROOT"

echo "构建H5适配器镜像（$TARGET_PLATFORM）"
docker build \
    --platform "$TARGET_PLATFORM" \
    -f "$SCRIPT_DIR/adapter/Dockerfile" \
    -t local/gb28181-h5-adapter:1.0.0-amd64 \
    "$SCRIPT_DIR/adapter"

echo "本地业务镜像构建完成。"
