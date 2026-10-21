jest.mock('@/views/common/DeviceTree.vue', () => ({}))
jest.mock('@/views/channel/player.vue', () => ({}))
jest.mock('@/views/dialog/commonChannelEditDialog.vue', () => ({}))
import MapPage from '@/views/map/index.vue'

function context() {
  const mapApp = { removeVectorLayer: jest.fn(), addVectorLayer: jest.fn(), removeMapChangeListener: jest.fn() }
  const component = Object.assign(MapPage.data(), MapPage.methods, {
    mapApp, mapReady: true, $store: { dispatch: jest.fn() }, $nextTick: callback => callback()
  })
  MapPage.created.call(component)
  component.mapApp = mapApp
  return component
}
const camera = { gbId: 1, gbName: '东门', gbLongitude: 114, gbLatitude: 30, gbStatus: 'ON' }

describe('camera map layer recovery', () => {
  beforeEach(() => jest.spyOn(console, 'error').mockImplementation(() => {}))
  afterEach(() => jest.restoreAllMocks())
  it('retries a failed point request without reloading the basemap', async() => {
    const page = context()
    page.$store.dispatch.mockRejectedValueOnce(new Error('timeout')).mockResolvedValueOnce([camera])
    const originalMap = page.mapApp
    await page.loadChannels()
    expect(page.channelLoadError).toContain('加载失败')
    expect(page.channelLoading).toBe(false)
    await page.loadChannels()
    expect(page.channelLoadError).toBe('')
    expect(page.mapApp).toBe(originalMap)
    expect(originalMap.addVectorLayer.mock.calls[0][0].data[0].id).toBe(camera.gbId)
  })
  it('ignores a late point request from the previous basemap', async() => {
    const page = context()
    let resolve
    page.$store.dispatch.mockImplementationOnce(() => new Promise(done => { resolve = done }))
    const request = page.loadChannels()
    const previousMap = page.mapApp
    page.onMapLoading()
    resolve([camera])
    await request
    expect(previousMap.addVectorLayer).not.toHaveBeenCalled()
    expect(page.channels).toEqual([])
    expect(page.mapReady).toBe(false)
  })
  it('retains error recovery when vector drawing throws', async() => {
    const page = context()
    page.$store.dispatch.mockResolvedValue([camera])
    page.mapApp.addVectorLayer.mockImplementationOnce(() => { throw new Error('layer not ready') })
    await page.loadChannels()
    expect(page.channelLoadError).toContain('加载失败')
    await page.loadChannels()
    expect(page.channelLoadError).toBe('')
  })
  it('refreshes points on activation and ignores earlier requests', async() => {
    const page = context()
    let resolve
    page.$store.dispatch.mockImplementationOnce(() => new Promise(done => { resolve = done })).mockResolvedValueOnce([camera])
    const stale = page.loadChannels()
    MapPage.activated.call(page)
    await Promise.resolve()
    await Promise.resolve()
    resolve([])
    await stale
    expect(page.channels).toEqual([camera])
  })
  it('shows empty coordinate results separately from request errors', async() => {
    const page = context()
    page.$store.dispatch.mockResolvedValue([{ gbLongitude: 0, gbLatitude: 0 }])
    await page.loadChannels()
    expect(page.channels).toEqual([])
    expect(page.channelLoadError).toBe('')
    expect(page.mapApp.addVectorLayer).not.toHaveBeenCalled()
  })
  it('filters invalid coordinates before passing them to the map engine', async() => {
    const page = context()
    page.$store.dispatch.mockResolvedValue([camera,
      { gbLongitude: 999, gbLatitude: 30 }, { gbLongitude: 114, gbLatitude: 999 },
      { gbLongitude: Infinity, gbLatitude: 30 }, { gbLongitude: 'bad', gbLatitude: 30 }])
    await page.loadChannels()
    expect(page.channels).toEqual([camera])
  })
  it('still loads points if a broken old map listener cannot be removed', async() => {
    const page = context()
    jest.spyOn(console, 'warn').mockImplementation(() => {})
    page.mapChangeHandler = () => {}
    page.mapApp.removeMapChangeListener.mockImplementation(() => { throw new Error('old map destroyed') })
    page.$store.dispatch.mockResolvedValue([camera])
    const nextMap = { removeVectorLayer: jest.fn(), addVectorLayer: jest.fn() }
    page.onMapReady(nextMap)
    await Promise.resolve()
    await Promise.resolve()
    expect(nextMap.addVectorLayer).toHaveBeenCalled()
  })
})
