package com.genersoft.iot.vmp.gb28181.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeviceChannelResourceTypeTest {

    @Test
    void classifiesRegionAndPlatformNodesAsNonPlayable() {
        DeviceChannel region = channel("4201", 1);
        region.identifyResourceType();
        assertEquals(DeviceChannel.RESOURCE_REGION, region.getChannelType());
        assertEquals("行政区划", region.getResourceTypeName());
        assertFalse(region.isPlayable());

        DeviceChannel platform = channel("42010000012005000001", 1);
        platform.identifyResourceType();
        assertEquals(DeviceChannel.RESOURCE_PLATFORM, platform.getChannelType());
        assertEquals("平台节点", platform.getResourceTypeName());
        assertFalse(platform.isPlayable());
    }

    @Test
    void classifiesCameraAsPlayableVideoChannel() {
        DeviceChannel camera = channel("42010000011325000001", 0);
        camera.identifyResourceType();

        assertEquals(DeviceChannel.RESOURCE_CHANNEL, camera.getChannelType());
        assertEquals("视频通道", camera.getResourceTypeName());
        assertTrue(camera.isPlayable());
    }

    @Test
    void parentalFrontendDeviceIsTreatedAsDirectory() {
        DeviceChannel directory = channel("42010000011185000001", 1);
        directory.identifyResourceType();

        assertEquals(DeviceChannel.RESOURCE_DIRECTORY, directory.getChannelType());
        assertFalse(directory.isPlayable());
    }

    private DeviceChannel channel(String deviceId, int parental) {
        DeviceChannel channel = new DeviceChannel();
        channel.setDeviceId(deviceId);
        channel.setParental(parental);
        return channel;
    }
}
