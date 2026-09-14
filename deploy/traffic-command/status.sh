#!/usr/bin/env sh
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"
./compose.sh ps
echo
echo "最近日志"
./compose.sh logs --tail=40 polaris-wvp polaris-media polaris-adapter
