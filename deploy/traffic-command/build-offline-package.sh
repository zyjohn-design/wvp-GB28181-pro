#!/usr/bin/env sh
set -eu

# 构建"完整离线部署包"（含全部镜像），用于 GitLab Releases 分发与现场一键部署。
# 用法：./build-offline-package.sh [arm64|amd64]      版本号取当前 git tag（可用 VERSION=... 覆盖）

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPOSITORY_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
TEMPLATE_DIR="$SCRIPT_DIR/update-package-templates/offline"
RELEASES_DIR="$SCRIPT_DIR/releases"
TARGET_ARCH=${1:-arm64}

case "$TARGET_ARCH" in
    amd64|arm64) ;;
    *)
        echo "错误：目标架构只支持 amd64 或 arm64。" >&2
        echo "用法：$0 [amd64|arm64]" >&2
        exit 1
        ;;
esac

for cmd in docker git tar; do
    command -v "$cmd" >/dev/null 2>&1 || { echo "错误：未找到 $cmd。" >&2; exit 1; }
done
if ! command -v sha256sum >/dev/null 2>&1 && ! command -v shasum >/dev/null 2>&1; then
    echo "错误：未找到 sha256sum 或 shasum。" >&2
    exit 1
fi

GIT_COMMIT=$(git -C "$REPOSITORY_ROOT" rev-parse HEAD)
VERSION=${VERSION:-$(git -C "$REPOSITORY_ROOT" describe --tags --abbrev=0 2>/dev/null || true)}
if [ -z "$VERSION" ]; then
    echo "错误：未找到 git tag，请先打版本号或显式指定 VERSION=..." >&2
    exit 1
fi

PLATFORM="linux/$TARGET_ARCH"
WVP_IMAGE="local/gb28181-wvp:${VERSION}-${TARGET_ARCH}"
WEB_IMAGE="local/gb28181-wvp-web:${VERSION}-${TARGET_ARCH}"
ADAPTER_IMAGE="local/gb28181-h5-adapter:1.0.0-${TARGET_ARCH}"
BASE_IMAGES="mysql:8.4
redis:7.4-alpine
zlmediakit/zlmediakit:master
nginx:1.27-alpine"

PACKAGE_NAME="wvp-offline-${TARGET_ARCH}-${VERSION}"
PACKAGE_DIR="$RELEASES_DIR/$PACKAGE_NAME"
ARCHIVE_PATH="$RELEASES_DIR/$PACKAGE_NAME.tar.gz"
WORK_DIR=$(mktemp -d "${TMPDIR:-/tmp}/wvp-offline.XXXXXX")

cleanup() {
    rm -rf "$WORK_DIR"
}
trap cleanup EXIT INT TERM

if [ -e "$PACKAGE_DIR" ] || [ -e "$ARCHIVE_PATH" ]; then
    echo "错误：发布包已存在：${PACKAGE_NAME}；请先移走旧包后再生成。" >&2
    exit 1
fi

hash_file() {
    if command -v sha256sum >/dev/null 2>&1; then
        sha256sum "$1" | awk '{print $1}'
    else
        shasum -a 256 "$1" | awk '{print $1}'
    fi
}

echo "[1/6] 检查/构建业务镜像（$VERSION-$TARGET_ARCH）"
if ! docker image inspect "$WVP_IMAGE" >/dev/null 2>&1; then
    echo "  构建后端镜像 $WVP_IMAGE（Maven 构建，耗时较长）..."
    docker build --platform "$PLATFORM" -f "$REPOSITORY_ROOT/docker/wvp/Dockerfile" -t "$WVP_IMAGE" "$REPOSITORY_ROOT"
else
    echo "  已存在：$WVP_IMAGE"
fi

if ! docker image inspect "$WEB_IMAGE" >/dev/null 2>&1; then
    echo "  构建前端镜像 $WEB_IMAGE ..."
    docker build --platform "$PLATFORM" -f "$REPOSITORY_ROOT/docker/nginx/Dockerfile" -t "$WEB_IMAGE" "$REPOSITORY_ROOT"
else
    echo "  已存在：$WEB_IMAGE"
fi

if ! docker image inspect "$ADAPTER_IMAGE" >/dev/null 2>&1; then
    echo "  构建H5适配层镜像 $ADAPTER_IMAGE ..."
    docker build --platform "$PLATFORM" -f "$SCRIPT_DIR/adapter/Dockerfile" -t "$ADAPTER_IMAGE" "$SCRIPT_DIR/adapter"
else
    echo "  已存在：$ADAPTER_IMAGE"
fi

echo "[2/6] 拉取基础镜像（$PLATFORM）"
printf '%s\n' "$BASE_IMAGES" | while IFS= read -r image; do
    [ -n "$image" ] || continue
    echo "  - $image"
    docker pull --platform "$PLATFORM" "$image" >/dev/null
done

echo "[3/6] 组装部署目录..."
PKG="$WORK_DIR/$PACKAGE_NAME"
mkdir -p "$PKG/images"

