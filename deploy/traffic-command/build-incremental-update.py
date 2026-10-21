#!/usr/bin/env python3
"""Build and verify committed WVP AMD64/ARM64 incremental packages.

Run from any directory: python3 deploy/traffic-command/build-incremental-update.py --clean-old
Requires Java 21, Maven, npm and installed web/node_modules. No Docker daemon is
needed here; the target host supplies its existing architecture-specific images.
"""
from __future__ import annotations

import argparse
from datetime import datetime
import hashlib
import json
import os
from pathlib import Path
import re
import shutil
import subprocess
import tarfile
import tempfile
import xml.etree.ElementTree as ET
import zipfile
from zoneinfo import ZoneInfo

ROOT = Path(__file__).resolve().parents[2]
DEPLOY = ROOT / 'deploy/traffic-command'
RELEASES = DEPLOY / 'releases'
TEMPLATES = DEPLOY / 'update-package-templates/device-register-status'


def require(condition, message):
    if not condition:
        raise RuntimeError(message)


def git(*args):
    return subprocess.check_output(['git', *args], cwd=ROOT, text=True).strip()


def digest(path):
    value = hashlib.sha256()
    with path.open('rb') as stream:
        for block in iter(lambda: stream.read(1024 * 1024), b''):
            value.update(block)
    return value.hexdigest()


