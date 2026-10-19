#!/usr/bin/env bash
set -euo pipefail

WVP_URL='http://localhost:18080'
DEVICE_ID='37020957774653726721'
CHANNEL_ID='34020000001310000055'
command -v curl >/dev/null 2>&1 || { echo '错误：未安装 curl。' >&2; exit 1; }

# 支持提前设置 WVP_API_KEY 或 WVP_ACCESS_TOKEN，也支持运行时交互输入。
if [[ -n "${WVP_API_KEY:-}" ]]; then
    AUTH_HEADER="api-key: $WVP_API_KEY"
elif [[ -n "${WVP_ACCESS_TOKEN:-}" ]]; then
    AUTH_HEADER="access-token: $WVP_ACCESS_TOKEN"
else
    read -r -s -p '请输入 WVP API Key：' WVP_API_KEY
    printf '\n'
    [[ -n "$WVP_API_KEY" ]] || { echo '错误：API Key 不能为空。' >&2; exit 1; }
    AUTH_HEADER="api-key: $WVP_API_KEY"
fi

RECORD_DIR=$(mktemp -d "${TMPDIR:-/tmp}/wvp-preset-cleanup.XXXXXX")
echo "设备：$DEVICE_ID"
echo "通道：$CHANNEL_ID"
echo "查询记录目录：$RECORD_DIR"

query_presets() {
    curl --silent --show-error --fail --connect-timeout 5 --max-time 20 \
        --get "$WVP_URL/api/front-end/preset/query/$DEVICE_ID/$CHANNEL_ID" \
        -H "$AUTH_HEADER"
}

echo '正在查询源端预置位…'
query_presets > "$RECORD_DIR/before.json"
cat "$RECORD_DIR/before.json"
printf '\n\n'

echo 'PresetID_6 是异常返回值，不能直接作为删除参数。'
echo '请填写原添加请求的数字 preset 编号（1-255），直接回车则退出、不删除。'
read -r -p '实际预置位编号：' PRESET_ID
[[ -n "$PRESET_ID" ]] || { echo '已退出，未删除。'; exit 0; }
if [[ ! "$PRESET_ID" =~ ^[1-9][0-9]{0,2}$ ]] || (( PRESET_ID > 255 )); then
    echo '错误：编号必须是 1-255 的整数。' >&2
    exit 1
fi

echo "将删除源端预置位 $PRESET_ID；查询记录不包含云台位置，无法用于恢复。"
read -r -p "确认原添加请求的编号是 $PRESET_ID，请输入 DELETE-$PRESET_ID：" CONFIRM
[[ "$CONFIRM" == "DELETE-$PRESET_ID" ]] || { echo '已取消，未删除。'; exit 0; }

echo '正在提交删除指令…'
curl --silent --show-error --fail --connect-timeout 5 --max-time 20 \
    --get "$WVP_URL/api/v1/control/preset" \
    -H "$AUTH_HEADER" \
    --data-urlencode "serial=$DEVICE_ID" \
    --data-urlencode "code=$CHANNEL_ID" \
    --data-urlencode 'command=remove' \
    --data-urlencode "preset=$PRESET_ID" > "$RECORD_DIR/delete-response.txt"
cat "$RECORD_DIR/delete-response.txt"
printf '\n'

sleep 2
echo '删除请求已提交，重新查询源端列表：'
query_presets > "$RECORD_DIR/after.json"
cat "$RECORD_DIR/after.json"
printf '\n\n'
echo "请核对目标条目是否消失；HTTP 成功不代表设备已执行删除。"
echo "原始列表与删除后列表：$RECORD_DIR"
echo '如果仍返回 PresetID_6，请检查源端预置位编号映射。'
