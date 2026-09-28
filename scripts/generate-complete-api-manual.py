#!/usr/bin/env python3
"""在唯一对外手册中生成逐路由文档及完整模型目录。

运行：python3 scripts/generate-complete-api-manual.py
人工编写的 1-11 节不变；只重建 GENERATED_API_REFERENCE 标记之间的内容。
"""

from __future__ import annotations

import importlib.util
import json
import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANUAL = ROOT / "doc/第三方对接接口手册.md"
START = "<!-- GENERATED_API_REFERENCE_START -->"
END = "<!-- GENERATED_API_REFERENCE_END -->"
SPEC = importlib.util.spec_from_file_location("api_reference", ROOT / "scripts/generate-api-reference.py")
assert SPEC and SPEC.loader
api_reference = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(api_reference)

PUBLIC_EXCLUDES = (
    "/api/user/login", "/index/hook/", "/api/device/query/snap/",
    "/api/alarm/snap/", "/api/jt1078/playback/download",
    "/api/jt1078/snap", "/api/emit",
)
RAW_PREFIXES = ("/api/v1/", "/index/hook/", "/api/video-")
ADMIN_PREFIXES = ("/api/role/", "/api/userApiKey/", "/api/user/", "/api/log/", "/api/test/")
MAX_EXAMPLE_FIELDS = 4
INITIAL_VERSION_DATE = "2026-09-28"
SECURITY_SOURCE = (ROOT / "src/main/java/com/genersoft/iot/vmp/conf/security/WebSecurityConfig.java").read_text()
ACTION_MATCH = re.search(r"GET_ACTION_SEGMENTS\s*=\s*Set\.of\((.*?)\);", SECURITY_SOURCE, re.DOTALL)
GET_ACTION_SEGMENTS = set(re.findall(r'"([^"]+)"', ACTION_MATCH.group(1))) if ACTION_MATCH else set()


def escape(value: object) -> str:
    return str(value).replace("|", "\\|").replace("\n", " ").strip()


def json_type(java_type: str) -> str:
    typ = java_type.strip().removesuffix("[]")
    if java_type.endswith("[]") or typ.startswith(("List<", "Set<", "Collection<", "Page<")):
        return "array"
    if typ in {"int", "long", "short", "byte", "Integer", "Long", "Short", "Byte", "BigInteger"}:
        return "integer"
    if typ in {"double", "float", "Double", "Float", "BigDecimal"}:
        return "number"
    if typ in {"boolean", "Boolean"}:
        return "boolean"
    if typ in {"String", "char", "Character", "Instant", "LocalDateTime", "Date", "UUID"}:
        return "string"
    if typ in {"void", "Void"}:
        return "null"
    if typ in {"byte[]", "InputStream", "Resource"}:
        return "binary"
    return "object"


def unwrap(java_type: str) -> str:
    typ = java_type.strip()
    while True:
        hit = re.fullmatch(r"(?:DeferredResult|ResponseEntity|WVPResult)<(.+)>", typ)
        if not hit:
            break
        typ = hit.group(1).strip()
    return typ


def model_name(java_type: str) -> str:
    return re.sub(r"<.*", "", java_type).strip()


def model_for(name: str, endpoint_path: Path, registry: dict[str, list[tuple]]) -> tuple | None:
    candidates = registry.get(name, [])
    if not candidates:
        return None
    if len(candidates) == 1:
        return candidates[0]
    # RecordInfo 等同名类存在于多个模块；优先当前 Controller 所在模块。
    return max(candidates, key=lambda model: len(set(endpoint_path.parts) & set(model[2].parts)))


def all_fields(model: tuple, registry: dict[str, list[tuple]], endpoint_path: Path, seen: set[str] | None = None) -> list[tuple[str, str, str]]:
    name, _, _, parent, fields = model
    seen = set() if seen is None else seen
    if name in seen:
        return list(fields)
    seen.add(name)
    inherited = []
    parent_model = model_for(parent, endpoint_path, registry) if parent else None
    if parent_model:
        inherited = all_fields(parent_model, registry, endpoint_path, seen)
    result = {field: (field, typ, desc) for field, typ, desc in inherited}
    result.update({field: (field, typ, desc) for field, typ, desc in fields})
    return list(result.values())