def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument('--arch', choices=['both', 'amd64', 'arm64'], default='both')
    parser.add_argument('--clean-old', action='store_true', help='验证新双架构包后删除releases中的旧更新包及解包目录')
    args = parser.parse_args()
    require(not args.clean_old or args.arch == 'both', '清理旧包必须同时生成两个架构')
    require(not git('status', '--porcelain'), '工作区必须先提交，保证包与提交一一对应')
    commit = git('rev-parse', 'HEAD')
    now = datetime.now(ZoneInfo('Asia/Shanghai'))
    release_id = now.strftime('%Y%m%d-%H%M%S') + '-' + commit[:9]
    RELEASES.mkdir(exist_ok=True)
    old = [p for p in RELEASES.iterdir() if p.name.startswith('wvp-') and
           (p.is_dir() or p.name.endswith(('.tar.gz', '.tar.gz.sha256')))]
    logs = Path(tempfile.mkdtemp(prefix='wvp-incremental-build-'))
    print(f'提交：{commit}\n构建日志：{logs}', flush=True)
    env = dict(os.environ)
    node_major = int(subprocess.check_output(['node', '--version'], text=True).strip().lstrip('v').split('.')[0])
    if node_major >= 17 and '--openssl-legacy-provider' not in env.get('NODE_OPTIONS', ''):
        env['NODE_OPTIONS'] = (env.get('NODE_OPTIONS', '') + ' --openssl-legacy-provider').strip()
    java = subprocess.run(['java', '-version'], capture_output=True, text=True, check=True)
    require(re.search(r'version "21\.', java.stderr + java.stdout), '需要Java 21')

    def run(label, command, cwd=ROOT):
        print(f'开始：{label}', flush=True)
        with (logs / (label + '.log')).open('w') as output:
            result = subprocess.run(command, cwd=cwd, env=env, stdout=output, stderr=subprocess.STDOUT)
        require(result.returncode == 0, f'{label}失败，日志：{logs / (label + ".log")}；旧包未清理')
        print(f'完成：{label}', flush=True)

    run('api-docs', ['python3', 'scripts/validate-api-docs.py'])
    run('upgrade-rollback-tests', ['python3', '-m', 'unittest', 'discover', '-s', str(DEPLOY / 'tests'), '-p', 'test_incremental_update.py', '-v'])
    run('frontend-tests', ['npx', '--no-install', 'vue-cli-service', 'test:unit', '--runInBand', '--watch=false',
                          '--json', '--outputFile=' + str(logs / 'frontend-tests.json'),
                          'tests/unit/utils/presetApi.spec.js', 'tests/unit/utils/presetId.spec.js',
                          'tests/unit/utils/requestError.spec.js', 'tests/unit/views/presetRemarks.spec.js',
                          'tests/unit/components/HiMap.spec.js', 'tests/unit/utils/mapBootstrap.spec.js',
                          'tests/unit/views/mapRecovery.spec.js', 'tests/unit/utils/channelMapLayer.spec.js',
                          'tests/unit/views/mapInteraction.spec.js'], ROOT / 'web')
    run('map-entry-tests', ['node', 'tests/map-entry-smoke.js'], ROOT / 'web')
    run('frontend-build', ['npm', 'run', 'build:prod'], ROOT / 'web')
    run('backend-test-package', ['mvn', '-q', 'clean', 'package', '-P', 'jar'])
    require(not git('status', '--porcelain') and git('rev-parse', 'HEAD') == commit, '构建期间源码发生变化，停止打包')
    jars = list((ROOT / 'target').glob('wvp-pro-*.jar'))
    require(len(jars) == 1, '必须只有一个新构建的JAR')
    jar = jars[0]
    static = ROOT / 'src/main/resources/static'
    counts = dict(tests=0, failures=0, errors=0, skipped=0)
    for report in (ROOT / 'target/surefire-reports').glob('TEST-*.xml'):
        suite = ET.parse(report).getroot()
        for key in counts:
            counts[key] += int(suite.get(key, '0'))
    require(counts['tests'] > 0 and counts['failures'] == counts['errors'] == 0, '后端测试报告不完整或失败')
    frontend = json.loads((logs / 'frontend-tests.json').read_text())
    require(frontend['success'] and frontend['numPassedTests'] > 0, '前端测试未通过')
    upgrade_log = (logs / 'upgrade-rollback-tests.log').read_text()
    upgrade_tests = re.search(r'Ran (\d+) tests?', upgrade_log)
    require(upgrade_tests and re.search(r'^OK$', upgrade_log, re.M), '升级回滚测试未通过')
    with zipfile.ZipFile(jar) as archive:
        for relative in ['db/preset-remark.sql', 'com/genersoft/iot/vmp/conf/PresetRemarkSchema.class',
                         'com/genersoft/iot/vmp/gb28181/service/impl/PresetRemarkService.class']:
            require(archive.read('BOOT-INF/classes/' + relative) == (ROOT / 'target/classes' / relative).read_bytes(), 'JAR内容不一致：' + relative)
        require(archive.read('BOOT-INF/classes/static/index.html') == (static / 'index.html').read_bytes(), 'JAR内前端不同步')
        properties = archive.read('BOOT-INF/classes/git.properties').decode()
        require(re.search(r'^git\.commit\.id(?:\.full)?=' + re.escape(commit) + r'$', properties, re.M), 'JAR提交号不一致')

    outputs = []
    for arch in (['amd64', 'arm64'] if args.arch == 'both' else [args.arch]):
        name = f'wvp-incremental-{release_id}-{arch}'
        package = RELEASES / name
        target = RELEASES / (name + '.tar.gz')
        require(not package.exists() and not target.exists(), '更新包已存在：' + name)
        (package / 'artifacts').mkdir(parents=True)
        (package / 'docker').mkdir()
        (package / 'docs').mkdir()
        (package / 'database').mkdir()
        (package / 'config').mkdir()
        shutil.copy2(DEPLOY / 'config/nginx/wvp-web.conf.template', package / 'config/wvp-web.conf.template')
        shutil.copy2(TEMPLATES / 'nginx-before-map-cache.conf.template', package / 'config/nginx-before-map-cache.conf.template')
        shutil.copy2(jar, package / 'artifacts/wvp.jar')
        shutil.copytree(static, package / 'artifacts/web-static', ignore=shutil.ignore_patterns('.DS_Store'))
        shutil.copytree(ROOT / 'doc/api', package / 'docs/api')
        for filename in ['第三方对接接口手册.md', '外部系统视频接入与云台控制接口文档.md', '预置位国标控制说明.md']:
            shutil.copy2(ROOT / 'doc' / filename, package / 'docs' / filename)
        shutil.copy2(ROOT / 'src/main/resources/db/preset-remark.sql', package / 'database/preset-remark.sql')
        for filename in ['backend.Dockerfile', 'web.Dockerfile']:
            shutil.copy2(TEMPLATES / filename, package / 'docker' / filename)
        values = {'__PACKAGE_NAME__': name, '__RELEASE_ID__': release_id, '__TARGET_ARCH__': arch,
                  '__GIT_COMMIT__': commit, '__BACKEND_RELEASE__': f'local/gb28181-wvp:{release_id}-{arch}',
                  '__WEB_RELEASE__': f'local/gb28181-wvp-web:{release_id}-{arch}',
                  '__BACKEND_BACKUP__': f'local/gb28181-wvp:pre-{release_id}-{arch}',
                  '__WEB_BACKUP__': f'local/gb28181-wvp-web:pre-{release_id}-{arch}'}
        for template in ['upgrade.sh.in', 'rollback.sh.in', 'RELEASE_INFO.txt.in']:
            text = (TEMPLATES / template).read_text()
            for key, value in values.items():
                text = text.replace(key, value)
            require(not re.search(r'__[A-Z_]+__', text), '模板仍有未填占位符')
            text = text.replace('database_migration=none', 'database_migration=auto_create_wvp_preset_remark_if_missing')
            text = text.replace('现场 .env、数据库和业务数据均未修改。', '现场.env保留；后端首次启动会在缺表时创建wvp_preset_remark。')
            (package / template.removesuffix('.in')).write_text(text)
        for filename in ['verify.sh', 'update-common.sh']:
            shutil.copy2(TEMPLATES / filename, package / filename)
        for filename in ['upgrade.sh', 'rollback.sh', 'verify.sh']:
            (package / filename).chmod(0o755)
        build_info = {'release_id': release_id, 'built_at': now.isoformat(), 'target_os': 'linux', 'target_arch': arch,
                      'git_commit': commit, 'source_worktree_dirty': False, 'java_runtime': 21,
                      'jar_sha256': digest(jar), 'backend_tests': counts,
                      'frontend_tests_passed': frontend['numPassedTests'],
                      'simulated_upgrade_rollback_tests_passed': int(upgrade_tests.group(1)),
                      'linux_runtime_tested': False, 'native_images_included': False}
        (package / 'BUILD_INFO.json').write_text(json.dumps(build_info, ensure_ascii=False, indent=2) + '\n')
        (package / 'README.md').write_text(f'''# WVP 前后端增量更新包

目标：Linux {arch}；Java 21；提交：{commit}。

本包累积包含当前版本前后端：国标预置位指令与SIP应答、平台名称备注、
预置位列表同步、云台参数与错误提示修复，以及可点击跳转的完整接口文档。
本次新增：点播在途状态超时清理与重试、停止点播的状态释放；电子地图加载失败恢复、
底图与点位分别重试、地图入口缓存修复，以及设备点位 Canvas 批量绘制与拖动跟随优化。
文档入口：docs/第三方对接接口手册.md，docs/api目录需一起保留。

## 适用环境与前置条件

沿用本项目Docker Compose部署，服务名polaris-wvp、polaris-wvp-web，
后端路径/opt/wvp/wvp.jar，前端路径/opt/dist/；部署目录含compose.yaml、compose.sh、.env。
目标服务器必须已有同架构后端Java21和前端基础镜像。脚本检测现场镜像并离线构建增量层，
不下载基础镜像，不包含JRE、数据库、Redis或流媒体镜像。两个架构使用相同的跨平台JAR和静态资源，
按各自架构校验主机、镜像与发布回滚标签。服务更新会短暂重启。

脚本会将项目原版 Nginx 模板更新为带地图缓存规则的版本，并备份原模板供回滚；
config/map 中的现场地图参数不修改。若 Nginx 模板已定制，脚本在修改镜像前停止，
请先由维护人员核对本包 config/wvp-web.conf.template 中的地图 location 与现场规则，
适配升级脚本后再升级，避免覆盖自定义代理配置。

## 数据库变化

后端启动时检查并在缺失时自动创建独立表wvp_preset_remark，保存平台预置位名称备注。
数据库账号需要CREATE权限；没有权限时由管理员先确认表是否存在，仅在缺表时执行
本包database/preset-remark.sql，再升级启动。该SQL是普通CREATE TABLE，不能重复执行。
既有设备表、.env和业务数据不覆盖；现场应按现有流程备份数据库。
回滚仅恢复前后端镜像，不删除新表与已保存名称备注。

## 升级

将tar.gz和同名.sha256置于同一目录，分别执行：

```sh
sha256sum -c {target.name}.sha256
tar -xzf {target.name}
cd {name}
./verify.sh
./upgrade.sh /opt/gb28181-h5
```

将/opt/gb28181-h5替换为实际部署目录。升级后检查后端启动日志、两个容器状态，
浏览器强制刷新；验收播放、云台stop、预置位查询/设置/调用/删除/改名。
设置会保存镜头当前位置，同号可能覆盖，验收请使用专用测试编号。
同时检查此前超时通道是否发起新点播、地图首次加载/切换页面/失败重试、点位显示和拖动跟随。

## 回滚

```sh
./rollback.sh /opt/gb28181-h5
```

保留升级前镜像以及部署目录.wvp-updates/{release_id}-{arch}/，回滚依赖这些记录。
回滚同时恢复升级前 Nginx 模板。不要删除该目录和备份镜像。
脚本检查容器running不等同于业务健康；仍需执行上述现场验收。

## 校验范围

后端{counts['tests']}项测试，失败{counts['failures']}、错误{counts['errors']}、跳过{counts['skipped']}；
前端{frontend['numPassedTests']}项测试通过；升级回滚模拟测试{upgrade_tests.group(1)}项通过。
前端生产构建、JAR构建、API文档、包内文件和压缩包校验完成。
未在真实Linux AMD64/ARM64服务器执行启动或升级验收；详情见BUILD_INFO.json。
''')
        files = sorted(p for p in package.rglob('*') if p.is_file())
        (package / 'SHA256SUMS').write_text(''.join(f'{digest(p)}  {p.relative_to(package).as_posix()}\n' for p in files))
        for script in package.glob('*.sh'):
            subprocess.run(['sh', '-n', str(script)], check=True)
        subprocess.run(['sh', str(package / 'verify.sh')], check=True, stdout=subprocess.DEVNULL)
        with tarfile.open(target, 'w:gz', compresslevel=6) as archive:
            archive.add(package, arcname=name)
        # Verify the delivered compressed payload by extracting and checking its own manifest.
        with tempfile.TemporaryDirectory(prefix='wvp-package-check-') as check:
            with tarfile.open(target) as archive:
                archive.extractall(check, filter='data')
            extracted = Path(check) / name
            subprocess.run(['sh', str(extracted / 'verify.sh')], check=True, stdout=subprocess.DEVNULL)
            require(digest(extracted / 'artifacts/wvp.jar') == digest(jar), '交付包JAR不一致')
        checksum = digest(target)
        Path(str(target) + '.sha256').write_text(f'{checksum}  {target.name}\n')
        outputs.append({'archive': str(target), 'bytes': target.stat().st_size, 'sha256': checksum})
        print('已校验：' + json.dumps(outputs[-1], ensure_ascii=False), flush=True)
    if args.clean_old:
        for path in old:
            require(path.parent == RELEASES and not path.is_symlink(), '拒绝清理目录外文件或软链接')
            if path.is_dir():
                shutil.rmtree(path)
            else:
                path.unlink()
        print(f'新包均校验通过，已删除{len(old)}个旧更新包/校验文件/解包目录。', flush=True)
    print(json.dumps({'commit': commit, 'packages': outputs, 'logs': str(logs)}, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()
