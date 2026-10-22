package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.GlobalExceptionHandler;
import com.genersoft.iot.vmp.gb28181.bean.*;
import com.genersoft.iot.vmp.gb28181.service.*;
import com.genersoft.iot.vmp.gb28181.service.impl.*;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DevicePresetQueryTest {
    private final IDeviceService devices = mock(IDeviceService.class);
    private final IDeviceChannelService channels = mock(IDeviceChannelService.class);
    private final PresetRemarkService remarks = mock(PresetRemarkService.class);
    private final Device device = new Device();
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        device.setDeviceId("device");
        when(devices.getDeviceByDeviceId("device")).thenReturn(device);
        when(remarks.apply(anyInt(), any())).thenAnswer(i -> i.getArgument(1));
        DevicePresetQueryService service = new DevicePresetQueryService(devices, channels, remarks, Runnable::run);
        PtzController controller = new PtzController();
        ReflectionTestUtils.setField(controller, "deviceService", devices);
        ReflectionTestUtils.setField(controller, "devicePresetQueryService", service);
        mvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    private DeviceChannel channel(String id, int type) {
        DeviceChannel channel = new DeviceChannel();
        channel.setId(7);
        channel.setDeviceId(id);
        channel.setName("通道" + id);
        channel.setChannelType(type);
        return channel;
    }

    private void reply(String channelId, int code, List<Preset> presets) {
        doAnswer(i -> {
            ErrorCallback<List<Preset>> callback = i.getArgument(2);
            callback.run(code, "查询结果", presets);
            return null;
        }).when(devices).queryPreset(eq(device), eq(channelId), any());
    }

    @Test
    void onlyReturnsNonEmptyVideoChannelsAndAppliesRemarks() throws Exception {
        when(channels.queryChaneListByDeviceId("device")).thenReturn(List.of(
                channel("a", 0), channel("b", 0), channel("directory", 4)));
        Preset preset = new Preset();
        preset.setPresetId("1");
        preset.setPresetName("设备名称");
        Preset named = new Preset();
        named.setPresetId("1");
        named.setPresetName("平台备注");
        reply("a", 0, List.of(preset));
        reply("b", 0, List.of());
        when(remarks.apply(7, List.of(preset))).thenReturn(List.of(named));
        MvcResult result = mvc.perform(get("/api/front-end/preset/query/device"))
                .andExpect(request().asyncStarted()).andReturn();
        mvc.perform(asyncDispatch(result)).andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].channelId").value("a"))
                .andExpect(jsonPath("$.data[0].channelName").value("通道a"))
                .andExpect(jsonPath("$.data[0].presets[0].presetName").value("平台备注"));
        verify(devices, never()).queryPreset(eq(device), eq("directory"), any());
    }

    @Test
    void keepsMatchesButMarksPartialFailureAndContinuesAfterException() throws Exception {
        when(channels.queryChaneListByDeviceId("device")).thenReturn(List.of(
                channel("broken", 0), channel("failed", 0), channel("a", 0)));
        doThrow(new IllegalStateException("发送失败")).when(devices).queryPreset(eq(device), eq("broken"), any());
        reply("failed", 486, null);
        Preset preset = new Preset();
        preset.setPresetId("2");
        reply("a", 0, List.of(preset));
        MvcResult result = mvc.perform(get("/api/front-end/preset/query/device")).andReturn();
        mvc.perform(asyncDispatch(result)).andExpect(jsonPath("$.code").value(100))
                .andExpect(jsonPath("$.msg").value(org.hamcrest.Matchers.containsString("2 个通道")))
                .andExpect(jsonPath("$.data.length()").value(1))
                .andExpect(jsonPath("$.data[0].channelId").value("a"));
    }

    @Test
    void emptyDeviceReturnsSuccessfulEmptyList() throws Exception {
        when(channels.queryChaneListByDeviceId("device")).thenReturn(List.of());
        MvcResult result = mvc.perform(get("/api/front-end/preset/query/device")).andReturn();
        mvc.perform(asyncDispatch(result)).andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isEmpty());
        verify(devices, never()).queryPreset(any(), any(), any());
    }

    @Test
    void rejectsInvalidTimeout() throws Exception {
        mvc.perform(get("/api/front-end/preset/query/device").param("timeout", "0"))
                .andExpect(jsonPath("$.code").value(400));
        verifyNoInteractions(channels);
    }

    @Test
    void stopsWhenOverallDeadlineExpires() throws Exception {
        when(channels.queryChaneListByDeviceId("device")).thenReturn(List.of(channel("a", 0), channel("b", 0)));
        MvcResult result = mvc.perform(get("/api/front-end/preset/query/device").param("timeout", "1")).andReturn();
        mvc.perform(asyncDispatch(result)).andExpect(jsonPath("$.code").value(100))
                .andExpect(jsonPath("$.data").isEmpty());
        verify(devices, never()).queryPreset(eq(device), eq("b"), any());
    }
}