def example_value(name: str, typ: str):
    if name in {"page", "pageNum"}:
        return 1
    if name in {"count", "pageSize"}:
        return 20
    if name in {"presetId", "id", "gbId", "channelId", "mediaServerId"} and json_type(typ) == "integer":
        return 1
    if name in {"startTime", "endTime"}:
        return "2026-09-28 08:00:00" if name == "startTime" else "2026-09-28 09:00:00"
    if name == "password":
        return "<32位MD5或该接口规定的密码>"
    kind = json_type(typ)
    if kind == "integer":
        return 1
    if kind == "number":
        return 1.0
    if kind == "boolean":
        return True
    if kind == "array":
        return []
    if kind == "object":
        return {}
    return f"<{name}>"


def sample_data(java_type: str, registry: dict[str, list[tuple]], endpoint_path: Path):
    typ = unwrap(java_type)
    if typ in {"void", "Void"}:
        return None
    page = re.fullmatch(r"PageInfo<(.+)>", typ)
    if page:
        return {"list": [sample_data(page.group(1), registry, endpoint_path)], "total": 1,
                "pageNum": 1, "pageSize": 20, "pages": 1, "size": 1,
                "hasNextPage": False, "hasPreviousPage": False}
    collection = re.fullmatch(r"(?:List|Set|Collection)<(.+)>", typ)
    if collection:
        return [sample_data(collection.group(1), registry, endpoint_path)]
    model = model_for(model_name(typ), endpoint_path, registry)
    if model:
        return {field: example_value(field, field_type)
                for field, field_type, _ in all_fields(model, registry, endpoint_path)[:MAX_EXAMPLE_FIELDS]}
    return example_value("value", typ)


def body_example(model: tuple | None, registry: dict[str, list[tuple]], endpoint_path: Path, route: str, typ: str):
    if not model:
        return [] if json_type(typ) == "array" else {}
    fields = all_fields(model, registry, endpoint_path)
    if route.startswith("/api/push/"):
        preferred = ["id", "app", "stream", "gbDeviceId", "gbName"] if "/update" in route else ["app", "stream", "gbDeviceId", "gbName"]
    elif route.startswith("/api/proxy/"):
        preferred = ["id", "type", "app", "stream", "srcUrl"] if "/update" in route else ["type", "app", "stream", "srcUrl"]
    else:
        preferred = []
    by_name = {name: (name, java_type) for name, java_type, _ in fields}
    selected = [by_name[name] for name in preferred if name in by_name]
    if not selected:
        selected = [(name, java_type) for name, java_type, _ in fields[:MAX_EXAMPLE_FIELDS]]
    return {name: example_value(name, java_type) for name, java_type in selected}


def response_fields(java_type: str, endpoint_path: Path, registry: dict[str, list[tuple]], wrapped: bool) -> list[tuple[str, str, str]]:
    prefix = "data" if wrapped else "response"
    rows = [("code", "integer", "业务状态码：0 成功，非 0 失败"),
            ("msg", "string", "结果说明；本系统实际字段为 msg"),
            ("data", "object / array / primitive / null", "业务结果，依接口返回类型变化")] if wrapped else []
    typ = unwrap(java_type)
    if typ in {"void", "Void"}:
        if wrapped:
            rows[-1] = ("data", "null", "无业务返回值")
        return rows
    page = re.fullmatch(r"PageInfo<(.+)>", typ)
    collection = re.fullmatch(r"(?:List|Set|Collection)<(.+)>", typ)
    if page:
        rows += [(f"{prefix}.list", "array", f"本页 {page.group(1)} 列表"),
                 (f"{prefix}.total", "integer", "总记录数"),
                 (f"{prefix}.pageNum", "integer", "当前页码，从 1 开始"),
                 (f"{prefix}.pageSize", "integer", "每页数量"),
                 (f"{prefix}.pages", "integer", "总页数"),
                 (f"{prefix}.size", "integer", "本页条数"),
                 (f"{prefix}.hasNextPage", "boolean", "是否还有下一页"),
                 (f"{prefix}.hasPreviousPage", "boolean", "是否有上一页")]
        typ, prefix = page.group(1), f"{prefix}.list[]"
    elif collection:
        rows.append((prefix, "array", f"{collection.group(1)} 数组"))
        typ, prefix = collection.group(1), f"{prefix}[]"
    model = model_for(model_name(typ), endpoint_path, registry)
    if model:
        rows.extend((f"{prefix}.{field}", json_type(field_type), f"{description}（Java: {field_type}）")
                    for field, field_type, description in all_fields(model, registry, endpoint_path))
    elif not page and not collection:
        rows.append((prefix, json_type(typ), f"源码返回类型：{typ}"))
    return rows


