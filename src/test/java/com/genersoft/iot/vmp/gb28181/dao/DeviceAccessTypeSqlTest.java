package com.genersoft.iot.vmp.gb28181.dao;

import org.apache.ibatis.annotations.Select;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviceAccessTypeSqlTest {

    @Test
    void deviceListCanSeparateDevicesAndLowerPlatforms() throws NoSuchMethodException {
        Method method = DeviceMapper.class.getMethod("getDeviceList", Integer.class, String.class,
                Boolean.class, String.class);
        String sql = String.join(" ", method.getAnnotation(Select.class).value());

        assertTrue(sql.contains("accessType == \"PLATFORM\""), sql);
        assertTrue(sql.contains("substring(device_id, 11, 3) &gt;= '200'"), sql);
        assertTrue(sql.contains("accessType == \"DEVICE\""), sql);
        assertTrue(sql.contains("substring(device_id, 11, 3) &lt; '200'"), sql);
    }
}
