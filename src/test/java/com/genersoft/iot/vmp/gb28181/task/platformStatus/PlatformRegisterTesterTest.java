package com.genersoft.iot.vmp.gb28181.task.platformStatus;

import com.genersoft.iot.vmp.gb28181.SipLayer;
import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.bean.PlatformRegisterResult;
import com.genersoft.iot.vmp.gb28181.service.IPlatformService;
import com.genersoft.iot.vmp.gb28181.transmit.SIPSender;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.SIPRequestHeaderPlarformProvider;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import gov.nist.javax.sip.SipProviderImpl;
import gov.nist.javax.sip.header.CallID;
import gov.nist.javax.sip.message.SIPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sip.SipFactory;
import javax.sip.header.CallIdHeader;
import javax.sip.message.Request;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * 验证测试注册把完整的 "首次REGISTER -> 401 -> 携带Digest再注册 -> 最终响应" 作为一次流程处理，
 * 第一次401不能直接返回失败。
 */
class PlatformRegisterTesterTest {

    private static final String CALL_ID = "test-call-id@192.168.1.10";

    private PlatformRegisterTester tester;
    private SIPSender sipSender;
    private SIPRequestHeaderPlarformProvider headerProvider;
    private PlatformRegisterResultManager resultManager;
    private IPlatformService platformService;

    @BeforeEach
    void setUp() throws Exception {
        tester = new PlatformRegisterTester();
        SipLayer sipLayer = mock(SipLayer.class);
        sipSender = mock(SIPSender.class);
        headerProvider = mock(SIPRequestHeaderPlarformProvider.class);
        IRedisCatchStorage redisCatchStorage = mock(IRedisCatchStorage.class);
        resultManager = mock(PlatformRegisterResultManager.class);
        platformService = mock(IPlatformService.class);

        when(sipLayer.getUdpSipProvider(anyString())).thenReturn(mock(SipProviderImpl.class));
        CallID callID = new CallID();
        callID.setCallId(CALL_ID);
        when(sipSender.getNewCallIdHeader(any(), any())).thenReturn(callID);
        when(redisCatchStorage.getCSEQ()).thenReturn(1L);
        when(headerProvider.createRegisterRequest(any(), anyLong(), any(), any(), any(CallIdHeader.class), anyInt()))
                .thenReturn(mock(Request.class));
        when(headerProvider.createRegisterRequest(any(), any(), any(), any(), any(CallIdHeader.class), anyInt()))
                .thenReturn(mock(Request.class));

        ReflectionTestUtils.setField(tester, "sipLayer", sipLayer);
        ReflectionTestUtils.setField(tester, "sipSender", sipSender);
        ReflectionTestUtils.setField(tester, "headerProviderPlatformProvider", headerProvider);
        ReflectionTestUtils.setField(tester, "redisCatchStorage", redisCatchStorage);
        ReflectionTestUtils.setField(tester, "resultManager", resultManager);
        ReflectionTestUtils.setField(tester, "platformService", platformService);
    }

    private Platform platform() {
        Platform platform = new Platform();
        platform.setId(1);
        platform.setEnable(true);
        platform.setName("上级平台");
        platform.setServerGBId("34020000002000000001");
        platform.setServerGBDomain("340200");
        platform.setServerIp("10.0.0.8");
        platform.setServerPort(5060);
        platform.setDeviceGBId("35020000002000000001");
        platform.setDeviceIp("192.168.1.10");
        platform.setDevicePort(5060);
        platform.setTransport("UDP");
        platform.setExpires(3600);
        return platform;
    }

