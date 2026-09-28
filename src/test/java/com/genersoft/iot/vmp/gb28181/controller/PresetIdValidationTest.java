package com.genersoft.iot.vmp.gb28181.controller;

import com.genersoft.iot.vmp.conf.GlobalExceptionHandler;
import com.genersoft.iot.vmp.conf.exception.ControllerException;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PresetIdValidationTest {

    @Test
    void acceptsOnlyCanonicalPresetIdsWithinRange() {
        assertEquals(1, PresetIdValidator.parse("1"));
        assertEquals(255, PresetIdValidator.parse("255"));
        for (String value : new String[]{null, "", "0", "256", "01", "1.5", "PresetID_1", "99999999999999999999"}) {
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
                            .param("presetId", "PresetID_1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.msg").value("预置位编号必须为1-255之间的整数"));
            mvc.perform(get("/api/common/channel/front-end/preset/" + action)
                            .param("channelId", "1")
                            .param("presetId", "PresetID_1"))
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
}