def is_public(route: str) -> bool:
    return any(route == p or (p.endswith("/") and route.startswith(p)) for p in PUBLIC_EXCLUDES)


def is_binary(route: str, return_type: str, parameters: list[tuple]) -> bool:
    if "byte[]" in return_type or "Resource" in return_type:
        return True
    if return_type == "void" and any(name == "response" or typ == "HttpServletResponse" for name, typ, *_ in parameters):
        return True
    return (route in {"/api/cloud/record/download/zip", "/api/cloud/record/zip",
                      "/api/jt1078/playback/download", "/api/jt1078/snap", "/api/sy/record/zip"}
            or route.startswith(("/api/device/query/snap/", "/api/alarm/snap/", "/api/log/file/")))


def auth_text(route: str, verb: str) -> str:
    if is_public(route):
        return "默认放行；媒体 Hook 另按媒体服务密钥校验" if route.startswith("/index/hook/") else "默认放行"
    if route in {"/api/user/changePassword", "/api/user/logout", "/api/user/userInfo"}:
        return "access-token 或 api-key；已登录"
    if route.startswith(ADMIN_PREFIXES) or route in {"/api/server/shutdown", "/api/server/config", "/api/server/system/configInfo"}:
        return "access-token 或 api-key；ADMIN"
    if verb in {"POST", "PUT", "PATCH", "DELETE"}:
        return "access-token 或 api-key；ADMIN / OPERATOR"
    if verb == "GET" and (route in {"/api/device/control/record", "/api/channel/playback"}
                          or any(segment.lower() in GET_ACTION_SEGMENTS for segment in route.split("/"))):
        return "access-token 或 api-key；ADMIN / OPERATOR"
    return "access-token 或 api-key；已登录"


def content_type(verb: str, parameters: list[tuple]) -> str:
    places = {place for _, _, place, _, _ in parameters}
    if "multipart" in places:
        return "multipart/form-data"
    if "JSON body" in places:
        return "application/json"
    if verb in {"POST", "PUT", "PATCH"} and "query/form" in places:
        return "application/x-www-form-urlencoded（也可按接口绑定方式使用 query）"
    return "无请求体，无须 Content-Type"


def parameter_tables(parameters: list[tuple], endpoint_path: Path, registry: dict[str, list[tuple]], verb: str) -> list[str]:
    lines: list[str] = []
    for heading, places in (("Path 参数", {"path"}), ("Query 参数", {"query/form"}),
                            ("Body 参数", {"JSON body", "multipart"})):
        entries = [p for p in parameters if p[2] in places]
        lines.extend([f"#### {heading}", "", "| 参数名 | 类型 | 必填 | 说明 | 示例 |", "|---|---|---|---|---|"])
        if not entries:
            lines.append("| — | — | — | 无 | — |")
        for name, typ, place, required, description in entries:
            if place == "JSON body":
                model = model_for(model_name(typ), endpoint_path, registry)
                example = "[]" if json_type(typ) == "array" else "{...}"
                lines.append(f"| `{name}` | {json_type(typ)} | {required} | {description}；模型 `{typ}` | `{example}` |")
                if model:
                    for field, field_type, field_desc in all_fields(model, registry, endpoint_path):
                        example = json.dumps(example_value(field, field_type), ensure_ascii=False)
                        lines.append(f"| `{name}.{field}` | {json_type(field_type)} | 未声明 | {field_desc} | `{example}` |")
            else:
                ex = json.dumps(example_value(name, typ), ensure_ascii=False)
                lines.append(f"| `{name}` | {json_type(typ)} | {required} | {description} | `{ex}` |")
        lines.append("")
        if heading == "Query 参数" and entries and verb in {"POST", "PUT", "PATCH"}:
            lines += ["POST/PUT/PATCH 且未使用 JSON body 时，表中参数由表单或查询串绑定；示例采用表单。", ""]
    return lines


