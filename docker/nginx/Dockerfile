FROM node:20-alpine AS builder

# Vue CLI 4使用旧版Webpack；仅构建阶段启用OpenSSL兼容选项。
ENV NODE_OPTIONS=--openssl-legacy-provider

# deasync 等依赖在 arm64 上没有预编译产物，需要 node-gyp 现场编译，
# 因此构建阶段必须带 python3/make/g++（仅构建镜像，不进最终镜像）。
RUN apk add --no-cache python3 make g++

COPY ./web /build
WORKDIR /build

RUN npm --registry=https://registry.npmmirror.com install --legacy-peer-deps
RUN npm run build:prod

WORKDIR /src/main/resources
RUN ls

WORKDIR /src/main/resources/static
RUN ls

FROM nginx:1.27-alpine

ARG TZ=Asia/Shanghai


COPY --from=builder /src/main/resources/static /opt/dist

CMD ["nginx","-g","daemon off;"]
