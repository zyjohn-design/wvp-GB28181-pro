package com.genersoft.iot.vmp.gb28181.task.platformStatus;

import com.genersoft.iot.vmp.gb28181.SipLayer;
import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.bean.PlatformRegisterResult;
import com.genersoft.iot.vmp.gb28181.bean.SipTransactionInfo;
import com.genersoft.iot.vmp.gb28181.service.IPlatformService;
import com.genersoft.iot.vmp.gb28181.transmit.SIPSender;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.SIPRequestHeaderPlarformProvider;
import com.genersoft.iot.vmp.gb28181.utils.SipUtils;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.sip.header.CallIdHeader;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 国标级联 测试注册。
 * <p>
 * UDP没有可靠的"连接测试", 只有真正发送一次REGISTER才能同时验证上级地址、传输模式、
 * 上级平台编号、本平台编号、是否需要认证、密码是否正确以及是否超时。
 * <p>
 * 这里把完整的 "首次REGISTER -> 401 -> 携带Digest再注册 -> 最终响应" 作为一次流程记录,
 * 不复用平台自动注册的回调, 避免收到第一个401就提前返回。
 *
 * @author lin
 */
@Slf4j
@Component
public class PlatformRegisterTester {

    /**
     * 单次REGISTER等待回复的时间
     */
    private static final long SIP_TIMEOUT = 3000L;

    /**
     * 整个测试流程（含认证后的第二次注册）的最长等待时间
     */
    private static final long TEST_TIMEOUT = 8000L;

    @Autowired
    private SipLayer sipLayer;

    @Autowired
    private SIPSender sipSender;

    @Autowired
    private SIPRequestHeaderPlarformProvider headerProviderPlatformProvider;

    @Autowired
    private IRedisCatchStorage redisCatchStorage;

    @Autowired
    private PlatformRegisterResultManager resultManager;

    @Lazy
    @Autowired
    private IPlatformService platformService;

    /**
     * 正在进行中的测试, key为callId
     */
    private final Map<String, TestSession> sessions = new ConcurrentHashMap<>();

    private static class TestSession {
        private Platform platform;
        private String callId;
        private String fromTag;
        /**
         * 是否已经发出携带Digest认证信息的注册
         */
        private volatile boolean authSent = false;
        /**
         * 注册成功时上级回复中的事务信息
         */
        private volatile SipTransactionInfo transactionInfo;
        private final CompletableFuture<PlatformRegisterResult> future = new CompletableFuture<>();
    }

