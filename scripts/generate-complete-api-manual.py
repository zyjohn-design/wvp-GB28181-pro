#!/usr/bin/env python3
"""生成轻量入口、逐接口文档、模型文档和机器可读路由索引。

运行：python3 scripts/generate-complete-api-manual.py
人工维护 doc/api/接入指南.md、AI对接指南.md；其他输出按源码重建。
"""

from __future__ import annotations

import importlib.util
import hashlib
import json
import re
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
MANUAL = ROOT / "doc/第三方对接接口手册.md"
OUT = ROOT / "doc/api"
SPEC = importlib.util.spec_from_file_location("api_reference", ROOT / "scripts/generate-api-reference.py")
assert SPEC and SPEC.loader
api_reference = importlib.util.module_from_spec(SPEC)
SPEC.loader.exec_module(api_reference)

PUBLIC_EXCLUDES = (
    "/api/server/health/ready",
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
    return re.sub(r"\\*\|", r"\\|", str(value)).replace("\n", " ").strip()


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
    if name == "presetId":
        return "1" if json_type(typ) == "string" else 1
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
        return example_value("body", typ)
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


REQUEST_HEADER = "| 字段 | 位置 | 类型 | 必填 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |"
REQUEST_DIVIDER = "|---|---|---|---|---|---|---|---|"
RESPONSE_HEADER = "| 字段 | 类型 | 可空 / 出现条件 | 格式 / 取值 | 说明 | 示例 |"
RESPONSE_DIVIDER = "|---|---|---|---|---|---|"


def field_rules(name: str, typ: str, description: str, route: str = "", place: str = "") -> tuple[str, str]:
    """Only publish source-declared constraints; do not infer limits from examples."""
    leaf = name.rsplit(".", 1)[-1].replace("[]", "")
    kind = typ if typ in {"string", "integer", "number", "boolean", "array", "object", "null"} else json_type(typ)
    rules = []
    if kind == "boolean": rules.append("true / false")
    elif kind == "integer": rules.append("整数")
    elif kind == "number": rules.append("数值")
    elif kind == "array": rules.append("JSON数组；元素类型见说明")
    elif kind == "object": rules.append("JSON对象；字段见模型表")
    elif kind == "null": rules.append("JSON null")
    if "yyyy-MM-dd HH:mm:ss" in description: rules.append("yyyy-MM-dd HH:mm:ss")
    for match in re.findall(r"(?<![A-Za-z0-9])-?\d+(?:\.\d+)?\s*[–—~～-]\s*\d+(?:\.\d+)?", description):
        bounds = re.fullmatch(r"(-?\d+(?:\.\d+)?)\s*[–—~～-]\s*(-?\d+(?:\.\d+)?)", match)
        if bounds and float(bounds.group(1)) < float(bounds.group(2)): rules.append(match)
    for clause in re.split(r"[；;。]", description):
        if any(word in clause for word in ("允许", "取值", "可选值", "长度", "最多", "不能超过", "正则", "下限", "上限", "格式")):
            rules.append(clause.strip())
    if not rules: rules.append("源码未声明格式或范围")
    default = "源码未声明默认值"
    defaults = re.findall(r"(?:不传默认|默认(?:值)?[为是：: ]*)\s*([^；;，,。]+)", description)
    if defaults: default = defaults[-1].strip()
    if "Java初始值：" in description:
        default = description.split("Java初始值：", 1)[1].split("；", 1)[0] + "（Java初始值；请求默认值依接口）"
    if preset_route(route):
        if leaf in {"presetId", "preset"}:
            rules = ["1–255整数；0号预留"]
            if leaf == "presetId": rules.append("兼容PresetID_1至PresetID_255")
            if place == "response": rules = ["字符串；已知PresetID_N规范化，其他编号保留展示；控制前校验"]
        if leaf in {"presetName", "name"} and place not in {"response", "model"}:
            rules = ["去除首尾空白；最多128个UTF-16编码单元"]
            default = "未传/空白：预置点+编号"
        if leaf == "command" and route == "/api/v1/control/preset": rules = ["set / goto / remove"]
    return "；".join(dict.fromkeys(rules)), default


def request_row(name: str, typ: str, place: str, required: str, desc: str, route: str = "") -> str:
    location = {"path": "Path", "query/form": "Query / Form", "JSON body": "JSON Body", "multipart": "Multipart", "header": "Header"}.get(place, place)
    rules, default = field_rules(name, typ, desc, route, place)
    if name == "Content-Type": rules = desc.removeprefix("请求体编码：")
    if name in {"access-token", "api-key"}:
        rules = "有效JWT" if name == "access-token" else "已启用的API Key"
        default = "默认放行" if required == "否" else "未传时须提供另一种凭证"
    if default == "源码未声明默认值" and required == "是": default = "无；必须提供"
    if required == "未声明" and place == "query/form" and "Java初始值：" not in desc and default != "源码未声明默认值":
        required = "否（有声明的默认值）"
    if required == "未声明": required = "源码未声明；见业务校验"
    sample = desc.removeprefix("请求体编码：").split("（", 1)[0] if name == "Content-Type" else example_value(name.rsplit(".", 1)[-1], typ)
    example = json.dumps(sample, ensure_ascii=False)
    values = [f"`{name}`", location, json_type(typ), required, rules, default, desc, f"`{example}`"]
    return "| " + " | ".join(escape(value) for value in values) + " |"


def response_row(field: str, typ: str, desc: str, route: str = "") -> str:
    rules, _ = field_rules(field, typ, desc, route, "response")
    nullable = "源码未声明可空约束；按实际响应处理"
    example = "按接口示例"
    if field == "code": nullable, rules, example = "统一响应时提供", "0成功；非0失败", "0"
    elif field == "msg": nullable, example = "统一WVPResult中为非空说明", '"成功"'
    elif typ == "null": nullable, example = "固定null", "null"
    elif "null" in typ: nullable = "允许null；具体含义见说明"
    elif field.endswith("presetName") and preset_route(route): nullable, example = "预置位存在时返回最终名称", '"东门入口"'
    elif field.endswith("presetId"): example = '"1"'
    values = [f"`{field}`", typ, nullable, rules, desc, f"`{example}`"]
    return "| " + " | ".join(escape(value) for value in values) + " |"


def model_row(field: str, typ: str, desc: str) -> str:
    rules, default = field_rules(field, typ, desc, place="model")
    example = json.dumps(example_value(field, typ), ensure_ascii=False)
    values = [f"`{field}`", json_type(typ), f"`{typ}`", "由具体接口决定", rules, default, desc, f"`{example}`"]
    return "| " + " | ".join(escape(value) for value in values) + " |"


def parameter_tables(parameters: list[tuple], endpoint_path: Path, registry: dict[str, list[tuple]], verb: str, route: str = "") -> list[str]:
    lines: list[str] = []
    for heading, places in (("Path 参数", {"path"}), ("Query 参数", {"query/form"}),
                            ("Body 参数", {"JSON body", "multipart"})):
        entries = [p for p in parameters if p[2] in places]
        lines.extend([f"#### {heading}", "", REQUEST_HEADER, REQUEST_DIVIDER])
        if not entries: lines.append("| — | — | — | — | — | — | 无字段 | — |")
        for name, typ, place, required, description in entries:
            field_name = "$body" if place == "JSON body" else name
            if place == "JSON body":
                description += f"；整个JSON请求体，Java形参名{name}不是额外的JSON包装字段"
            lines.append(request_row(field_name, typ, place, required, description, route))
            if place == "JSON body":
                model = model_for(model_name(typ), endpoint_path, registry)
                if model:
                    for field, field_type, field_desc in all_fields(model, registry, endpoint_path):
                        lines.append(request_row(f"$body.{field}", field_type, place, "未声明", field_desc, route))
        lines.append("")
        if heading == "Query 参数" and entries and verb in {"POST", "PUT", "PATCH"}:
            lines += ["表中 Query / Form 字段由表单或查询串绑定；示例采用表单，不能改成 JSON body。", ""]
        if heading == "Body 参数" and any(p[2] == "JSON body" for p in entries):
            lines += ["`$body`表示整个JSON值，`$body.name`表示根对象的name字段；不要发送名为$body或Java形参名的额外包装对象。", ""]
    return lines


def request_example(verb: str, route: str, parameters: list[tuple], registry: dict[str, list[tuple]], path: Path, public: bool) -> str:
    sample_route = re.sub(r"\{([^}]+)}", lambda m: f"<{m.group(1)}>", route)
    query = []
    form = []
    for name, typ, place, required, _ in parameters:
        if place != "query/form" or required == "否":
            continue
        value = "goto" if route == "/api/v1/control/preset" and name == "command" else example_value(name, typ)
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


def preset_route(route: str) -> bool:
    return route.startswith(("/api/front-end/preset/", "/api/common/channel/front-end/preset/")) or route == "/api/v1/control/preset"


def preset_parameters(route: str, parameters: list[tuple]) -> list[tuple]:
    """Business validation is stricter than RequestParam(required=false) at these routes."""
    if not preset_route(route):
        return parameters
    result = []
    for name, typ, place, required, desc in parameters:
        if name == "channelId" and place == "query/form" and route != "/api/v1/control/preset":
            required, desc = "是", "通道数据库整数ID（CommonGBChannel.gbId），不是通道国标编号"
        elif name == "presetId":
            required, desc = "是", "预置位编号1–255；兼容已知PresetID_N格式；0号预留"
        elif name == "presetName" or (route == "/api/v1/control/preset" and name == "name"):
            required, desc = "否", "平台名称备注，最多128个UTF-16编码单元；空白或未传默认预置点+编号；不下发设备"
        elif route == "/api/v1/control/preset":
            if name in {"serial", "code", "command", "preset"}:
                required = "是"
            desc = {"serial": "设备国标编号", "code": "目标通道国标编号，不能用channel代替",
                    "command": "set设置、goto调用、remove删除", "preset": "预置位编号1–255，0号预留",
                    "channel": "保留兼容参数；当前没有通道序号映射，请传code"}.get(name, desc)
        result.append((name, typ, place, required, desc))
    return result


def render_route(number: int, title: str, cls: str, path: Path, verb: str, route: str,
                 summary: str, return_type: str, parameters: list[tuple], registry: dict[str, list[tuple]]) -> list[str]:
    parameters = preset_parameters(route, parameters)
    preset = preset_route(route)
    device_presets = route == "/api/front-end/preset/query/{deviceId}"
    remark = preset and "/remark" in route
    control = preset and (route == "/api/v1/control/preset" or any("/" + action in route for action in ("add", "call", "delete")))
    public = is_public(route)
    binary = is_binary(route, return_type, parameters)
    readiness = route == "/api/server/health/ready"
    raw = readiness or (route.startswith(RAW_PREFIXES) and "WVPResult<" not in return_type) or binary
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
             "#### 请求头", "", REQUEST_HEADER, REQUEST_DIVIDER]
    for credential, description in (("access-token", "有效JWT；与api-key二选一；权限见基本信息"),
                                    ("api-key", "已启用的API Key；与access-token二选一；权限见基本信息")):
        lines.append(request_row(credential, "String", "header", "否" if public else "二选一", description))
    for name, typ, place, required, description in parameters:
        if place == "header":
            lines.append(request_row(name, typ, "header", required, description, route))
    ctype = content_type(verb, parameters)
    if not ctype.startswith("无请求体"):
        lines.append(request_row("Content-Type", "String", "header", "是", "请求体编码：" + ctype, route))
    lines.append("")
    if preset:
        lines += ["#### 业务约定", "",
                  "参数、名称优先级、应答语义与完整示例见[第8.2节](#82-预置位)。名称备注只保存到平台，不进入国标控制指令。", ""]
        if device_presets:
            lines += ["按设备实时逐个查询全部视频通道，排除目录和预置位为空的通道；名称优先使用平台备注。整体timeout默认60秒，范围1–300，每个通道最多等待5秒。整体超时后不再发起后续查询。", "",
                      "只有code=0表示全量查询完成；全部成功且无预置位时data=[]。部分通道失败或未完成时code非0，data仅含已成功查到的非空通道，不可依据此部分结果删除已有预置位。", ""]
        if control:
            lines += ["收到设备 SIP 200 OK 后才返回 code=0；拒绝保留 SIP 状态码，SIP/跨节点应答超时返回408。设备已接收但备注保存失败返回100；不应重新设置位置，应通过修改名称接口补存。网络错误或超时后不自动重试。", ""]
        elif remark:
            lines += ["仅保存平台名称，不下发设备指令；成功data为最终名称，省略或留空名称恢复预置点+编号。", ""]
        else:
            lines += ["查询设备当前列表后合并名称：平台备注优先，否则设备名称，否则自动名称；备注不会产生设备查询中不存在的预置位。", ""]
    lines += parameter_tables(parameters, path, registry, verb, route)
    lines += ["#### 请求示例", "", "示例展示请求结构；尖括号值需替换，设备能力、业务必填规则与部署鉴权以实际环境为准。", "",
              "```http", request_example(verb, route, parameters, registry, path, public), "```", "",
              "#### 响应参数", "", RESPONSE_HEADER, RESPONSE_DIVIDER]
    if binary:
        lines.append(response_row("响应体", "binary / stream", "控制器直接写出文件或媒体内容；依Content-Type保存"))
    elif raw and unwrap(return_type) in {"void", "Void"}:
        lines.append(response_row("响应体", "empty", "成功时无业务响应体"))
    else:
        response_type = "List<Preset>" if preset and "/query" in route and not device_presets else return_type
        rows = response_fields(response_type, path, registry, not raw)
        if readiness:
            rows = [("status", "string", "UP表示已完成启动恢复；NOT_READY表示尚未就绪或正在关闭；HTTP状态分别为200和503")]
        if control:
            rows = [("code", "integer", "0表示设备已接收指令且平台备注操作完成；非0失败"),
                    ("msg", "string", "实际结果或失败原因"), ("data", "null", "无业务返回值")]
        elif remark:
            rows = [("code", "integer", "0表示平台名称备注已保存"), ("msg", "string", "结果说明"),
                    ("data", "string", "最终保存的名称，空白输入生成预置点+编号")]
        elif preset:
            rows = [(field, typ, "最终显示名称：平台备注→设备名称→自动名称" if field == "data[].presetName" else desc)
                    for field, typ, desc in rows]
        if device_presets:
            rows = [("code", "integer", "0表示全量完成；非0表示失败或结果不完整"),
                    ("msg", "string", "结果说明，部分失败时提示未完成数量"),
                    ("data", "array", "仅含有预置位的通道；无匹配为[]；失败时可能是部分结果或null"),
                    ("data[].channelId", "string", "通道国标编号"),
                    ("data[].channelName", "string", "通道名称"),
                    ("data[].presets", "array", "非空预置位列表"),
                    ("data[].presets[].presetId", "string", "预置位编号，控制前校验"),
                    ("data[].presets[].presetName", "string", "最终显示名称：平台备注→设备名称→自动名称")]
        rows = list({field: (field, typ, desc) for field, typ, desc in rows}.values())
        if not raw and not control and not remark:
            rows = [(field, json_type(unwrap(response_type)) if field == "data" and typ == "object / array / primitive / null" else typ, desc)
                    for field, typ, desc in rows]
        lines += [response_row(field, typ, desc, route) for field, typ, desc in rows]
        if raw:
            lines.append("| — | — | — | — | 此路由不经过统一响应包装，按对应媒体或兼容协议解析 | — |")
    lines += ["", "#### 成功响应", ""]
    if binary:
        lines += ["```http", "HTTP/1.1 200 OK", "Content-Type: <以实际响应头为准>", "", "<二进制内容>", "```", ""]
    elif raw and unwrap(return_type) in {"void", "Void"}:
        lines += ["```http", "HTTP/1.1 200 OK", "", "<空响应体>", "```", ""]
    else:
        data = sample_data(return_type, registry, path)
        if readiness:
            data = {"status": "UP"}
        payload = data if raw else {"code": 0, "msg": "成功", "data": data}
        if control:
            payload = {"code": 0, "msg": "设备已接收预置位指令", "data": None}
        elif remark:
            payload = {"code": 0, "msg": "成功", "data": "预置点1"}
        elif preset:
            payload = {"code": 0, "msg": "成功", "data": [{"presetId": "1", "presetName": "东门入口"}]}
        if device_presets:
            payload = {"code": 0, "msg": "成功", "data": [{"channelId": "34020000001320000001",
                       "channelName": "东门球机", "presets": [{"presetId": "1", "presetName": "入口"}]}]}
        lines += ["```json", json.dumps(payload, ensure_ascii=False, indent=2), "```", ""]
    lines += ["#### 错误响应", ""]
    if readiness:
        lines += ["```http", "HTTP/1.1 503 Service Unavailable", "Content-Type: application/json", "", '{"status":"NOT_READY"}', "```", ""]
    elif raw:
        lines += ["```http", "HTTP/1.1 4xx/5xx", "Content-Type: <以实际响应头为准>", "", "<格式由该兼容协议或媒体服务决定>", "```", ""]
    else:
        error = {"code": 100, "msg": "失败：请检查具体错误说明", "data": None}
        if device_presets:
            error = {"code": 100, "msg": "有 1 个通道查询失败或未完成，结果不完整，请勿作为全量同步依据", "data": payload["data"]}
        lines += ["```json", json.dumps(error, ensure_ascii=False, indent=2), "```", ""]
    lines += ["#### 错误码", "", "| code | msg | 说明 |", "|---|---|---|"]
    if readiness:
        lines += ["| HTTP 200 | UP | 全部启动恢复任务完成，可接收请求 |", "| HTTP 503 | NOT_READY | 尚未就绪或正在关闭；连接失败同样不能视为就绪 |"]
    elif raw:
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
            lines.append("| 100 | 预置位编号必须为1-255之间的整数 | `presetId` 无效；已知 PresetID_N 格式兼容支持 |")
    lines += ["", "#### 变更记录", "", "| 版本 | 日期 | 说明 | 作者 |", "|---|---|---|---|",
              f"| v1.0 | {'2026-10-22' if device_presets else '2026-10-21' if remark else INITIAL_VERSION_DATE} | 根据当前源码首次收录；具体业务规则以源码和部署配置为准 | 项目维护者（未指定个人） |"]
    if preset and not remark and not device_presets:
        lines.append("| v1.1 | 2026-10-21 | 同步平台名称备注、参数校验、SIP应答及响应示例 | 项目维护者（未指定个人） |")
    lines += ["", "---", ""]
    return lines


