package com.genersoft.iot.vmp.gb28181.utils;

import org.junit.jupiter.api.Test;
import org.dom4j.Element;

import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 国标报文字符集统一处理的测试。覆盖现场最常见的几种“配置与实际不一致”的乱码场景。
 */
class SipCharsetUtilsTest {

    private static final String NAME = "03-硚口大队";

    private static String xml(String declaredCharset) {
        return "<?xml version=\"1.0\" encoding=\"" + declaredCharset + "\"?>\r\n"
                + "<Notify>\r\n<CmdType>Catalog</CmdType>\r\n<Name>" + NAME + "</Name>\r\n</Notify>\r\n";
    }

    @Test
    void normalizeUsesGb18030ForLegacyChineseCharsets() {
        assertEquals("GB18030", SipCharsetUtils.normalize(null));
        assertEquals("GB18030", SipCharsetUtils.normalize(""));
        assertEquals("GB18030", SipCharsetUtils.normalize("GB2312"));
        assertEquals("GB18030", SipCharsetUtils.normalize("gbk"));
        assertEquals("GB18030", SipCharsetUtils.normalize("GB18030"));
        assertEquals("GB18030", SipCharsetUtils.normalize("不存在的字符集"));
        assertEquals("UTF-8", SipCharsetUtils.normalize("utf8"));
        assertEquals("UTF-8", SipCharsetUtils.normalize("UTF-8"));
    }

    /**
     * WVP上配置UTF-8，下级平台实际发送GBK：以前会解码出“03-�~口大队”
     */
    @Test
    void detectsGbkBytesWhenConfiguredUtf8() throws Exception {
        byte[] content = xml("UTF-8").getBytes(Charset.forName("GBK"));

        assertEquals("GB18030", SipCharsetUtils.resolveInbound(content, "UTF-8"));
        assertTrue(SipCharsetUtils.decode(content, "UTF-8").contains(NAME));

        Element root = XmlUtil.getRootElement(content, "UTF-8");
        assertEquals(NAME, root.elementTextTrim("Name"));
    }

    /**
     * WVP上配置GB2312，下级平台实际发送UTF-8：以前会解码出“锟斤拷”
     */
    @Test
    void detectsUtf8BytesWhenConfiguredGb2312() throws Exception {
        byte[] content = xml("GB2312").getBytes(Charset.forName("UTF-8"));

        assertEquals("UTF-8", SipCharsetUtils.resolveInbound(content, "GB2312"));

        Element root = XmlUtil.getRootElement(content, "GB2312");
        assertEquals(NAME, root.elementTextTrim("Name"));
    }

    /**
     * 声明GB2312但使用了GB2312未收录的扩展汉字（GBK/GB18030）
     */
    @Test
    void decodesGbkExtensionCharacterWhenDeclaredGb2312() throws Exception {
        byte[] content = xml("GB2312").getBytes(Charset.forName("GB18030"));

        Element root = XmlUtil.getRootElement(content, "GB2312");
        assertEquals(NAME, root.elementTextTrim("Name"));
    }

    @Test
    void supportsUtf8WithBom() throws Exception {
        byte[] body = xml("UTF-8").getBytes(Charset.forName("UTF-8"));
        byte[] content = new byte[body.length + 3];
        content[0] = (byte) 0xEF;
        content[1] = (byte) 0xBB;
        content[2] = (byte) 0xBF;
        System.arraycopy(body, 0, content, 3, body.length);

        assertEquals("UTF-8", SipCharsetUtils.resolveInbound(content, "GB2312"));

        Element root = XmlUtil.getRootElement(content, "GB2312");
        assertEquals(NAME, root.elementTextTrim("Name"));
    }

    /**
     * 部分设备的Name/Address中带有未转义的&
     */
    @Test
    void escapesUnescapedAmpersand() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>\r\n"
                + "<Notify>\r\n<Name>A&B队&amp;C</Name>\r\n</Notify>\r\n";
        byte[] content = xml.getBytes(Charset.forName("GB18030"));

        Element root = XmlUtil.getRootElement(content, "GB2312");
        assertEquals("A&B队&C", root.elementTextTrim("Name"));
    }

    @Test
    void returnsNullForEmptyContent() throws Exception {
        assertEquals(null, XmlUtil.getRootElement(new byte[0], "GB2312"));
        assertEquals("", SipCharsetUtils.decode(null, "GB2312"));
    }

    @Test
    void detectsGarbledNames() {
        assertTrue(SipCharsetUtils.isGarbled("03-\uFFFD~口大队"));
        assertTrue(SipCharsetUtils.isGarbled("锟斤拷口大队"));
        assertTrue(!SipCharsetUtils.isGarbled(NAME));
        assertTrue(!SipCharsetUtils.isGarbled(null));
    }

    @Test
    void outboundUpgradesGb2312ToGb18030() {
        assertEquals("GB18030", SipCharsetUtils.resolveOutbound("GB2312"));
        assertEquals("UTF-8", SipCharsetUtils.resolveOutbound("UTF-8"));
        // GB2312编码不了“硚”，会变成'?'；GB18030可以正常编码，且对GB2312已有字符字节完全一致
        assertEquals("03-?口大队", new String(NAME.getBytes(Charset.forName("GB2312")), Charset.forName("GB2312")));
        byte[] encoded = SipCharsetUtils.encode(NAME, "GB2312");
        assertEquals(NAME, new String(encoded, Charset.forName("GB18030")));
        assertEquals("03-", new String(encoded, 0, 3, Charset.forName("GB2312")));
    }
}
