package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.web.gb28181.ApiControlController;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.service.*;
import com.genersoft.iot.vmp.gb28181.service.impl.SourcePTZServiceForGbImpl;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import org.junit.jupiter.api.Test;
import com.genersoft.iot.vmp.gb28181.service.impl.PresetRemarkService;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PresetResponseTest {
    @Test
    void bothHttpEntrypointsWaitForSipResultForEveryPresetOperation() throws Exception {
        IPTZService ptz = mock(IPTZService.class);
        IDeviceService devices = mock(IDeviceService.class);
        IGbChannelService channels = mock(IGbChannelService.class);
        IGbChannelControlService controls = mock(IGbChannelControlService.class);
        when(devices.getDeviceByDeviceId("device")).thenReturn(new Device());
        when(channels.getOne(123)).thenReturn(new CommonGBChannel());
        PresetRemarkService remarks = mock(PresetRemarkService.class);
        when(remarks.controlResult(anyInt(), anyInt(), anyInt(), anyString(), anyInt(), anyString(), any()))
                .thenAnswer(i -> new WVPResult<>(i.getArgument(4), i.getArgument(5), i.getArgument(6)));
        PtzController device = new PtzController();
        ReflectionTestUtils.setField(device, "presetRemarkService", remarks);
        ReflectionTestUtils.setField(device, "deviceService", devices);
        ReflectionTestUtils.setField(device, "ptzService", ptz);
        ChannelFrontEndController common = new ChannelFrontEndController();
        ReflectionTestUtils.setField(common, "presetRemarkService", remarks);
        ReflectionTestUtils.setField(common, "channelService", channels);
        ReflectionTestUtils.setField(common, "channelControlService", controls);
        SourcePTZServiceForGbImpl adapter = new SourcePTZServiceForGbImpl();
        ReflectionTestUtils.setField(adapter, "ptzService", ptz);
        doAnswer(i -> {
            adapter.preset(i.getArgument(0), i.getArgument(1), i.getArgument(2));
            return null;
        }).when(controls).preset(any(), any(), any());
        AtomicReference<ErrorCallback<String>> result = new AtomicReference<>();
        doAnswer(i -> { result.set(i.getArgument(4)); return null; })
                .when(ptz).preset(any(Device.class), anyString(), anyInt(), anyInt(), any());
        doAnswer(i -> { result.set(i.getArgument(3)); return null; })
                .when(ptz).preset(any(CommonGBChannel.class), anyInt(), anyInt(), any());
        ApiControlController compatibility = new ApiControlController();
        ReflectionTestUtils.setField(compatibility, "presetRemarkService", remarks);
        ReflectionTestUtils.setField(compatibility, "deviceService", devices);
        ReflectionTestUtils.setField(compatibility, "ptzService", ptz);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(device, common, compatibility).build();
        for (String action : new String[]{"add", "call", "delete"}) {
            for (int entry = 0; entry < 3; entry++) {
                for (int status : new int[]{0, 403, 408}) {
                    result.set(null);
                    String url = entry == 0 ? "/api/front-end/preset/" + action + "/device/channel"
                            : entry == 1 ? "/api/common/channel/front-end/preset/" + action : "/api/v1/control/preset";
                    String command = action.equals("add") ? "set" : action.equals("call") ? "goto" : "remove";
                    MvcResult pending = mvc.perform(get(url).param("channelId", "123").param("presetId", "5")
                            .param("serial", "device").param("code", "channel").param("preset", "5").param("command", command))
                            .andExpect(request().asyncStarted()).andReturn();
                    assertThrows(IllegalStateException.class, () -> pending.getAsyncResult(0));
                    assertNotNull(result.get());
                    result.get().run(status, "SIP result " + status, null);
                    mvc.perform(asyncDispatch(pending)).andExpect(status().isOk())
                            .andExpect(jsonPath("$.code").value(status))
                            .andExpect(jsonPath("$.msg").value("SIP result " + status));
                }
            }
        }
    }
    @Test
    void compatibilityApiRejectsInvalidCommandsIdsAndUnspecifiedTargetBeforeSending() throws Exception {
        ApiControlController controller = new ApiControlController();
        IPTZService ptz = mock(IPTZService.class);
        ReflectionTestUtils.setField(controller, "ptzService", ptz);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new com.genersoft.iot.vmp.conf.GlobalExceptionHandler()).build();
        for (String preset : new String[]{"0", "256", ""}) {
            mvc.perform(get("/api/v1/control/preset").param("serial", "device").param("code", "channel")
                            .param("command", "goto").param("preset", preset))
                    .andExpect(jsonPath("$.code").value(400));
        }
        mvc.perform(get("/api/v1/control/preset").param("serial", "device").param("code", "channel")
                        .param("command", "unknown").param("preset", "1"))
                .andExpect(jsonPath("$.code").value(400));
        mvc.perform(get("/api/v1/control/preset").param("serial", "device")
                        .param("command", "goto").param("preset", "1"))
                .andExpect(jsonPath("$.code").value(400));
        verifyNoInteractions(ptz);
    }

}
