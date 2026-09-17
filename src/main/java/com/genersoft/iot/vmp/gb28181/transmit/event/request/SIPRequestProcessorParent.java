package com.genersoft.iot.vmp.gb28181.transmit.event.request;

import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.transmit.SIPSender;
import com.genersoft.iot.vmp.gb28181.utils.SipUtils;
import com.genersoft.iot.vmp.utils.IpPortUtil;
import com.google.common.primitives.Bytes;
import gov.nist.javax.sip.message.SIPRequest;
import gov.nist.javax.sip.message.SIPResponse;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.ObjectUtils;

import javax.sip.*;
import javax.sip.address.Address;
import javax.sip.address.SipURI;
import javax.sip.header.ContentTypeHeader;
import javax.sip.header.ExpiresHeader;
import javax.sip.header.HeaderFactory;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;
import java.io.ByteArrayInputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CodingErrorAction;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
		SIPResponse response = (SIPResponse)getMessageFactory().createResponse(statusCode, sipRequest);
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
		return responseAck(request, Response.OK, null, responseAckExtraParam);
	}

	public Element getRootElement(RequestEvent evt) throws DocumentException {
		return getRootElement(evt, "gb2312");
	}
	public Element getRootElement(RequestEvent evt, String charset) throws DocumentException {

		byte[] rawContent = evt.getRequest().getRawContent();
		if (evt.getRequest().getContentLength().getContentLength() == 0
				|| rawContent == null
				|| rawContent.length == 0
				|| ObjectUtils.isEmpty(new String(rawContent))) {
			return null;
		}

		// 目录编码在现场经常配置成 UTF-8，但宇视仍按 GBK 发送扩展汉字（例如“硚”）。
		// 仅依赖设备配置会在 UTF-8 解码时得到“�~”。先用严格解码探测原始字节，
		// 对声明为 UTF-8 但实际不是合法 UTF-8 的内容自动回退到 GB18030。
		charset = resolveInboundCharset(charset, rawContent);
		SAXReader reader = new SAXReader();
		reader.setEncoding(charset);
		// 对海康出现的未转义字符做处理。
		String[] destStrArray = new String[]{"&lt;","&gt;","&amp;","&apos;","&quot;"};
		// 或许可扩展兼容其他字符
		char despChar = '&';
		byte destBye = (byte) despChar;
		List<Byte> result = new ArrayList<>();
		for (int i = 0; i < rawContent.length; i++) {
			if (rawContent[i] == destBye) {
				boolean resul = false;
				for (String destStr : destStrArray) {
					if (i + destStr.length() <= rawContent.length) {
						byte[] bytes = Arrays.copyOfRange(rawContent, i, i + destStr.length());
						resul = resul || (Arrays.equals(bytes,destStr.getBytes()));
					}
				}
				if (resul) {
					result.add(rawContent[i]);
				}
			}else {
				result.add(rawContent[i]);
			}
		}
		byte[] bytesResult = Bytes.toArray(result);

		Document xml;
		try {
			xml = reader.read(new ByteArrayInputStream(bytesResult));
		}catch (DocumentException e) {
			log.warn("[xml解析异常]： 原文如下： \r\n{}", new String(bytesResult, Charset.forName(charset)));
			log.warn("[xml解析异常]： 原文如下： 尝试兼容性处理");
			String[] xmlLineArray = new String(bytesResult, Charset.forName(charset)).split("\\r?\\n");

			// 兼容海康的address字段带有<破换xml结构导致无法解析xml的问题
			StringBuilder stringBuilder = new StringBuilder();
			for (String s : xmlLineArray) {
				if (s.startsWith("<Address")) {
					continue;
				}
				stringBuilder.append(s);
			}
			xml = reader.read(new StringReader(stringBuilder.toString()));
		}
		return xml.getRootElement();
	}

	/**
	 * GB18030向下兼容GBK和GB2312。部分平台虽然在XML中声明GB2312，实际会发送
	 * “硚”等GBK扩展字符；用严格GB2312解码会产生替换字符“�”。这里只放宽入站
	 * XML解码，设备保存的字符集和出站SIP报文配置均不改变。
	 */
	static String resolveInboundCharset(String charset) {
		if (ObjectUtils.isEmpty(charset)
				|| "GB2312".equalsIgnoreCase(charset)
				|| "GBK".equalsIgnoreCase(charset)
				|| "GB18030".equalsIgnoreCase(charset)) {
			return "GB18030";
		}
		return charset;
	}

	static String resolveInboundCharset(String charset, byte[] rawContent) {
		String configured = resolveInboundCharset(charset);
		if (rawContent == null || rawContent.length == 0) {
			return configured;
		}

		// GB18030 兼容 GBK/GB2312，优先保证国标中文扩展字符可读。
		if ("UTF-8".equalsIgnoreCase(charset) && !isStrictlyDecodable(rawContent, "UTF-8")) {
			return "GB18030";
		}
		return configured;
	}

	private static boolean isStrictlyDecodable(byte[] content, String charset) {
		try {
			Charset.forName(charset).newDecoder()
					.onMalformedInput(CodingErrorAction.REPORT)
					.onUnmappableCharacter(CodingErrorAction.REPORT)
					.decode(ByteBuffer.wrap(content));
			return true;
		} catch (CharacterCodingException | RuntimeException e) {
			return false;
		}
	}


}
