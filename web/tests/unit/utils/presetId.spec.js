import { getNextAvailablePresetId, parsePresetId } from '@/utils/presetId'

describe('preset ID validation', () => {
  it('accepts only whole numbers from 1 to 255', () => {
    expect(parsePresetId(1)).toBe(1)
    expect(parsePresetId('255')).toBe(255)
    for (const value of [null, '', 0, 256, 1.5, '01', 'PresetID_1', NaN]) {
      expect(parsePresetId(value)).toBeNull()
    }
  })

  it('finds a free ID only when the device list has valid IDs', () => {
    expect(getNextAvailablePresetId([{ presetId: '1' }, { presetId: '3' }])).toBe(2)
    expect(getNextAvailablePresetId([{ presetId: 'PresetID_1' }])).toBeNull()
    expect(getNextAvailablePresetId(Array.from({ length: 255 }, (_, i) => ({ presetId: i + 1 })))).toBeNull()
  })
})
