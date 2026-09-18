package com.genersoft.iot.vmp.gb28181.transmit.event.request;

import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.transmit.SIPSender;
import com.genersoft.iot.vmp.gb28181.utils.SipCharsetUtils;
import com.genersoft.iot.vmp.gb28181.utils.SipUtils;
import com.genersoft.iot.vmp.gb28181.utils.XmlUtil;
import com.genersoft.iot.vmp.utils.IpPortUtil;
import gov.nist.javax.sip.message.MessageFactoryImpl;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;

import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.text.ParseException;

/**
 * @description:处理接收IPCamera发来的SIP协议请求消息
 * @author: songww
 * @date:   2020年5月3日 下午4:42:22
 */
@Slf4j
public abstract class SIPRequestProcessorParent {

	@Autowired
	private SIPSender sipSender;

	public HeaderFactory getHeaderFactory() {
		try {
			return SipFactory.getInstance().createHeaderFactory();
		} catch (PeerUnavailableException e) {
			log.error("未处理的异常 ", e);
		}
		return null;
	}

	public MessageFactory getMessageFactory() {
		try {
			return SipFactory.getInstance().createMessageFactory();
		} catch (PeerUnavailableException e) {
			log.error("未处理的异常 ", e);
		}
		return null;
	}

	class ResponseAckExtraParam{
		String content;
		ContentTypeHeader contentTypeHeader;
		SipURI sipURI;
		int expires = -1;
		/**
		 * 报文内容的编码字符集，为空时使用协议栈默认值
		 */
		String charset;
	}

	/***
	 * 回复状态码
	 * 100 trying
	 * 200 OK
	 * 400
	 * 404
	 */
	public SIPResponse responseAck(SIPRequest sipRequest, int statusCode) throws SipException, InvalidArgumentException, ParseException {
		return responseAck(sipRequest, statusCode, null);
	}

	@Async
	public void responseAckAsync(SIPRequest sipRequest, int statusCode) throws SipException, InvalidArgumentException, ParseException {
		responseAck(sipRequest, statusCode, null);
	}

	public SIPResponse responseAck(SIPRequest sipRequest, int statusCode, String msg) throws SipException, InvalidArgumentException, ParseException {
		return responseAck(sipRequest, statusCode, msg, null);
	}


	public SIPResponse responseAck(SIPRequest sipRequest, int statusCode, String msg, ResponseAckExtraParam responseAckExtraParam) throws SipException, InvalidArgumentException, ParseException {
		// 全局防御：校验SIP状态码合法性，防止非法状态码传入JAIN-SIP导致IllegalArgumentException
		if (statusCode < 100 || statusCode > 699) {
			log.error("[SIP响应] 非法状态码: {}，已替换为500 Server Internal Error。原始消息: {}", statusCode, msg);
			statusCode = Response.SERVER_INTERNAL_ERROR; // 500
		}

		if (sipRequest.getToHeader().getTag() == null) {
			sipRequest.getToHeader().setTag(SipUtils.getNewTag());
		}
		MessageFactory messageFactory = getMessageFactory();
		if (responseAckExtraParam != null && responseAckExtraParam.charset != null
				&& messageFactory instanceof MessageFactoryImpl) {
			// 出站内容统一把GB2312/GBK提升为GB18030，避免“硚”这类扩展汉字被编码成'?'
			((MessageFactoryImpl) messageFactory)
					.setDefaultContentEncodingCharset(SipCharsetUtils.resolveOutbound(responseAckExtraParam.charset));
		}
		SIPResponse response = (SIPResponse)messageFactory.createResponse(statusCode, sipRequest);
		response.setStatusCode(statusCode);
		if (msg != null) {
			response.setReasonPhrase(msg);
		}

		if (responseAckExtraParam != null) {
			if (responseAckExtraParam.sipURI != null && sipRequest.getMethod().equals(Request.INVITE)) {
				log.debug("responseSdpAck SipURI: {}:{}", responseAckExtraParam.sipURI.getHost(), responseAckExtraParam.sipURI.getPort());
				Address concatAddress = SipFactory.getInstance().createAddressFactory().createAddress(
						SipFactory.getInstance().createAddressFactory().createSipURI(responseAckExtraParam.sipURI.getUser(), IpPortUtil.concatenateIpAndPort(responseAckExtraParam.sipURI.getHost(), String.valueOf(responseAckExtraParam.sipURI.getPort()))
						));
				response.addHeader(SipFactory.getInstance().createHeaderFactory().createContactHeader(concatAddress));
			}
			if (responseAckExtraParam.contentTypeHeader != null) {
				response.setContent(responseAckExtraParam.content, responseAckExtraParam.contentTypeHeader);
			}

			if (sipRequest.getMethod().equals(Request.SUBSCRIBE)) {
				if (responseAckExtraParam.expires == -1) {
					log.error("[参数不全] 2xx的SUBSCRIBE回复，必须设置Expires header");
				}else {
					ExpiresHeader expiresHeader = SipFactory.getInstance().createHeaderFactory().createExpiresHeader(responseAckExtraParam.expires);
					response.addHeader(expiresHeader);
				}
			}
		}else {
			if (sipRequest.getMethod().equals(Request.SUBSCRIBE)) {
				log.error("[参数不全] 2xx的SUBSCRIBE回复，必须设置Expires header");
			}
		}

		// 发送response
		sipSender.transmitRequest(sipRequest.getLocalAddress().getHostAddress(), response);

		return response;
	}