    /**
     * 发送一次真实的REGISTER, 同步返回明确的结果
     */
    public PlatformRegisterResult test(Platform platform) {
        // 1. 先检查本地是否存在对应的信令监听， 不存在则不可能发送成功
        PlatformRegisterResult localCheckResult = checkLocalListener(platform);
        if (localCheckResult != null) {
            resultManager.save(localCheckResult);
            return localCheckResult;
        }

        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(platform.getDeviceIp(), platform.getTransport());
        if (callIdHeader == null) {
            PlatformRegisterResult result = PlatformRegisterResult.localFail(platform, PlatformRegisterResult.SOURCE_TEST,
                    "本地发送失败：无法创建CallId, 未找到可用的信令监听",
                    "请检查配置 sip.ip 与 sip.port, 并确认服务已正常启动信令监听");
            resultManager.save(result);
            return result;
        }

        TestSession session = new TestSession();
        session.platform = platform;
        session.callId = callIdHeader.getCallId();
        session.fromTag = SipUtils.getNewFromTag();
        sessions.put(session.callId, session);

        PlatformRegisterResult result;
        try {
            log.info("[国标级联] 测试注册, 平台： {}({}), 上级地址： {}:{}/{}", platform.getName(), platform.getServerGBId(),
                    platform.getServerIp(), platform.getServerPort(), platform.getTransport());
            sendRegister(session, null, null);
            result = session.future.get(TEST_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            result = PlatformRegisterResult.fail(platform, PlatformRegisterResult.SOURCE_TEST,
                    session.authSent ? PlatformRegisterResult.STAGE_REGISTER_AUTH : PlatformRegisterResult.STAGE_REGISTER,
                    PlatformRegisterResult.CODE_TIMEOUT, null);
        } catch (Exception e) {
            log.error("[国标级联] 测试注册失败, 平台： {}", platform.getServerGBId(), e);
            result = PlatformRegisterResult.localFail(platform, PlatformRegisterResult.SOURCE_TEST,
                    "本地发送失败：" + e.getMessage(),
                    "请查看服务日志中[国标级联]相关内容");
        } finally {
            sessions.remove(session.callId);
        }
        resultManager.save(result);
        onTestFinished(platform, result, session.transactionInfo);
        return result;
    }

    /**
     * 处理REGISTER的回复, 如果该回复属于一次测试流程则返回true, 由本类处理, 不再走平台自动注册的逻辑
     */
    public boolean handleResponse(SIPResponse response) {
        if (response == null || response.getCallIdHeader() == null) {
            return false;
        }
        TestSession session = sessions.get(response.getCallIdHeader().getCallId());
        if (session == null) {
            return false;
        }
        Platform platform = session.platform;
        int statusCode = response.getStatusCode();
        log.info("[国标级联] 测试注册收到回复 {}, 平台： {}", statusCode, platform.getServerGBId());
        if (statusCode == Response.UNAUTHORIZED) {
            if (session.authSent) {
                // 第二次401, 认证确实失败了
                session.future.complete(PlatformRegisterResult.fail(platform, PlatformRegisterResult.SOURCE_TEST,
                        PlatformRegisterResult.STAGE_REGISTER_AUTH, statusCode, response.getReasonPhrase()));
                return true;
            }
            // 第一次401属于正常流程： 上级要求认证， 携带Digest再注册一次
            WWWAuthenticateHeader www = (WWWAuthenticateHeader) response.getHeader(WWWAuthenticateHeader.NAME);
            if (www == null) {
                session.future.complete(PlatformRegisterResult.fail(platform, PlatformRegisterResult.SOURCE_TEST,
                        PlatformRegisterResult.STAGE_REGISTER, statusCode, "401回复中缺少WWW-Authenticate头"));
                return true;
            }
            session.authSent = true;
            try {
                sendRegister(session, www, new SipTransactionInfo(response));
            } catch (Exception e) {
                log.error("[国标级联] 测试注册, 携带认证信息注册失败, 平台： {}", platform.getServerGBId(), e);
                session.future.complete(PlatformRegisterResult.localFail(platform, PlatformRegisterResult.SOURCE_TEST,
                        "本地发送失败：" + e.getMessage(), "请查看服务日志中[国标级联]相关内容"));
            }
            return true;
        }
        if (statusCode == Response.OK) {
            PlatformRegisterResult result = PlatformRegisterResult.success(platform, PlatformRegisterResult.SOURCE_TEST);
            result.setStage(session.authSent ? PlatformRegisterResult.STAGE_REGISTER_AUTH : PlatformRegisterResult.STAGE_REGISTER);
            session.transactionInfo = new SipTransactionInfo(response);
            session.future.complete(result);
            return true;
        }
        session.future.complete(PlatformRegisterResult.fail(platform, PlatformRegisterResult.SOURCE_TEST,
                session.authSent ? PlatformRegisterResult.STAGE_REGISTER_AUTH : PlatformRegisterResult.STAGE_REGISTER,
                statusCode, response.getReasonPhrase()));
        return true;
    }

    private void sendRegister(TestSession session, WWWAuthenticateHeader www, SipTransactionInfo transactionInfo) throws Exception {
        Platform platform = session.platform;
        CallIdHeader callIdHeader = sipSender.getNewCallIdHeader(platform.getDeviceIp(), platform.getTransport());
        callIdHeader.setCallId(session.callId);
        String toTag = transactionInfo == null ? null : transactionInfo.getToTag();
        int expires = platform.getExpires() > 0 ? platform.getExpires() : 3600;
        Request request;
        if (www == null) {
            request = headerProviderPlatformProvider.createRegisterRequest(platform, redisCatchStorage.getCSEQ(),
                    session.fromTag, toTag, callIdHeader, expires);
        } else {
            request = headerProviderPlatformProvider.createRegisterRequest(platform, session.fromTag, toTag, www,
                    callIdHeader, expires);
        }
        boolean authStage = session.authSent;
        sipSender.transmitRequest(platform.getDeviceIp(), request, eventResult -> {
            // 超时以及4xx/5xx等失败回复
            session.future.complete(PlatformRegisterResult.fail(platform, PlatformRegisterResult.SOURCE_TEST,
                    authStage ? PlatformRegisterResult.STAGE_REGISTER_AUTH : PlatformRegisterResult.STAGE_REGISTER,
                    eventResult.statusCode, eventResult.msg));
        }, null, SIP_TIMEOUT);
    }

    /**
     * 测试注册成功后, 如果平台是启用状态, 直接让平台上线, 免得用户还要等自动注册
     */
    private void onTestFinished(Platform platform, PlatformRegisterResult result, SipTransactionInfo transactionInfo) {
        if (!result.isSuccess() || !platform.isEnable() || transactionInfo == null) {
            return;
        }
        try {
            platformService.online(platform, transactionInfo);
        } catch (Exception e) {
            log.error("[国标级联] 测试注册成功后上线失败, 平台： {}", platform.getServerGBId(), e);
        }
    }

    private PlatformRegisterResult checkLocalListener(Platform platform) {
        boolean tcp = "TCP".equalsIgnoreCase(platform.getTransport());
        boolean exist = tcp ? sipLayer.getTcpSipProvider(platform.getDeviceIp()) != null
                : sipLayer.getUdpSipProvider(platform.getDeviceIp()) != null;
        if (exist) {
            return null;
        }
        return PlatformRegisterResult.localFail(platform, PlatformRegisterResult.SOURCE_TEST,
                String.format("本地发送失败：未找到本地IP %s 对应的%s监听", platform.getDeviceIp(), tcp ? "TCP" : "UDP"),
                String.format("请把平台的[本地IP]改为本服务实际监听的IP; 若该平台由集群其他节点(%s)执行注册, 请在对应节点上测试",
                        platform.getServerId()));
    }
}
