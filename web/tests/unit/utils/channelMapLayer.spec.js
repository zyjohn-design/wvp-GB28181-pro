import { createChannelMapLayer } from '@/utils/channel-map-layer'

const camera = { gbId: 1, gbName: '东门', gbLongitude: 114, gbLatitude: 30, gbStatus: 'ON' }
function context(canvas = true) {
  const layer = { setData: jest.fn(), addClickListener: jest.fn() }
  const map = { addVectorLayer: jest.fn(), removeVectorLayer: jest.fn(), freshVectorLayer: jest.fn() }
  if (canvas) Object.assign(map, { addCanvasSymbolLayer: jest.fn(() => layer), removeCanvasSymbolLayer: jest.fn() })
  const clicked = jest.fn()
  return { layer, map, clicked, adapter: createChannelMapLayer(map, { iconBase: '/static/images/gis/', onClick: clicked }) }
}

describe('camera layer rendering', () => {
  afterEach(() => jest.restoreAllMocks())
  it('batch draws thousands of points in one engine layer with no DOM marker creation', () => {
    const { adapter, map, layer } = context()
    const cameras = Array.from({ length: 5000 }, (_, id) => ({ ...camera, gbId: id }))
    adapter.update(cameras)
    expect(map.addCanvasSymbolLayer).toHaveBeenCalledTimes(1)
    expect(map.addCanvasSymbolLayer.mock.calls[0][0]).toMatchObject({ cluster: false, showText: false })
    expect(layer.setData).toHaveBeenCalledTimes(1)
    const features = layer.setData.mock.calls[0][0]
    expect(features).toHaveLength(5000)
    expect(features[0].geometry.coordinates).toEqual([114, 30])
    expect(features[0].properties).toMatchObject({ icon: 'camera1', iconwidth: 28, iconheight: 32 })
    expect(features[0].properties.channel).toBeUndefined()
    expect(map.addVectorLayer).not.toHaveBeenCalled()
  })
  it('reuses the same layer when coordinates or online state changes', () => {
    const { adapter, map, layer } = context()
    adapter.update([camera])
    adapter.update([{ ...camera, gbStatus: 'OFF', gbLongitude: 115 }])
    expect(map.addCanvasSymbolLayer).toHaveBeenCalledTimes(1)
    expect(layer.addClickListener).toHaveBeenCalledTimes(1)
    expect(layer.setData.mock.calls[1][0][0]).toMatchObject({
      geometry: { coordinates: [115, 30] }, properties: { icon: 'camera1-offline' }
    })
  })
  it('does not redraw unchanged points but clicks use fresh channel metadata', () => {
    const { adapter, layer, clicked } = context()
    adapter.update([camera])
    const updated = { ...camera, gbAddress: '新的安装地址' }
    for (let i = 0; i < 10; i++) adapter.update([updated])
    expect(layer.setData).toHaveBeenCalledTimes(1)
    layer.addClickListener.mock.calls[0][0]([{ properties: { id: '1' } }])
    expect(clicked).toHaveBeenCalledWith(updated)
  })
  it('clears points and releases the layer once on disposal', () => {
    const { adapter, layer, map, clicked } = context()
    adapter.update([camera])
    adapter.update([])
    expect(layer.setData).toHaveBeenLastCalledWith([])
    adapter.dispose()
    adapter.dispose()
    expect(map.removeCanvasSymbolLayer).toHaveBeenCalledTimes(1)
    layer.addClickListener.mock.calls[0][0]([{ properties: { id: 1 } }])
    expect(clicked).not.toHaveBeenCalled()
  })
  it('falls back for an older SDK, reuses vector data and handles SDK click payloads', () => {
    const { adapter, map, clicked } = context(false)
    adapter.update([camera])
    const config = map.addVectorLayer.mock.calls[0][0]
    expect(config).toMatchObject({ centable: false, isOverlap: false })
    config.clicklistener({ marker: { params: { id: 1 } } })
    expect(clicked).toHaveBeenCalledWith(camera)
    adapter.update([{ ...camera, gbStatus: 'OFF' }])
    expect(map.freshVectorLayer).toHaveBeenCalledTimes(1)
    expect(map.addVectorLayer).toHaveBeenCalledTimes(1)
    adapter.dispose()
    expect(map.removeVectorLayer).toHaveBeenCalledTimes(1)
  })
  it('cleans an unsupported canvas handle before falling back', () => {
    jest.spyOn(console, 'warn').mockImplementation(() => {})
    const { adapter, map, layer } = context()
    layer.setData.mockImplementation(() => { throw new Error('unsupported engine') })
    adapter.update([camera])
    expect(map.removeCanvasSymbolLayer).toHaveBeenCalledWith(layer)
    expect(map.addVectorLayer).toHaveBeenCalledTimes(1)
  })
  it('does not build a layer for an empty result and ignores unknown clicked IDs', () => {
    const { adapter, map, layer, clicked } = context()
    adapter.update([])
    expect(map.addCanvasSymbolLayer).not.toHaveBeenCalled()
    adapter.update([camera])
    layer.addClickListener.mock.calls[0][0]([{ properties: { id: 999 } }])
    expect(clicked).not.toHaveBeenCalled()
  })
})
