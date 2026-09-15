package com.genersoft.iot.vmp.gb28181.session;

import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonSessionManagerTest {

    @Test
    void unknownSessionReturnsNull() {
        assertNull(new CommonSessionManager().get("missing"));
    }

    @Test
    void cleanupUsesConfiguredTimeout() {
        CommonSessionManager manager = new CommonSessionManager();
        AtomicBoolean oneMinuteExpired = new AtomicBoolean();
        AtomicBoolean fiveMinutesExpired = new AtomicBoolean();
        manager.add("one", value -> { }, value -> oneMinuteExpired.set(true), 1);
        manager.add("five", value -> { }, value -> fiveMinutesExpired.set(true), 5);

        Map<?, ?> sessions = (Map<?, ?>) ReflectionTestUtils.getField(manager, "callbackMap");
        assertNotNull(sessions);
        long twoMinutesAgo = System.currentTimeMillis() - 120_000;
        ReflectionTestUtils.setField(sessions.get("one"), "createTime", twoMinutesAgo);
        ReflectionTestUtils.setField(sessions.get("five"), "createTime", twoMinutesAgo);

        manager.execute();

        assertTrue(oneMinuteExpired.get());
        assertFalse(fiveMinutesExpired.get());
        assertNull(manager.get("one"));
        assertNotNull(manager.get("five"));
    }
}