    private SIPResponse response(int statusCode, String reason, boolean withWWWAuthenticate) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("SIP/2.0 ").append(statusCode).append(" ").append(reason).append("\r\n")
                .append("Via: SIP/2.0/UDP 192.168.1.10:5060;branch=z9hG4bK123456\r\n")
                .append("From: <sip:35020000002000000001@340200>;tag=fromtag1\r\n")
                .append("To: <sip:35020000002000000001@340200>;tag=totag1\r\n")
                .append("Call-ID: ").append(CALL_ID).append("\r\n")
                .append("CSeq: 1 REGISTER\r\n");
        if (withWWWAuthenticate) {
            sb.append("WWW-Authenticate: Digest realm=\"340200\",nonce=\"44010200492000000001\"\r\n");
        }
        sb.append("Content-Length: 0\r\n\r\n");
        return (SIPResponse) SipFactory.getInstance().createMessageFactory().createResponse(sb.toString());
    }

    @Test
    void firstUnauthorizedShouldTriggerDigestRegisterAndNotFinishTheTest() throws Exception {
        Platform platform = platform();
        CompletableFuture<PlatformRegisterResult> async = CompletableFuture.supplyAsync(() -> tester.test(platform));

        // 第一次REGISTER已发出
        verify(sipSender, timeout(3000)).transmitRequest(anyString(), any(), any(), any(), anyLong());

        // 上级要求认证， 这时不能返回失败， 应该携带Digest再注册一次
        assertTrue(tester.handleResponse(response(401, "Unauthorized", true)));
        verify(sipSender, timeout(3000).times(2)).transmitRequest(anyString(), any(), any(), any(), anyLong());
        assertFalse(async.isDone(), "收到第一个401不应该提前结束测试");

        // 认证后的第二次401才是真正的认证失败
        assertTrue(tester.handleResponse(response(401, "Unauthorized", true)));
        PlatformRegisterResult result = async.get(3, TimeUnit.SECONDS);
        assertFalse(result.isSuccess());
        assertEquals(401, result.getStatusCode());
        assertEquals(PlatformRegisterResult.STAGE_REGISTER_AUTH, result.getStage());
        assertEquals("401 Unauthorized：认证失败", result.getMessage());
        verify(resultManager).save(result);
        verify(platformService, never()).online(any(), any());
    }

    @Test
    void okAfterDigestShouldBeSuccessAndBringPlatformOnline() throws Exception {
        Platform platform = platform();
        CompletableFuture<PlatformRegisterResult> async = CompletableFuture.supplyAsync(() -> tester.test(platform));
        verify(sipSender, timeout(3000)).transmitRequest(anyString(), any(), any(), any(), anyLong());

        assertTrue(tester.handleResponse(response(401, "Unauthorized", true)));
        verify(sipSender, timeout(3000).times(2)).transmitRequest(anyString(), any(), any(), any(), anyLong());
        assertTrue(tester.handleResponse(response(200, "OK", false)));

        PlatformRegisterResult result = async.get(3, TimeUnit.SECONDS);
        assertTrue(result.isSuccess());
        assertEquals(200, result.getStatusCode());
        assertEquals("200 OK：注册成功", result.getMessage());
        verify(resultManager).save(result);
        // 启用的平台测试成功后直接上线
        verify(platformService).online(eq(platform), any());
    }

    @Test
    void forbiddenShouldFinishTheTestImmediately() throws Exception {
        Platform platform = platform();
        CompletableFuture<PlatformRegisterResult> async = CompletableFuture.supplyAsync(() -> tester.test(platform));
        verify(sipSender, timeout(3000)).transmitRequest(anyString(), any(), any(), any(), anyLong());

        assertTrue(tester.handleResponse(response(403, "Forbidden", false)));
        PlatformRegisterResult result = async.get(3, TimeUnit.SECONDS);
        assertFalse(result.isSuccess());
        assertEquals(403, result.getStatusCode());
        assertEquals(PlatformRegisterResult.STAGE_REGISTER, result.getStage());
        verify(platformService, never()).online(any(), any());
    }

    @Test
    void responseOfOtherCallIdShouldNotBeHandled() throws Exception {
        SIPResponse response = response(200, "OK", false);
        CallID callID = new CallID();
        callID.setCallId("other-call-id@192.168.1.10");
        response.setCallId(callID);
        assertFalse(tester.handleResponse(response));
    }

    @Test
    void missingLocalListenerShouldFailBeforeSending() throws Exception {
        SipLayer sipLayer = mock(SipLayer.class);
        when(sipLayer.getUdpSipProvider(anyString())).thenReturn(null);
        ReflectionTestUtils.setField(tester, "sipLayer", sipLayer);

        PlatformRegisterResult result = tester.test(platform());
        assertFalse(result.isSuccess());
        assertEquals(PlatformRegisterResult.STAGE_LOCAL_SEND, result.getStage());
        assertTrue(result.getMessage().contains("未找到本地IP"));
        Mockito.verifyNoInteractions(sipSender);
    }
}
