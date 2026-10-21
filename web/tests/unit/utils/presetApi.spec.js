import request from '@/utils/request'
import * as deviceApi from '@/api/frontEnd'
import * as channelApi from '@/api/commonChannel'
import deviceStore from '@/store/modules/frontEnd'
import channelStore from '@/store/modules/commonChanel'

jest.mock('@/utils/request', () => jest.fn(() => Promise.resolve({ code: 0 })))

describe('preset controls', () => {
  beforeEach(() => request.mockClear())

  it('submits numeric IDs for aliases at all device and common channel endpoints', async() => {
    for (const action of ['addPreset', 'callPreset', 'deletePreset']) {
      await deviceApi[action](['device', 'channel', 'PresetID_5'])
      expect(request.mock.calls[request.mock.calls.length - 1][0].params.presetId).toBe(5)
      await channelApi[action]({ channelId: 123, presetId: 'PresetID_5', presetName: '' })
      expect(request.mock.calls[request.mock.calls.length - 1][0].params.presetId).toBe(5)
    }
    expect(request).toHaveBeenCalledTimes(6)
    request.mock.calls.forEach(([config]) => expect(config.retryOnNetworkError).toBe(false))
  })

  it('submits optional platform names and edits names without invoking preset control', async() => {
    await deviceApi.addPreset(['device', 'channel', 5, '东门入口'])
    expect(request.mock.calls[0][0].params.presetName).toBe('东门入口')
    await channelApi.addPreset({ channelId: 123, presetId: 5, presetName: '路口北侧' })
    expect(request.mock.calls[1][0].params.presetName).toBe('路口北侧')
    await deviceStore.actions.setPresetRemark({}, ['device', 'channel', 5, '大门'])
    expect(request.mock.calls[2][0]).toEqual({
      method: 'post', url: '/api/front-end/preset/remark/device/channel', params: { presetId: 5, presetName: '大门' }
    })
    await channelStore.actions.setPresetRemark({}, { channelId: 123, presetId: 5, presetName: '' })
    expect(request.mock.calls[3][0]).toEqual({
      method: 'post', url: '/api/common/channel/front-end/preset/remark', params: { channelId: 123, presetId: 5, presetName: '' }
    })
  })

  it('blocks malformed or out of range IDs before sending a control request', async() => {
    for (const action of ['addPreset', 'callPreset', 'deletePreset']) {
      await expect(deviceApi[action](['device', 'channel', 'PresetID_256'])).rejects.toThrow()
      await expect(channelApi[action]({ channelId: 123, presetId: 'PresetName_5' })).rejects.toThrow()
    }
    expect(request).not.toHaveBeenCalled()
  })

  it('round trips an old query response through the store into a numeric control request', async() => {
    const response = { data: [{ presetId: 'PresetID_5', presetName: 'PresetName_5' }] }
    request.mockResolvedValueOnce(response)
    const devicePresets = await deviceStore.actions.queryPreset({}, ['device', 'channel'])
    expect(devicePresets[0]).toEqual({ presetId: '5', presetName: 'PresetName_5' })
    await deviceStore.actions.callPreset({}, ['device', 'channel', devicePresets[0].presetId])
    expect(request.mock.calls[1][0].params.presetId).toBe(5)

    request.mockResolvedValueOnce(response)
    const channelPresets = await channelStore.actions.queryPreset({}, 123)
    await channelStore.actions.deletePreset({}, { channelId: 123, presetId: channelPresets[0].presetId })
    expect(request.mock.calls[3][0].params.presetId).toBe(5)
    expect(response.data[0].presetId).toBe('PresetID_5')
  })
})