def stable_name(prefix: str, identity: str) -> str:
    """Stable links independent of route order; disambiguate same-name Java classes."""
    return prefix + "-" + hashlib.sha256(identity.encode()).hexdigest()[:12] + ".md"


def link_integration_guide(routes: list[dict]) -> str:
    """Link exact API references in prose; leave executable fenced examples intact."""
    guide = (OUT / "接入指南.md").read_text(encoding="utf-8")
    # Restore generated links to their source notation before refreshing targets.
    guide = re.sub(r"\[([^\]\n]+)\]\(routes/[^)]+\)（\[模块条目\]\(modules/[^)]+\)）",
                   lambda match: "`" + match.group(1) + "`", guide)
    by_path: dict[str, list[dict]] = defaultdict(list)
    for route in routes:
        by_path[route["path"]].append(route)

    def link(match: re.Match) -> str:
        label = match.group(1)
        reference = re.fullmatch(r"(?:(GET|POST|PUT|PATCH|DELETE|HEAD|OPTIONS|ANY) )?(/api/[^\s`]+)", label)
        if not reference:
            return match.group(0)
        method, path = reference.groups()
        candidates = by_path.get(path.split("?", 1)[0], [])
        if method:
            candidates = [route for route in candidates if route["method"] == method]
        if len(candidates) != 1:
            return match.group(0)
        route = candidates[0]
        return f"[{label}]({route['document']})（[模块条目]({route['moduleDocument']})）"

    chunks = re.split(r"(```[^\n]*\n.*?\n```)", guide, flags=re.DOTALL)
    for index in range(0, len(chunks), 2):
        chunks[index] = re.sub(r"(?<!\[)`([^`\n]+)`", link, chunks[index])
    return "".join(chunks)


