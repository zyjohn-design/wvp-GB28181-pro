export function parsePresetId(value) {
  if (typeof value !== 'number' && (typeof value !== 'string' || !/^[1-9][0-9]*$/.test(value))) {
    return null
  }
  const id = Number(value)
  return Number.isInteger(id) && id >= 1 && id <= 255 ? id : null
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