def request_example(verb: str, route: str, parameters: list[tuple], registry: dict[str, list[tuple]], path: Path, public: bool) -> str:
    sample_route = re.sub(r"\{([^}]+)}", lambda m: f"<{m.group(1)}>", route)
    query = []
    form = []
    for name, typ, place, required, _ in parameters:
        if place != "query/form" or required == "否":
            continue
        value = example_value(name, typ)
        value = str(value).lower() if isinstance(value, bool) else str(value)
        (query if verb == "GET" else form).append(f"{name}={value.replace(' ', '%20')}")
    if query:
        sample_route += "?" + "&".join(query)
    lines = [f"{verb if verb != 'ANY' else 'GET（RequestMapping 未限定方法）'} {sample_route} HTTP/1.1",
             "Host: <wvp-host>"]
    if not public:
        lines.append("access-token: <JWT>")
    for name, typ, place, _, _ in parameters:
        if place == "header" and name not in {"access-token", "api-key"}:
            lines.append(f"{name}: <{name}>")
    body = next((p for p in parameters if p[2] == "JSON body"), None)
    multipart = any(p[2] == "multipart" for p in parameters)
    if body:
        name, typ, *_ = body
        model = model_for(model_name(typ), path, registry)
        data = body_example(model, registry, path, route, typ)
        lines += ["Content-Type: application/json", "", json.dumps(data, ensure_ascii=False, indent=2)]
    elif multipart:
        lines += ["Content-Type: multipart/form-data; boundary=<boundary>", "", "<按字段上传 multipart 表单>"]
    elif form:
        lines += ["Content-Type: application/x-www-form-urlencoded", "", "&".join(form)]
    return "\n".join(lines)


