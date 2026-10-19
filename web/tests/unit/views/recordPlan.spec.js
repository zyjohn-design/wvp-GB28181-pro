import Vue from 'vue'
import Edit from '@/views/recordPlan/edit.vue'
import WeekTimePicker from '@/views/common/weekTimePicker.vue'
import LinkChannels from '@/views/recordPlan/dialog/linkChannelRecord.vue'

function editor() {
  const vm = new (Vue.extend(Edit))()
  vm.$store = { dispatch: jest.fn(() => Promise.resolve()) }
  vm.$message = { error: jest.fn(), success: jest.fn() }
  vm.showDialog = true
  vm.endCallback = jest.fn()
  vm.planArray[0].data.push({ start: 480, end: 1080 })
  return vm
}

async function flush() {
  for (let i = 0; i < 8; i++) await Promise.resolve()
}

describe('录制计划编辑', () => {
  it('名称为空时不发送请求', () => {
    const vm = editor()
    vm.planName = '   '
    vm.onSubmit()
    expect(vm.$store.dispatch).not.toHaveBeenCalled()
    expect(vm.$message.error).toHaveBeenCalledWith('请输入计划名称')
  })

  it('失败时保留填写内容，成功时关闭并刷新列表', async() => {
    const vm = editor()
    vm.planName = ' 工作日 '
    vm.$store.dispatch.mockRejectedValueOnce('数据库暂不可用')
    vm.onSubmit()
    vm.onSubmit()
    expect(vm.$store.dispatch).toHaveBeenCalledTimes(1)
    await flush()
    expect(vm.showDialog).toBe(true)
    expect(vm.planName).toBe(' 工作日 ')
    expect(vm.$message.error).toHaveBeenCalledWith('数据库暂不可用')
    const callback = vm.endCallback
    vm.onSubmit()
    await flush()
    expect(vm.$store.dispatch).toHaveBeenLastCalledWith('recordPlan/addPlan', {
      id: null, name: '工作日', planList: [{ start: 480, stop: 1080, weekDay: 1, planId: null }]
    })
    expect(callback).toHaveBeenCalledTimes(1)
    expect(vm.showDialog).toBe(false)
  })

  it('时间段合并和复制时回传数据给保存表单', () => {
    jest.useFakeTimers()
    const vm = new (Vue.extend(WeekTimePicker))({ propsData: { planArray: [] } })
    const emit = jest.spyOn(vm, '$emit')
    vm.weekData[0].data = [{ start: 0, end: 60 }, { start: 30, end: 90 }]
    vm.trackHandler(0, 0, 90)
    expect(emit).toHaveBeenLastCalledWith('update:planArray', vm.weekData)
    expect(vm.weekData[0].data).toEqual([{ start: 0, end: 90 }])
    vm.weekDataCheckBox[1] = true
    vm.closeCopyBox = jest.fn()
    vm.onSubmitCopy(0)
    expect(vm.weekData[1].data).toEqual([{ start: 0, end: 90 }])
    expect(emit).toHaveBeenLastCalledWith('update:planArray', vm.weekData)
    vm.$destroy()
    jest.runAllTimers()
    jest.useRealTimers()
  })

  it('通道关联翻页和搜索保持正确分页参数', () => {
    const context = { currentPage: 1, count: 25, total: 100, getChannelList: jest.fn() }
    LinkChannels.methods.currentChange.call(context, 3)
    expect(context.currentPage).toBe(3)
    LinkChannels.methods.search.call(context)
    expect(context.currentPage).toBe(1)
    expect(context.count).toBe(25)
  })
})
