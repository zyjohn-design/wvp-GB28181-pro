package com.genersoft.iot.vmp.gb28181.service;

import com.genersoft.iot.vmp.common.InviteInfo;
import com.genersoft.iot.vmp.common.InviteSessionType;
import com.genersoft.iot.vmp.common.StreamInfo;
import com.genersoft.iot.vmp.service.bean.ErrorCallback;

import java.util.List;

/**
 * 记录国标点播的状态，包括实时预览，下载，录像回放
 */
public interface IInviteStreamService {

    /**
     * 更新点播的状态信息
     */
    void updateInviteInfo(InviteInfo inviteInfo);

    void updateInviteInfo(InviteInfo inviteInfo, Long time);

    InviteInfo updateInviteInfoForStream(InviteInfo inviteInfo, String stream);

    /**
     * 获取点播的状态信息
     */
    InviteInfo getInviteInfo(InviteSessionType type, Integer channelId, String stream);

    /**
     * 移除点播的状态信息
     */
    void removeInviteInfo(InviteSessionType type, Integer channelId, String stream);
    /**
     * 移除点播的状态信息
     */
    void removeInviteInfo(InviteInfo inviteInfo);
    /**
     * 移除点播的状态信息
     */
    void removeInviteInfoByDeviceAndChannel(InviteSessionType inviteSessionType, Integer channelId);

    List<InviteInfo> getAllInviteInfo();

    /**
     * 获取点播的状态信息
     */
    InviteInfo getInviteInfoByDeviceAndChannel(InviteSessionType type, Integer channelId);

    /**
     * 获取点播的状态信息
     */
    InviteInfo getInviteInfoByStream(InviteSessionType type, String stream);


    /**
     * 添加一个invite回调
     */
    void once(InviteSessionType type, Integer channelId, String stream,  ErrorCallback<StreamInfo> callback);

    /**
     * 原子注册invite回调并认领点播发起权
     * @return true 表示本次注册是第一个（发起者，负责发起点播），false 表示已有发起者在途，仅需等待结果
     */
    boolean onceAndFirst(InviteSessionType type, Integer channelId, String stream, ErrorCallback<StreamInfo> callback);

    /** 注册一轮点播；异步结果只能完成本轮，不能完成超时后新建的下一轮。 */
    InviteRequest register(InviteSessionType type, Integer channelId, String stream, ErrorCallback<StreamInfo> callback);

    interface InviteRequest extends ErrorCallback<StreamInfo> {
        boolean isFirst();

        boolean isPending();

        /** 收流可能早于 SIP 200 到达；本轮完成后仍允许本轮的 200 处理，直到被新一轮替换。 */
        boolean isCurrent();
    }

    /** 终止等待者；stream 为 null 时终止该通道、该类型的所有等待。不会修改 SSRC/Redis 状态。 */
    void cancel(InviteSessionType type, Integer channelId, String stream);

    /**
     * 调用一个invite回调
     */
    void call(InviteSessionType type,  Integer channelId, String stream,  int code, String msg, StreamInfo data);

    /**
     * 清空一个设备的所有invite信息
     */
    void clearInviteInfo(String deviceId);

    /**
     * 统计同一个zlm下的国标收流个数
     */
    int getStreamInfoCount(String mediaServerId);


    /**
     * 获取MediaServer下的流信息
     */
    InviteInfo getInviteInfoBySSRC(String ssrc);

    /**
     * 更新ssrc
     */
    InviteInfo updateInviteInfoForSSRC(InviteInfo inviteInfo, String ssrcInResponse);
}
