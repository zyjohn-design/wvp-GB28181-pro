package com.genersoft.iot.vmp.gb28181.service;

import com.genersoft.iot.vmp.gb28181.bean.CommonGBChannel;
import com.genersoft.iot.vmp.gb28181.bean.Platform;
import com.genersoft.iot.vmp.gb28181.bean.PlatformRegisterResult;
import com.genersoft.iot.vmp.gb28181.bean.SipTransactionInfo;
import com.genersoft.iot.vmp.gb28181.event.SipSubscribe;
import com.genersoft.iot.vmp.media.bean.MediaServer;
import com.genersoft.iot.vmp.media.event.hook.HookSubscribe;
import com.genersoft.iot.vmp.service.bean.InviteTimeOutCallback;
import com.github.pagehelper.PageInfo;

import javax.sip.InvalidArgumentException;
import javax.sip.SipException;
import java.text.ParseException;
import java.util.List;

/**
 * 国标平台的业务类
 * @author lin
 */
public interface IPlatformService {

    Platform queryPlatformByServerGBId(String platformGbId);

    /**
     * 分页获取上级平台
     * @param page
     * @param count
     * @return
     */
    PageInfo<Platform> queryPlatformList(int page, int count, String query);

    /**
     * 添加级联平台
     * @param parentPlatform 级联平台
     */
    boolean add(Platform parentPlatform);

    /**
     * 立即发送一次真实的REGISTER, 用于测试上级平台的连通性与配置是否正确, 同步返回明确的结果
     * @param id 平台的数据库ID
     */
    PlatformRegisterResult testRegister(Integer id);

    /**
     * 查询最近一次注册（或测试注册）的结果
     * @param platformServerGBId 上级平台国标编号
     */
    PlatformRegisterResult getLastRegisterResult(String platformServerGBId);

    /**
     * 添加级联平台
     * @param parentPlatform 级联平台
     */
    boolean update(Platform parentPlatform);

    /**
     * 平台上线
     * @param parentPlatform 平台信息
     */
    void online(Platform parentPlatform, SipTransactionInfo sipTransactionInfo);

    /**
     * 平台离线
     * @param parentPlatform 平台信息
     */
    void offline(Platform parentPlatform);

    /**
     * 向上级平台发送位置订阅
     * @param platformId 平台
     */
    void sendNotifyMobilePosition(String platformId);

    /**
     * 向上级发送语音喊话的消息
     */
    void broadcastInvite(Platform platform, CommonGBChannel channel, String sourceId, MediaServer mediaServerItem, HookSubscribe.Event hookEvent,
                         SipSubscribe.Event errorEvent, InviteTimeOutCallback timeoutCallback) throws InvalidArgumentException, ParseException, SipException;

    /**
     * 语音喊话回复BYE
     */
    void stopBroadcast(Platform platform, CommonGBChannel channel, String app, String stream, boolean sendBye, MediaServer mediaServerItem);

    void addSimulatedSubscribeInfo(Platform parentPlatform);

    Platform queryOne(Integer platformId);

    List<Platform> queryEnablePlatformList(String serverId);

    boolean delete(Integer platformId);

    List<Platform> queryAll(String serverId);

}
