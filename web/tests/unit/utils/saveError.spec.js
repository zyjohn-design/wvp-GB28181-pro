import { getSaveErrorMessage } from '@/utils/saveError'

describe('device save error message', () => {
  it('shows the backend reason for business and HTTP errors', () => {
    expect(getSaveErrorMessage('设备编号已存在')).toBe('设备编号已存在')
    expect(getSaveErrorMessage({
      message: 'Request failed with status code 500',
      response: { data: { msg: '平台注册密码无效' } }
    })).toBe('平台注册密码无效')
  })

  it('uses a readable fallback when no reason is available', () => {
    expect(getSaveErrorMessage(undefined)).toBe('保存失败，请检查填写内容后重试')
  })
})
