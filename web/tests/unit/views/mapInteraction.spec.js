jest.mock('@/views/common/DeviceTree.vue', () => ({}))
jest.mock('@/views/channel/player.vue', () => ({}))
jest.mock('@/views/dialog/commonChannelEditDialog.vue', () => ({}))
import Vue from 'vue'
import MapPage from '@/views/map/index.vue'
import HiMap from '@/components/HiMap/index.vue'

function context() {
  const page = Object.assign(MapPage.data(), MapPage.methods, { $refs: {} })
  MapPage.created.call(page)
  page.mapApp = { mapCoordToPixel: jest.fn(() => ({ x: 101, y: 202 })), removeMapChangeListener: jest.fn() }
  return page
}

describe('map interaction frames', () => {
  let frame
  beforeEach(() => {
    window.requestAnimationFrame = jest.fn(callback => { frame = callback; return 42 })
    window.cancelAnimationFrame = jest.fn()
  })
  it('coalesces rapid map movement into one frame and only updates the visible popup', () => {
    const page = context()
    page.infoBoxPoint = [114, 30]
    page.$refs.infoBox = { style: {} }
    for (let i = 0; i < 100; i++) page.scheduleBoxPosition()
    expect(window.requestAnimationFrame).toHaveBeenCalledTimes(1)
    expect(page.mapApp.mapCoordToPixel).not.toHaveBeenCalled()
    frame()
    expect(page.mapApp.mapCoordToPixel).toHaveBeenCalledTimes(1)
    expect(page.$refs.infoBox.style.transform).toContain('translate3d(101px, 190px, 0)')
    expect(page.infoBoxPixel).toBe(null) // No reactive position writes / whole-page rendering during drag.
    expect(page.boxFrame).toBe(null)
  })
  it('does no projection work while all popups are closed', () => {
    const page = context()
    page.scheduleBoxPosition()
    expect(window.requestAnimationFrame).not.toHaveBeenCalled()
    expect(page.mapApp.mapCoordToPixel).not.toHaveBeenCalled()
  })
  it('cancels queued work when leaving or replacing the map', () => {
    const page = context()
    page.infoBoxPoint = [114, 30]
    page.mapChangeHandler = () => {}
    page.scheduleBoxPosition()
    const oldHandler = page.mapChangeHandler
    page.detachMapListener()
    expect(window.cancelAnimationFrame).toHaveBeenCalledWith(42)
    expect(page.mapApp.removeMapChangeListener).toHaveBeenCalledWith(oldHandler)
    expect(page.boxFrame).toBe(null)
  })
  it('keeps SDK instances and frozen point arrays outside deep Vue observation', () => {
    for (const component of [HiMap, MapPage]) {
      const instance = new Vue({ data: component.data, created: component.created })
      const sdk = { map: {}, layer: { points: [] } }
      instance.mapApp = sdk
      expect(sdk.__ob__).toBeUndefined()
      expect(sdk.layer.__ob__).toBeUndefined()
      if (component === MapPage) {
        const camera = { gbId: 1 }
        instance.channels = Object.freeze([camera])
        expect(instance.channels.__ob__).toBeUndefined()
        expect(camera.__ob__).toBeUndefined()
      }
      instance.$destroy()
    }
  })
})