# 编排文件：按目标架构打补丁（平台 + 本地镜像标签）
sed \
    -e "s|platform: linux/amd64|platform: ${PLATFORM}|g" \
    -e "s|local/gb28181-wvp:fb45787-amd64|${WVP_IMAGE}|g" \
    -e "s|local/gb28181-wvp-web:fb45787-amd64|${WEB_IMAGE}|g" \
    -e "s|local/gb28181-h5-adapter:1.0.0-amd64|${ADAPTER_IMAGE}|g" \
    "$SCRIPT_DIR/compose.yaml" > "$PKG/compose.yaml"

# 镜像校验脚本：按目标架构替换标签
sed \
    -e "s|local/gb28181-wvp:fb45787-amd64|${WVP_IMAGE}|g" \
    -e "s|local/gb28181-wvp-web:fb45787-amd64|${WEB_IMAGE}|g" \
    -e "s|local/gb28181-h5-adapter:1.0.0-amd64|${ADAPTER_IMAGE}|g" \
    "$SCRIPT_DIR/verify-images.sh" > "$PKG/verify-images.sh"

for f in compose.sh start.sh stop.sh status.sh restart-adapter.sh preflight.sh \
         load-images.sh export-video-channels.sh .env.example; do
    if [ -f "$SCRIPT_DIR/$f" ]; then
        cp "$SCRIPT_DIR/$f" "$PKG/$f"
    fi
done

cp -R "$SCRIPT_DIR/config" "$PKG/config"
cp -R "$SCRIPT_DIR/adapter" "$PKG/adapter"
cp -R "$SCRIPT_DIR/database" "$PKG/database"
find "$PKG" -name '.DS_Store' -delete 2>/dev/null || true
find "$PKG/adapter" -name 'node_modules' -prune -exec rm -rf {} + 2>/dev/null || true

cp "$TEMPLATE_DIR/env.preset" "$PKG/.env"
cp "$TEMPLATE_DIR/verify.sh" "$PKG/verify.sh"
chmod +x "$PKG"/*.sh

echo "[4/6] 导出离线镜像（体积较大，请耐心等待）..."
save_image() {
    image=$1
    file=$2
    echo "  - $image → images/$file"
    docker save "$image" | gzip -1 > "$PKG/images/$file"
}
save_image mysql:8.4 mysql-8.4.tar.gz
save_image redis:7.4-alpine redis-7.4-alpine.tar.gz
save_image zlmediakit/zlmediakit:master zlmediakit-master.tar.gz
save_image nginx:1.27-alpine nginx-1.27-alpine.tar.gz
save_image "$WVP_IMAGE" "wvp-${VERSION}-${TARGET_ARCH}.tar.gz"
save_image "$WEB_IMAGE" "wvp-web-${VERSION}-${TARGET_ARCH}.tar.gz"
save_image "$ADAPTER_IMAGE" "h5-adapter-1.0.0-${TARGET_ARCH}.tar.gz"

( cd "$PKG/images" && find . -type f ! -name SHA256SUMS -print | LC_ALL=C sort | while IFS= read -r file; do
    printf '%s  %s\n' "$(hash_file "$file")" "${file#./}"
done > SHA256SUMS )

echo "[5/6] 生成说明与校验文件..."
render_template() {
    input=$1
    output=$2
    sed \
        -e "s|__PACKAGE_NAME__|$PACKAGE_NAME|g" \
        -e "s|__VERSION__|$VERSION|g" \
        -e "s|__TARGET_ARCH__|$TARGET_ARCH|g" \
        -e "s|__GIT_COMMIT__|$GIT_COMMIT|g" \
        -e "s|__WVP_IMAGE__|$WVP_IMAGE|g" \
        -e "s|__WEB_IMAGE__|$WEB_IMAGE|g" \
        -e "s|__ADAPTER_IMAGE__|$ADAPTER_IMAGE|g" \
        "$input" > "$output"
}
render_template "$TEMPLATE_DIR/README.md.in" "$PKG/README.md"
render_template "$TEMPLATE_DIR/RELEASE_INFO.txt.in" "$PKG/RELEASE_INFO.txt"

( cd "$PKG" && find . -type f ! -name SHA256SUMS -print | LC_ALL=C sort | while IFS= read -r file; do
    printf '%s  %s\n' "$(hash_file "$file")" "${file#./}"
done > SHA256SUMS )

echo "[6/6] 打包..."
mkdir -p "$RELEASES_DIR"
mv "$PKG" "$PACKAGE_DIR"
tar -C "$RELEASES_DIR" -cf - "$PACKAGE_NAME" | gzip -1 > "$ARCHIVE_PATH"
printf '%s  %s\n' "$(hash_file "$ARCHIVE_PATH")" "$(basename "$ARCHIVE_PATH")" > "$ARCHIVE_PATH.sha256"

echo "离线部署包生成完成："
echo "  $ARCHIVE_PATH"
echo "  $ARCHIVE_PATH.sha256"
echo "  镜像：$(ls "$PACKAGE_DIR/images" | grep -c 'tar.gz') 个"
echo "  大小：$(du -sh "$ARCHIVE_PATH" | cut -f1)"
