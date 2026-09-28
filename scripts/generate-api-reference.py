#!/usr/bin/env python3
"""从 Controller 和 @Schema 注解重建静态接口索引。

运行：python3 scripts/generate-api-reference.py
这是源码索引，不替代运行实例的 /v3/api-docs；复杂条件和实际鉴权以运行配置为准。
"""

from __future__ import annotations

import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
JAVA = ROOT / "src/main/java/com/genersoft/iot/vmp"
OUT = ROOT / "doc/_content/api"
MAPPING = re.compile(r"@(Get|Post|Put|Patch|Delete|Request)Mapping\s*\(")
METHOD = re.compile(r"(?m)^\s*(?:public|private|protected)\s+(?:(?:synchronized|static)\s+)*([^\n(]+?)\s+(\w+)\s*\(")
FIELD = re.compile(r"(?m)^\s*(?:private|protected|public)\s+(?!static|final)([\w<>?, .\[\]]+?)\s+(\w+)\s*(?:=[^;]*)?;")
SPECIAL = {
    "page": "页码，从 1 开始", "count": "每页记录数", "query": "按名称或编号筛选，具体范围由该接口实现决定",
    "deviceId": "设备或下级平台国标编号", "channelId": "通道标识；直连接口为国标编号，/api/common/channel 接口为数据库整数 ID",
    "id": "数据库记录 ID", "startTime": "查询起始时间", "endTime": "查询结束时间",
    "app": "流应用名", "stream": "流 ID", "mediaServerId": "流媒体节点 ID",
    "online": "在线状态过滤，true 为在线", "status": "状态过滤，true 为在线",
    "command": "控制命令，取值见对应接口说明", "accessType": "接入类型：DEVICE 设备，PLATFORM 下级平台",
    "resourceType": "资源类型：CHANNEL 视频通道，DIRECTORY 目录节点",
    "channel": "通道对象；字段见模型索引", "serial": "设备或通道序列号",
    "code": "业务编码或验证码；具体格式见对应接口实现", "ids": "记录 ID 集合",
    "callId": "本次 SIP 会话或录像的唯一标识", "z": "地图瓦片缩放层级",
    "x": "地图瓦片横向坐标", "y": "地图瓦片纵向坐标",
    "civilCode": "行政区划编码", "schema": "请求使用的 URL 协议方案",
    "remoteHost": "外部可访问的完整媒体服务地址，含协议、主机和端口",
    "name": "名称", "start": "分页起始偏移量", "limit": "分页数量上限",
    "q": "搜索关键字", "timeout": "超时时间；单位见接口实现",
    "cdn": "CDN 相关选项", "audio": "是否启用音频",
    "hasChannel": "是否仅显示有通道的节点", "businessGroup": "业务分组编号",
    "broadcastMode": "语音广播模式开关", "params": "请求参数集合",
    "file": "上传文件", "fileName": "文件名", "type": "类型编码",
    "expiresAt": "过期时间，格式 yyyy-MM-dd HH:mm:ss", "enable": "是否启用",
    "userId": "用户数据库 ID", "oldPassword": "旧密码的 32 位 MD5 字符串",
    "device": "设备对象；字段见模型索引", "speed": "控制速度，范围见对应接口",
    "preset": "预置位对象", "username": "用户名", "password": "密码；登录为 32 位 MD5，管理接口按各接口说明传值",
    "channel_type": "通道类型过滤", "dir_serial": "目录节点序列号",
    "fill": "是否填充地图区域", "transport": "传输协议",
    "checkchannelstatus": "是否检查通道状态", "transportmode": "媒体传输模式",
    "check_outputs": "是否检查输出状态", "t": "时间或防缓存参数，见接口实现",
    "autorestart": "是否自动重启", "passwd": "认证密码；请勿记录明文",
}
FIELD_HELP = {
    "id": "数据库记录 ID", "deviceId": "设备国标编号", "channelId": "通道标识",
    "name": "名称", "app": "流应用名", "stream": "流 ID", "mediaServerId": "流媒体节点 ID",
    "createTime": "创建时间", "updateTime": "更新时间", "startTime": "开始时间", "endTime": "结束时间",
    "password": "密码等敏感字段；不得写入日志或对外示例", "apiKey": "API Key 凭证；仅向授权调用方提供",
    "gbDeviceId": "国标通道编号", "gbPtzType": "国标摄像机结构类型",
    "gbSecrecy": "国标保密属性", "gbSupplyLightType": "国标补光类型",
    "gbSvcSpaceSupportMod": "国标空域增强编码能力", "gbSvcTimeSupportMode": "国标时域增强编码能力",
    "gpsAltitude": "GPS 海拔高度", "gpsSpeed": "GPS 速度", "gpsDirection": "GPS 方向角",
    "gpsTime": "GPS 定位时间", "dataType": "关联的数据来源类型", "dbKey": "数据库或缓存键",
    "callId": "本次会话的 Call-ID", "deviceIds": "设备编号集合", "channelIds": "通道 ID 集合",
    "parentId": "父节点编号", "businessGroup": "业务分组编号",
    "schema": "媒体流协议类型", "vhost": "媒体服务虚拟主机", "ip": "IP 地址",
    "port": "网络端口", "local_ip": "本地 IP 地址", "local_port": "本地端口",
    "peer_ip": "对端 IP 地址", "peer_port": "对端端口",
    "localipAddress": "本地 IP 地址", "url": "资源 URL", "sourceURL": "源流 URL",
    "params": "附加参数", "paramMap": "解析后的附加参数映射",
    "positionCapability": "定位能力标识", "secrecy": "保密属性",
    "attribute": "设备或终端属性", "status": "状态", "datetime": "日期时间",
    "width": "画面宽度", "height": "画面高度", "lng": "经度", "lat": "纬度",
    "civilCode": "行政区划编码", "geoCoordSys": "地理坐标系",
    "fileName": "文件名称", "fileSize": "文件大小", "filePath": "文件路径",
    "file_name": "文件名称", "file_size": "文件大小", "file_path": "文件路径",
    "folder": "文件夹路径", "start_time": "开始时间", "time_len": "时长",
    "stream_id": "流 ID", "streamInfo": "流信息对象", "mediaInfo": "媒体编码信息",
    "mediaServer": "流媒体节点对象", "recordInfo": "录像文件信息",
    "playUrl": "录像播放 URL", "downloadUrl": "录像下载 URL",
    "httpPath": "HTTP 文件地址", "httpsPath": "HTTPS 文件地址",
    "httpDomainPath": "带域名的 HTTP 文件地址", "httpsDomainPath": "带域名的 HTTPS 文件地址",
    "recordList": "录像记录列表", "ids": "记录 ID 列表",
    "progress": "处理进度", "count": "记录数量", "lastTime": "最近一次时间",
    "username": "用户名", "enable": "是否启用", "authentication": "是否要求认证",
    "address": "网络或设备地址", "tcpPort": "TCP 端口", "udpPort": "UDP 端口",
    "networkType": "网络类型", "control": "控制选项", "key": "操作键或鉴权键",
    "totalReaderCount": "当前观看人数", "readerCount": "观看人数",
    "videoCodec": "视频编码格式", "audioCodec": "音频编码格式",
    "audioChannels": "音频声道数", "audioSampleRate": "音频采样率",
    "videoFrameSpeed": "视频帧率", "videoBitrate": "视频码率", "audioBitrate": "音频码率",
    "uniqueKey": "记录唯一键", "identifier": "标识符",
    "localTag": "本端 SIP 标签", "remoteTag": "对端 SIP 标签",
    "branch": "SIP Via 分支标识", "transactionInfo": "SIP 事务信息",
    "regist": "是否注册事件", "originType": "流来源类型编码",
    "originTypeStr": "流来源类型名称", "originUrl": "源流 URL",
    "originSock": "源流网络连接信息", "createStamp": "创建时间戳",
    "aliveSecond": "存活秒数", "bytesSpeed": "每秒数据量，字节/秒",
    "ssrcInfo": "RTP 流 SSRC 信息", "receiveIp": "RTP 收流 IP",
    "receivePort": "RTP 收流端口", "streamMode": "媒体传输模式",
    "type": "业务类型；可选值见对应枚举", "expirationTime": "到期时间",
    "cpu": "CPU 使用信息", "mem": "内存使用信息", "net": "网络使用信息",
    "netTotal": "网络累计流量", "disk": "磁盘使用信息",
    "accessToken": "登录后签发的 JWT", "serverId": "WVP 服务节点 ID",
    "alarmMethod": "报警方式编码", "alarmMethodDescription": "报警方式说明",
    "certifiable": "证书有效标识", "ptzType": "摄像机结构类型编码",
    "supplyLightType": "摄像机补光类型编码", "svcSpaceSupportMod": "空域增强编码能力",
    "svcTimeSupportMode": "时域增强编码能力", "process": "处理进度值",
    "msg": "结果或错误信息", "code": "结果或业务状态码",
    "customSecrecy": "共享通道自定义保密属性", "customPtzType": "共享通道自定义摄像机类型",
    "customPhotoelectricImagingTyp": "共享通道自定义光电成像类型",
    "customSupplyLightType": "共享通道自定义补光类型",
    "customSvcSpaceSupportMod": "共享通道自定义空域编码能力",
    "customSvcTimeSupportMode": "共享通道自定义时域编码能力",
    "customMobileDeviceType": "共享通道自定义移动设备类型",
    "customPoType": "共享通道自定义定位类型",
    "customFunctionType": "共享通道自定义功能类型",
    "presetId": "预置位编号", "presetName": "预置位名称",
    "zoomParam": "地图缩放参数", "extent": "地图经纬度范围",
    "chanelFields": "需要重置的通道字段名列表",
    "gprs": "是否支持 GPRS", "cdma": "是否支持 CDMA",
    "tdScdma": "是否支持 TD-SCDMA", "wcdma": "是否支持 WCDMA",
    "cdma2000": "是否支持 CDMA2000", "tdLte": "是否支持 TD-LTE",
    "other": "其他通信能力", "gps": "是否支持 GPS",
    "beidou": "是否支持北斗定位", "glonass": "是否支持 GLONASS 定位",
    "gaLiLeo": "是否支持 Galileo 定位",
    "audioSamplingRate": "音频采样率", "audioSamplingBits": "音频采样位数",
    "audioOutputEnable": "是否启用音频输出",
    "alarmSign": "报警标志", "mediaType": "媒体类型",
    "streamType": "码流类型", "storageType": "存储类型",
    "size": "数据大小或数量，单位见对应接口",
    "currentFileDuration": "当前录像文件时长",
    "TotalVideoDuration": "累计录像时长",
    "enableHls": "是否启用 HLS", "transcodingStatus": "转码状态",
    "noneReaderDuration": "无观看者持续时间",
    "hookType": "媒体 Hook 事件类型", "expireTime": "到期时间",
    "enable_audio": "是否启用音频", "enable_mp4": "是否启用 MP4 录制",
    "mp4_max_second": "单段 MP4 最大时长，秒", "mp4_save_path": "MP4 保存路径",
    "stream_replace": "替换后的流标识", "modify_stamp": "是否修改时间戳",
    "tcpMode": "TCP 收流模式", "re_use_port": "是否复用端口",
    "ssrc": "RTP SSRC", "data": "事件数据对象",
    "push": "推流负载", "proxy": "拉流代理负载",
    "gbReceive": "国标收流负载", "gbSend": "国标发流负载",
    "authority": "角色权限集合", "pushKey": "推流鉴权键",
    "role": "用户角色对象", "coordinateSystem": "地图坐标系",
    "tilesUrl": "地图瓦片 URL", "tileSize": "地图瓦片尺寸",
    "zoom": "地图缩放级别", "center": "地图中心坐标",
    "maxZoom": "地图最大缩放级别", "minZoom": "地图最小缩放级别",
    "device": "设备信息对象", "channel": "通道信息对象",
    "serverPort": "服务监听端口", "sip": "SIP 配置信息",
    "addOn": "附加配置信息", "version": "系统版本信息",
    "jt1078Config": "JT1078 配置信息",
    "groupAlias": "业务分组别名", "allCount": "总数量",
    "onlineCount": "在线数量", "controlType": "控制类型",
    "no": "成员编号", "unicodeNo": "统一编码编号",
    "blockId": "区块或区域 ID", "unitNo": "单元编号",
    "terminalMemberStatus": "终端成员状态", "channelDeviceId": "通道国标编号",
}


