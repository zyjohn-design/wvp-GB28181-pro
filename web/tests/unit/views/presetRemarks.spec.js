import DevicePresets from '@/views/device/common/ptzPresetConfig.vue'
import ChannelPresets from '@/views/channel/common/ptzPresetConfig.vue'

async function flush() {
  for (let i = 0; i < 10; i++) await Promise.resolve()
}

for (const [component, namespace] of [[DevicePresets, 'frontEnd'], [ChannelPresets, 'commonChanel']]) {
  describe(namespace + ' preset remarks', () => {
    function context() {
      return Object.assign(component.data(), component.methods, {
        deviceId: 'device', channelDeviceId: 'channel', channelId: '123',
        $store: { dispatch: jest.fn(() => Promise.resolve([])) },
        $message: jest.fn(),
        $prompt: jest.fn(() => Promise.resolve({ value: '路口北侧' }))
      })
    }

    it('submits the entered name and preserves it after a failed add', async() => {
      const vm = context()
      vm.openAdd()
      vm.addPresetName = '东门入口'
      vm.$store.dispatch.mockRejectedValueOnce('设备拒绝')
      vm.confirmAdd()
      await flush()
      expect(vm.showAddForm).toBe(true)
      expect(vm.addPresetName).toBe('东门入口')
      const params = namespace === 'frontEnd' ? ['device', 'channel', 1, '东门入口']
        : { channelId: '123', presetId: 1, presetName: '东门入口' }
      expect(vm.$store.dispatch).toHaveBeenCalledWith(namespace + '/addPreset', params)
      vm.cancelAdd()
      vm.openAdd()
      expect(vm.addPresetName).toBe('')
    })

    it('edits only platform metadata and refreshes displayed names', async() => {
      const vm = context()
      vm.editPresetName({ presetId: 'PresetID_5', presetName: '旧名称' })
      await flush()
      const params = namespace === 'frontEnd' ? ['device', 'channel', 5, '路口北侧']
        : { channelId: '123', presetId: 5, presetName: '路口北侧' }
      expect(vm.$store.dispatch).toHaveBeenNthCalledWith(1, namespace + '/setPresetRemark', params)
      expect(vm.$store.dispatch.mock.calls.map(([action]) => action)).toEqual([
        namespace + '/setPresetRemark', namespace + '/queryPreset'
      ])
      expect(vm.$message).toHaveBeenCalledWith({ showClose: true, message: '名称备注已保存', type: 'success' })
    })
  })
}
