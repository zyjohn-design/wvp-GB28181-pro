package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.GlobalExceptionHandler;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.gb28181.service.IPTZService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PtzSpeedValidationTest {
    private MockMvc mvc;
    private IPTZService ptz;
    private Device device;

    @BeforeEach
    void setUp() {
        PtzController controller = new PtzController();
        IDeviceService devices = mock(IDeviceService.class);
        ptz = mock(IPTZService.class);
        device = new Device();
        when(devices.getDeviceByDeviceId("device")).thenReturn(device);
        ReflectionTestUtils.setField(controller, "deviceService", devices);
        ReflectionTestUtils.setField(controller, "ptzService", ptz);
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void omittedZoomSpeedAllowsDirectionAndZoomCommands() throws Exception {
        String[] commands = {"right", "zoomin", "zoomout"};
        int[] codes = {1, 16, 32};
        for (int i = 0; i < commands.length; i++) {
            mvc.perform(get("/api/front-end/ptz/device/channel")
                            .param("command", commands[i])
                            .param("horizonSpeed", "6").param("verticalSpeed", "6"))
                    .andExpect(status().isOk());
            verify(ptz).frontEndCommand(device, "channel", codes[i], 6, 6, 1);
        }
    }

    @Test
    void explicitZoomSpeedPreservesBoundariesAndRejectsOutOfRangeValues() throws Exception {
        for (int speed : new int[]{0, 15}) {
            mvc.perform(get("/api/front-end/ptz/device/channel")
                            .param("command", "zoomin").param("zoomSpeed", String.valueOf(speed)))
                    .andExpect(status().isOk());
            verify(ptz).frontEndCommand(device, "channel", 16, 100, 100, speed);
        }
        clearInvocations(ptz);
        for (int speed : new int[]{-1, 16}) {
            mvc.perform(get("/api/front-end/ptz/device/channel")
                            .param("command", "zoomin").param("zoomSpeed", String.valueOf(speed)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.code").value(100))
                    .andExpect(jsonPath("$.msg").value("zoomSpeed 为 0-15的数字"));
        }
        verifyNoInteractions(ptz);
    }

    @Test
    void invalidAndMissingCommandsReturnActionableErrors() throws Exception {
        mvc.perform(get("/api/front-end/ptz/device/channel"))
                .andExpect(jsonPath("$.code").value(100))
                .andExpect(jsonPath("$.msg").value("command 不能为空"));
        mvc.perform(get("/api/front-end/ptz/device/channel").param("command", "invalid"))
                .andExpect(jsonPath("$.code").value(100))
                .andExpect(jsonPath("$.msg").value("不支持的云台指令 command：invalid"));
        mvc.perform(get("/api/front-end/ptz/device/channel")
                        .param("command", "right").param("zoomSpeed", "abc"))
                .andExpect(jsonPath("$.code").value(100))
                .andExpect(jsonPath("$.msg").value("参数 zoomSpeed 类型错误，请检查参数格式和取值范围"));
        verifyNoInteractions(ptz);
    }

    @Test
    void stopClearsAllSpeedsWhenZoomSpeedIsOmitted() throws Exception {
        mvc.perform(get("/api/front-end/ptz/device/channel").param("command", "stop"))
                .andExpect(status().isOk());
        verify(ptz).frontEndCommand(device, "channel", 0, 0, 0, 0);
    }
}
