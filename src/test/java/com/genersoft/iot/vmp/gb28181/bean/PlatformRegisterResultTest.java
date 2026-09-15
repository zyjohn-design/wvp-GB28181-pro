package com.genersoft.iot.vmp.gb28181.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlatformRegisterResultTest {

    private Platform platform() {
        Platform platform = new Platform();
        platform.setName("上级平台");
        platform.setServerGBId("34020000002000000001");
        platform.setServerGBDomain("340200");
        platform.setServerIp("10.0.0.8");
        platform.setServerPort(5060);
        platform.setDeviceGBId("35020000002000000001");
        platform.setDeviceIp("192.168.1.10");
        platform.setTransport("UDP");
        platform.setUsername("35020000002000000001");
        return platform;
    }

    @Test
    void timeoutShouldTellWhichUpstreamDidNotRespond() {
        PlatformRegisterResult result = PlatformRegisterResult.fail(platform(), PlatformRegisterResult.SOURCE_TEST,
                PlatformRegisterResult.STAGE_REGISTER, PlatformRegisterResult.CODE_TIMEOUT, null);
        assertFalse(result.isSuccess());
        assertTrue(result.getMessage().contains("注册超时"));
        assertTrue(result.getMessage().contains("10.0.0.8:5060"));
        assertTrue(result.getSuggestion().contains("UDP"));
        assertNotNull(result.getTime());
    }

    @Test
    void forbiddenShouldPointToDeviceIdAndSourceIp() {
        PlatformRegisterResult result = PlatformRegisterResult.fail(platform(), PlatformRegisterResult.SOURCE_AUTO,
                PlatformRegisterResult.STAGE_REGISTER, 403, "Forbidden");
        assertFalse(result.isSuccess());
        assertEquals(403, result.getStatusCode());
        assertTrue(result.getMessage().startsWith("403 Forbidden"));
        assertTrue(result.getSuggestion().contains("35020000002000000001"));
        assertTrue(result.getSuggestion().contains("192.168.1.10"));
    }

    @Test
    void unauthorizedAfterDigestShouldBeAuthFailure() {
        PlatformRegisterResult result = PlatformRegisterResult.fail(platform(), PlatformRegisterResult.SOURCE_TEST,
                PlatformRegisterResult.STAGE_REGISTER_AUTH, 401, "Unauthorized");
        assertFalse(result.isSuccess());
        assertEquals("401 Unauthorized：认证失败", result.getMessage());
        assertTrue(result.getSuggestion().contains("密码"));
    }

    @Test
    void notFoundShouldPointToServerIdAndDomain() {
        PlatformRegisterResult result = PlatformRegisterResult.fail(platform(), PlatformRegisterResult.SOURCE_TEST,
                PlatformRegisterResult.STAGE_REGISTER, 404, "Not Found");
        assertTrue(result.getMessage().startsWith("404 Not Found"));
        assertTrue(result.getSuggestion().contains("34020000002000000001"));
        assertTrue(result.getSuggestion().contains("340200"));
    }

    @Test
    void serverErrorShouldBeMarkedAsUpstreamException() {
        PlatformRegisterResult result = PlatformRegisterResult.fail(platform(), PlatformRegisterResult.SOURCE_TEST,
                PlatformRegisterResult.STAGE_REGISTER, 503, "Service Unavailable");
        assertTrue(result.getMessage().contains("上级平台内部异常"));
    }

    @Test
    void keepaliveTimeoutShouldSayRegisterOkButKeepaliveFailed() {
        PlatformRegisterResult result = PlatformRegisterResult.keepaliveFail(platform(), PlatformRegisterResult.SOURCE_AUTO,
                PlatformRegisterResult.CODE_TIMEOUT, null);
        assertFalse(result.isSuccess());
        assertEquals(PlatformRegisterResult.STAGE_KEEPALIVE, result.getStage());
        assertEquals("注册成功，但连续3次心跳未收到200 OK", result.getMessage());
    }

    @Test
    void successShouldBe200() {
        PlatformRegisterResult result = PlatformRegisterResult.success(platform(), PlatformRegisterResult.SOURCE_TEST);
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertEquals("34020000002000000001", result.getPlatformId());
    }
}
