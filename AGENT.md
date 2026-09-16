# AGENT.md

面向 AI 编码助手的项目指南。人类同样适用，但内容偏向“改代码前必须知道的约束”。
本文件是权威版本，`CLAUDE.md` 指向本文件。

## 1. 项目是什么

WVP（web video platform）：开箱即用的 GB28181-2016 / 部标 JT808 / JT1078 视频平台，
负责信令与设备管理，流媒体由 ZLMediaKit 承担，前端为内置的管理后台。

- 后端：Java 21 + Spring Boot 3.4.4 + MyBatis（注解式 Mapper）+ Redis
- 前端：Vue 2 + Element UI + vue-cli（`web/`），构建产物输出到 `src/main/resources/static`
- 数据库：MySQL / PostgreSQL / 金仓 KingBase / 达梦 / H2（多方言，改表必须同时兼容）
- 部署：`docker/`（通用）与 `deploy/traffic-command/`（指挥调度离线部署套件）

## 2. 常用命令

后端（需要 JDK 21，Maven 默认 JDK 若是 8 会编译失败）：

```bash
mvn compile                 # 编译
mvn test                    # 跑单元测试（surefire 已开启，不再 skipTests）
mvn test -Dtest=XxxTest     # 跑单个测试类
mvn package -P jar          # 打 jar（另有 -P war）
```

前端（`web/` 目录）：

```bash
npm run dev                 # 开发服务，默认 9528，代理到 http://127.0.0.1:18080
npm run lint                # eslint（提交前必须 0 error）
npx vue-cli-service build   # 构建，产物写入 ../src/main/resources/static（已被 gitignore）
npm run test:unit           # jest
```

本地起后端：`application.yml` 里 `spring.profiles.active` 指向的 `application-<profile>.yml`
被 gitignore 忽略，需要自行创建（可参考 `application-dev.yml` 与 `配置详情.yml`）。

## 3. 代码结构

```
src/main/java/com/genersoft/iot/vmp/
├── gb28181/        国标核心：bean / dao / service / transmit(SIP 收发) / task(状态任务) / controller
├── jt1078/         部标 808/1078
├── media/          流媒体节点（zlm、abl）对接与管理
├── streamProxy/    拉流代理     streamPush/ 推流
├── service/        通用业务 + redisMsg（集群消息与 redis-rpc 控制器）
├── conf/           配置、security（鉴权）、redis、异步线程池
├── vmanager/       对外 REST 接口（部分模块的 controller 在各自包内）
├── storager/       dao 与 redis 缓存
└── web/            自定义对接（custom）与外部 API
web/                前端工程
数据库/<版本>/       各方言 SQL：初始化-xxx.sql 与 更新-xxx.sql
deploy/ docker/     部署资产
```

## 4. 必须遵守的约定

### 数据库改动

- 每个方言都要改：`数据库/<当前版本>/初始化-mysql-*.sql`、`初始化-postgresql-kingbase-*.sql`、
  `初始化-达梦-*.sql`，以及对应的 `更新-*.sql`（存量用户靠它升级）。
- 达梦不支持 `true/false`，Mapper 里用 `@Select(value = "... enable=1 ...", databaseId = "dm")`
  追加方言专用语句（参考 `PlatformMapper`）。
- 仅用于展示的临时/易失状态，优先存 Redis（key 前缀 `VMP_*`，见 `PlatformRegisterResultManager`），
  避免为一个字段让所有用户执行升级脚本。

### 线程与队列

- 不要写 `Thread.startVirtualThread(...)`、`Executors.newVirtualThreadPerTaskExecutor()`、
  `parallelStream()`。注入 `org.springframework.core.task.TaskExecutor`（见 `conf/AsyncConfig`）。
- 所有缓存待处理消息的队列必须有界（`LinkedBlockingQueue<>(N)`），入队用 `offer` 并在满时打日志，
  批量取数用 `drainTo`。无界队列在高并发上报下会 OOM。
- `@EnableAsync(proxyTargetClass = true)`：部分 Service 实现接口又声明接口外的 `@Scheduled` 方法，
  改成 JDK 代理会导致定时任务失效、启动报错。

### SIP / 国标

- 发送统一走 `SIPSender.transmitRequest(...)`；回调订阅以 `callId + CSeq` 为 key（`SipSubscribe`），
  超时通过延时队列触发 errorEvent（statusCode `-1024`）。