def render_route(number: int, title: str, cls: str, path: Path, verb: str, route: str,
                 summary: str, return_type: str, parameters: list[tuple], registry: dict[str, list[tuple]]) -> list[str]:
    public = is_public(route)
    binary = is_binary(route, return_type, parameters)
    raw = route.startswith(RAW_PREFIXES) or binary
    label = summary or route
    condition = "；需启用对应配置" if "需启用配置" in title else ""
    if "隐藏或内部接口" in title:
        condition += "；内部/回调接口，不建议第三方直接调用"
    lines = [f"### {number}. {label}", "", "#### 基本信息", "",
             "| 项 | 值 |", "|---|---|",
             f"| 接口名称 | {escape(label)} |",
             f"| 请求方法 | `{verb}` |",
             f"| 请求路径 | `{route}` |",
             f"| 认证方式 | {auth_text(route, verb)}{condition} |",
             f"| Content-Type | {content_type(verb, parameters)} |",
             "| 负责人 | 仓库未指定；由对应模块维护者确认 |",
             f"| 源码位置 | `{path.relative_to(ROOT)}` / `{cls}` |",
             f"| 源码返回类型 | `{return_type}` |", "",
             "#### 请求头", "", "| 参数名 | 类型 | 必填 | 说明 | 示例 |", "|---|---|---|---|---|"]
    if public:
        lines.append("| `access-token` / `api-key` | string | 否 | 默认放行；媒体 Hook 的独立鉴权由媒体服务配置 | — |")
    else:
        lines.append("| `access-token` 或 `api-key` | string | 是 | JWT 或服务间 API Key，二选一；权限依基本信息 | `<JWT>` |")
    for name, typ, place, required, description in parameters:
        if place == "header":
            lines.append(f"| `{name}` | {json_type(typ)} | {required} | {description} | `{json.dumps(example_value(name, typ), ensure_ascii=False)}` |")
    ctype = content_type(verb, parameters)
    if not ctype.startswith("无请求体"):
        lines.append(f"| `Content-Type` | string | 是 | 请求体编码 | `{ctype.split('（')[0]}` |")
    lines.append("")
    lines += parameter_tables(parameters, path, registry, verb)
    lines += ["#### 请求示例", "", "示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。", "",
              "```http", request_example(verb, route, parameters, registry, path, public), "```", "",
              "#### 响应参数", "", "| 参数名 | 类型 | 说明 |", "|---|---|---|"]
    if binary:
        lines.append("| 响应体 | binary / stream | 控制器直接写出文件或媒体内容；依 Content-Type 保存 |")
    elif raw and unwrap(return_type) in {"void", "Void"}:
        lines.append("| 响应体 | empty | 成功时无业务响应体 |")
    else:
        rows = response_fields(return_type, path, registry, not raw)
        lines += [f"| `{field}` | {typ} | {escape(desc)} |" for field, typ, desc in rows]
        if raw:
            lines.append("| — | — | 此路由不经过统一响应包装，按对应媒体或兼容协议解析 |")
    lines += ["", "#### 成功响应", ""]
    if binary:
        lines += ["```http", "HTTP/1.1 200 OK", "Content-Type: <以实际响应头为准>", "", "<二进制内容>", "```", ""]
    elif raw and unwrap(return_type) in {"void", "Void"}:
        lines += ["```http", "HTTP/1.1 200 OK", "", "<空响应体>", "```", ""]
    else:
        data = sample_data(return_type, registry, path)
        payload = data if raw else {"code": 0, "msg": "成功", "data": data}
        lines += ["```json", json.dumps(payload, ensure_ascii=False, indent=2), "```", ""]
    lines += ["#### 错误响应", ""]
    if raw:
        lines += ["```http", "HTTP/1.1 4xx/5xx", "Content-Type: <以实际响应头为准>", "", "<格式由该兼容协议或媒体服务决定>", "```", ""]
    else:
        lines += ["```json", json.dumps({"code": 100, "msg": "失败：请检查具体错误说明", "data": None}, ensure_ascii=False, indent=2), "```", ""]
    lines += ["#### 错误码", "", "| code | msg | 说明 |", "|---|---|---|"]
    if raw:
        lines += ["| HTTP 2xx | — | 成功；业务体格式依接口协议 |",
                  "| HTTP 4xx | — | 参数、鉴权或资源错误，按响应体处理 |",
                  "| HTTP 5xx | — | 服务端或媒体服务异常 |"]
    else:
        lines += ["| 0 | 成功 | 业务成功 |", "| 100 | 失败 | 通用业务失败，读取实际 `msg` |",
                  "| 400 | 参数或方法错误 | 参数不合法或调用方式错误 |",
                  "| 401 | 请登录后重新请求 | 身份失效或缺失 |",
                  "| 403 | 无权限操作 | 当前身份权限不足 |",
                  "| 404 | 资源未找到 | 设备、通道或记录不存在 |",
                  "| 408 / 486 | 超时或无响应 | 等待设备或媒体服务超时 |",
                  "| 500 | 系统异常 | 服务器内部异常；也可能返回 HTTP 500 |"]
        if "/preset/" in route and any(segment in route for segment in ("/add", "/call", "/delete")):
            lines.append("| 100 | 预置位编号必须为1-255之间的整数 | `presetId` 不在合法范围或不是纯数字 |")
    lines += ["", "#### 变更记录", "", "| 版本 | 日期 | 说明 | 作者 |", "|---|---|---|---|",
              f"| v1.0 | {INITIAL_VERSION_DATE} | 根据当前源码首次收录；具体业务规则以源码和部署配置为准 | 项目维护者（未指定个人） |", "", "---", ""]
    return lines


