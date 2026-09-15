package com.genersoft.iot.vmp.conf;

import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.EnableAsync;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AsyncConfigTest {

    @Test
    void asyncInfrastructureUsesClassBasedProxies() {
        EnableAsync enableAsync = AsyncConfig.class.getAnnotation(EnableAsync.class);
        assertNotNull(enableAsync);
        assertTrue(enableAsync.proxyTargetClass(),
                "必须使用CGLIB代理，否则实现接口的Service中非接口@Scheduled方法无法被调用");
    }
}