- `SIPProcessorObserver` 把 `401` 归入“成功”分支：注册的 401 属于正常认证流程，需要携带 Digest 再注册，
  第二次 401 才是认证失败。新增注册相关逻辑时不要把首个 401 当失败。
- 上级平台注册/心跳状态由 `gb28181/task/platformStatus/` 管理；最近一次注册结果由
  `PlatformRegisterResultManager` 存 Redis，页面直接展示，不要只写日志。

### 集群

- 每个节点有 `userSetting.serverId`；跨节点调用走 redis-rpc（`conf/redis/RedisRpcConfig` +
  `service/redisMsg/control/*`）。响应约定：`fromId` = 本机，`toId` = 请求方；带 `toId` 的请求只由目标节点处理。
- 超时参数注意单位，`userSetting.getPlayTimeout()` 是毫秒。

### 接口与返回

- Controller 直接返回业务对象，`GlobalResponseAdvice` 统一包成 `WVPResult{code,msg,data}`；
  失败抛 `ControllerException(ErrorCode...)` 或用 `Assert`。
- 接口鉴权在 `conf/security/WebSecurityConfig`：写操作需要 `ADMIN`/`OPERATOR`，
  用户/角色/日志/系统配置类接口仅 `ADMIN`；以动作命名的 GET（`add`、`delete`、`control`、`ptz` 等，
  白名单 `GET_ACTION_SEGMENTS`）也按写操作校验。新增“GET 触发写操作”的接口要同步维护该名单。
- 新增接口需要 `@Operation` / `@Parameter` 注解（Swagger 文档）。

### 前端

- 调用链固定为：`web/src/api/<模块>.js` → `web/src/store/modules/<模块>.js`（namespaced action）
  → `views/`，不要在页面里直接 import api。
- `utils/request.js` 拦截器在 `code !== 0` 时 `throw res.msg`，所以 `.catch(error)` 拿到的是字符串。
- 使用 `dangerouslyUseHTMLString` 时必须转义插值内容（参考 `views/platform/index.vue` 的 `escapeHtml`）。
- 日志、提示、注释用中文，与现有风格一致。

### 日志

`log.info("[模块名] ...")` 形式，模块名用中文方括号前缀，如 `[国标级联]`、`[redis-rpc]`、`[平台离线]`。

## 5. 测试

- JUnit 5 + Mockito（`spring-boot-starter-test`）。现有测试都是纯单元测试，不启动 Spring 上下文、
  不依赖 DB/Redis；新增测试请保持这一点（用 mock + `ReflectionTestUtils` 注入字段）。
- 改动 SIP、集群、鉴权这类难以手工验证的逻辑时，补单元测试覆盖关键分支
  （例：`PlatformRegisterTesterTest` 覆盖 401 → Digest → 最终响应）。
- 提交前至少跑：`mvn test` 与 `npm run lint`；动了前端页面再跑一次前端构建。

## 6. 版本控制

- 提交信息：`类型(范围): 中文摘要`（`feat` / `fix` / `perf` / `chore`），正文说明“为什么”和影响面。
- 按功能拆分提交，只 `git add` 相关文件；仓库常有其他未提交的改动，不要一把 `git add .`。
- 不要提交（已在 `.gitignore`）：
  `src/main/resources/application-*.yml`、`src/main/resources/static/`（前端产物）、
  `*.tar`/`*.tar.gz`/`*.war`、`deploy/traffic-command/.env`、`deploy/traffic-command/data|logs`、
  `deploy/traffic-command/images/*.tar*`、`deploy/traffic-command/releases/`（发布/增量升级包与校验文件）、
  `.DS_Store`、`logs/`、`target/`。
- 未获明确许可不要推送 `master`/`main`，不要 `--force`、`reset --hard` 等破坏性操作。

## 7. 安全

- 不要在日志或接口响应里输出上级平台密码、`sip.password`、JWT 密钥等敏感值；引用时只提字段名。
- 新增对外暴露的接口默认要走鉴权；确实需要放行时在 `defaultExcludes` 里显式声明并说明原因。
- 上传/回调地址、设备返回内容都属于外部输入，拼接命令、SQL、HTML 前先校验或转义。
