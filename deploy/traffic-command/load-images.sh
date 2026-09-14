#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
found=0

for archive in "$SCRIPT_DIR"/images/*.tar.gz; do
    if [ ! -f "$archive" ]; then
        continue
    fi
    found=1
    echo "正在导入 $(basename "$archive")"
    gzip -dc "$archive" | docker load
done

if [ "$found" -eq 0 ]; then
    echo "images目录没有离线镜像，请使用在线部署方式：docker compose pull"
fi
