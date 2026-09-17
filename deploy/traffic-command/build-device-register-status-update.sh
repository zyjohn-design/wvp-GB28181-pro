#!/usr/bin/env sh
set -eu

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPOSITORY_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
TEMPLATE_DIR="$SCRIPT_DIR/update-package-templates/device-register-status"
RELEASES_DIR="$SCRIPT_DIR/releases"
TARGET_ARCH=${1:-amd64}

case "$TARGET_ARCH" in
    amd64|arm64) ;;
    *)
        echo "错误：目标架构只支持 amd64 或 arm64。" >&2
        echo "用法：$0 [amd64|arm64]" >&2
        exit 1
        ;;
esac

BASE_TAG="fb45787-$TARGET_ARCH"

if ! command -v npm >/dev/null 2>&1 || ! command -v npx >/dev/null 2>&1; then
    echo "错误：未找到 npm/npx，无法构建前端。" >&2
    exit 1
fi
if ! command -v mvn >/dev/null 2>&1; then
    echo "错误：未找到 Maven，无法构建后端。" >&2
    exit 1
fi
if ! command -v sha256sum >/dev/null 2>&1 && ! command -v shasum >/dev/null 2>&1; then
    echo "错误：未找到 sha256sum 或 shasum，无法生成校验文件。" >&2
    exit 1
fi

JAVA_COMMAND=java
if [ -n "${JAVA_HOME:-}" ] && [ -x "$JAVA_HOME/bin/java" ]; then
    JAVA_COMMAND="$JAVA_HOME/bin/java"
fi
JAVA_VERSION=$($JAVA_COMMAND -version 2>&1 | sed -n '1p')
case "$JAVA_VERSION" in
    *'"21.'*) ;;
    *)
        echo "错误：生成升级包需要 Java 21，当前为：$JAVA_VERSION" >&2
        exit 1
        ;;
esac

GIT_SHORT=$(git -C "$REPOSITORY_ROOT" rev-parse --short HEAD)
GIT_COMMIT=$(git -C "$REPOSITORY_ROOT" rev-parse HEAD)
RELEASE_DATE=$(date +%Y%m%d)
RELEASE_ID=${RELEASE_ID:-"${RELEASE_DATE}-${GIT_SHORT}"}
PACKAGE_NAME="wvp-device-register-status-${RELEASE_ID}-${TARGET_ARCH}"
PACKAGE_DIR="$RELEASES_DIR/$PACKAGE_NAME"
ARCHIVE_PATH="$RELEASES_DIR/$PACKAGE_NAME.tar.gz"
WORK_DIR=$(mktemp -d "${TMPDIR:-/tmp}/wvp-update.XXXXXX")

cleanup() {
    rm -rf "$WORK_DIR"
}
trap cleanup EXIT INT TERM

if [ -e "$PACKAGE_DIR" ] || [ -e "$ARCHIVE_PATH" ]; then
    echo "错误：发布包已存在：${PACKAGE_NAME}；请先移走旧包后再生成。" >&2
    exit 1
fi

echo "[1/5] 构建前端静态资源..."
(cd "$REPOSITORY_ROOT/web" && npx --no-install vue-cli-service build)

echo "[2/5] 使用 Java 21 构建后端 JAR..."
(cd "$REPOSITORY_ROOT" && mvn -q -DskipTests clean package -P jar)

JAR_PATH=$(find "$REPOSITORY_ROOT/target" -maxdepth 1 -type f -name 'wvp-pro-*.jar' -print | head -n 1)
if [ -z "$JAR_PATH" ] || [ ! -f "$JAR_PATH" ]; then
    echo "错误：未找到后端构建产物。" >&2
    exit 1
fi
if [ ! -f "$REPOSITORY_ROOT/src/main/resources/static/index.html" ]; then
    echo "错误：未找到前端构建产物 index.html。" >&2
    exit 1
fi

echo "[3/5] 组装离线差分包..."
mkdir -p "$WORK_DIR/$PACKAGE_NAME/artifacts/web-static" "$WORK_DIR/$PACKAGE_NAME/docker"
cp "$JAR_PATH" "$WORK_DIR/$PACKAGE_NAME/artifacts/wvp.jar"
cp -R "$REPOSITORY_ROOT/src/main/resources/static/." "$WORK_DIR/$PACKAGE_NAME/artifacts/web-static/"
cp "$TEMPLATE_DIR/backend.Dockerfile" "$WORK_DIR/$PACKAGE_NAME/docker/backend.Dockerfile"
cp "$TEMPLATE_DIR/web.Dockerfile" "$WORK_DIR/$PACKAGE_NAME/docker/web.Dockerfile"

