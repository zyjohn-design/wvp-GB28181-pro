package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.common.enums.ChannelDataType;
import com.genersoft.iot.vmp.conf.SipConfig;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.SipLayer;
import com.genersoft.iot.vmp.gb28181.bean.*;
import com.genersoft.iot.vmp.gb28181.event.SipSubscribe;
import com.genersoft.iot.vmp.gb28181.event.sip.SipEvent;
import com.genersoft.iot.vmp.gb28181.service.IDeviceChannelService;
import com.genersoft.iot.vmp.gb28181.service.IDeviceService;
import com.genersoft.iot.vmp.gb28181.transmit.SIPProcessorObserver;
import com.genersoft.iot.vmp.gb28181.transmit.SIPSender;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.SIPRequestHeaderProvider;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.impl.SIPCommander;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.redisMsg.IRedisRpcPlayService;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import gov.nist.javax.sip.SipProviderImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;
import org.dom4j.DocumentHelper;

import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.SipFactory;
import javax.sip.header.ContentTypeHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/** Exercises service -> XML/MESSAGE -> sender subscription -> real SIP response dispatch. */
class PresetControlTest {
    private final PTZServiceImpl service = new PTZServiceImpl();
    private final SipSubscribe subscriptions = new SipSubscribe();
    private final SIPProcessorObserver observer = new SIPProcessorObserver();
    private final SipProviderImpl provider = mock(SipProviderImpl.class);
    private final IDeviceChannelService channels = mock(IDeviceChannelService.class);
    private final IDeviceService devices = mock(IDeviceService.class);
    private final IRedisRpcPlayService rpc = mock(IRedisRpcPlayService.class);
    private final Device device = new Device();
    private final DeviceChannel source = new DeviceChannel();
    @SuppressWarnings("unchecked")
    private final ErrorCallback<String> callback = mock(ErrorCallback.class);

    @BeforeEach
    void setup() throws Exception {
        SipConfig config = new SipConfig();
        config.setId("34020000002000000001");
        config.setDomain("3402000000");
        config.setPort(5060);
        config.setTimeout(60_000L);
        SipLayer layer = mock(SipLayer.class);
        when(layer.getLocalIp(any())).thenReturn("127.0.0.1");
        when(layer.getUdpSipProvider("127.0.0.1")).thenReturn(provider);
        when(provider.getNewCallId()).thenAnswer(i -> SipFactory.getInstance().createHeaderFactory()
                .createCallIdHeader(UUID.randomUUID().toString()));
        IRedisCatchStorage storage = mock(IRedisCatchStorage.class);
        when(storage.getCSEQ()).thenReturn(1L);
        SIPRequestHeaderProvider headers = new SIPRequestHeaderProvider();
        ReflectionTestUtils.setField(headers, "sipLayer", layer);
        ReflectionTestUtils.setField(headers, "sipConfig", config);
        ReflectionTestUtils.setField(headers, "redisCatchStorage", storage);
        SIPSender sender = new SIPSender();
        ReflectionTestUtils.setField(sender, "sipLayer", layer);
        ReflectionTestUtils.setField(sender, "sipConfig", config);
        ReflectionTestUtils.setField(sender, "sipSubscribe", subscriptions);
        SIPCommander commander = new SIPCommander();
        ReflectionTestUtils.setField(commander, "sipLayer", layer);
        ReflectionTestUtils.setField(commander, "headerProvider", headers);
        ReflectionTestUtils.setField(commander, "sipSender", sender);
        UserSetting settings = new UserSetting();
        settings.setServerId("local");
        ReflectionTestUtils.setField(service, "cmder", commander);
        ReflectionTestUtils.setField(service, "userSetting", settings);
        ReflectionTestUtils.setField(service, "deviceChannelService", channels);
        ReflectionTestUtils.setField(service, "deviceService", devices);
        ReflectionTestUtils.setField(service, "redisRpcPlayService", rpc);
        ReflectionTestUtils.setField(observer, "sipSubscribe", subscriptions);
        device.setDeviceId("34020000002000000002");
        device.setHostAddress("127.0.0.1:5061");
        device.setLocalIp("127.0.0.1");
        device.setTransport("UDP");
        device.setCharset("GB2312");
        device.setServerId("local");
        source.setId(123);
        source.setDeviceId("34020000001310000001");
        when(channels.getOneForSource(eq(device.getDeviceId()), anyString())).thenReturn(source);
    }

    private Request sent() throws Exception {
        ArgumentCaptor<Request> request = ArgumentCaptor.forClass(Request.class);
        verify(provider).sendRequest(request.capture());
        return request.getValue();
    }

    private void respond(Request request, int status) throws Exception {
        Response response = SipFactory.getInstance().createMessageFactory().createResponse(status, request);
        observer.processResponse(new ResponseEvent(this, null, null, response));
    }

