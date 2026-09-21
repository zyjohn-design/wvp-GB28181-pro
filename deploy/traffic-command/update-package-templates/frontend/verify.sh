#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
cd "$SCRIPT_DIR"

if command -v sha256sum >/dev/null 2>&1; then
    sha256sum -c SHA256SUMS
elif command -v shasum >/dev/null 2>&1; then
    shasum -a 256 -c SHA256SUMS
else
    echo "错误：系统缺少 sha256sum 或 shasum，无法校验升级包。" >&2
    exit 1
fi

echo "升级包完整性校验通过。"