def generate_split_docs(groups, models, registry) -> None:
    outputs: dict[Path, str] = {}

    def save(relative: str, lines: list[str]) -> None:
        outputs[OUT / relative] = "\n".join(lines).rstrip() + "\n"

    model_files = {str(m[2]): "models/" + stable_name(m[0], str(m[2].relative_to(ROOT))) for m in models}
    model_index = ["# 模型索引", "", "[文档入口](../第三方对接接口手册.md) · [接口索引](接口索引.md)", "",
                   "按接口中的关联模型链接读取。模型不等同于请求DTO；必填、默认值和可空以具体接口为准。同名Java类通过源码路径区分。动态JSONObject和多态返回值可能没有固定字段。", "",
                   "| 模型 | 说明 | 源码 |", "|---|---|---|"]
    for model in models:
        name, title, path, parent, _ = model
        filename = model_files[str(path)]
        model_index.append(f"| [{name}]({filename}) | {escape(title)} | `{path.relative_to(ROOT)}` |")
        lines = [f"# {name}" + (f"：{title}" if title else ""), "",
                 "[文档入口](../../第三方对接接口手册.md) · [模型索引](../模型索引.md)", "",
                 f"源码：`{path.relative_to(ROOT)}`" + (f"；继承 `{parent}`" if parent else ""), "",
                 "模型字段表描述可识别的源码字段，包含继承字段；请求必填由具体接口决定。Java初始值不等于请求默认值。未声明的格式、单位或可空条件不能自行推断。", "",
                 "| 字段 | JSON类型 | Java类型 | 请求必填 / 可空 | 格式 / 范围 / 可选值 | 默认值 / 未传行为 | 说明 | 示例 |", REQUEST_DIVIDER]
        fields = all_fields(model, registry, path)
        lines += [model_row(field, typ, "平台最终显示名称：平台备注优先，否则设备名称，否则自动名称；不保证是设备端名称" if name == "Preset" and field == "presetName" else desc) for field, typ, desc in fields]
        nested = set(re.findall(r"[A-Za-z_][A-Za-z_0-9]*", " ".join(typ for _, typ, _ in fields)))
        related = [model_for(n, path, registry) for n in sorted(nested | ({parent} if parent else set()))]
        related = [m for m in related if m and m[2] != path]
        if related:
            lines += ["", "## 关联模型", ""]
            lines += [f"- [{m[0]}]({Path(model_files[str(m[2])]).name})：`{m[2].relative_to(ROOT)}`" for m in related]
        save(filename, lines)
    save("模型索引.md", model_index)

    routes = []
    module_rows = []
    for title, cls, path, endpoints in groups:
        module_filename = "modules/" + cls + ".md"
        module_lines = [f"# {title}（{cls}）", "",
                        "[文档入口](../../第三方对接接口手册.md) · [完整接口索引](../接口索引.md) · [AI 对接指南](../AI对接指南.md)", "",
                        "| 方法 | 路径 | 功能 |", "|---|---|---|"]
        module_count = 0
        for mappings, summary, return_type, parameters in endpoints:
            for verb, route in mappings:
                filename = "routes/" + stable_name(verb.lower(), verb + " " + route)
                anchor = "route-" + Path(filename).stem
                module_document = module_filename + "#" + anchor
                module_lines.append(f'| <a id="{anchor}"></a>`{verb}` | [{escape(route)}](../{filename}) | {escape(summary)} |')
                lines = render_route(len(routes) + 1, title, cls, path, verb, route, summary, return_type, parameters, registry)
                lines[0] = f"# {verb} {route}"
                lines[1:1] = ["", f"[文档入口](../../第三方对接接口手册.md) · [所属模块条目](../{module_document}) · [AI 对接指南](../AI对接指南.md)"]
                lines = [line.replace("[第8.2节](#82-预置位)", "[接入指南第8.2节](../接入指南.md#82-预置位)") for line in lines]
                types = set(re.findall(r"[A-Za-z_][A-Za-z_0-9]*", return_type + " " + " ".join(p[1] for p in parameters)))
                if preset_route(route) and "/query" in route:
                    types.add("Preset")
                related = [model_for(n, path, registry) for n in sorted(types)]
                related = [m for m in related if m]
                lines += ["## 解析与实现约定", "",
                          "先核对本页请求位置与Content-Type；Query/Form不能自动替换成JSON。示例只展示形状，可能省略业务字段；必填以字段表与业务说明共同判断。", "",
                          "普通统一响应同时检查HTTP状态和code；data为null不等于失败。文件、流、兼容返回按本页响应类型解析。通用错误码表不是本接口必然返回的穷举集合。", "",
                          "控制或配置操作即使使用GET也会改变状态；网络超时表示结果未知，重试策略见[AI对接指南](../AI对接指南.md)。源码未声明的要求应向部署方确认。", ""]
                if related:
                    lines += ["## 关联模型", ""]
                    lines += [f"- [{m[0]}](../{model_files[str(m[2])]})：`{m[2].relative_to(ROOT)}`" for m in related]
                save(filename, lines)
                effective_parameters = preset_parameters(route, parameters)
                routes.append({"method": verb, "path": route, "summary": summary, "module": cls,
                               "document": filename, "moduleDocument": module_document, "source": str(path.relative_to(ROOT)),
                               "authentication": auth_text(route, verb), "contentType": content_type(verb, parameters),
                               "returnJavaType": return_type,
                               "parameters": [{"name": n, "javaType": t, "location": loc,
                                               "required": req, "description": desc}
                                              for n, t, loc, req, desc in effective_parameters],
                               "models": [model_files[str(m[2])] for m in related]})
                module_count += 1
        save(module_filename, module_lines)
        module_rows.append(f"| [{escape(title)}]({module_filename}) | {module_count} | `{cls}` |")

    outputs[OUT / "接入指南.md"] = link_integration_guide(routes)
    save("接口索引.md", ["# 完整接口索引", "", "[文档入口](../第三方对接接口手册.md) · [AI 对接指南](AI对接指南.md)", "",
                          "仅索引，不加载所有字段表。先按方法和完整路径定位，再打开一条接口详情。含内部回调、管理及可选模块；是否适合第三方调用看详情。", "",
                          "| 方法 | 路径 | 功能 | 模块 |", "|---|---|---|---|"] +
         [f"| `{r['method']}` | [{escape(r['path'])}]({r['document']}) | {escape(r['summary'])} | {r['module']} |" for r in routes])
    save("模块索引.md", ["# 模块索引", "", "[文档入口](../第三方对接接口手册.md) · [完整接口索引](接口索引.md)", "",
                          "| 模块 | 路由数 | Controller |", "|---|---:|---|"] + module_rows)
    manifest = {"schemaVersion": 1, "description": "源码检索索引，不是OpenAPI，也不代表已经完成业务校验审计。document和models相对本JSON文件所在目录。required保留文档原始标记，不可直接当作布尔值。",
                "entry": "../第三方对接接口手册.md", "aiGuide": "AI对接指南.md", "integrationGuide": "接入指南.md",
                "routeCount": len(routes), "modelCount": len(models), "routes": routes,
                "models": [{"name": m[0], "source": str(m[2].relative_to(ROOT)), "document": model_files[str(m[2])]} for m in models]}
    outputs[OUT / "route-index.json"] = json.dumps(manifest, ensure_ascii=False, indent=2) + "\n"
    outputs[MANUAL] = "\n".join([
        "# WVP-PRO 接口文档", "", "版本：2026-10-22。本文是统一入口，接口按需读取，避免一次加载全部字段表。", "",
        f"覆盖 **{len(routes)} 条路由、{len(groups)} 个 Controller、{len(models)} 个模型**。部署地址、凭证、模块开关与可用媒体协议由部署方提供。", "",
        "| 阅读目的 | 文档 |", "|---|---|",
        "| AI工具开始对接 | [AI对接指南](api/AI对接指南.md)：读取顺序、标识映射、调用流程、编码与错误处理 |",
        "| 了解业务流程 | [接入指南](api/接入指南.md)：鉴权、播放、接入、录像、云台、预置位 |",
        "| 按业务找接口 | [模块索引](api/模块索引.md) |",
        "| 已知方法和路径 | [完整接口索引](api/接口索引.md)：打开对应的一条接口详情 |",
        "| 查字段和嵌套对象 | [模型索引](api/模型索引.md)：每个模型独立一页 |",
        "| 程序检索路由 | [JSON路由索引](api/route-index.json)：定位文件，无须读全量文档 |",
        "| 维护或离线交付 | [文档维护说明](api/维护说明.md) |", "",
        "建议先读AI对接指南，再读一个业务流程和本次使用的接口、关联模型。整个doc/api目录应一起交付，保留相对路径。", "",
        "原手册第1—11节已迁至[接入指南](api/接入指南.md)；第12节拆为接口详情，第13节拆为模型详情。原有HTTP接口路径不受文档拆分影响。", ""])
    for path, content in outputs.items():
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(content, encoding="utf-8")
    # Only remove stale files from generator-owned directories, never manual guides.
    for directory in ("routes", "models", "modules"):
        for stale in (OUT / directory).glob("*.md"):
            if stale not in outputs:
                stale.unlink()
    largest = max((len(content.encode()), path) for path, content in outputs.items() if path.suffix == ".md")
    print(f"已生成：{len(routes)}条路由、{len(groups)}个模块、{len(models)}个模型；最大生成Markdown {largest[0]}字节（{largest[1].relative_to(ROOT)}）")


def main() -> None:
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
    generate_split_docs(groups, models, registry)


if __name__ == "__main__":
    main()
