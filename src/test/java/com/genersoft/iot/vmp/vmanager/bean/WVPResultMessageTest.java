package com.genersoft.iot.vmp.vmanager.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WVPResultMessageTest {
    @Test
    void serializesReadableFallbackForEveryKnownErrorAndUnknownCodes() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        for (ErrorCode code : ErrorCode.values()) {
            WVPResult<String> result = new WVPResult<>();
            result.setCode(code.getCode());
            result.setMsg("  ");
            assertEquals(code.getMsg(), mapper.readTree(mapper.writeValueAsString(result)).get("msg").asText());
        }
        assertEquals("请求失败，错误码：999", WVPResult.fail(999, null).getMsg());
        assertEquals("设备离线", WVPResult.fail(100, "设备离线").getMsg());
    }
}
