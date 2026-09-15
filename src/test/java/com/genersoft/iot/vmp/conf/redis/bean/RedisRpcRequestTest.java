package com.genersoft.iot.vmp.conf.redis.bean;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RedisRpcRequestTest {

    @Test
    void responseSwapsSourceAndDestination() {
        RedisRpcRequest request = new RedisRpcRequest();
        request.setFromId("requester");
        request.setToId("worker");
        request.setSn(42L);
        request.setUri("channel/play");

        RedisRpcResponse response = request.getResponse();

        assertEquals("worker", response.getFromId());
        assertEquals("requester", response.getToId());
        assertEquals(42L, response.getSn());
        assertEquals("channel/play", response.getUri());
    }
}