	/**
	 * 回复带sdp的200
	 */
	public SIPResponse responseSdpAck(SIPRequest request, String sdp, Platform platform) throws SipException, InvalidArgumentException, ParseException {

		ContentTypeHeader contentTypeHeader = SipFactory.getInstance().createHeaderFactory().createContentTypeHeader("APPLICATION", "SDP");

		// 兼容国标中的使用编码@域名作为RequestURI的情况
		SipURI sipURI = (SipURI)request.getRequestURI();
		if (sipURI.getPort() == -1) {
			sipURI = SipFactory.getInstance().createAddressFactory().createSipURI(platform.getServerGBId(),  IpPortUtil.concatenateIpAndPort(platform.getServerIp(), String.valueOf(platform.getServerPort())));
		}
		ResponseAckExtraParam responseAckExtraParam = new ResponseAckExtraParam();
		responseAckExtraParam.contentTypeHeader = contentTypeHeader;
		responseAckExtraParam.content = sdp;
		responseAckExtraParam.sipURI = sipURI;

		SIPResponse sipResponse = responseAck(request, Response.OK, null, responseAckExtraParam);


		return sipResponse;
	}

	/**
	 * 回复带xml的200
	 */
	public SIPResponse responseXmlAck(SIPRequest request, String xml, Platform platform, Integer expires) throws SipException, InvalidArgumentException, ParseException {
		ContentTypeHeader contentTypeHeader = SipFactory.getInstance().createHeaderFactory().createContentTypeHeader("Application", "MANSCDP+xml");

		SipURI sipURI = (SipURI)request.getRequestURI();
		if (sipURI.getPort() == -1) {
			sipURI = SipFactory.getInstance().createAddressFactory().createSipURI(platform.getServerGBId(), IpPortUtil.concatenateIpAndPort(platform.getServerIp(), String.valueOf(platform.getServerPort())));
		}
		ResponseAckExtraParam responseAckExtraParam = new ResponseAckExtraParam();
		responseAckExtraParam.contentTypeHeader = contentTypeHeader;
		responseAckExtraParam.content = xml;
		responseAckExtraParam.sipURI = sipURI;
		responseAckExtraParam.expires = expires;
		responseAckExtraParam.charset = platform == null ? null : platform.getCharacterSet();
		return responseAck(request, Response.OK, null, responseAckExtraParam);
	}

	public Element getRootElement(RequestEvent evt) throws DocumentException {
		return getRootElement(evt, null);
	}

	/**
	 * 解析国标XML报文。字符集统一由{@link SipCharsetUtils}按实际字节探测，
	 * 解析逻辑统一收敛到{@link XmlUtil#getRootElement(byte[], String)}，避免多份实现行为不一致。
	 *
	 * @param charset 设备/平台上配置的字符集，仅作为参考，配置错误时以实际字节为准
	 */
	public Element getRootElement(RequestEvent evt, String charset) throws DocumentException {
		byte[] rawContent = evt.getRequest().getRawContent();
		if (rawContent == null || rawContent.length == 0) {
			return null;
		}
		return XmlUtil.getRootElement(rawContent, charset);
	}

}
