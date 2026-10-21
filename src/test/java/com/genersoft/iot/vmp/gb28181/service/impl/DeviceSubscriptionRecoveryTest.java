package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.dao.DeviceMapper;
import com.genersoft.iot.vmp.gb28181.task.deviceStatus.DeviceStatusManager;
import com.genersoft.iot.vmp.gb28181.task.deviceSubscribe.*;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.ISIPCommander;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DeviceSubscriptionRecoveryTest {
    private final DeviceServiceImpl service = new DeviceServiceImpl();
    private final SubscribeTaskRunner tasks = mock(SubscribeTaskRunner.class);
    private final Device device = new Device();
    private final ISIPCommander commander = mock(ISIPCommander.class);

    @BeforeEach
    void setup() {
        UserSetting settings = new UserSetting();
        settings.setServerId("local");
        DeviceMapper mapper = mock(DeviceMapper.class);
        IRedisCatchStorage storage = mock(IRedisCatchStorage.class);
        DeviceStatusManager status = mock(DeviceStatusManager.class);
        device.setDeviceId("device");
        device.setServerId("local");
        device.setOnLine(true);
        device.setSubscribeCycleForCatalog(3600);
        device.setSubscribeCycleForMobilePosition(600);
        device.setSubscribeCycleForAlarm(1800);
        when(mapper.getAll()).thenReturn(List.of(device));
        when(mapper.getOnlineDevicesByServerId("local")).thenReturn(List.of(device));
        when(storage.getAllDevices()).thenReturn(List.of(device));
        when(storage.getDevice("device")).thenReturn(device);
        when(status.getAll()).thenReturn(Set.of("device"));
        ReflectionTestUtils.setField(service, "userSetting", settings);
        ReflectionTestUtils.setField(service, "deviceMapper", mapper);
        ReflectionTestUtils.setField(service, "redisCatchStorage", storage);
        ReflectionTestUtils.setField(service, "deviceStatusManager", status);
        ReflectionTestUtils.setField(service, "subscribeTaskRunner", tasks);
        ReflectionTestUtils.setField(service, "sipCommander", commander);
    }

    @ParameterizedTest
    @ValueSource(strings = {"catalog", "mobilPosition", "alarm"})
    void readyEventRestoresRemainingTimeAndPreservesConfiguredCycle(String type) throws Exception {
        SubscribeTaskInfo info = new SubscribeTaskInfo();
        info.setDeviceId("device");
        info.setName(type);
        info.setKey(type + "_device");
        info.setExpireTime(1);
        when(tasks.getAllTaskInfo()).thenReturn(List.of(info));
        long before = System.currentTimeMillis();
        assertDoesNotThrow(service::onApplicationReady);
        ArgumentCaptor<SubscribeTask> task = ArgumentCaptor.forClass(SubscribeTask.class);
        verify(tasks).addSubscribe(task.capture());
        assertTrue(task.getValue().getDelayTime() >= before + 1000);
        assertTrue(task.getValue().getDelayTime() <= System.currentTimeMillis() + 1000);
        assertEquals(3600, device.getSubscribeCycleForCatalog());
        assertEquals(600, device.getSubscribeCycleForMobilePosition());
        assertEquals(1800, device.getSubscribeCycleForAlarm());
        task.getValue().expired();
        switch (type) {
            case "catalog" -> verify(commander).catalogSubscribe(eq(device), isNull(), any(), any());
            case "mobilPosition" -> verify(commander).mobilePositionSubscribe(eq(device), isNull(), any(), any());
            case "alarm" -> verify(commander).alarmSubscribe(eq(device), isNull(), any(), any());
        }
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -2})
    void readyEventSkipsInvalidTtl(long ttl) {
        SubscribeTaskInfo info = new SubscribeTaskInfo();
        info.setDeviceId("device");
        info.setName("catalog");
        info.setExpireTime(ttl);
        when(tasks.getAllTaskInfo()).thenReturn(List.of(info));
        assertDoesNotThrow(service::onApplicationReady);
        verify(tasks, never()).addSubscribe(any());
    }
}
