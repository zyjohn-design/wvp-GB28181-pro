#!/usr/bin/env sh
set -eu

# 构建"完整离线部署包"（含全部镜像），用于 GitLab Releases 分发与现场一键部署。
# 用法：./build-offline-package.sh [arm64|amd64]      版本号取当前 git tag（可用 VERSION=... 覆盖）

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd)
REPOSITORY_ROOT=$(CDPATH= cd -- "$SCRIPT_DIR/../.." && pwd)
TEMPLATE_DIR="$SCRIPT_DIR/update-package-templates/offline"
RELEASES_DIR="$SCRIPT_DIR/releases"
TARGET_ARCH=${1:-arm64}
FORCE_REBUILD=${FORCE_REBUILD:-0}

case "$TARGET_ARCH" in
    amd64|arm64) ;;
    *)
        echo "错误：目标架构只支持 amd64 或 arm64。" >&2
        echo "用法：$0 [amd64|arm64]" >&2
        exit 1
        ;;
esac

for cmd in docker git tar; do
    command -v "$cmd" >/dev/null 2>&1 || { echo "错误：未找到 ${cmd}。" >&2; exit 1; }
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

# 判断某个架构名是否等于目标架构（amd64/aarch64 与 Docker 的 x86_64/arm64 写法并存）。
arch_matches() {
    case "${TARGET_ARCH}:$1" in
        amd64:amd64|amd64:x86_64) return 0 ;;
        arm64:arm64|arm64:aarch64) return 0 ;;
        *) return 1 ;;
    esac
}

# 注意：不要用 docker image inspect 来判定"本地有没有目标平台的内容"。
# containerd 镜像存储下，同一个标签可以同时挂着多平台内容，inspect 回报的是
# 宿主机能用的那个平台（在 arm64 构建机上拉 amd64 内容，它照样报 arm64），
# 据此断言会误判。唯一可靠的判定是 docker save --platform：
# 本地缺少目标平台内容时它直接报错，不会静默导出别的架构。
#
# 收集 OCI 归档里出现过的全部平台架构。
# containerd 导出的索引可能是嵌套的（顶层 index.json 指向子索引，子索引才带 platform），
# 因此必须逐层解开，否则读不到真实平台，也就拦不住混入的 amd64。
# 索引条目若没有 platform 字段（本地 docker build 出来的镜像常常如此），
# 回退去读该镜像清单指向的 config blob —— 里面的 architecture 才是真实值。
# 找不到 index.json（传统 docker 格式）时返回 1。
archive_arches() {
    archive=$1
    dir=$(mktemp -d "${TMPDIR:-/tmp}/wvp-arches.XXXXXX")

    if ! tar -xzOf "$archive" index.json > "$dir/q0.json" 2>/dev/null; then
        rm -rf "$dir"
        return 1
    fi

    : > "$dir/out"
    : > "$dir/manifests"
    level=0
    while [ -f "$dir/q${level}.json" ] && [ "$level" -lt 8 ]; do
        json="$dir/q${level}.json"

        # unknown 是 attestation 清单的伪平台（只带元数据、不含可执行内容），不计入真实架构。
        grep -Eo '"architecture"[[:space:]]*:[[:space:]]*"[a-zA-Z0-9_]+"' "$json" 2>/dev/null \
            | sed -e 's/.*"\([a-zA-Z0-9_]*\)"$/\1/' \
            | grep -v '^unknown$' >> "$dir/out" || true

        tr '{' '\n' < "$json" \
            | grep 'application/vnd\.oci\.image\.index\.v1+json' \
            | grep -Eo '"digest"[[:space:]]*:[[:space:]]*"sha256:[0-9a-f]+"' \
            | sed -e 's/.*"sha256:\([0-9a-f]*\)".*/\1/' > "$dir/digests" || true

        # 记录镜像清单摘要，供上面说的 config 回退使用。
        tr '{' '\n' < "$json" \
            | grep 'application/vnd\.oci\.image\.manifest\.v1+json' \
            | grep -Eo '"digest"[[:space:]]*:[[:space:]]*"sha256:[0-9a-f]+"' \
            | sed -e 's/.*"sha256:\([0-9a-f]*\)".*/\1/' >> "$dir/manifests" || true

        level=$((level + 1))
        : > "$dir/q${level}.json"
        while IFS= read -r digest; do
            [ -n "$digest" ] || continue
            tar -xzOf "$archive" "blobs/sha256/$digest" >> "$dir/q${level}.json" 2>/dev/null || true
        done < "$dir/digests"
        [ -s "$dir/q${level}.json" ] || rm -f "$dir/q${level}.json"
    done

    # 回退：索引没给 platform，就从镜像 config 里读真实架构。
    if [ ! -s "$dir/out" ] && [ -s "$dir/manifests" ]; then
        members=""
        while IFS= read -r digest; do
            [ -n "$digest" ] || continue
            members="$members blobs/sha256/$digest"
        done < "$dir/manifests"

        for pass in manifest config; do
            [ -n "$members" ] || break
            # shellcheck disable=SC2086
            tar -xzf "$archive" -C "$dir" $members 2>/dev/null || true

            next=""
            for member in $members; do
                [ -f "$dir/$member" ] || continue
                # 镜像清单与 config 都是格式化过的多行 JSON，而 grep 按行匹配；
                # 先把换行折成空格，"config" 与 "digest" 跨行时才能一次匹配到。
                flat=$(tr '\n' ' ' < "$dir/$member" 2>/dev/null || true)
                if [ "$pass" = "manifest" ]; then
                    # 镜像清单里的 config 摘要：{"config":{"mediaType":"...","digest":"sha256:..."}}
                    found=$(printf '%s' "$flat" \
                        | grep -Eo '"config"[[:space:]]*:[[:space:]]*\{[^{}]*"digest"[[:space:]]*:[[:space:]]*"sha256:[0-9a-f]+"' \
                        | sed -e 's/.*"sha256:\([0-9a-f]*\)".*/\1/' || true)
                    for digest in $found; do
                        next="$next blobs/sha256/$digest"
                    done
                else
                    printf '%s' "$flat" \
                        | grep -Eo '"architecture"[[:space:]]*:[[:space:]]*"[a-zA-Z0-9_]+"' \
                        | sed -e 's/.*"\([a-zA-Z0-9_]*\)"$/\1/' \
                        | grep -v '^unknown$' >> "$dir/out" || true
                fi
                rm -f "$dir/$member"
            done
            members=$next
        done
    fi

    LC_ALL=C sort -u "$dir/out"
    rm -rf "$dir"
    return 0
}

