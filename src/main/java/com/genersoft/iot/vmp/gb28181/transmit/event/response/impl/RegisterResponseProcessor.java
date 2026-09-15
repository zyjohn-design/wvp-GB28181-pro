package com.genersoft.iot.vmp.gb28181.transmit.event.response.impl;

import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.bean.PlatformRegisterResult;
import com.genersoft.iot.vmp.gb28181.bean.SipTransactionInfo;
import com.genersoft.iot.vmp.gb28181.event.SipSubscribe;
import com.genersoft.iot.vmp.gb28181.event.sip.SipEvent;
import com.genersoft.iot.vmp.gb28181.service.IPlatformService;
import com.genersoft.iot.vmp.gb28181.task.platformStatus.PlatformRegisterResultManager;
import com.genersoft.iot.vmp.gb28181.task.platformStatus.PlatformRegisterTester;
import com.genersoft.iot.vmp.gb28181.transmit.SIPProcessorObserver;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.ISIPCommanderForPlatform;
import com.genersoft.iot.vmp.gb28181.transmit.event.response.SIPResponseProcessorAbstract;
import com.genersoft.iot.vmp.storager.IRedisCatchStorage;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sip.InvalidArgumentException;
import javax.sip.ResponseEvent;
import javax.sip.SipException;
import javax.sip.header.WWWAuthenticateHeader;
import javax.sip.message.Response;
import java.text.ParseException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description:Register响应处理器
 * @author: swwheihei
 * @date:   2020年5月3日 下午5:32:23
 */
@Slf4j
@Component
public class RegisterResponseProcessor extends SIPResponseProcessorAbstract {

	private final String method = "REGISTER";

	@Autowired
	private ISIPCommanderForPlatform sipCommanderForPlatform;

	@Autowired
	private IRedisCatchStorage redisCatchStorage;

	@Autowired
	private SIPProcessorObserver sipProcessorObserver;

	@Autowired
	private IPlatformService platformService;

	@Autowired
	private SipSubscribe sipSubscribe;

	@Autowired
	private PlatformRegisterTester platformRegisterTester;

	@Autowired
	private PlatformRegisterResultManager registerResultManager;

	/**
	 * 记录每个注册流程(callId)已经发送认证注册的次数, 用于区分第一次401(正常流程)和认证后的401(认证失败)
	 */
	private final Map<String, Integer> authCountMap = new ConcurrentHashMap<>();

	@Override
	public void afterPropertiesSet() throws Exception {
		// 添加消息处理的订阅
		sipProcessorObserver.addResponseProcessor(method, this);
	}

	/**
	 * 处理Register响应
	 *
 	 * @param evt 事件
	 */
	@Override
	public void process(ResponseEvent evt) {
		SIPResponse response = (SIPResponse)evt.getResponse();
		// 手动触发的测试注册, 由测试流程自己完整处理 首次REGISTER -> 401 -> 携带Digest再注册 -> 最终响应
		if (platformRegisterTester.handleResponse(response)) {
			return;
		}
		String callId = response.getCallIdHeader().getCallId();
		long seqNumber = response.getCSeqHeader().getSeqNumber();
		SipEvent subscribe = sipSubscribe.getSubscribe(callId + seqNumber);
		if (subscribe == null || subscribe.getSipTransactionInfo() == null || subscribe.getSipTransactionInfo().getUser() == null) {
			return;
		}

		boolean isRegister = subscribe.getSipTransactionInfo().getExpires()  > 0;
		String action = isRegister ? "注册" : "注销";
		String platFormServerGbId = subscribe.getSipTransactionInfo().getUser();

		log.info("[国标级联]{} {}响应 {} ", action, response.getStatusCode(), platFormServerGbId);
		Platform platform = platformService.queryPlatformByServerGBId(platFormServerGbId);
		if (platform == null) {
			log.warn("[国标级联]收到 来自{}的 {} 回复 {}, 但是平台信息未查询到!!!", platFormServerGbId, action, response.getStatusCode());
			return;
		}

		if (response.getStatusCode() == Response.UNAUTHORIZED) {
			Integer authCount = authCountMap.get(callId);
			if (authCount != null && authCount > 0) {
				// 已经携带认证信息注册过了， 仍然返回401， 说明认证失败， 不再重复注册
				authCountMap.remove(callId);
				log.warn("[国标级联]{} 认证失败， 平台： {}", action, platFormServerGbId);
				if (isRegister) {
					registerResultManager.save(PlatformRegisterResult.fail(platform, PlatformRegisterResult.SOURCE_AUTO,
							PlatformRegisterResult.STAGE_REGISTER_AUTH, response.getStatusCode(), response.getReasonPhrase()));
				}
				platformService.offline(platform);
				return;
			}
			WWWAuthenticateHeader www = (WWWAuthenticateHeader)response.getHeader(WWWAuthenticateHeader.NAME);
			SipTransactionInfo sipTransactionInfo = new SipTransactionInfo(response);
			try {
				if (authCountMap.size() > 1000) {
					// 兜底， 防止异常情况下无限增长
					authCountMap.clear();
				}
				authCountMap.put(callId, 1);
				sipCommanderForPlatform.register(platform, sipTransactionInfo, www, null, null, isRegister);
			} catch (SipException | InvalidArgumentException | ParseException e) {
				authCountMap.remove(callId);
				log.error("[命令发送失败] 国标级联 再次注册: {}", e.getMessage());
				if (isRegister) {
					registerResultManager.save(PlatformRegisterResult.localFail(platform, PlatformRegisterResult.SOURCE_AUTO,
							"本地发送失败：" + e.getMessage(), "请查看服务日志中[国标级联]相关内容"));
				}
			}
		}else if (response.getStatusCode() == Response.OK){
			authCountMap.remove(callId);
			if (isRegister) {
				SipTransactionInfo sipTransactionInfo = new SipTransactionInfo(response);
				registerResultManager.save(PlatformRegisterResult.success(platform, PlatformRegisterResult.SOURCE_AUTO));
				platformService.online(platform, sipTransactionInfo);
			}else {
				platformService.offline(platform);
			}
		}
	}
}
