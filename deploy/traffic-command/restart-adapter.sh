#!/usr/bin/env sh
set -eu
SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"
./compose.sh restart polaris-adapter
./compose.sh logs --tail=80 polaris-adapter
