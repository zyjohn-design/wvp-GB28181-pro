package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.GlobalExceptionHandler;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.Preset;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import org.junit.jupiter.api.Test;
import com.genersoft.iot.vmp.gb28181.service.impl.PresetRemarkService;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PresetSyncQueryTest {
    @Test
    void queriesCurrentDevicePresetsAndDistinguishesEmptyListFromFailure() throws Exception {
        IDeviceService devices = mock(IDeviceService.class);
        Device device = new Device();
        when(devices.getDeviceByDeviceId("device")).thenReturn(device);
        PtzController controller = new PtzController();
        PresetRemarkService remarks = mock(PresetRemarkService.class);
        when(remarks.apply(anyInt(), any())).thenAnswer(i -> i.getArgument(1));
        ReflectionTestUtils.setField(controller, "presetRemarkService", remarks);
        ReflectionTestUtils.setField(controller, "deviceService", devices);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        Preset preset = new Preset();
        preset.setPresetId("1");
        preset.setPresetName("修改后的名称");
        AtomicReference<List<Preset>> current = new AtomicReference<>(List.of(preset));
        doAnswer(invocation -> {
            ErrorCallback<List<Preset>> callback = invocation.getArgument(2);
            callback.run(0, "成功", current.get());
            return null;
        }).when(devices).queryPreset(eq(device), eq("channel"), any());
        MvcResult first = mvc.perform(get("/api/front-end/preset/query/device/channel"))
                .andExpect(request().asyncStarted()).andReturn();
        mvc.perform(asyncDispatch(first)).andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data[0].presetId").value("1"))
                .andExpect(jsonPath("$.data[0].presetName").value("修改后的名称"));
        current.set(List.of());
        MvcResult empty = mvc.perform(get("/api/front-end/preset/query/device/channel")).andReturn();
        mvc.perform(asyncDispatch(empty)).andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isEmpty());
        doAnswer(invocation -> {
            ErrorCallback<List<Preset>> callback = invocation.getArgument(2);
            callback.run(486, "消息超时未回复", null);
            return null;
        }).when(devices).queryPreset(eq(device), eq("channel"), any());
        MvcResult failed = mvc.perform(get("/api/front-end/preset/query/device/channel")).andReturn();
        mvc.perform(asyncDispatch(failed)).andExpect(jsonPath("$.code").value(486))
                .andExpect(jsonPath("$.msg").value("消息超时未回复"))
                .andExpect(jsonPath("$.data").doesNotExist());
        verify(devices, times(3)).queryPreset(eq(device), eq("channel"), any());
    }
}
