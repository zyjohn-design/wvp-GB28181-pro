import axios from 'axios'
import { Message } from 'element-ui'
import store from '@/store'
import '@/utils/request'

jest.mock('axios', () => ({
  create: jest.fn(() => ({
    interceptors: {
      request: { use: jest.fn() },
      response: { use: jest.fn() }
    }
  }))
}))
jest.mock('element-ui', () => ({ Message: { error: jest.fn() }, MessageBox: { confirm: jest.fn() } }))
jest.mock('@/store', () => ({ getters: { showConfirmBoxForLoginLose: false } }))
jest.mock('@/utils/auth', () => ({ getToken: jest.fn() }))

const service = axios.create.mock.results[0].value
const [success, failure] = service.interceptors.response.use.mock.calls[0]

describe('unified API error messages', () => {
  beforeEach(() => Message.error.mockClear())

  it('displays HTTP 200 business errors and keeps the rejection reason', async() => {
    await expect(success({ data: { code: 100, msg: 'zoomSpeed 为 0-15的数字' } }))
      .rejects.toBe('zoomSpeed 为 0-15的数字')
    expect(Message.error).toHaveBeenCalledWith({ message: 'zoomSpeed 为 0-15的数字', showClose: true })
  })

  it('supplies a nonempty message when a business error has no reason', async() => {
    await expect(success({ data: { code: 100, msg: '  ' } })).rejects.toBe('请求失败，错误码：100')
    expect(Message.error).toHaveBeenCalledTimes(1)
  })

  it('still displays HTTP errors when the login dialog has been disabled', async() => {
    expect(store.getters.showConfirmBoxForLoginLose).toBe(false)
    const error = { response: { status: 500, data: { msg: '设备连接失败' } } }
    await expect(failure(error)).rejects.toBe(error)
    expect(Message.error).toHaveBeenCalledWith({ message: '设备连接失败', showClose: true })
  })

  it('displays final network failures, timeouts and expired authentication', async() => {
    for (const [error, message] of [
      [{ config: { method: 'get', __networkRetried: true } }, '网络连接失败，请检查网络后重试'],
      [{ code: 'ECONNABORTED' }, '请求超时，请稍后重试'],
      [{ response: { status: 401 } }, '登录已失效，请重新登录']
    ]) {
      await expect(failure(error)).rejects.toBe(error)
      expect(Message.error).toHaveBeenLastCalledWith({ message, showClose: true })
    }
  })

  it('does not retry preset commands after a lost response', async() => {
    const error = { config: { method: 'get', retryOnNetworkError: false }, message: 'Network Error' }
    await expect(failure(error)).rejects.toBe(error)
    expect(error.config.__networkRetried).toBeUndefined()
    expect(Message.error).toHaveBeenCalledWith({ message: 'Network Error', showClose: true })
  })

  it('leaves successful results alone', () => {
    const data = { code: 0, msg: '成功', data: [] }
    expect(success({ data })).toBe(data)
    expect(Message.error).not.toHaveBeenCalled()
  })
})
