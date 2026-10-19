"""Exercise the real upgrade/rollback scripts with a simulated Docker installation."""

import json
import os
from pathlib import Path
import shutil
import subprocess
import tempfile
import unittest


TEMPLATES = Path(__file__).resolve().parents[1] / 'update-package-templates/device-register-status'


class IncrementalUpdateTest(unittest.TestCase):
    def setUp(self):
        self.temp = tempfile.TemporaryDirectory()
        self.addCleanup(self.temp.cleanup)
        self.root = Path(self.temp.name)
        self.deploy = self.root / 'deployment'
        self.package = self.root / 'package'
        self.bin = self.root / 'bin'
        for directory in (self.deploy, self.package, self.bin):
            directory.mkdir()
        self.backend = 'registry.example:5000/custom/backend:v1.0.0-amd64'
        self.web = 'registry.example:5000/custom/web:v1.0.0-amd64'
        self.images = self.root / 'images.json'
        self.images.write_text(json.dumps({self.backend: ['original-backend', 'linux/amd64'],
                                           self.web: ['original-web', 'linux/amd64']}))
        self.log = self.root / 'commands.jsonl'
        self.write_config()
        (self.deploy / '.env').write_text('FIELD_CONFIG=preserve\n')
        values = {'__TARGET_ARCH__': 'amd64', '__RELEASE_ID__': 'test-release',
                  '__BACKEND_RELEASE__': 'local/backend:release', '__WEB_RELEASE__': 'local/web:release',
                  '__BACKEND_BACKUP__': 'local/backend:backup', '__WEB_BACKUP__': 'local/web:backup'}
        for name in ('upgrade', 'rollback'):
            source = (TEMPLATES / f'{name}.sh.in').read_text()
            for key, value in values.items():
                source = source.replace(key, value)
            self.executable(self.package / f'{name}.sh', source)
        shutil.copyfile(TEMPLATES / 'update-common.sh', self.package / 'update-common.sh')
        self.executable(self.package / 'verify.sh', '#!/bin/sh\nexit 0\n')
        self.executable(self.bin / 'uname', '#!/bin/sh\necho x86_64\n')
        self.executable(self.deploy / 'compose.sh', '''#!/usr/bin/env python3
import json, os, sys
from pathlib import Path
with open(os.environ['MOCK_LOG'], 'a') as f: f.write(json.dumps(['compose']+sys.argv[1:])+'\\n')
if sys.argv[1]=='config': print(Path(__file__).with_name('compose.yaml').read_text())
elif sys.argv[1]=='ps' and '-q' in sys.argv: print('container-'+sys.argv[-1])
''')
        self.executable(self.bin / 'docker', '''#!/usr/bin/env python3
import json, os, sys
from pathlib import Path
args=sys.argv[1:]
with open(os.environ['MOCK_LOG'], 'a') as f: f.write(json.dumps(['docker']+args)+'\\n')
path=Path(os.environ['MOCK_IMAGES']); images=json.loads(path.read_text())
if args[:2]==['image','inspect']:
    if args[-1] not in images: sys.exit(1)
    if '-f' in args: print(images[args[-1]][1])
elif args[0]=='tag': images[args[2]]=images[args[1]]
elif args[0]=='build':
    target=args[args.index('-t')+1]
    if os.environ.get('FAIL_WEB_BUILD') and target=='local/web:release': sys.exit(1)
    images[target]=['built-'+target, 'linux/amd64']
elif args[0]=='inspect': print('true')
else: sys.exit(2)
path.write_text(json.dumps(images))
''')
        self.env = {**os.environ, 'PATH': str(self.bin) + os.pathsep + os.environ['PATH'],
                    'MOCK_IMAGES': str(self.images), 'MOCK_LOG': str(self.log)}

    @staticmethod
    def executable(path, content):
        path.write_text(content)
        path.chmod(0o755)

    def write_config(self, web=True):
        # Reversed service ordering, unrelated images and quoted references.
        config = "services:\n  unrelated:\n    image: local/gb28181-wvp:wrong\n"
        if web:
            config += f'  polaris-wvp-web:\n    image: "{self.web}"\n'
        config += f"  polaris-wvp:\n    image: '{self.backend}'\nnetworks:\n  default: {{}}\n"
        (self.deploy / 'compose.yaml').write_text(config)

    def run_script(self, name, success=True):
        result = subprocess.run(['sh', str(self.package / f'{name}.sh'), str(self.deploy)],
                                env=self.env, capture_output=True, text=True)
        self.assertEqual(result.returncode == 0, success, result.stdout + result.stderr)
        return result

    def commands(self):
        return [json.loads(line) for line in self.log.read_text().splitlines()]

    def assert_no_mutation(self):
        self.assertFalse(any(c[1] in ('tag', 'build', 'up') for c in self.commands()))

    def test_versioned_images_upgrade_and_rollback_with_original_backup(self):
        config = (self.deploy / 'compose.yaml').read_bytes()
        self.run_script('upgrade')
        self.run_script('upgrade')  # A retry must preserve the first backup.
        images = json.loads(self.images.read_text())
        self.assertEqual(images['local/backend:backup'][0], 'original-backend')
        self.assertEqual(images[self.backend][0], 'built-local/backend:release')
        builds = [c for c in self.commands() if c[1] == 'build']
        self.assertTrue(all('--platform=linux/amd64' in c and '--pull=false' in c and '--network=none' in c for c in builds))
        self.assertTrue(all('--no-deps' in c and c[-2:] == ['polaris-wvp', 'polaris-wvp-web']
                            for c in self.commands() if c[1] == 'up'))
        self.run_script('rollback')
        images = json.loads(self.images.read_text())
        self.assertEqual(images[self.backend][0], 'original-backend')
        self.assertEqual(images[self.web][0], 'original-web')
        self.assertEqual((self.deploy / 'compose.yaml').read_bytes(), config)
        self.assertEqual((self.deploy / '.env').read_text(), 'FIELD_CONFIG=preserve\n')

    def test_wrong_architecture_stops_before_changes(self):
        images = json.loads(self.images.read_text())
        images[self.web][1] = 'linux/arm64'
        self.images.write_text(json.dumps(images))
        self.run_script('upgrade', False)
        self.assert_no_mutation()

    def test_missing_local_image_stops_before_changes(self):
        self.images.write_text(json.dumps({self.backend: ['original-backend', 'linux/amd64']}))
        self.run_script('upgrade', False)
        self.assert_no_mutation()

    def test_missing_service_stops_before_changes(self):
        self.write_config(web=False)
        self.run_script('upgrade', False)
        self.assert_no_mutation()

    def test_digest_reference_stops_before_changes(self):
        self.web += '@sha256:abcd'
        self.write_config()
        self.run_script('upgrade', False)
        self.assert_no_mutation()

    def test_failed_second_build_leaves_current_tags_unchanged(self):
        self.env['FAIL_WEB_BUILD'] = '1'
        self.run_script('upgrade', False)
        images = json.loads(self.images.read_text())
        self.assertEqual(images[self.backend][0], 'original-backend')
        self.assertEqual(images[self.web][0], 'original-web')
        self.assertFalse(any(c[1] == 'up' for c in self.commands()))

    def test_changed_compose_prevents_rollback_to_wrong_tags(self):
        self.run_script('upgrade')
        self.web = 'other/web:changed'
        self.write_config()
        before = self.images.read_bytes()
        self.run_script('rollback', False)
        self.assertEqual(self.images.read_bytes(), before)


if __name__ == '__main__':
    unittest.main()
