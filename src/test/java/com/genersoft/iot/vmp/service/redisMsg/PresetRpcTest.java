package com.genersoft.iot.vmp.service.redisMsg;

import com.alibaba.fastjson2.JSONObject;
import com.genersoft.iot.vmp.conf.SipConfig;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.conf.redis.RedisRpcConfig;
import com.genersoft.iot.vmp.conf.redis.bean.*;
import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.service.IGbChannelService;
import com.genersoft.iot.vmp.gb28181.service.IPTZService;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.redisMsg.control.RedisRpcChannelPlayController;
import com.genersoft.iot.vmp.service.redisMsg.service.RedisRpcPlayServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PresetRpcTest {
    @Test
    @SuppressWarnings("unchecked")
    void owningNodeReturnsOnlyAfterSipResponseAndKeepsErrorReason() {
        RedisRpcChannelPlayController controller = new RedisRpcChannelPlayController();
        IGbChannelService channels = mock(IGbChannelService.class);
        IPTZService ptz = mock(IPTZService.class);
        RedisTemplate<String, Object> redis = mock(RedisTemplate.class);
        UserSetting settings = new UserSetting();
        settings.setServerId("owner");
        ReflectionTestUtils.setField(controller, "channelService", channels);
        ReflectionTestUtils.setField(controller, "iptzService", ptz);
        ReflectionTestUtils.setField(controller, "redisTemplate", redis);
        ReflectionTestUtils.setField(controller, "userSetting", settings);
        CommonGBChannel channel = new CommonGBChannel();
        when(channels.getOne(123)).thenReturn(channel);
        RedisRpcRequest request = new RedisRpcRequest();
        request.setFromId("caller");
        request.setToId("owner");
        request.setSn(42L);
        request.setUri("channel/ptz/preset");
        JSONObject params = new JSONObject();
        params.put("channelId", 123);
        params.put("cmdCode", 0x82);
        params.put("presetId", 5);
        request.setParam(params);
        assertNull(controller.preset(request));
        verifyNoInteractions(redis);
        ArgumentCaptor<ErrorCallback<String>> callback = ArgumentCaptor.forClass(ErrorCallback.class);
        verify(ptz).preset(eq(channel), eq(0x82), eq(5), callback.capture());
        callback.getValue().run(403, "Forbidden", null);
        ArgumentCaptor<Object> reply = ArgumentCaptor.forClass(Object.class);
        verify(redis).convertAndSend(eq(RedisRpcConfig.REDIS_REQUEST_CHANNEL_KEY), reply.capture());
        RedisRpcResponse response = ((RedisRpcMessage) reply.getValue()).getResponse();
        assertEquals(403, response.getStatusCode());
        assertEquals("Forbidden", response.getBody());
        assertEquals("caller", response.getToId());
        assertEquals(42L, response.getSn());
        params.put("presetId", 0);
        assertEquals(400, controller.preset(request).getStatusCode());
        verifyNoMoreInteractions(ptz);
    }

    @Test
    @SuppressWarnings("unchecked")
    void callerPropagatesSipResultAndRpcTimeoutWithoutReportingSuccess() {
        RedisRpcPlayServiceImpl client = new RedisRpcPlayServiceImpl();
        RedisRpcConfig redis = mock(RedisRpcConfig.class);
        UserSetting settings = new UserSetting();
        settings.setServerId("caller");
        settings.setPlayTimeout(10);
        SipConfig sip = new SipConfig();
        sip.setTimeout(1000);
        ReflectionTestUtils.setField(client, "redisRpcConfig", redis);
        ReflectionTestUtils.setField(client, "userSetting", settings);
        ReflectionTestUtils.setField(client, "sipConfig", sip);
        ErrorCallback<String> callback = mock(ErrorCallback.class);
        RedisRpcResponse failure = new RedisRpcResponse();
        failure.setStatusCode(403);
        failure.setBody("Forbidden");
        RedisRpcResponse success = new RedisRpcResponse();
        success.setStatusCode(0);
        success.setBody("设备已接收预置位指令");
        when(redis.request(any(), eq(2000L), eq(TimeUnit.MILLISECONDS))).thenReturn(failure, null, success);
        client.preset("owner", 123, 0x82, 5, callback);
        verify(callback).run(403, "Forbidden", null);
        client.preset("owner", 123, 0x82, 5, callback);
        verify(callback).run(eq(408), contains("跨节点应答超时"), isNull());
        client.preset("owner", 123, 0x82, 5, callback);
        verify(callback).run(0, "设备已接收预置位指令", null);
        ArgumentCaptor<RedisRpcRequest> sent = ArgumentCaptor.forClass(RedisRpcRequest.class);
        verify(redis, times(3)).request(sent.capture(), eq(2000L), eq(TimeUnit.MILLISECONDS));
        assertEquals("channel/ptz/preset", sent.getValue().getUri());
        assertEquals("owner", sent.getValue().getToId());
        assertEquals(5, ((JSONObject) sent.getValue().getParam()).getIntValue("presetId"));
    }
}