# 断言离线归档里只有目标架构。
# OCI 归档可以承载多平台内容，必须逐条核对；
# 传统 docker 格式（manifest.json）本身不支持多平台，无需检查。
assert_archive_arch() {
    archive=$1
    [ -s "$archive" ] || { echo "错误：离线镜像为空：${archive}" >&2; exit 1; }

    found=""
    if ! found=$(archive_arches "$archive"); then
        tar -xzOf "$archive" manifest.json >/dev/null 2>&1 || {
            echo "错误：无法识别离线镜像格式（缺少 index.json / manifest.json）：${archive}" >&2
            exit 1
        }
        return 0
    fi

    if [ -z "$found" ]; then
        echo "错误：离线归档 $(basename "$archive") 未声明任何平台架构，无法确认它只含 ${TARGET_ARCH}。" >&2
        echo "      通常是没有用 docker save --platform ${PLATFORM} 导出，多平台内容会被一并打包。" >&2
        exit 1
    fi

    for one in $found; do
        if ! arch_matches "$one"; then
            echo "错误：离线归档 $(basename "$archive") 含非目标架构「${one}」，目标为 ${TARGET_ARCH}。" >&2
            echo "      该包在 ${TARGET_ARCH} 机器上加载后会报 exec format error，已中止。" >&2
            exit 1
        fi
    done
}

echo "[1/6] 检查/构建业务镜像（${VERSION}-${TARGET_ARCH}）"
if [ "$FORCE_REBUILD" = 1 ] || ! docker image inspect "$WVP_IMAGE" >/dev/null 2>&1; then
    echo "  构建后端镜像 ${WVP_IMAGE}（Maven 构建，耗时较长）..."
    docker build --platform "$PLATFORM" -f "$REPOSITORY_ROOT/docker/wvp/Dockerfile" -t "$WVP_IMAGE" "$REPOSITORY_ROOT"
else
    echo "  已存在：$WVP_IMAGE"
fi

if [ "$FORCE_REBUILD" = 1 ] || ! docker image inspect "$WEB_IMAGE" >/dev/null 2>&1; then
    echo "  构建前端镜像 $WEB_IMAGE ..."
    docker build --platform "$PLATFORM" -f "$REPOSITORY_ROOT/docker/nginx/Dockerfile" -t "$WEB_IMAGE" "$REPOSITORY_ROOT"
else
    echo "  已存在：$WEB_IMAGE"
fi

if [ "$FORCE_REBUILD" = 1 ] || ! docker image inspect "$ADAPTER_IMAGE" >/dev/null 2>&1; then
    echo "  构建H5适配层镜像 $ADAPTER_IMAGE ..."
    docker build --platform "$PLATFORM" -f "$SCRIPT_DIR/adapter/Dockerfile" -t "$ADAPTER_IMAGE" "$SCRIPT_DIR/adapter"
else
    echo "  已存在：$ADAPTER_IMAGE"
fi

# 业务镜像的架构不在这里用 inspect 判断（原因见 arch_matches 上方的说明）：
# 导出时 docker save --platform 会拒绝不含目标平台内容的镜像，
# 导出后 assert_archive_arch 再逐条核对归档，两道关都过不了才会放行。

echo "[2/6] 拉取基础镜像（${PLATFORM}）"
# 用 here-doc 而不是管道，避免 while 体落在子 Shell 中。
while IFS= read -r image; do
    [ -n "$image" ] || continue
    echo "  - $image"
    docker pull --platform "$PLATFORM" "$image" >/dev/null
done <<EOF
$BASE_IMAGES
EOF

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
# 先落成未压缩 tar 再压缩：docker save 与 gzip 组成管道时，docker save 的失败状态
# 会被 gzip 的 0 掩盖，从而生成一个空/截断的压缩包而无人察觉。
save_image() {
    image=$1
    file=$2
    tmp_tar="$WORK_DIR/${file%.tar.gz}.tar"
    echo "  - $image → images/$file"
    # containerd 镜像存储下同一个标签可能同时保留多平台内容（例如本机已拉过 amd64），
    # 不指定 --platform 会把非目标架构一并打进离线包：目标机 docker load 后会选中
    # 错误架构，容器启动即 "exec format error"，且包体积翻倍。
    if ! docker save --platform "$PLATFORM" "$image" -o "$tmp_tar"; then
        rm -f "$tmp_tar"
        echo "错误：本地没有 ${image} 的 ${PLATFORM} 内容，导出失败。" >&2
        echo "      基础镜像请先执行：docker pull --platform ${PLATFORM} ${image}" >&2
        echo "      本地构建的业务镜像请用 --platform ${PLATFORM} 重新构建（docker rmi 后重跑本脚本）。" >&2
        exit 1
    fi
    gzip -1 < "$tmp_tar" > "$PKG/images/$file"
    rm -f "$tmp_tar"
    assert_archive_arch "$PKG/images/$file"
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
