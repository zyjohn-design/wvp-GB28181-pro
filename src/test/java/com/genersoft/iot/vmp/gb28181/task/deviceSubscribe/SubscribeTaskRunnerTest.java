package com.genersoft.iot.vmp.gb28181.task.deviceSubscribe;

import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.utils.redis.RedisUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SubscribeTaskRunnerTest {
    static class TestRunner extends SubscribeTaskRunner {
        long now = System.currentTimeMillis();
        @Override long currentTimeMillis() { return now; }
    }
    private final TestRunner runner = new TestRunner();
    @SuppressWarnings("unchecked")
    private final RedisTemplate<String, Object> redis = mock(RedisTemplate.class);
    @SuppressWarnings("unchecked")
    private final ValueOperations<String, Object> values = mock(ValueOperations.class);
    private final String redisKey = "VMP_DEVICE_SUBSCRIBE_local_catalog_device";

    @BeforeEach
    void setup() {
        UserSetting settings = new UserSetting();
        settings.setServerId("local");
        ReflectionTestUtils.setField(runner, "userSetting", settings);
        ReflectionTestUtils.setField(runner, "redisTemplate", redis);
        when(redis.opsForValue()).thenReturn(values);
    }

    private SubscribeTask task(long remaining) {
        SubscribeTask task = mock(SubscribeTask.class);
        when(task.getKey()).thenReturn("catalog_device");
        when(task.getDelayTime()).thenReturn(runner.now + remaining);
        when(task.getInfo()).thenReturn(new SubscribeTaskInfo());
        return task;
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 500, 999, 1000, 60000})
    void validTtlRetainsMilliseconds(long remaining) {
        runner.addSubscribe(task(remaining));
        verify(values).set(eq(redisKey), any(SubscribeTaskInfo.class), eq(Duration.ofMillis(remaining)));
        assertTrue(runner.containsKey("catalog_device"));
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -500, -1000})
    void expiredTasksNeverWriteInvalidRedisTtl(long remaining) {
        runner.addSubscribe(task(remaining));
        verifyNoInteractions(values);
        assertFalse(runner.containsKey("catalog_device"));
    }

    @Test
    void failedRedisWriteDoesNotLeaveLocalTask() {
        doThrow(new IllegalStateException("Redis unavailable")).when(values).set(anyString(), any(), any(Duration.class));
        assertThrows(IllegalStateException.class, () -> runner.addSubscribe(task(500)));
        assertFalse(runner.containsKey("catalog_device"));
    }

    @Test
    void renewalRetainsSubsecondTtl() {
        runner.addSubscribe(task(5000));
        assertTrue(runner.updateDelay("catalog_device", runner.now + 500));
        verify(redis).expire(redisKey, Duration.ofMillis(500));
    }

    @Test
    void expiredRenewalRemovesLocalAndRedisState() {
        runner.addSubscribe(task(5000));
        assertFalse(runner.updateDelay("catalog_device", runner.now - 500));
        verify(redis).delete(redisKey);
        verify(redis, never()).expire(anyString(), any(Duration.class));
        assertFalse(runner.containsKey("catalog_device"));
    }

    @Test
    void replacingTaskDoesNotLeaveDuplicateQueueEntry() {
        runner.addSubscribe(task(1000));
        runner.addSubscribe(task(2000));
        DelayQueue<?> queue = (DelayQueue<?>) ReflectionTestUtils.getField(runner, "delayQueue");
        assertEquals(1, queue.size());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -2})
    void startupSkipsExpiredOrPersistentCachedSubscriptions(long ttl) {
        try (MockedStatic<RedisUtil> scan = mockStatic(RedisUtil.class)) {
            scan.when(() -> RedisUtil.scan(redis, "VMP_DEVICE_SUBSCRIBE_local_*")).thenReturn(List.of(redisKey));
            when(values.get(redisKey)).thenReturn(new SubscribeTaskInfo());
            when(redis.getExpire(redisKey, TimeUnit.SECONDS)).thenReturn(ttl);
            assertTrue(runner.getAllTaskInfo().isEmpty());
        }
    }

    @Test
    void startupRetainsValidCachedSubscription() {
        try (MockedStatic<RedisUtil> scan = mockStatic(RedisUtil.class)) {
            scan.when(() -> RedisUtil.scan(redis, "VMP_DEVICE_SUBSCRIBE_local_*")).thenReturn(List.of(redisKey));
            when(values.get(redisKey)).thenReturn(new SubscribeTaskInfo());
            when(redis.getExpire(redisKey, TimeUnit.SECONDS)).thenReturn(1L);
            assertEquals(1L, runner.getAllTaskInfo().get(0).getExpireTime());
        }
    }

    @Test
    void expirationCheckDoesNotBlockOnFutureTasks() {
        SubscribeTask task = task(60000);
        when(task.getDelay(any(TimeUnit.class))).thenReturn(60000L);
        runner.addSubscribe(task);
        assertTimeoutPreemptively(Duration.ofSeconds(1), runner::expirationCheck);
        verify(task, never()).expired();
    }
}
