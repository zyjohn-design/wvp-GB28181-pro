package com.genersoft.iot.vmp.vmanager.server;

import org.junit.jupiter.api.Test;
import com.genersoft.iot.vmp.conf.GlobalResponseAdvice;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReadinessControllerTest {
    @Test
    void listeningHttpPortIsNotReadyUntilStartupCallbacksFinishAndRefusesOnShutdown() throws Exception {
        ApplicationAvailability availability = mock(ApplicationAvailability.class);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(new ReadinessController(availability))
                .setControllerAdvice(new GlobalResponseAdvice()).build();
        when(availability.getReadinessState()).thenReturn(ReadinessState.REFUSING_TRAFFIC);
        mvc.perform(get("/api/server/health/ready")).andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.status").value("NOT_READY"));
        when(availability.getReadinessState()).thenReturn(ReadinessState.ACCEPTING_TRAFFIC);
        mvc.perform(get("/api/server/health/ready")).andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().json("{\"status\":\"UP\"}"));
        when(availability.getReadinessState()).thenReturn(ReadinessState.REFUSING_TRAFFIC);
        mvc.perform(get("/api/server/health/ready")).andExpect(status().isServiceUnavailable());
    }
}
