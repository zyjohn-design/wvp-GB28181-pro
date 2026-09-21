#!/usr/bin/env sh
set -eu

# 导出 WVP 中的视频通道（包含表结构）。
# 用法：
#   ./export-video-channels.sh [部署目录] [输出目录]
# 默认部署目录为当前目录，默认输出到 <部署目录>/exports。

DEPLOY_DIR=$(CDPATH= cd -- "${1:-.}" && pwd)
OUT_DIR=${2:-$DEPLOY_DIR/exports}

if [ ! -f "$DEPLOY_DIR/compose.yaml" ] || [ ! -x "$DEPLOY_DIR/compose.sh" ]; then
    echo "错误：$DEPLOY_DIR 不是有效的 traffic-command 部署目录。" >&2
    exit 1
fi
if [ ! -f "$DEPLOY_DIR/.env" ]; then
    echo "错误：$DEPLOY_DIR/.env 不存在。" >&2
    exit 1
fi
if ! command -v docker >/dev/null 2>&1; then
    echo "错误：未找到 docker。" >&2
    exit 1
fi

# 从现场 .env 读取 root 密码，不把密码写入 mysqldump 的命令行参数。
MYSQL_ROOT_PASSWORD=$(awk -F= '
    $1 == "MYSQL_ROOT_PASSWORD" {
        value = $0
        sub(/^[^=]*=/, "", value)
        print value
        exit
    }
' "$DEPLOY_DIR/.env")
if [ -z "$MYSQL_ROOT_PASSWORD" ]; then
    echo "错误：.env 中没有 MYSQL_ROOT_PASSWORD。" >&2
    exit 1
fi

cd "$DEPLOY_DIR"
MYSQL_ID=$(./compose.sh ps -q polaris-mysql)
if [ -z "$MYSQL_ID" ]; then
    echo "错误：polaris-mysql 容器未运行，请先执行 ./compose.sh up -d polaris-mysql。" >&2
    exit 1
fi

mkdir -p "$OUT_DIR"
STAMP=$(date +%Y%m%d_%H%M%S)
ALL_DUMP="$OUT_DIR/wvp_video_channels_all_$STAMP.sql"
GB_DUMP="$OUT_DIR/wvp_gb28181_video_channels_$STAMP.sql"
SCHEMA_DUMP="$OUT_DIR/wvp_device_channel_schema_$STAMP.sql"
TSV_EXPORT="$OUT_DIR/wvp_video_channels_all_$STAMP.tsv"

MYSQL_ENV="MYSQL_PWD=$MYSQL_ROOT_PASSWORD"
MYSQL_ARGS="--default-character-set=utf8mb4"
DUMP_ARGS="--no-tablespaces --single-transaction --skip-lock-tables --quick --set-gtid-purged=OFF --default-character-set=utf8mb4"

echo "导出所有视频通道（channel_type=0，包含表结构）..."
docker exec -e "$MYSQL_ENV" "$MYSQL_ID" mysqldump $DUMP_ARGS wvp wvp_device_channel \
    --where='channel_type = 0' > "$ALL_DUMP"

echo "导出国标视频通道（data_type=1 且 channel_type=0，包含表结构）..."
docker exec -e "$MYSQL_ENV" "$MYSQL_ID" mysqldump $DUMP_ARGS wvp wvp_device_channel \
    --where='data_type = 1 AND channel_type = 0' > "$GB_DUMP"

echo "导出原始表结构..."
docker exec -e "$MYSQL_ENV" "$MYSQL_ID" mysql $MYSQL_ARGS -uroot wvp \
    -e 'SHOW CREATE TABLE wvp_device_channel;' > "$SCHEMA_DUMP"

echo "导出便于 Excel 打开的 TSV（按现场实际表结构导出全部字段）..."
docker exec -e "$MYSQL_ENV" "$MYSQL_ID" mysql --batch --raw $MYSQL_ARGS -uroot wvp -e '
SELECT dc.*
FROM wvp_device_channel dc
WHERE dc.channel_type = 0
ORDER BY dc.id;
' > "$TSV_EXPORT"

CHECKSUM_FILE="$OUT_DIR/wvp_video_channels_$STAMP.sha256"
if command -v sha256sum >/dev/null 2>&1; then
    sha256sum "$ALL_DUMP" "$GB_DUMP" "$SCHEMA_DUMP" "$TSV_EXPORT" > "$CHECKSUM_FILE"
elif command -v shasum >/dev/null 2>&1; then
    shasum -a 256 "$ALL_DUMP" "$GB_DUMP" "$SCHEMA_DUMP" "$TSV_EXPORT" > "$CHECKSUM_FILE"
else
    echo "警告：未找到 sha256sum 或 shasum，跳过校验清单生成。" >&2
    CHECKSUM_FILE=""
fi

echo "导出完成："
echo "  所有视频通道 SQL（含结构）：$ALL_DUMP"
echo "  国标视频通道 SQL（含结构）：$GB_DUMP"
echo "  原始表结构：$SCHEMA_DUMP"
echo "  TSV 明细：$TSV_EXPORT"
if [ -n "$CHECKSUM_FILE" ]; then
    echo "  校验清单：$CHECKSUM_FILE"
fi
