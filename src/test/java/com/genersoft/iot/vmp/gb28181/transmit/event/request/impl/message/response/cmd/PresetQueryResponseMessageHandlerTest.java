package com.genersoft.iot.vmp.gb28181.transmit.event.request.impl.message.response.cmd;

import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.Preset;
import com.genersoft.iot.vmp.gb28181.transmit.event.request.impl.message.response.ResponseMessageHandler;
import gov.nist.javax.sip.message.SIPRequest;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import javax.sip.RequestEvent;
import javax.sip.message.Response;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PresetQueryResponseMessageHandlerTest {

    @Test
    @SuppressWarnings("unchecked")
    void normalizesQueryResponseWithoutChangingNamesOrDroppingUnknownIds() throws Exception {
        Element root = DocumentHelper.parseText("""
                <Response><CmdType>PresetQuery</CmdType><SN>12345</SN><SumNum>4</SumNum>
                <PresetList Num="4">
                <Item><PresetID>PresetID_5</PresetID><PresetName>PresetName_5</PresetName></Item>
                <Item><PresetID>6</PresetID><PresetName>正常预置位</PresetName></Item>
                <Item><PresetID>unknown</PresetID><PresetName>未知编号</PresetName></Item>
                <Item><PresetID>PresetID_256</PresetID><PresetName>超范围</PresetName></Item>
                </PresetList></Response>
                """).getRootElement();
        PresetQueryResponseMessageHandler handler = spy(new PresetQueryResponseMessageHandler());
        ResponseMessageHandler responses = mock(ResponseMessageHandler.class);
        ReflectionTestUtils.setField(handler, "responseMessageHandler", responses);
        RequestEvent event = mock(RequestEvent.class);
        SIPRequest request = mock(SIPRequest.class);
        when(event.getRequest()).thenReturn(request);
        Device device = new Device();
        device.setCharset("UTF-8");
        doReturn(root).when(handler).getRootElement(event, "UTF-8");
        doReturn(null).when(handler).responseAck(request, Response.OK);

        handler.handForDevice(event, device, root);

        ArgumentCaptor<Object> data = ArgumentCaptor.forClass(Object.class);
        verify(responses).handMessageEvent(eq(root), data.capture());
        List<Preset> presets = (List<Preset>) data.getValue();
        assertEquals(List.of("5", "6", "unknown", "PresetID_256"),
                presets.stream().map(Preset::getPresetId).toList());
        assertEquals("PresetName_5", presets.getFirst().getPresetName());
        assertEquals("正常预置位", presets.get(1).getPresetName());
    }
}
