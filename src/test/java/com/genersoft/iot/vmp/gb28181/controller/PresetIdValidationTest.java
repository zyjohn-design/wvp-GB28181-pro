package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.GlobalExceptionHandler;
import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.bean.FrontEndControlCodeForPreset;
import com.genersoft.iot.vmp.gb28181.service.IGbChannelService;
import com.genersoft.iot.vmp.gb28181.service.IGbChannelControlService;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.gb28181.service.IPTZService;
import org.junit.jupiter.api.Test;
import com.genersoft.iot.vmp.gb28181.service.impl.PresetRemarkService;
import com.genersoft.iot.vmp.vmanager.bean.WVPResult;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

class PresetIdValidationTest {

    @Test
    void acceptsNumericIdsAndKnownPlatformAliasesWithinRange() {
        assertEquals(1, PresetIdValidator.parse("1"));
        assertEquals(255, PresetIdValidator.parse("255"));
        assertEquals(5, PresetIdValidator.parse("PresetID_5"));
        assertEquals(255, PresetIdValidator.parse("PresetID_255"));
        for (String value : new String[]{null, "", "0", "256", "01", "1.5", "PresetID_0", "PresetID_256", "PresetID_05", "PresetName_5", "abc5", "99999999999999999999"}) {
            assertThrows(ControllerException.class, () -> PresetIdValidator.parse(value));
        }
    }

    @Test
    void rejectsDeviceStringsAtEveryPresetControlEndpoint() throws Exception {
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new PtzController(), new ChannelFrontEndController())
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        for (String action : new String[]{"add", "call", "delete"}) {
            mvc.perform(get("/api/front-end/preset/" + action + "/device/channel")
                            .param("presetId", "PresetName_1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("预置位编号必须为1-255之间的整数"));
            mvc.perform(get("/api/common/channel/front-end/preset/" + action)
                            .param("channelId", "1")
                            .param("presetId", "PresetName_1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("预置位编号必须为1-255之间的整数"));
        }
        mvc.perform(get("/api/front-end/preset/add/device/channel"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("预置位编号必须为1-255之间的整数"));
        mvc.perform(get("/api/common/channel/front-end/preset/add").param("channelId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.msg").value("预置位编号必须为1-255之间的整数"));
    }

    @Test
    void sendsTheSameNumericCommandForQueriedAliasAndOriginalId() throws Exception {
        PtzController controller = new PtzController();
        ReflectionTestUtils.setField(controller, "presetRemarkService", mock(PresetRemarkService.class));
        IDeviceService devices = mock(IDeviceService.class);
        IPTZService ptz = mock(IPTZService.class);
        Device device = new Device();
        when(devices.getDeviceByDeviceId("device")).thenReturn(device);
        ReflectionTestUtils.setField(controller, "deviceService", devices);
        ReflectionTestUtils.setField(controller, "ptzService", ptz);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
        String[] actions = {"add", "call", "delete"};
        for (int i = 0; i < actions.length; i++) {
            for (String id : new String[]{"5", "PresetID_5"}) {
                mvc.perform(get("/api/front-end/preset/" + actions[i] + "/device/channel")
                                .param("presetId", id)).andExpect(request().asyncStarted());
            }
            verify(ptz, times(2)).preset(eq(device), eq("channel"), eq(0x81 + i), eq(5), any());
        }
    }

    @Test
    void commonChannelControlsAlsoConvertAliasesToNumericIds() throws Exception {
        ChannelFrontEndController controller = new ChannelFrontEndController();
        IGbChannelService channels = mock(IGbChannelService.class);
        IGbChannelControlService controls = mock(IGbChannelControlService.class);
        CommonGBChannel channel = new CommonGBChannel();
        when(channels.getOne(123)).thenReturn(channel);
        ReflectionTestUtils.setField(controller, "channelService", channels);
        ReflectionTestUtils.setField(controller, "channelControlService", controls);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(controller).build();
        for (String action : new String[]{"add", "call", "delete"}) {
            mvc.perform(get("/api/common/channel/front-end/preset/" + action)
                            .param("channelId", "123").param("presetId", "PresetID_5"))
                    .andExpect(request().asyncStarted());
        }
        ArgumentCaptor<FrontEndControlCodeForPreset> codes = ArgumentCaptor.forClass(FrontEndControlCodeForPreset.class);
        verify(controls, times(3)).preset(eq(channel), codes.capture(), any());
        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, codes.getAllValues().get(i).getCode());
            assertEquals(5, codes.getAllValues().get(i).getPresetId());
        }
    }
}
