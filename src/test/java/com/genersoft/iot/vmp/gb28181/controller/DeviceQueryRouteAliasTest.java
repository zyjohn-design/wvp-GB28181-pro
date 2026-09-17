package com.genersoft.iot.vmp.gb28181.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.reflect.Method;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviceQueryRouteAliasTest {

    @Test
    void shouldExposeDeviceRoutesWithoutBlockedDevicesPath() throws Exception {
        assertGetAlias("devices", new Class<?>[]{String.class}, "/device/{deviceId}");
        assertGetAlias("devices", new Class<?>[]{int.class, int.class, String.class, Boolean.class, String.class}, "/list");
        assertGetAlias("channels",
                new Class<?>[]{String.class, int.class, int.class, String.class, Boolean.class, Boolean.class, String.class},
                "/device/{deviceId}/channels");
        assertGetAlias("devicesSync", new Class<?>[]{String.class}, "/device/{deviceId}/sync");
        assertGetAlias("deviceStatusApi", new Class<?>[]{String.class}, "/device/{deviceId}/status");

        Method delete = DeviceQuery.class.getDeclaredMethod("delete", String.class);
        assertTrue(Arrays.asList(delete.getAnnotation(DeleteMapping.class).value())
                .contains("/device/{deviceId}/delete"));
    }

    private void assertGetAlias(String methodName, Class<?>[] parameterTypes, String alias) throws Exception {
        Method method = DeviceQuery.class.getDeclaredMethod(methodName, parameterTypes);
        assertTrue(Arrays.asList(method.getAnnotation(GetMapping.class).value()).contains(alias));
    }
}
