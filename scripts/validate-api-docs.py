#!/usr/bin/env python3
"""Validate generated API documentation, relative links and reproducibility offline."""
from __future__ import annotations

import hashlib
import importlib.util
import json
import re
import subprocess
import sys
from pathlib import Path
from urllib.parse import unquote

ROOT = Path(__file__).resolve().parents[1]
DOC = ROOT / 'doc/api'
ENTRY = ROOT / 'doc/第三方对接接口手册.md'


def require(condition, message):
    if not condition:
        raise AssertionError(message)


def snapshots():
    return {str(p.relative_to(ROOT)): hashlib.sha256(p.read_bytes()).hexdigest()
            for p in [ENTRY, *DOC.rglob('*')] if p.is_file()}


def slug(text):
    text = re.sub(r'<[^>]+>', '', text).strip().lower().replace('`', '')
    return re.sub(r'[^\w\-\s\u4e00-\u9fff]', '', text).replace(' ', '-')


def main():
    before = snapshots()
    subprocess.run([sys.executable, str(ROOT / 'scripts/generate-complete-api-manual.py')], check=True)
    require(before == snapshots(), '重新生成后有变化，请先生成并保存输出再校验')
    manifest = json.loads((DOC / 'route-index.json').read_text())
    spec = importlib.util.spec_from_file_location('api_source', ROOT / 'scripts/generate-api-reference.py')
    source = importlib.util.module_from_spec(spec)
    spec.loader.exec_module(source)
    expected = [(method, route) for _, _, _, endpoints in source.parse_controllers()
                for mappings, _, _, _ in endpoints for method, route in mappings]
    actual = [(r['method'], r['path']) for r in manifest['routes']]
    require(sorted(expected) == sorted(actual), '路由与源码不一致')
    require(len(actual) == len(set(actual)), '方法+路径重复，不能共用稳定文件名')
    require(len(actual) == manifest['routeCount'], '路由计数不一致')
    require(len(manifest['models']) == manifest['modelCount'], '模型计数不一致')
    require(len(list((DOC / 'routes').glob('*.md'))) == len(actual), '接口文件数不一致')
    require(len(list((DOC / 'models').glob('*.md'))) == manifest['modelCount'], '模型文件数不一致')
    require(len({m['document'] for m in manifest['models']}) == manifest['modelCount'], '模型文件名冲突')
    for route in manifest['routes']:
        path = DOC / route['document']
        require(path.is_file(), f'接口文件不存在：{path}')
        content = path.read_text()
        require(content.startswith(f"# {route['method']} {route['path']}\n"), f'接口页错配：{path}')
        require(content.count('| 字段 | 位置 | 类型 | 必填 |') == 4, f'请求字段表不完整：{path}')
        for model in route['models']:
            require((DOC / model).is_file(), f'关联模型不存在：{model}')
    for model in manifest['models']:
        require((DOC / model['document']).is_file(), f'模型文件不存在：{model}')
        require((ROOT / model['source']).is_file(), f'模型源码不存在：{model}')

    files = [ENTRY, *DOC.rglob('*.md'), ROOT / 'doc/外部系统视频接入与云台控制接口文档.md']
    tables = examples = links = 0
    anchors = {}
    for path in files:
        headings = re.findall(r'^#{1,6}\s+(.+)$', path.read_text(), re.M)
        anchors[path.resolve()] = {slug(h) for h in headings}
    for path in files:
        content = path.read_text()
        fenced = False
        width = None
        for line_no, line in enumerate(content.splitlines(), 1):
            if line.startswith('```'):
                fenced = not fenced
                width = None
                continue
            if fenced:
                continue
            if line.startswith('|'):
                columns = len(re.split(r'(?<!\\)\|', line)) - 2
                if width is None:
                    width = columns
                    tables += 1
                require(columns == width, f'表格列数错误：{path}:{line_no}，{columns}!={width}')
            else:
                width = None
        require(not fenced, f'代码块未闭合：{path}')
        for block in re.findall(r'```json\s*\n(.*?)\n```', content, re.S):
            json.loads(block)
            examples += 1
        prose = re.sub(r'```[^\n]*\n.*?\n```', '', content, flags=re.S)
        for target in re.findall(r'\]\(([^\s)]+)\)', prose):
            if re.match(r'\w+://', target):
                continue
            relative, _, anchor = unquote(target).partition('#')
            linked = (path.parent / relative).resolve() if relative else path.resolve()
            require(linked.is_file(), f'链接文件不存在：{path} -> {target}')
            if anchor and linked in anchors:
                require(anchor in anchors[linked], f'锚点不存在：{path} -> {target}')
            links += 1
    require(ENTRY.stat().st_size < 4096, '入口超过4KB，不应嵌入大表')
    print(f'校验通过：{len(actual)}路由、{manifest["modelCount"]}模型、{tables}字段/索引表、'
          f'{examples}JSON示例、{links}链接；生成结果稳定。')


if __name__ == '__main__':
    main()
