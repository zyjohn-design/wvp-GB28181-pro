package com.genersoft.iot.vmp.gb28181.transmit.event.request;

import org.junit.jupiter.api.Test;
import org.dom4j.Element;

import javax.sip.RequestEvent;
import javax.sip.header.ContentLengthHeader;
import javax.sip.message.Request;
import java.nio.charset.Charset;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SIPRequestProcessorParentTest {

    @Test
    void usesGb18030ForChineseLegacyCharsets() {
        assertEquals("GB18030", SIPRequestProcessorParent.resolveInboundCharset(null));
        assertEquals("GB18030", SIPRequestProcessorParent.resolveInboundCharset("GB2312"));
        assertEquals("GB18030", SIPRequestProcessorParent.resolveInboundCharset("gbk"));
        assertEquals("GB18030", SIPRequestProcessorParent.resolveInboundCharset("GB18030"));
    }

    @Test
    void preservesUtf8Configuration() {
        assertEquals("UTF-8", SIPRequestProcessorParent.resolveInboundCharset("UTF-8"));
    }

    @Test
    void fallsBackToGb18030WhenUtf8SettingReceivesGbkBytes() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<Response><Name>03-硚口大队</Name></Response>";
        byte[] content = xml.getBytes(Charset.forName("GBK"));

        assertEquals("GB18030", SIPRequestProcessorParent.resolveInboundCharset("UTF-8", content));

        Request request = mock(Request.class);
        ContentLengthHeader contentLength = mock(ContentLengthHeader.class);
        when(contentLength.getContentLength()).thenReturn(content.length);
        when(request.getContentLength()).thenReturn(contentLength);
        when(request.getRawContent()).thenReturn(content);
        RequestEvent event = mock(RequestEvent.class);
        when(event.getRequest()).thenReturn(request);

        SIPRequestProcessorParent processor = new SIPRequestProcessorParent() { };
        Element root = processor.getRootElement(event, "UTF-8");

        assertEquals("03-硚口大队", root.elementTextTrim("Name"));
    }

    @Test
    void decodesGbkExtensionCharacterWhenDeviceDeclaresGb2312() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"GB2312\"?>"
                + "<Response><Name>03-硚口大队</Name></Response>";
        byte[] content = xml.getBytes(Charset.forName("GBK"));

        Request request = mock(Request.class);
        ContentLengthHeader contentLength = mock(ContentLengthHeader.class);
        when(contentLength.getContentLength()).thenReturn(content.length);
        when(request.getContentLength()).thenReturn(contentLength);
        when(request.getRawContent()).thenReturn(content);
        RequestEvent event = mock(RequestEvent.class);
        when(event.getRequest()).thenReturn(request);

        SIPRequestProcessorParent processor = new SIPRequestProcessorParent() { };
        Element root = processor.getRootElement(event, "GB2312");

        assertEquals("03-硚口大队", root.elementTextTrim("Name"));
    }
}
