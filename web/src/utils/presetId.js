export function parsePresetId(value) {
  if (typeof value === 'string') {
    const match = /^(?:PresetID_)?([1-9][0-9]{0,2})$/.exec(value)
    if (!match || match[0] !== value) return null
    value = match[1]
  } else if (typeof value !== 'number') {
    return null
  }
  const id = Number(value)
  return Number.isInteger(id) && id >= 1 && id <= 255 ? id : null
}

// Normalize responses from older backends as well, preserving names and unknown IDs.
export function normalizePresetList(presets) {
  return (presets || []).map(preset => {
    const id = parsePresetId(preset.presetId)
    return id === null ? { ...preset } : { ...preset, presetId: String(id) }
  })
}

export function getNextAvailablePresetId(presets) {
  const used = new Set()
  for (const preset of presets || []) {
    const id = parsePresetId(preset.presetId)
    if (id === null) return null
    used.add(id)
  }
  for (let id = 1; id <= 255; id++) {
    if (!used.has(id)) return id
  }
  return null
}
