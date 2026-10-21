/* 实际解析已发布入口并加载资源，验证 document.write 脚本顺序。
 * SDK 由本地桩代替；此测试不访问现场地图服务。运行：node tests/map-entry-smoke.js
 */
const http = require('http')
const fs = require('fs')
const path = require('path')
const assert = require('assert')
const { JSDOM, VirtualConsole } = require('jsdom')
const publicRoot = path.resolve(__dirname, '../public')
let delayed = false
const server = http.createServer((request, response) => {
  const pathname = new URL(request.url, 'http://localhost').pathname
  response.setHeader('Content-Type', pathname.endsWith('.html') ? 'text/html' : pathname.endsWith('.css') ? 'text/css' : 'application/javascript')
  if (pathname.includes('himap-config-')) {
    return response.end(`var HOSTNAME='http://127.0.0.1:${server.address().port}/sdk/';var gkmapHOSTNAME=HOSTNAME;var CENTERPOINT='114,30';var INITLEVEL=4;`)
  }
  if (pathname === '/sdk/himap.js') {
    return response.end(`
      var script=Array.from(document.getElementsByTagName('script')).find(s=>s.src.endsWith('/himap.js'));
      if(!script||script.getAttribute('maptype')!=='minemap'||script.getAttribute('onready')!=='mapready')throw Error('legacy SDK script URL recognition failed');
      window.HiMapConfig={};window.HiMap=function(el){
      if(!el)throw Error('no DOM');if(!window.TrackPlay)throw Error('extension race');
      window.initCount=(window.initCount||0)+1;this.map={loaded:true};
    };${delayed ? 'setTimeout(mapready,5);' : 'mapready();'}`)
  }
  if (pathname.endsWith('himap_gkpt.js')) return response.end('window.TrackPlay=function(){};window.extendHiMap=function(){};')
  if (pathname.endsWith('.css')) return response.end('')
  const file = path.join(publicRoot, pathname)
  if (fs.existsSync(file)) return response.end(fs.readFileSync(file))
  response.statusCode = 404
  response.end('missing')
})

async function run() {
  await new Promise(resolve => server.listen(0, '127.0.0.1', resolve))
  try {
    for (const theme of ['arcgis', 'arcgis-dark']) {
      for (const delay of [false, true]) {
        delayed = delay
        const errors = []
        const console = new VirtualConsole()
        console.on('jsdomError', error => errors.push(error.message))
        const dom = await JSDOM.fromURL(`http://127.0.0.1:${server.address().port}/map-entry/${theme}.html?v=smoke`, {
          resources: 'usable', runScripts: 'dangerously', virtualConsole: console
        })
        try {
          for (let i = 0; i < 100 && !dom.window._MapApp; i++) await new Promise(resolve => setTimeout(resolve, 10))
          assert(dom.window._MapApp, `${theme} missing map: ${JSON.stringify(dom.window._MapLoadState)}; ${errors.join(';')}`)
          assert.strictEqual(dom.window.initCount, 1)
          assert.strictEqual(dom.window._MapLoadState.error, false)
          assert.deepStrictEqual(errors, [])
          process.stdout.write(`${theme}: ${delay ? 'delayed' : 'immediate'} SDK callback passed\n`)
        } finally {
          dom.window.close()
        }
      }
    }
  } finally {
    server.close()
  }
}
run().catch(error => {
  process.stderr.write(error.stack + '\n')
  process.exitCode = 1
})
