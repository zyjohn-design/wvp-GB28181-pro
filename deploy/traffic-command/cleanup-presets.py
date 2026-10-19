#!/usr/bin/env python3
"""查询国标预置位，或删除已人工核实的数字编号；仅使用 Python 3 标准库。"""

import argparse
import datetime
import json
import os
from pathlib import Path
import re
import sys
import time
import urllib.error
import urllib.parse
import urllib.request


def numeric_id(value):
    if not re.fullmatch(r"[1-9][0-9]*", str(value)) or not 1 <= int(value) <= 255:
        raise argparse.ArgumentTypeError("预置位编号必须是 1-255 的整数，不能传 PresetID_6")
    return int(value)


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--url", required=True, help="WVP 地址，例如 http://127.0.0.1:18080")
    parser.add_argument("--device-id", required=True, help="设备/下级平台国标编号")
    parser.add_argument("--channel-id", required=True, help="通道国标编号（不是数据库整数 ID）")
    parser.add_argument("--preset-id", type=numeric_id, help="已在源端核实的实际数字编号")
    parser.add_argument("--reported-id", help="对应异常返回值，例如 PresetID_6；用于核对是否消失")
    parser.add_argument("--delete", action="store_true", help="发送删除指令；默认只查询")
    parser.add_argument("--output-dir", default="preset-cleanup-records", help="查询结果保存目录")
    args = parser.parse_args()
    url = urllib.parse.urlsplit(args.url)
    if url.scheme not in ("http", "https") or not url.netloc or url.query or url.fragment or url.username:
        parser.error("--url 必须是有效的 HTTP(S) 地址，不包含凭据、查询参数或片段")
    if args.delete and args.preset_id is None:
        parser.error("删除必须显式提供 --preset-id，脚本不会从异常字符串推断编号")
    for value in (args.device_id, args.channel_id):
        if not re.fullmatch(r"[0-9]{20}", value):
            parser.error("设备和通道请填写 20 位国标编号")
    api_key = os.environ.get("WVP_API_KEY")
    token = os.environ.get("WVP_ACCESS_TOKEN")
    if not api_key and not token:
        parser.error("请设置 WVP_API_KEY 或 WVP_ACCESS_TOKEN 环境变量")
    headers = {"api-key": api_key} if api_key else {"access-token": token}
    headers["Accept"] = "application/json"
    # 不跟随重定向，以免鉴权信息被转发到其他地址。
    class NoRedirect(urllib.request.HTTPRedirectHandler):
        def redirect_request(self, req, fp, code, msg, hdrs, newurl):
            return None
    opener = urllib.request.build_opener(NoRedirect())

    def request(action):
        endpoint = f"{args.url.rstrip('/')}/api/front-end/preset/{action}/{args.device_id}/{args.channel_id}"
        if action == "delete":
            endpoint += "?" + urllib.parse.urlencode({"presetId": args.preset_id})
        req = urllib.request.Request(endpoint, headers=headers)
        try:
            with opener.open(req, timeout=15) as response:
                body = response.read().decode("utf-8")
        except urllib.error.HTTPError as exc:
            raise RuntimeError(f"{action} 接口 HTTP {exc.code}；请检查地址、鉴权和服务日志") from exc
        except urllib.error.URLError as exc:
            raise RuntimeError(f"{action} 接口连接失败：{exc.reason}") from exc
        if not body.strip():
            if action == "delete":
                return None  # 旧版删除接口正常返回空响应。
            raise RuntimeError("查询返回空响应")
        try:
            result = json.loads(body)
        except ValueError as exc:
            raise RuntimeError("接口未返回 JSON，请检查 WVP 地址") from exc
        if not isinstance(result, dict) or result.get("code") != 0:
            raise RuntimeError(f"{action} 接口业务失败：{result}")
        return result

    def query():
        result = request("query")
        if not isinstance(result.get("data"), list) or any(not isinstance(p, dict) for p in result["data"]):
            raise RuntimeError("预置位查询 data 不是有效列表")
        return result

    before = query()
    out = Path(args.output_dir)
    out.mkdir(parents=True, exist_ok=True)
    stamp = datetime.datetime.now().strftime("%Y%m%d-%H%M%S-%f")

    def save(suffix, data):
        path = out / f"{args.channel_id}-{stamp}-{suffix}.json"
        with path.open("x", encoding="utf-8") as stream:
            json.dump(data, stream, ensure_ascii=False, indent=2)
        print(f"查询记录：{path}")

    save("before", before)
    print(json.dumps(before["data"], ensure_ascii=False, indent=2))
    if not args.delete:
        print("只查询，未删除。异常编号需先在设备/下级平台核实实际数字编号。")
        return 0
    if args.reported_id and not any(str(p.get("presetId")) == args.reported_id for p in before["data"]):
        raise RuntimeError("查询列表没有指定 --reported-id，已停止，未发送删除指令")
    print(f"发送删除指令：通道 {args.channel_id}，实际编号 {args.preset_id}")
    request("delete")
    # 接口成功只表示指令已提交，必须重新查询源端验证。
    for attempt in range(3):
        time.sleep(2)
        after = query()
        save(f"after-{attempt + 1}", after)
        remaining = []
        for preset in after["data"]:
            raw = str(preset.get("presetId"))
            try:
                matches = numeric_id(raw) == args.preset_id
            except argparse.ArgumentTypeError:
                matches = False
            if matches or (args.reported_id is not None and raw == args.reported_id):
                remaining.append(preset)
        if not remaining:
            print("重新查询确认目标预置位已消失。请关闭并重新打开播放窗口刷新列表。")
            print("JSON 仅保存列表证据，不包含云台位置，无法据此恢复预置位。")
            return 0
    print("目标预置位仍由源端返回，未确认清理成功；请到设备/下级平台删除或修正预置位映射。", file=sys.stderr)
    return 2


if __name__ == "__main__":
    try:
        sys.exit(main())
    except (RuntimeError, OSError) as error:
        print(f"错误：{error}", file=sys.stderr)
        sys.exit(1)
