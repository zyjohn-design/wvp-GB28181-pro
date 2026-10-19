package com.genersoft.iot.vmp.media.zlm;

import com.genersoft.iot.vmp.media.bean.MediaServer;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ZLMRecordControlTest {
    @Test
    void sendsMp4ControlAndChecksBothCodeAndResult() {
        ZLMRESTfulUtils api = spy(new ZLMRESTfulUtils());
        MediaServer server = new MediaServer();
        doReturn("{\"code\":0,\"result\":true}")
                .when(api).sendPost(eq(server), anyString(), anyMap(), isNull());
        assertTrue(api.setRecord(server, "rtp", "camera", true));
        ArgumentCaptor<Map<String, Object>> params = ArgumentCaptor.forClass(Map.class);
        verify(api).sendPost(eq(server), eq("startRecord"), params.capture(), isNull());
        assertEquals(1, params.getValue().get("type"));
        assertEquals("__defaultVhost__", params.getValue().get("vhost"));
        assertEquals("rtp", params.getValue().get("app"));
        assertEquals("camera", params.getValue().get("stream"));
        assertTrue(api.setRecord(server, "rtp", "camera", false));
        verify(api).sendPost(eq(server), eq("stopRecord"), anyMap(), isNull());
        for (String response : new String[]{null, "{}", "{\"code\":0,\"result\":false}", "{\"code\":-1,\"result\":true}"}) {
            doReturn(response).when(api).sendPost(eq(server), anyString(), anyMap(), isNull());
            assertFalse(api.setRecord(server, "rtp", "camera", true));
        }
    }
}
