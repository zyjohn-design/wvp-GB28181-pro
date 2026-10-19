package com.genersoft.iot.vmp.service.impl;

import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.conf.exception.ControllerException;
import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.dao.CommonGBChannelMapper;
import com.genersoft.iot.vmp.gb28181.service.IGbChannelPlayService;
import com.genersoft.iot.vmp.media.bean.MediaInfo;
import com.genersoft.iot.vmp.media.bean.MediaServer;
import com.genersoft.iot.vmp.media.event.media.MediaDepartureEvent;
import com.genersoft.iot.vmp.media.service.IMediaServerService;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.bean.InviteErrorCode;
import com.genersoft.iot.vmp.service.bean.RecordPlan;
import com.genersoft.iot.vmp.service.bean.RecordPlanItem;
import com.genersoft.iot.vmp.storager.dao.RecordPlanMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordPlanServiceImplTest {
    @Mock RecordPlanMapper recordPlanMapper;
    @Mock CommonGBChannelMapper channelMapper;
    @Mock IGbChannelPlayService channelPlayService;
    @Mock IMediaServerService mediaServerService;
    @InjectMocks RecordPlanServiceImpl service;

    private RecordPlan plan() {
        RecordPlan plan = new RecordPlan();
        plan.setName("  工作日录像  ");
        plan.setId(10);
        RecordPlanItem item = new RecordPlanItem();
        item.setStart(0);
        item.setStop(1440);
        item.setWeekDay(7);
        plan.setPlanItemList(new ArrayList<>(List.of(item)));
        return plan;
    }

    private StreamInfo stream() {
        StreamInfo info = new StreamInfo();
        info.setApp("rtp");
        info.setStream("device_channel");
        MediaServer server = new MediaServer();
        server.setId("zlm-1");
        info.setMediaServer(server);
        return info;
    }

    @Test
    void rejectsMissingOrBlankNameBeforeAnyDatabaseWrite() {
        for (String name : new String[]{null, "", "  "}) {
            RecordPlan plan = plan();
            plan.setName(name);
            assertThrows(ControllerException.class, () -> service.add(plan));
            assertThrows(ControllerException.class, () -> service.update(plan));
        }
        verifyNoInteractions(recordPlanMapper);
    }

    @Test
    void rejectsInvalidPeriodsBeforeReplacingExistingItems() {
        RecordPlan plan = plan();
        RecordPlanItem item = plan.getPlanItemList().get(0);
        item.setStop(1441);
        assertThrows(ControllerException.class, () -> service.update(plan));
        item.setStop(0);
        assertThrows(ControllerException.class, () -> service.add(plan));
        item.setStop(1440);
        item.setWeekDay(0);
        assertThrows(ControllerException.class, () -> service.add(plan));
        item.setWeekDay(8);
        assertThrows(ControllerException.class, () -> service.add(plan));
        plan.setPlanItemList(null);
        assertThrows(ControllerException.class, () -> service.update(plan));
        verifyNoInteractions(recordPlanMapper);
    }

    @Test
    void savesTrimmedNameAndDefaultSnapAndRefreshesOnlyAfterCommit() {
        TransactionSynchronizationManager.initSynchronization();
        try {
            RecordPlan plan = plan();
            service.add(plan);
            assertEquals("工作日录像", plan.getName());
            assertEquals(false, plan.getSnap());
            verify(recordPlanMapper).batchAddItem(10, plan.getPlanItemList());
            verify(recordPlanMapper, never()).queryRecordIng(anyInt(), anyInt());
            TransactionSynchronizationManager.getSynchronizations().forEach(sync -> sync.afterCommit());
            verify(recordPlanMapper).queryRecordIng(anyInt(), anyInt());
        } finally {
            TransactionSynchronizationManager.clearSynchronization();
        }
    }

    @Test
    void rejectsUnknownPlanForUpdateAndAssociation() {
        assertThrows(ControllerException.class, () -> service.update(plan()));
        assertThrows(ControllerException.class, () -> service.link(List.of(1), 10));
        assertThrows(ControllerException.class, () -> service.linkAll(null));
        assertThrows(ControllerException.class, () -> service.cleanAll(null));
        verify(recordPlanMapper, never()).cleanItems(anyInt());
        verifyNoInteractions(channelMapper);
    }

    @Test
    void startsMp4OnExistingStreamAndDoesNotMutateMapperResult() {
        when(recordPlanMapper.queryRecordIng(anyInt(), anyInt())).thenReturn(List.of(1));
        CommonGBChannel channel = new CommonGBChannel();
        channel.setGbId(1);
        when(channelMapper.queryByIds(anyCollection())).thenReturn(List.of(channel));
        StreamInfo info = stream();
        when(mediaServerService.startRecord(info.getMediaServer(), info.getApp(), info.getStream())).thenReturn(true);
        doAnswer(call -> {
            ErrorCallback<StreamInfo> callback = call.getArgument(3);
            callback.run(InviteErrorCode.SUCCESS.getCode(), "成功", info);
            return null;
        }).when(channelPlayService).play(eq(channel), isNull(), eq(true), any());
        service.execution();
        service.execution();
        verify(channelPlayService, times(1)).play(eq(channel), isNull(), eq(true), any());
        verify(mediaServerService).startRecord(info.getMediaServer(), info.getApp(), info.getStream());
        assertEquals(1, service.recording(info.getApp(), info.getStream()));
    }

    @Test
    void avoidsDuplicateStartsWhilePlayCallbackIsPendingAndStopsLateResult() {
        when(recordPlanMapper.queryRecordIng(anyInt(), anyInt())).thenReturn(List.of(1));
        CommonGBChannel channel = new CommonGBChannel();
        channel.setGbId(1);
        when(channelMapper.queryByIds(anyCollection())).thenReturn(List.of(channel));
        service.execution();
        service.execution();
        ArgumentCaptor<ErrorCallback<StreamInfo>> callback = ArgumentCaptor.forClass(ErrorCallback.class);
        verify(channelPlayService).play(eq(channel), isNull(), eq(true), callback.capture());
        when(recordPlanMapper.queryRecordIng(anyInt(), anyInt())).thenReturn(List.of());
        StreamInfo info = stream();
        when(mediaServerService.stopRecord(info.getMediaServer(), info.getApp(), info.getStream())).thenReturn(true);
        callback.getValue().run(InviteErrorCode.SUCCESS.getCode(), "成功", info);
        verify(mediaServerService).stopRecord(info.getMediaServer(), info.getApp(), info.getStream());
        verify(mediaServerService, never()).startRecord(any(), anyString(), anyString());
        assertNull(service.recording(info.getApp(), info.getStream()));
    }

    @Test
    void stopsMp4ButKeepsStreamWhenThereAreViewers() {
        StreamInfo info = stream();
        service.recordStreamMap.put(1, info);
        when(mediaServerService.stopRecord(info.getMediaServer(), info.getApp(), info.getStream())).thenReturn(true);
        MediaInfo media = new MediaInfo();
        media.setReaderCount(2);
        when(mediaServerService.getMediaInfo(info.getMediaServer(), info.getApp(), info.getStream())).thenReturn(media);
        service.execution();
        verify(mediaServerService).stopRecord(info.getMediaServer(), info.getApp(), info.getStream());
        verify(mediaServerService, never()).closeStreams(any(), anyString(), anyString());
        assertTrue(service.recordStreamMap.isEmpty());
    }

    @Test
    void retriesFailedStopAndThenClosesUnwatchedStream() {
        StreamInfo info = stream();
        service.recordStreamMap.put(1, info);
        when(mediaServerService.stopRecord(info.getMediaServer(), info.getApp(), info.getStream())).thenReturn(false, true);
        MediaInfo media = new MediaInfo();
        media.setReaderCount(0);
        when(mediaServerService.getMediaInfo(info.getMediaServer(), info.getApp(), info.getStream())).thenReturn(media);
        service.execution();
        assertFalse(service.recordStreamMap.isEmpty());
        service.execution();
        verify(mediaServerService).closeStreams(info.getMediaServer(), info.getApp(), info.getStream());
        assertTrue(service.recordStreamMap.isEmpty());
    }

    @Test
    void doesNotRestartDepartedStreamAfterPlanEnded() {
        StreamInfo info = stream();
        service.recordStreamMap.put(1, info);
        MediaDepartureEvent event = new MediaDepartureEvent(this);
        event.setApp(info.getApp());
        event.setStream(info.getStream());
        service.onApplicationEvent(event);
        verifyNoInteractions(channelPlayService);
        assertTrue(service.recordStreamMap.isEmpty());
    }

    @Test
    void refreshesAfterBulkLinkAndUnlink() {
        when(recordPlanMapper.get(10)).thenReturn(plan());
        service.linkAll(10);
        service.cleanAll(10);
        verify(recordPlanMapper, times(2)).queryRecordIng(anyInt(), anyInt());
        verify(channelMapper).addRecordPlanForAll(10);
        verify(channelMapper).removeRecordPlanByPlanId(10);
    }
}