BACKEND_CURRENT="local/gb28181-wvp:$BASE_TAG"
WEB_CURRENT="local/gb28181-wvp-web:$BASE_TAG"
BACKEND_RELEASE="local/gb28181-wvp:${RELEASE_ID}-${TARGET_ARCH}"
WEB_RELEASE="local/gb28181-wvp-web:${RELEASE_ID}-${TARGET_ARCH}"
BACKEND_BACKUP="local/gb28181-wvp:pre-${RELEASE_ID}-${TARGET_ARCH}"
WEB_BACKUP="local/gb28181-wvp-web:pre-${RELEASE_ID}-${TARGET_ARCH}"

render_template() {
    input=$1
    output=$2
    sed \
        -e "s|__PACKAGE_NAME__|$PACKAGE_NAME|g" \
        -e "s|__RELEASE_ID__|$RELEASE_ID|g" \
        -e "s|__TARGET_ARCH__|$TARGET_ARCH|g" \
        -e "s|__GIT_COMMIT__|$GIT_COMMIT|g" \
        -e "s|__BACKEND_CURRENT__|$BACKEND_CURRENT|g" \
        -e "s|__WEB_CURRENT__|$WEB_CURRENT|g" \
        -e "s|__BACKEND_RELEASE__|$BACKEND_RELEASE|g" \
        -e "s|__WEB_RELEASE__|$WEB_RELEASE|g" \
        -e "s|__BACKEND_BACKUP__|$BACKEND_BACKUP|g" \
        -e "s|__WEB_BACKUP__|$WEB_BACKUP|g" \
        "$input" > "$output"
}

render_template "$TEMPLATE_DIR/upgrade.sh.in" "$WORK_DIR/$PACKAGE_NAME/upgrade.sh"
render_template "$TEMPLATE_DIR/rollback.sh.in" "$WORK_DIR/$PACKAGE_NAME/rollback.sh"
render_template "$TEMPLATE_DIR/README.md.in" "$WORK_DIR/$PACKAGE_NAME/README.md"
render_template "$TEMPLATE_DIR/RELEASE_INFO.txt.in" "$WORK_DIR/$PACKAGE_NAME/RELEASE_INFO.txt"
cp "$TEMPLATE_DIR/verify.sh" "$WORK_DIR/$PACKAGE_NAME/verify.sh"
chmod +x "$WORK_DIR/$PACKAGE_NAME/upgrade.sh" "$WORK_DIR/$PACKAGE_NAME/rollback.sh" "$WORK_DIR/$PACKAGE_NAME/verify.sh"

hash_file() {
    if command -v sha256sum >/dev/null 2>&1; then
        sha256sum "$1" | awk '{print $1}'
    else
        shasum -a 256 "$1" | awk '{print $1}'
    fi
}

echo "[4/5] 生成文件校验清单..."
CHECKSUM_FILE="$WORK_DIR/$PACKAGE_NAME/SHA256SUMS"
find "$WORK_DIR/$PACKAGE_NAME" -type f ! -name SHA256SUMS -print | LC_ALL=C sort | while IFS= read -r file; do
    relative=${file#"$WORK_DIR/$PACKAGE_NAME/"}
    printf '%s  %s\n' "$(hash_file "$file")" "$relative"
done > "$CHECKSUM_FILE"

echo "[5/5] 生成压缩包..."
mkdir -p "$RELEASES_DIR"
mv "$WORK_DIR/$PACKAGE_NAME" "$PACKAGE_DIR"
tar -C "$RELEASES_DIR" -czf "$ARCHIVE_PATH" "$PACKAGE_NAME"
printf '%s  %s\n' "$(hash_file "$ARCHIVE_PATH")" "$(basename "$ARCHIVE_PATH")" > "$ARCHIVE_PATH.sha256"

echo "增量更新包生成完成："
echo "  $ARCHIVE_PATH"
echo "  $ARCHIVE_PATH.sha256"
echo "服务器解压后执行：./upgrade.sh /opt/gb28181-h5"
