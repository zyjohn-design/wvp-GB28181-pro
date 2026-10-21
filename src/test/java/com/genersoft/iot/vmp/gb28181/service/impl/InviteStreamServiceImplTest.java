package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.common.InviteSessionType;
import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.service.IInviteStreamService.InviteRequest;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.bean.InviteErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class InviteStreamServiceImplTest {
    static class TestService extends InviteStreamServiceImpl {
        long now;
        @Override long nanoTime() { return now; }
        void advance(long millis) { now += TimeUnit.MILLISECONDS.toNanos(millis); }
    }

    private final TestService service = new TestService();
    @SuppressWarnings("unchecked")
    private final ErrorCallback<StreamInfo> callback = mock(ErrorCallback.class);

    @BeforeEach
    void setup() {
        UserSetting settings = new UserSetting();
        settings.setPlayTimeout(30_000);
        ReflectionTestUtils.setField(service, "userSetting", settings);
    }

    @Test
    void orphanWithoutAnyRedisInviteExpiresAndAllowsRetry() {
        RedisTemplate<?, ?> redis = mock(RedisTemplate.class, RETURNS_DEEP_STUBS);
        ReflectionTestUtils.setField(service, "redisTemplate", redis);
        InviteRequest old = service.register(InviteSessionType.PLAY, 44796, null, callback);
        assertTrue(old.isFirst());
        service.advance(60_000);
        service.execute(); // Redis hash is empty; in-memory expiry must still run.
        assertFalse(old.isPending());
        verify(callback).run(eq(-2), contains("点播等待超时"), isNull());
        assertTrue(service.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void retryReclaimsExpiredOwnerEvenBeforeScheduledSweep() {
        InviteRequest old = service.register(InviteSessionType.PLAY, 44796, null, callback);
        service.advance(22 * 60_000); // Reproduce the two requests 22 minutes apart from the site log.
        InviteRequest retry = service.register(InviteSessionType.PLAY, 44796, null, null);
        assertTrue(retry.isFirst());
        assertFalse(old.isPending());
        verify(callback).run(eq(-2), anyString(), isNull());
    }

    @Test
    void joiningWaitersDoNotExtendOriginalDeadline() {
        service.register(InviteSessionType.PLAY, 44796, null, callback);
        service.advance(59_999);
        assertFalse(service.register(InviteSessionType.PLAY, 44796, null, callback).isFirst());
        service.advance(1);
        service.expirePendingInvites();
        verify(callback, times(2)).run(eq(-2), anyString(), isNull());
        assertTrue(service.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void lateResultCannotCompleteNewAttempt() {
        InviteRequest old = service.register(InviteSessionType.PLAY, 44796, null, callback);
        service.advance(60_000);
        ErrorCallback<StreamInfo> retryCallback = mock(ErrorCallback.class);
        InviteRequest retry = service.register(InviteSessionType.PLAY, 44796, null, retryCallback);
        old.run(0, "late publish", new StreamInfo());
        old.run(-1, "late SIP timeout", null);
        verifyNoInteractions(retryCallback);
        assertTrue(retry.isPending());
        retry.run(0, "ok", null);
        verify(retryCallback).run(0, "ok", null);
    }

    @Test
    void sipOkMayArriveAfterMediaButNotAfterReplacementOrFailure() {
        InviteRequest owner = service.register(InviteSessionType.PLAY, 44796, null, callback);
        owner.run(0, "media arrived", new StreamInfo());
        assertFalse(owner.isPending());
        assertTrue(owner.isCurrent(), "SIP 200 may arrive after the media hook");
        InviteRequest retry = service.register(InviteSessionType.PLAY, 44796, null, null);
        assertTrue(retry.isFirst());
        assertFalse(owner.isCurrent());
        retry.run(-2, "timeout", null);
        assertFalse(retry.isCurrent(), "late SIP 200 must not revive a failed attempt");
    }

    @Test
    void completedGenerationIsEventuallyEvictedWithoutAnotherCallback() {
        InviteRequest owner = service.register(InviteSessionType.PLAY, 44796, null, callback);
        owner.run(0, "ok", null);
        service.advance(60_000);
        service.expirePendingInvites();
        assertFalse(owner.isCurrent());
        verify(callback).run(0, "ok", null);
        verifyNoMoreInteractions(callback);
    }

    @Test
    void callbackExceptionDoesNotBlockOtherWaitersOrRetry() {
        doThrow(new IllegalStateException("disconnected HTTP client")).when(callback).run(anyInt(), anyString(), any());
        InviteRequest owner = service.register(InviteSessionType.PLAY, 44796, null, callback);
        ErrorCallback<StreamInfo> other = mock(ErrorCallback.class);
        service.register(InviteSessionType.PLAY, 44796, null, other);
        assertDoesNotThrow(() -> owner.run(0, "ok", null));
        verify(other).run(0, "ok", null);
        owner.run(0, "duplicate", null);
        verifyNoMoreInteractions(other);
        assertTrue(service.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void cancellingLiveWithConcreteStreamReleasesNullStreamClaim() {
        InviteRequest old = service.register(InviteSessionType.PLAY, 44796, null, callback);
        service.cancel(InviteSessionType.PLAY, 44796, "42010000012005000001_42010600001329668360");
        assertFalse(old.isPending());
        verify(callback).run(eq(InviteErrorCode.ERROR_FOR_FINISH.getCode()), anyString(), isNull());
        assertTrue(service.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void playbackSegmentsAndTypesAreIndependentAndCancelIsScoped() {
        InviteRequest a = service.register(InviteSessionType.PLAYBACK, 44796, "a", callback);
        InviteRequest b = service.register(InviteSessionType.PLAYBACK, 44796, "b", callback);
        InviteRequest download = service.register(InviteSessionType.DOWNLOAD, 44796, "a", callback);
        InviteRequest otherChannel = service.register(InviteSessionType.PLAYBACK, 44797, "a", callback);
        service.cancel(InviteSessionType.PLAYBACK, 44796, "a");
        assertFalse(a.isPending());
        assertTrue(b.isPending());
        assertTrue(download.isPending());
        assertTrue(otherChannel.isPending());
        service.cancel(InviteSessionType.PLAYBACK, 44796, null);
        assertFalse(b.isPending());
        assertTrue(download.isPending());
        assertTrue(otherChannel.isPending());
    }

    @Test
    void concurrentRegistrationsHaveOneOwnerAndNoLostCallbacks() throws Exception {
        int count = 64;
        ExecutorService pool = Executors.newFixedThreadPool(8);
        AtomicInteger completed = new AtomicInteger();
        CountDownLatch start = new CountDownLatch(1);
        List<Future<InviteRequest>> futures = new ArrayList<>();
        try {
            for (int i = 0; i < count; i++) {
                futures.add(pool.submit(() -> {
                    start.await();
                    return service.register(InviteSessionType.PLAY, 44796, null,
                            (code, msg, data) -> completed.incrementAndGet());
                }));
            }
            start.countDown();
            int owners = 0;
            InviteRequest owner = null;
            for (Future<InviteRequest> future : futures) {
                InviteRequest request = future.get(5, TimeUnit.SECONDS);
                if (request.isFirst()) {
                    owners++;
                    owner = request;
                }
            }
            assertEquals(1, owners);
            owner.run(0, "ok", null);
            assertEquals(count, completed.get());
        } finally {
            pool.shutdownNow();
        }
    }

    @Test
    void completionRacingRegistrationNeverDropsNewCallback() throws Exception {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        try {
            for (int i = 0; i < 200; i++) {
                AtomicInteger completed = new AtomicInteger();
                InviteRequest owner = service.register(InviteSessionType.PLAY, 44796, null, null);
                Future<InviteRequest> joining = pool.submit(() -> service.register(InviteSessionType.PLAY, 44796, null,
                        (code, msg, data) -> completed.incrementAndGet()));
                owner.run(0, "ok", null);
                InviteRequest waiter = joining.get(5, TimeUnit.SECONDS);
                if (waiter.isFirst()) {
                    waiter.run(0, "ok", null);
                }
                assertEquals(1, completed.get());
            }
        } finally {
            pool.shutdownNow();
        }
    }
}