def balanced(src: str, opening: int) -> tuple[str, int]:
    """返回括号内文本和右括号位置，忽略字符串中的括号。"""
    depth = 0
    quoted = False
    escaped = False
    for i in range(opening, len(src)):
        char = src[i]
        if quoted:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                quoted = False
        elif char == '"':
            quoted = True
        elif char == "(":
            depth += 1
        elif char == ")":
            depth -= 1
            if depth == 0:
                return src[opening + 1:i], i
    return "", opening


def parts(src: str) -> list[str]:
    result = []
    start = 0
    angle = paren = brace = 0
    quoted = escaped = False
    for i, char in enumerate(src):
        if quoted:
            if escaped:
                escaped = False
            elif char == "\\":
                escaped = True
            elif char == '"':
                quoted = False
            continue
        if char == '"':
            quoted = True
        elif char == "<":
            angle += 1
        elif char == ">":
            angle -= 1
        elif char == "(":
            paren += 1
        elif char == ")":
            paren -= 1
        elif char == "{":
            brace += 1
        elif char == "}":
            brace -= 1
        elif char == "," and angle == paren == brace == 0:
            result.append(src[start:i].strip())
            start = i + 1
    result.append(src[start:].strip())
    return [p for p in result if p]


def annotations(src: str, name: str) -> list[str]:
    found = []
    for match in re.finditer(r"@" + re.escape(name) + r"\s*\(", src):
        value, _ = balanced(src, match.end() - 1)
        found.append(value)
    return found


