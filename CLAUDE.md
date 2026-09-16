# CLAUDE.md

本项目的编码规范、目录结构与禁止事项统一维护在 [AGENT.md](./AGENT.md)，请先完整阅读该文件再动手。
这里只放最常用的速查内容，两份文件如有冲突以 AGENT.md 为准。

## 速查

```bash
# 后端（需 JDK 21）
mvn compile
mvn test
mvn test -Dtest=XxxTest
mvn package -P jar

# 前端（web/ 目录）
npm run dev                 # 9528，代理 http://127.0.0.1:18080
npm run lint
npx vue-cli-service build   # 产物 -> src/main/resources/static（gitignore）
```

## 最容易踩的五条

1. 改表要同时更新 `数据库/<版本>/` 下 mysql、postgresql-kingbase、达梦 的初始化与更新脚本；
   达梦布尔值用 `1/0`，Mapper 加 `databaseId = "dm"` 变体。仅展示用的易失状态优先存 Redis。
2. 不要用 `Thread.startVirtualThread` / 虚拟线程执行器 / `parallelStream`，注入 `TaskExecutor`；
   缓存消息的队列必须有界并用 `offer` + `drainTo`。
3. 注册流程里第一个 `401` 是正常认证流程（要携带 Digest 再注册），第二个 `401` 才是认证失败。
4. Controller 返回业务对象即可，由 `GlobalResponseAdvice` 包装；失败抛 `ControllerException`；
   新增写操作型 GET 接口要同步维护 `WebSecurityConfig.GET_ACTION_SEGMENTS`。
5. 前端固定 `api/` → `store/modules/` → `views/` 三层；`request.js` 在 `code !== 0` 时抛出的是字符串。

## 协作要求

- 改完必须跑 `mvn test`（动前端再跑 `npm run lint` 与前端构建），把结果贴给用户。
- 按功能拆分提交，只 `git add` 相关文件；仓库里常有他人未提交的改动。
- 未获明确许可不要推送 `master`，不要执行 `--force`、`reset --hard` 等破坏性命令。
- 不要提交发布产物与密钥：`deploy/traffic-command/releases/`、`*.tar.gz`、`.env`、
  `application-*.yml`、`src/main/resources/static/`。
