#!/usr/bin/env sh
set -eu

# 把离线部署包发布到 GitLab Releases：
#   1) 上传压缩包到 Generic Package Registry
#   2) 为版本 tag 创建 Release 并挂上下载链接
#
# 用法（在能访问 GitLab 的机器上执行）：
#   GITLAB_URL=http://10.16.1.86:21110 \
#   GITLAB_TOKEN=<Personal Access Token，scope: api> \
#   ./publish-release.sh releases/wvp-offline-arm64-v1.0.0.tar.gz v1.0.0
#
# 可选环境变量：PROJECT_PATH（默认下方仓库路径）、PACKAGE_NAME（默认取包名前缀）

FILE=${1:?用法: GITLAB_URL=... GITLAB_TOKEN=... $0 <包文件> [版本号]}
VERSION=${2:-$(basename "$FILE" .tar.gz | sed 's/^wvp-offline-[a-z0-9]*-//')}
GITLAB_URL=${GITLAB_URL:?请设置 GITLAB_URL，例如 http://10.16.1.86:21110}
GITLAB_TOKEN=${GITLAB_TOKEN:?请设置 GITLAB_TOKEN（Personal Access Token，scope: api）}
PROJECT_PATH=${PROJECT_PATH:-hitap/gitlab/hitap2.0/hi_wvp_gb28181_pro}
PACKAGE_NAME=${PACKAGE_NAME:-$(basename "$FILE" .tar.gz | sed "s/-${VERSION}$//")}

API="${GITLAB_URL%/}/api/v4"
PROJECT_ENC=$(printf '%s' "$PROJECT_PATH" | sed 's|/|%2F|g')
BASENAME=$(basename "$FILE")

command -v curl >/dev/null 2>&1 || { echo "错误：未找到 curl。" >&2; exit 1; }
[ -f "$FILE" ] || { echo "错误：文件不存在：$FILE" >&2; exit 1; }

echo "[1/4] 校验文件 sha256..."
if [ -f "$FILE.sha256" ]; then
    if command -v sha256sum >/dev/null 2>&1; then
        ( cd "$(dirname "$FILE")" && sha256sum -c "$(basename "$FILE").sha256" )
    else
        ( cd "$(dirname "$FILE")" && shasum -a 256 -c "$(basename "$FILE").sha256" )
    fi
else
    echo "  未找到 $BASENAME.sha256，跳过"
fi

echo "[2/4] 查询项目 ID：$PROJECT_PATH"
PROJECT_RESP=$(curl -sS --max-time 60 --write-out '\n%{http_code}' \
    --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
    "$API/projects/$PROJECT_ENC" 2>&1) || { echo "$PROJECT_RESP" >&2; exit 1; }
PROJECT_CODE=$(printf '%s' "$PROJECT_RESP" | tail -n 1)
PROJECT_JSON=$(printf '%s' "$PROJECT_RESP" | sed '$d')
case "$PROJECT_CODE" in
    2*) ;;
    *) echo "错误：查询项目失败（HTTP $PROJECT_CODE）：$PROJECT_JSON" >&2; exit 1 ;;
esac
PROJECT_ID=$(printf '%s' "$PROJECT_JSON" | tr -d ' \n' | sed -n 's/.*"id":\([0-9][0-9]*\).*/\1/p' | head -n 1)
[ -n "$PROJECT_ID" ] || { echo "错误：未解析到项目ID，返回：$PROJECT_JSON" >&2; exit 1; }
echo "  项目ID：$PROJECT_ID"

echo "[3/4] 上传到 Generic Package Registry（$(du -h "$FILE" | cut -f1)，请耐心等待）..."
UPLOAD_RESP=$(curl -sS --max-time 3600 --write-out '\n%{http_code}' \
    --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
    --upload-file "$FILE" \
    "$API/projects/$PROJECT_ID/packages/generic/$PACKAGE_NAME/$VERSION/$BASENAME" 2>&1) \
    || { echo "上传失败：$UPLOAD_RESP" >&2; exit 1; }
UPLOAD_CODE=$(printf '%s' "$UPLOAD_RESP" | tail -n 1)
case "$UPLOAD_CODE" in
    2*) ;;
    *) echo "上传失败（HTTP $UPLOAD_CODE）：$(printf '%s' "$UPLOAD_RESP" | sed '$d')" >&2
       echo "提示：若为 413，请在 GitLab 侧调大 nginx client_max_body_size / 包大小限制。" >&2
       exit 1 ;;
esac
echo "  上传成功"

ASSET_URL="$GITLAB_URL/$PROJECT_PATH/-/packages/$PACKAGE_NAME/$VERSION/$BASENAME"
echo "[4/4] 创建/更新 Release：$VERSION"
RELEASE_PAYLOAD=$(cat <<JSON
{
  "name": "WVP 指挥调度视频平台 $VERSION",
  "tag_name": "$VERSION",
  "description": "完整离线部署包（$PACKAGE_NAME），含全部镜像与 HiMap 地图配置说明。\n\n下载后执行：tar -xzf $BASENAME && cd $PACKAGE_NAME && ./verify.sh && vi .env && ./load-images.sh && ./verify-images.sh && ./start.sh",
  "assets": {
    "links": [
      { "name": "$BASENAME", "url": "$ASSET_URL" },
      { "name": "$BASENAME.sha256", "url": "$ASSET_URL.sha256" }
    ]
  }
}
JSON
)

RELEASE_RESULT=$(curl -sS --request POST --max-time 120 --write-out '\n%{http_code}' \
    --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
    --header "Content-Type: application/json" \
    --data "$RELEASE_PAYLOAD" \
    "$API/projects/$PROJECT_ID/releases" 2>&1) || true
RELEASE_BODY=$(printf '%s' "$RELEASE_RESULT" | sed '$d')

case "$RELEASE_BODY" in
    *'"tag_name"'*) echo "  Release 创建成功：$GITLAB_URL/$PROJECT_PATH/-/releases/$VERSION" ;;
    *)
        echo "  Release 创建失败或已存在，改用更新接口..."
        RELEASE_RESULT=$(curl -sS --request PUT --max-time 120 --write-out '\n%{http_code}' \
            --header "PRIVATE-TOKEN: $GITLAB_TOKEN" \
            --header "Content-Type: application/json" \
            --data "$RELEASE_PAYLOAD" \
            "$API/projects/$PROJECT_ID/releases/$VERSION" 2>&1) || true
        RELEASE_BODY=$(printf '%s' "$RELEASE_RESULT" | sed '$d')
        case "$RELEASE_BODY" in
            *'"tag_name"'*) echo "  Release 更新成功：$GITLAB_URL/$PROJECT_PATH/-/releases/$VERSION" ;;
            *) echo "  Release 创建/更新失败：$RELEASE_BODY" >&2; echo "  文件已上传，可在 GitLab 页面手工创建 Release 并挂上下载链接。" >&2 ;;
        esac
        ;;
esac

echo
echo "下载地址：$ASSET_URL"
echo "校验文件：$ASSET_URL.sha256"