def strings(src: str) -> list[str]:
    return re.findall(r'"((?:\\.|[^"\\])*)"', src)


def attr(src: str, key: str) -> str:
    match = re.search(r"\b" + re.escape(key) + r'\s*=\s*"((?:\\.|[^"\\])*)"', src)
    if not match:
        return ""
    value = match.group(1)
    offset = match.end()
    while extra := re.match(r'\s*\+\s*"((?:\\.|[^"\\])*)"', src[offset:]):
        value += extra.group(1)
        offset += extra.end()
    return value


def clean(src: str) -> str:
    return re.sub(r"\s+", " ", src.strip()).replace("|", "\\|")


def location(base: str, suffix: str) -> str:
    return "/" + "/".join(filter(None, [base.strip("/"), suffix.strip("/")]))


def mapping_values(annotation: str) -> list[str]:
    values = strings(annotation)
    return [v for v in values if v.startswith("/")] or ([""] if not values else values)


def parse_controllers():
    groups = []
    for path in sorted(JAVA.rglob("*.java")):
        source = path.read_text(encoding="utf-8-sig")
        if "@RestController" not in source and "@Controller" not in source:
            continue
        class_match = re.search(r"\bpublic\s+class\s+(\w+)", source)
        if not class_match:
            continue
        class_header = source[:class_match.start()]
        bases = annotations(class_header, "RequestMapping")
        base = mapping_values(bases[-1])[0] if bases else ""
        tags = annotations(class_header, "Tag")
        title = attr(tags[-1], "name") if tags else class_match.group(1)
        if "@Hidden" in class_header:
            title += "（隐藏或内部接口）"
        if "@ConditionalOnProperty" in class_header:
            title += "（需启用配置）"
        class_body = source.find("{", class_match.end()) + 1
        methods = list(METHOD.finditer(source[class_body:]))
        endpoints = []
        for method in methods:
            start = class_body + method.start()
            closings = list(re.finditer(r"(?m)^\s*}\s*$", source[class_body:start]))
            prior = class_body + closings[-1].end() if closings else class_body
            prefix = source[prior:start]
            mappings = []
            for mapped in MAPPING.finditer(prefix):
                value, _ = balanced(prefix, mapped.end() - 1)
                http = mapped.group(1).upper()
                if http == "REQUEST":
                    verbs = re.findall(r"RequestMethod\.(\w+)", value)
                    http = "/".join(verbs) if verbs else "ANY"
                for suffix in mapping_values(value):
                    mappings.append((http, location(base, suffix)))
            if not mappings:
                continue
            signature, _ = balanced(source, class_body + method.end() - 1)
            operations = annotations(prefix, "Operation")
            summary = attr(operations[-1], "summary") if operations else method.group(2)
            described = {}
            for parameter in annotations(prefix, "Parameter"):
                key = attr(parameter, "name")
                if key:
                    described[key] = (attr(parameter, "description"), "required = true" in parameter)
            parameters = []
            for raw in parts(signature):
                ann = list(re.finditer(r"@\w+(?:\([^()]*\))?", raw))
                place = "query/form"
                if "@PathVariable" in raw:
                    place = "path"
                elif "@RequestBody" in raw:
                    place = "JSON body"
                elif "@RequestHeader" in raw:
                    place = "header"
                elif "@RequestPart" in raw or "MultipartFile" in raw:
                    place = "multipart"
                bare = re.sub(r"@\w+(?:\([^()]*\))?\s*", "", raw).replace("final ", "").strip()
                match = re.search(r"(.+?)\s+(\w+)$", bare)
                if not match:
                    continue
                typ, name = match.groups()
                if typ in {"HttpServletRequest", "HttpServletResponse", "ServletRequest", "ServletResponse", "Principal"}:
                    continue
                name_override = re.search(r'@(?:RequestParam|PathVariable|RequestHeader)\s*\(\s*(?:value|name)?\s*=?\s*"([^"]+)"', raw)
                if name_override:
                    name = name_override.group(1)
                description, annotated_required = described.get(name, ("", False))
                if not description:
                    description = SPECIAL.get(name, "源码未提供说明；请结合业务字段核对")
                required = "是" if place == "path" or annotated_required or "required = true" in raw else "未声明"
                if "required = false" in raw or "defaultValue" in raw:
                    required = "否"
                if place == "JSON body" and "@RequestBody" in raw:
                    required = "否" if "required = false" in raw else "是"
                    if typ == "List<Long>":
                        description = "JSON 整数数组，元素为记录 ID"
                    elif typ == "JSONObject":
                        description = "动态 JSON 对象；字段由对应回调协议定义"
                    else:
                        description = "请求体模型；字段见模型索引"
                parameters.append((name, clean(typ), place, required, clean(description)))
            endpoints.append((mappings, clean(summary), clean(method.group(1)), parameters))
        if endpoints:
            groups.append((title, class_match.group(1), path, endpoints))
    return groups


