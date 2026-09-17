package com.genersoft.iot.vmp.gb28181.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviceRegisterAttemptTest {

    @Test
    void shouldCreatePendingConfigAttemptWithoutPassword() {
        DeviceRegisterAttempt attempt = DeviceRegisterAttempt.pendingConfig(
                "42010000012005000001", "27.10.24.1", 5061, "UDP", null);

        assertEquals(DeviceRegisterAttempt.STATUS_PENDING_CONFIG, attempt.getStatus());
        assertEquals(403, attempt.getStatusCode());
        assertEquals(1, attempt.getAttemptCount());
        assertNull(attempt.getAuthUsername());
        assertTrue(attempt.getSuggestion().contains("配置接入"));
    }

    @Test
    void shouldKeepFirstTimeAndIncreaseCountWhenStatusChanges() {
        DeviceRegisterAttempt first = DeviceRegisterAttempt.pendingConfig(
                "42010000012005000001", "27.10.24.1", 5061, "UDP", null);
        DeviceRegisterAttempt second = DeviceRegisterAttempt.authFailed(
                "42010000012005000001", "27.10.24.1", 5061, "UDP",
                "42010000002002609171a", first);

        assertEquals(DeviceRegisterAttempt.STATUS_AUTH_FAILED, second.getStatus());
        assertEquals(2, second.getAttemptCount());
        assertEquals(first.getFirstAttemptTime(), second.getFirstAttemptTime());
        assertEquals("42010000002002609171a", second.getAuthUsername());
        assertTrue(second.getMessage().contains("Digest认证失败"));
    }
}
