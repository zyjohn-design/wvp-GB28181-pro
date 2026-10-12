// Axios may reject with an Error, while the API interceptor rejects business errors as strings.
export function getSaveErrorMessage(error) {
  const data = error && error.response && error.response.data
  const serverMessage = data && typeof data === 'object' && (data.msg || data.message)
  const message = serverMessage || (typeof error === 'string' ? error : error && error.message)
  return typeof message === 'string' && message.trim() ? message.trim() : '保存失败，请检查填写内容后重试'
}
