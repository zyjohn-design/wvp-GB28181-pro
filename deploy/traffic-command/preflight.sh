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

# 目标架构以 RELEASE_INFO.txt 为准，避免脚本里写死架构导致误报（arm64 包曾提示 amd64）。
expected_arch=$(sed -n 's/^target_arch=//p' RELEASE_INFO.txt 2>/dev/null | head -n 1)
arch=$(uname -m)
case "$arch" in
    x86_64|amd64) host_arch=amd64 ;;
    aarch64|arm64) host_arch=arm64 ;;
    *) host_arch=$arch ;;
esac
if [ -n "$expected_arch" ] && [ "$host_arch" != "$expected_arch" ]; then
    echo "警告：当前架构为${host_arch}，本包目标架构为linux/${expected_arch}，容器可能无法运行。"
fi

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
