import fs from 'fs'
import path from 'path'
import vm from 'vm'
import { URLSearchParams } from 'url'

const source = fs.readFileSync(path.resolve(__dirname, '../../../public/map-bootstrap.js'), 'utf8')
function environment() {
  const writes = []
  const box = {
    URLSearchParams, location: { search: '?v=retry-123' },
    console: { error: jest.fn(), warn: jest.fn() },
    document: { getElementById: jest.fn(() => ({ id: 'map' })), write: html => writes.push(html) },
    HiMap: jest.fn(() => ({ map: { loaded: true } })),
    HiMapConfig: {}, HOSTNAME: 'http://maps.test/HiMap/', CENTERPOINT: '114,30', INITLEVEL: 4
  }
  box.window = box
  vm.runInNewContext(source, box)
  return { box, writes }
}

describe('map bootstrap dependency ordering', () => {
  it('handles an immediate SDK callback from a warm cache without racing the extension', () => {
    const { box, writes } = environment()
    box.HiMapBootstrap.loadEngine({ pitch: 45 })
    box.mapready()
    expect(box.HiMap).not.toHaveBeenCalled()
    box.extendHiMap = jest.fn(() => expect(box._MapApp).toBeDefined())
    box.TrackPlay = function () {}
    box.HiMapBootstrap.scriptsLoaded()
    box.mapready()
    expect(box.HiMap).toHaveBeenCalledTimes(1)
    expect(box.HiMap.mock.calls[0][1].pitch).toBe(45)
    expect(box._MapApp.TrackPlay).toBe(box.TrackPlay)
    expect(writes[0]).toContain('src="http://maps.test/HiMap/himap.js"')
    expect(writes[0]).toContain('maptype="minemap" onready="mapready"')
    expect(writes.slice(1).every(html => html.includes('_mapv=retry-123'))).toBe(true)
    expect(box._MapLoadState.error).toBe(false)
  })
  it('supports an asynchronous SDK callback after scripts finished', () => {
    const { box } = environment()
    box.HiMapBootstrap.loadEngine()
    box.HiMapBootstrap.scriptsLoaded()
    expect(box.HiMap).not.toHaveBeenCalled()
    box.mapready()
    expect(box.HiMap).toHaveBeenCalledTimes(1)
  })
  it('reports config and initialization errors without publishing a broken instance', () => {
    const { box } = environment()
    delete box.HOSTNAME
    box.HiMapBootstrap.loadEngine()
    expect(box._MapLoadState.message).toContain('HOSTNAME')
    const second = environment().box
    second.HiMap.mockImplementation(() => { throw new Error('WebGL unavailable') })
    second.mapready()
    second.HiMapBootstrap.scriptsLoaded()
    expect(second._MapLoadState.message).toContain('WebGL unavailable')
    expect(second._MapApp).toBeUndefined()
  })
  it.each(['arcgis', 'arcgis-dark'])('%s entry puts DOM before startup and uses mounted config', theme => {
    const html = fs.readFileSync(path.resolve(__dirname, '../../../public/map-entry/' + theme + '.html'), 'utf8')
    expect(html.indexOf('id="map"')).toBeLessThan(html.indexOf('map-bootstrap.js'))
    expect(html).toContain('../map/himap-config-')
    expect(html.indexOf('himap-config-')).toBeLessThan(html.indexOf('map-bootstrap.js'))
    expect(html).toContain('data-himap-autostart="true"')
    const { box } = environment()
    const scripts = [...html.matchAll(/<script>([\s\S]*?)<\/script>/g)]
    scripts.forEach(([, script]) => vm.runInNewContext(script, box))
    expect(box._MapLoadState.error).toBe(false)
  })
})
