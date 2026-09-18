package com.genersoft.iot.vmp.gb28181.transmit.event.request;

import org.junit.jupiter.api.Test;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import javax.sip.message.Request;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 校验SIP请求处理入口的XML解析已统一到{@link com.genersoft.iot.vmp.gb28181.utils.XmlUtil}，
 * 并且不再依赖设备上配置的字符集
 */
class SIPRequestProcessorParentTest {

    private RequestEvent mockEvent(byte[] content) {
        Request request = mock(Request.class);
        when(request.getRawContent()).thenReturn(content);
        RequestEvent event = mock(RequestEvent.class);
        when(event.getRequest()).thenReturn(request);
        return event;
    }

    @Test
    void fallsBackToGb18030WhenUtf8SettingReceivesGbkBytes() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Response><Name>03-硚口大队</Name></Response>";
        byte[] content = xml.getBytes(Charset.forName("GBK"));

        SIPRequestProcessorParent processor = new SIPRequestProcessorParent() { };
        Element root = processor.getRootElement(mockEvent(content), "UTF-8");

        assertEquals("03-硚口大队", root.elementTextTrim("Name"));
    }

    @Test
    void decodesUtf8BytesWhenDeviceDeclaresGb2312() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>"
                + "<Response><Name>03-硚口大队</Name></Response>";
        byte[] content = xml.getBytes(Charset.forName("UTF-8"));

        SIPRequestProcessorParent processor = new SIPRequestProcessorParent() { };
        Element root = processor.getRootElement(mockEvent(content), "GB2312");

        assertEquals("03-硚口大队", root.elementTextTrim("Name"));
    }

    @Test
    void decodesGbkExtensionCharacterWithoutCharsetConfig() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>"
                + "<Response><Name>03-硚口大队</Name></Response>";
        byte[] content = xml.getBytes(Charset.forName("GB18030"));

        SIPRequestProcessorParent processor = new SIPRequestProcessorParent() { };
        Element root = processor.getRootElement(mockEvent(content));

        assertEquals("03-硚口大队", root.elementTextTrim("Name"));
    }

    @Test
    void returnsNullWhenContentIsEmpty() throws Exception {
        SIPRequestProcessorParent processor = new SIPRequestProcessorParent() { };
        assertNull(processor.getRootElement(mockEvent(new byte[0])));
        assertNull(processor.getRootElement(mockEvent(null)));
    }
}
