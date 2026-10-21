package com.genersoft.iot.vmp.gb28181.task.deviceSubscribe;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.conf.redis.RedisTemplateConfig;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.dao.DeviceMapper;
import com.genersoft.iot.vmp.gb28181.service.impl.DeviceServiceImpl;
import com.genersoft.iot.vmp.gb28181.task.deviceStatus.DeviceStatusManager;
import com.genersoft.iot.vmp.gb28181.task.deviceSubscribe.impl.SubscribeTaskForCatalog;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/** Uses an isolated Redis instance, never a deployment's Redis or data. */
@EnabledIfEnvironmentVariable(named = "WVP_TEST_REDIS_PORT", matches = "[0-9]+")
class SubscribeTaskRunnerRedisTest {
    @Test
    void reproducesZeroTtlAndRecoversSubscriptionsDuringActualReadyHandler() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory("127.0.0.1",
                Integer.parseInt(System.getenv("WVP_TEST_REDIS_PORT")));
        factory.afterPropertiesSet();
        factory.start();
        RedisTemplate<String, Object> redis = new RedisTemplateConfig().redisTemplate(factory);
        redis.afterPropertiesSet();
        UserSetting settings = new UserSetting();
        settings.setServerId("startup-test-" + UUID.randomUUID());
        String prefix = "VMP_DEVICE_SUBSCRIBE_" + settings.getServerId() + "_";
        SubscribeTaskRunner runner = new SubscribeTaskRunner();
        ReflectionTestUtils.setField(runner, "redisTemplate", redis);
        ReflectionTestUtils.setField(runner, "userSetting", settings);
        try {
            // Same calculation as the failed deployment: 500ms becomes an invalid 0-second SET.
            RedisSystemException failure = assertThrows(RedisSystemException.class,
                    () -> redis.opsForValue().set(prefix + "old", new SubscribeTaskInfo(), Duration.ofSeconds(500 / 1000)));
            assertTrue(failure.getMostSpecificCause().getMessage().contains("invalid expire time"));

            Device device = new Device();
            device.setDeviceId("device");
            device.setOnLine(true);
            device.setServerId(settings.getServerId());
            device.setSubscribeCycleForCatalog(1);
            SubscribeTask shortTask = SubscribeTaskForCatalog.getInstance(device, null, null);
            assertDoesNotThrow(() -> runner.addSubscribe(shortTask));
            Long remaining = redis.getExpire(prefix + shortTask.getKey(), TimeUnit.MILLISECONDS);
            assertNotNull(remaining);
            assertTrue(remaining > 0 && remaining <= 500);

            // Restore the real DeviceService startup path with a valid cached subscription.
            device.setSubscribeCycleForCatalog(3600);
            shortTask.setDelayTime(System.currentTimeMillis() + 5000);
            runner.addSubscribe(shortTask);
            redis.opsForValue().set(prefix + "persistent", shortTask.getInfo());
            assertEquals(1, runner.getAllTaskInfo().size(), "persistent cache entry must not be restored");
            DeviceServiceImpl service = new DeviceServiceImpl();
            DeviceMapper devices = mock(DeviceMapper.class);
            IRedisCatchStorage storage = mock(IRedisCatchStorage.class);
            DeviceStatusManager status = mock(DeviceStatusManager.class);
            when(devices.getAll()).thenReturn(List.of(device));
            when(devices.getOnlineDevicesByServerId(settings.getServerId())).thenReturn(List.of(device));
            when(storage.getAllDevices()).thenReturn(List.of(device));
            when(storage.getDevice("device")).thenReturn(device);
            when(status.getAll()).thenReturn(Set.of("device"));
            ReflectionTestUtils.setField(service, "userSetting", settings);
            ReflectionTestUtils.setField(service, "deviceMapper", devices);
            ReflectionTestUtils.setField(service, "redisCatchStorage", storage);
            ReflectionTestUtils.setField(service, "deviceStatusManager", status);
            ReflectionTestUtils.setField(service, "subscribeTaskRunner", runner);
            assertDoesNotThrow(service::onApplicationReady);
            assertEquals(3600, device.getSubscribeCycleForCatalog());
            assertTrue(redis.getExpire(prefix + shortTask.getKey(), TimeUnit.MILLISECONDS) > 0);
        } finally {
            Set<String> keys = redis.keys(prefix + "*");
            if (keys != null && !keys.isEmpty()) redis.delete(keys);
            factory.destroy();
        }
    }
}
