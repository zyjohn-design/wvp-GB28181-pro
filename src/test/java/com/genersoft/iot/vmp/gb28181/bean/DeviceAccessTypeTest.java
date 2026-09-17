package com.genersoft.iot.vmp.gb28181.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeviceAccessTypeTest {

    @Test
    void signalingServerIdIsClassifiedAsLowerPlatform() {
        Device device = new Device();
        device.setDeviceId("42010000012005000001");

        assertEquals("200", device.getTypeCode());
        assertEquals("中心信令控制服务器编码", device.getTypeName());
        assertEquals(Device.ACCESS_TYPE_PLATFORM, device.getAccessType());
        assertEquals("下级平台", device.getAccessTypeName());
    }

    @Test
    void ipcIdIsClassifiedAsGbDevice() {
        Device device = new Device();
        device.setDeviceId("42010000011325000001");

        assertEquals("132", device.getTypeCode());
        assertEquals(Device.ACCESS_TYPE_DEVICE, device.getAccessType());
        assertEquals("国标设备", device.getAccessTypeName());
    }
}
