#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"

if ! command -v docker >/dev/null 2>&1; then
    echo "错误：未安装Docker Engine。"
    exit 1
fi

if ! docker info >/dev/null 2>&1; then
    echo "错误：Docker服务未启动或当前用户无权限访问。"
    exit 1
fi

arch=$(uname -m)
case "$arch" in
    x86_64|amd64) ;;
    *) echo "警告：当前架构为${arch}，本包目标架构为linux/amd64。" ;;
esac

if [ ! -f .env ]; then
    echo "错误：缺少.env。请先执行 cp .env.example .env 并修改参数。"
    exit 1
fi

# 只检查实际配置行，说明注释中出现“请修改”不会造成误报。
if grep -v '^[[:space:]]*#' .env | grep -q '请修改\|192.168.1.100'; then
    echo "错误：.env仍包含示例值，请先填写服务器IP和密码。"
    exit 1
fi

# 离线包/压缩包不会保存空目录，启动前统一创建持久化与日志目录。
mkdir -p data/mysql data/redis data/record data/wvp-config logs/wvp logs/zlm images

./compose.sh config --quiet
echo "预检查通过。"
