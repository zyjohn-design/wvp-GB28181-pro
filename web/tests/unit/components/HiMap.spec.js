import HiMap from '@/components/HiMap/index.vue'
import { shallowMount } from '@vue/test-utils'

function context() {
  const vm = Object.assign(HiMap.data(), HiMap.methods, {
    maptype: 'arcgis', initMapObj: true, pollInterval: 500, readyTimeout: 20000, loadGracePeriod: 3000,
    $emit: jest.fn(), $nextTick: callback => Promise.resolve().then(callback)
  })
  HiMap.created.call(vm)
  vm.$refs = { frame: { contentWindow: {
    location: { search: '?v=' + vm.loadToken }, document: {}
  } } }
  Object.defineProperty(vm, 'iframeSrc', { get: () => HiMap.computed.iframeSrc.call(vm) })
  return vm
}

describe('HiMap loading recovery', () => {
  beforeEach(() => {
    jest.useFakeTimers()
    jest.spyOn(console, 'error').mockImplementation(() => {})
  })
  afterEach(() => {
    jest.clearAllTimers()
    jest.useRealTimers()
    jest.restoreAllMocks()
    delete window.MAP_OBJ
    delete window.CONTENT_DOCUMENT
  })

  it('does not treat an unready map as ready when the old grace period expires', () => {
    const vm = context()
    let now = 1000
    jest.spyOn(Date, 'now').mockImplementation(() => now)
    vm.$refs.frame.contentWindow._MapApp = { map: { _loaded: false } }
    vm.startPolling()
    now = 5000
    vm.checkMapApp()
    expect(vm.ready).toBe(false)
    expect(vm.$emit).toHaveBeenCalledWith('loadwarning')
    expect(vm.$emit.mock.calls.some(([event]) => event === 'ready')).toBe(false)
    vm.$refs.frame.contentWindow._MapApp.map._loaded = true
    vm.checkMapApp()
    expect(vm.ready).toBe(true)
  })

  it('recognizes the ArcGIS boolean loaded signal and emits ready only once', () => {
    const vm = context()
    vm.$refs.frame.contentWindow._MapApp = { map: { loaded: true } }
    vm.startPolling()
    vm.onFrameLoad()
    vm.checkMapApp()
    expect(vm.ready).toBe(true)
    expect(vm.$emit.mock.calls.filter(([event]) => event === 'ready')).toHaveLength(1)
    expect(window.MAP_OBJ).toBe(vm.mapApp)
  })

  it('reloads the same theme with a new iframe node and URL', async() => {
    const wrapper = shallowMount(HiMap)
    const oldFrame = wrapper.find('iframe').element
    const oldUrl = oldFrame.getAttribute('src')
    wrapper.vm.reload()
    await wrapper.vm.$nextTick()
    expect(wrapper.find('iframe').element).not.toBe(oldFrame)
    expect(wrapper.find('iframe').attributes('src')).not.toBe(oldUrl)
    expect(wrapper.find('iframe').attributes('src')).toContain('/map-entry/arcgis.html?v=')
    wrapper.destroy()
  })

  it('rejects the previous iframe instance while navigation is in progress', async() => {
    const vm = context()
    vm.$refs.frame.contentWindow._MapApp = { map: { loaded: true } }
    const oldToken = vm.loadToken
    vm.reload()
    await Promise.resolve()
    expect(vm.loadToken).not.toBe(oldToken)
    vm.checkMapApp()
    expect(vm.ready).toBe(false)
  })

  it('keeps the original timeout when a late iframe load event arrives', () => {
    const vm = context()
    vm.startPolling()
    jest.advanceTimersByTime(19000)
    vm.onFrameLoad()
    jest.advanceTimersByTime(1000)
    expect(vm.errorMessage).toContain('超时')
  })

  it('retries at most twice and leaves a manual recovery after repeated errors', async() => {
    const vm = context()
    for (let i = 0; i < 3; i++) {
      vm.failLoading('service unavailable')
      jest.advanceTimersByTime(1500)
      await Promise.resolve()
    }
    expect(vm.retryCount).toBe(2)
    expect(vm.errorMessage).toBe('service unavailable')
    expect(vm.retryTimer).toBe(null)
    vm.reload()
    expect(vm.retryCount).toBe(0)
    expect(vm.errorMessage).toBe('')
  })

  it('shows resource failure and clears only the global instance it owns', () => {
    const vm = context()
    vm.$refs.frame.contentWindow._MapLoadState = { error: true, message: '地图配置加载失败' }
    vm.checkMapApp()
    expect(vm.errorMessage).toBe('地图配置加载失败')
    window.MAP_OBJ = { other: true }
    vm.clearMapApp()
    expect(window.MAP_OBJ).toEqual({ other: true })
  })

  it('clears pending retry timers on destroy and recovers a cached failed page on activation', () => {
    const vm = context()
    vm.failLoading('failed')
    HiMap.deactivated.call(vm)
    expect(vm.retryTimer).toBe(null)
    HiMap.activated.call(vm)
    expect(vm.errorMessage).toBe('')
    HiMap.beforeDestroy.call(vm)
    jest.advanceTimersByTime(30000)
    expect(vm.disposed).toBe(true)
    expect(vm.timer).toBe(null)
  })
})