    @Test
    void waitsFor200AndUsesOriginalChannelInStandardsCompliantMessage() throws Exception {
        service.preset(device, "platform-channel", 0x82, 1, callback);
        verifyNoInteractions(callback);
        Request request = sent();
        assertEquals("MESSAGE", request.getMethod());
        ContentTypeHeader type = (ContentTypeHeader) request.getHeader(ContentTypeHeader.NAME);
        assertEquals("application", type.getContentType().toLowerCase());
        assertEquals("manscdp+xml", type.getContentSubType().toLowerCase());
        var root = DocumentHelper.parseText(new String(request.getRawContent(), "GB18030")).getRootElement();
        assertEquals("Control", root.getName());
        assertEquals("DeviceControl", root.elementText("CmdType"));
        assertEquals(source.getDeviceId(), root.elementText("DeviceID"));
        assertEquals("A50F018200010038", root.elementText("PTZCmd"));
        assertTrue(Integer.parseInt(root.elementText("SN")) > 0);
        respond(request, 100);
        verifyNoInteractions(callback);
        respond(request, 200);
        verify(callback).run(0, "设备已接收预置位指令", null);
        assertTrue(subscriptions.isEmpty());
        respond(request, 200);
        verifyNoMoreInteractions(callback);
    }

    @Test
    void encodesAllThreeCommandsForEveryAllowedPresetWithCorrectChecksum() {
        for (int code = 0x81; code <= 0x83; code++) {
            for (int id = 1; id <= 255; id++) {
                String command = SIPCommander.frontEndCmdString(code, 0, id, 0);
                assertEquals(16, command.length());
                assertEquals(code, Integer.parseInt(command.substring(6, 8), 16));
                assertEquals("00", command.substring(8, 10));
                assertEquals(id, Integer.parseInt(command.substring(10, 12), 16));
                assertEquals("00", command.substring(12, 14));
                int checksum = 0;
                for (int i = 0; i < 14; i += 2) checksum += Integer.parseInt(command.substring(i, i + 2), 16);
                assertEquals(checksum & 255, Integer.parseInt(command.substring(14, 16), 16));
            }
        }
    }

    @Test
    void commonChannelSetAndDeleteWaitForResponseAndUseZeroData1() throws Exception {
        CommonGBChannel channel = new CommonGBChannel();
        channel.setGbId(123);
        channel.setDataDeviceId(7);
        channel.setDataType(ChannelDataType.GB28181);
        when(devices.getDevice(7)).thenReturn(device);
        when(channels.getOneForSourceById(123)).thenReturn(source);
        for (int code : new int[]{0x81, 0x83}) {
            clearInvocations(provider, callback);
            service.preset(channel, code, 255, callback);
            verifyNoInteractions(callback);
            Request request = sent();
            String xml = new String(request.getRawContent(), "GB18030");
            assertTrue(xml.contains("<PTZCmd>" + SIPCommander.frontEndCmdString(code, 0, 255, 0) + "</PTZCmd>"));
            respond(request, 200);
            verify(callback).run(0, "设备已接收预置位指令", null);
        }
    }

    @Test
    void propagatesRejectionAndDoesNotTreat401Or202As200() throws Exception {
        for (int status : new int[]{403, 401, 202}) {
            clearInvocations(provider, callback);
            service.preset(device, "platform-channel", 0x82, 5, callback);
            respond(sent(), status);
            verify(callback).run(eq(status), anyString(), isNull());
            verify(callback, never()).run(eq(0), anyString(), any());
            assertTrue(subscriptions.isEmpty());
        }
    }

    @Test
    void returnsFailureOnSipTimeoutAndIgnoresLate200() throws Exception {
        service.preset(device, "platform-channel", 0x82, 5, callback);
        Request request = sent();
        String key = request.getHeader("Call-ID").toString().replace("Call-ID:", "").trim() + "1";
        SipEvent event = subscriptions.getSubscribe(key);
        assertNotNull(event);
        event.setDelay(System.currentTimeMillis() - 1);
        subscriptions.execute();
        verify(callback).run(eq(408), contains("超时"), isNull());
        assertTrue(subscriptions.isEmpty());
        respond(request, 200);
        verifyNoMoreInteractions(callback);
    }

    @Test
    void reportsSendFailureAndRemovesSubscription() throws Exception {
        doThrow(new SipException("network down")).when(provider).sendRequest(any());
        service.preset(device, "platform-channel", 0x82, 5, callback);
        verify(callback).run(eq(100), contains("network down"), isNull());
        assertTrue(subscriptions.isEmpty());
    }

    @Test
    void validatesBeforeSendingAndDoesNotTurnInvalidPresetIntoStop() {
        for (int id : new int[]{0, 256}) service.preset(device, "platform-channel", 0x82, id, callback);
        service.preset(device, "platform-channel", 0, 1, callback);
        verify(callback, times(3)).run(eq(400), anyString(), isNull());
        verifyNoInteractions(provider);
    }

    @Test
    void remoteDeviceWaitsForOwningNodesResponse() {
        device.setServerId("remote");
        service.preset(device, "platform-channel", 0x82, 5, callback);
        ArgumentCaptor<ErrorCallback<String>> result = ArgumentCaptor.forClass(ErrorCallback.class);
        verify(rpc).preset(eq("remote"), eq(123), eq(0x82), eq(5), result.capture());
        verifyNoInteractions(callback, provider);
        result.getValue().run(403, "Forbidden", null);
        verify(callback).run(403, "Forbidden", null);
    }
}
