package com.genersoft.iot.vmp.gb28181.utils;

import org.springframework.util.ObjectUtils;

import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.util.Arrays;

/**
 * 国标(GB28181)报文字符集的统一处理入口。
 * <p>
 * 现场经常出现三类配置与实际不一致的情况，只依赖设备/平台上配置的字符集必然乱码：
 * <ul>
 *     <li>WVP中配置UTF-8，下级平台/设备实际按GBK发送（“硚”会被解码成“�~”）；</li>
 *     <li>WVP中配置GB2312，下级平台实际按UTF-8发送（中文变成“锟斤拷”）；</li>
 *     <li>XML声明为GB2312，但实际使用了GB2312未收录的扩展汉字（GBK/GB18030）。</li>
 * </ul>
 * 因此入站统一按字节探测：能严格按UTF-8解码的就是UTF-8，否则按GB18030（向下兼容GBK/GB2312）解码；
 * 出站统一把GB2312/GBK提升为GB18030编码（对GB2312内的字符字节完全一致，扩展汉字不再变成'?'）。
 *
 * @author wvp
 */
public class SipCharsetUtils {

    /**
     * GB18030是GBK、GB2312的超集，且对二者已有字符的编码字节完全一致，作为中文兜底字符集。
     */
    public static final String CHINESE = "GB18030";

    public static final String UTF8 = "UTF-8";

    private static final byte[] UTF8_BOM = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

    private SipCharsetUtils() {
    }

    /**
     * 规范化配置的字符集：中文相关字符集统一使用GB18030，未配置或不支持的字符集也回退到GB18030。
     */
    public static String normalize(String charset) {
        if (ObjectUtils.isEmpty(charset)) {
            return CHINESE;
        }
        String value = charset.trim();
        if ("GB2312".equalsIgnoreCase(value)
                || "GBK".equalsIgnoreCase(value)
                || "GB18030".equalsIgnoreCase(value)
                || "GB-2312".equalsIgnoreCase(value)) {
            return CHINESE;
        }
        if ("UTF-8".equalsIgnoreCase(value) || "UTF8".equalsIgnoreCase(value)) {
            return UTF8;
        }
        return isSupported(value) ? value : CHINESE;
    }

    /**
     * 入站报文字符集探测。忽略配置与XML声明可能存在的错误，以实际字节为准。
     *
     * @param rawContent        原始报文内容
     * @param configuredCharset 设备/平台上配置的字符集，仅在纯ASCII或内容为空时作为参考
     */
    public static String resolveInbound(byte[] rawContent, String configuredCharset) {
        if (rawContent == null || rawContent.length == 0) {
            return normalize(configuredCharset);
        }
        if (hasUtf8Bom(rawContent)) {
            return UTF8;
        }
        byte[] content = stripBom(rawContent);
        if (isPureAscii(content)) {
            // 纯ASCII时两种字符集解码结果一致，尊重配置
            return normalize(configuredCharset);
        }
        // GBK/GB2312编码的中文极难同时构成合法的UTF-8序列，因此可以据此区分
        if (isStrictlyDecodable(content, UTF8)) {
            return UTF8;
        }
        return CHINESE;
    }

    /**
     * 出站报文字符集。GB2312/GBK统一使用GB18030编码，避免扩展汉字被编码成'?'。
     */
    public static String resolveOutbound(String charset) {
        return normalize(charset);
    }

    /**
     * 按探测出的字符集解码报文内容
     */
    public static String decode(byte[] rawContent, String configuredCharset) {
        if (rawContent == null || rawContent.length == 0) {
            return "";
        }
        String charset = resolveInbound(rawContent, configuredCharset);
        return new String(stripBom(rawContent), Charset.forName(charset));
    }

    /**
     * 按出站字符集编码报文内容
     */
    public static byte[] encode(String content, String configuredCharset) {
        if (content == null) {
            return new byte[0];
        }
        return content.getBytes(Charset.forName(resolveOutbound(configuredCharset)));
    }

    /**
     * 判断字符串中是否包含解码失败产生的替换字符“\uFFFD”，用于识别历史乱码数据
     */
    public static boolean hasReplacementCharacter(String value) {
        return value != null && value.indexOf('\uFFFD') >= 0;
    }

    /**
     * 判断名称是否为历史乱码数据。包含两类典型特征：
     * <ul>
     *     <li>“\uFFFD”：GBK字节被按UTF-8解码，如“03-�~口大队”；</li>
     *     <li>“锟斤拷”：UTF-8字节被按GB2312/GBK解码。</li>
     * </ul>
     */
    public static boolean isGarbled(String value) {
        return hasReplacementCharacter(value) || (value != null && value.contains("锟斤拷"));
    }

    public static boolean hasUtf8Bom(byte[] content) {        if (content == null || content.length < UTF8_BOM.length) {
            return false;
        }
        return Arrays.equals(Arrays.copyOfRange(content, 0, UTF8_BOM.length), UTF8_BOM);
    }

    public static byte[] stripBom(byte[] content) {
        if (hasUtf8Bom(content)) {
            return Arrays.copyOfRange(content, UTF8_BOM.length, content.length);
        }
        return content;
    }

    private static boolean isPureAscii(byte[] content) {
        for (byte b : content) {
            if (b < 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isSupported(String charset) {
        try {
            return Charset.isSupported(charset);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean isStrictlyDecodable(byte[] content, String charset) {
        try {
            Charset.forName(charset).newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
                    .decode(ByteBuffer.wrap(content));
            return true;
        } catch (CharacterCodingException | IllegalArgumentException e) {
            return false;
        }
    }
}
