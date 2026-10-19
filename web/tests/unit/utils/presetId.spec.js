import { getNextAvailablePresetId, normalizePresetList, parsePresetId } from '@/utils/presetId'

describe('preset ID validation', () => {
  it('accepts only whole numbers from 1 to 255', () => {
    expect(parsePresetId(1)).toBe(1)
    expect(parsePresetId('255')).toBe(255)
    expect(parsePresetId('PresetID_5')).toBe(5)
    expect(parsePresetId('PresetID_255')).toBe(255)
    for (const value of [null, '', 0, 256, 1.5, '01', 'PresetID_0', 'PresetID_256', 'PresetID_05', 'PresetID_5\n', 'PresetName_5', 'abc5', '5x', true, NaN]) {
      expect(parsePresetId(value)).toBeNull()
    }
  })

  it('finds a free ID only when the device list has valid IDs', () => {
    expect(getNextAvailablePresetId([{ presetId: '1' }, { presetId: '3' }])).toBe(2)
    expect(getNextAvailablePresetId([{ presetId: 'PresetID_1' }])).toBe(2)
    expect(getNextAvailablePresetId([{ presetId: 'unknown' }])).toBeNull()
    expect(getNextAvailablePresetId(Array.from({ length: 255 }, (_, i) => ({ presetId: i + 1 })))).toBeNull()
  })

  it('normalizes old server responses for all consumers without mutating the source', () => {
    const original = [{ presetId: 'PresetID_5', presetName: 'PresetName_5' }, { presetId: 'unknown', presetName: '未知' }]
    expect(normalizePresetList(original)).toEqual([{ presetId: '5', presetName: 'PresetName_5' }, original[1]])
    expect(original[0].presetId).toBe('PresetID_5')
    expect(normalizePresetList(null)).toEqual([])
  })
})