def main() -> None:
    original = MANUAL.read_text(encoding="utf-8")
    assert original.count(START) == original.count(END) == 1, "找不到唯一的生成区域标记"
    groups = api_reference.parse_controllers()
    model_types: set[str] = set()
    for _, _, _, endpoints in groups:
        for _, _, result_type, parameters in endpoints:
            model_types.update(re.findall(r"[A-Za-z_][A-Za-z_0-9]*", result_type))
            model_types.update(typ for _, typ, place, _, _ in parameters if place == "JSON body")
    while True:
        models = api_reference.parse_models(model_types)
        nested_types = set(model_types)
        for _, _, _, _, fields in models:
            for _, field_type, _ in fields:
                nested_types.update(re.findall(r"[A-Za-z_][A-Za-z_0-9]*", field_type))
        if nested_types == model_types:
            break
        model_types = nested_types
    registry: dict[str, list[tuple]] = defaultdict(list)
    for model in models:
        registry[model[0]].append(model)
    lines = ["", "## 12. 全系统 HTTP 接口逐项文档", "",
             "本节依据当前仓库全部 Controller 路由生成，包含业务接口、管理接口、可选模块、内部 Hook 和兼容接口。每条按统一模板列出基本信息、请求头、Path/Query/Body、示例、响应字段、错误码和变更记录。示例数据用于展示 JSON 形状，不代表存在该设备或记录；没有源码注释的字段标为“未声明”。错误码表列出平台通用码，单个接口只会返回适用的子集。`msg` 是本系统真实字段名，不能按示例中的 `message` 解析。文件、流以及 `/api/v1/**`、`/index/hook/**` 等排除统一包装的路由在条目中单独注明。", "",
             "负责人姓名未记录在源码中，统一标为未指定；不要将模板里的示例人名当作本项目负责人。权限根据 `WebSecurityConfig` 默认规则列示，部署方可调整放行列表。", ""]
    lines += ["| 模块 | 路由数 | 快速跳转 |", "|---|---:|---|"]
    for title, cls, _, endpoints in groups:
        route_count = sum(len(mappings) for mappings, _, _, _ in endpoints)
        lines.append(f"| {escape(title)} | {route_count} | [查看](#api-{cls}) |")
    lines.append("")
    count = 0
    for title, cls, path, endpoints in groups:
        lines += [f'<a id="api-{cls}"></a>', "", f"## {title}（{cls}）", ""]
        for mappings, summary, return_type, parameters in endpoints:
            for verb, route in mappings:
                count += 1
                lines += render_route(count, title, cls, path, verb, route, summary, return_type, parameters, registry)
    lines += ["## 13. 完整模型字段目录", "",
              "本节收录 Controller 请求体、返回值及其嵌套字段所涉及的可识别模型，以及源码带 `@Schema` 的模型。表中“Java 类型”保留源码原型，JSON 类型用于外部解码；继承字段也列出。接口返回的多态接口、动态 `JSONObject` 和标准库对象没有固定字段，以具体协议或运行响应为准。部分类型是内部对象，第三方只应依赖相应接口实际返回的字段。", ""]
    for name, title, path, parent, _ in models:
        model = (name, title, path, parent, next(m[4] for m in registry[name] if m[2] == path))
        lines += [f"### {name}" + (f"：{title}" if title else ""), "",
                  f"源码：`{path.relative_to(ROOT)}`" + (f"；继承 `{parent}`" if parent else ""), "",
                  "| 字段 | JSON 类型 | Java 类型 | 说明 |", "|---|---|---|---|"]
        fields = all_fields(model, registry, path)
        lines += [f"| `{field}` | {json_type(typ)} | `{typ}` | {desc} |" for field, typ, desc in fields]
        lines.append("")
    lines += [f"逐项收录 **{count} 条路由映射、{len(groups)} 个 Controller、{len(models)} 个模型**。", ""]
    updated = original.split(START)[0] + START + "\n" + "\n".join(lines) + END + original.split(END)[1]
    MANUAL.write_text(updated, encoding="utf-8")
    print(f"已更新 {MANUAL.relative_to(ROOT)}：{count} 条路由，{len(models)} 个模型")


if __name__ == "__main__":
    main()
