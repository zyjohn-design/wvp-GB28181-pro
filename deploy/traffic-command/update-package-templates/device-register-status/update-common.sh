#!/usr/bin/env sh

# Sourced by upgrade/rollback. Read resolved service images rather than fixed tags.
prepare_deployment() {
    if ! command -v docker >/dev/null 2>&1; then
        echo '错误：未找到 docker。' >&2
        exit 1
    fi
    if [ ! -f "$DEPLOY_DIR/compose.yaml" ] || [ ! -x "$DEPLOY_DIR/compose.sh" ] || [ ! -f "$DEPLOY_DIR/.env" ]; then
        echo "错误：$DEPLOY_DIR 必须包含 compose.yaml、可执行的 compose.sh 和现场 .env。" >&2
        exit 1
    fi
    DEPLOY_DIR=$(CDPATH= cd -- "$DEPLOY_DIR" && pwd)
    machine_arch=$(uname -m)
    case "$TARGET_ARCH:$machine_arch" in
        amd64:x86_64|amd64:amd64|arm64:aarch64|arm64:arm64) ;;
        *) echo "错误：服务器架构 $machine_arch 与更新包 $TARGET_ARCH 不匹配。" >&2; exit 1 ;;
    esac
    cd "$DEPLOY_DIR"
    COMPOSE_CONFIG=$(./compose.sh config)
    BACKEND_CURRENT=$(service_image polaris-wvp)
    WEB_CURRENT=$(service_image polaris-wvp-web)
    if [ -z "$BACKEND_CURRENT" ] || [ -z "$WEB_CURRENT" ] || [ "$BACKEND_CURRENT" = "$WEB_CURRENT" ]; then
        echo '错误：无法从 Compose 解析两个独立的 WVP 前后端服务镜像。' >&2
        exit 1
    fi
    for image in "$BACKEND_CURRENT" "$WEB_CURRENT"; do
        case "$image" in
            *@*) echo "错误：镜像 $image 使用 digest 引用，不能通过重打标签更新。" >&2; exit 1 ;;
        esac
    done
    STATE_DIR="$DEPLOY_DIR/.wvp-updates/${RELEASE_ID}-${TARGET_ARCH}"
}

service_image() {
    printf '%s\n' "$COMPOSE_CONFIG" | awk -v wanted="$1" '
        /^services:/ { in_services = 1; next }
        in_services && /^[^[:space:]]/ { in_services = 0 }
        in_services && /^  [^[:space:]]/ {
            service = $1; sub(/:$/, "", service)
        }
        in_services && service == wanted && /^    image:/ {
            sub(/^    image:[[:space:]]*/, "")
            sub(/[[:space:]]*$/, "")
            quote = substr($0, 1, 1)
            if ((quote == "\"" || quote == sprintf("%c", 39)) && substr($0, length($0), 1) == quote) {
                print substr($0, 2, length($0) - 2)
            } else { print }
        }
    '
}

validate_image() {
    image_platform=$(docker image inspect -f '{{.Os}}/{{.Architecture}}' "$1") || {
        echo "错误：本地缺少镜像 $1；增量升级要求保留现场基础镜像。" >&2
        exit 1
    }
    if [ "$image_platform" != "linux/$TARGET_ARCH" ]; then
        echo "错误：镜像 $1 为 ${image_platform}，更新包要求 linux/${TARGET_ARCH}。" >&2
        exit 1
    fi
}

check_saved_references() {
    if [ ! -f "$STATE_DIR/backend-image" ] || [ ! -f "$STATE_DIR/web-image" ] || \
       [ "$(cat "$STATE_DIR/backend-image")" != "$BACKEND_CURRENT" ] || \
       [ "$(cat "$STATE_DIR/web-image")" != "$WEB_CURRENT" ]; then
        echo "错误：Compose 镜像与 $STATE_DIR 中的升级记录不一致，请检查现场配置。" >&2
        exit 1
    fi
}

# Only replace the known project template. Never overwrite a site's custom proxy rules.
check_nginx_template() {
    NGINX_TARGET="$DEPLOY_DIR/config/nginx/wvp-web.conf.template"
    [ -f "$SCRIPT_DIR/config/wvp-web.conf.template" ] || return 0
    if [ ! -f "$NGINX_TARGET" ] || \
       { ! cmp -s "$NGINX_TARGET" "$SCRIPT_DIR/config/nginx-before-map-cache.conf.template" && \
         ! cmp -s "$NGINX_TARGET" "$SCRIPT_DIR/config/wvp-web.conf.template"; }; then
        printf '%s\n' "错误：现场 Nginx 模板缺失或经过定制。请维护人员核对包内 config/wvp-web.conf.template 与 ${NGINX_TARGET}，适配升级脚本后再升级。本次未更新镜像。" >&2
        exit 1
    fi
}

install_nginx_template() {
    [ -f "$SCRIPT_DIR/config/wvp-web.conf.template" ] || return 0
    if [ ! -f "$STATE_DIR/nginx-before.conf.template" ]; then
        cp -p "$NGINX_TARGET" "$STATE_DIR/nginx-before.conf.template"
    fi
    cp "$SCRIPT_DIR/config/wvp-web.conf.template" "$NGINX_TARGET"
}

restore_nginx_template() {
    [ -f "$STATE_DIR/nginx-before.conf.template" ] || return 0
    cp "$STATE_DIR/nginx-before.conf.template" "$DEPLOY_DIR/config/nginx/wvp-web.conf.template"
}
