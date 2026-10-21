package com.genersoft.iot.vmp.gb28181.service.impl;

import com.genersoft.iot.vmp.common.*;
import com.genersoft.iot.vmp.conf.UserSetting;
import com.genersoft.iot.vmp.gb28181.bean.Device;
import com.genersoft.iot.vmp.gb28181.bean.DeviceChannel;
import com.genersoft.iot.vmp.gb28181.bean.OpenRTPServerResult;
import com.genersoft.iot.vmp.gb28181.service.IDeviceChannelService;
import com.genersoft.iot.vmp.gb28181.session.SipInviteSessionManager;
import com.genersoft.iot.vmp.gb28181.transmit.cmd.ISIPCommander;
import com.genersoft.iot.vmp.media.bean.MediaServer;
import com.genersoft.iot.vmp.media.service.IMediaServerService;
import com.genersoft.iot.vmp.service.IReceiveRtpServerService;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;
import com.genersoft.iot.vmp.service.bean.SSRCInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PlayServicePendingInviteTest {
    private final PlayServiceImpl play = new PlayServiceImpl();
    private final InviteStreamServiceImplTest.TestService invites = spy(new InviteStreamServiceImplTest.TestService());
    private final IReceiveRtpServerService rtp = mock(IReceiveRtpServerService.class);
    private final IMediaServerService media = mock(IMediaServerService.class);
    private final IDeviceChannelService channels = mock(IDeviceChannelService.class);
    private final ISIPCommander commander = mock(ISIPCommander.class);
    private final Device device = new Device();
    private final DeviceChannel channel = new DeviceChannel();
    private final MediaServer server = new MediaServer();
    private final List<ErrorCallback<OpenRTPServerResult>> rtpCallbacks = new ArrayList<>();
    @SuppressWarnings("unchecked")
    private final ErrorCallback<StreamInfo> callback = mock(ErrorCallback.class);

    @BeforeEach
    void setup() {
        UserSetting settings = new UserSetting();
        settings.setServerId("local");
        settings.setPlayTimeout(30_000);
        ReflectionTestUtils.setField(invites, "userSetting", settings);
        // Keep the actual local waiting registry while stubbing external Redis I/O.
        doReturn(null).when(invites).getInviteInfo(any(), any(), nullable(String.class));
        doNothing().when(invites).updateInviteInfo(any());
        doNothing().when(invites).removeInviteInfo(any(InviteInfo.class));
        doNothing().when(invites).removeInviteInfoByDeviceAndChannel(any(), any());
        ReflectionTestUtils.setField(play, "userSetting", settings);
        ReflectionTestUtils.setField(play, "inviteStreamService", invites);
        ReflectionTestUtils.setField(play, "receiveRtpServerService", rtp);
        ReflectionTestUtils.setField(play, "mediaServerService", media);
        ReflectionTestUtils.setField(play, "deviceChannelService", channels);
        ReflectionTestUtils.setField(play, "cmder", commander);
        ReflectionTestUtils.setField(play, "sessionManager", mock(SipInviteSessionManager.class));
        device.setServerId("local");
        device.setDeviceId("42010000012005000001");
        device.setStreamMode("UDP");
        channel.setId(44796);
        channel.setDeviceId("42010600001329668360");
        server.setId("media");
        server.setSdpIp("192.0.2.1");
        when(media.getMediaServerForMinimumLoad(null)).thenReturn(server);
        when(rtp.openGbRTPServerForPlay(eq(server), eq(device), eq(channel), isNull(), anyBoolean(), any()))
                .thenAnswer(invocation -> {
                    rtpCallbacks.add(invocation.getArgument(5));
                    return new SSRCInfo(10000, "1234567890", "rtp", "device_channel");
                });
    }

    @Test
    void orphanOwnerReleasesAndActuallySendsNewInviteAfter22Minutes() throws Exception {
        play.play(device, channel, callback);
        play.play(device, channel, callback);
        verify(commander, times(1)).playStreamCmd(any(), any(), any(), any(), any(), any(), anyLong());
        invites.advance(22 * 60_000);
        ErrorCallback<StreamInfo> retry = mock(ErrorCallback.class);
        play.play(device, channel, retry);
        verify(commander, times(2)).playStreamCmd(any(), any(), any(), any(), any(), any(), anyLong());
        verify(callback, times(2)).run(eq(-2), contains("点播等待超时"), isNull());
        int removals = mockingDetails(invites).getInvocations().size();
        rtpCallbacks.get(0).run(-2, "old RTP timeout", null);
        verifyNoInteractions(retry);
        assertEquals(removals, mockingDetails(invites).getInvocations().size(), "old callback must not touch new Redis state");
        rtpCallbacks.get(1).run(-2, "new RTP timeout", null);
        verify(retry).run(-2, "new RTP timeout", null);
    }

    @Test
    void stopWithMissingRedisStateReleasesClaimAndNextPlaySendsInvite() throws Exception {
        play.play(device, channel, callback);
        play.stop(InviteSessionType.PLAY, device, channel, "device_channel");
        verify(callback).run(eq(-20), anyString(), isNull());
        play.play(device, channel, null);
        verify(commander, times(2)).playStreamCmd(any(), any(), any(), any(), any(), any(), anyLong());
        rtpCallbacks.get(0).run(-2, "old RTP timeout", null);
        assertFalse(invites.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void exceptionDuringConfirmationDoesNotLeakClaim() {
        // First lookup precedes claiming; only the second (confirmation) should fail.
        doReturn(null).doThrow(new IllegalStateException("Redis unavailable"))
                .doReturn(null).when(invites).getInviteInfoByDeviceAndChannel(InviteSessionType.PLAY, 44796);
        assertDoesNotThrow(() -> play.play(device, channel, callback));
        verify(callback).run(eq(-10), anyString(), isNull());
        assertTrue(invites.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void synchronousRtpFailureDoesNotWriteInviteOrSendSip() throws Exception {
        when(rtp.openGbRTPServerForPlay(any(), any(), any(), isNull(), anyBoolean(), any()))
                .thenAnswer(invocation -> {
                    ErrorCallback<OpenRTPServerResult> result = invocation.getArgument(5);
                    result.run(-3, "port unavailable", null);
                    return new SSRCInfo(10000, "1234567890", "rtp", "device_channel");
                });
        play.play(device, channel, callback);
        verify(callback).run(-3, "port unavailable", null);
        verify(invites, never()).updateInviteInfo(any());
        verifyNoInteractions(commander);
        assertTrue(invites.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }

    @Test
    void stopWhileWaitingClosesRtpEvenWithoutStreamInfo() {
        play.play(device, channel, callback);
        InviteInfo ready = InviteInfo.getInviteInfo(device.getDeviceId(), channel.getId(), "actual_stream",
                null, server.getId(), server.getSdpIp(), 10000, "UDP", InviteSessionType.PLAY, InviteSessionStatus.ready);
        doReturn(ready).when(invites).getInviteInfo(InviteSessionType.PLAY, 44796, "device_channel");
        play.stop(InviteSessionType.PLAY, device, channel, "device_channel");
        verify(rtp).closeRTPServerByMediaServerId("media", "rtp", "actual_stream");
        assertTrue(invites.register(InviteSessionType.PLAY, 44796, null, null).isFirst());
    }
}