def parse_models(model_types: set[str]):
    models = []
    for path in sorted(JAVA.rglob("*.java")):
        source = path.read_text(encoding="utf-8-sig")
        class_match = re.search(r"\bpublic\s+(?:class|record|enum)\s+(\w+)", source)
        if not class_match:
            continue
        class_name = class_match.group(1)
        if "@Schema" not in source and class_name not in model_types:
            continue
        header = source[:class_match.start()]
        class_schema = annotations(header[-500:], "Schema")
        title = attr(class_schema[-1], "description") if class_schema else ""
        fields = []
        body = source[source.find("{", class_match.end()) + 1:]
        structural = re.sub(r'/\*.*?\*/|//[^\n]*|"(?:\\.|[^"\\])*"',
                            lambda found: " " * len(found.group()), body, flags=re.DOTALL)
        depth = 1
        scanned = 0
        for match in FIELD.finditer(body):
            for character in structural[scanned:match.start()]:
                if character == "{":
                    depth += 1
                elif character == "}":
                    depth -= 1
            scanned = match.start()
            if depth != 1:
                continue
            # 结构化文本已屏蔽字符串与注释，避免描述中的分号截断注解。
            previous_field_end = structural.rfind(";", 0, match.start())
            tail = body[max(previous_field_end + 1, match.start() - 1200):match.start()]
            schema = annotations(tail, "Schema")
            description = attr(schema[-1], "description") if schema else ""
            if not description:
                config = annotations(tail, "ConfigAttribute")
                description = attr(config[-1], "description") if config else ""
            if not description:
                comments = re.findall(r"/\*\*(.*?)\*/", tail, re.DOTALL)
                if comments:
                    description = clean(re.sub(r"(?m)^\s*\*\s?", "", comments[-1]))
            if not description:
                description = FIELD_HELP.get(match.group(2), "源码未标注字段含义")
            fields.append((match.group(2), clean(match.group(1)), clean(description)))
        field_names = {name for name, _, _ in fields}
        for annotation in re.finditer(r"@Schema\s*\(", body):
            if 1 + structural[:annotation.start()].count("{") - structural[:annotation.start()].count("}") != 1:
                continue
            value, end = balanced(body, annotation.end() - 1)
            getter = re.match(r"\s*public\s+([\w<>, ?\[\].]+?)\s+(?:get|is)([A-Z]\w*)\s*\(\s*\)", body[end + 1:end + 250])
            if not getter:
                continue
            name = getter.group(2)[0].lower() + getter.group(2)[1:]
            description = attr(value, "description")
            if name not in field_names and description:
                fields.append((name, clean(getter.group(1)), clean(description + "（派生字段）")))
                field_names.add(name)
        if fields:
            parent = re.search(r"\bextends\s+(\w+)", source[class_match.end():source.find("{", class_match.end())])
            models.append((class_name, title, path, parent.group(1) if parent else "", fields))
    return models


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    groups = parse_controllers()
    lines = ["# 全项目 HTTP 接口索引", "", "本索引由 `scripts/generate-api-reference.py` 从当前 Java Controller 与 OpenAPI 注解生成。路径、方法、参数类型、必填标记、描述和返回类型可在源码中追溯。没有注解的参数保留‘未声明’，避免猜测。`ANY` 表示源码的 `@RequestMapping` 未限定 HTTP 方法。运行实例的 `/v3/api-docs` 可用于查看实际启用的接口。标为‘隐藏或内部接口’的路由通常供流媒体回调、兼容层或内部系统使用；标为‘需启用配置’的路由只有相应配置开启时才存在。", "", "请求参数位置 `query/form`：GET 通常放查询串，POST 且未使用 `@RequestBody` 时通常以表单或查询参数提交。`JSON body` 的字段见[模型字段索引](models.md)。接口鉴权依运行配置，默认规则见[对接接口详解](integration.md)。响应一般包装为 `WVPResult`，流、文件和 `/api/v1/**` 等例外按各接口实际返回。", ""]
    count = 0
    for title, cls, path, endpoints in groups:
        lines += [f"## {title}（{cls}）", "", f"源码：`{path.relative_to(ROOT)}`", ""]
        for mappings, summary, return_type, parameters in endpoints:
            for http, route in mappings:
                lines += [f"### `{http} {route}`", "", f"{summary}。源码返回类型：`{return_type}`。", ""]
                if parameters:
                    lines += ["| 参数 | Java 类型 | 位置 | 必填 | 说明 |", "|---|---|---|---|---|"]
                    lines += [f"| `{name}` | `{typ}` | {place} | {required} | {description} |" for name, typ, place, required, description in parameters]
                    lines.append("")
                else:
                    lines += ["无业务请求参数。", ""]
                count += 1
    lines += [f"共收录 {count} 条路由映射，来自 {len(groups)} 个 Controller。", ""]
    (OUT / "reference.md").write_text("\n".join(lines), encoding="utf-8")

    model_types = set()
    for _, _, _, endpoints in groups:
        for _, _, return_type, parameters in endpoints:
            model_types.update(re.findall(r"[A-Za-z_][A-Za-z_0-9]*", return_type))
            model_types.update(typ for _, typ, place, _, _ in parameters if place == "JSON body")
    models = parse_models(model_types)
    lines = ["# API 模型字段索引", "", "本页汇总源码 `@Schema` 模型与 Controller 中使用的请求体、返回值模型。字段说明优先取源码 `@Schema`、`@ConfigAttribute` 或字段注释；其余采用生成脚本中的通用字段词典，具体取值、单位和行为仍以该模型源码及运行实例为准。继承模型还包含父类字段。", ""]
    for name, title, path, parent, fields in models:
        lines += [f"## {name}" + (f"：{title}" if title else ""), "", f"源码：`{path.relative_to(ROOT)}`" + (f"；继承 `{parent}`，还包含父类字段" if parent else ""), "", "| 字段 | Java 类型 | 说明 |", "|---|---|---|"]
        lines += [f"| `{field}` | `{typ}` | {description} |" for field, typ, description in fields]
        lines.append("")
    lines += [f"共收录 {len(models)} 个模型。", ""]
    (OUT / "models.md").write_text("\n".join(lines), encoding="utf-8")
    print(f"生成 {count} 条路由映射、{len(models)} 个模型")


if __name__ == "__main__":
    main()
