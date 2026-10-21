package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.GlobalExceptionHandler;
import com.genersoft.iot.vmp.gb28181.bean.*;
import com.genersoft.iot.vmp.gb28181.dao.PresetRemarkMapper;
import com.genersoft.iot.vmp.gb28181.service.*;
import com.genersoft.iot.vmp.gb28181.service.impl.PresetRemarkService;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PresetRemarkControllerTest {
    private final PresetRemarkMapper mapper = mock(PresetRemarkMapper.class);
    private final IPTZService ptz = mock(IPTZService.class);
    private final IGbChannelControlService controls = mock(IGbChannelControlService.class);
    private final IDeviceService devices = mock(IDeviceService.class);
    private MockMvc mvc;

    @BeforeEach
    void setup() {
        Device device = new Device();
        device.setDeviceId("device");
        when(devices.getDeviceByDeviceId("device")).thenReturn(device);
        IDeviceChannelService sources = mock(IDeviceChannelService.class);
        DeviceChannel source = new DeviceChannel();
        source.setId(123);
        when(sources.getOneForSource("device", "channel")).thenReturn(source);
        IGbChannelService channels = mock(IGbChannelService.class);
        when(channels.getOne(123)).thenReturn(new CommonGBChannel());
        PresetRemarkService remarks = new PresetRemarkService(mapper, sources);
        when(mapper.update(anyInt(), anyInt(), anyString())).thenReturn(1);
        PtzController deviceApi = new PtzController();
        ReflectionTestUtils.setField(deviceApi, "deviceService", devices);
        ReflectionTestUtils.setField(deviceApi, "ptzService", ptz);
        ReflectionTestUtils.setField(deviceApi, "presetRemarkService", remarks);
        ChannelFrontEndController commonApi = new ChannelFrontEndController();
        ReflectionTestUtils.setField(commonApi, "channelService", channels);
        ReflectionTestUtils.setField(commonApi, "channelControlService", controls);
        ReflectionTestUtils.setField(commonApi, "presetRemarkService", remarks);
        mvc = MockMvcBuilders.standaloneSetup(deviceApi, commonApi)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void savesEnteredNameOnlyAfterDeviceAcceptsAndDefaultsBlankName() throws Exception {
        AtomicReference<ErrorCallback<String>> callback = new AtomicReference<>();
        doAnswer(i -> { callback.set(i.getArgument(4)); return null; })
                .when(ptz).preset(any(Device.class), eq("channel"), eq(0x81), eq(5), any());
        var pending = mvc.perform(get("/api/front-end/preset/add/device/channel")
                        .param("presetId", "5").param("presetName", " 东门入口 "))
                .andExpect(request().asyncStarted()).andReturn();
        verifyNoInteractions(mapper);
        callback.get().run(0, "accepted", null);
        mvc.perform(asyncDispatch(pending)).andExpect(jsonPath("$.code").value(0));
        verify(mapper).update(123, 5, "东门入口");
        doAnswer(i -> { callback.set(i.getArgument(2)); return null; }).when(controls).preset(any(), any(), any());
        var common = mvc.perform(get("/api/common/channel/front-end/preset/add")
                        .param("channelId", "123").param("presetId", "6"))
                .andExpect(request().asyncStarted()).andReturn();
        callback.get().run(0, "accepted", null);
        mvc.perform(asyncDispatch(common)).andExpect(jsonPath("$.code").value(0));
        verify(mapper).update(123, 6, "预置点6");
    }

    @Test
    void renamesThroughEitherEntryWithoutSendingAnyDeviceControl() throws Exception {
        mvc.perform(post("/api/front-end/preset/remark/device/channel")
                        .param("presetId", "5").param("presetName", "路口北侧"))
                .andExpect(jsonPath("$.data").value("路口北侧"));
        verify(mapper).update(123, 5, "路口北侧");
        mvc.perform(post("/api/common/channel/front-end/preset/remark")
                        .param("channelId", "123").param("presetId", "5").param("presetName", " "))
                .andExpect(jsonPath("$.data").value("预置点5"));
        verify(mapper).update(123, 5, "预置点5");
        verifyNoInteractions(ptz, controls);
    }

    @Test
    void bothListsReturnPlatformRemarkWithoutAddingPresetsAbsentOnDevice() throws Exception {
        Preset devicePreset = new Preset();
        devicePreset.setPresetId("PresetID_5");
        devicePreset.setPresetName("设备原名");
        Preset remark = new Preset();
        remark.setPresetId("5");
        remark.setPresetName("东门入口");
        when(mapper.list(123)).thenReturn(List.of(remark));
        doAnswer(i -> { ((ErrorCallback<List<Preset>>) i.getArgument(2)).run(0, "OK", List.of(devicePreset)); return null; })
                .when(devices).queryPreset(any(), eq("channel"), any());
        doAnswer(i -> { ((ErrorCallback<List<Preset>>) i.getArgument(1)).run(0, "OK", List.of(devicePreset)); return null; })
                .when(controls).queryPreset(any(), any());
        for (String url : List.of("/api/front-end/preset/query/device/channel", "/api/common/channel/front-end/preset/query")) {
            var result = mvc.perform(get(url).param("channelId", "123")).andReturn();
            mvc.perform(asyncDispatch(result)).andExpect(jsonPath("$.data[0].presetName").value("东门入口"));
        }
        doAnswer(i -> { ((ErrorCallback<List<Preset>>) i.getArgument(2)).run(0, "OK", List.of()); return null; })
                .when(devices).queryPreset(any(), eq("channel"), any());
        var empty = mvc.perform(get("/api/front-end/preset/query/device/channel")).andReturn();
        mvc.perform(asyncDispatch(empty)).andExpect(jsonPath("$.data").isEmpty());
    }

    @Test
    void rejectsLongRemarksBeforeSendingDeviceCommand() throws Exception {
        mvc.perform(get("/api/front-end/preset/add/device/channel")
                        .param("presetId", "5").param("presetName", "名".repeat(129)))
                .andExpect(jsonPath("$.code").value(400));
        mvc.perform(get("/api/common/channel/front-end/preset/add")
                        .param("channelId", "123").param("presetId", "5").param("presetName", "名".repeat(129)))
                .andExpect(jsonPath("$.code").value(400));
        verifyNoInteractions(ptz, controls, mapper);
    }
}
